package com.asgard.game.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.asgard.game.models.BlockType;
import com.asgard.game.models.CargoItem;

/**
 * Provides CRUD access between BlockType objects and the database table
 * 
 * @author Benjamin
 */
public class BlockTypeDataSource extends CargoItemDataSource {

	public BlockTypeDataSource(Context context) {
		super(context);
	}

	public static final String[] ALL_COLUMNS = { BlockTypeTable.COLUMN_ID,
			BlockTypeTable.COLUMN_HARDNESS, BlockTypeTable.COLUMN_CARGO_ID };

	/**
	 * CRUD operations
	 */
	// Adds a new block type
	public long add(BlockType type) {

		// Add universal data
		long cargoID = super.add(type);

		// Put the values of the type into content values object
		ContentValues cv = new ContentValues();
		cv.put(BlockTypeTable.COLUMN_HARDNESS, type.getHardness());
		cv.put(BlockTypeTable.COLUMN_CARGO_ID, cargoID);

		// Insert the rows
		return mDatabase.insert(BlockTypeTable.TABLE_BLOCK_TYPE, null, cv);
	}

	// Get the block type of the specific id
	public BlockType get(long id, Context context) {

		// Query all items for matching id
		Cursor cursor = mDatabase.query(BlockTypeTable.TABLE_BLOCK_TYPE,
				ALL_COLUMNS, BlockTypeTable.COLUMN_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null);

		BlockType blockType = null;

		if (cursor.moveToFirst()) {

			// Get the cargo item info from associated table
			// Returns with bitmap set up
			CargoItem item = super.get(cursor.getInt(cursor
					.getColumnIndex(BlockTypeTable.COLUMN_CARGO_ID)), context);

			// Create the block type using copy constructor
			blockType = new BlockType(item);

			// Create the block type object and return it
			blockType.setHardness(cursor.getFloat(cursor
					.getColumnIndex(BlockTypeTable.COLUMN_HARDNESS)));
			blockType.setTypeID(cursor.getInt(cursor
					.getColumnIndex(BlockTypeTable.COLUMN_ID)));

		}
		return blockType;
	}

	public BlockType get(String name, Context context) {

		// Get the super item
		CargoItem item = super.get(name, context);

		// Get the associated block type
		Cursor cursor = mDatabase
				.query(BlockTypeTable.TABLE_BLOCK_TYPE, ALL_COLUMNS,
						BlockTypeTable.COLUMN_CARGO_ID + "=?",
						new String[] { String.valueOf(item.getID()) }, null,
						null, null);

		BlockType blockType = null;

		if (cursor.moveToFirst()) {

			// Create the block type using copy constructor
			blockType = new BlockType(item);

			// Create the block type object and return it
			blockType.setHardness(cursor.getFloat(cursor
					.getColumnIndex(BlockTypeTable.COLUMN_HARDNESS)));
			blockType.setTypeID(cursor.getInt(cursor
					.getColumnIndex(BlockTypeTable.COLUMN_ID)));

		}
		return blockType;
	}

	// Get every block type
	public List<BlockType> getAll(Context context) {

		// Initialize list and run query
		List<BlockType> typeList = new ArrayList<BlockType>();

		String selectQuery = "SELECT * FROM " + BlockTypeTable.TABLE_BLOCK_TYPE;

		Cursor cursor = mDatabase.rawQuery(selectQuery, null);

		BlockType blockType = null;

		// Loop over ever item
		if (cursor.moveToFirst()) {
			do {
				// Get the cargo item info from associated table
				// Returns with bitmap set up
				CargoItem item = super.get(cursor.getInt(cursor
						.getColumnIndex(BlockTypeTable.COLUMN_CARGO_ID)),
						context);

				blockType = new BlockType(item);

				// Create the block type object and return it
				blockType.setHardness(cursor.getFloat(cursor
						.getColumnIndex(BlockTypeTable.COLUMN_HARDNESS)));
				blockType.setTypeID(cursor.getInt(cursor
						.getColumnIndex(BlockTypeTable.COLUMN_ID)));
				typeList.add(blockType);
			} while (cursor.moveToNext());
		}

		// Return the list
		return typeList;
	}
}
