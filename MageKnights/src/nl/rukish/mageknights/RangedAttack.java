package nl.rukish.mageknights;

import nl.rukish.mageknights.framework.Attack;

public class RangedAttack extends Attack {
	private static int ra_width = 20;
	private static int ra_height = 10;
	private static int ra_xSpeed = 10;
	private static int ra_ySpeed = 0;
	private static int ra_power = 10;
	private static int ra_duration = -1;
	
	public RangedAttack(int xPos, int yPos, int direction) {
		super(xPos, yPos, ra_width, ra_height, direction, ra_xSpeed, ra_ySpeed, ra_power, ra_duration);
		
		
		
	}

}
