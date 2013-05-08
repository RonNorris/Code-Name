package com.asgard.game.models;

import java.util.List;

import android.content.res.Resources;

/**
 * 
 * @author Benjamin
 * 
 *         Contains all of the block types
 */
public class BlockType extends CargoItem {

	/* How much fuel the ship uses to get through the block */
	protected float mHardness;

	/* The size to draw on the grid */
	public static int GRID_SIZE = 90;

	/* The ID of the associated BlockType database entry */
	protected long mTypeID;

	public BlockType() {

	}

	public BlockType(float hardness, int resID, String description) {
		setHardness(hardness);
		setResID(resID);
		setDescription(description);
	}

	/* Copy constructor */
	public BlockType(CargoItem item) {
		super(item);
	}

	// Static storage of all block types once loaded from file
	public static List<BlockType> types;

	/* Getters and setters */
	public float getHardness() {
		return mHardness;
	}

	public void setHardness(float mHardness) {
		this.mHardness = mHardness;
	}

	/* Set the sprite and update the block size  */
	@Override
	public void setSprite(Resources resources) {
		super.setSprite(resources);
		GRID_SIZE = mSprite.getHeight();
	}
	
	/* Set the size and update block type sizes */
	@Override
	public void setSize(int size) {
		super.setSize(size);
		GRID_SIZE = mSprite.getHeight();
	}
}
