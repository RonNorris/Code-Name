package com.asgard.game.db;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.asgard.game.models.Block;
import com.asgard.game.models.BlockType;
import com.asgard.game.models.Grid;
import com.asgard.game.models.Point;

/**
 * The Block data source provides access to the Block table in the database. The
 * block table is the biggest and requires careful optimization to keep things
 * running smoothly.
 * 
 * @author Benjamin
 * 
 */
public class BlockDataSource extends DataSource {

	private static final String TAG = BlockDataSource.class.getSimpleName();

	public BlockDataSource(Context context) {
		super(context);
	}

	public static final String[] ALL_COLUMNS = { BlockTable.COLUMN_ID,
			BlockTable.COLUMN_BLOCK_TYPE, BlockTable.COLUMN_GRID,
			BlockTable.COLUMN_X, BlockTable.COLUMN_Y };

	/* Compiled SQL makes database inserts faster */
	public static final String INSERT = "INSERT INTO " + BlockTable.TABLE_BLOCK
			+ "(" + BlockTable.COLUMN_BLOCK_TYPE + "," + BlockTable.COLUMN_GRID
			+ "," + BlockTable.COLUMN_X + "," + BlockTable.COLUMN_Y
			+ ") VALUES (?,?,?,?)";

	/**
	 * CRUD operations
	 */
	// Adds a new block type
	public void add(Block block, Grid grid) {

		// Put the values of the type into content values object
		ContentValues cv = new ContentValues();
		cv.put(BlockTable.COLUMN_BLOCK_TYPE, block.getType().getID());
		cv.put(BlockTable.COLUMN_GRID, grid.getID());
		cv.put(BlockTable.COLUMN_X, block.getPoint().x);
		cv.put(BlockTable.COLUMN_Y, block.getPoint().y);

		// Insert the rows
		mDatabase.insert(BlockTable.TABLE_BLOCK, null, cv);
	}

	// Adds an array of blocks to the grid using bulk insert
	public void addBlocks(Block[][] blocks, Grid grid) {

		Log.d(TAG, "Adding block grid");

		// Grab the grids id
		int gridID = grid.getID();

		// Begin outer transaction
		mDatabase.beginTransaction();

		// Record the time
		long totalTime = System.currentTimeMillis();

		try {
			for (Block[] b : blocks) {

				// Time of each row
				long rowTime = System.currentTimeMillis();

				// Begin inner transaction
				mDatabase.beginTransaction();

				try {
					// Use compiled sql for better performance
					SQLiteStatement statement = mDatabase
							.compileStatement(INSERT);
					for (Block bb : b) {

						statement.bindLong(1, bb.getType().getID());
						statement.bindLong(2, gridID);
						statement.bindLong(3, bb.getPoint().x);
						statement.bindLong(4, bb.getPoint().y);
						statement.execute();
						statement.clearBindings();
					}

					// Row transaction time
					Log.d(TAG, "Row insert took"
							+ (System.currentTimeMillis() - rowTime) + "ms");

					mDatabase.setTransactionSuccessful();
				} catch (SQLiteException e) {
					e.printStackTrace();
				} finally {
					mDatabase.endTransaction();
				}
			}

			// Total transaction time, get time
			Log.d(TAG, "Took " + (System.currentTimeMillis() - totalTime)
					+ " ms");

			mDatabase.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			mDatabase.endTransaction();
		}
	}

	// Adds an array of blocks to the grid using bulk insert
	public void updateBlocks(Block[][] blocks, Grid grid) {

		// Drop the table
		mDatabase.execSQL("DROP TABLE IF EXISTS " + BlockTable.TABLE_BLOCK +  ";");
		
		Log.d(TAG, "updating block grid");

		// Grab the grids id
		int gridID = grid.getID();		
		
		// Begin outer transaction
		mDatabase.beginTransaction();

		// Record the time
		long totalTime = System.currentTimeMillis();

		try {
			for (Block[] b : blocks) {

				// Time of each row
				long rowTime = System.currentTimeMillis();

				// Begin inner transaction
				mDatabase.beginTransaction();

				try {
					// Use compiled sql for better performance
					SQLiteStatement statement = mDatabase
							.compileStatement(INSERT);
					for (Block bb : b) {

						statement.bindLong(1, bb.getType().getID());
						statement.bindLong(2, gridID);
						statement.bindLong(3, bb.getPoint().x);
						statement.bindLong(4, bb.getPoint().y);
						statement.execute();
						statement.clearBindings();
					}

					// Row transaction time
					Log.d(TAG, "Row insert took"
							+ (System.currentTimeMillis() - rowTime) + "ms");

					mDatabase.setTransactionSuccessful();
				} catch (SQLiteException e) {
					e.printStackTrace();
				} finally {
					mDatabase.endTransaction();
				}
			}

			// Total transaction time, get time
			Log.d(TAG, "Took " + (System.currentTimeMillis() - totalTime)
					+ " ms");

			mDatabase.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			mDatabase.endTransaction();
		}
	}

	// Get the block stored at the specific ID
	public Block getBlock(int id, Context context) {
		// Query all items for matching id
		Cursor cursor = mDatabase.query(BlockTable.TABLE_BLOCK, ALL_COLUMNS,
				BlockTable.COLUMN_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		// Get the point
		Point p = new Point(cursor.getInt(cursor
				.getColumnIndex(BlockTable.COLUMN_X)), cursor.getInt(cursor
				.getColumnIndex(BlockTable.COLUMN_Y)));

		// Get the type (resolved from the id)
		BlockTypeDataSource blockTypeSource = new BlockTypeDataSource(context);
		BlockType bt = blockTypeSource.get(
				cursor.getInt(cursor.getColumnIndex(BlockTypeTable.COLUMN_ID)),
				context);

		// Create the new block
		Block block = new Block(p, bt);
		block.setID(cursor.getInt(cursor.getColumnIndex(BlockTable.COLUMN_ID)));

		return block;
	}

	/* Get blocks in the given grid in between start and end point */
	public Block[][] getBlocksInGrid(Context context, Grid grid,
			List<BlockType> types, Point start, Point end) {
		// Open a block type data source
		BlockTypeDataSource blockTypeSource = new BlockTypeDataSource(context);
		blockTypeSource.open();

		// Create new array the size of the difference
		Block[][] blocks = new Block[end.x - start.x][end.y - start.y];

		// Query all items for matching grid id within range
		Cursor cursor = mDatabase
				.query(BlockTable.TABLE_BLOCK, ALL_COLUMNS,
						BlockTable.COLUMN_GRID + "=? and "
								+ BlockTable.COLUMN_X + ">=" + start.x
								+ " and " + BlockTable.COLUMN_X + "<" + end.x
								+ " and " + BlockTable.COLUMN_Y + ">="
								+ start.y + " and " + BlockTable.COLUMN_Y + "<"
								+ end.y,
						new String[] { String.valueOf(grid.getID()) }, null,
						null, null);

		// Loop over ever item
		if (cursor.moveToFirst()) {
			do {
				// Get the point
				Point p = new Point(cursor.getInt(cursor
						.getColumnIndex(BlockTable.COLUMN_X)),
						cursor.getInt(cursor
								.getColumnIndex(BlockTable.COLUMN_Y)));

				// Get the type (resolved from the id)
				BlockType bt = null;
				int ID = cursor.getInt(cursor
						.getColumnIndex(BlockTable.COLUMN_BLOCK_TYPE));
				for (BlockType t : types) {
					if (t.getID() == ID) {
						bt = t;
					}
				}

				// Create the new block
				Block block = new Block(p, bt);
				block.setID(cursor.getInt(cursor
						.getColumnIndex(BlockTable.COLUMN_ID)));

				blocks[p.x - start.x][p.y - start.y] = block;
			} while (cursor.moveToNext());
		}

		blockTypeSource.close();

		return blocks;
	}

	/* Updates the given block */
	public int update(Block block, Grid grid) {
		// Put the values of the type into content values object
		ContentValues values = new ContentValues();
		values.put(BlockTable.COLUMN_BLOCK_TYPE, block.getType().getID());
		values.put(BlockTable.COLUMN_GRID, grid.getID());
		values.put(BlockTable.COLUMN_X, block.getPoint().x);
		values.put(BlockTable.COLUMN_Y, block.getPoint().y);

		return mDatabase.update(BlockTable.TABLE_BLOCK, values,
				BlockTable.COLUMN_ID + "=?",
				new String[] { String.valueOf(block.getID()) });
	}
}
