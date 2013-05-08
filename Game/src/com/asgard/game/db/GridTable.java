package com.asgard.game.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 
 * @author Benjamin
 * 
 *         Helper class for creating and updating Grid Table
 */
public class GridTable {

	// Database info
	public static final String TABLE_GRID = "grid";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_PLANE = "plane";

	// SQL statement to create the database
	private static final String DATABASE_CREATE = "create table " + TABLE_GRID
			+ "(" + COLUMN_ID + " integer primary key autoincrement,"
			+ COLUMN_PLANE + " integer not null);";

	// Creates the database
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	// Updates database to newest version, deleting all old data
	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(BlockTypeTable.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destory all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_GRID);
		onCreate(database);
	}
}
