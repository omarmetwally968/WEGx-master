package com.example.techvalley.wegx;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.nitri.gauge.Gauge;

import static android.content.Context.ALARM_SERVICE;
import static androidx.constraintlayout.widget.Constraints.TAG;

//electricity
public class Tab4 extends Fragment implements OnChartGestureListener, OnChartValueSelectedListener {

    private Switch main_switch;
    private TextView main_status;
    private DatabaseReference main;
    private Gauge elec_gauge;
    private TextView elecT;
    private TextView elecU;
    private DatabaseReference total;
    private DatabaseReference ElecUsage;
//    public static  DatabaseReference elec_daily_usage;
    private Button total_RBt;
    private Button btnviewAll;
//    public static Double dayvalueElec;
    public static DatabaseHelperElec myDbElec;

    private Switch p1_switch;
    private Switch p1_sch;
    private TextView p1_status;
    public static DatabaseReference p1;

    private Switch p2_switch;
    private Switch p2_sch;
    private TextView p2_status;
    public static DatabaseReference p2;

    private Switch p3_switch;
    private Switch p3_sch;
    private TextView p3_status;
    public static DatabaseReference p3;

    public static int h, m, s;
    public static int hh, mm, ss;
    public static int hhh, mmm, sss;

   private LineChart lineChart;
    private TextView txtTmonthE;
    private TextView txtAvgmonthE;


    public AlarmManager alarmManagerp1 , alarmManagerp2 , alarmManagerp3;
    PendingIntent pendingIntentp1 , pendingIntentp2 , pendingIntentp3;


    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i(TAG, "onChartGestureStarts: X:"+ me.getX()+ "Y: "+ me.getY());
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i(TAG,"onChartGestureEnd: "+lastPerformedGesture);
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i(TAG,"onChartLongPressed: ");

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i(TAG,"onChartDoubleTapped: ");

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i(TAG,"onChartSingleTapped: ");

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i(TAG, "onChartFling: veloX: "+ velocityX+ "veloY: "+ velocityY);

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i(TAG, "onChartScale: scaleX: "+ scaleX+ "scaleY: "+ scaleY);

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i(TAG,"onChartTranslate: dX: "+dX+"dY: "+dY);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i(TAG, "onValueSelected: "+e.toString());
    }

    @Override
    public void onNothingSelected() {
        Log.i(TAG, "onNothingSelected: ");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View RootView =inflater.inflate(R.layout.tab4, container, false);
        myDbElec = new DatabaseHelperElec(getActivity());
        lineChart = RootView.findViewById(R.id.lineChartElec);
        main_switch =  RootView.findViewById(R.id.main_switch);
        main_status =  RootView.findViewById(R.id.main_power_status);
        main = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("elec_stat");
        elec_gauge = RootView.findViewById(R.id.gaugeE);
        elecT =  RootView.findViewById(R.id.elec_total);
        elecU =  RootView.findViewById(R.id.c_e_usage);
        total_RBt = RootView.findViewById(R.id.elec_RBt);
        btnviewAll=RootView.findViewById(R.id.elec_dbview);

//        elec_daily_usage= FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("elecDailyUsage");
        total = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("elec_usage_T");
        ElecUsage = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("elec usage");

        p1_switch =  RootView.findViewById(R.id.plug1);
        p1_sch =     RootView.findViewById(R.id.plug11);
        p1_status=   RootView.findViewById(R.id.plug1_status);
        p1 = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("elec_plug1");

        p2_switch =  RootView.findViewById(R.id.plug2);
        p2_sch =     RootView.findViewById(R.id.plug22);
        p2_status=   RootView.findViewById(R.id.plug2_status);
        p2 = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("elec_plug2");

        p3_switch =  RootView.findViewById(R.id.plug3);
        p3_sch =     RootView.findViewById(R.id.plug33);
        p3_status=   RootView.findViewById(R.id.plug3_status);
        p3 = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("elec_plug3");

        txtTmonthE = RootView.findViewById(R.id.txtTElec);
        txtAvgmonthE = RootView.findViewById(R.id.txtAvgElec);


        alarmManagerp2 =  (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        Intent plug2 = new Intent(getActivity(), Plug2_Receiver.class);
        pendingIntentp2 = PendingIntent.getBroadcast(
                getActivity().getApplicationContext(), 2, plug2, 0);

        alarmManagerp3 =  (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        Intent plug3 = new Intent(getActivity(), Plug3_Receiver.class);
        pendingIntentp3 = PendingIntent.getBroadcast(
                getActivity().getApplicationContext(), 3, plug3, 0);




        main.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer status = Integer.valueOf(dataSnapshot.getValue().toString());
                switch (status) {
                    case 0 :
                        main_status.setText("Off");
                        main_switch.setChecked(false);
                        break;
                    case 1 :
                        main_status.setText("On");
                        main_switch.setChecked(true);
                        break;
                    default:

                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        main_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (main_switch.isChecked()) {
                    main.setValue(1);
                    main_status.setText("On");
                } else {
                    main.setValue(0);
                    main_status.setText("Off");
                }
            }
        });

        ElecUsage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Float currentvalue = Float.valueOf(dataSnapshot.getValue().toString());
                String cv = String.valueOf(dataSnapshot.getValue().toString());

                elecU.setText(cv);
                elec_gauge.moveToValue(currentvalue);
                elec_gauge.animate();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        total.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Float TOTAL_USAGE = Float.valueOf(dataSnapshot.getValue().toString());
                elecT.setText("Total electricity usage:"+TOTAL_USAGE + "KW" );


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        total_RBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                total.setValue(0.0);
                Toast.makeText(getContext(),"you just reset the recorded total value..starting to record from now till next reset", Toast.LENGTH_LONG).show();



            }

        });



        p1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer status = Integer.valueOf(dataSnapshot.getValue().toString());
                switch (status) {
                    case 0 :
                        p1_status.setText("Off");
                        p1_switch.setChecked(false);
                        p1_sch.setChecked(false);
                        break;
                    case 1 :
                        p1_status.setText("On");
                        p1_switch.setChecked(true);
                        break;
                    default:

                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        p1_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (p1_switch.isChecked()) {
                    p1.setValue(1);
                    p1_status.setText("On");
                } else {
                    p1.setValue(0);
                    p1_status.setText("Off");
                }
            }
        });

        alarmManagerp1 =  (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        Intent plug1 = new Intent(getActivity(), Plug1_Receiver.class);
        pendingIntentp1 = PendingIntent.getBroadcast(
                getActivity().getApplicationContext(), 1, plug1, 0);

        p1_sch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (p1_sch.isChecked()) {

                    final Calendar calendar = Calendar.getInstance();
                    final Calendar current = Calendar.getInstance();



                    TimePickerDialog d = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                            if (android.os.Build.VERSION.SDK_INT >= 23) {
                                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                        view.getHour(), view.getMinute(), 0);
                            } else {
                                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                        view.getCurrentHour(), view.getCurrentMinute(), 0);
                            }


                            alarmManagerp1.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntentp1);


                            Long remTimeSecs = calendar.getTimeInMillis() - current.getTimeInMillis();
                            h = (int) ((remTimeSecs / 1000) / 3600);
                            m = (int) (((remTimeSecs / 1000) / 60) % 60);
                            s = (int) ((remTimeSecs / 1000) % 60);
                            Toast.makeText(getActivity(),"Plug no.1 Alarm set for : " + h + " Hours " + m + " Mins " + s + " Secs",Toast.LENGTH_LONG).show();



                        }
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                    d.setCanceledOnTouchOutside(false);
                    d.setOnCancelListener(new DialogInterface.OnCancelListener() {

                        @Override
                        public void onCancel(DialogInterface dialog) {
                            p1_sch.setChecked(false);
                            alarmManagerp1.cancel(pendingIntentp1);


                        }
                    });
                    d.show();





                } else {

                }
            }
        });

        p2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer status = Integer.valueOf(dataSnapshot.getValue().toString());
                switch (status) {
                    case 0 :
                        p2_status.setText("Off");
                        p2_switch.setChecked(false);
                        p2_sch.setChecked(false);
                        break;
                    case 1 :
                        p2_status.setText("On");
                        p2_switch.setChecked(true);
                        break;
                    default:

                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        p2_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (p2_switch.isChecked()) {
                    p2.setValue(1);
                    p2_status.setText("On");
                } else {
                    p2.setValue(0);
                    p2_status.setText("Off");
                }
            }
        });

        p2_sch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (p2_sch.isChecked()) {

                    final Calendar calendar = Calendar.getInstance();
                    final Calendar current = Calendar.getInstance();



                    TimePickerDialog d1 = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                            if (android.os.Build.VERSION.SDK_INT >= 23) {
                                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                        view.getHour(), view.getMinute(), 0);
                            } else {
                                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                        view.getCurrentHour(), view.getCurrentMinute(), 0);
                            }


                            alarmManagerp2.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntentp2);


                            Long remTimeSecs = calendar.getTimeInMillis() - current.getTimeInMillis();
                            hh = (int) ((remTimeSecs / 1000) / 3600);
                            mm = (int) (((remTimeSecs / 1000) / 60) % 60);
                            ss = (int) ((remTimeSecs / 1000) % 60);
                            Toast.makeText(getActivity(),"Plug no.2 Alarm set for : " + hh + " Hours " + mm + " Mins " + ss + " Secs",Toast.LENGTH_LONG).show();
//                        switchButton.setChecked(true);
//                        setTextOn();


                        }
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                    d1.setCanceledOnTouchOutside(false);
                    d1.setOnCancelListener(new DialogInterface.OnCancelListener() {

                        @Override
                        public void onCancel(DialogInterface dialog) {
                             p2_sch.setChecked(false);
                            alarmManagerp2.cancel(pendingIntentp2);
                        }
                    });
                    d1.show();







                } else {

                }
            }
        });




        p3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer status = Integer.valueOf(dataSnapshot.getValue().toString());
                switch (status) {
                    case 0 :
                        p3_status.setText("Off");
                        p3_switch.setChecked(false);
                        p3_sch.setChecked(false);
                        break;
                    case 1 :
                        p3_status.setText("On");
                        p3_switch.setChecked(true);
                        break;
                    default:

                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        p3_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (p3_switch.isChecked()) {
                    p3.setValue(1);
                    p3_status.setText("On");
                } else {
                    p3.setValue(0);
                    p3_status.setText("Off");
                }
            }
        });

        p3_sch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (p3_sch.isChecked()) {

                    final Calendar calendar = Calendar.getInstance();
                    final Calendar current = Calendar.getInstance();



                    TimePickerDialog d1 = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                            if (android.os.Build.VERSION.SDK_INT >= 23) {
                                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                        view.getHour(), view.getMinute(), 0);
                            } else {
                                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                        view.getCurrentHour(), view.getCurrentMinute(), 0);
                            }


                            alarmManagerp3.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntentp3);


                            Long remTimeSecs = calendar.getTimeInMillis() - current.getTimeInMillis();
                            hhh = (int) ((remTimeSecs / 1000) / 3600);
                            mmm = (int) (((remTimeSecs / 1000) / 60) % 60);
                            sss = (int) ((remTimeSecs / 1000) % 60);
                            Toast.makeText(getActivity(),"Plug no.3 Alarm set for : " + hhh + " Hours " + mmm + " Mins " + sss + " Secs",Toast.LENGTH_LONG).show();



                        }
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                    d1.setCanceledOnTouchOutside(false);
                    d1.setOnCancelListener(new DialogInterface.OnCancelListener() {

                        @Override
                        public void onCancel(DialogInterface dialog) {
                            p3_sch.setChecked(false);
                            alarmManagerp3.cancel(pendingIntentp3);
                        }
                    });
                    d1.show();
                }
            }
        });

//        elec_daily_usage.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                dayvalueElec = Double.valueOf(dataSnapshot.getValue().toString());
//                MainActivity.checktime_addvalue (getActivity());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


        btnviewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor res = myDbElec.getAllData();
                if(res.getCount() == 0) {
                    // show message
                    showMessage("Error","Nothing found");
                    return;
                }

                if (res == null)
                {
                    showMessage("Error","Nothing found");

                }

                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
//              buffer.append("Id :"+ res.getString(0)+"\n");
                    buffer.append("Date: "+ res.getString(1)+"\n");
                    buffer.append("Electricity usage: "+ res.getString(2)+" KWatt"+"\n");
//             buffer.append("Month: "+ res.getString(3)+"\n");
                }

                // Show all data
                showMessage("Electricity used each day",buffer.toString());
            }




        });




        //populate an ArrayList<String> from the database and then view it
        ArrayList<Entry> Y = new ArrayList<>();
        ArrayList<Float> YYY = new ArrayList<>();
        Float yy;
        String xx;
        Integer db_Month;
        Calendar C_month = Calendar.getInstance();
        Integer currentMonth = C_month.get(Calendar.MONTH);
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(C_month.getTime());
        currentMonth++;
        Cursor data = myDbElec.getAllData();
        if(data.getCount() == 0){
            Toast.makeText(getActivity(), "There are no contents in this electricity list!",Toast.LENGTH_LONG).show();
        }else{
            while(data.moveToNext()){
                yy = data.getFloat(2);
                xx = data.getString(1);
                db_Month = data.getInt(3);
                String [] dateParts = xx.split("/");
                String day = dateParts[0];
                Float xxx = Float.valueOf(day.trim()).floatValue();
                if (currentMonth == db_Month )
                {
                    Y.add(new Entry(xxx,yy));
                    YYY.add(yy);
                }



            }
        }

        float sum = 0 ;
        float avg = 0;
        for(int i = 0; i < YYY.size(); i++)
        {
            sum=sum+YYY.get(i);
            avg= sum/30;
        }
        txtAvgmonthE.setText("Average power used each day = "+ avg +" KWatt");
        txtTmonthE.setText("Total power used this month = "+ sum +" KWatt");





        LineDataSet SET = new LineDataSet(Y,"Electricity used this month");

//       SET.setFillAlpha(110);
        SET.setColor(Color.GREEN);
        SET.setDrawCircles(true);
        SET.setDrawValues(true);
        SET.setValueTextSize(12);
        SET.setValueTextColor(Color.BLACK);
        Description description = new Description();
        description.setText("Power daily usage chart for " + month_name);
        description.setTextColor(Color.BLACK);
        description.setTextSize(12);
        ArrayList<ILineDataSet> DATASETS = new ArrayList<>();
        DATASETS.add(SET);
        LineData DA = new LineData(DATASETS);
        lineChart.setData(DA);
        lineChart.setDrawBorders(true);
        lineChart.setScaleEnabled(true);
        lineChart.notifyDataSetChanged();
        lineChart.setDescription(description);
        lineChart.animateXY(4000,4000);
        lineChart.setOnChartGestureListener(Tab4.this);
        lineChart.setOnChartValueSelectedListener(Tab4.this);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
//        lineChart.setBackground();
        lineChart.setPinchZoom(true);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);
        xAxis.setAxisMaximum(31);
        xAxis.setAxisMinimum(0);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setDrawZeroLine(true); // draw a zero line
//        yAxis.setAxisMaximum(60);
        yAxis.setAxisMinimum(0);
        lineChart.getAxisRight().setEnabled(false); // no right axis

        return RootView;
    }
    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

}
