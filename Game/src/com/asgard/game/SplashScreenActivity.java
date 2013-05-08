package com.asgard.game;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.asgard.game.db.DatabaseLoader;
import com.asgard.game.db.GameDatabaseSchema;

public class SplashScreenActivity extends Activity {

	private static final String TAG = SplashScreenActivity.class
			.getSimpleName();

	// Time to show the splash screen in ms
	protected final int SCREEN_TIME = 5000;

	//the file containing the difficulty preferences
	public static final String PREFS_NAME = "MyPrefsFile";

	// The thread that keeps the splash screen open
	protected Thread mThread;

	// A flag to ensure new activities only start
	protected boolean mIsAlive;

	/**
	 * Start a thread that runs for 5 seconds or until the user touches the
	 * screen
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mIsAlive = true;

		// Splash screen view
		setContentView(R.layout.splash_screen_layout);

		final SplashScreenActivity ss = this;

		// Run a thread
		mThread = new Thread() {
			public void run() {
				// Get the current time
				long startTime = System.currentTimeMillis();

				try {
					synchronized (this) {

						// Check if the database exists
						if (!dbExists(ss)) {
							// Restore preferences
							SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
							SharedPreferences.Editor editor = settings.edit();

							if (!settings.contains("difficulty")){
								editor.putInt("difficulty", 0);
								editor.commit();
							}
							

							Log.d(TAG, "Creating Database...");
							DatabaseLoader.createDatabase(ss);
						}

						Log.d(TAG, "Loading Database buffer");
						// Load the game data
						DatabaseLoader.loadGame(ss);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				// Make sure it has taken at least the minimum time
				long timeTaken = System.currentTimeMillis() - startTime;
				final long timeLeft = SCREEN_TIME - timeTaken;
				if (timeLeft > 0) {

					try {
						synchronized (this) {
							this.wait(timeLeft);
						}

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				Log.d(TAG, "Starting menu");

				// Start the main menu activity if still alive
				if (mIsAlive) {
					Intent mainActivity = new Intent(ss, MainMenuActivity.class);
					startActivity(mainActivity);
				}

				// Stop this activity
				finish();
			};
		};

		mThread.start();
	}

	/**
	 * End the splash screen thread on touch
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			synchronized (mThread) {
				mThread.notifyAll();
			}
		}
		return true;
	}

	/**
	 * Kill any background threads, and don't allow them to launch new
	 * activities
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();

		mIsAlive = false;
		synchronized (mThread) {
			mThread.notifyAll();
		}
	}

	/* Checks if the database exists yet. */
	private boolean dbExists(Context c) {
		File dbFile = c.getDatabasePath(GameDatabaseSchema.DATABASE_NAME);
		return dbFile.exists();
	}
}
