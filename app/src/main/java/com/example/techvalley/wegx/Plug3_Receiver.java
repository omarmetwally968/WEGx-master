package com.example.techvalley.wegx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.techvalley.wegx.Tab4.p3;

public class Plug3_Receiver extends BroadcastReceiver {
    MainActivity m = new MainActivity();
    Uri notification;
    Ringtone r;

    @Override
    public void onReceive(final Context context, Intent intent) {
//
//
        try {
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            r = RingtoneManager.getRingtone(context, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }





        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                p3.setValue(1);
                r.play();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {



                        r.stop();


                    }
                }, 8000);



                m.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText( context, "Plug no.3 is working as scheduled", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }, 1000);
    }
}
