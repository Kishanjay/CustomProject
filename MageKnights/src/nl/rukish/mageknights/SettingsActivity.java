package nl.rukish.mageknights;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import java.util.List;

public class SettingsActivity extends PreferenceActivity {
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        addPreferencesFromResource(R.xml.preferences);
        Preference myPref = (Preference) findPreference("prefReset");
        myPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                     public boolean onPreferenceClick(Preference preference) {
						return false;
                         //open browser or intent here
                     }
                 });
    }
    
    public void resetScore(){
    	DBHelper mydb = new DBHelper(this);
    	mydb.clearDatabase();
    }
}