package nl.rukish.mageknights;

import android.annotation.SuppressLint;
import android.graphics.Canvas;

public class GameLoopThread extends Thread {
	static final double FPS = 60;
	static double currentFPS = FPS;
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
		currentFPS = FPS;
		double ticksPS = 1000 / currentFPS;
		double startTime;
		double sleepTime;
		
		while (running) {
			Canvas c = null;
			startTime = System.currentTimeMillis();
			try {
				c = view.getHolder().lockCanvas();
				synchronized (view.getHolder()) {
					view.update();
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
					if (currentFPS < FPS){
						currentFPS ++;
						ticksPS = 1000 / currentFPS;
					}
					sleep((long) sleepTime);
				} else { //took too long -> decrease frame rate
					currentFPS--; 
					ticksPS = 1000 / currentFPS;
				}
			} catch (Exception e) {
			}
		}
	}
}