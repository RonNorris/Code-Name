package com.asgard.game.views;

import android.graphics.Canvas;
import android.util.Log;
import com.asgard.game.models.Block;
import com.asgard.game.models.Point;

/**
 * 
 * @author Benjamin
 * 
 *         The view used to render each block.
 */
public class BlockView {

	private final static String TAG = BlockView.class.getSimpleName();

	/* The TOP RIGHT pixel of the block */
	private Point mLocation;

	/* The related block model */
	private Block mBlock;

	/* Interface that listens for game events */
	private GameEventListener mListener;
	
	private boolean mTouched;

	public BlockView(Block block, Point location) {
		setBlock(block);
		setLocation(location);
	}

	/* Sets the block for this view */
	public void setBlock(Block block) {
		mBlock = block;
	}
	
	/* Gets the block in this view */
	public Block getBlock() {
		return mBlock;
	}

	/* Sets the location of the view */
	public void setLocation(Point location) {
		mLocation = location;
	}

	/* Gets the location */
	public Point getLocation() {
		return mLocation;
	}

	/* Sets whether this view has been touched or not */
	public void setTouched(boolean touched) {
		mTouched = touched;
	}

	public boolean isTouched() {
		return mTouched;
	}

	/* Sets the strategy for shifting when clicked */
	public void setListener(GameEventListener listener) {
		mListener = listener;
	}

	/*
	 * Draw the view (by drawing the associated blocks bitmap) at the given
	 * location.
	 */
	public void draw(Canvas canvas) {
		canvas.drawBitmap(mBlock.getType().getSprite(), mLocation.x,
				mLocation.y, null);
	}

	/* Handle touch actions. Should only be enabled on blocks that can be tapped */
	public void handleActionDown(int eventX, int eventY, Point direction) {
		if (eventX >= (mLocation.x)
				&& eventX <= (mLocation.x + mBlock.getSize())) {
			if (eventY >= (mLocation.y)
					&& (eventY <= mLocation.y + mBlock.getSize())) {
				// Log event and set touched to true
				Log.d(TAG, "Block clicked at " + mLocation.toString());

				// Call the strategy if it exists
				if (mListener != null) {
					mListener.blockClicked(this, direction);
				}

			}
		}
	}
}
