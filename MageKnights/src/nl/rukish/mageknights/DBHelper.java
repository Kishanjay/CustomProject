package nl.rukish.mageknights;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "MageKnights.db";
	public static final String SCORE_TABLE = "scores";

	public DBHelper(Context context) {
		// TODO Auto-generated constructor stub
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table scores "
				+ "(id integer primary key, name text, score integer)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public boolean insertScore(String name, int score) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();

		contentValues.put("name", name);
		contentValues.put("score", score);

		db.insert("scores", null, contentValues);
		return true;
	}
	
	public void clearDatabase(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE * FROM scores");
	}

	public Cursor getData() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor result = db.rawQuery("SELECT * FROM scores ORDER BY score DESC",
				null);
		return result;
	}
	
	public int getHighscore() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor result = db.rawQuery("SELECT score FROM scores ORDER BY score DESC LIMIT 1", null);
		result.moveToFirst();
		//if there is no score
		if (result.isAfterLast())
			return 0;
		//return highest score
		return result.getInt(0);
	}

}
