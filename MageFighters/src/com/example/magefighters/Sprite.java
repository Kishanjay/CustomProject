package com.example.magefighters;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Sprite {
	int x, y, ground;
	Screen screen;
	int width, height;
	int jumped;
	int jumpheight;
	
	public Sprite(Screen screen, int width, int height){
		jumpheight = 50;
		this.screen = screen;
		this.width = width;
		this.height = height;
		x = 10;
		y = ground = 78*screen.getHeight()/100;
	}
	
	public void onDraw(Canvas canvas){
		canvas.drawRect(x, y-height, x+width, y, new Paint());
		if (jumped == -1){ //jumping
			y -= 1;
			if (y <= ground-jumpheight){
				jumped = 1;
			}
		}
		else if (jumped == 1){ //going down
			y += 1;
			if (y >= ground){
				jumped = 0;
			}
		}
	}
	
	public void moveLeft(){
		x--;
	}
	
	public void moveRight(){
		x++;
	}
	
	public void jump(){
		if (jumped == 0){
			jumped = -1;
		}
	}
}
