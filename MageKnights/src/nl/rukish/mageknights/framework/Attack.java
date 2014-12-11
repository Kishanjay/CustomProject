package nl.rukish.mageknights.framework;

import java.util.ArrayList;
import java.util.List;

import nl.rukish.mageknights.GameView;
import android.graphics.Rect;
import nl.rukish.mageknights.framework.Character;

public class Attack {

	private int xPos, yPos;
	private int width, height;
	private int duration;
	private int power;
	private int xSpeed, ySpeed;
	private boolean visible;
	
	private int hashCode; //hashCode of the owner

	public Attack(int xPos, int yPos, int width, int height, int direction, int xSpeed, int ySpeed, int power, int duration, int hashCode) {
		this.xPos = xPos - width/2; //start exact in the middle of the character
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		this.xSpeed = xSpeed * direction;
		this.ySpeed = ySpeed;
		this.power = power;
		this.duration = duration;
		this.hashCode = hashCode;
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
		Character player1 = GameView.getPlayer1();
		//Collision with player
		if (player1.hashCode() != hashCode && player1.isVisible()) {
			Rect playerRect = player1.getRect();
			if (intersectAttack(playerRect)) {
				player1.hit(xSpeed);
				visible = false;
				return;
			}
		}
		
		Character enemy1 = GameView.getEnemy1();
		//Collision with enemy
		if (enemy1.hashCode() != hashCode && enemy1.isVisible()) {
			Rect enemyRect = enemy1.getRect();
			if (intersectAttack(enemyRect)) {
				enemy1.hit(xSpeed);
				visible = false;
				return;
			}
		}
		
		//Collision with another attack
		List<Attack> allAttacks = new ArrayList<Attack>();
		allAttacks.addAll(player1.getAttack());
		allAttacks.addAll(enemy1.getAttack());
		if (allAttacks != null){
			for (int i = 0; i < allAttacks.size(); i++){
				Attack att = allAttacks.get(i);
				if (att.hashCode() != hashCode() && intersectAttack(att.getRect())){
					visible = false;
					att.setVisible(false);
					return;
				}
			}
		}
		
		//Collision with a defend block
		Attack defend = player1.getDefend();
		if (defend != null && defend.isVisible() && intersectAttack(defend.getRect())){
			visible = false;
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

	// Getters and Setters below.
	public int getxPos() {
		return xPos;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	

}

