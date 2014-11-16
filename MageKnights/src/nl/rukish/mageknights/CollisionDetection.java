package nl.rukish.mageknights;

import android.graphics.Rect;

final class CollisionDetection {

	static boolean goesThrough(int xStart, int yStart, int xEnd, int yEnd, int width, int height, Rect rect){
		int xBegin = (xStart > xEnd) ? xStart : xEnd;
		int xEind = (xStart < xEnd) ? xStart : xEnd;
		int yBegin = (yStart > yEnd) ? yStart : yEnd;
		int yEind = (yStart < yEnd) ? yStart : yEnd;
		
		//?????????????????
		
		Rect beginPos = new Rect(xStart, yStart, xStart+width, yStart+height);
		Rect endPos = new Rect(xEnd, yEnd, xEnd+width, yEnd+height);
		return goesThrough(beginPos, endPos, rect);
	}
	
	static boolean goesThrough(Rect beginPos, Rect endPos, Rect rect){
		
		return false;
	}
}
