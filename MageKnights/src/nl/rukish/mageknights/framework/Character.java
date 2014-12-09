package nl.rukish.mageknights.framework;

import java.util.ArrayList;
import java.util.List;

import nl.rukish.mageknights.DefendAttack;
import nl.rukish.mageknights.GameView;
import nl.rukish.mageknights.RangedAttack;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class Character {
	// Properties
	private int width, height;
	private int health;
	
	// Powers
	protected int movingSpeed;
	protected int jumpingPower;
	protected int attackDelay;
	protected int defendDelay;
	
	// Control variables
	private int xPos;
	private int yPos;
	private int speedX, speedY;
	private boolean jumped;
	
	 // 1 is right -1 is left
	private int direction;
	
	//time left until a character can do another attack/defend
	private int attackCooldown;
	private int defendCooldown;

	// list of attacks
	private List<Attack> attack;
	private Attack defend;
	
	// visual
	protected Bitmap b_standing, b_jumping, b_attack, b_defend, b_bullet, b_wall, b_dead;
	protected List<Bitmap> b_running = new ArrayList<Bitmap>();
	private int frameNumber, lastAttackFrameNumber, lastDefendFrameNumber;
	private int b_runningDelay = 10;
	//visibility of the character
	private boolean visible;

	public Character(int xPos, int yPos, int width, int height) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		
		// default powers
		movingSpeed = 5; 
		jumpingPower = 17;
		attackDelay = 30;
		
		// init values
		visible = true;
		jumped = false;
		direction = 1;
		health = 1;
		attack = new ArrayList<Attack>();
		
		// graphic stuff
		frameNumber = 0;
		attackCooldown = 0;
		lastAttackFrameNumber = 0;
		lastDefendFrameNumber = 0;
	}

	public void update() {
		//current framenumber
		frameNumber++; 
		
		// update attack stuff
		attackCooldown--;
		for (int i = 0; i < attack.size(); i++) {
			Attack att = (Attack) attack.get(i);
			if (att.isVisible())
				att.update();
			else
				attack.remove(i);
		}
		if (defend.isVisible()){
			defend.update();
		}
		
		// update character
		speedY += 1; //gravity
		xPos += speedX;
		yPos += speedY;
		
		// let player walk on platforms
		checkMapCollisions();
		// keeps player inside screen
		checkScreenCollisions(); 
		// prohibits jumping when falling
		if (speedY > 3) {
			jumped = true;
		}

	}

	private void checkScreenCollisions() {
		// left map bound
		if (xPos < 0) {
			xPos = 0;
			// speedX = 0;
		}
		// right map bound
		if (xPos > GameMap.getMapWidth() - width) {
			xPos = GameMap.getMapWidth() - width;
			// speedX = 0;
		}
		// up map bound
		if (yPos < 0) {
			yPos = 0;
			speedY = 0;
		}
		// down map bound (we use a floor instead)
		// if (yPos > currentMap.height-height){
		// yPos = currentMap.height-height;
		// speedY = 0;
		// }
	}

	// returns a true is a rectangle intersects this object
	public boolean intersectCharacter(Rect r) {
		Rect playerRect = getRect();
		boolean noCollision = (playerRect.right < r.left
				|| playerRect.left > r.right || playerRect.top > r.bottom || playerRect.bottom < r.top);
		return !noCollision;
	}

	private void checkMapCollisions() {
		List<Rect> walls = GameMap.getWalls();
		List<Rect> floors = GameMap.getFloors();
		List<Rect> ceils = GameMap.getCeils();

		// rectangle of this character
		Rect characterRect = getRect(); 
		
		// if we land on a floor
		for (int i = 0; i < floors.size(); i++) {
			Rect floor = (Rect) floors.get(i);
			if (speedY > 0 //We are falling 
				&& characterRect.bottom - speedY <= floor.top //the bottom from our char is already inside the top of the floor
				&& intersectCharacter(floor)) //our character intersects the floor
			{
				if (speedY > 0) // reset falling speed to 0
					speedY = 0;
				jumped = false; // we can now jump again
				yPos = floor.top - height; // reset the yPos of our character
			}
		}
		
		// character cannot walk through defend blocks
		if (GameView.player1.defend != null && GameView.player1.defend.isVisible()){
			Rect defRect = GameView.player1.defend.getRect();
			if (intersectCharacter(defRect)){
				if (speedX > 0 && defRect.top < characterRect.bottom - 15)
					xPos = defRect.left - width;
				if (speedX < 0 && defRect.top < characterRect.bottom - 15)
					xPos = defRect.right;
				/* // lets users stand on a defend block
				if (speedY > 1 && defRect.top > characterRect.bottom - 15){
					yPos = defRect.top - height;
					jumped = false;
					if (speedY > 0)
						speedY = 0;
				}
				*/
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
		if (attackCooldown <= 0){
			attack.add(new RangedAttack(xPos + width/2, yPos + height / 2, direction));
			attackCooldown = attackDelay;
			lastAttackFrameNumber = frameNumber;
		}
		
	}
	
	public void defend(){
		defend = new DefendAttack(xPos + width/2, yPos + height, direction);
		lastDefendFrameNumber = frameNumber;
	}
	
	public void hit(int dir){
		//got hit
		health --;
		xPos = xPos + 2 * dir;
	}
	
	public void onShake(){
		mirrorTeleport();
	}
	
	public void mirrorTeleport(){
		xPos = GameMap.getMapWidth() - (xPos + width/2);
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


	//Images getters and setters
	public Bitmap getB_standing() {
		return b_standing;
	}
	
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
