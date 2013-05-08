package com.asgard.game.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.asgard.game.models.Point;
import com.asgard.game.models.Ship;

public class ShipDataSource extends DataSource {

	public ShipDataSource(Context context) {
		super(context);
	}

	/**
	 * CRUD operations
	 */
	/* Adds the ship to the database */
	public void add(Ship ship) {
		// Create the values
		ContentValues cv = new ContentValues();
		cv.put(ShipTable.COLUMN_EXP, ship.getExperience());
		cv.put(ShipTable.COLUMN_X, ship.getLocation().x);
		cv.put(ShipTable.COLUMN_Y, ship.getLocation().y);
		cv.put(ShipTable.COLUMN_PLANE, ship.getCurrentPlane());
		cv.put(ShipTable.COLUMN_SPEED, ship.getSpeed());

		mDatabase.insert(ShipTable.TABLE_SHIP, null, cv);
	}

	/* Load the ship */
	public Ship getShip() {
		String selectQuery = "SELECT * FROM " + ShipTable.TABLE_SHIP;
		Cursor cursor = mDatabase.rawQuery(selectQuery, null);
		Ship ship = new Ship();
		if (cursor.moveToFirst()) {
			ship.setSpeed(cursor.getFloat(cursor
					.getColumnIndex(ShipTable.COLUMN_SPEED)));
			ship.setCurrentPlane(cursor.getInt(cursor
					.getColumnIndex(ShipTable.COLUMN_PLANE)));
			ship.setExperience(cursor.getInt(cursor
					.getColumnIndex(ShipTable.COLUMN_EXP)));
			ship.setID(cursor.getInt(cursor.getColumnIndex(ShipTable.COLUMN_ID)));
			ship.setLocation(new Point(cursor.getInt(cursor
					.getColumnIndex(ShipTable.COLUMN_X)), cursor.getInt(cursor
					.getColumnIndex(ShipTable.COLUMN_Y))));
		}
		return ship;
	}

	/* Update the ship */
	public int update(Ship ship) {
		// Create the values
		ContentValues cv = new ContentValues();
		cv.put(ShipTable.COLUMN_EXP, ship.getExperience());
		cv.put(ShipTable.COLUMN_X, ship.getLocation().x);
		cv.put(ShipTable.COLUMN_Y, ship.getLocation().y);
		cv.put(ShipTable.COLUMN_PLANE, ship.getCurrentPlane());
		cv.put(ShipTable.COLUMN_SPEED, ship.getSpeed());

		return mDatabase.update(ShipTable.TABLE_SHIP, cv, ShipTable.COLUMN_ID
				+ "=?", new String[] { String.valueOf(ship.getID()) });
	}
}
