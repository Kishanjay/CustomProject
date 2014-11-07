package com.example.magefighters;

public class Screen {
	private int height, width;
	
	public Screen(int height, int width){ //inverse because landscape
		this.height = width;
		this.width = height;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getWidth(){
		return width;
	}
}
