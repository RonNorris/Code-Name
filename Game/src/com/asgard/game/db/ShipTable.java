package com.asgard.game.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ShipTable {

	// Database constants
	public static final String TABLE_SHIP = "ship";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_SPEED = "speed";
	public static final String COLUMN_PLANE = "plane";
	public static final String COLUMN_X = "x";
	public static final String COLUMN_Y = "y";
	public static final String COLUMN_EXP = "exp";

	// SQL statement to create the database
	private static final String DATABASE_CREATE = "create table " + TABLE_SHIP
			+ "(" + COLUMN_ID + " integer primary key autoincrement,"
			+ COLUMN_SPEED + " float not null," + COLUMN_PLANE
			+ " integer not null," + COLUMN_X + " integer not null," + COLUMN_Y
			+ " integer not null," + COLUMN_EXP + " integer not null);";
			
	// Creates db
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	// Updates db to newest version, deleting all old data
	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(BlockTypeTable.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destory all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_SHIP);
		onCreate(database);

	}
}
