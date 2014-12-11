package nl.rukish.mageknights;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MainMenu extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		
		setContentView(R.layout.activity_main_menu);
		showUserName();
		startGame(null);
		//showHighscores(null);
	}

	public void startGame(View view) {
		Intent intent = new Intent(MainMenu.this, GameActivity.class);
		MainMenu.this.startActivity(intent);
	}
	
	public void showHighscores(View view) {
		Intent intent = new Intent(MainMenu.this, HighscoreActivity.class);
		MainMenu.this.startActivity(intent);
	}
	
	public void openSettings(View view) {
		Intent intent = new Intent(MainMenu.this, SettingsActivity.class);
		MainMenu.this.startActivityForResult(intent, 1);
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 
        switch (requestCode) {
        case 1:
            showUserName();
            break;
 
        }
 
    }
    
    public void showUserName(){
    	SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
    	
    	String username = sharedPrefs.getString("prefUsername", "NULL");

 
        TextView userNameTextView = (TextView) findViewById(R.id.tv_username);
 
        userNameTextView.setText(username);
    }
	
	// MENU STUFF
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ( keyCode == KeyEvent.KEYCODE_MENU ) {
	        openSettings(null);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}   

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
