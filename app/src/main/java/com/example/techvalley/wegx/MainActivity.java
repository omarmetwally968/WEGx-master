package com.example.techvalley.wegx;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.techvalley.wegx.Login.firebaseAuth;
import static com.example.techvalley.wegx.Tab2.myDbgas;
import static com.example.techvalley.wegx.Tab3.myDb;
import static com.example.techvalley.wegx.Tab4.myDbElec;




public class MainActivity extends AppCompatActivity {
    public static final String CHANNEL_ID = "WEGx_ID";
    private static final String CHANNEL_NAME = "WEGx_NAME";
    private static final String CHANNEL_DESC = "WEGx_DESC";
    public static DatabaseReference gas_daily_usage;
    public static Double dayvaluegas;
    public static  DatabaseReference water_daily_usage;
    public static Double dayvalue;
    public static  DatabaseReference elec_daily_usage;
    public static Double dayvalueElec;
    public static  DatabaseReference Reset;
    public static Integer RESET;
//    private static final String DEBUG_TAG = "NetworkStatusExample";
private static final String LOG_TAG = "CheckNetworkStatus";
    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;


    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */


    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        setContentView(R.layout.activity_main);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager =  findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout =  findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);

//        ConnectivityManager connMgr =
//                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        boolean isWifiConn = false;
//        boolean isMobileConn = false;
//        for (Network network : connMgr.getAllNetworks()) {
//            NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
//            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
//                isWifiConn |= networkInfo.isConnected();
//            }
//            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
//                isMobileConn |= networkInfo.isConnected();
//            }
//        }
//        Log.d(DEBUG_TAG, "Wifi connected: " + isWifiConn);
//        Log.d(DEBUG_TAG, "Mobile connected: " + isMobileConn);

        gas_daily_usage = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("gasDailyUsage");
        gas_daily_usage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dayvaluegas = Double.valueOf(dataSnapshot.getValue().toString())*0.001;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        water_daily_usage = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("waterDailyUsage");
        water_daily_usage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dayvalue = Double.valueOf(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        elec_daily_usage= FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("elecDailyUsage");
        elec_daily_usage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dayvalueElec = Double.valueOf(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        checktime_addvalue(MainActivity.this);

        Reset = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("reset");
        Reset.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RESET = Integer.valueOf(dataSnapshot.getValue().toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
//    public boolean isOnline() {
//        ConnectivityManager connMgr = (ConnectivityManager)
//                getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//        return (networkInfo != null && networkInfo.isConnected());
//    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(receiver);

    }


    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            isNetworkAvailable(context);

        }


        private boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivity = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            if(!isConnected){

                                Toast.makeText(MainActivity.this, "you are connected to Internet ", Toast.LENGTH_LONG).show();

                                isConnected = true;
                                //do your processing here ---
                                //if you need to post any data to the server or get status
                                //update from the server
                            }
                            return true;
                        }
                    }
                }
            }
            Toast.makeText(MainActivity.this,"You are not connected to Internet! you will not be able to control the system, please reconnect to the internet", Toast.LENGTH_LONG).show();
            isConnected = false;
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Do you want to exit the app ?")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finishAffinity();

                        //moveTaskToBack(true);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            firebaseAuth.signOut();

            Login.user1=  firebaseAuth.getCurrentUser();
            if(Login.user1 == null){

                startActivity(new Intent(MainActivity.this, Login.class));
               // Toast.makeText(MainActivity.this , user1.getEmail().toString(),Toast.LENGTH_LONG).show();

            }
            return true;

        }else if (id == R.id.action_settings2)
        {
              Reset.setValue(1);
            Toast.makeText(MainActivity.this, "Resetting the system..  ", Toast.LENGTH_LONG).show();

        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).


            switch (position){
                case 0:
                    Tab1 tab1 = new Tab1();
                    return tab1;//homepage

                case 1:
                    Tab3 tab3 = new Tab3();
                    return tab3;//water
                case 2:
                    Tab4 tab4 = new Tab4();
                    return tab4;//elec
                case 3:
                    Tab2 tab2 = new Tab2 ();
                    return tab2  ;//gas
                case 4:
                    Year_tab year_tab = new Year_tab ();
                    return year_tab  ;//yearCharts
                  }
             return PlaceholderFragment.newInstance(position + 1);

        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }
    }




    public static void displayNotification(Context context, String title, String body, int ID) {


        Intent intent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, 0);

//        PendingIntent pendingIntent = PendingIntent.getActivity(
//                context,
//                100,
//                intent,
//                PendingIntent.FLAG_CANCEL_CURRENT
//
//        );
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ppp)

                        .setContentTitle(title)
                        .setColor(204-0-0)
                        .setContentText(body)
                        .setOnlyAlertOnce(true)
                        .setAutoCancel(true)
                        .setColorized(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        mBuilder.setContentIntent(pendingIntent );


        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(context);
        mNotificationMgr.notify(ID, mBuilder.build());

    }

    public static void checktime_addvalue (final Context CC) {
       final  MainActivity m = new MainActivity();
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(900);
                        m.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Calendar calendar = Calendar.getInstance();
                                DateFormat f = new SimpleDateFormat("dd/MM/yyyy");
                                int hr = calendar.get(Calendar.HOUR_OF_DAY);
                                int min = calendar.get(Calendar.MINUTE);
                                int sec = calendar.get(Calendar.SECOND);
                                Integer month = calendar.get(Calendar.MONTH);
                                month++;
                                String currentDate = f.format(calendar.getTime());

                                if (hr == 23    && min == 36 && sec == 0){

                                    Toast.makeText(CC, "saving new data  ", Toast.LENGTH_LONG).show();

                                     boolean isInsertedElec = myDbElec.insertData(currentDate,dayvalueElec,month);

                                       if(isInsertedElec==true )
                                       {
                                        elec_daily_usage.setValue(0.0);
                                       }

                                     boolean isInserted = myDb.insertData(currentDate,dayvalue,month);

                                          if(isInserted == true  )
                                          {
                                              water_daily_usage.setValue(0.0);
                                          }


                                     boolean isInsertedgas = myDbgas.insertData(currentDate,dayvaluegas,month);


                                            if( isInsertedgas==true )
                                            {
                                                gas_daily_usage.setValue(0.0);
                                            }

                                }

                            }
                        });
                    }
                } catch (InterruptedException e) {
                                                   e.printStackTrace();
                                                  }
            }
        };
        t.start();
    }


}
