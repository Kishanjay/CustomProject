package nl.rukish.mageknights;

import nl.rukish.mageknights.framework.Attack;

public class DefendAttack extends Attack{
	public static int da_width = 40;
	public static int da_height = 60;
	public static int da_xSpeed = 0;
	public static int da_ySpeed = 0;
	public static int da_power = 0;
	public static int da_duration = 100;
	
	public DefendAttack(int xPos, int yPos, int direction) {
		super(xPos, yPos, da_width, da_height, direction, da_xSpeed, da_ySpeed, da_power, da_duration);
	}

}
