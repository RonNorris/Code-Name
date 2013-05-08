package com.asgard.game.db;

import java.util.ArrayList;
import java.util.List;

import com.asgard.game.models.CargoItem;
import com.asgard.game.models.CraftItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class CraftItemDataSource extends CargoItemDataSource {

	public static final String INVENTORY_QUERY = "SELECT "
			+ CargoItemTable.TABLE_CARGO_ITEM + "."
			+ CargoItemTable.COLUMN_RES_ID + ", "
			+ CargoItemTable.TABLE_CARGO_ITEM + "."
			+ CargoItemTable.COLUMN_DESCRIPTION + ", "
			+ CraftItemTable.TABLE_CRAFT_ITEM + "." + CraftItemTable.COLUMN_EXP
			+ ", " + CraftItemTable.TABLE_CRAFT_ITEM + "."
			+ CraftItemTable.COLUMN_LEVEL + ", "
			+ CraftItemTable.TABLE_CRAFT_ITEM + "."
			+ CraftItemTable.COLUMN_COST + ", "
			+ CraftItemTable.TABLE_CRAFT_ITEM + "." + CraftItemTable.COLUMN_ID
			+ " AS _id FROM " + CraftItemTable.TABLE_CRAFT_ITEM + ", "
			+ CargoItemTable.TABLE_CARGO_ITEM + " WHERE "
			+ CraftItemTable.TABLE_CRAFT_ITEM + "."
			+ CraftItemTable.COLUMN_CARGO_ID + "="
			+ CargoItemTable.TABLE_CARGO_ITEM + "." + CargoItemTable.COLUMN_ID;

	public CraftItemDataSource(Context context) {
		super(context);
	}

	// Adds a new block type
	public long add(CraftItem item) {

		// Add universal data
		long cargoID = super.add(item);

		// Put the values of the type into content values object
		ContentValues cv = new ContentValues();
		cv.put(CraftItemTable.COLUMN_CARGO_ID, cargoID);
		cv.put(CraftItemTable.COLUMN_COST, item.getCost().getID());
		cv.put(CraftItemTable.COLUMN_EXP, item.getExp());
		cv.put(CraftItemTable.COLUMN_LEVEL, item.getLevel());

		// Insert the rows
		return mDatabase.insert(CraftItemTable.TABLE_CRAFT_ITEM, null, cv);
	}

	// Get every block type
	public List<CraftItem> getAll(Context context) {

		// Initialize list and run query
		List<CraftItem> typeList = new ArrayList<CraftItem>();

		String selectQuery = "SELECT * FROM " + CraftItemTable.TABLE_CRAFT_ITEM;

		Cursor cursor = mDatabase.rawQuery(selectQuery, null);

		CraftItem craftItem = null;

		// Loop over ever item
		if (cursor.moveToFirst()) {
			do {
				// Get the cargo item info from associated table
				// Returns with bitmap set up
				CargoItem item = super.get(cursor.getInt(cursor
						.getColumnIndex(CraftItemTable.COLUMN_CARGO_ID)),
						context);

				// Create the craft item object and return it
				craftItem = new CraftItem(item);
				craftItem.setTypeID(cursor.getInt(cursor
						.getColumnIndex(CraftItemTable.COLUMN_ID)));
				craftItem.setExp(cursor.getInt(cursor
						.getColumnIndex(CraftItemTable.COLUMN_EXP)));
				craftItem.setLevel(cursor.getInt(cursor
						.getColumnIndex(CraftItemTable.COLUMN_LEVEL)));
				CargoItem cost = super.get(cursor.getInt(cursor
						.getColumnIndex(CraftItemTable.COLUMN_COST)), context);
				craftItem.setCost(cost);

				typeList.add(craftItem);
			} while (cursor.moveToNext());
		}

		// Return the list
		return typeList;
	}

	public Cursor getInventoryCursor(Context context) {
		return mDatabase.rawQuery(INVENTORY_QUERY, null);
	}
}
