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


//water
public class Tab3 extends Fragment implements OnChartGestureListener, OnChartValueSelectedListener {

    private Switch wswitch;
    private TextView txtTmonth;
    private TextView txtAvgmonth;
    private TextView wstatus;
    private TextView water_current_usage_D;
    private Button total_RBt;
    private Button btnviewAll;
    private TextView total_usage;
    private Gauge water_gauge;
    private DatabaseReference total;
    private DatabaseReference wswitchs;
    private DatabaseReference water_current_usage;
//    public static  DatabaseReference water_daily_usage;
//    public static Double dayvalue;

   private LineChart lineChart;

//    PendingIntent pendingIntentpW;
//    AlarmManager alarmManagerpW;

   public static DatabaseHelperWater myDb;

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

        View RootView = inflater.inflate(R.layout.tab3, container, false);
        wstatus = RootView.findViewById(R.id.water_status);
        wswitch = RootView.findViewById(R.id.waterS);
        water_gauge = RootView.findViewById(R.id.gaugeW);
        water_current_usage_D = RootView.findViewById(R.id.c_w_usage);
        total_RBt = RootView.findViewById(R.id.water_RBt);
        total_usage = RootView.findViewById(R.id.water_total);
        btnviewAll = RootView.findViewById(R.id.water_dbview);
        lineChart =  RootView.findViewById(R.id.lineChart);
       txtTmonth = RootView.findViewById(R.id.txtT);
        txtAvgmonth = RootView.findViewById(R.id.txtAvg);


        myDb = new DatabaseHelperWater(getActivity());

        wswitchs = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("water valve status");
        water_current_usage = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("water usage");
        total = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("water_total");
//        water_daily_usage = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("waterDailyUsage");







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
                Float TOTAL_USAGE = Float.valueOf(dataSnapshot.getValue().toString());
                total_usage.setText("Total water usage:"+TOTAL_USAGE + " L" );


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        wswitchs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer status = Integer.valueOf(dataSnapshot.getValue().toString());
                switch (status) {
                    case 0 :
                        wstatus.setText("Off");
                        wswitch.setChecked(false);
                        break;
                    case 1 :
                        wstatus.setText("On");
                        wswitch.setChecked(true);
                        break;
                    default:

                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        wswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (wswitch.isChecked()) {
                    wswitchs.setValue(1);
                    wstatus.setText("On");
                } else {
                    wswitchs.setValue(0);
                    wstatus.setText("Off");
                }
            }
        });


        water_current_usage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Float currentvalue = Float.valueOf(dataSnapshot.getValue().toString());
                String cv =dataSnapshot.getValue().toString();

                water_current_usage_D.setText(cv);
                water_gauge.moveToValue(currentvalue);
                water_gauge.animate();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        water_daily_usage.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                dayvalue = Double.valueOf(dataSnapshot.getValue().toString());
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

            Cursor res = myDb.getAllData();
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
             buffer.append("Water usage: "+ res.getString(2)+" Litre"+"\n");
//             buffer.append("Month: "+ res.getString(3)+"\n");
                        }

            // Show all data
             showMessage("Water used each day",buffer.toString());
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
        Cursor data = myDb.getAllData();
        if(data.getCount() == 0){
            Toast.makeText(getActivity(), "There are no contents in the water list!",Toast.LENGTH_LONG).show();
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
        txtAvgmonth.setText("Average water used each day = "+ avg +" litre");
        txtTmonth.setText("Total water used this month = "+ sum +" litre");





        LineDataSet SET = new LineDataSet(Y,"Water used this month");

//       SET.setFillAlpha(110);
       SET.setColor(Color.GREEN);
       SET.setDrawCircles(true);
       SET.setDrawValues(true);
       SET.setValueTextSize(12);
       SET.setValueTextColor(Color.BLACK);
       Description description = new Description();
        description.setText("Water daily usage chart for " + month_name);
        description.setTextColor(Color.BLACK);
        description.setTextSize(12);
       ArrayList<ILineDataSet> DATASETS = new ArrayList<>();
       DATASETS.add(SET);
       LineData DA = new LineData(DATASETS);
       lineChart.setData(DA);
       lineChart.setDrawBorders(true);
       lineChart.notifyDataSetChanged();
        lineChart.setDescription(description);
        lineChart.animateXY(4000,4000);
       lineChart.setOnChartGestureListener(Tab3.this);
       lineChart.setOnChartValueSelectedListener(Tab3.this);
        lineChart.setDragEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setScaleEnabled(true);

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








        return  RootView;
    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }






}
