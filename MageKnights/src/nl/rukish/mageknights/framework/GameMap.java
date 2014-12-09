package nl.rukish.mageknights.framework;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Rect;

public class GameMap {

	private static List<Rect> floors;
	private static List<Rect> ceils;
	private static List<Rect> walls;
	private static int width;
	private static int height;
	
	public GameMap(int width, int height) {
		floors = new ArrayList<Rect>();
		ceils = new ArrayList<Rect>();
		walls = new ArrayList<Rect>();
		
		this.width = width;
		this.height = height;
	}
	
	//Add the width/height bounds to the map
	public void addBoundsToMap(){ 
		addWall(-40, 0, 50, height); //left
		addWall(width-10, 0, 50, height); //right
		addCeil(0, -40, width, 50); //top
		addFloor(0, height-10, width, 50); //bottom
	}
	
	//stuff our characters can stand on
	public void addFloor(int x, int y, int width, int height){
		floors.add(new Rect(x, y, x+width, y+height));
	}
	
	//stuff our character cannot jump through
	public void addCeil(int x, int y, int width, int height){
		ceils.add(new Rect(x, y, x+width, y+height));
	}
	
	//stuff our character cannot walk through
	public void addWall(int x, int y, int width, int height){
		assert(width>2);
		int halfwidth = width/2;
		walls.add(new Rect(x, y, x+halfwidth, y+height));
		walls.add(new Rect(x+halfwidth, y, x+width, y+height));
	}
	
	//stuff our character can stand on (and not jump through)
	public void addPlatform(int x, int y, int width, int height){
		assert(height>1);
		assert(width>100);
		int halfheight = height/2;
		ceils.add(new Rect(x, y+halfheight, x+width, y+height));
		floors.add(new Rect(x, y, x+width, y+halfheight));
		walls.add(new Rect(x, y, x+50, y+halfheight+10));
		walls.add(new Rect(x+width-50, y, x+width, y+halfheight+10));
	}

	public static List<Rect> getFloors(){
		return floors;
	}
	public static List<Rect> getCeils(){
		return ceils;
	}
	public static List<Rect> getWalls(){
		return walls;
	}
	
	public static List<Rect> getMap(){
		List<Rect> totalMap = new ArrayList<Rect>();
		totalMap.addAll(floors);
		//totalMap.addAll(ceils);
		totalMap.addAll(walls);
		return totalMap;
	}
	
	public static Rect getRect(){
		return new Rect(0, 0, width, height);
	}
	
	public static int getMapWidth(){
		return width;
	}
	
	public static int getMapHeight(){
		return height;
	}
}