package nl.rukish.mageknights;

import java.util.ArrayList;
import java.util.Arrays;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class HighscoreActivity extends ActionBarActivity {

	private ListView mainListView ; 
	private ArrayAdapter<String> listAdapter;
	private DBHelper mydb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_highscore);
		mydb = new DBHelper(this);
		
		// Find the ListView resource.   
	    mainListView = (ListView) findViewById( R.id.mainListView );  
	     
	    ArrayList<String> scoreList = new ArrayList<String>();  
	        
	    listAdapter = new ArrayAdapter<String>(this, R.layout.highscore_row, scoreList);  

	    Cursor result = mydb.getData();
	    
	    result.moveToFirst();
	    int indexCounter = 1;
	      while(result.isAfterLast() == false){
	    	  listAdapter.add(indexCounter + ". " + result.getString(1) + result.getString(2));  
	    	  result.moveToNext();
	    	  indexCounter++;
	      }
	      
	    // Set the ArrayAdapter as the ListView's adapter.  
	    mainListView.setAdapter( listAdapter );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.highscore, menu);
		return true;
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
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(this, MainMenu.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
		this.finish();
	}
}
