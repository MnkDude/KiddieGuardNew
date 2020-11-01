package com.nizam.training.parentalcontrol;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class SearchRunningApp extends BroadcastReceiver {

    private static final float incTime = 0.083333336f;
    final String TAG = "MNK";
    MyDB myDB;
    ArrayList<String> strings;
    ArrayList<Integer> isPerm;
    Context ct;

    @Override
    public void onReceive(Context aContext, Intent anIntent) {
        Log.i(TAG, "called");
        ct = aContext;
        try {
            myDB = new MyDB(aContext);
            ct = aContext;
            ActivityManager am = (ActivityManager) aContext
                    .getSystemService(Context.ACTIVITY_SERVICE);
            strings = myDB.getPack();
            isPerm = myDB.getIsPermenant();
            SharedPreferences sharedPreferences = aContext.getSharedPreferences("BlockAppTime2", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            int i = 0;
            String s;
            s = am.getRunningTasks(1).get(0).topActivity.flattenToShortString();
            for (String pack_name : strings) {
                if (s.contains(pack_name)) {
                    int HoursOfDay = myDB.getData(AppSettingActivity.HoursOfDay, pack_name);
                    int MinutesOfHours = myDB.getData(AppSettingActivity.MinutesOfDay, pack_name);
                    int timer = HoursOfDay * 60 + MinutesOfHours;
                    int[] intweek1 = AppSettingActivity.strToInt(myDB.getWeekData(AppSettingActivity.NoOfdayInWeek1, pack_name));
                    Calendar calendar = Calendar.getInstance();
                    if (timer != 0) {
                        for (int i1 : intweek1) {
                            if (i1 == calendar.get(Calendar.DAY_OF_WEEK)) {
                                if (pack_name.equals("wifi")) {
                                    Log.i(TAG, "called inside wifif");

                                    WifiManager wifi = (WifiManager) ct.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                                    if (wifi.isWifiEnabled()) {
                                        editor.putFloat(pack_name, sharedPreferences.getFloat(pack_name, 0) + incTime).apply();
                                        if (timer <= sharedPreferences.getFloat(pack_name, 0)) {
                                            wifi.setWifiEnabled(false);
                                        }
                                    }
                                } else if (pack_name.equals("bluetooth")) {
                                    Log.i(TAG, "called inside bluetooth");

                                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                                    if (bluetoothAdapter.isEnabled()) {
                                        editor.putFloat(pack_name, sharedPreferences.getFloat(pack_name, 0) + incTime).apply();
                                        if (timer <= sharedPreferences.getFloat(pack_name, 0)) {
                                            bluetoothAdapter.disable();
                                        }
                                    }
                                } else {
                                    Log.i(TAG, "timer" + sharedPreferences.getFloat(pack_name, 0));
                                    editor.putFloat(pack_name, sharedPreferences.getFloat(pack_name, 0) + incTime).apply();
                                    if (timer <= sharedPreferences.getFloat(pack_name, 0)) {
                                        blockApp();
                                    }
                                }
                            }
                        }
                    }
                }
                i++;
            }
            Log.i(TAG, "aTask.topActivity: " + s);
        } catch (Throwable t) {
            Log.i(TAG, "Throwable caught: "
                    + t.getMessage(), t);
        }
        blockApp();
    }

    private void blockApp() {
        String option = PreferenceManager.getDefaultSharedPreferences(ct).getString("block_method", "0");
        switch (option) {
            case "1":
                ct.startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            default:
                ct.startActivity(new Intent(ct, BlockActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
        }
    }
}
