package nl.rukish.mageknights.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nl.rukish.mageknights.DefendAttack;
import nl.rukish.mageknights.GameView;
import nl.rukish.mageknights.RangedAttack;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class Character {
	// Properties
	private int width, height;
	private int totalHealth;
	
	// Powers
	protected int movingSpeed;
	protected int jumpingPower;
	protected int attackDelay;
	protected int defendDelay;
	
	// Control variables
	private int xPos; //left
	private int yPos; //bottom
	private int speedX, speedY;
	private boolean jumped;
	private int currentHealth;
	
	 // 1 is right -1 is left
	private int direction;
	
	//time left until a character can do another attack/defend
	protected int attackCooldown;
	private int defendCooldown;

	// list of attacks
	private List<Attack> attack;
	private Attack defend;
	
	// visual
	protected Bitmap b_standing, b_jumping, b_attack, b_defend;
	private Bitmap b_bullet;
	private Bitmap b_wall;
	private Bitmap b_dead;
	protected List<Bitmap> b_running = new ArrayList<Bitmap>();
	private int frameNumber, lastAttackFrameNumber, lastDefendFrameNumber;
	private int b_runningDelay = 10;
	//visibility of the character
	private boolean visible;

	
	public Character(int xPos, int yPos, int width, int height, int health) {
		this.width = width;
		this.height = height;
		this.totalHealth = health;
		
		// default powers
		movingSpeed = 5; 
		jumpingPower = 14;
		attackDelay = 30;
		
		// init values
		jumped = false;
		direction = 1;
		attack = new ArrayList<Attack>();
		
		// graphic stuff
		frameNumber = 0;
		attackCooldown = 0;
		lastAttackFrameNumber = 0;
		lastDefendFrameNumber = 0;
		
		spawn(xPos, yPos);
	}

	public void spawn() {
		spawn(GameMap.getRandomX(), 0);
	}
	
	public void spawn(int xPos, int yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
		currentHealth = totalHealth;
		visible = true;
		jumped = false;
	}
	
	public void upgrade() {
		if (attackDelay > 5){
			attackDelay--;
		}
		else if (movingSpeed < 10){
			movingSpeed++;
		}
		else {
			totalHealth++;
		}
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
		
		if (defend != null && defend.isVisible()){
			defend.update();
		}
		
		// update character
		speedY += 1; //gravity
		setxPos(getxPos() + speedX);
		setyPos(getyPos() + speedY);
		
		// prohibits jumping when falling
		if (speedY > 3) {
			jumped = true;
		}

		// let player walk on platforms
		checkMapCollisions();
		// keeps player inside screen
		checkScreenCollisions();
		
		//check if player had died
		if (isDead()){
			speedX = 0;
		}
	}

	private void checkScreenCollisions() {
		// left map bound
		if (getxPos() < 0) {
			setxPos(0);
			// speedX = 0;
		}
		// right map bound
		if (getxPos() > GameMap.getMapWidth() - getWidth()) {
			setxPos(GameMap.getMapWidth() - getWidth());
			// speedX = 0;
		}
		// up map bound
		if (getyPos()-getHeight() < 0) {
			setyPos(0 + getHeight());
			speedY = 0;
		}
		
		//down map bound (we also use a floor)
		if (yPos > GameMap.getMapHeight()){
			yPos = GameMap.getMapHeight();
			speedY = 0;
		}
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
				&& characterRect.bottom - speedY - 10 <= floor.top // the character is comming from above the floor
				&& intersectCharacter(floor)) //our character intersects the floor
			{
				if (speedY > 0) // reset falling speed to 0
					speedY = 0;
				jumped = false; // we can now jump again
				setyPos(floor.top); // reset the yPos of our character
			}
		}
		
		// character cannot walk through defend blocks
		Character player1 = GameView.getPlayer1();
		Attack defend = player1.getDefend();
		if (defend != null && defend.isVisible()){
			Rect defRect = defend.getRect();
			if (intersectCharacter(defRect)){
				if (speedX > 0 && defRect.top < characterRect.bottom - 15)
					setxPos(defRect.left - width);
				if (speedX < 0 && defRect.top < characterRect.bottom - 15)
					setxPos(defRect.right);
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
		if (!isDead()){
			direction = -1;
			speedX = -movingSpeed;
		}
	}

	public void moveRight() {
		if (!isDead()){
			direction = 1;
			speedX = movingSpeed;
		}
	}
	
	public void moveTo(int x, int y){
		setxPos(x);
		setyPos(y);
	}

	public void stop() {
		speedX = 0;
	}

	public void jump() {
		if (jumped == false && !isDead()) {
			speedY = -jumpingPower;
			jumped = true;
		}
	}

	public void attack() {
		if (attackCooldown <= 0 && !isDead()){
			attack.add(new RangedAttack(getxPos() + getWidth()/2, getyPos() - getHeight() / 2, direction, hashCode()));
			attackCooldown = attackDelay;
			lastAttackFrameNumber = frameNumber;
		}
		
	}
	
	public void defend(){
		if (!isDead()){
			defend = new DefendAttack(getxPos() + getWidth()/2, getyPos(), direction, hashCode());
			lastDefendFrameNumber = frameNumber;
		}
	}
	
	public void hit(int dir){
		//got hit
		currentHealth --;
		setxPos(getxPos() + 2 * dir);
	}
	
	public void onShake(){
		if (!isDead()){
			mirrorTeleport();
		}
	}
	
	public void mirrorTeleport(){
		setxPos(GameMap.getMapWidth() - (xPos + width/2));
		direction *=-1; //face the otherway
	}

	public Rect getRect() {
		//Rect imgRect = getBitmap();
		Rect rect = new Rect(xPos, yPos - getHeight(), xPos + getWidth(), yPos);
		return rect;
	}

	public int getWidth(){
		return getBitmap().getWidth();
	}
	
	public int getHeight(){
		return getBitmap().getHeight();
	}

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

	public int getHealth(){
		return currentHealth;
	}
	
	public void setHealth(int health){
		this.currentHealth = health;
	}
	
	public Attack getDefend(){
		return defend;
	}
	
	public List<Attack> getAttack(){
		return attack;
	}
	
	public boolean isDead(){
		return currentHealth <= 0;
	}
	
	public boolean isVisible(){
		return visible;
	}
	
	public void setVisible(boolean val){
		visible = val;
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
		if (isDead()){
			bmp = b_dead;
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
	
	public void addB_running(Bitmap b_running) {
		this.b_running.add(b_running);
	}

	public Bitmap getB_bullet() {
		return b_bullet;
	}

	public void setB_bullet(Bitmap b_bullet) {
		this.b_bullet = b_bullet;
	}

	public Bitmap getB_wall() {
		return b_wall;
	}

	public void setB_wall(Bitmap b_wall) {
		this.b_wall = b_wall;
	}

	public Bitmap getB_dead() {
		return b_dead;
	}

	public void setB_dead(Bitmap b_dead) {
		this.b_dead = b_dead;
	}

}
