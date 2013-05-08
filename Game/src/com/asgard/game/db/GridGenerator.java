package com.asgard.game.db;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.util.Log;

import com.asgard.game.SplashScreenActivity;
import com.asgard.game.models.Block;
import com.asgard.game.models.BlockType;
import com.asgard.game.models.Grid;
import com.asgard.game.models.Point;

public class GridGenerator {
	private static final String TAG = SplashScreenActivity.class
			.getSimpleName();
	
	//default starting values;
	private static int m_dirt = 75;
	private static int m_water = 17;
	private static int m_metal = 5;
	private static int m_lumrock = 3;

	public static int generateType(){
		
		Random r = new Random();
		int type = r.nextInt(100);
		int blockType = 0;
		
		if (type <= m_dirt){
			blockType = 0;
		}
		else if (type > m_dirt && type <= (m_dirt+m_water)){
			if ((type%3) == 0){
				blockType = 4;
			}
			else if ((type%3) == 1){
				blockType = 5;
			}
			else if ((type%3) == 2){
				blockType = 6;
			}
		}
		else if (type > m_water && type <= (m_dirt+m_water+m_metal)){
			blockType = 3;
		}
		else if (type > m_metal && type <= (m_dirt+m_water+m_metal+m_lumrock)){
			blockType = 2;
		}	
		else {
			return -1;
		}
		
		return blockType;
	}
	public static void generateRand(Context context, long id) {

		Log.d(TAG, "Generating random blocks");

		int tempIndex = 0;

		// Open the necessary sources
		BlockTypeDataSource blockTypeSource = new BlockTypeDataSource(context);
		blockTypeSource.open();
		BlockDataSource blockSource = new BlockDataSource(context);
		blockSource.open();
		GridDataSource gridSource = new GridDataSource(context);
		gridSource.open();

		// Get the grids and block types
		List<BlockType> types = blockTypeSource.getAll(context);
		Grid grid = gridSource.getGrid(id);

		Block[][] blocks = new Block[Grid.GRID_WIDTH][Grid.GRID_LENGTH];
		for (int i = 0; i < Grid.NUM_PLANES / 100; i++) {
			Log.d(TAG, "Inserting for grid " + grid.getID());
			for (int j = 0; j < Grid.GRID_WIDTH; j++) {
				for (int k = 0; k < Grid.GRID_LENGTH; k++) {

					if (j == 24 && k == 24){
						blocks[j][k] = new Block(new Point(j, k), types.get(1));
					}

					else{
						// Create a random blocks
						tempIndex = generateType();
						if (tempIndex >=0 && tempIndex < types.size()){
							blocks[j][k] = new Block(new Point(j, k), types.get(tempIndex));
						}
						else{
							blocks[j][k] = new Block(new Point(j, k), types.get(0));
						}
						//blocks[j][k] = new Block(new Point(j, k), types.get(r.nextInt(types.size())));
					}
				}
			}

			// Add the block array to the database in bulk
			blockSource.addBlocks(blocks, grid);
		}

		blockTypeSource.close();
		blockSource.close();
		gridSource.close();
	}

	public static void generateEasy(Context context, long id) {
		m_dirt = 75;
		m_water = 17;
		m_metal = 5;
		m_lumrock = 3;
		
		generateRand(context, id);
	}

	public static void generateMed(Context context, long id) {

		m_dirt = 80;
		m_water = 14;
		m_metal = 4;
		m_lumrock = 2;
		
		generateRand(context, id);
	}

	public static void generateHard(Context context, long id) {

		m_dirt = 85;
		m_water = 11;
		m_metal = 3;
		m_lumrock = 1;
		
		generateRand(context, id);
	}
}
