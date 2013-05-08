package com.asgard.game.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Describes how to create and update the CraftItem table
 * 
 * @author Benjamin
 * 
 */
public class CraftItemTable {

	/* Database name and column names */
	public static final String TABLE_CRAFT_ITEM = "craft_item";
	public static final String COLUMN_CARGO_ID = "_cargo_id"; //cargo item that this craft item inherits from
	public static final String COLUMN_COST = "_cost"; //id of the cargo item that makes this craft item
	public static final String COLUMN_EXP = "experience"; //amount of xp given when crafted
	public static final String COLUMN_LEVEL = "level"; //necessary level to craft
	public static final String COLUMN_ID = "craft_item_id"; //if id of this item in the craft table (smaller than cargo table)

	/* Statement for creating table */
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_CRAFT_ITEM + "(" + COLUMN_ID
			+ " integer primary key autoincrement," + COLUMN_CARGO_ID
			+ " integer not null," + COLUMN_LEVEL + " integer not null,"
			+ COLUMN_EXP + " integer not null," + COLUMN_COST
			+ " integer not null," + " foreign key (" + COLUMN_CARGO_ID
			+ ") references " + CargoItemTable.TABLE_CARGO_ITEM + " ("
			+ CargoItemTable.COLUMN_ID + "), foreign key (" + COLUMN_COST + ") references "
			+ CargoItemTable.TABLE_CARGO_ITEM + "(" + CargoItemTable.COLUMN_ID
			+ "));";

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
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_CRAFT_ITEM);
		onCreate(database);

	}

}
