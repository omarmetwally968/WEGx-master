package com.example.techvalley.wegx;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import static androidx.constraintlayout.widget.Constraints.TAG;


//GAS
public class Tab2 extends Fragment implements OnChartGestureListener, OnChartValueSelectedListener {
    private Switch gswitch;
    private TextView gstatus;
    private  Gauge gaugeG;
    private DatabaseReference gswitchs;
    private DatabaseReference gas_usage_C;
    private TextView gas_usage_D;
    private Button total_RBt;
    private Button btnviewAll;
//    public static  DatabaseReference gas_daily_usage;
//    public static Double dayvaluegas;
    private TextView total_usage;
    private DatabaseReference total;
    private LineChart lineChart;
    public static DatabaseHelperGas myDbgas;
    private TextView txtTmonth;
    private TextView txtAvgmonth;

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
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View RootView = inflater.inflate(R.layout.tab2, container, false);

        myDbgas = new DatabaseHelperGas(getActivity());

        gstatus = RootView.findViewById(R.id.gas_status);
        gas_usage_D = RootView.findViewById(R.id.c_g_usage);
        gswitch =  RootView.findViewById(R.id.gas_switch);
        gaugeG =  RootView.findViewById(R.id.gaugeG);
        total_RBt = RootView.findViewById(R.id.gas_RBt);
        total_usage = RootView.findViewById(R.id.gas_total);
        txtTmonth = RootView.findViewById(R.id.txtT);
        txtAvgmonth = RootView.findViewById(R.id.txtAvg);
        lineChart =  RootView.findViewById(R.id.lineChart);
        btnviewAll = RootView.findViewById(R.id.gas_dbview);
//        gas_daily_usage = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("gasDailyUsage");
        gswitchs = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("gas valve status");
        gas_usage_C = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("gas usage");
        total = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("gas_total");

        total_RBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                total.setValue(0.0);
                Toast.makeText(getContext(),"you just reset the recorded total value..starting to record from now till next reset", Toast.LENGTH_LONG).show();



            }

        });

        total.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Double TOTAL_USAGE =Double.valueOf(dataSnapshot.getValue().toString());
                total_usage.setText("Total Gas usage:"+TOTAL_USAGE * 0.001 +"m3");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        gswitchs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer status = Integer.valueOf(dataSnapshot.getValue().toString());
                switch (status) {
                    case 0 :
                        gstatus.setText("Off");
                        gswitch.setChecked(false);
                        break;
                    case 1 :
                        gstatus.setText("On");
                        gswitch.setChecked(true);
                        break;
                    default:

                        break;


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        gswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (gswitch.isChecked()) {
                    gswitchs.setValue(1);
                    gstatus.setText("ON");
                } else {
                    gswitchs.setValue(0);
                    gstatus.setText("OFF");
                }
            }
        });

        gas_usage_C.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Float currentvalue = Float.valueOf(dataSnapshot.getValue().toString());
                String cv = String.valueOf(dataSnapshot.getValue().toString());

                gas_usage_D.setText(cv);
                gaugeG.moveToValue(currentvalue);
                gaugeG.animate();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        gas_daily_usage.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                dayvaluegas = Double.valueOf(dataSnapshot.getValue().toString())*0.001;
//
//
////                Calendar calendar = Calendar.getInstance();
////
////                               alarmManagerpW.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntentpW);
//
//
//
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

                Cursor res = myDbgas.getAllData();
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
                    buffer.append("gas usage: "+ res.getString(2)+" m3"+"\n");
//             buffer.append("Month: "+ res.getString(3)+"\n");
                }

                // Show all data
                showMessage("gas used each day",buffer.toString());
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
        Cursor data = myDbgas.getAllData();
        if(data.getCount() == 0){
            Toast.makeText(getActivity(), "There are no contents in the gas list!!!",Toast.LENGTH_LONG).show();
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
        txtAvgmonth.setText("Average gas used this each day = "+ avg +"  m3");
        txtTmonth.setText("Total gas used this month = "+ sum +"  m3");





        LineDataSet SET = new LineDataSet(Y,"gas used this month");

//       SET.setFillAlpha(110);
        SET.setColor(Color.GREEN);
        SET.setDrawCircles(true);
        SET.setDrawValues(true);
        SET.setValueTextSize(12);
        SET.setValueTextColor(Color.BLACK);
        Description description = new Description();
        description.setText("Gas daily usage chart  for " + month_name);
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
        lineChart.setOnChartGestureListener(Tab2.this);
        lineChart.setOnChartValueSelectedListener(Tab2.this);
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
        yAxis.setAxisMinimum(0);
//        yAxis.setAxisMaximum(20);
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
