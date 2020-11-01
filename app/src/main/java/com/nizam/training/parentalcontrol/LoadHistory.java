package com.nizam.training.parentalcontrol;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class LoadHistory extends AsyncTask<Void, Integer, HistoryAdapter> {
    private ProgressBar progressBar;
    private RecyclerView rv;
    private Context ct;

    LoadHistory(RecyclerView rv, Context ct, ProgressBar progressBar) {
        super();
        this.progressBar = progressBar;
        this.rv = rv;
        this.ct = ct;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(HistoryAdapter listAppAdapter) {
        super.onPostExecute(listAppAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ct);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setAdapter(listAppAdapter);
        rv.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0]);
    }

    @Override
    protected HistoryAdapter doInBackground(Void... voids) {
        ArrayList<String> app_name, start_time, end_time, timeUsed;
        HistoryDB hDB = new HistoryDB(ct);
        app_name = hDB.getData("AppName", true);
        start_time = hDB.getData("StartTime", false);
        end_time = hDB.getData("EndTime", false);
        timeUsed = hDB.getData("TimeUsed", false);
        return new HistoryAdapter(app_name, start_time, end_time, timeUsed, ct);
    }
}