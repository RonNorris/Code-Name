package com.asgard.game.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.asgard.game.models.Grid;

public class GridDataSource extends DataSource {

	public GridDataSource(Context context) {
		super(context);
	}

	private static final String[] ALL_COLUMNS = { GridTable.COLUMN_ID,
			GridTable.COLUMN_PLANE };

	/**
	 * CRUD operations
	 * 
	 * @return
	 */
	/* Adds a new grid to the database */
	public long add(Grid grid) {
		// Get the values
		ContentValues cv = new ContentValues();
		cv.put(GridTable.COLUMN_PLANE, grid.getPlane());

		// Write to the db
		return mDatabase.insert(GridTable.TABLE_GRID, null, cv);
	}

	/* Get the grid with the specified id */
	public Grid getGrid(long id) {
		// Query all items for matching id
		Cursor cursor = mDatabase.query(GridTable.TABLE_GRID, ALL_COLUMNS,
				GridTable.COLUMN_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null);

		Grid grid = null;

		if (cursor.moveToFirst()) {
			cursor.moveToFirst();

			grid = new Grid(cursor.getInt(cursor
					.getColumnIndex(GridTable.COLUMN_PLANE)));

			grid.setID(cursor.getInt(cursor.getColumnIndex(GridTable.COLUMN_ID)));
		}

		return grid;
	}

	/* Get the grid with the specified id */
	public Grid getGrid(int plane) {
		// Query all items for matching id
		Cursor cursor = mDatabase.query(GridTable.TABLE_GRID, ALL_COLUMNS,
				GridTable.COLUMN_PLANE + "=?",
				new String[] { String.valueOf(plane) }, null, null, null);

		Grid grid = null;

		if (cursor.moveToFirst()) {
			cursor.moveToFirst();

			grid = new Grid(cursor.getInt(cursor
					.getColumnIndex(GridTable.COLUMN_PLANE)));

			grid.setID(cursor.getInt(cursor.getColumnIndex(GridTable.COLUMN_ID)));
		}

		return grid;
	}

	
	/* Gets all of the grids in the database */
	public List<Grid> getAllGrids() {

		List<Grid> grids = new ArrayList<Grid>();

		String selectQuery = "SELECT * FROM " + GridTable.TABLE_GRID;

		Cursor cursor = mDatabase.rawQuery(selectQuery, null);

		// Loop over ever item
		if (cursor.moveToFirst()) {
			do {
				Grid grid = new Grid(cursor.getInt(cursor
						.getColumnIndex(GridTable.COLUMN_PLANE)));
				grid.setID(cursor.getInt(cursor
						.getColumnIndex(GridTable.COLUMN_ID)));
				grids.add(grid);
			} while (cursor.moveToNext());
		}

		// Return the list
		return grids;
	}
}
