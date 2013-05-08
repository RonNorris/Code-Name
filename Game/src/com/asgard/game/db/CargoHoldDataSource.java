package com.asgard.game.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.asgard.game.models.CargoHold;
import com.asgard.game.models.CargoItem;

public class CargoHoldDataSource extends DataSource {

	public static final String[] ALL_COLUMNS = {
			CargoHoldTable.COLUMN_CARGO_ITEM, CargoHoldTable.COLUMN_ID,
			CargoHoldTable.COLUMN_NUMBER };

	// Used for cursor creation when displaying the database
	public static final String INVENTORY_SELECTION = "SELECT "
			+ CargoHoldTable.TABLE_CARGO_HOLD + "."
			+ CargoHoldTable.COLUMN_NUMBER + ", "
			+ CargoItemTable.TABLE_CARGO_ITEM + "."
			+ CargoItemTable.COLUMN_RES_ID + " AS _id FROM "
			+ CargoHoldTable.TABLE_CARGO_HOLD + ", "
			+ CargoItemTable.TABLE_CARGO_ITEM + " WHERE "
			+ CargoHoldTable.TABLE_CARGO_HOLD + "."
			+ CargoHoldTable.COLUMN_CARGO_ITEM + " = "
			+ CargoItemTable.TABLE_CARGO_ITEM + "." + CargoItemTable.COLUMN_ID;

	public CargoHoldDataSource(Context context) {
		super(context);
	}

	/**
	 * CRUD operations
	 */
	/* Add a single CargoItem to the database */
	public long add(CargoItem item) {

		// Check if there is already an item in the database
		Cursor exists = mDatabase
				.query(CargoHoldTable.TABLE_CARGO_HOLD, ALL_COLUMNS,
						CargoHoldTable.COLUMN_CARGO_ITEM + "=?",
						new String[] { String.valueOf(item.getID()) }, null,
						null, null);
		
		// Content values for storing changes
		ContentValues values = new ContentValues();
		values.put(CargoHoldTable.COLUMN_CARGO_ITEM, item.getID());

		// If table already has entry,
		if (exists.moveToFirst()) {
			// Get the old number and increase it by one
			int oldNum = exists.getInt(exists
					.getColumnIndex(CargoHoldTable.COLUMN_NUMBER));
			values.put(CargoHoldTable.COLUMN_NUMBER, ++oldNum);

			// Try the update, checking the result
			return mDatabase.update(CargoHoldTable.TABLE_CARGO_HOLD,
					values, CargoHoldTable.COLUMN_ID + "=?", new String[] {String.valueOf(item.getID())});
			
		} else {
			values.put(CargoHoldTable.COLUMN_NUMBER, 1);
			return mDatabase.insert(CargoHoldTable.TABLE_CARGO_HOLD, null,
					values);
		}
	}

	/* Retrieves the necessary columns for displaying the current inventory */
	public Cursor getInventory() {

		return mDatabase.rawQuery(INVENTORY_SELECTION, null);
	}

	/* Returns the total number of items in the cargo */
	public int getTotal() {
		String select = "SELECT * FROM " + CargoHoldTable.TABLE_CARGO_HOLD;
		return mDatabase.rawQuery(select, null).getCount();

	}

	/* Returns every cargo item in the database */
	public CargoHold getAll(Context context, int cargoHoldSize) { 

		String select = "SELECT * FROM " + CargoHoldTable.TABLE_CARGO_HOLD;

		// Retrieve every cargo item
		Cursor cursor = mDatabase.rawQuery(select, null);

		CargoHold hold = new CargoHold(cargoHoldSize);

		if (cursor.moveToFirst()) {
			do {
				// Get the associated cargo item
				CargoItemDataSource cargoDataSource = new CargoItemDataSource(
						context);
				cargoDataSource.open();
				CargoItem item = cargoDataSource.get(cursor.getLong(cursor
						.getColumnIndex(CargoHoldTable.COLUMN_CARGO_ITEM)),
						context);
				cargoDataSource.close();

				hold.addItem(item, cursor.getInt(cursor
						.getColumnIndex(CargoHoldTable.COLUMN_NUMBER)));

			} while (cursor.moveToNext());
		}
		return hold;
	}

}
