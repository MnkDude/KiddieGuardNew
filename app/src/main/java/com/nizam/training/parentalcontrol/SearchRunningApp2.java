package com.nizam.training.parentalcontrol;

import android.accessibilityservice.AccessibilityService;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.util.ArrayList;
import java.util.Calendar;

public class SearchRunningApp2 extends AccessibilityService {
    String appName;
    MyDB myDB;
    ArrayList<String> strings;
    ArrayList<Integer> isPerm;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        Log.i("TopAct", am.getRunningTasks(1).get(0).topActivity.flattenToShortString());
        boolean app_state = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("app_off", false);
        if (!app_state) {
            String runningPackName = event.getPackageName().toString();
            SharedPreferences sharedPreferences = getSharedPreferences("BlockAppTime", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("curApp", runningPackName).apply();
            Calendar c1 = Calendar.getInstance();
            int curTime = c1.get(Calendar.HOUR_OF_DAY) * 60 * 60 + c1.get(Calendar.MINUTE) * 60 + c1.get(Calendar.SECOND);
            editor.putInt("curTime", curTime).apply();
            if (!PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("history_switch", false))
                history(event);
            myDB = new MyDB(getApplicationContext());
            strings = myDB.getPack();
            isPerm = myDB.getIsPermenant();
            int i = 0;
            for (String pack_name : strings) {
                if (runningPackName.contains(pack_name)) {
                    if (isPerm.get(i) == 1) {
                        blockApp();
                    } else {
                        int startHour = myDB.getData(AppSettingActivity.StartTimeInHours, pack_name);
                        int startMinute = myDB.getData(AppSettingActivity.StartTimeInMinutes, pack_name);
                        int endHour = myDB.getData(AppSettingActivity.EndTimeInHours, pack_name);
                        int endMinute = myDB.getData(AppSettingActivity.EndTimeInMinutes, pack_name);
                        int startTime = startHour * 60 + startMinute;
                        int endTime = endHour * 60 + endMinute;
                        int[] intweek2 = AppSettingActivity.strToInt(myDB.getWeekData(AppSettingActivity.NoOfdayInWeek2, pack_name));
                        Calendar calendar = Calendar.getInstance();
                        int currentTime = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);
                        if (!(startTime == 0 && endTime == 0)) {
                            for (int i1 : intweek2)
                                if (i1 == calendar.get(Calendar.DAY_OF_WEEK) && startTime <= currentTime && endTime >= currentTime)
                                    blockApp();
                        }
                    }
                    int HoursOfDay = myDB.getData(AppSettingActivity.HoursOfDay, pack_name);
                    int MinutesOfHours = myDB.getData(AppSettingActivity.MinutesOfDay, pack_name);
                    int timer = HoursOfDay * 60 * 60 + MinutesOfHours * 60;
                    if (sharedPreferences.getInt("timeUsed" + pack_name, 0) >= timer) {
                        blockApp();
                    } else {
                        try {

                        } catch (Exception e) {
                            Log.i("AlarmManager", "Exception : " + e);
                        }
                    }
                }
                if (sharedPreferences.getString("prevApp", "").equals(pack_name)) {
                    int HoursOfDay = myDB.getData(AppSettingActivity.HoursOfDay, pack_name);
                    int MinutesOfHours = myDB.getData(AppSettingActivity.MinutesOfDay, pack_name);
                    int timer = HoursOfDay * 60 * 60 + MinutesOfHours * 60;
                    int[] intweek1 = AppSettingActivity.strToInt(myDB.getWeekData(AppSettingActivity.NoOfdayInWeek1, pack_name));
                    if (timer != 0) {
                        for (int i1 : intweek1) {
                            if (i1 == Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
                                if (sharedPreferences.getString("prevApp", "").equals(pack_name)) {
                                    editor.putInt("timeUsed" + pack_name, sharedPreferences.getInt("timeUsed" + pack_name, 0) + (curTime - sharedPreferences.getInt("prevTime", 0))).apply();
                                }
                            }
                        }
                    }
                }
                i++;
            }
            editor.putString("prevApp", runningPackName).apply();
            editor.putInt("prevTime", curTime).apply();
            SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            if (am.getRunningTasks(1).get(0).topActivity.flattenToShortString().equals("com.android.settings/.DeviceAdminAdd")) {
                sf.edit().putInt("unins_close", sf.getInt("unins_close", 0) + 1).apply();
                int unins_on_off = sf.getInt("unins_close", 0);
                if (unins_on_off > 1) {
                    startActivity(new Intent(Intent.ACTION_MAIN).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addCategory(Intent.CATEGORY_HOME));
                }
            }
        }
    }

    private void history(AccessibilityEvent event) {
        SharedPreferences sf = getSharedPreferences("TempApp", MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        int count = sf.getInt("count", 0) + 1;
        appName = event.getPackageName().toString();
        String tempApp = sf.getString("appName", " ");
        if (!tempApp.equals(appName)) {
            editor.putInt("count", count).apply();
            editor.putString("appName", appName).apply();
            Calendar cl = Calendar.getInstance();
            String time, hour, minute, second;
            hour = (cl.get(Calendar.HOUR) < 10) ? "0" + cl.get(Calendar.HOUR) : "" + cl.get(Calendar.HOUR);
            minute = (cl.get(Calendar.MINUTE) < 10) ? "0" + cl.get(Calendar.MINUTE) : "" + cl.get(Calendar.MINUTE);
            second = (cl.get(Calendar.SECOND) < 10) ? "0" + cl.get(Calendar.SECOND) : "" + cl.get(Calendar.SECOND);
            time = hour + ":" + minute + ":" + second;
            time += (cl.get(Calendar.AM_PM) == 0) ? " AM" : " PM";
            int timeStart = cl.get(Calendar.HOUR_OF_DAY) * 60 * 60 + cl.get(Calendar.MINUTE) * 60 + cl.get(Calendar.SECOND);
            editor.putInt("nowTime", timeStart).apply();
            HistoryDB hDB = new HistoryDB(getApplicationContext());
            hDB.insertData(appName, count);
            hDB.update(count, appName, "StartTime", time);
            int timeUsed = sf.getInt("nowTime", 0) - sf.getInt("prevTime", 0);
            if (count == 1) count = 2;
            hDB.update(count - 1, tempApp, "EndTime", time);
            hDB.update(count - 1, tempApp, "TimeUsed", String.valueOf(timeUsed));
            editor.putInt("prevTime", timeStart).apply();
        }
    }

    private void blockApp() {
        String option = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("block_method", "0");
        switch (option) {
            case "1":
                startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            default:
                startActivity(new Intent(getApplicationContext(), BlockActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
        }
    }

    @Override
    public void onInterrupt() {

    }
}
