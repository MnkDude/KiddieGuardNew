package com.nizam.training.parentalcontrol;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    public static int i = 5;
    Bundle save;

    public static void setLanguage(Context ct, Resources res) {
        Locale myLocale = new Locale(PreferenceManager.getDefaultSharedPreferences(ct).getString("lang_select", "en"));
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        save = savedInstanceState;

        String lan = null;
        try {
            lan = getIntent().getStringExtra("lang");
            setLanguage(getApplicationContext(), getResources());
            //      setLang(lan);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle(R.string.settings);

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new BlockFragment())
                .commit();      //  ToggleButton toggleButton=findViewById(R.id.tbLang);


    }

    private void setLang(String lan) {
        Intent refresh = new Intent(this, SettingActivity.class);
        refresh.putExtra("lang", lan);
        refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(refresh);
    }
}
