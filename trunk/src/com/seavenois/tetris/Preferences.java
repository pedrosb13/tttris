package com.seavenois.tetris;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

public class Preferences extends PreferenceActivity{
	
	public static final String MUSIC_KEY = "prefSoundMusic";
	public static final String FX_KEY = "prefSoundFX";
	public static final String BG_KEY = "prefBackgrounds";
	public static final String KEEPON_KEY = "prefKeepOn";
	
	PreferenceScreen preferences;
    SharedPreferences settings;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    addPreferencesFromResource(R.xml.preferences);
		
	    final CheckBoxPreference music = (CheckBoxPreference) findPreference(MUSIC_KEY);
	    final CheckBoxPreference fx = (CheckBoxPreference) findPreference(FX_KEY);
	    final CheckBoxPreference backgrounds = (CheckBoxPreference) findPreference(BG_KEY);
	    final CheckBoxPreference keepOn = (CheckBoxPreference) findPreference(KEEPON_KEY);

	    music.setOnPreferenceChangeListener(new CheckBoxPreference.OnPreferenceChangeListener() {

            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                    final CheckBoxPreference music = (CheckBoxPreference) preference;
                    music.setChecked((Boolean) newValue);
                    saveBoolean("music", (Boolean) newValue);
                    return false;
            	}
	    });
	    
	    fx.setOnPreferenceChangeListener(new CheckBoxPreference.OnPreferenceChangeListener() {

            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                    final CheckBoxPreference fx = (CheckBoxPreference) preference;
                    fx.setChecked((Boolean) newValue);
                    saveBoolean("fx", (Boolean) newValue);
                    return false;
            	}
	    });
	    
	    backgrounds.setOnPreferenceChangeListener(new CheckBoxPreference.OnPreferenceChangeListener() {

            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                    final CheckBoxPreference backgrounds = (CheckBoxPreference) preference;
                    backgrounds.setChecked((Boolean) newValue);
                    saveBoolean("backgrounds", (Boolean) newValue);
                    return false;
            	}
	    });
	    
	    keepOn.setOnPreferenceChangeListener(new CheckBoxPreference.OnPreferenceChangeListener() {

            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                    final CheckBoxPreference keepOn = (CheckBoxPreference) preference;
                    keepOn.setChecked((Boolean) newValue);
                    saveBoolean("keepOn", (Boolean) newValue);
                    return false;
            	}
	    });
	}
	
	private void saveBoolean(final String field, final boolean value) {
        final SharedPreferences prefFile = getSharedPreferences("settings", 0);
        final SharedPreferences.Editor editor = prefFile.edit();
        editor.putBoolean(field, value);
        editor.commit();
}
}
