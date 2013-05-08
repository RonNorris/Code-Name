package com.asgard.game.models;

import com.asgard.game.R;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * The representation of the player's state
 * 
 * @author Benjamin
 * 
 */
public class Ship {

	public static final int[] resourceIDs = { R.drawable.drill_1,
			R.drawable.drill_2, R.drawable.drill_3 };

	public static final int MAX_SPEED = 10;

	public static final int MIN_SPEED = 2;
	
	/* The speed which the player moves */
	protected float mSpeed;
	/* The current location of the player */
	protected Point mLocation;
	/* The plane which the player is currently on */
	protected int mCurrentPlane;
	/* The player's cargo hold */
	protected CargoHold mCargoHold;
	/* The current experience */
	protected int mExperience;
	/* The drawables for the ship */
	protected Bitmap[] mSprites;
	/* The direction the ship is facing */
	protected Point mDirection;

	/* The id of the ship in the database */
	protected int mID;

	/* Static reference for ship once its loaded */
	public static Ship ship;

	public Ship() {
		// Initialize array
		mSprites = new Bitmap[resourceIDs.length];
	}

	public float getSpeed() {
		return mSpeed;
	}

	public void setSpeed(float speed) {
		this.mSpeed = speed;
	}

	public Point getLocation() {
		return mLocation;
	}

	public void setLocation(Point location) {
		this.mLocation = location;
	}

	public int getCurrentPlane() {
		return mCurrentPlane;
	}

	public void setCurrentPlane(int currentPlane) {
		this.mCurrentPlane = currentPlane;
	}

	public CargoHold getCargoHold() {
		return mCargoHold;
	}

	public void setCargoHold(CargoHold cargoHold) {
		this.mCargoHold = cargoHold;
	}

	public int getExperience() {
		return mExperience;
	}

	public void setExperience(int experience) {
		this.mExperience = experience;
	}

	public int getID() {
		return mID;
	}

	public void setID(int id) {
		mID = id;
	}

	public Point getDirection() {
		return mDirection;
	}

	public void setDirection(Point direction) {
		this.mDirection = direction;
	}

	public Bitmap[] getSprites() {
		return mSprites;
	}

	public void setSprites(Bitmap[] sprites) {
		mSprites = sprites;
	}

	/* Set all of the sprites */
	public void setSprite(Resources resources) {
		for (int i = 0; i < Ship.resourceIDs.length; i++) {
			mSprites[i] = BitmapFactory.decodeResource(resources,
					Ship.resourceIDs[i]);
		}
	}

	/* Rotates the bitmaps */
	public void rotateSprites(Point direction) {

		// Determine rotation angle based on direction difference
		float angle = 0;

		// Direction reversed, rotate 180
		if ((direction.x == mDirection.x && direction.y != mDirection.y)
				|| (direction.x != mDirection.x && direction.y == mDirection.y)) {
			angle = 180;
		} else if (direction.x != mDirection.x && direction.y != mDirection.y) {
			// Rotate at some 90 degree angle
			if (mDirection.x == 1) {
				if (direction.y == 1) {
					angle = 90;
				} else {
					angle = 270;
				}
			} else if (mDirection.x == -1) {
				if (direction.y == 1) {
					angle = 270;
				} else {
					angle = 90;
				}
			} else if (mDirection.y == 1) {
				if (direction.x == 1) {
					angle = 270;
				} else {
					angle = 90;
				}
			} else if (mDirection.y == -1) {
				if (direction.x == 1) {
					angle = 90;
				} else {
					angle = 270;
				}
			}

		}

		if (angle != 0) {
			// Update the ships direction
			mDirection = direction;

			Matrix matrix = new Matrix();
			matrix.postRotate(angle);
			for (int i = 0; i < Ship.resourceIDs.length; i++) {
				mSprites[i] = Bitmap.createBitmap(mSprites[i], 0, 0,
						mSprites[i].getWidth(), mSprites[i].getWidth(), matrix,
						true);
			}
		}
	}

}
