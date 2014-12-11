package nl.rukish.mageknights;

import nl.rukish.mageknights.framework.Attack;
import nl.rukish.mageknights.framework.GameMap;
import nl.rukish.mageknights.framework.Character;
import java.util.List;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class Enemy extends Character {
	public static final int ENEMY_HEALTH = 3;
	public static final int ENEMY_WIDTH = 40;
	public static final int ENEMY_HEIGHT = 60;
	
	private int  movingFrames, spawnTime;
	private Random rand;

	public Enemy(int xPos, int yPos) {
		super(xPos, yPos, ENEMY_WIDTH, ENEMY_HEIGHT, ENEMY_HEALTH);
		
		rand = new Random();
	}

	public void parseSpritesheet(Bitmap spriteSheet2){
		setB_standing(Bitmap.createBitmap(spriteSheet2, 67, 3, 25, 40));
		addB_running(Bitmap.createBitmap(spriteSheet2, 60, 54, 37, 53));
		addB_running(Bitmap.createBitmap(spriteSheet2, 106, 55, 26, 56));
		addB_running(Bitmap.createBitmap(spriteSheet2, 145, 55, 38, 52));
		setB_jumping(Bitmap.createBitmap(spriteSheet2, 115, 129, 30, 40));
		setB_attack(Bitmap.createBitmap(spriteSheet2, 117, 503, 33, 37));
		setB_bullet(Bitmap.createBitmap(spriteSheet2, 291, 514, 17, 17));
		setB_dead(Bitmap.createBitmap(spriteSheet2, 98, 918, 40, 20));
	}
	
	public void update() {
		//KO SENSOR
		if (getHealth() <= 0){
			//increase the score
			if (isVisible() == true){
				GameView.increaseScore();
				setVisible(false);
			}
			
			//remove all attacks of the enemy
			List<Attack> attack = getAttack();
			if (attack.size() > 0)
				attack.removeAll(attack);
			if (spawnTime > 0){
				spawnTime--;
			}
			else {
				//make the enemy stronger
				upgrade();
				//let him live again
				spawn();
				//reset spawnTime
				spawnTime = 200;
			}
			return;
		}
		
		// Artificial intelligence
		Character player1 = GameView.getPlayer1();
		Rect player1rect = player1.getRect();
		Rect enemyrect = getRect();

		if (rand.nextInt(100) <= 3) {
			if (player1rect.centerX() < enemyrect.centerX())
				moveLeft();
			else if (player1rect.centerX() > enemyrect.centerX())
				moveRight();
			else if (player1rect.centerY() == enemyrect.centerY())
				stop();
			else if (rand.nextInt(100) <= 3)
				moveLeft();
			else
				moveRight();
				
		}

		movingFrames += 1;

		if (movingFrames > 30) {
			stop();
			movingFrames = 0;
		}

		if (rand.nextInt(100) == 1)
			attack();
		if (rand.nextInt(100) == 1)
			jump();
		if (rand.nextInt(100) <= 3) {
			if (player1rect.centerY() <= enemyrect.centerY())
				jump();
			else
				moveLeft();
		}

		// update
		stop();
		attack();
		super.update();
	}

}
