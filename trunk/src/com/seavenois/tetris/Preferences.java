package com.seavenois.tetris;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

/*************************************************/
/* Preferences screen activity *******************/
/*************************************************/
public class Preferences extends PreferenceActivity{
	
	//Names the preferences are stored with
	public static final String MUSIC_KEY = "prefSoundMusic";
	public static final String FX_KEY = "prefSoundFX";
	public static final String VIB_KEY = "prefVibration";
	public static final String BG_KEY = "prefBackgrounds";
	public static final String ABG_KEY = "prefAboutBg";
	public static final String KEEPON_KEY = "prefKeepOn";
	
	PreferenceScreen preferences;
    SharedPreferences settings;
	
    /*************************************************/
	/* On create *************************************/
	/*************************************************/
	/* Sets the layout and the ui elements and *******/
	/* defines listener to change preferences. *******/
	/*************************************************/
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    addPreferencesFromResource(R.xml.preferences);
		
	    final CheckBoxPreference music = (CheckBoxPreference) findPreference(MUSIC_KEY);
	    final CheckBoxPreference fx = (CheckBoxPreference) findPreference(FX_KEY);
	    final CheckBoxPreference vib = (CheckBoxPreference) findPreference(VIB_KEY);
	    final CheckBoxPreference backgrounds = (CheckBoxPreference) findPreference(BG_KEY);
	    final Preference aBackgrounds = (Preference) findPreference(ABG_KEY);
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
	    
	    vib.setOnPreferenceChangeListener(new CheckBoxPreference.OnPreferenceChangeListener() {

            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                    final CheckBoxPreference vib = (CheckBoxPreference) preference;
                    vib.setChecked((Boolean) newValue);
                    saveBoolean("vib", (Boolean) newValue);
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
	    aBackgrounds.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
            	Intent intent = new Intent();
            	intent.setComponent(new ComponentName("com.seavenois.tetris", "com.seavenois.tetris.AboutBackgrounds"));
            	startActivity(intent);
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
	
	/*************************************************/
	/* Saves the value "value" for the preference ****/
	/* "field". **************************************/
	/*************************************************/
	private void saveBoolean(final String field, final boolean value) {
        final SharedPreferences prefFile = getSharedPreferences("settings", 0);
        final SharedPreferences.Editor editor = prefFile.edit();
        editor.putBoolean(field, value);
        editor.commit();
	}
}
