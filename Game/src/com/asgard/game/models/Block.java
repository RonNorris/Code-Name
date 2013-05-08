package com.asgard.game.models;

public class Block {	
	
	/* The location (on the grid) where this block is */
	protected Point mPoint;
	/* The blocks type */
	protected BlockType mType;
	
	/* The id of the block in the database */
	protected int mID;

	public Block(Point p, BlockType bt) {
		setPoint(p);
		setType(bt);
	}

	public void setPoint(Point p) {
		mPoint = p;
	}

	public Point getPoint() {
		return mPoint;
	}

	public void setType(BlockType bt) {
		mType = bt;
	}

	public BlockType getType() {
		return mType;
	}
	
	public void setID(int id) {
		mID = id;
	}
	
	public int getID() {
		return mID;
	}
	
	/* Return the size (always a square) */
	public int getSize() {
		return getType().getSprite().getHeight();
	}
}
