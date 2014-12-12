package nl.rukish.mageknights;

import nl.rukish.mageknights.framework.Character;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;

public class Player extends Character {
	public static final int PLAYER_HEALTH = 5;
	public static final int PLAYER_WIDTH = 40;
	public static final int PLAYER_HEIGHT = 60;
	
	public Player(int xPos, int yPos) {
		super(xPos, yPos, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_HEALTH);
		// TODO Auto-generated constructor stub
	}

	public void parseSpritesheet(Bitmap spriteSheet){
		setB_standing(Bitmap.createBitmap(spriteSheet, 60, 0, 15, 35));
		addB_running(Bitmap.createBitmap(spriteSheet, 65, 40, 30, 30));
		addB_running(Bitmap.createBitmap(spriteSheet, 95, 40, 30, 30));
		addB_running(Bitmap.createBitmap(spriteSheet, 125, 40, 30, 30));
		setB_jumping(Bitmap.createBitmap(spriteSheet, 130, 80, 20, 40));
		setB_attack(Bitmap.createBitmap(spriteSheet, 130, 180, 31, 36));
		setB_defend(Bitmap.createBitmap(spriteSheet, 182, 473, 28, 32));
		setB_bullet(Bitmap.createBitmap(spriteSheet, 315, 250, 25, 15));
		setB_wall(Bitmap.createBitmap(spriteSheet, 207, 120, 32, 48));
		setB_dead(Bitmap.createBitmap(spriteSheet, 158, 961, 34, 19));
	}
	
	
	public void parseSounds(Context context){

		setSndBlock(context, R.raw.block);
		setSndHit(context, R.raw.hit2);
		setSndShot(context, R.raw.shot2);
		setSndDead(context, R.raw.dead);
	}
}
