package com.asgard.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainMenuActivity extends Activity implements OnClickListener {

	protected TextView mStart;
	protected TextView mHelp;
	protected TextView mOptions;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu_layout);
		
		mStart = (TextView) findViewById(R.id.main_menu_start_option);
		mHelp = (TextView) findViewById(R.id.main_menu_help_option);
		mOptions = (TextView) findViewById(R.id.main_menu_options_option);
		
		mStart.setOnClickListener(this);
		mHelp.setOnClickListener(this);
		mOptions.setOnClickListener(this);
	}

	// Start a new activity on click
	@Override
	public void onClick(View v) {

		Intent start;
		
		switch (v.getId()) {
		case R.id.main_menu_start_option:
			start = new Intent(MainMenuActivity.this, GameActivity.class);
			startActivity(start);
			break;
		case R.id.main_menu_help_option:
			start = new Intent(MainMenuActivity.this, HelpActivity.class);
			startActivity(start);
			break;
		case R.id.main_menu_options_option:
			start = new Intent(MainMenuActivity.this, OptionsActivity.class);
			startActivity(start);
			break;
		default:
			return;
		}

	}

}
