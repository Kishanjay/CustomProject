package nl.rukish.mageknights;

public class Highscore {
	private int index;
	private String name;
	private int score;
	
	public Highscore(int index, String name, int score) {
		super();
		this.index = index;
		this.name = name;
		this.score = score;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
