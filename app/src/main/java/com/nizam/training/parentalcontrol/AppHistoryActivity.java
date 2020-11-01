package com.nizam.training.parentalcontrol;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class AppHistoryActivity extends AppCompatActivity {

    ProgressBar mProgressDialog;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_history);
        setTitle(R.string.app_history);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mProgressDialog = findViewById(R.id.pb);
        mProgressDialog.setIndeterminate(false);
        rv = findViewById(R.id.historyTable);
        new LoadHistory(rv, getApplicationContext(), mProgressDialog).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.delete, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.deleteBut) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.deletemsg));
            builder.setTitle(getString(R.string.deletetitle));
            builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new HistoryDB(getApplicationContext()).deleteAll();
                    onResume();
                }
            });
            builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        return true;
    }
}

class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyBuilder> {

    private ArrayList<String> appnames, startTimes, endTimes, timeUsed;
    private Context context;

    HistoryAdapter(ArrayList<String> appnames, ArrayList<String> startTimes, ArrayList<String> endTimes, ArrayList<String> timeUsed, Context context) {
        this.appnames = appnames;
        this.startTimes = startTimes;
        this.endTimes = endTimes;
        this.timeUsed = timeUsed;
        this.context = context;
    }

    @NonNull
    @Override
    public MyBuilder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.history_view_row, parent, false);
        return new MyBuilder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyBuilder holder, final int positions) {
        final int position = holder.getAdapterPosition();
        String appName = "";
        try {
            appName = context.getPackageManager().getApplicationLabel(context.getPackageManager().getApplicationInfo(appnames.get(position), PackageManager.GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        holder.app_name.setText(appName);
        holder.start_time.setText(startTimes.get(position));
        holder.end_time.setText(endTimes.get(position));
        int tu = Integer.parseInt(timeUsed.get(position));
        //time used calculation
        if (tu < 60) {
            holder.timeUsed.setText(tu + context.getString(R.string.sec));
        } else if (tu < 3600) {
            holder.timeUsed.setText(tu / 60 + context.getString(R.string.mm) + tu % 60 + context.getString(R.string.ss));
        } else {
            holder.timeUsed.setText(tu / 60 / 60 + context.getString(R.string.h) + (tu % 3600) + context.getString(R.string.mm) + tu % 60 + context.getString(R.string.ss));
        }
    }

    @Override
    public int getItemCount() {
        return appnames.size();
    }

    class MyBuilder extends RecyclerView.ViewHolder {
        TextView app_name, start_time, end_time, timeUsed;

        private MyBuilder(@NonNull View itemView) {
            super(itemView);
            app_name = itemView.findViewById(R.id.historyAppName);
            start_time = itemView.findViewById(R.id.historyStartTime);
            end_time = itemView.findViewById(R.id.historyEndTime);
            timeUsed = itemView.findViewById(R.id.timeUsed);
        }
    }
}