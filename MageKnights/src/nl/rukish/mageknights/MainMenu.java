package nl.rukish.mageknights;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


public class MainMenu extends ActionBarActivity {
	
	public static Media media;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		media = new Media(MainMenu.this);
		
		setContentView(R.layout.activity_main_menu);
		showUserName();
		
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//media.menu.stop();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!Media.menu.isPlaying()){
			Media.menu.start();
		}
	}

	public void startGame(View view) {
		media.menu.pause();
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
        userNameTextView.setText("Welcome " + username);
    }
	
	//change default menu button
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ( keyCode == KeyEvent.KEYCODE_MENU ) {
	        openSettings(null);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}   
}
