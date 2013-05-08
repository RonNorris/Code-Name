package com.asgard.game.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Responsible for managing creation and updating of the database. Individual
 * access for each table can be found in the respective helper classes.
 * 
 * @author Benjamin
 * 
 */
public class GameDatabaseHelper extends SQLiteOpenHelper {

	public GameDatabaseHelper(Context context) {
		super(context, GameDatabaseSchema.DATABASE_NAME, null,
				GameDatabaseSchema.DATABASE_VERSION);
	}

	/* Call the create statements for each table in the correct order */
	@Override
	public void onCreate(SQLiteDatabase database) {
		CargoItemTable.onCreate(database);
		BlockTypeTable.onCreate(database);
		GridTable.onCreate(database);
		BlockTable.onCreate(database);
		ShipTable.onCreate(database);
		CargoHoldTable.onCreate(database);
		CraftItemTable.onCreate(database);
	}

	/* Calls the upgrade statements for each table in correct order */
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		CargoItemTable.onUpgrade(database, oldVersion, newVersion);
		BlockTypeTable.onUpgrade(database, oldVersion, newVersion);
		GridTable.onUpgrade(database, oldVersion, newVersion);
		BlockTable.onUpgrade(database, oldVersion, newVersion);
		ShipTable.onUpgrade(database, oldVersion, newVersion);
		CargoHoldTable.onUpgrade(database, oldVersion, newVersion);
		CraftItemTable.onUpgrade(database, oldVersion, newVersion);
	}
}
