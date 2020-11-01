package com.nizam.training.parentalcontrol;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;

public class BlockFragment extends PreferenceFragmentCompat {
    public BlockFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.activity_setting, s);

        ListPreference listPreference = (ListPreference) findPreference("lang_select");
        ListPreference listPreference1 = (ListPreference) findPreference("block_method");
        final CheckBoxPreference listPreference2 = (CheckBoxPreference) findPreference("unins_on_off");
        String val = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("lang_select", "en");
        String val1 = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("block_method", "0");
        listPreference.setSummary(listPreference.getEntries()[listPreference.findIndexOfValue(val)]);
        listPreference1.setSummary(listPreference1.getEntries()[listPreference1.findIndexOfValue(val1)]);
        listPreference2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                DevicePolicyManager policyManager = (DevicePolicyManager) getContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
                ComponentName componentName = new ComponentName("com.nizam.training.parentalcontrol", "com.nizam.training.parentalcontrol.DevAdmRec");
                SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(getContext());
                if (listPreference2.isChecked()) {
                    if (!policyManager.isAdminActive(componentName)) {
                        Intent activateDeviceAdmin = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                        activateDeviceAdmin.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                        activateDeviceAdmin.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                                "After activating admin, you will be able to block application uninstallation.");
                        startActivity(activateDeviceAdmin);
                        sf.edit().putInt("unins_close", 0).apply();
                    }
                } else {
                    if (policyManager.isAdminActive(componentName)) {
                        policyManager.removeActiveAdmin(componentName);
                        sf.edit().putInt("unins_close", 0).apply();
                    }
                }
                return false;
            }
        });
        /*listPreference2.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                return false;
            }
        });*/
    }
}
