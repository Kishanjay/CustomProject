package com.example.magefighters;

import android.annotation.SuppressLint;
import android.graphics.Canvas;

public class GameLoopThread extends Thread {
	static final double FPS = 60;
	private GameView view;
	private boolean running = false;

	public GameLoopThread(GameView view) {
		this.view = view;
	}

	public void setRunning(boolean run) {
		running = run;
	}

	@SuppressLint("WrongCall")
	@Override
	public void run() {
		double currentFPS = FPS;
		double ticksPS = 1000 / currentFPS;
		double startTime;
		double sleepTime;
		
		while (running) {
			Canvas c = null;
			startTime = System.currentTimeMillis();
			try {
				c = view.getHolder().lockCanvas();
				synchronized (view.getHolder()) {
					view.onDraw(c);
				}
			} finally {
				if (c != null) {
					view.getHolder().unlockCanvasAndPost(c);
				}
			}
			sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
			try {
				if (sleepTime > 0) {
					sleep((long) sleepTime);
				} else { //decrease framerate
					currentFPS--; 
					ticksPS = 1000 / currentFPS;
				}
			} catch (Exception e) {
			}
		}
	}
}