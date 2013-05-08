package com.asgard.game.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/**
 * Superclass representation of any data source
 * 
 * @author Benjamin
 *
 */
public class DataSource {

	protected GameDatabaseHelper mHelper;
	protected SQLiteDatabase mDatabase;
	
	public DataSource(Context context) {
		mHelper = new GameDatabaseHelper(context);
	}
	
	public void open() throws SQLiteException {
		mDatabase = mHelper.getWritableDatabase();
	}
	
	public void close() {
		if (mDatabase.isOpen()) {
			mDatabase.close();
		}
	}
	
}
