package com.calintat.explorer.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.calintat.explorer.R;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}