package com.asgard.game.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.asgard.game.models.CargoItem;

/**
 * The datasource for Cargo Items, used for CRUD operations.
 * 
 * @author Benjamin
 * @see CargoItemTable
 */
public class CargoItemDataSource extends DataSource {

	public static final String[] ALL_COLUMNS = { CargoItemTable.COLUMN_ID,
			CargoItemTable.COLUMN_DESCRIPTION, CargoItemTable.COLUMN_RES_ID };

	public CargoItemDataSource(Context context) {
		super(context);
	}

	/**
	 * CRUD operations
	 */
	// Add the cargo item to the database
	public long add(CargoItem item) {
		// Get data
		ContentValues cv = new ContentValues();
		cv.put(CargoItemTable.COLUMN_DESCRIPTION, item.getDescription());
		cv.put(CargoItemTable.COLUMN_RES_ID, item.getResID());

		// Add to the database
		return mDatabase.insert(CargoItemTable.TABLE_CARGO_ITEM, null, cv);
	}

	/* Retrieve the information in a CargoItem object */
	public CargoItem get(long id, Context context) {
		// Query all items for matching id
		Cursor cursor = mDatabase.query(CargoItemTable.TABLE_CARGO_ITEM,
				ALL_COLUMNS, CargoItemTable.COLUMN_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null);

		CargoItem item = null;
		
		// If something was returned
		if (cursor.moveToFirst()) {
			item = new CargoItem();
			// Set the ID, resID and description
			item.setID(cursor.getInt(cursor
					.getColumnIndex(CargoItemTable.COLUMN_ID)));
			item.setResID(cursor.getInt(cursor
					.getColumnIndex(CargoItemTable.COLUMN_RES_ID)));
			item.setDescription(cursor.getString(cursor
					.getColumnIndex(CargoItemTable.COLUMN_DESCRIPTION)));
			
			// Load the bitmap
			item.setSprite(context.getResources());
		}

		return item;
	}
	
	/* Retrieve the information in a CargoItem object */
	public CargoItem get(String description, Context context) {
		// Query all items for matching description
		Cursor cursor = mDatabase.query(CargoItemTable.TABLE_CARGO_ITEM,
				ALL_COLUMNS, CargoItemTable.COLUMN_DESCRIPTION + "=?",
				new String[] { description }, null, null, null);

		CargoItem item = null;
		
		// If something was returned
		if (cursor.moveToFirst()) {
			item = new CargoItem();
			// Set the ID, resID and description
			item.setID(cursor.getInt(cursor
					.getColumnIndex(CargoItemTable.COLUMN_ID)));
			item.setResID(cursor.getInt(cursor
					.getColumnIndex(CargoItemTable.COLUMN_RES_ID)));
			item.setDescription(cursor.getString(cursor
					.getColumnIndex(CargoItemTable.COLUMN_DESCRIPTION)));
			
			// Load the bitmap
			item.setSprite(context.getResources());
		}

		return item;
	}
}
