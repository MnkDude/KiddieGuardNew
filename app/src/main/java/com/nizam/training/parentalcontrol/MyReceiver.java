package com.nizam.training.parentalcontrol;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {

    static final String TAG = "SR";

    @Override
    public void onReceive(Context context, Intent intent) {
        final AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        try {
            Intent i7 = new Intent(context, SearchRunningApp.class);
            PendingIntent ServiceManagementIntent = PendingIntent.getBroadcast(context, 11, i7, 0);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(),
                    5000, ServiceManagementIntent);
        } catch (Exception e) {
            Log.i(TAG, "Exception : " + e);
        }
    }
}