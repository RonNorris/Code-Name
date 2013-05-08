package com.asgard.game.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * The SQL layout of Blocks
 * 
 * @author Benjamin
 * 
 */
public class BlockTable {

	// Databse constants
	public static final String TABLE_BLOCK = "block";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_BLOCK_TYPE = "block_type";
	public static final String COLUMN_X = "x";
	public static final String COLUMN_Y = "y";
	public static final String COLUMN_GRID = "grid";

	// SQL statement to create the database
	private static final String DATABASE_CREATE = "create table " + TABLE_BLOCK
			+ "(" + COLUMN_ID + " integer primary key autoincrement,"
			+ COLUMN_BLOCK_TYPE + " integer not null," + COLUMN_GRID
			+ " integer not null," + COLUMN_X + " integer not null," + COLUMN_Y
			+ " integer not null," + " foreign key (" + COLUMN_BLOCK_TYPE
			+ ") references " + BlockTypeTable.TABLE_BLOCK_TYPE + " ("
			+ BlockTypeTable.COLUMN_ID + "), foreign key (" + COLUMN_GRID
			+ ") references " + GridTable.TABLE_GRID + " ("
			+ GridTable.COLUMN_ID + "));";

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
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_BLOCK);
		onCreate(database);

	}
}
