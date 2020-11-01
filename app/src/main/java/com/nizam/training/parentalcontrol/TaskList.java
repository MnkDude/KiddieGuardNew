// TaskList.java
package com.nizam.training.parentalcontrol;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class TaskList extends AppCompatActivity {
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list);
        setTitle(R.string.tasklist);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.taskmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tm1:
                start(AppList.class);
                break;
            case R.id.tm3:
                start(AppHistoryActivity.class);
                break;
            case R.id.tm4:
                start(SettingActivity.class);
                break;
            case R.id.tm6:
                start(HelpActivity.class);
                break;
            case R.id.tm5:
                start(AboutActivity.class);
                break;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // show  all task list
        rec();
        rv = findViewById(R.id.taskrv);
        new LoadTask(rv, getApplicationContext()).execute();

    }

    void rec() {
    }

    private void start(Class appListClass) {
        Intent intent = new Intent(getApplicationContext(), appListClass);
        startActivity(intent);
    }
}

class LoadTask extends AsyncTask<Void, Void, TaskAdapter> {
    private RecyclerView rv;
    private Context ct;

    LoadTask(RecyclerView rv, Context ct) {
        super();
        this.rv = rv;
        this.ct = ct;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(TaskAdapter listAppAdapter) {
        super.onPostExecute(listAppAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ct);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setAdapter(listAppAdapter);
        rv.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected TaskAdapter doInBackground(Void... voids) {
        ArrayList<String> taskAppName, packs;
        ArrayList<Drawable> icons = new ArrayList<>();
        ArrayList<Integer> isp, hd, md, sth, stm, eth, etm;
        taskAppName = new ArrayList<>();
        isp = new ArrayList<>();
        hd = new ArrayList<>();
        md = new ArrayList<>();
        sth = new ArrayList<>();
        stm = new ArrayList<>();
        eth = new ArrayList<>();
        etm = new ArrayList<>();
        MyDB db = new MyDB(ct);
        Bundle data;
        packs = db.getPack();
        for (String s : packs) {
            data = db.taskListData(s);
            taskAppName.add(data.getString(AppSettingActivity.AppName));
            isp.add(data.getInt(AppSettingActivity.IsPermanent));
            hd.add(data.getInt(AppSettingActivity.HoursOfDay));
            md.add(data.getInt(AppSettingActivity.MinutesOfDay));
            sth.add(data.getInt(AppSettingActivity.StartTimeInHours));
            stm.add(data.getInt(AppSettingActivity.StartTimeInMinutes));
            eth.add(data.getInt(AppSettingActivity.EndTimeInHours));
            etm.add(data.getInt(AppSettingActivity.EndTimeInMinutes));
            try {
                icons.add(ct.getPackageManager().getApplicationIcon(ct.getPackageManager()
                        .getApplicationInfo(s, PackageManager.GET_META_DATA)));
            } catch (Exception e) {
                Log.i("mnk", e.getMessage());
            }
        }
        return new TaskAdapter(taskAppName, isp, hd, md, sth, stm, eth, etm, icons, packs, ct, rv);
    }
}

class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyBuilder> {

    private ArrayList<String> taskAppName, packs;
    private ArrayList<Drawable> icons;
    private Context context;
    private ArrayList<Integer> isp, hd, md, sth, stm, eth, etm;
    private RecyclerView rv;

    TaskAdapter(ArrayList<String> taskAppName, ArrayList<Integer> isp, ArrayList<Integer> hd, ArrayList<Integer> md, ArrayList<Integer> sth, ArrayList<Integer> stm, ArrayList<Integer> eth, ArrayList<Integer> etm, ArrayList<Drawable> icons, ArrayList<String> packs, Context ct, RecyclerView rv) {
        this.taskAppName = taskAppName;
        this.isp = isp;
        this.hd = hd;
        this.md = md;
        this.sth = sth;
        this.stm = stm;
        this.eth = eth;
        this.etm = etm;
        this.icons = icons;
        this.packs = packs;
        context = ct;
        this.rv = rv;
    }

    @NonNull
    @Override
    public MyBuilder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.tasklist_row, parent, false);
        return new MyBuilder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyBuilder holder, final int positions) {
        final int position = holder.getAdapterPosition();
        holder.app_name.setText(taskAppName.get(position));
        try {
            holder.icon.setImageDrawable(icons.get(position));
        } catch (Exception e) {
            Log.i("mnk", e.getMessage());
        }
        if (isp.get(position) == 1) {
            holder.desc.setText(R.string.taaskdescperm);
        } else if (hd.get(position) != 0 && md.get(position) != 0) {
            holder.desc.setText(context.getString(R.string.allowed) + hd.get(position) + "h :" + hd.get(position) + "m");
        } else {
            holder.desc.setText(context.getString(R.string.blockbet) + AppSettingActivity.timeFormat(sth.get(position), stm.get(position)) + " to " + AppSettingActivity.timeFormat(eth.get(position), etm.get(position)));
        }
        holder.desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAct(position);
            }
        });
        holder.app_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAct(position);
            }
        });
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAct(position);
            }
        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyDB(context).deleteRow(packs.get(position));
                icons.remove(position);
                taskAppName.remove(position);
                hd.remove(position);
                md.remove(position);
                sth.remove(position);
                stm.remove(position);
                etm.remove(position);
                eth.remove(position);
                packs.remove(position);
                notifyDataSetChanged();
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, taskAppName.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskAppName.size();
    }

    private void startAct(int position) {
        Bitmap bitmap;
        try {
            bitmap = getBitmapFromDrawable(icons.get(position));
            Intent intent = new Intent(context, AppSettingActivity.class);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("label", taskAppName.get(position));
            intent.putExtra("package", packs.get(position));
            intent.putExtra("picture", bitmap);
            intent.putExtra("position", position);
            context.startActivity(intent);
        } catch (Exception e) {
            //       bitmap = (Bitmap) drawables.get(position);
            Log.i("HHH", e.getMessage());
            Toast.makeText(context, "You can't open this type of apps", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

    class MyBuilder extends RecyclerView.ViewHolder {
        TextView app_name, desc;
        ImageView icon;
        ImageButton remove;


        private MyBuilder(@NonNull View itemView) {
            super(itemView);
            app_name = itemView.findViewById(R.id.taskAppName);
            desc = itemView.findViewById(R.id.taskDesc);
            icon = itemView.findViewById(R.id.taskappIcon);
            remove = itemView.findViewById(R.id.taskremove);
        }
    }

}