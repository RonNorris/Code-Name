package com.asgard.game.views;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import com.asgard.game.GameActivity;
import com.asgard.game.db.BlockDataSource;
import com.asgard.game.db.BlockTypeDataSource;
import com.asgard.game.db.CargoHoldDataSource;
import com.asgard.game.db.DatabaseLoader;
import com.asgard.game.models.Block;
import com.asgard.game.models.BlockType;
import com.asgard.game.models.Grid;
import com.asgard.game.models.Point;
import com.asgard.game.models.Ship;

/**
 * The class in charge of managing the number of blocks to load at a time.
 * Should be created during splash screen to minimize delay!
 * 
 * The manager buffers an area around what the user can see. When the user
 * changes position, the old farthest edge is deleted and replaced with a new
 * one
 * 
 * @author Benjamin
 * 
 */
public class GameScreenManager implements GameEventListener {

	public static final String TAG = GameScreenManager.class.getSimpleName();

	/* Base scroll speed */
	protected static final int SCROLL_SPEED = 2;

	/* The midpoint (where the screen is centered) */
	protected Point mMidPoint;

	/* Screen properties */
	protected int mScreenWidth;
	protected int mScreenHeight;

	/* Current grid position */
	protected Point mCurrentPos;

	/* The array of BlockView objects that are currently loaded */
	protected BlockView[][] mBlockViews;

	/* The blocks */
	protected Block[][] mBlocks;

	/* The view and activity that the manager talks to */
	protected GameActivity mActivity;
	protected MainGameView mView;

	/* The thread which draws the game */
	protected MainGameThread mGameThread;

	/* The point that dictates the direction of motion */
	protected Point mMotion;
	/* Counter for moving grid */
	protected int mMovesLeft;
	/* The speed of motion */
	protected int mMoveSpeed;

	/* Used to control resizing */
	protected boolean mIsResizing;
	protected int mSize;

	/* The only views that have touch listeners */
	protected BlockView mLeft;
	protected BlockView mRight;
	protected BlockView mUp;
	protected BlockView mDown;

	/* Used to control drill spin */
	protected int mDrillSpin;

	/* Used to flag starting and stopping of thread */
	protected boolean mIsMoving;

	/* Get screen information and create the grid */
	public GameScreenManager(GameActivity activity, MainGameView view,
			int width, int height) {
		mActivity = activity;
		mView = view;

		// Set screen info
		mScreenWidth = width;
		mScreenHeight = height;
		mCurrentPos = Ship.ship.getLocation();

		// Render once
		mIsMoving = true;
		mMovesLeft = 0;
		mMotion = new Point(0, 0);
		mIsResizing = false;

		// Set the size and resize, starting the creation
		mSize = BlockType.GRID_SIZE;
		resizeGrid();
	}

	/* Starts the game thread for rendering */
	public void startGameThread() {
		// Set the flags and start the thread
		if (mGameThread == null) {
			mGameThread = new MainGameThread(mView.getHolder(), mView);
			mGameThread.setAlive(true);
			mGameThread.setRunning(true);
			mGameThread.start();
		}
	}

	/*
	 * Kills the game thread safely. Should be called when the surfaceView is
	 * destroyed
	 */
	public void stopGameThread() {
		// Stop the thread safely
		mGameThread.setRunning(false);
		mGameThread.setAlive(false);

		boolean retry = true;
		while (retry) {
			try {
				mGameThread.join();
				retry = false;
			} catch (InterruptedException e) {
				// Will try again to shut down thread
			}
		}

		mGameThread = null;
	}

	/* Start or stops the thread from rendering the screen */
	public void runThread(boolean running) {
		mGameThread.setRunning(running);
	}

	/* Creates the grid relative to the current position */
	private void createGrid() {
		if (mCurrentPos.x >= 0 && mCurrentPos.x < Grid.GRID_WIDTH
				&& mCurrentPos.y >= 0 && mCurrentPos.y < Grid.GRID_LENGTH) {

			Log.d(TAG, "Creating grid at " + mCurrentPos.toString());

			// Determine the number of blocks that can fit on the screen and add
			// a bit of padding
			int maxWidth = (int) Math.ceil((double) mScreenWidth
					/ (double) BlockType.GRID_SIZE) + 2;
			int maxHeight = (int) Math.ceil((double) mScreenHeight
					/ (double) BlockType.GRID_SIZE) + 2;

			// Round up to the nearest odd number to keep symmetry
			if (maxWidth % 2 == 0) {
				maxWidth++;
			}
			if (maxHeight % 2 == 0) {
				maxHeight++;
			}

			// Store width * height at a time
			mBlockViews = new BlockView[maxWidth][maxHeight];

			// Get the top left pixel point
			int topLeftX = (mScreenWidth / 2)
					- (BlockType.GRID_SIZE * maxWidth / 2);
			int topLeftY = (mScreenHeight / 2)
					- (BlockType.GRID_SIZE * maxHeight / 2);

			Point pixelPoint = new Point(topLeftX, topLeftY);

			// Get the top left grid point
			Point gridPoint = new Point(mCurrentPos.x - maxWidth / 2,
					mCurrentPos.y - maxHeight / 2);

			// Load the blocks from the database
			mBlocks = DatabaseLoader.loadBlocks(mActivity, Grid.grid,
					BlockType.types, gridPoint, new Point(gridPoint.x
							+ maxWidth, gridPoint.y + maxHeight));

			// Create the view for each block
			for (int i = 0; i < maxWidth; i++) {
				for (int j = 0; j < maxHeight; j++) {
					// If inside grid,
					if (gridPoint.x >= 0 && gridPoint.y >= 0
							&& gridPoint.x < Grid.GRID_WIDTH
							&& gridPoint.y < Grid.GRID_LENGTH) {

						// Create a new BlockView at each point
						Block block = mBlocks[i][j];
						mBlockViews[i][j] = new BlockView(block, new Point(
								pixelPoint.x, pixelPoint.y));

					}
					// Increment height
					gridPoint.y += 1;
					pixelPoint.y += BlockType.GRID_SIZE;
				}
				// Go back to top and increase x
				gridPoint.y -= maxHeight;
				pixelPoint.y -= (maxHeight * BlockType.GRID_SIZE);
				gridPoint.x += 1;
				pixelPoint.x += BlockType.GRID_SIZE;
			}

			// Set the touch listeners on each side of the origin block
			int midX = mBlockViews.length / 2;
			int midY = mBlockViews[0].length / 2;

			// Set the click listeners. Only if not null!
			mLeft = mBlockViews[midX - 1][midY];
			mRight = mBlockViews[midX + 1][midY];
			mUp = mBlockViews[midX][midY - 1];
			mDown = mBlockViews[midX][midY + 1];

			// The screen moves in the opposite direction you click!
			if (mLeft != null) {
				mLeft.setListener(this);
			}
			if (mRight != null) {
				mRight.setListener(this);
			}
			if (mUp != null) {
				mUp.setListener(this);
			}
			if (mDown != null) {
				mDown.setListener(this);
			}

		} else {
			Log.d(TAG, "Ship off screen!");
		}
	}

	/*
	 * Draws every item in the grid. Rendering is called by the attached view
	 * and is started and stopped by the game thread
	 */
	public void renderScreen(Canvas canvas) {

		if (mBlocks != null) {
			drawBlocks(canvas);
		}

		if (Ship.ship != null) {
			drawShip(canvas, Ship.ship);
		}

		// Stop motion if we are done moving
		if (!mIsMoving) {
			Log.d(TAG, "Stopping game thread");
			mGameThread.setRunning(false);
			createGrid();
		}
	}

	/* Draws the grid */
	public void drawBlocks(Canvas canvas) {
		// Move the grid before drawing
		if (mMovesLeft >= 0) {
			moveGrid(mMotion);
		}

		// Resize the grid before drawing
		if (mIsResizing) {
			resizeGrid();
		}

		// Draw each block
		for (int i = 0; i < mBlockViews.length; i++) {
			for (int j = 0; j < mBlockViews[i].length; j++) {
				if (mBlockViews[i][j] != null) {
					mBlockViews[i][j].draw(canvas);
				}
			}
		}
	}

	/* Draws the given ship sprite on the canvas */
	public void drawShip(Canvas canvas, Ship ship) {
		// Change the drill image
		mDrillSpin = (mDrillSpin + 1) % Ship.resourceIDs.length;

		// Draw the ship in the middle of the screen
		Point mid = new Point(mScreenWidth / 2 - BlockType.GRID_SIZE / 2,
				mScreenHeight / 2 - BlockType.GRID_SIZE / 2);

		canvas.drawBitmap(ship.getSprites()[mDrillSpin], mid.x, mid.y, null);
	}

	/* Resizes the grid */
	private void resizeGrid() {

		for (BlockType t : BlockType.types) {
			t.setSize(mSize);
		}

		// Recreate the grid
		createGrid();
		mIsResizing = false;
	}

	/* Interface for setting a resize */
	public void resize(int sizeDifference) {
		mSize = sizeDifference + BlockType.GRID_SIZE;
		Log.d(TAG, "Resizing to " + mSize);
		mIsResizing = true;
	}

	/* Adds the given point to each block in the grid */
	private void moveGrid(Point gridDirection) {

		// Change the location
		if (mMovesLeft > 1) {
			// Move each view p * speed units
			for (int i = 0; i < mBlockViews.length; i++) {
				for (int j = 0; j < mBlockViews[i].length; j++) {
					if (mBlockViews[i][j] != null) {
						Point location = mBlockViews[i][j].getLocation();
						location.x += gridDirection.x * mMoveSpeed;
						location.y += gridDirection.y * mMoveSpeed;
						mBlockViews[i][j].setLocation(location);
					}
				}
			}
		} else if (mMovesLeft == 1) {
			// Move to the destination pixel
			int dist = BlockType.GRID_SIZE % mMoveSpeed;
			for (int i = 0; i < mBlockViews.length; i++) {
				for (int j = 0; j < mBlockViews[i].length; j++) {
					if (mBlockViews[i][j] != null) {
						Point location = mBlockViews[i][j].getLocation();
						location.x += gridDirection.x * dist;
						location.y += gridDirection.y * dist;
						mBlockViews[i][j].setLocation(location);
					}
				}
			}
		} else {

			// Change current pos to new pos (opposite the direction traveled)
			mCurrentPos.x -= mMotion.x;
			mCurrentPos.y -= mMotion.y;
			Ship.ship.setLocation(mCurrentPos);

			// Stop running
			mIsMoving = false;
		}

		// Decrement counter every time
		mMovesLeft--;
	}

	/* Handle on click. Check each block adjacent to the origin */
	public void onTouch(MotionEvent event) {

		// Make sure they exist!!!!
		if (mLeft != null) {
			mLeft.handleActionDown((int) event.getX(), (int) event.getY(),
					new Point(1, 0));
		}
		if (mRight != null) {
			mRight.handleActionDown((int) event.getX(), (int) event.getY(),
					new Point(-1, 0));
		}
		if (mUp != null) {
			mUp.handleActionDown((int) event.getX(), (int) event.getY(),
					new Point(0, 1));
		}
		if (mDown != null) {
			mDown.handleActionDown((int) event.getX(), (int) event.getY(),
					new Point(0, -1));
		}
	}

	/* When a block is clicked, the thread resumes */
	@Override
	public void blockClicked(BlockView blockView, Point direction) {
		// If not moving,
		if (!mIsMoving) {
			mIsMoving = true;

			// Rotate the ship to face in the appropriate direction
			Ship.ship.rotateSprites(direction);

			// Update the database
			BlockDataSource blockDataSource = new BlockDataSource(mActivity);
			blockDataSource.open();
			try {

				BlockType oldType = blockView.getBlock().getType();

				int updateResult = 0;

				// Only change the block type if it isn't empty
				if (!oldType.getDescription().equals("empty")) {
					BlockTypeDataSource typeDataSource = new BlockTypeDataSource(
							mActivity);
					typeDataSource.open();
					BlockType emptyBlockType = typeDataSource.get("empty",
							mActivity);
					typeDataSource.close();
					blockView.getBlock().setType(emptyBlockType);
					updateResult = blockDataSource.update(blockView.getBlock(),
							Grid.grid);

					if (updateResult == 1) {
						// Add the old block type to the database
						CargoHoldDataSource holdDataSource = new CargoHoldDataSource(
								mActivity);
						holdDataSource.open();
						holdDataSource.add(oldType);
						holdDataSource.close();

						// Update the inventory to reflect the change
						mActivity.refreshInventory();
					}
				}

				// Get speed of motion, number of moves and the grid destination
				mMoveSpeed = (int) (Ship.ship.getSpeed() / oldType
						.getHardness());
				// Make sure speed is within range
				if (mMoveSpeed < Ship.MIN_SPEED) {
					mMoveSpeed = Ship.MIN_SPEED;
				}
				if(mMoveSpeed > Ship.MAX_SPEED) {
					mMoveSpeed = Ship.MAX_SPEED;
				}
				mMovesLeft = (int) (BlockType.GRID_SIZE / mMoveSpeed);
				mMotion = direction;

			} finally {
				blockDataSource.close();
			}
			// Start running the thread
			Log.d(TAG, "Starting to render");
			mGameThread.setRunning(true);
		}
	}

}
