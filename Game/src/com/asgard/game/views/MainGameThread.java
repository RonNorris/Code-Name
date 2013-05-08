package com.asgard.game.views;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class MainGameThread extends Thread {

	private final String TAG = MainGameThread.class.getSimpleName();

	/* Members used for drawing and updating view */
	private SurfaceHolder mSurfaceHolder;
	private MainGameView mGameView;

	/* Flags for state */
	private boolean mRunning;
	private boolean mAlive;

	public MainGameThread(SurfaceHolder surfaceHolder, MainGameView gameView) {
		super("Main game thread");
		this.mSurfaceHolder = surfaceHolder;
		this.mGameView = gameView;
	}

	/* Setting to false causes spin-wait */
	public void setRunning(boolean running) {
		this.mRunning = running;
	}

	public boolean isRunning() {
		return mRunning;
	}

	/* Setting to false ends thread */
	public void setAlive(boolean alive) {
		this.mAlive = alive;
	}

	@Override
	public void run() {
		Canvas canvas;
		Log.d(TAG, "Starting game loop");
		while (mAlive) {
			try {
				// Sleep for a small amount of time
				// TODO Find a less hacky implementation!
				// TODO Just delete old thread and create new one?
				Thread.sleep(1);
				while (mRunning) {
					canvas = null;
					try {
						// Must lock canvas before drawing
						canvas = mSurfaceHolder.lockCanvas();
						synchronized (mSurfaceHolder) {
							if (canvas != null) {
								mGameView.onDraw(canvas);
							}
						}
					} finally {
						if (canvas != null) {
							mSurfaceHolder.unlockCanvasAndPost(canvas);
						}
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Log.d(TAG, "Thread shut down cleanly");
	}
}
