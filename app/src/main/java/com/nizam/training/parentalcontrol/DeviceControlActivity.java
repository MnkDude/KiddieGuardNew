package com.nizam.training.parentalcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class DeviceControlActivity extends AppCompatActivity {
    ImageButton wifi, call, bth, mobData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        setTitle(R.string.devcon);
        wifi = findViewById(R.id.btWifi);
        call = findViewById(R.id.btCall);
        bth = findViewById(R.id.btBluetooth);
        mobData = findViewById(R.id.btData);
        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callIntent(getString(R.string.wifisetting), "wifi");
            }
        });
        bth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callIntent(getString(R.string.bluetoothsetting), "bluetooth");
            }
        });
        mobData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callIntent("Data Setting", "data");
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callIntent("Call Setting", "call");
            }
        });
    }

    private void callIntent(String SettingLabel, String Setting) {
        Intent intent = new Intent(getApplicationContext(), AppSettingActivity.class);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("label", SettingLabel);
        intent.putExtra("package", Setting);
        startActivity(intent);
    }
}/*if(isChecked)
                {
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
                    registerReceiver(wifiReceiver, intentFilter);
                }
                else
                {
                    unregisterReceiver(wifiReceiver);
                }*/
