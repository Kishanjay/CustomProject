package nl.rukish.mageknights;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import nl.rukish.mageknights.framework.Attack;
import nl.rukish.mageknights.framework.GameMap;
import nl.rukish.mageknights.framework.Character;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class GameView extends SurfaceView {
	// scaling variables
	private final int CANVAS_WIDTH = 570;
	private final int CANVAS_HEIGHT = 330;

	// important stuff
	private SurfaceHolder holder; // used to draw the canvas
	private GameLoopThread gameLoopThread; // the heartbeat of the game
	private AssetManager assetManager; // used to load files
	private GameViewListener gameViewListener; // used to call functions of the gameActivity class

	// The visual buttons the game uses
	private Button left, right, attack, defend, jump;
	private Button button_menu, button_retry, button_pause, button_resume;

	private static Player player1;
	private static Enemy enemy1;

	private static GameMap gameMap;
	private static int score;

	//bitmaps
	private Bitmap spriteSheet, spriteSheet2, background; //backgrounds
	private Bitmap bo_gameover, bo_pause; //overlays
	private Bitmap b_menu, b_retry, bc_resume, bc_pause; //buttons
	private Bitmap bi_heart1, bi_heart2; //icons
	
	// helper variables
	private boolean scoreSubmitted, paused;
	
	private Paint BLUEPAINT, REDPAINT, BLACKPAINT30, BLACKPAINT25, WHITEPAINT, TRANSPAINT;

	//rectangle of the current screen
	private static Rect screenRect;

	
	public GameView(Context context) {
		super(context);

		gameViewListener = (GameViewListener) context;
		gameLoopThread = new GameLoopThread(this);
		assetManager = context.getAssets();

		createMap(context);
		initFunctional(); // sets all functional variables
		initVisual(); // sets all visual variables

		// start/stop the gameloop
		holder = getHolder();
		holder.setFixedSize(570, 330);
		holder.addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				gameLoopThread.setRunning(false);
			}
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				gameLoopThread.setRunning(true);
				gameLoopThread.start();
			}
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void initFunctional() {
		// init values
		score = 0;
		scoreSubmitted = false;
		paused = false;

		screenRect = GameMap.getRect();

		// create players
		player1 = new Player(100, 200);
		enemy1 = new Enemy(GameMap.getMapWidth()-100, 200);

		// create buttons
		left = new Button(10, GameMap.getMapHeight() - 110, 110, 100);
		right = new Button(140, GameMap.getMapHeight() - 110, 110, 100);
		jump = new Button(GameMap.getMapWidth() - 200, GameMap.getMapHeight() - 110, 80, 80);
		attack = new Button(GameMap.getMapWidth() - 100, GameMap.getMapHeight() - 110, 80, 80);
		defend = new Button(GameMap.getMapWidth() - 100, GameMap.getMapHeight() - 200, 80, 80);
	}
	
	private void initVisual(){
		// load sprite sheets and create images for characters
		InputStream in = null;
		try {
			in = assetManager.open("spriteSheet.png");
			spriteSheet = BitmapFactory.decodeStream(in);
			in = assetManager.open("spriteSheet2.png");
			spriteSheet2 = BitmapFactory.decodeStream(in);
			in = assetManager.open("background.png");
			background = BitmapFactory.decodeStream(in);
			in = assetManager.open("overlay_gameover.png");
			bo_gameover = BitmapFactory.decodeStream(in);
			in = assetManager.open("overlay_pause.png");
			bo_pause = BitmapFactory.decodeStream(in);
			in = assetManager.open("button_menu.png");
			b_menu = BitmapFactory.decodeStream(in);
			in = assetManager.open("button_retry.png");
			b_retry = BitmapFactory.decodeStream(in);
			in = assetManager.open("button_resume.png");
			bc_resume = BitmapFactory.decodeStream(in);
			in = assetManager.open("button_pause.png");
			bc_pause = BitmapFactory.decodeStream(in);
			in = assetManager.open("icon_heart1.png");
			bi_heart1 = BitmapFactory.decodeStream(in);
			in = assetManager.open("icon_heart2.png");
			bi_heart2 = BitmapFactory.decodeStream(in);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		button_menu = new Button(320, 130, b_menu);
		button_retry = new Button(140, 130, b_retry);
		button_resume = new Button(10, 10, bc_resume);
		button_pause = new Button(10,10, bc_pause);
		
		player1.parseSpritesheet(spriteSheet);
		enemy1.parseSpritesheet(spriteSheet2);

		REDPAINT = new Paint();
		BLUEPAINT = new Paint();
		BLACKPAINT30 = new Paint();
		BLACKPAINT25 = new Paint();
		WHITEPAINT = new Paint();
		TRANSPAINT = new Paint();
		REDPAINT.setColor(Color.RED);
		BLUEPAINT.setColor(Color.BLUE);
		BLACKPAINT30.setColor(Color.BLACK);
		BLACKPAINT30.setTextSize(30); //used for writing
		BLACKPAINT25.setColor(Color.BLACK);
		BLACKPAINT25.setTextSize(25);
		WHITEPAINT.setColor(Color.WHITE);
		TRANSPAINT.setARGB(200, 0, 0, 0); //transparent
		
	}

	private void createMap(Context context) {
		// get screen dimensions
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;

		gameMap = new GameMap(CANVAS_WIDTH, CANVAS_HEIGHT, width, height);

		gameMap.addPlatform(-50, GameMap.getMapHeight() - 30, GameMap.getMapWidth()+50, 100); // floor
		gameMap.addPlatform(GameMap.getMapWidth() / 2 - 150, GameMap.getMapHeight() - 110, 300, 30); // middle
		gameMap.addPlatform(50, GameMap.getMapHeight() - 200, 150, 30); // left
		gameMap.addPlatform(GameMap.getMapWidth() - 200, GameMap.getMapHeight() - 200, 150, 30); // right
	}

	public void update() {
		if (!isPaused()){
			player1.update();
			enemy1.update();
		}
		if (isGameOver()){
			if (!scoreSubmitted){
				gameViewListener.onSubmitScore(score);
				scoreSubmitted = true;
			}
		}	
	}

	@Override
	protected void onDraw(Canvas canvas) {
		//draw the background
		canvas.drawBitmap(background, null, screenRect, null);

		//draw all of the platforms
		drawMap(canvas);
		
		//draw the playbuttons
		drawButtons(canvas);

		//draws some gameinfo
		canvas.drawText("FPS: " + Integer.toString((int) GameLoopThread.currentFPS), GameMap.getMapWidth() - 50, 70, REDPAINT);
		canvas.drawText("Score: " + Integer.toString(score), GameMap.getMapWidth() - 130, 40, BLACKPAINT30);

		drawPlayer(canvas);
		drawEnemy(canvas);
		
		//on gameover (visual)
		if (isGameOver()){
			drawGameOver(canvas); 
		}
		else if (isPaused()){
			drawPause(canvas);
		}
	}
	
	private void drawPlayer(Canvas canvas) {
		//draws the actual character
		if (player1.isVisible()){
			canvas.drawBitmap(player1.getBitmap(), player1.getRect().left, player1.getRect().top, null);
		}

		//draw all of the attack of the player
		 List<Attack> attacks = player1.getAttack(); 
		 for (int i = 0; i < attacks.size(); i++) {
			 Attack att = (Attack) attacks.get(i);
			 canvas.drawRect(att.getRect(), BLUEPAINT); 
			 canvas.drawBitmap((player1.getB_bullet()), null, att.getRect(), null);
		 } 
		 //draw the defend block
		 if (player1.getDefend() != null && player1.getDefend().isVisible()){
			 canvas.drawBitmap((player1.getB_wall()), null, player1.getDefend().getRect(), null); 
		 } 
		 //draw the health
		 for (int i = 0; i < player1.getHealth(); i++) {
			 canvas.drawBitmap(bi_heart1, 70 + 33*i, 10, TRANSPAINT);
		 }
	}
	
	private void drawEnemy(Canvas canvas){
		//draw the character
		if (enemy1.isVisible()) {
			canvas.drawBitmap(enemy1.getBitmap(), enemy1.getRect().left, enemy1.getRect().top, null);
		}
		
		//draw all of the attacks
		List<Attack> attacks = enemy1.getAttack(); 
		for (int i = 0; i < attacks.size(); i++) {
			Attack att = (Attack) attacks.get(i); 
			canvas.drawBitmap((enemy1.getB_bullet()), null, att.getRect(), null);
		} 
		//draw the health
		for (int i = 0; i < enemy1.getHealth(); i++) {
			canvas.drawBitmap(bi_heart2, 400 - 33*i, 10, TRANSPAINT);
		}

	}
	
	private void drawMap(Canvas canvas) {
		Paint white = new Paint();
		white.setColor(Color.WHITE);

		List<Rect> map = GameMap.getMap();

		for (int i = 0; i < map.size(); i++) {
			Rect rect = (Rect) map.get(i);
			canvas.drawRect(rect, white);
		}
	}

	private void drawButtons(Canvas canvas) {
		Paint white = new Paint();
		white.setColor(Color.WHITE);
		white.setAlpha(50);
		canvas.drawRect(left.getRect(), white);
		canvas.drawRect(right.getRect(), white);
		canvas.drawRect(jump.getRect(), white);
		canvas.drawRect(attack.getRect(), white);
		canvas.drawRect(defend.getRect(), white);
		
		//button in the leftup corner
		if (isPaused()){
			canvas.drawBitmap(bc_resume, null, button_resume.getRect(), TRANSPAINT);
		}
		else {
			canvas.drawBitmap(bc_pause, null, button_pause.getRect(), TRANSPAINT);
		}

	}

	private void drawGameOver(Canvas canvas) {
		int highscore = gameViewListener.getHighScore();
		
		canvas.drawBitmap(bo_gameover, 100, 50, TRANSPAINT);

		canvas.drawBitmap(b_retry, null, button_retry.getRect(), TRANSPAINT);
		canvas.drawBitmap(b_menu, null, button_menu.getRect(), TRANSPAINT);
		
		canvas.drawText("Score: " + score, 210, 220, BLACKPAINT30);
		canvas.drawText("Highscore: " + highscore, 210, 250, BLACKPAINT25);
	}
	
	private void drawPause(Canvas canvas){
		canvas.drawBitmap(bo_pause, 100, 50, TRANSPAINT);
		canvas.drawBitmap(b_retry, null, button_retry.getRect(), TRANSPAINT);
		canvas.drawBitmap(b_menu, null, button_menu.getRect(), TRANSPAINT);
	}

	// Gamefunctions
	private void restartGame() {
		score = 0;
		scoreSubmitted = false;
		
		getPlayer1().spawn();
		getEnemy1().spawn();
		paused = false;
	}

	private void stopGame() {
		gameViewListener.onStopGame();
	}
	
	private void togglePause() {
		if (isPaused()){
			paused = false;
		}
		else {
			paused = true;
		}
	}
	
	public static void increaseScore(){
		score++;
	}
	
	private boolean isPaused(){
		return paused;
	}
	
	private boolean isGameOver(){
		return getPlayer1().isDead();
	}
	

	// Touch sensor stuff
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		final int maskedAction = action & MotionEvent.ACTION_MASK;
		if (maskedAction == MotionEvent.ACTION_DOWN
				|| maskedAction == MotionEvent.ACTION_POINTER_DOWN) {
			int which = action >> MotionEvent.ACTION_POINTER_ID_SHIFT;
			int pointerId = ev.getPointerId(which);
			if (pointerId >= ev.getPointerCount()) {
				player1.stop();
				return true;
			}
			final float x = GameMap.scaleX(ev.getX(pointerId));
			final float y = GameMap.scaleY(ev.getY(pointerId));

			if (left.isPressed(x, y, pointerId)) {
				player1.moveLeft();
			}
			if (right.isPressed(x, y, pointerId)) {
				player1.moveRight();
			}
			if (jump.isPressed(x, y, pointerId)) {
				player1.jump();
			}
			if (attack.isPressed(x, y, pointerId)) {
				player1.attack();
			}
			if (defend.isPressed(x, y, pointerId)) {
				player1.defend();
			}
			if (player1.isDead() || isPaused()) {
				if (button_menu.isPressed(x, y, pointerId)) {
					stopGame();
				}
				if (button_retry.isPressed(x, y, pointerId)) {
					restartGame();
				}
			}
			if (button_pause.isPressed(x, y, pointerId) || button_resume.isPressed(x, y, pointerId)){
				togglePause();
			}
		}
		if (maskedAction == MotionEvent.ACTION_UP
				|| maskedAction == MotionEvent.ACTION_POINTER_UP) {
			int which = action >> MotionEvent.ACTION_POINTER_ID_SHIFT;
			int pointerId = ev.getPointerId(which);
			if (pointerId >= ev.getPointerCount()) {
				player1.stop();
				return true;
			}
			final float x = GameMap.scaleX(ev.getX(pointerId));
			final float y = GameMap.scaleY(ev.getY(pointerId));

			if (left.isTouched()) {
				if (left.isReleased(pointerId)) {
					player1.stop();
				}
			}
			if (right.isTouched()) {
				if (right.isReleased(pointerId)) {
					player1.stop();
				}
			}
		}
		if (maskedAction == MotionEvent.ACTION_MOVE) {
			int which = action >> MotionEvent.ACTION_POINTER_ID_SHIFT;
			int pointerId = ev.getPointerId(which);
			if (pointerId >= ev.getPointerCount()) {
				player1.stop();
				return true;
			}
			final float x = GameMap.scaleX(ev.getX(pointerId));
			final float y = GameMap.scaleY(ev.getY(pointerId));

			if (left.isTouched() && left.getPointerId() == pointerId) {
				// check if isStillTouched()
				if (!left.isStillTouched(x, y)) {
					player1.stop();
				}
			}
			if (!left.isTouched()) {
				// check if isNowTouched()
				if (left.isPressed(x, y, pointerId)) {
					player1.moveLeft();
				}
			}
			if (right.isTouched() && right.getPointerId() == pointerId) {
				// check if isStillTouched()
				if (!right.isStillTouched(x, y)) {
					player1.stop();
				}
			}
			if (!right.isTouched()) {
				// check if isNowTouched()
				if (right.isPressed(x, y, pointerId)) {
					player1.moveRight();
				}
			}
		}
		return true;
	}

	// horizontal flip of bitmap
	public static Bitmap flipBitmap(Bitmap src) {
		Matrix m = new Matrix();
		m.preScale(-1, 1);
		Bitmap dst = Bitmap.createBitmap(src, 0, 0, src.getWidth(),
				src.getHeight(), m, false);
		dst.setDensity(DisplayMetrics.DENSITY_DEFAULT);
		return dst;
	}
	
	//Getters and Setters
	public static Character getPlayer1() {
		return player1;
	}
	
	public static Character getEnemy1() {
		return enemy1;
	}
	
	public static List<Character> getCharacters(){
		List<Character> gameCharacters = null;
		gameCharacters.add(player1);
		gameCharacters.add(enemy1);
		return gameCharacters;
	}
}
