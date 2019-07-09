package com.example.techvalley.wegx;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import static com.example.techvalley.wegx.MainActivity.displayNotification;

//homepage

public class Tab1 extends Fragment {

    private DatabaseReference mdatabaseref;
    private DatabaseReference power_stat;
    private DatabaseReference gas_leakage;
    private DatabaseReference water_leakage;
    private DatabaseReference wifi_mod_WG;
    private DatabaseReference wifi_mod_E;


    private TextView ausername;
    private TextView water_warning;
    private TextView elec_warning;
    private TextView gas_warning;
    private DatabaseReference leak_location;
    private TextView LEAK_LOCATION;
    private TextView mod_conWG;
    private TextView mod_conE;


    public  static Integer EE ;
    public  static Integer WG ;
    public static int gas_ID=0;
    public static int water_ID=1;
    public static int elec_ID=2;
    public static  Integer Gleak;
    public static  Integer wleak;
    public static Integer ps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.tab1, container, false); //fragment layout

        ausername =  RootView.findViewById (R.id.xusername);
        mod_conE = RootView.findViewById(R.id.conn_statE);
        mod_conWG = RootView.findViewById(R.id.conn_statWG);

        water_warning=  RootView.findViewById(R.id.waterw);
        elec_warning=  RootView.findViewById(R.id.elecw);
        gas_warning=  RootView.findViewById(R.id.gasw);
        LEAK_LOCATION = RootView.findViewById(R.id.leak_L);

        leak_location = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("water_leak_location");
        mdatabaseref = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("username");
        power_stat = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("elec_stat");
        gas_leakage = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("gasleak");
        water_leakage = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("waterleak");
        wifi_mod_WG = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("wifiWG");
        wifi_mod_E = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("wifiE");



        wifi_mod_WG.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 WG = Integer.valueOf(String.valueOf(dataSnapshot.getValue()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        wifi_mod_E.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                EE = Integer.valueOf(String.valueOf(dataSnapshot.getValue()));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(EE!=null&&WG !=null) {
            String Stat;
            Stat = check_conn(EE,WG);

            mod_conE.setText(Stat);
        }
        else {            mod_conE.setText("trying to get module status ");
        }

        mdatabaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue().toString();
                ausername.setText("Welcome Mr." + name);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        gas_leakage.addValueEventListener(new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Gleak = Integer.valueOf(dataSnapshot.getValue().toString());
                switch (Gleak)
                {
                    case 1 :

                        displayNotification(getContext(), "HOME WARNING !!!", "GAS LEAKAGE DETECTED",gas_ID);
                        gas_warning.setText("GAS LEAKAGE DETECTED PLEASE CHECK IT ");



                        break;
                    case 0:
                        gas_warning.setText("YOUR GAS SYSTEM IS OK.");
                        break;


                    default:
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }



        });
        water_leakage.addValueEventListener(new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                wleak = Integer.valueOf(dataSnapshot.getValue().toString());
                switch (wleak)
                {
                    case 1 :

                        displayNotification(getContext(), "HOME WARNING !!!", "WATER LEAKAGE DETECTED",water_ID);
                        water_warning.setText("WATER LEAKAGE DETECTED PLEASE CHECK IT ");


                        break;
                    case 0:
                        water_warning.setText("YOUR WATER SYSTEM IS OK. ");
                        break;
                    default:

                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }



        });



        leak_location.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Float LL = Float.valueOf(dataSnapshot.getValue().toString());

                if(LL==0)
                {
                    LEAK_LOCATION.setText("you have no water leak ");

                }else if (LL>0)
                {
                    LEAK_LOCATION.setText("the leak you have is at "+ LL +" meter from the module" );
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        power_stat.addValueEventListener(new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ps = Integer.valueOf(dataSnapshot.getValue().toString());
                switch (ps)
                {
                    case 0 :


                        displayNotification(getContext(), "HOME WARNING !!!", "Power is off the system is running on backup batteries",elec_ID);
                        elec_warning.setText("HOME POWER IS OFF the system is running on backup batteries ");

                        break;
                    case 1:
                        elec_warning.setText("HOME POWER IS ON ");
                        break;
                    default:

                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }



        });



        return RootView;

    }

    private String check_conn(Integer e ,Integer wg)
    {
        String S ="nothing";

        if(e==1 && wg ==1)
        {
            S = "the module is connected and working fine";

        }
        else if (e==0 && wg ==1)
          {
              S="the electricity module is not connected and may not be working properly";
              displayNotification(getContext(), "HOME WARNING !!!", "the system is not working properly",3);

          }
        else if (e==1 && wg ==0)
            {
            S ="the water and gas  module is not connected and may not be working properly";
                displayNotification(getContext(), "HOME WARNING !!!", "the system is not working properly",4);

            }
        else if(e==0 && wg ==0)
              {
                S = "the module is not  connected ";
                  displayNotification(getContext(), "HOME WARNING !!!", "the system is not working properly",5);

              }


        return S;
    }

}