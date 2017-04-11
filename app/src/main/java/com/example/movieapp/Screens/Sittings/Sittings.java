package com.example.movieapp.Screens.Sittings;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.os.Bundle;

import com.example.movieapp.R;

public class Sittings extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    private String current_Cate;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.list_preference);
        bind(findPreference("MovieKey"));
        onPreferenceChange(findPreference("MovieKey"), current_Cate);
    }

    private void bind(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
        onPreferenceChange(preference,
                PreferenceManager.getDefaultSharedPreferences(preference.getContext()
                ).getString(preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        String ClickedItem = newValue.toString();

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(ClickedItem);
            if (index >= 0) {
                preference.setSummary(listPreference.getEntries()[index]);
            }
            if (!ClickedItem.equals(current_Cate)) {
                editor = getSharedPreferences("MyPre", MODE_PRIVATE).edit();
                editor.putString("Category", ClickedItem);
                editor.putBoolean("Changed", true);
                editor.commit();
            }
        } else {
            preference.setSummary(ClickedItem);
        }
        current_Cate = ClickedItem;

        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

}
