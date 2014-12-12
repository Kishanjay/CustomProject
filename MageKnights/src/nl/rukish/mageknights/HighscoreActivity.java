package nl.rukish.mageknights;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.util.Log;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class HighscoreActivity extends ActionBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//full screen landscape
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_highscore);
		
		//load up database
		DBHelper mydb = new DBHelper(this);
	    Cursor result = mydb.getData();
	    result.moveToFirst();
	     
	    List<Highscore> highscoreList = new ArrayList<Highscore>();
	    
		// Find the ListView resource.   
	    ListView mainListView = (ListView) findViewById( R.id.mainListView );  	     
	    
	    
	    //add highscores to list
	    int indexCounter = 1;
	    while(result.isAfterLast() == false){
	    	highscoreList.add(new Highscore(indexCounter, result.getString(1), result.getInt(2)));  
	    	result.moveToNext();
	    	indexCounter++;
	    }
	    
	    //load scores into the mainlistview
	    mainListView.setAdapter(new HighscoreListAdapter(this, R.layout.highscore_row, highscoreList));
	}

	//disable default menu button
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ( keyCode == KeyEvent.KEYCODE_MENU ) {
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}  
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	public void toMenu(View view){
		onBackPressed();
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
