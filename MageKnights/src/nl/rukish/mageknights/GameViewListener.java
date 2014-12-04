package nl.rukish.mageknights;

public interface GameViewListener {
	public void onSubmitScore(int score);
	
	public void onStopGame();
	
	public String getUserName();
	
	public int getHighScore();
}
