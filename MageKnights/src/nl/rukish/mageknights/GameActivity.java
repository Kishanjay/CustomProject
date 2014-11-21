package nl.rukish.mageknights;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends ActionBarActivity implements GameViewListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(new GameView(this));
	}

	
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}



	@Override
	public void onSubmitScore(int score) {
		Intent intent = new Intent(this, HighscoreActivity.class);
		intent.putExtra("score", score);
		this.startActivity(intent);
		this.finish();
	}
}
