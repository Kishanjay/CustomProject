package nl.rukish.mageknights;

import android.graphics.Rect;

public class Button {

	private int xPos, yPos;
	private int width, height;
	private int pointerId;
	private boolean isTouched;
	
	public Button(int xPos, int yPos, int width, int height) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		isTouched = false;
	}
	
	public Rect getRect(){
		Rect rect = new Rect(xPos, yPos, xPos+width, yPos+height);
		return rect;
	}
	
	public boolean isPressed(float x, float y, int pointerId){
		if (x > xPos && x < xPos+width && y > yPos && y < yPos+height){
			isTouched = true;
			this.pointerId = pointerId;
			return true;
		}
		return false;
	}
	
	public boolean isReleased(int pointerId){
		if (isTouched == false)
			return true;
		if (this.pointerId != pointerId)
			return false;
		isTouched = false;
		return true;
	}
	
	public boolean isStillTouched(float x, float y){
		if (x > xPos && x < xPos+width && y > yPos && y < yPos+height){
			return true;
		}
		isTouched = false;
		return false;
	}
	
	public boolean isTouched(){
		return isTouched;
	}
	
	public int getPointerId(){
		return pointerId;
	}

}
