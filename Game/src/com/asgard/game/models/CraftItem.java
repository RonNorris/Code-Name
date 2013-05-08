package com.asgard.game.models;

/**
 * A craft item is any cargo item which can be crafted
 * 
 * @author Benjamin
 * 
 */
public class CraftItem extends CargoItem {

	/* The ID of the associated BlockType database entry */
	protected long mTypeID;
	/* The level required to create the item */
	protected int mLevel;
	/* The amount of experience crafting the item gives */
	protected int mExp;
	/* The cargo item required to create the item */
	protected CargoItem mCost;
	
	public CraftItem(int resID, String description, int level, int exp, CargoItem item) {
		mResID = resID;
		mDescription = description;
		mLevel = level;
		mExp = exp;
		mCost = item;
	}
	
	public CraftItem(CargoItem item) {
		super(item);
	}
	
	public int getLevel() {
		return mLevel;
	}

	public void setLevel(int level) {
		this.mLevel = level;
	}

	public int getExp() {
		return mExp;
	}

	public void setExp(int exp) {
		this.mExp = exp;
	}

	public CargoItem getCost() {
		return mCost;
	}

	public void setCost(CargoItem cost) {
		this.mCost = cost;
	}
}
