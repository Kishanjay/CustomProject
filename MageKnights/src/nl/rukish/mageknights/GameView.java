package nl.rukish.mageknights;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class GameView extends SurfaceView {
	private SurfaceHolder holder;
	GameLoopThread gameLoopThread;

	private Button left, right, attack, defend, jump;

	static Player player1;
	static Map currentMap;
	static Enemy enemy1;
	static int score;

	public GameView(Context context) {
		super(context);
		gameLoopThread = new GameLoopThread(this);

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
		enemy1 = new Enemy(200, 200, 40, 60);
		
		player1 = new Player(100, 200, 40, 60);
		left = new Button(10, currentMap.height-110, 110, 100);
		right = new Button(140, currentMap.height-110, 110, 100);
		jump = new Button(currentMap.width-200, currentMap.height-110, 80, 80);
		attack = new Button(currentMap.width-100, currentMap.height-110, 80, 80);
		defend = new Button(currentMap.width-100, currentMap.height-200, 80, 80);
		
		score = 0;
	}
	
	private void createMap(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		
		currentMap = new Map(width, height);
		//currentMap.addSquareToMap();
		currentMap.addPlatform(0, currentMap.height-50, currentMap.width, 150); //floor
		currentMap.addPlatform(280, currentMap.height-170, 300, 50); //middle
		currentMap.addPlatform(600, currentMap.height-300, 200, 50); //right
		currentMap.addPlatform(60, currentMap.height-300, 200, 50); //left
	}

	public void update() {
		player1.update();
		enemy1.updateEnemy();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK); // background color
		Paint blue = new Paint();
		blue.setColor(Color.BLUE);
		Paint red = new Paint();
		red.setColor(Color.RED);
		
		drawMap(canvas);
		drawButtons(canvas);
		
		canvas.drawText("FPS: " + Integer.toString((int) GameLoopThread.currentFPS), 800,
				40, blue);
		canvas.drawText("Score: " + Integer.toString(score), 600,
				40, blue);
		
		//Player draw stuff
		canvas.drawRect(player1.getRect(), blue);
		List<Attack> attacks = player1.attack;
		for (int i = 0; i < attacks.size(); i++) {
			Attack att = (Attack) attacks.get(i);
			canvas.drawRect(att.getRect(), blue);
		}
		if (player1.defend != null && player1.defend.isVisible())
			canvas.drawRect(player1.defend.getRect(), blue);
		for (int i = 0; i < player1.getHealth(); i++){
			canvas.drawRect(30 + (i * 40), 20, 60 + (i * 40), 50, red);
		}
		
		
		//Enemy draw stuff
		if (enemy1.isVisible())
			canvas.drawRect(enemy1.getRect(), red);
		attacks = enemy1.attack;
		for (int i = 0; i < attacks.size(); i++) {
			Attack att = (Attack) attacks.get(i);
			canvas.drawRect(att.getRect(), red);
		}
		for (int i = 0; i < enemy1.getHealth(); i++){
			canvas.drawRect(330 + (i * 40), 20, 360 + (i * 40), 50, blue);
		}
	}

	private void drawMap(Canvas canvas){
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
			Log.d("info",
					Integer.toString((int) x) + ", "
							+ Integer.toString((int) y));
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
}
