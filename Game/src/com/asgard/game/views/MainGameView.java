package com.asgard.game.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.asgard.game.GameActivity;
import com.asgard.game.models.Grid;

/**
 * 
 * @author Benjamin
 * 
 *         The view that contains the game. Since frame-rate requirements are
 *         very low, each grid item can be its own image view (maybe?)
 */
public class MainGameView extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = MainGameView.class.getSimpleName();

	/* The surface being drawn upon */
	protected Surface mSurface;

	/* The current grid */
	protected Grid mGrid;

	/* The manager which blocks to show */
	protected GameScreenManager mManager;

	/* The activity that called this view */
	protected GameActivity mActivity;

	/* Pinch to zoom stuff */
	private static final int NONE = 0;
	private static final int ZOOM = 1;
	private int mPinchMode = NONE;
	private float mOldDist = 1f;

	public MainGameView(Context context) {
		super(context);

		// Intercept surface events in this view
		getHolder().addCallback(this);

		// Make sure the view is focusable so it can handle events
		setFocusable(true);

		mActivity = (GameActivity) context;
	}

	public MainGameView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);

		// Intercept surface events in this view
		getHolder().addCallback(this);

		// Make sure the view is focusable so it can handle events
		setFocusable(true);

		mActivity = (GameActivity) context;
	}

	/* Start any threads and grab any resources after surface has been created */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		// Create a manager for the game, set at the current ship location
		mManager = new GameScreenManager(mActivity, this, getWidth(),
				getHeight());

		mManager.startGameThread();
	}

	/* Release resources and threads after surface is destroyed */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mManager.stopGameThread();

	}

	/* Draw the view here */
	@Override
	protected void onDraw(Canvas canvas) {
		// Draw the background
		canvas.drawColor(Color.BLACK);

		mManager.renderScreen(canvas);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	/* Handles the touch event on the view */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {

		case MotionEvent.ACTION_DOWN:
			// Send to block manager
			mManager.onTouch(event);
			break;

		case MotionEvent.ACTION_POINTER_DOWN:
			// Calculate the distance between points
			mOldDist = pinchDistance(event);
			Log.d(TAG, "Old pinch disance = " + mOldDist);
			// If distance great enough, start zoom mode
			if (mOldDist > 10f) {
				mPinchMode = ZOOM;
				Log.d(TAG, "mode=ZOOM");
			}
			break;

		case MotionEvent.ACTION_POINTER_UP:
			mPinchMode = NONE;
			Log.d(TAG, "mode=NONE");
			break;

		case MotionEvent.ACTION_MOVE:
			if (mPinchMode == ZOOM) {
				// Get the distance between fingers
				float dist = pinchDistance(event);

				// Only check if fingers are > than 10 pixels apart!
				if (dist > 10f) {
					float scale = dist / mOldDist;

					// Use scale to alter size of blocks
					if (scale > 1.1f) {
						// Bigger than 1.1 so make bigger
						Log.d(TAG, "Zooming in");
						// Set new dist to old dist
						mOldDist = dist;
						mManager.resize(1);
					} else if (scale < 0.9f) {
						// Smaller than 0.9 so make smaller
						Log.d(TAG, "Zooming out");
						// Set new dist to old dist
						mOldDist = dist;
						mManager.resize(-1);
					}
				}
			}
			break;
		}
		return true;
	}

	/* Used to determine the distance between two points during a pinch gesture */
	private float pinchDistance(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) FloatMath.sqrt(x * x + y * y);
	}

}
