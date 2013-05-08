package com.asgard.game.models;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


/**
 * An abstract representation of items stored in the cargo hold
 * 
 * @author Benjamin
 */
public class CargoItem {

	/* The name of the item */
	protected String mDescription;
	/* The ID of the image */
	protected int mResID;
	/* The sprite for this item, retrievable with resID */
	protected Bitmap mSprite;

	/* The id of the cargo item in the database */
	protected long mID;
	/* The ID of subclasses which specifies the type */
	protected long mTypeID;
	

	public CargoItem() {
		
	}
	
	/* Copy constructor (for downcasting) */
	public CargoItem(CargoItem item) {
		mDescription = item.getDescription();
		mResID = item.getResID();
		mSprite = item.getSprite();
		mID = item.getID();
	}
	
	public String getDescription() {
		return mDescription;
	}
	
	public void setDescription(String description) {
		mDescription = description;
	}
	
	public int getResID() {
		return mResID;
	}
	
	public void setResID(int resID) {
		mResID = resID;
	}
	
	public Bitmap getSprite() {
		return mSprite;
	}
	
	/* Load the sprite from the given resources file */
	public void setSprite(Resources resources) {
		this.mSprite = BitmapFactory.decodeResource(resources, mResID);
	}
	
	public long getID() {
		return mID;
	}
	
	public void setID(long id) {
		mID = id;
	}
	
	public void setTypeID(long id) {
		mTypeID = id;
	}
	
	public long getTypeID() {
		return mTypeID;
	}
	

	/* Set the size of the bitmap */
	public void setSize(int size) {
		// Only change size if it is different!
		if (mSprite.getHeight() != size) {
			mSprite = Bitmap.createScaledBitmap(mSprite, size, size, true);

		}
	}
}
