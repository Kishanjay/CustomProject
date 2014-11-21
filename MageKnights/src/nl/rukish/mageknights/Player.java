package nl.rukish.mageknights;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class Player {
	// Control variables
	private int xPos;
	private int yPos;
	private int width, height;
	private int speedX, speedY;
	private boolean jumped;
	public List<Attack> attack;
	private int direction; // 1 is right -1 is left
	protected int health;
	public Defend defend;
	protected boolean visible;
	private int cooldown;

	// Powers
	protected int movingSpeed;
	protected int jumpingPower;
	protected int attackCooldown;
	
	//visual
	protected Bitmap b_standing, b_jumping, b_attack, b_defend, b_bullet, b_wall;
	protected List<Bitmap> b_running = new ArrayList<Bitmap>();
	private int frameNumber, lastAttackFrameNumber, lastDefendFrameNumber;
	private int b_runningDelay = 10;

	public Player(int xPos, int yPos, int width, int height) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		movingSpeed = 5; // default
		jumpingPower = 17; // default
		jumped = false;
		direction = 1;
		attack = new ArrayList<Attack>();
		health = 1;
		visible = true;
		cooldown = 0;
		attackCooldown = 30;
		frameNumber = 0;
		lastAttackFrameNumber = 0;
		lastDefendFrameNumber = 0;
	}

	public void update() {
		cooldown--;
		frameNumber++;
		for (int i = 0; i < attack.size(); i++) {
			Attack att = (Attack) attack.get(i);
			if (att.isVisible())
				att.update();
			else
				attack.remove(i);
		}
		if (defend != null && defend.isVisible())
			defend.update();
		speedY += 1;
		xPos += speedX;
		yPos += speedY;
		checkMapCollisions();
		checkScreenCollisions(); // keeps player inside screen
		if (speedY > 3) {
			jumped = true;
		}

	}

	private void checkScreenCollisions() {
		Map currentMap = GameView.currentMap;
		if (xPos < 0) {
			xPos = 0;
			// speedX = 0;
		}
		if (xPos > currentMap.width - width) {
			xPos = currentMap.width - width;
			// speedX = 0;
		}
		if (yPos < 0) {
			yPos = 0;
			speedY = 0;
		}
		// if (yPos > currentMap.height-height){
		// yPos = currentMap.height-height;
		// speedY = 0;
		// }
	}

	public boolean intersectPlayer(Rect r) {
		Rect playerRect = getRect();
		boolean noCollision = (playerRect.right < r.left
				|| playerRect.left > r.right || playerRect.top > r.bottom || playerRect.bottom < r.top);
		return !noCollision;
	}

	private void checkMapCollisions() {
		Map currentMap = GameView.currentMap;

		List<Rect> leftWalls = currentMap.getLeftWalls();
		List<Rect> rightWalls = currentMap.getRightWalls();
		List<Rect> floors = currentMap.getFloors();
		List<Rect> ceils = currentMap.getCeils();

		Rect playerRect = new Rect(xPos, yPos, xPos + width, yPos + height);

		/*
		 * //WERK NOG NIET HELEMAAL LEKKER for (int i = 0; i < leftWalls.size();
		 * i++) { Rect rect = (Rect) leftWalls.get(i); if (speedX > 0 &&
		 * playerRect.right < rect.left-5 && yPos+height-10 > rect.top &&
		 * intersectPlayer(rect)){ speedX = 0; xPos = rect.left-width-1; } }
		 * 
		 * for (int i = 0; i < rightWalls.size(); i++) { Rect rect = (Rect)
		 * rightWalls.get(i); if (yPos+height-10 > rect.top &&
		 * intersectPlayer(rect)){ speedX = 0; xPos = rect.right+1; } }
		 * 
		 * for (int i = 0; i < ceils.size(); i++) { Rect rect = (Rect)
		 * ceils.get(i); if (intersectPlayer(rect)){ speedY = 0; yPos =
		 * rect.bottom; } }
		 */
		for (int i = 0; i < floors.size(); i++) {
			Rect rect = (Rect) floors.get(i);
			if (speedY > 0 && playerRect.bottom - speedY <= rect.top
					&& intersectPlayer(rect)) {
				if (speedY > 0)
					speedY = 0;
				jumped = false;
				yPos = rect.top - height;
			}
		}
		
		if (GameView.player1.defend != null && GameView.player1.defend.isVisible()){
			Rect defRect = GameView.player1.defend.getRect();
			if (intersectPlayer(defRect)){
				if (speedX > 0 && defRect.top < playerRect.bottom - 15)
					xPos = defRect.left - width;
				if (speedX < 0 && defRect.top < playerRect.bottom - 15)
					xPos = defRect.right;
				if (speedY > 1 && defRect.top > playerRect.bottom - 15){
					yPos = defRect.top - height;
					jumped = false;
					if (speedY > 0)
						speedY = 0;
				}
			}
		}
	}

	public void moveLeft() {
		direction = -1;
		speedX = -movingSpeed;
	}

	public void moveRight() {
		direction = 1;
		speedX = movingSpeed;
	}
	
	public void moveTo(int x, int y){
		xPos = x;
		yPos = y;
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
		if (cooldown <= 0){
			attack.add(new Attack(xPos + width/2, yPos + 10, 20, 10, direction));
			cooldown = attackCooldown;
			lastAttackFrameNumber = frameNumber;
		}
		
	}
	
	public void defend(){
		if (!jumped)
			defend = new Defend(xPos + width/4, yPos + height, 20, 80, direction);
			lastDefendFrameNumber = frameNumber;
	}
	
	public void hit(int dir){
		//got hit
		health --;
		xPos = xPos + 2 * dir;
		
	}

	public Rect getRect() {
		Rect rect = new Rect(xPos, yPos, xPos + width, yPos + height);
		return rect;
	}
	
	public int getHealth(){
		return health;
	}

	public int getWidth(){
		return width;
	}
	
	public boolean isVisible(){
		return visible;
	}
	
	public Bitmap getBitmap(){
		Bitmap bmp;
		if (frameNumber - lastAttackFrameNumber < 10){
			bmp = b_attack;
		}
		else if (jumped){
			bmp = b_jumping;
		}
		
		else if (frameNumber - lastDefendFrameNumber < 10){
			bmp = b_defend;
		}
		else if (speedX == 0){
			bmp = b_standing;
		}
		else {
			bmp = b_running.get((frameNumber % (b_running.size()*b_runningDelay))/b_runningDelay);
		}
		if (direction == -1){
			return (GameView.flipBitmap(bmp));
		}
		return (bmp);
	}

	public Bitmap getB_standing() {
		return b_standing;
	}

	//Images getters and setters
	public void setB_standing(Bitmap b_standing) {
		this.b_standing = b_standing;
	}

	public Bitmap getB_jumping() {
		return b_jumping;
	}

	public void setB_jumping(Bitmap b_jumping) {
		this.b_jumping = b_jumping;
	}

	public Bitmap getB_attack() {
		return b_attack;
	}

	public void setB_attack(Bitmap b_attack) {
		this.b_attack = b_attack;
	}

	public Bitmap getB_defend() {
		return b_defend;
	}

	public void setB_defend(Bitmap b_defend) {
		this.b_defend = b_defend;
	}
}
