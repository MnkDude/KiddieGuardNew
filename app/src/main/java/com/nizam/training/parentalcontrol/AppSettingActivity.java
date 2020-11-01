//AppSettingActivity.java
package com.nizam.training.parentalcontrol;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import static android.widget.Toast.makeText;

public class AppSettingActivity extends AppCompatActivity {

    static final String AppName = "AppName", PackageName = "PackageName", HoursOfDay = "HoursOfDay", MinutesOfDay = "MinutesOfDay", StartTimeInHours = "StartTimeInHours", StartTimeInMinutes = "StartTimeInMinutes",
            EndTimeInHours = "EndTimeInHours", EndTimeInMinutes = "EndTimeInMinutes", NoOfdayInWeek1 = "daysInWeek1", NoOfdayInWeek2 = "daysInWeek2", IsPermanent = "IsPermanent";
    ImageView imageView;
    boolean[] week1, week2, week;
    TextView textView, deActive, deActiveTime, firstTime, secondTime, deActiveBetween, andText;
    Switch aSwitch;
    String package_name;
    ToggleButton tb1, tb2;
    int hours, minutes;
    MyDB myDB;
    RelativeLayout relativeLayout;
    boolean weekSelector;

    static String timeFormat(int hourOfDay, int minute) {
        String am_pm = "AM";
        if (hourOfDay > 12) {
            hourOfDay = hourOfDay - 12;
            am_pm = "PM";
        } else if (hourOfDay == 12) {
            am_pm = "PM";
        } else if (hourOfDay == 0) {
            hourOfDay = 12;
        }
        return hourOfDay + ":" + minute + ":" + am_pm;
    }

    static String boolToStr(boolean[] bt) {
        String temp = "";
        for (Boolean b : bt) {
            if (b)
                temp = temp.concat("T");
            else
                temp = temp.concat("F");
        }
        return temp;
    }

    static int[] strToInt(String sr) {
        int[] temp = new int[7];
        for (int i = 0; i < sr.length(); i++) {
            if (sr.substring(i, i + 1).equals("T"))
                temp[i] = i + 1;
            else
                temp[i] = 0;
        }
        return temp;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setting);
        relativeLayout = findViewById(R.id.rell);
        startAnimations();
        final String[] weeks = getResources().getStringArray(R.array.weeks);
        final boolean[] checked1 = new boolean[weeks.length];
        week1 = new boolean[weeks.length];
        week2 = new boolean[weeks.length];
        week = new boolean[weeks.length];
        myDB = new MyDB(this);
        imageView = findViewById(R.id.SettingIcon);
        textView = findViewById(R.id.SettingLabel);
        andText = findViewById(R.id.andText);
        deActive = findViewById(R.id.SettingInactiveFor);
        deActiveBetween = findViewById(R.id.DeacBetween);
        deActiveTime = findViewById(R.id.DeactiveTime);
        textView.setText(getIntent().getStringExtra("label"));
        firstTime = findViewById(R.id.firstTime);
        secondTime = findViewById(R.id.secondTime);
        tb1 = findViewById(R.id.tb1);
        tb2 = findViewById(R.id.tb2);
        setTitle("Control " + getIntent().getStringExtra("label"));

        Bitmap bitmap = null;
        try {
            bitmap = getIntent().getParcelableExtra("picture");
        } catch (Exception e) {
            e.printStackTrace();
        }
        package_name = getIntent().getStringExtra("package");

        switch (package_name) {
            case "wifi":
                imageView.setImageResource(R.drawable.ic_wifi_black_24dp);
                break;
            case "bluetooth":
                imageView.setImageResource(R.drawable.ic_bluetooth_audio_black_24dp);
                break;
            default:
                imageView.setImageBitmap(bitmap);
                break;
        }
        String name = getIntent().getStringExtra("label");
        StringBuilder b;
        if (name.contains("'")) {
            b = new StringBuilder(name);
            b.deleteCharAt(b.indexOf("'"));
            name = b.toString();
        }
        myDB.insertData(name, package_name, 0, 0,
                0, 0, 0,
                0, 0, 0, 0);

        final AlertDialog alertDialog1 = createDialog(weeks, checked1);
        final AlertDialog alertDialog2 = createDialog(weeks, checked1);

        int n1 = myDB.getData(AppSettingActivity.HoursOfDay, package_name);
        int n2 = myDB.getData(AppSettingActivity.MinutesOfDay, package_name);
        if ((n1 * 60 + n2) != 0) {
            tb1.setChecked(true);
            deActiveTime.setText("" + n1 + getString(R.string.hrs) + n2 + getString(R.string.mins));
        }
        int m1 = myDB.getData(AppSettingActivity.StartTimeInHours, package_name);
        int m2 = myDB.getData(AppSettingActivity.StartTimeInMinutes, package_name);
        int m3 = myDB.getData(AppSettingActivity.EndTimeInHours, package_name);
        int m4 = myDB.getData(AppSettingActivity.EndTimeInHours, package_name);
        if ((m1 * 60 + m2) != 0 && (m2 * 60 + m2) != 0) {
            firstTime.setText(timeFormat(m1, m2));
            secondTime.setText(timeFormat(m3, m4));
        }
        //updating starting time to block app
        final TimePickerDialog timePickerDialog1 = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                firstTime.setText(timeFormat(hourOfDay, minute));
                myDB.updateData(package_name, StartTimeInHours, hourOfDay);
                myDB.updateData(package_name, StartTimeInMinutes, minute);
            }
        }, 1, 1, false);
        firstTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog1.show();
            }
        });
        //updating ending time to block app
        final TimePickerDialog timePickerDialog2 = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                secondTime.setText(timeFormat(hourOfDay, minute));
                myDB.updateData(package_name, EndTimeInHours, hourOfDay);
                myDB.updateData(package_name, EndTimeInMinutes, minute);
                alertDialog1.show();

            }
        }, 1, 1, false);
        secondTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weekSelector = false;
                timePickerDialog2.show();
            }
        });
        //selecting time to block in every day
        final TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hours = hourOfDay;
                minutes = minute;
                deActiveTime.setText("" + hourOfDay + getString(R.string.hrs) + minute + getString(R.string.mins));
                alertDialog2.show();
                myDB.updateData(package_name, HoursOfDay, hourOfDay);
                myDB.updateData(package_name, MinutesOfDay, minute);
            }
        }, 1, 1, true);
        deActiveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weekSelector = true;
                timePickerDialog.show();
            }
        });
        tb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    makeText(AppSettingActivity.this, "on", Toast.LENGTH_SHORT).show();
                    firstTime.setEnabled(false);
                    secondTime.setEnabled(false);
                    deActiveBetween.setEnabled(false);
                    andText.setEnabled(false);
                    tb2.setChecked(false);
                    deActiveTime.setEnabled(true);
                    deActive.setEnabled(true);
                    firstTime.setText(R.string.time_diff);
                    secondTime.setText(R.string.time_diff);
                    myDB.updateData(package_name, StartTimeInHours, 0);
                    myDB.updateData(package_name, StartTimeInMinutes, 0);
                    myDB.updateData(package_name, EndTimeInHours, 0);
                    myDB.updateData(package_name, EndTimeInMinutes, 0);
                } else {
                    deActive.setEnabled(false);
                    deActiveTime.setEnabled(false);
                    firstTime.setEnabled(true);
                    secondTime.setEnabled(true);
                    deActiveBetween.setEnabled(true);
                    andText.setEnabled(true);
                }
            }
        });
        tb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tb1.setChecked(false);
                    firstTime.setEnabled(true);
                    secondTime.setEnabled(true);
                    deActiveBetween.setEnabled(true);
                    andText.setEnabled(true);
                    deActive.setEnabled(false);
                    deActiveTime.setEnabled(false);
                    deActiveTime.setText(R.string._00_00);
                    myDB.updateData(package_name, HoursOfDay, 0);
                    myDB.updateData(package_name, MinutesOfDay, 0);
                } else {
                    firstTime.setEnabled(false);
                    secondTime.setEnabled(false);
                    deActiveBetween.setEnabled(false);
                    andText.setEnabled(false);
                    deActiveTime.setEnabled(true);
                    deActive.setEnabled(true);

                    myDB.updateData(package_name, StartTimeInHours, 0);
                    myDB.updateData(package_name, StartTimeInMinutes, 0);
                    myDB.updateData(package_name, EndTimeInHours, 0);
                    myDB.updateData(package_name, EndTimeInMinutes, 0);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //checkRow();
    }

    @Override
    protected void onPause() {
        super.onPause();
        checkRow();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        aSwitch = menu.findItem(R.id.q1).getActionView().findViewById(R.id.switchonoff);
        int isPer = myDB.getData(AppSettingActivity.IsPermanent, package_name);
        if (isPer == 1) {
            aSwitch.setChecked(true);
            disableAll();
        }
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    disableAll();
                    deActiveTime.setText(R.string._00_00);
                    deActiveTime.setEnabled(false);
                    deActive.setEnabled(false);
                    firstTime.setText(R.string.time_diff);
                    secondTime.setText(R.string.time_diff);
                    myDB.updateData(package_name, IsPermanent, 1);
                    myDB.updateData(package_name, HoursOfDay, 0);
                    myDB.updateData(package_name, MinutesOfDay, 0);
                    myDB.updateData(package_name, StartTimeInHours, 0);
                    myDB.updateData(package_name, StartTimeInMinutes, 0);
                    myDB.updateData(package_name, EndTimeInHours, 0);
                    myDB.updateData(package_name, EndTimeInMinutes, 0);
                    makeText(AppSettingActivity.this, getString(R.string.permmsg), Toast.LENGTH_SHORT).show();
                } else {
                    enableAll();
                    myDB.updateData(package_name, "IsPermanent", 0);
                }
            }
        });
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // checkRow();
    }

    private void disableAll() {
        deActive.setEnabled(false);
        deActiveBetween.setEnabled(false);
        deActiveTime.setEnabled(false);
        firstTime.setEnabled(false);
        secondTime.setEnabled(false);
        andText.setEnabled(false);
        tb1.setChecked(false);
        tb2.setChecked(false);
        tb1.setEnabled(false);
        tb2.setEnabled(false);
    }

    private void enableAll() {
        deActive.setEnabled(true);
        andText.setEnabled(true);
        deActiveBetween.setEnabled(true);
        deActiveTime.setEnabled(true);
        firstTime.setEnabled(true);
        secondTime.setEnabled(true);
        tb1.setEnabled(true);
        tb2.setEnabled(true);
    }

    private AlertDialog createDialog(String[] items, boolean[] checked) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.weekday));
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (weekSelector)
                    myDB.updateData(package_name, NoOfdayInWeek1, boolToStr(week1));
                else
                    myDB.updateData(package_name, NoOfdayInWeek2, boolToStr(week2));
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setMultiChoiceItems(items, checked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (weekSelector)
                    week1[which] = isChecked;
                else
                    week2[which] = isChecked;
            }
        });
        return builder.create();
    }

    void checkRow() {
        for (String s : myDB.getPack()) {
            int[] c1 = new int[]{myDB.getData(HoursOfDay, s),
                    myDB.getData(MinutesOfDay, s),
                    myDB.getData(StartTimeInHours, s),
                    myDB.getData(StartTimeInMinutes, s),
                    myDB.getData(EndTimeInHours, s),
                    myDB.getData(EndTimeInMinutes, s),
                    myDB.getData(IsPermanent, s)};
            int j = 0;
            for (int i = 0; i < c1.length; i++) {
                if (c1[i] != 0)
                    j++;
                if (j == 0 && i == 6)
                    myDB.deleteRow(s);
            }
        }
    }

    private void startAnimations() {
        Animation fadein = AnimationUtils.loadAnimation(this, R.anim.fadein);
        relativeLayout.startAnimation(fadein);
    }
}