package com.asgard.game.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * The CargoHold table represents every CraftItem that the Ship currently has
 * 
 * @author Benjamin
 * 
 */
public class CargoHoldTable {

	/* Table and column names */
	public static final String TABLE_CARGO_HOLD = "cargo_hold";
	public static final String COLUMN_ID = "cargo_hold_id";
	public static final String COLUMN_NUMBER = "number";
	public static final String COLUMN_CARGO_ITEM = "_cargo_item_id";

	/* Create table statement */
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_CARGO_HOLD + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_CARGO_ITEM
			+ " int not null," + COLUMN_NUMBER + " int not null, foreign key ("
			+ COLUMN_CARGO_ITEM + ") references " + CargoItemTable.TABLE_CARGO_ITEM + "("
			+ CargoItemTable.COLUMN_ID + "));";

	/* Creates the CargoHold table */
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	/* Updates the CargoHold table */
	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(BlockTypeTable.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destory all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_CARGO_HOLD);
		onCreate(database);

	}
}
