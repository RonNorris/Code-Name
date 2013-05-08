package com.asgard.game.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * A database helper for Block Type Table
 * 
 * @author Benjamin
 */
public class BlockTypeTable {

	/* Database name and column names */
	public static final String TABLE_BLOCK_TYPE = "block_type";
	public static final String COLUMN_ID = "block_type_id";
	public static final String COLUMN_HARDNESS = "hardness";
	public static final String COLUMN_CARGO_ID = "_cargo_id";

	/* Statement for creating table */
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_BLOCK_TYPE + "(" + COLUMN_ID
			+ " integer primary key autoincrement," + COLUMN_HARDNESS
			+ " real not null," + COLUMN_CARGO_ID
			+ " integer not null, foreign key (" + COLUMN_CARGO_ID
			+ ") references " + CargoItemTable.TABLE_CARGO_ITEM + " ("
			+ CargoItemTable.COLUMN_ID + "));";

	/* Create the BlockType table */
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	/* Update the BlockType table */
	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(BlockTypeTable.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destory all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_BLOCK_TYPE);
		onCreate(database);

	}
}
