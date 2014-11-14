package nl.rukish.mageknights;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
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

	public GameView(Context context) {
		super(context);
		gameLoopThread = new GameLoopThread(this);

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
		player1 = new Player(100, 200, 50, 80);
		left = new Button(50, 350, 110, 100);
		right = new Button(200, 350, 110, 100);
		jump = new Button(500, 350, 80, 80);
		attack = new Button(600, 350, 80, 80);
	}

	public void update() {
		player1.update();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK); // background color
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		canvas.drawRect(player1.getRect(), paint);
		canvas.drawText(Integer.toString((int) GameLoopThread.currentFPS), 300,
				40, paint);
		canvas.drawRect(player1.attack1.getRect(), paint);
		drawButtons(canvas);
	}

	public void drawButtons(Canvas canvas) {
		Paint white = new Paint();
		white.setColor(Color.WHITE);
		canvas.drawRect(left.getRect(), white);
		canvas.drawRect(right.getRect(), white);
		canvas.drawRect(jump.getRect(), white);
		canvas.drawRect(attack.getRect(), white);
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		
		
		final int maskedAction = action & MotionEvent.ACTION_MASK;
		if (maskedAction == MotionEvent.ACTION_DOWN || maskedAction == MotionEvent.ACTION_POINTER_DOWN) {
			int which = action >> MotionEvent.ACTION_POINTER_ID_SHIFT;
		    int pointerId = ev.getPointerId(which);
		    if (pointerId >= ev.getPointerCount()){
				return false;
			}
			final float x = ev.getX(pointerId);
			final float y = ev.getY(pointerId);
			
			if (left.isPressed(x, y, pointerId)){
				player1.moveLeft();
			}
			if (right.isPressed(x, y, pointerId)){
				player1.moveRight();
			}
			if (jump.isPressed(x, y, pointerId)){
				player1.jump();
			}
			if (attack.isPressed(x, y, pointerId)){
				player1.attack();
			}
			Log.d("info", Integer.toString((int) x) + ", " + Integer.toString((int) y));
		}
		if (maskedAction == MotionEvent.ACTION_UP || maskedAction == MotionEvent.ACTION_POINTER_UP){
			int which = action >> MotionEvent.ACTION_POINTER_ID_SHIFT;
		    int pointerId = ev.getPointerId(which);
		    if (pointerId >= ev.getPointerCount()){
				return false;
			}
			final float x = ev.getX(pointerId);
			final float y = ev.getY(pointerId);
			
			if (left.isTouched()){
				if (left.isReleased(pointerId)){
					player1.stop();
				}
			}
			if (right.isTouched()){
				if (right.isReleased(pointerId)){
					player1.stop();
				}
			}
		}
		if (maskedAction == MotionEvent.ACTION_MOVE){
			int which = action >> MotionEvent.ACTION_POINTER_ID_SHIFT;
		    int pointerId = ev.getPointerId(which);
		    if (pointerId >= ev.getPointerCount()){
				return false;
			}
			final float x = ev.getX(pointerId);
			final float y = ev.getY(pointerId);
			
			if (left.isTouched() && left.getPointerId() == pointerId){
				//check if isStillTouched()
				if (!left.isStillTouched(x, y)){
					player1.stop();
				}
			}
			if (!left.isTouched()){
				//check if isNowTouched()
				if (left.isPressed(x, y, pointerId)){
					player1.moveLeft();
				}
			}
			if (right.isTouched() && right.getPointerId() == pointerId){
				//check if isStillTouched()
				if (!right.isStillTouched(x, y)){
					player1.stop();
				}
			}
			if (!right.isTouched()){
				//check if isNowTouched()
				if (right.isPressed(x, y, pointerId)){
					player1.moveRight();
				}
			}
		}
		return true;
	}
}
