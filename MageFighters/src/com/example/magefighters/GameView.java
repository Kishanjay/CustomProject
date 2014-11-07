package com.example.magefighters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class GameView extends SurfaceView {
	private SurfaceHolder holder;
	private GameLoopThread gameLoopThread;
	private Sprite player;
	private Button left, right, jump;
	private Screen screen;
	
	public GameView(Context context, Screen screen) {
		super(context);
		this.screen = screen;
		
		gameLoopThread = new GameLoopThread(this);
		
		player = new Sprite(screen, xPrToPx(3), yPrToPx(13));
		left = new Button(xPrToPx(3), yPrToPx(85), xPrToPx(10), yPrToPx(10));
		right = new Button(xPrToPx(15), yPrToPx(85), xPrToPx(10), yPrToPx(10));
		jump = new Button(xPrToPx(85), yPrToPx(85), yPrToPx(10), yPrToPx(10));
		
		holder = getHolder();
		holder.addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				gameLoopThread.setRunning(false);
			}

			@SuppressLint("WrongCall")
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

	@SuppressLint("WrongCall")
	@Override
	protected void onDraw(Canvas canvas) { 
		canvas.drawColor(Color.WHITE); // background color
		drawButtons(canvas);
		player.onDraw(canvas);
	}
	
	@SuppressLint("WrongCall")
	public void drawButtons(Canvas canvas){
		left.onDraw(canvas);
		right.onDraw(canvas);
		jump.onDraw(canvas);
		canvas.drawRect(0, yPrToPx(78), screen.getWidth(), yPrToPx(80), new Paint());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		if (left.isTouched(x,y))
			player.moveLeft();
		if (right.isTouched(x, y))
			player.moveRight();
		if (jump.isTouched(x, y))
			player.jump();
		
		return true;

	}
	
	public int xPrToPx(int percentage){
		return (int) percentage*screen.getWidth()/100;
	}
	
	public int yPrToPx(int percentage){
		return (int) percentage*screen.getHeight()/100;
	}
}