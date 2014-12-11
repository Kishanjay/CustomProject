package nl.rukish.mageknights;

import java.util.List;

import android.graphics.Rect;
import nl.rukish.mageknights.framework.Attack;

public class Defend {

	private int xPos, yPos;
	private int width, height, currentHeight;
	private int duration;
	private int power;
	private int xSpeed, ySpeed, growSpeed;
	private boolean visible;

	public Defend(int xPos, int yPos, int width, int height, int direction) {
		this.xPos = xPos + direction * 60;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		xSpeed = 0;
		ySpeed = 0;
		visible = true;
		growSpeed = 15;
		duration = 100;
	}

	public Rect getRect() {
		Rect rect = new Rect(xPos, yPos, xPos + width, yPos + currentHeight);
		return rect;
	}

	public void update() {
		if (currentHeight < height) {
			currentHeight += growSpeed;
			yPos -= growSpeed;
		} else {
			duration--;
			if (duration < 0) {
				visible = false;
				return;
			}
		}
		checkCollision();
	}

	public void checkCollision() {
		List<Attack> attack = GameView.player1.getAttack();
		for (int i = 0; i < attack.size(); i++) {
			Attack att = (Attack) attack.get(i);
			if (intersectDefend(att.getRect())) {
				attack.remove(i);
			}
		}
		List<Attack> attack2 = GameView.enemy1.getAttack();
		for (int i = 0; i < attack2.size(); i++) {
			Attack att = (Attack) attack2.get(i);
			if (intersectDefend(att.getRect())) {
				attack2.remove(i);
			}
		}
	}

	public boolean intersectDefend(Rect r) {
		Rect defendRect = getRect();
		boolean noCollision = (defendRect.right < r.left
				|| defendRect.left > r.right || defendRect.top > r.bottom || defendRect.bottom < r.top);
		return !noCollision;
	}

	public boolean isVisible() {
		return visible;
	}

}
