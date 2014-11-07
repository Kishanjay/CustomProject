package com.example.magefighters;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class Button {
	int xStart;
	int yStart;
	int xEnd;
	int yEnd;
	
	public Button(int xStart, int yStart, int width, int height){
		this.xStart = xStart;
		this.yStart = yStart;
		this.xEnd = xStart + width;
		this.yEnd = yStart + height;
	}
	
	public void onDraw(Canvas canvas){
		canvas.drawRect(xStart, yStart, xEnd, yEnd, new Paint());
	}
	
	public boolean isTouched(int xPos, int yPos){
		if (xStart <= xPos && xEnd >= xPos && yStart <= yPos && yEnd >= yPos)
			return true;
		return false;
	}
}
