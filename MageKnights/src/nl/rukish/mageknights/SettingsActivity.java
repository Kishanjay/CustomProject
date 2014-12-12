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
import android.widget.Toast;

import java.util.List;

public class SettingsActivity extends PreferenceActivity {
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        addPreferencesFromResource(R.xml.preferences);

        
        Preference button = (Preference)findPreference("prefReset");
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference arg0) {
                        	resetScore(); 
                        	Toast toast = Toast.makeText(getApplicationContext(), "All highscores deleted!", Toast.LENGTH_LONG);
                            toast.show();
                            return true;
                        }
                    });
    }
    
    public void resetScore(){
    	DBHelper mydb = new DBHelper(this);
    	mydb.clearDatabase();
    }
    
    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Media.menu.pause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!Media.menu.isPlaying()){
			Media.menu.start();
		}
	}
}