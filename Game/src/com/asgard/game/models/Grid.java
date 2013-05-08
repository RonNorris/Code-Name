package com.asgard.game.models;

public class Grid {

	/* Blocks right/left */
	public static final int GRID_WIDTH = 50;
	/* Blocks up/down */
	public static final int GRID_LENGTH = 50;
	/* Number of layers */
	public static final int NUM_PLANES = 100;

	/* The array of blocks that this grid represents */
	protected Block[][] mBlocks;
	/* The plane which this grid represents */
	protected int mPlane;

	/* The id of this grid in the database */
	protected int mID;

	public static Grid grid;

	public Grid(int plane) {
		mPlane = plane;
	}

	public void setBlocks(Block[][] blocks) {
		mBlocks = blocks;
	}

	public Block[][] getBlocks() {
		return mBlocks;
	}

	public void setPlane(int plane) {
		mPlane = plane;
	}

	public int getPlane() {
		return mPlane;
	}

	public void setID(int id) {
		mID = id;
	}

	public int getID() {
		return mID;
	}

}
