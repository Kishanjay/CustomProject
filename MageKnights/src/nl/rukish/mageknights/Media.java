package nl.rukish.mageknights;

import android.media.MediaPlayer;

public class Media {
	
	public static MediaPlayer menu, gameover;
	
	public Media(MainMenu mainMenu) {
		menu = MediaPlayer.create(mainMenu, R.raw.menu);
		menu.setLooping(true);
		menu.start();
		
		gameover = MediaPlayer.create(mainMenu, R.raw.lost);
		gameover.setLooping(true);
		
	}

}
