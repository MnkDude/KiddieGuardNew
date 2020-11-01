// AppList.java
package com.nizam.training.parentalcontrol;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class AppList extends AppCompatActivity {

    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_list);
        setTitle(R.string.applist);
        rv = findViewById(R.id.rv);
        rv.setLongClickable(true);
        rv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(AppList.this, "khgjfhdg", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setTitle(getString(R.string.listapp));
        mProgressDialog.setMessage(getString(R.string.listappdesc));
        new LoadApp(rv, getApplicationContext(), mProgressDialog).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wiblu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.wifiBut:
                Intent intent = new Intent(getApplicationContext(), AppSettingActivity.class);
                intent.putExtra("package", "wifi");
                intent.putExtra("label", "wifi");

                startActivity(intent);
                break;
            case R.id.blueBut:
                Intent intent1 = new Intent(getApplicationContext(), AppSettingActivity.class);
                intent1.putExtra("package", "bluetooth");
                intent1.putExtra("label", "bluetooth");

                startActivity(intent1);
                break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
