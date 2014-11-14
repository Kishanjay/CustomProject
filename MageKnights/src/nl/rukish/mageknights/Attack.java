package nl.rukish.mageknights;

import android.graphics.Rect;

public class Attack {

	private int xPos, yPos;
	private int width, height;
	private int duration;
	private int power;
	private int xSpeed, ySpeed;
	private boolean visible;
	
	public Attack(int xPos, int yPos, int width, int height) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		xSpeed = 10;
		ySpeed = 0;
		visible = true;
	}

	
	public Rect getRect(){
		Rect rect = new Rect(xPos, yPos, xPos+width, yPos+height);
		return rect;
	}
	
	public void update(){
		xPos += xSpeed;
		yPos += ySpeed;
		if (xPos > 900){
			visible = false;
		}
	}
	
	public boolean isVisible(){
		return visible;
	}
	
}
