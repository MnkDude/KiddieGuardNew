package com.nizam.training.parentalcontrol;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

import java.util.ArrayList;

public class HistoryDB extends SQLiteOpenHelper {
    private static String DB_NAME = "History.db";
    private static int DB_VR = 1;
    private static String DB_TABLE = "HistoryData";
    private static SQLiteDatabase dbb;
    private Context context;

    HistoryDB(Context context) {
        super(context, DB_NAME, null, DB_VR);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table  " + DB_TABLE + "(SlNo Integer,AppName text,StartTime text ,EndTime text,TimeUsed Integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    void insertData(String appName, int slNo) {
        dbb = getWritableDatabase();
        dbb.execSQL("insert into " + DB_TABLE + "(SlNo,AppName,StartTime,EndTime,TimeUsed) values(" + slNo + ",'" + appName + "',' ','                  ',0);");
        dbb.close();
    }

    void update(int slNo, String appName, String colName, String time) {
        dbb = getWritableDatabase();
        dbb.execSQL("update " + DB_TABLE + " set " + colName + "='" + time + "' where AppName='" + appName + "' And SlNo=" + slNo);
        dbb.close();
    }

    ArrayList<String> getData(String colName, boolean needOne) {
        SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(context);
        dbb = getReadableDatabase();
        Cursor cr;
        ArrayList<String> arrayList = new ArrayList<>();
        if (!needOne) {
            cr = dbb.rawQuery("select AppName," + colName + ",SlNo from " + DB_TABLE, null);
            while (cr.moveToNext()) {
                if (!cr.getString(0).equals(sf.getString("home", "l")))//&&cr.getInt(2)>3)
                {
                    arrayList.add(0, cr.getString(1));
                }
            }
        } else {
            cr = dbb.rawQuery("select " + colName + ",SlNo from " + DB_TABLE, null);
            while (cr.moveToNext()) {
                if (!cr.getString(0).equals(sf.getString("home", "l")))//&&cr.getInt(1)>3)
                {
                    arrayList.add(0, cr.getString(0));
                }
            }
        }
        dbb.close();
        cr.close();
        return arrayList;
    }

    void deleteAll() {
        dbb = getWritableDatabase();
        dbb.execSQL("delete from " + DB_TABLE);
        dbb.close();
    }
}
