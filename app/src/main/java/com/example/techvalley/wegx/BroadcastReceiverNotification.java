package com.example.techvalley.wegx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import static com.example.techvalley.wegx.MainActivity.displayNotification;
import static com.example.techvalley.wegx.Tab1.Gleak;
import static com.example.techvalley.wegx.Tab1.elec_ID;
import static com.example.techvalley.wegx.Tab1.gas_ID;
import static com.example.techvalley.wegx.Tab1.ps;
import static com.example.techvalley.wegx.Tab1.water_ID;
import static com.example.techvalley.wegx.Tab1.wleak;


public class BroadcastReceiverNotification extends BroadcastReceiver {


    @Override
    public void onReceive(final Context context, Intent intent) {
             if(Gleak==1)
             {
                 displayNotification(context, "HOME WARNING !!!", "GAS LEAKAGE DETECTED", gas_ID);

             }
              if (wleak==1)

             {
                 displayNotification(context, "HOME WARNING !!!", "WATER LEAKAGE DETECTED", water_ID);

             }
             if (ps==0)

             {
                 displayNotification(context, "HOME WARNING !!!", "power is off the system is running on backup batteries", elec_ID);

             }

    }
//    public static void displayNotification(Context context, String title, String body, int ID) {
//
//
//
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
////        stackBuilder.addNextIntentWithParentStack(intent);
//        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, 0);
//
////        PendingIntent pendingIntent = PendingIntent.getActivity(
////                context,
////                100,
////                intent,
////                PendingIntent.FLAG_CANCEL_CURRENT
////
////        );
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(context, CHANNEL_ID)
//                        .setSmallIcon(R.drawable.ppp)
//                        .setContentTitle(title)
//                        .setColor(204-0-0)
//                        .setContentText(body)
//                        .setOnlyAlertOnce(true)
//                        .setAutoCancel(true)
//                        .setColorized(true)
//                        .setPriority(NotificationCompat.PRIORITY_HIGH);
//
//        mBuilder.setContentIntent(pendingIntent );
//
//
//        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(context);
//        mNotificationMgr.notify(ID, mBuilder.build());
//
//    }
}
