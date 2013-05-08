package com.asgard.game.db;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.asgard.game.R;
import com.asgard.game.models.Block;
import com.asgard.game.models.BlockType;
import com.asgard.game.models.CraftItem;
import com.asgard.game.models.Grid;
import com.asgard.game.models.Point;
import com.asgard.game.models.Ship;

import com.asgard.game.db.BlockTypeDataSource;

;

/**
 * The class used to create and load tables in/from the database
 * 
 * @author Benjamin
 */
public class DatabaseLoader {

	private static final String TAG = DatabaseLoader.class.getSimpleName();

	/**
	 * Create each of the tables in the database using their respective data
	 * source classes.
	 * 
	 * @param context
	 * 
	 */
	public static void createDatabase(Context context) {
		// Create the database
		SQLiteDatabase db = context.openOrCreateDatabase(
				GameDatabaseSchema.DATABASE_NAME, Context.MODE_PRIVATE, null);
		db.close();
		
		// Create the ship
		createShip(context);

		// Create the block types
		createBlockTypes(context);

		// Create a new grid
		createGrid(context);

		// Create craft items
		createCraftItems(context);
	}

	/* Fills the database with the block types */
	private static void createBlockTypes(Context context) {
		// Create the table
		Log.d(TAG, "Filling Block Type database...");

		BlockTypeDataSource blockTypeSource = new BlockTypeDataSource(context);
		blockTypeSource.open();

		// Create the block types
		blockTypeSource.add(new BlockType(0.2f, R.drawable.dirt, "dirt"));

		blockTypeSource.add(new BlockType(0.1f, R.drawable.empty, "empty"));

		blockTypeSource.add(new BlockType(2.5f, R.drawable.lumrock, "lumrock"));

		blockTypeSource.add(new BlockType(1.0f, R.drawable.metal, "metal"));
		
		blockTypeSource.add(new BlockType(0.3f, R.drawable.water1, "water1"));
		
		blockTypeSource.add(new BlockType(0.3f, R.drawable.water2, "water2"));
		
		blockTypeSource.add(new BlockType(0.3f, R.drawable.water3, "water3"));
		
		blockTypeSource.close();
	}

	/* Fills the database with the grids */
	private static void createGrid(Context context) {

		Log.d(TAG, "Filling Grid database...");

		GridDataSource gridSource = new GridDataSource(context);

		// Open the source and add a grid
		gridSource.open();
		Grid g = new Grid(Grid.NUM_PLANES);
		long id = gridSource.add(g);
		gridSource.close();

		createBlocks(context, id);
	}

	/* Fills the database with blocks */
	private static void createBlocks(Context context, long id) {

		
		Log.d(TAG, "Filling Block database...");
		//the file containing the difficulty preferences
		 String PREFS_NAME = "MyPrefsFile";
		
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
		int e_m_h = settings.getInt("difficulty", -1);

		switch (e_m_h){
		
		case 0:
			GridGenerator.generateEasy(context, id);
			break;
			
		case 1:
			GridGenerator.generateMed(context, id);
			break;
			
		case 2:
			GridGenerator.generateHard(context, id);
			break;
			
			default:
				GridGenerator.generateEasy(context, id);
		}
	}

	/* Creates initial ship information */
	private static void createShip(Context context) {

		Log.d(TAG, "Filling Ship table...");

		// Create the first ship, on the bottom plane
		Ship startShip = new Ship();
		startShip.setExperience(0);
		startShip.setLocation(new Point(Grid.GRID_WIDTH / 2 - 1,
				Grid.GRID_LENGTH / 2 - 1));
		startShip.setCurrentPlane(Grid.NUM_PLANES);
		startShip.setSpeed(2.0f);

		// Add to the database
		ShipDataSource shipSource = new ShipDataSource(context);
		shipSource.open();
		shipSource.add(startShip);
		shipSource.close();
	}


	/* Creates the craft items with their associated rules */
	private static void createCraftItems(Context context) {
		Log.d(TAG, "Filling Craft Item table");
		
		// Get block types
		BlockTypeDataSource blockTypeSource = new BlockTypeDataSource(context);
		blockTypeSource.open();
		List<BlockType> types = blockTypeSource.getAll(context);
		blockTypeSource.close();
		
		CraftItemDataSource source = new CraftItemDataSource(context);
		source.open();
		source.add(new CraftItem(R.drawable.upgrade2, "battery", 1, 2, types.get(3)));
		source.add(new CraftItem(R.drawable.upgrade3, "generator", 1, 2, types.get(3)));
		source.add(new CraftItem(R.drawable.upgrade1, "drill boost", 1, 2, types.get(3)));
		source.close();
	}


	/**
	 * Calls the load functions in the correct order.
	 * 
	 * Note that blocks should be loaded dynamically later to prevent long
	 * database waits
	 */
	public static void loadGame(Context context) {
		BlockType.types = loadBlockTypes(context);
		Ship.ship = loadShip(context);
		Grid.grid = loadGrid(context, BlockType.types);
	}

	/* Loads the ship */
	public static Ship loadShip(Context context) {
		ShipDataSource shipSource = new ShipDataSource(context);
		shipSource.open();
		Ship ship = shipSource.getShip();
		shipSource.close();
		
		// Load all of the ship animations
		ship.setSprite(context.getResources());
		ship.setDirection(new Point(1,0));
		return ship;
	}

	/* Fills block type objects with data */
	public static List<BlockType> loadBlockTypes(Context context) {
		// Load the block types
		BlockTypeDataSource blockTypeSource = new BlockTypeDataSource(context);
		blockTypeSource.open();
		List<BlockType> types = blockTypeSource.getAll(context);
		blockTypeSource.close();
		return types;
	}

	/* Fills the grid with its data */
	public static Grid loadGrid(Context context, List<BlockType> types) {
		// Load the current grid
		GridDataSource gridSource = new GridDataSource(context);
		gridSource.open();
		Grid grid = gridSource.getGrid(Ship.ship.getCurrentPlane());
		gridSource.close();

		return grid;
	}

	/* Loads the grid objects from the start point to the end point */
	public static Block[][] loadBlocks(Context context, Grid grid,
			List<BlockType> types, Point start, Point end) {

		// Make sure the end point is larger than the start point
		if (start.x < end.x && start.y < end.y) {
			BlockDataSource blockSource = new BlockDataSource(context);
			blockSource.open();
			Block[][] blocks = blockSource.getBlocksInGrid(context, grid,
					types, start, end);
			blockSource.close();
			return blocks;
		}

		// Return null if it fails
		return null;
	}

	/**
	 * Save to the database
	 */
	public static void saveShip(Context context, Ship ship) {
		ShipDataSource shipSource = new ShipDataSource(context);
		shipSource.open();
		shipSource.update(ship);
		shipSource.close();
	}
}
