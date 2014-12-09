package nl.rukish.mageknights;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
	private SurfaceHolder holder;
	GameLoopThread gameLoopThread;
	AssetManager assetManager;
	GameViewListener gameViewListener;

	private Button left, right, attack, defend, jump;
	private Button button_menu, button_retry;

	public static Player player1;
	static Map currentMap;
	public static Enemy enemy1;
	static int score;
	static Bitmap spriteSheet, spriteSheet2, background;
	static Rect screenRect;
	
	private boolean scoreSubmitted;

	public GameView(Context context) {
		super(context);
		
		gameViewListener = (GameViewListener) context;
		
		gameLoopThread = new GameLoopThread(this);
		assetManager = context.getAssets();

		createMap(context);
		initVariables();

		holder = getHolder();
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

	private void initVariables() {
		screenRect = currentMap.getRect();
		enemy1 = new Enemy(200, 200, 40, 60);

		player1 = new Player(100, 200, 40, 60);
		left = new Button(10, currentMap.height - 110, 110, 100);
		right = new Button(140, currentMap.height - 110, 110, 100);
		jump = new Button(currentMap.width - 200, currentMap.height - 110, 80,
				80);
		attack = new Button(currentMap.width - 100, currentMap.height - 110,
				80, 80);
		defend = new Button(currentMap.width - 100, currentMap.height - 200,
				80, 80);

		score = 0;
		scoreSubmitted = false;
		
		button_retry = new Button(200, 400, 200, 50);
		button_menu = new Button(450, 400, 200, 50);

		InputStream in = null;
		try {
			in = assetManager.open("spriteSheet.png");
			spriteSheet = BitmapFactory.decodeStream(in);
			in = assetManager.open("spriteSheet2.png");
			spriteSheet2 = BitmapFactory.decodeStream(in);
			in = assetManager.open("background.png");
			background = BitmapFactory.decodeStream(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		player1.b_standing = Bitmap.createBitmap(spriteSheet, 60, 0, 15, 35);
		player1.b_running.add(Bitmap.createBitmap(spriteSheet, 65, 40, 30, 30));
		player1.b_running.add(Bitmap.createBitmap(spriteSheet, 95, 40, 30, 30));
		player1.b_running.add(Bitmap.createBitmap(spriteSheet, 125, 40, 30, 30));
		player1.b_jumping = Bitmap.createBitmap(spriteSheet, 130, 80, 20, 40);
		player1.b_attack = Bitmap.createBitmap(spriteSheet, 130, 180, 31, 36);
		player1.b_defend = Bitmap.createBitmap(spriteSheet, 182, 473, 28, 32);
		player1.b_bullet = Bitmap.createBitmap(spriteSheet, 315, 250, 25, 15);
		player1.b_wall = Bitmap.createBitmap(spriteSheet, 207, 120, 32, 48);
		player1.b_dead = Bitmap.createBitmap(spriteSheet, 158, 961, 34, 19);
		
		enemy1.b_standing = Bitmap.createBitmap(spriteSheet2, 67, 3, 25, 40);
		enemy1.b_running.add(Bitmap.createBitmap(spriteSheet2, 60, 54, 37, 53));
		enemy1.b_running.add(Bitmap.createBitmap(spriteSheet2, 106, 55, 26, 56));
		enemy1.b_running.add(Bitmap.createBitmap(spriteSheet2, 145, 55, 38, 52));
		enemy1.b_jumping = Bitmap.createBitmap(spriteSheet2, 115, 129, 30, 40);
		enemy1.b_attack = Bitmap.createBitmap(spriteSheet2, 117, 503, 33, 37);
		enemy1.b_bullet = Bitmap.createBitmap(spriteSheet2, 291, 514, 17, 17);
		
	}

	private void createMap(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;

		currentMap = new Map(width, height);
		// currentMap.addSquareToMap();
		currentMap
				.addPlatform(0, currentMap.height - 50, currentMap.width, 150); // floor
		currentMap.addPlatform(280, currentMap.height - 170, 300, 50); // middle
		currentMap.addPlatform(600, currentMap.height - 300, 200, 50); // right
		currentMap.addPlatform(60, currentMap.height - 300, 200, 50); // left
	}

	public void update() {
		player1.update();
		enemy1.updateEnemy();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		//canvas.drawColor(Color.BLACK); // background color
		canvas.drawBitmap(background, null, screenRect, null);
		Paint blue = new Paint();
		blue.setColor(Color.BLUE);
		Paint red = new Paint();
		red.setColor(Color.RED);

		drawMap(canvas);
		drawButtons(canvas);

		canvas.drawText(
				"FPS: " + Integer.toString((int) GameLoopThread.currentFPS),
				800, 40, blue);
		canvas.drawText("Score: " + Integer.toString(score), 600, 40, blue);

		// Player draw stuff
		//canvas.drawRect(player1.getRect(), blue);
		
		if (player1.health > 0)
			canvas.drawBitmap((player1.getBitmap()), null, player1.getRect(), null);
		else
			canvas.drawBitmap(player1.b_dead, player1.getRect().left, player1.getRect().top, null);
		//Rect dead = new Rect(player1.getRect().left, player1.getRect().top,player1.getRect().left + 34, player1.getRect().top + 19);
		List<Attack> attacks = player1.attack;
		for (int i = 0; i < attacks.size(); i++) {
			Attack att = (Attack) attacks.get(i);
			canvas.drawRect(att.getRect(), blue);
			canvas.drawBitmap((player1.b_bullet), null, att.getRect(), null);
		}
		if (player1.defend != null && player1.defend.isVisible()){
			//canvas.drawRect(player1.defend.getRect(), blue);
			canvas.drawBitmap((player1.b_wall), null, player1.defend.getRect(), null);
		}
		for (int i = 0; i < player1.getHealth(); i++) {
			canvas.drawRect(30 + (i * 40), 20, 60 + (i * 40), 50, red);
		}

		// Enemy draw stuff
		if (enemy1.isVisible()){
			//canvas.drawRect(enemy1.getRect(), red);
			canvas.drawBitmap((enemy1.getBitmap()), null, enemy1.getRect(), null);
		}
		attacks = enemy1.attack;
		for (int i = 0; i < attacks.size(); i++) {
			Attack att = (Attack) attacks.get(i);
			canvas.drawRect(att.getRect(), red);
			canvas.drawBitmap((enemy1.b_bullet), null, att.getRect(), null);
		}
		for (int i = 0; i < enemy1.getHealth(); i++) {
			canvas.drawRect(330 + (i * 40), 20, 360 + (i * 40), 50, blue);
		}

		if (player1.health <= 0){
			drawGameOver(canvas);
			if (!scoreSubmitted){
				scoreSubmitted = true;
				gameViewListener.onSubmitScore(score);
			}
			
		}
		
	}

	private void drawMap(Canvas canvas) {
		Paint white = new Paint();
		white.setColor(Color.WHITE);

		List<Rect> map = currentMap.getMap();

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

	}
	
	private void drawGameOver(Canvas canvas){
		Paint white = new Paint();
		white.setColor(Color.WHITE);
		white.setAlpha(150);
		Paint black = new Paint();
		black.setColor(Color.BLACK);
		canvas.drawRect(100, 20, currentMap.width - 100, currentMap.height - 20, white);
		canvas.drawText("GAME OVER!", currentMap.width/2, 100, black);
		
		canvas.drawRect(button_menu.getRect(), black);
		canvas.drawRect(button_retry.getRect(), black);
	}
	
	private void restartGame(){
		player1.xPos = 30;
		enemy1.xPos = 400;
		player1.yPos = 0;
		enemy1.yPos = 0;
		player1.health = 5;
		enemy1.health = 3;
		score = 0;
		scoreSubmitted = false;
	}
	
	private void stopGame(){
		gameViewListener.onStopGame();
	}
	
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
			final float x = ev.getX(pointerId);
			final float y = ev.getY(pointerId);

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
			if (player1.health <= 0){
				if (button_retry.isPressed(x, y, pointerId)){
					restartGame();
					
				}
				if (button_menu.isPressed(x, y, pointerId)){
					stopGame();
				}
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
			final float x = ev.getX(pointerId);
			final float y = ev.getY(pointerId);

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
			final float x = ev.getX(pointerId);
			final float y = ev.getY(pointerId);

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

	public static Bitmap flipBitmap(Bitmap src) {
		Matrix m = new Matrix();
		m.preScale(-1, 1);
		Bitmap dst = Bitmap.createBitmap(src, 0, 0, src.getWidth(),
				src.getHeight(), m, false);
		dst.setDensity(DisplayMetrics.DENSITY_DEFAULT);
		return dst;
	}

	public static Bitmap makeTransparent(Bitmap bit) {
		int width = bit.getWidth();
		int height = bit.getHeight();
		Bitmap myBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		int[] allpixels = new int[myBitmap.getHeight() * myBitmap.getWidth()];
		bit.getPixels(allpixels, 0, myBitmap.getWidth(), 0, 0,
				myBitmap.getWidth(), myBitmap.getHeight());
		myBitmap.setPixels(allpixels, 0, width, 0, 0, width, height);

		for (int i = 0; i < myBitmap.getHeight() * myBitmap.getWidth(); i++) {
			if (allpixels[i] == Color.RED)

				allpixels[i] = Color.alpha(Color.TRANSPARENT);
		}

		myBitmap.setPixels(allpixels, 0, myBitmap.getWidth(), 0, 0,
				myBitmap.getWidth(), myBitmap.getHeight());
		Log.d("Supports alpha?", myBitmap.hasAlpha() + "");
		return myBitmap;
	}
}
