package nl.rukish.mageknights;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Rect;

public class Map {

	private List<Rect> floors;
	private List<Rect> ceils;
	private List<Rect> leftWalls;
	private List<Rect> rightWalls;
	public int width, height;
	
	public Map(int width, int height) {
		floors = new ArrayList<Rect>();
		ceils = new ArrayList<Rect>();
		leftWalls = new ArrayList<Rect>();
		rightWalls = new ArrayList<Rect>();
		this.width = width;
		this.height = height;
	}
	
	public void addSquareToMap(){
		addWall(-40, 0, 50, height); //left
		addWall(width-10, 0, 50, height); //right
		addCeil(0, -40, width, 50); //top
		addFloor(0, height-10, width, 50); //bottom
	}
	
	public void addFloor(int x, int y, int width, int height){
		floors.add(new Rect(x, y, x+width, y+height));
	}
	
	public void addCeil(int x, int y, int width, int height){
		ceils.add(new Rect(x, y, x+width, y+height));
	}
	
	public void addWall(int x, int y, int width, int height){
		assert(width>2);
		int halfwidth = width/2;
		leftWalls.add(new Rect(x, y, x+halfwidth, y+height));
		rightWalls.add(new Rect(x+halfwidth, y, x+width, y+height));
	}
	
	public void addPlatform(int x, int y, int width, int height){
		assert(height>1);
		assert(width>100);
		int halfheight = height/2;
		ceils.add(new Rect(x, y+halfheight, x+width, y+height));
		floors.add(new Rect(x, y, x+width, y+halfheight));
		leftWalls.add(new Rect(x, y, x+50, y+halfheight+10));
		rightWalls.add(new Rect(x+width-50, y, x+width, y+halfheight+10));
	}

	public List<Rect> getFloors(){
		return floors;
	}
	public List<Rect> getCeils(){
		return ceils;
	}
	public List<Rect> getLeftWalls(){
		return leftWalls;
	}
	public List<Rect> getRightWalls(){
		return rightWalls;
	}
	
	public List<Rect> getMap(){
		List<Rect> totalMap = new ArrayList<Rect>();
		totalMap.addAll(floors);
		//totalMap.addAll(ceils);
		totalMap.addAll(leftWalls);
		totalMap.addAll(rightWalls);
		return totalMap;
	}
}
