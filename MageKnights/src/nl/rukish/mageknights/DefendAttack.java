package nl.rukish.mageknights;

import android.graphics.Rect;
import nl.rukish.mageknights.framework.Attack;

public class DefendAttack extends Attack{
	public static final int DA_WIDTH = 20;
	public static final int DA_HEIGHT = 60;
	public static final int DA_XSPEED = 0;
	public static final int DA_YSPEED = 0;
	public static final int DA_POWER = 0;
	public static final int DA_DURATION = 100;
	public static final int DA_DELTAX = 40; //X distance from character
	
	private int growspeedY; //growth per frame 
	private int currentHeight;
	
	public DefendAttack(int xPos, int yBottom, int direction, int hashCode) {
		super(xPos + (DA_DELTAX*direction), yBottom - DA_HEIGHT, DA_WIDTH, DA_HEIGHT, direction, DA_XSPEED, DA_YSPEED, DA_POWER, DA_DURATION, hashCode);
		
		currentHeight = 0;
		growspeedY = 15;
	}

	
	public void update(){
		super.update(); //update of the Attack
		
		if (currentHeight < getHeight()){
			currentHeight += growspeedY;
		}
	}
	
	public Rect getRect() {
		Rect rect = new Rect(getxPos(), getyPos() + getHeight() - currentHeight, getxPos() + getWidth(), getyPos() + getHeight());
		return rect;
	}
}
