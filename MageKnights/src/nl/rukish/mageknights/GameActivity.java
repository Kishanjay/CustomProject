package nl.rukish.mageknights;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class GameActivity extends ActionBarActivity implements GameViewListener {
	  
	//Shake sensor stuff
	private SensorManager mSensorManager;
	private float mAccel; // acceleration apart from gravity
	private float mAccelCurrent; // current acceleration including gravity
	private float mAccelLast; // last acceleration including gravity
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		/* do this in onCreate */
	    mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	    mAccel = 0.00f;
	    mAccelCurrent = SensorManager.GRAVITY_EARTH;
	    mAccelLast = SensorManager.GRAVITY_EARTH;
		
		setContentView(new GameView(this));
	}

	
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}

	public void onStopGame(){
		onBackPressed();
	}
	
	@Override
	public void onSubmitScore(int score) {
		if (score > 0){
			DBHelper mydb = new DBHelper(this);
			mydb.insertScore(getUserName(), score);
		}
	}

	@Override
	public String getUserName() {
		SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
    	
    	String username = sharedPrefs.getString("prefUsername", "NULL");
    	return username;
	}

	@Override
	public int getHighScore() {
		DBHelper mydb = new DBHelper(this);
		return mydb.getHighscore();
	}
	

	private final SensorEventListener mSensorListener = new SensorEventListener() {
		public void onSensorChanged(SensorEvent se) {
			float x = se.values[0];
			float y = se.values[1];
			float z = se.values[2];
			mAccelLast = mAccelCurrent;
			mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
			float delta = mAccelCurrent - mAccelLast;
			mAccel = mAccel * 0.9f + delta; // perform low-cut filter
			   
			if (mAccel > 6) {
				GameView.getPlayer1().onShake();
				mAccel = 0;
			}
		}
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};

	  @Override
	  protected void onResume() {
	    super.onResume();
	    mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	  }

	  @Override
	  protected void onPause() {
	    mSensorManager.unregisterListener(mSensorListener);
	    super.onPause();
	  }
}
