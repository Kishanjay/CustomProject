package nl.rukish.mageknights;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "MageKnights.db";
	public static final String SCORE_TABLE = "mk_scores";
	public static final String NAME_COLUMN = "name";
	public static final String SCORE_COLUMN = "score";

	public DBHelper(Context context) {
		// TODO Auto-generated constructor stub
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table "+SCORE_TABLE+" "
				+ "(id integer primary key, "+NAME_COLUMN+" text, "+SCORE_COLUMN+" integer)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public boolean insertScore(String name, int score) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();

		contentValues.put(NAME_COLUMN, name);
		contentValues.put(SCORE_COLUMN, score);

		db.insert(SCORE_TABLE, null, contentValues);
		return true;
	}
	
	public void clearDatabase(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM "+SCORE_TABLE);
	}

	public Cursor getData() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor result = db.rawQuery("SELECT * FROM "+SCORE_TABLE+" ORDER BY "+SCORE_COLUMN+" DESC LIMIT 15",
				null);
		return result;
	}
	
	public int getHighscore() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor result = db.rawQuery("SELECT "+SCORE_COLUMN+" FROM "+SCORE_TABLE+" ORDER BY "+SCORE_COLUMN+" DESC LIMIT 1", null);
		result.moveToFirst();
		//if there is no score
		if (result.isAfterLast())
			return 0;
		//return highest score
		return result.getInt(0);
	}

}
