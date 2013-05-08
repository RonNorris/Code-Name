package com.asgard.game.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * The CargoItem table represents all of the common elements of in game items,
 * including a description and the id of the associated Bitmap resource
 * 
 * @author Benjamin
 * 
 */
public class CargoItemTable {

	/* Database name and column names */
	public static final String TABLE_CARGO_ITEM = "cargo_item";
	public static final String COLUMN_ID = "cargo_item_id";
	public static final String COLUMN_DESCRIPTION = "name";
	public static final String COLUMN_RES_ID = "res_id";

	/* Statement for creating table */
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_CARGO_ITEM + "(" + COLUMN_ID
			+ " integer primary key autoincrement," + COLUMN_DESCRIPTION
			+ " text not null," + COLUMN_RES_ID + " integer not null);";

	/* Create the CargoItem table */
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	/* Update the CargoItem table */
	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(BlockTypeTable.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destory all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_CARGO_ITEM);
		onCreate(database);

	}
}
