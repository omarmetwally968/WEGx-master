package com.example.techvalley.wegx;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.techvalley.wegx.Tab2.myDbgas;
import static com.example.techvalley.wegx.Tab3.myDb;
import static com.example.techvalley.wegx.Tab4.myDbElec;

public class Year_tab extends Fragment implements OnChartGestureListener, OnChartValueSelectedListener {

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

    private LineChart LineChartyW;
    private LineChart LineChartyE;
    private LineChart LineChartyG;
    private TextView WT ,Wavg , ET, Eavg , Gavg, GT ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View RootView = inflater.inflate(R.layout.year_tablayout, container, false);
        LineChartyW =  RootView.findViewById(R.id.lineChart_Y_water);
        WT = RootView.findViewById(R.id.water_T);
        Wavg =RootView.findViewById(R.id.water_avg);
        LineChartyE =  RootView.findViewById(R.id.lineChart_Y_elec);
        ET = RootView.findViewById(R.id.elec_T);
        Eavg =RootView.findViewById(R.id.elec_avg);
        LineChartyG =  RootView.findViewById(R.id.lineChart_Y_gas);
        GT = RootView.findViewById(R.id.gas_T);
        Gavg =RootView.findViewById(R.id.gas_avg);

        annualWater();
        annualElec();
        annualGas();


        return RootView;
    }

    private float getSum (ArrayList<Float> q)
    { float sum=0 ;

        for(int i = 0; i < q.size(); i++)
        {
            sum= sum +q.get(i);
        }


        return sum;
    }

    private void annualWater ()
    {
        ArrayList<Float> jan= new ArrayList<>();
        ArrayList<Float> feb= new ArrayList<>();
        ArrayList<Float> mar= new ArrayList<>();
        ArrayList<Float> ap= new ArrayList<>();
        ArrayList<Float> ma= new ArrayList<>();
        ArrayList<Float> jun= new ArrayList<>();
        ArrayList<Float> jul= new ArrayList<>();
        ArrayList<Float> aug= new ArrayList<>();
        ArrayList<Float> sep= new ArrayList<>();
        ArrayList<Float> oct= new ArrayList<>();
        ArrayList<Float> nov= new ArrayList<>();
        ArrayList<Float> dec= new ArrayList<>();
        ArrayList<Entry> Y = new ArrayList<>();

        Float yy;


        Integer db_Month;
        Calendar C_year = Calendar.getInstance();
        Integer currentYear = C_year.get(Calendar.YEAR);

        Cursor data = myDb.getAllData();
        if(data.getCount() == 0){
//            Toast.makeText(getActivity(), "There are no contents in this list!",Toast.LENGTH_LONG).show();
        }else{
            while(data.moveToNext()){
                yy = data.getFloat(2); //value
                db_Month = data.getInt(3);
                db_Month--;
                switch(db_Month)
                {
                    case 1 :
                        jan.add(yy);
                    break;
                    case 2 :
                        feb.add(yy);
                        break;
                    case 3 :
                        mar.add(yy);
                        break;
                    case 4 :
                        ap.add(yy);
                        break;
                    case 5 :
                        ma.add(yy);
                        break;
                    case 6 :
                        jun.add(yy);
                        break;
                    case 7 :
                        jul.add(yy);
                        break;
                    case 8 :
                        aug.add(yy);
                        break;
                    case 9 :
                        sep.add(yy);
                        break;
                    case 10 :
                        oct.add(yy);
                        break;
                    case 11 :
                        nov.add(yy);
                        break;
                    case 12 :
                        dec.add(yy);
                        break;
                }


            }
        }

        ArrayList <Float> monthly_sum = new ArrayList<>();
        monthly_sum.add(getSum(jan));
        monthly_sum.add(getSum(feb));
        monthly_sum.add(getSum(mar));
        monthly_sum.add(getSum(ap));
        monthly_sum.add(getSum(ma));
        monthly_sum.add(getSum(jun));
        monthly_sum.add(getSum(jul));
        monthly_sum.add(getSum(aug));
        monthly_sum.add(getSum(sep));
        monthly_sum.add(getSum(oct));
        monthly_sum.add(getSum(nov));
        monthly_sum.add(getSum(dec));
        float yearUsage = 0;


        for(int i=0;i<12 ; i++ )
        {   yearUsage = yearUsage+monthly_sum.get(i);
            if(monthly_sum.get(i)!=0) {
                Y.add(new Entry(i + 1, monthly_sum.get(i)));
            }
        }
        float yearUsageAvg = yearUsage/12;

        Wavg.setText("Average water used each month = "+ yearUsageAvg +" litre");
        WT.setText("Total water used this year = "+ yearUsage +" litre");


        LineDataSet SET = new LineDataSet(Y,"Water used this year in litre");

       SET.setFillAlpha(110);
        SET.setColor(Color.GREEN);
        SET.setDrawCircles(true);
        SET.setDrawValues(true);
        SET.setValueTextSize(12);
        SET.setValueTextColor(Color.BLACK);
        Description description = new Description();
        description.setText("Water monthly usage chart for " + currentYear);
        description.setTextColor(Color.BLACK);
        description.setTextSize(12);
        ArrayList<ILineDataSet> DATASETS = new ArrayList<>();
        DATASETS.add(SET);
        LineData DA = new LineData(DATASETS);
        LineChartyW.setData(DA);
        LineChartyW.setDrawBorders(true);
//        LineChartyW.setScaleEnabled(true);
        LineChartyW.notifyDataSetChanged();
        LineChartyW.setDescription(description);
        LineChartyW.animateXY(3000,3000);
        LineChartyW.setOnChartGestureListener(Year_tab.this);
        LineChartyW.setOnChartValueSelectedListener(Year_tab.this);
        LineChartyW.setDragEnabled(true);
        LineChartyW.setPinchZoom(true);
        LineChartyW.setScaleEnabled(true);
        LineChartyW.setDrawGridBackground(false);

        XAxis xAxis = LineChartyW.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
//        xAxis.setDrawAxisLine(true);
//        xAxis.setDrawGridLines(true);
        xAxis.setAxisMaximum(13);
        xAxis.setAxisMinimum(0);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);
        final String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul" , "Aug" , "Sep", "Oct", "Nov", "Dec"};
        xAxis.setValueFormatter(new IndexAxisValueFormatter(monthNames));

        YAxis yAxis = LineChartyW.getAxisLeft();
        yAxis.setDrawZeroLine(false); // draw a zero line
        LineChartyW.invalidate();        //refresh
        yAxis.setAxisMinimum(0);
        yAxis.mDecimals=2;
        LineChartyW.getAxisRight().setEnabled(false); // no right axis

    }
    private void annualElec ()
    {
        ArrayList<Float> jan= new ArrayList<>();
        ArrayList<Float> feb= new ArrayList<>();
        ArrayList<Float> mar= new ArrayList<>();
        ArrayList<Float> ap= new ArrayList<>();
        ArrayList<Float> ma= new ArrayList<>();
        ArrayList<Float> jun= new ArrayList<>();
        ArrayList<Float> jul= new ArrayList<>();
        ArrayList<Float> aug= new ArrayList<>();
        ArrayList<Float> sep= new ArrayList<>();
        ArrayList<Float> oct= new ArrayList<>();
        ArrayList<Float> nov= new ArrayList<>();
        ArrayList<Float> dec= new ArrayList<>();
        ArrayList<Entry> Y = new ArrayList<>();

        Float yy;


        Integer db_Month;
        Calendar C_year = Calendar.getInstance();
        Integer currentYear = C_year.get(Calendar.YEAR);

        Cursor dataE = myDbElec.getAllData();
        if(dataE.getCount() == 0){
//            Toast.makeText(getActivity(), "There are no contents in this list!",Toast.LENGTH_LONG).show();
        }else{
            while(dataE.moveToNext()){
                yy = dataE.getFloat(2); //value
                db_Month = dataE.getInt(3);
                db_Month--;
                switch(db_Month)
                {
                    case 1 :
                        jan.add(yy);
                        break;
                    case 2 :
                        feb.add(yy);
                        break;
                    case 3 :
                        mar.add(yy);
                        break;
                    case 4 :
                        ap.add(yy);
                        break;
                    case 5 :
                        ma.add(yy);
                        break;
                    case 6 :
                        jun.add(yy);
                        break;
                    case 7 :
                        jul.add(yy);
                        break;
                    case 8 :
                        aug.add(yy);
                        break;
                    case 9 :
                        sep.add(yy);
                        break;
                    case 10 :
                        oct.add(yy);
                        break;
                    case 11 :
                        nov.add(yy);
                        break;
                    case 12 :
                        dec.add(yy);
                        break;
                }


            }
        }

        ArrayList <Float> monthly_sum = new ArrayList<>();
        monthly_sum.add(getSum(jan));
        monthly_sum.add(getSum(feb));
        monthly_sum.add(getSum(mar));
        monthly_sum.add(getSum(ap));
        monthly_sum.add(getSum(ma));
        monthly_sum.add(getSum(jun));
        monthly_sum.add(getSum(jul));
        monthly_sum.add(getSum(aug));
        monthly_sum.add(getSum(sep));
        monthly_sum.add(getSum(oct));
        monthly_sum.add(getSum(nov));
        monthly_sum.add(getSum(dec));
        float yearUsage = 0;


        for(int i=0;i<12 ; i++ )
        {   yearUsage = yearUsage+monthly_sum.get(i);
            if(monthly_sum.get(i)!=0) {
                Y.add(new Entry(i + 1, monthly_sum.get(i)));
            }
        }
        float yearUsageAvg = yearUsage/12;

        Eavg.setText("Average electricity used each month = "+ yearUsageAvg +" KWH");
        ET.setText("Total electricity used this year = "+ yearUsage +" KWH");


        LineDataSet SET = new LineDataSet(Y,"Electricity used this year in KWH");

        SET.setFillAlpha(110);
        SET.setColor(Color.GREEN);
        SET.setDrawCircles(true);
        SET.setDrawValues(true);
        SET.setValueTextSize(12);
        SET.setValueTextColor(Color.BLACK);
        Description description = new Description();
        description.setText("Electricity monthly usage chart for " + currentYear);
        description.setTextColor(Color.BLACK);
        description.setTextSize(12);
        ArrayList<ILineDataSet> DATASETS = new ArrayList<>();
        DATASETS.add(SET);
        LineData DA = new LineData(DATASETS);
        LineChartyE.setData(DA);
        LineChartyE.setDrawBorders(true);
//        LineChartyE.setScaleEnabled(true);
        LineChartyE.notifyDataSetChanged();
        LineChartyE.setDescription(description);
        LineChartyE.animateXY(3000,3000);
        LineChartyE.setOnChartGestureListener(Year_tab.this);
        LineChartyE.setOnChartValueSelectedListener(Year_tab.this);
        LineChartyE.setDragEnabled(true);
        LineChartyE.setPinchZoom(true);
        LineChartyE.setScaleEnabled(true);
        LineChartyE.setDrawGridBackground(false);

        XAxis xAxis = LineChartyE.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
//        xAxis.setDrawAxisLine(true);
//        xAxis.setDrawGridLines(true);
        xAxis.setAxisMaximum(13);
        xAxis.setAxisMinimum(0);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);
        final String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul" , "Aug" , "Sep", "Oct", "Nov", "Dec"};
        xAxis.setValueFormatter(new IndexAxisValueFormatter(monthNames));

        YAxis yAxis = LineChartyE.getAxisLeft();
        yAxis.setDrawZeroLine(false); // draw a zero line
        LineChartyE.invalidate();        //refresh
        yAxis.setAxisMinimum(0);
        yAxis.mDecimals=2;
        LineChartyE.getAxisRight().setEnabled(false); // no right axis

    }
    private void annualGas ()
    {
        ArrayList<Float> jan= new ArrayList<>();
        ArrayList<Float> feb= new ArrayList<>();
        ArrayList<Float> mar= new ArrayList<>();
        ArrayList<Float> ap= new ArrayList<>();
        ArrayList<Float> ma= new ArrayList<>();
        ArrayList<Float> jun= new ArrayList<>();
        ArrayList<Float> jul= new ArrayList<>();
        ArrayList<Float> aug= new ArrayList<>();
        ArrayList<Float> sep= new ArrayList<>();
        ArrayList<Float> oct= new ArrayList<>();
        ArrayList<Float> nov= new ArrayList<>();
        ArrayList<Float> dec= new ArrayList<>();
        ArrayList<Entry> Y = new ArrayList<>();

        Float yy;


        Integer db_Month;
        Calendar C_year = Calendar.getInstance();
        Integer currentYear = C_year.get(Calendar.YEAR);

        Cursor dataG = myDbgas.getAllData();
        if(dataG.getCount() == 0){
//            Toast.makeText(getActivity(), "There are no contents in this list!",Toast.LENGTH_LONG).show();
        }else{
            while(dataG.moveToNext()){
                yy = dataG.getFloat(2); //value
                db_Month = dataG.getInt(3);
                db_Month--;
                switch(db_Month)
                {
                    case 1 :
                        jan.add(yy);
                        break;
                    case 2 :
                        feb.add(yy);
                        break;
                    case 3 :
                        mar.add(yy);
                        break;
                    case 4 :
                        ap.add(yy);
                        break;
                    case 5 :
                        ma.add(yy);
                        break;
                    case 6 :
                        jun.add(yy);
                        break;
                    case 7 :
                        jul.add(yy);
                        break;
                    case 8 :
                        aug.add(yy);
                        break;
                    case 9 :
                        sep.add(yy);
                        break;
                    case 10 :
                        oct.add(yy);
                        break;
                    case 11 :
                        nov.add(yy);
                        break;
                    case 12 :
                        dec.add(yy);
                        break;
                }


            }
        }

        ArrayList <Float> monthly_sum = new ArrayList<>();
        monthly_sum.add(getSum(jan));
        monthly_sum.add(getSum(feb));
        monthly_sum.add(getSum(mar));
        monthly_sum.add(getSum(ap));
        monthly_sum.add(getSum(ma));
        monthly_sum.add(getSum(jun));
        monthly_sum.add(getSum(jul));
        monthly_sum.add(getSum(aug));
        monthly_sum.add(getSum(sep));
        monthly_sum.add(getSum(oct));
        monthly_sum.add(getSum(nov));
        monthly_sum.add(getSum(dec));
        float yearUsage = 0;


        for(int i=0;i<12 ; i++ )
        {   yearUsage = yearUsage+monthly_sum.get(i);
            if(monthly_sum.get(i)!=0) {
                Y.add(new Entry(i + 1, monthly_sum.get(i)));
            }
        }
        float yearUsageAvg = yearUsage/12;

        Gavg.setText("Average gas used each month = "+ yearUsageAvg +" m3");
        GT.setText("Total gas used this year = "+ yearUsage +" m3");


        LineDataSet SET = new LineDataSet(Y,"Gas used this year in Cubic meter");

        SET.setFillAlpha(110);
        SET.setColor(Color.GREEN);
        SET.setDrawCircles(true);
        SET.setDrawValues(true);
        SET.setValueTextSize(12);
        SET.setValueTextColor(Color.BLACK);
        Description description = new Description();
        description.setText("Gas monthly usage chart for " + currentYear);
        description.setTextColor(Color.BLACK);
        description.setTextSize(12);
        ArrayList<ILineDataSet> DATASETS = new ArrayList<>();
        DATASETS.add(SET);
        LineData DA = new LineData(DATASETS);
        LineChartyG.setData(DA);
        LineChartyG.setDrawBorders(true);
//        LineChartyW.setScaleEnabled(true);
        LineChartyG.notifyDataSetChanged();
        LineChartyG.setDescription(description);
        LineChartyG.animateXY(3000,3000);
        LineChartyG.setOnChartGestureListener(Year_tab.this);
        LineChartyG.setOnChartValueSelectedListener(Year_tab.this);
        LineChartyG.setDragEnabled(true);
        LineChartyG.setPinchZoom(true);
        LineChartyG.setScaleEnabled(true);
        LineChartyG.setDrawGridBackground(false);

        XAxis xAxis = LineChartyG.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
//        xAxis.setDrawAxisLine(true);
//        xAxis.setDrawGridLines(true);
        xAxis.setAxisMaximum(13);
        xAxis.setAxisMinimum(0);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);
        final String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul" , "Aug" , "Sep", "Oct", "Nov", "Dec"};
        xAxis.setValueFormatter(new IndexAxisValueFormatter(monthNames));

        YAxis yAxis = LineChartyG.getAxisLeft();
        yAxis.setDrawZeroLine(false); // draw a zero line
        LineChartyG.invalidate();        //refresh
        yAxis.setAxisMinimum(0);

        yAxis.mDecimals=2;
        LineChartyG.getAxisRight().setEnabled(false); // no right axis

    }

}
