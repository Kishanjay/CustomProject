package nl.rukish.mageknights;

import nl.rukish.mageknights.framework.Attack;

public class RangedAttack extends Attack {
	private static final int RA_WIDTH = 15;
	private static final int RA_HEIGHT = 6;
	private static final int RA_XSPEED = 10;
	private static final int RA_YSPEED = 0;
	private static final int RA_POWER = 10;
	private static final int RA_DURATION = -1;
	
	public RangedAttack(int xPos, int yPos, int direction, int hashCode) {
		super(xPos, yPos, RA_WIDTH, RA_HEIGHT, direction, RA_XSPEED, RA_YSPEED, RA_POWER, RA_DURATION, hashCode);
		
		
		
	}

}
