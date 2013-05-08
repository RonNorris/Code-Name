package com.asgard.game;

import com.asgard.game.db.DatabaseLoader;
import com.asgard.game.db.GameDatabaseHelper;
import com.asgard.game.db.GameDatabaseSchema;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

//import com.asgard.game.db.*;

public class OptionsActivity extends Activity implements OnClickListener, OnSeekBarChangeListener {

	protected Button  mNewGame;
	protected Button mLeaderboard;
	protected Button mEasy;
	protected Button mMedium;
	protected Button mHard;
	protected SeekBar mSoundFx;
	protected SeekBar mMusic;
	
	public static final String PREFS_NAME = "MyPrefsFile";	
	
	protected enum difficulties {
		EASY(0), MEDIUM(1), HARD(2);
		
		int currentDif;
		
		private difficulties(int level){
			this.currentDif = level;
		}
		
		public int getValue(){
			return this.currentDif;
		}
	
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options_layout);

		mNewGame = (Button) findViewById(R.id.new_game_button);
		mLeaderboard = (Button) findViewById(R.id.leaderboard_button);
		mEasy = (Button) findViewById(R.id.easy_button);
		mMedium = (Button) findViewById(R.id.medium_button);
		mHard = (Button) findViewById(R.id.hard_button);
		mSoundFx = (SeekBar) findViewById(R.id.sound_fx_volume_seekbar);
		mMusic = (SeekBar) findViewById(R.id.music_volume_seekbar);

		mNewGame.setOnClickListener(this);
		mLeaderboard.setOnClickListener(this);
		mEasy.setOnClickListener(this);
		mMedium.setOnClickListener(this);
		mHard.setOnClickListener(this);
		mSoundFx.setOnSeekBarChangeListener(this);
		mMusic.setOnSeekBarChangeListener(this);

	}


	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    TextView toast = (TextView) findViewById(R.id.notification);

		
		switch (v.getId()) {

		case R.id.new_game_button:
			GameDatabaseHelper gdbHelper = new GameDatabaseHelper(this);
			SQLiteDatabase db = this.openOrCreateDatabase(
					GameDatabaseSchema.DATABASE_NAME, Context.MODE_PRIVATE, null);
			db.close();
			gdbHelper.onUpgrade(db, 1, 2);
			DatabaseLoader.createDatabase(this);
			break;
		case R.id.leaderboard_button:

			break;
		case R.id.easy_button:
			mEasy.setBackgroundColor(Color.BLUE);
			
			mMedium.setBackgroundColor(Color.GRAY);
			mHard.setBackgroundColor(Color.GRAY);
			
		    editor.putInt("difficulty", 0);
		    editor.commit();
		    
		    toast.setText("Changes won't take effect unless starting a new game!");

			break;
		case R.id.medium_button:
			mMedium.setBackgroundColor(Color.BLUE);
			
			mEasy.setBackgroundColor(Color.GRAY);
			mHard.setBackgroundColor(Color.GRAY);
			
			editor.putInt("difficulty", 1);
		    editor.commit();
		    
		    toast.setText("Changes won't take effect unless starting a new game!");

			break;
		case R.id.hard_button:
			mHard.setBackgroundColor(Color.BLUE);
			
			mEasy.setBackgroundColor(Color.GRAY);
			mMedium.setBackgroundColor(Color.GRAY);
			
			editor.putInt("difficulty", 2);
		    editor.commit();
		    
		    toast.setText("Changes won't take effect unless starting a new game!");

			break;
		default:
			return;
		}

	}

}
