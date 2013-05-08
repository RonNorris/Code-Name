package com.asgard.game.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The cargo hold of the ship, contains all of the items that the ship currently
 * has. Each slot stacks similar items.
 * 
 * @author Benjamin
 * 
 */
public class CargoHold {

	/* The number of slots in the cargo hold */
	protected int mSize;
	/* The cargo items in the cargo hold */
	protected List<CargoContainer> mInventory;

	/* The id of the cargo hold in the database */
	protected int mID;

	/* Create a cargo hold with the given size */
	public CargoHold(int size) {
		mSize = size;
		mInventory = new ArrayList<CargoContainer>(mSize);
		for (int i = 0; i < mSize; i++) {
			// Add an empty cargo container at each slot
			CargoContainer temp = new CargoContainer();
			temp.mNumber = 0;
			mInventory.add(temp);
		}
	}

	public int getSize() {
		return mSize;
	}

	public void setSize(int size) {
		this.mSize = size;
	}

	public int getID() {
		return mID;
	}

	public void setID(int id) {
		mID = id;
	}

	/* Attempt to add an item, returning the result */
	public boolean addItem(CargoItem item, int number) {
		// Remove the first empty slot
		int emptyPos = 0;

		// Determine the current empty position
		while (emptyPos < mSize) {
			if (mInventory.get(emptyPos).mNumber == 0) {
				break;
			}
			emptyPos++;
		}

		// Add to that position
		if (emptyPos < mSize) {
			mInventory.set(emptyPos, new CargoContainer(item, number));
			return true;
		}

		return false;
	}
	
	public int numStock (int cargoIndex){
		CargoContainer temp;
		
		for (Iterator<CargoContainer> i = mInventory.iterator(); i.hasNext(); ){
			temp = i.next();
			if (temp.mItem.getID() == cargoIndex){
				return temp.mNumber;
			}
		}
		return 0;
		
	}

	public CargoContainer getItem(int location) {
		return mInventory.get(location);
	}

	/**
	 * PODO for cargo items
	 * 
	 * @author Benjamin
	 * 
	 */
	public class CargoContainer {
		public CargoItem mItem;
		public int mNumber;

		public CargoContainer() {

		}

		public CargoContainer(CargoItem item, int num) {
			mItem = item;
			mNumber = num;
		}
	}

	public List<CargoContainer> getInventory() {
		return mInventory;
	}

}
