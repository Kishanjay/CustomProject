package nl.rukish.mageknights.framework;

import nl.rukish.mageknights.GameView;
import android.graphics.Rect;

public class Attack {

	private int xPos, yPos;
	private int width, height;
	private int duration;
	private int power;
	private int xSpeed, ySpeed;
	private boolean visible;

	public Attack(int xPos, int yPos, int width, int height, int direction, int xSpeed, int ySpeed, int power, int duration) {
		this.xPos = xPos - width/2; //start exact in the middle of the character
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		this.xSpeed = xSpeed * direction;
		this.ySpeed = ySpeed;
		this.power = power;
		this.duration = duration;
		visible = true;
	}

	public Rect getRect() {
		Rect rect = new Rect(xPos, yPos, xPos + width, yPos + height);
		return rect;
	}

	public void update() {
		xPos += xSpeed;
		yPos += ySpeed;
		if (xPos > 900 || xPos+width < 0 || duration == 0) {
			visible = false;
		}
		else {
			duration--;
			if (power > 0)
				checkCollision();
		}
	}

	public void checkCollision() {
		if (GameView.player1.isVisible()) {
			Rect playerRect = GameView.player1.getRect();
			if (intersectAttack(playerRect)) {
				GameView.player1.hit(xSpeed);
				visible = false;
				return;
			}
		}

		if (GameView.enemy1.isVisible()) {
			Rect enemyRect = GameView.enemy1.getRect();
			if (intersectAttack(enemyRect)) {
				GameView.enemy1.hit(xSpeed);
				visible = false;
				return;
			}
		}
	}

	public boolean isVisible() {
		return visible;
	}

	public boolean intersectAttack(Rect r) {
		Rect attackRect = getRect();
		boolean noCollision = (attackRect.right < r.left
				|| attackRect.left > r.right || attackRect.top > r.bottom || attackRect.bottom < r.top);
		return !noCollision;
	}

}

