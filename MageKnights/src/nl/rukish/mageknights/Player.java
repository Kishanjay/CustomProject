package nl.rukish.mageknights;

import android.graphics.Rect;

public class Player {

	// Control variables
	private int xPos, yPos;
	private int width, height;
	private int speedX, speedY;
	private boolean jumped;
	public Attack attack1;

	// Powers
	private int movingSpeed;
	private int jumpingPower;

	public Player(int xPos, int yPos, int width, int height) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		movingSpeed = 5; // default
		jumpingPower = 15; // default
		jumped = false;
		attack1 = new Attack(800, 100, 100, 100);
	}

	public void update() {
		xPos += speedX;
		yPos += speedY;

		if (jumped == true) {
			speedY += 1;
			if (speedY == jumpingPower + 1) {
				speedY = 0;
				jumped = false;
			}
		}

		if (attack1.isVisible()) {
			attack1.update();
		}
	}

	public void moveLeft() {
		speedX = -movingSpeed;
	}

	public void moveRight() {
		speedX = movingSpeed;
	}

	public void stop() {
		speedX = 0;
	}

	public void jump() {
		if (jumped == false) {
			speedY = -jumpingPower;
			jumped = true;
		}
	}

	public void attack() {
		if (!attack1.isVisible())
			attack1 = new Attack(xPos + 15, yPos + 10, 20, 10);
	}

	public Rect getRect() {
		Rect rect = new Rect(xPos, yPos, xPos + width, yPos + height);
		return rect;
	}

}
