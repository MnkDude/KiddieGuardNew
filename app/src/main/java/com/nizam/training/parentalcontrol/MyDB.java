package com.nizam.training.parentalcontrol;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import java.util.ArrayList;

public class MyDB extends SQLiteOpenHelper {
    private static String DB_NAME = "MyDatabase1.db";
    private static int DB_VR = 1;
    private static String DB_TABLE = "KiddieGuard1";
    private static SQLiteDatabase db;

    MyDB(Context context) {
        super(context, DB_NAME, null, DB_VR);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DB_TABLE + "(AppName text,PackageName text primary key, HoursOfDay integer," +
                "MinutesOfDay integer,StartTimeInHours integer,StartTimeInMinutes integer," +
                "EndTimeInHours integer,EndTimeInMinutes integer,daysInWeek1 text," + "daysInWeek2 text," +
                "IsPermanent integer);");
    }
    /*void create()
    {
        String DB_TABLE = "KiddieGuard";
        dbb=getWritableDatabase();
        dbb.execSQL("insert into "+DB_TABLE+"(AppName,PackageName,HoursOfDay,MinutesOfDay,StartTimeInHours,StartTimeInMinutes" +
                "EndTimeInHours,EndTimeInMinutes,NoOfdayInWeek,IsPermanent) values(' ',' ',0,0,0,0,0,0,0,0) ");
   } */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    void insertData(String AppName, String PackageName, int HoursOfDay,
                    int MinutesOfDay, int StartTimeInHours, int StartTimeInMinutes,
                    int EndTimeInHours, int EndTimeInMinutes, int NoOfdayInWeek1, int NoOfdayInWeek2, int IsPermanent) {
        db = getWritableDatabase();
        try {
            db.execSQL("insert into " + DB_TABLE + "(AppName,PackageName,HoursOfDay,MinutesOfDay,StartTimeInHours,StartTimeInMinutes," +
                    "EndTimeInHours,EndTimeInMinutes,daysInWeek1,daysInWeek2,IsPermanent) values('" +
                    AppName + "','" + PackageName + "'," + HoursOfDay + "," + MinutesOfDay + "," + StartTimeInHours + "," + StartTimeInMinutes +
                    "," + EndTimeInHours + "," + EndTimeInMinutes + "," + NoOfdayInWeek1 + "," + NoOfdayInWeek2 + "," + IsPermanent + ");");
        } catch (SQLiteConstraintException sc) {
        }

    }

    void updateData(String PackageName, String ColName, int value) {
        db = getWritableDatabase();
        db.execSQL("update " + DB_TABLE + " set " + ColName + "=" + value + " where PackageName ='" + PackageName + "';");

    }

    void updateData(String PackageName, String ColName, String value) {
        db = getWritableDatabase();
        db.execSQL("update " + DB_TABLE + " set " + ColName + "='" + value + "' where PackageName ='" + PackageName + "';");

    }

    ArrayList<String> getPack() {
        db = getReadableDatabase();
        Cursor cr = db.rawQuery("select PackageName from " + DB_TABLE, null);
        ArrayList<String> pck = new ArrayList<>();
        int i = 0;
        while (cr.moveToNext()) {
            pck.add(cr.getString(0));
        }

        cr.close();
        return pck;
    }

    ArrayList<Integer> getIsPermenant() {
        db = getReadableDatabase();
        Cursor cr = db.rawQuery("select IsPermanent from " + DB_TABLE, null);
        ArrayList<Integer> isPerm = new ArrayList<>();
        int i = 0;
        while (cr.moveToNext()) {
            isPerm.add(cr.getInt(0));
        }

        cr.close();
        return isPerm;
    }

    int getData(String ColName, String pack_name) {
        db = getReadableDatabase();
        Cursor cr = db.rawQuery("select " + ColName + " from " + DB_TABLE + " where " + AppSettingActivity.PackageName + " = '" + pack_name + "'", null);
        int any = 0;
        while (cr.moveToNext()) {
            any = cr.getInt(0);
        }
        cr.close();

        return any;
    }

    String getWeekData(String ColName, String pack_name) {
        db = getReadableDatabase();
        Cursor cr = db.rawQuery("select " + ColName + " from " + DB_TABLE + " where " + AppSettingActivity.PackageName + " = '" + pack_name + "'", null);
        String any = "";
        while (cr.moveToNext()) {
            any = cr.getString(0);
        }
        cr.close();
        return any;
    }

    void deleteRow(String package_name) {
        db = getWritableDatabase();
        db.execSQL("delete  from " + DB_TABLE + " where " + AppSettingActivity.PackageName + " ='" + package_name + "'");
        db.close();

    }

    Bundle taskListData(String pack) {
        Bundle data = new Bundle();
        db = getReadableDatabase();
        Cursor cr = db.rawQuery("select * from " + DB_TABLE + " where  " + AppSettingActivity.PackageName + " ='" + pack + "'", null);
        while (cr.moveToNext()) {
            data.putString(AppSettingActivity.AppName, cr.getString(0));
            data.putInt(AppSettingActivity.IsPermanent, cr.getInt(10));
            data.putInt(AppSettingActivity.HoursOfDay, cr.getInt(2));
            data.putInt(AppSettingActivity.MinutesOfDay, cr.getInt(3));
            data.putInt(AppSettingActivity.StartTimeInHours, cr.getInt(4));
            data.putInt(AppSettingActivity.StartTimeInMinutes, cr.getInt(5));
            data.putInt(AppSettingActivity.EndTimeInHours, cr.getInt(6));
            data.putInt(AppSettingActivity.EndTimeInMinutes, cr.getInt(7));
        }
        cr.close();

        return data;
    }
}