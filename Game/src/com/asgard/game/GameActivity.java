package com.asgard.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.asgard.game.db.CargoHoldDataSource;
import com.asgard.game.db.DatabaseLoader;
import com.asgard.game.models.CargoHold;
import com.asgard.game.models.CargoHold.CargoContainer;
import com.asgard.game.models.Ship;
import com.asgard.game.models.BlockType;

public class GameActivity extends Activity implements OnClickListener {

	private static final String TAG = GameActivity.class.getSimpleName();

	/* View objects used for displaying inventory */
	protected RelativeLayout mInventoryView;
	protected ListView mInventoryList;
	protected InventoryAdapter mAdapter;

	/* The ships current inventory */
	protected CargoHold mInventory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_layout);

		// Set the button handlers
		ImageButton inventoryButton = (ImageButton) findViewById(R.id.craft_hud_button);
		ImageButton craftButton = (ImageButton) findViewById(R.id.inventory_hud_button);

		if (inventoryButton != null) {
			inventoryButton.setOnClickListener(this);
		}
		if (craftButton != null) {
			craftButton.setOnClickListener(this);
		}

		createInventoryView();
	}

	/* Creates the inventory view WITHOUT displaying it */
	private void createInventoryView() {

		// Take up the grid size
		int width = BlockType.GRID_SIZE;
		// and show 6 items at time
		int height = width * 6;

		updateInventory();

		// Inflate the Inventory View and set parameters
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		mInventoryView = (RelativeLayout) inflater.inflate(
				R.layout.inventory_view, null);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				width, height);
		params.alignWithParent = true;
		params.addRule(RelativeLayout.ABOVE, R.id.inventory_hud_button);
		mInventoryView.setLayoutParams(params);

		// Set the view to invisible
		mInventoryView.setVisibility(View.INVISIBLE);

		// Get the list view
		mInventoryList = (ListView) mInventoryView.getChildAt(0);
		
		// Set the adapter
		mAdapter = new InventoryAdapter(this, R.id.inventory_amount, mInventory);
		mInventoryList.setAdapter(mAdapter);

		// Add the inventory view to the games heads up display view
		ViewGroup home = (ViewGroup) findViewById(R.id.game_hud);
		if (home != null) {
			home.addView(mInventoryView);
		}
	}

	/* Update the data */
	private void updateInventory() {
		// Get the number of items in the cargo hold to determine size
		CargoHoldDataSource cargoDataSource = new CargoHoldDataSource(this);
		cargoDataSource.open();
		// TODO save size of the cargo hold in ship database!
		mInventory = cargoDataSource.getAll(this, 10);
		if (mAdapter != null) {
			mAdapter.clear();
			for (CargoContainer t : mInventory.getInventory()) {
				mAdapter.add(t);
			}
			mAdapter.notifyDataSetChanged();
		}
		cargoDataSource.close();
	}

	/* Resets the data source to update the list view */
	public void refreshInventory() {
		updateInventory();
		mAdapter = new InventoryAdapter(this, R.id.inventory_amount, mInventory);
		mInventoryList.setAdapter(mAdapter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Save the ship
		DatabaseLoader.saveShip(getApplicationContext(), Ship.ship);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "Destroying...");
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.craft_hud_button:
			intent = new Intent(GameActivity.this, CraftActivity.class);
			startActivity(intent);
			break;
		case R.id.inventory_hud_button:
			if (mInventoryView != null) {
				if (mInventoryView.getVisibility() == View.VISIBLE) {
					mInventoryView.setVisibility(View.INVISIBLE);
				} else {
					mInventoryView.setVisibility(View.VISIBLE);
				}
			}
			break;
		}
	}

	/**
	 * Adapter used by the inventory list view
	 * 
	 * @author Benjamin
	 * 
	 */
	public class InventoryAdapter extends ArrayAdapter<CargoContainer> {

		protected CargoHold mInventory;

		public InventoryAdapter(Context context, int textViewResourceId,
				CargoHold inventory) {
			super(context, textViewResourceId, inventory.getInventory());

			mInventory = inventory;
		}

		/* Sets the data source */
		public void setInventory(CargoHold inventory) {
			mInventory = inventory;
			notifyDataSetChanged();
		}

		/* Return the correct view */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View v = convertView;

			// Inflate if not reusing
			if (convertView == null) {
				LayoutInflater li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				v = li.inflate(R.layout.inventory_list_item, null);

			}

			ImageView iv = (ImageView) v.findViewById(R.id.inventory_image);

			// If inventory slot isn't empty, draw bitmap
			if (mInventory.getItem(position).mNumber > 0) {
				iv.setImageBitmap(getItem(position).mItem.getSprite());
			}
			// Otherwise draw empty bitmap
			else {
				iv.setImageResource(R.drawable.empty_inventory_box);
			}

			// Tag with the item
			v.setTag(mInventory.getItem(position));

			return v;
		}
	}
}
