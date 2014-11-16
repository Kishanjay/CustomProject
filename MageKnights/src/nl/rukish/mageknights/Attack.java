package nl.rukish.mageknights;

import android.graphics.Rect;

public class Attack {

	private int xPos, yPos;
	private int width, height;
	private int duration;
	private int power;
	private int xSpeed, ySpeed;
	private boolean visible;
	
	public Attack(int xPos, int yPos, int width, int height, int direction) {
		this.xPos = xPos + (GameView.player1.getWidth()/2+3) * direction - width/2;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		xSpeed = 10*direction;
		ySpeed = 0;
		visible = true;
	}

	
	public Rect getRect(){
		Rect rect = new Rect(xPos, yPos, xPos+width, yPos+height);
		return rect;
	}
	
	public void update(){
		xPos += xSpeed;
		yPos += ySpeed;
		if (xPos > 900){
			visible = false;
		}
		checkCollision();
	}
	
	public void checkCollision(){
		Rect playerRect = GameView.player1.getRect();
		if (intersectAttack(playerRect)){
			GameView.player1.hit();
			visible = false;
			return;
		}
		
		Rect enemyRect = GameView.enemy1.getRect();
		if (intersectAttack(enemyRect)){
			GameView.enemy1.hit();
			visible = false;
			return;
		}
	}
	
	public boolean isVisible(){
		return visible;
	}
	
	public boolean intersectAttack(Rect r) {
		Rect attackRect = getRect();
		boolean noCollision = (attackRect.right < r.left
				|| attackRect.left > r.right || attackRect.top > r.bottom || attackRect.bottom < r.top);
		return !noCollision;
	}
	
}
