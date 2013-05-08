package com.asgard.game;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asgard.game.db.CargoHoldDataSource;
import com.asgard.game.db.CraftItemDataSource;
import com.asgard.game.models.CraftItem;
import com.asgard.game.models.CargoHold;

public class CraftActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	/* The adapter for inventory items */
	protected CraftAdapter mAdapter;
	/* The layout containing detailed info of the item */
	protected RelativeLayout mDetailedInfo;
	
	private CraftItem item;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crafting_layout);

		// Get the craft items from the database (very fast)
		CraftItemDataSource craftItemSource = new CraftItemDataSource(this);
		craftItemSource.open();
		List<CraftItem> craftItems = craftItemSource.getAll(this);
		craftItemSource.close();

		// Get the detailed info layout
		mDetailedInfo = (RelativeLayout) findViewById(R.id.craft_item_detail_container);
		
		// Set up list view
		ListView storage = (ListView) findViewById(R.id.craft_item_list);
		storage.setOnItemClickListener(this);
		storage.setAdapter(new CraftAdapter(this, R.id.crafting_list_item_name, craftItems));
	}

	@Override
	public void onClick(View v) {
		
		TextView cost = (TextView) mDetailedInfo.findViewById(R.id.craft_item_cost);
		
		
		switch (v.getId()) {
		case R.id.craft_item_craft_button:
			
			CargoHoldDataSource inventorySrc = new CargoHoldDataSource(this);
			inventorySrc.open();
			CargoHold inventory;
			
			inventory = inventorySrc.getAll(this, 10);
			// Craft if possible
			int cargoIDofCost = (int)item.getCost().getID();
			
			if (item != null){
				if (inventory.numStock(cargoIDofCost) > 0){
					//CargoItem addedItem = inventory.getItem((int)item.getID()).mItem;
					
					/*if (inventory.numStock((int)addedItem.getID()) > 0){
						inventory.
					}*/
					//inventory.addItem(addedItem, 1);
					cost.setText("Added to Inventory!");
				}
				else{
					cost.setText("No " + item.getCost().getDescription() + "found");
				}
			}
			inventorySrc.close();
			break;
		}

	}

	/* Called when an item in the list is clicked */
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int arg2,
			long arg3) {
		
		// Makes sure it came from the list!
		switch (adapterView.getId()) {
		case R.id.craft_item_list:
			// Retrieve the correct cargo item
			item = (CraftItem) view.getTag();
			setCraftDescription(item);
			break;
		}
	}

	/* Sets the detailed description of the craft item */
	private void setCraftDescription(CraftItem item) {
		ImageView image = (ImageView) mDetailedInfo.findViewById(R.id.craft_item_image);
		TextView description = (TextView) mDetailedInfo.findViewById(R.id.craft_item_description);
		TextView cost = (TextView) mDetailedInfo.findViewById(R.id.craft_item_cost);
		Button button = (Button) mDetailedInfo.findViewById(R.id.craft_item_craft_button);
		
		if (image != null) {
			image.setImageBitmap(item.getSprite());
		}
		if(description != null) {
			description.setText(item.getDescription());
		}
		if(cost != null) {
			cost.setText(item.getCost().getDescription());
		}
		if(button != null) {
			button.setOnClickListener(this);
		}
		
	}

	public class CraftAdapter extends ArrayAdapter<CraftItem> {

		protected List<CraftItem> mItems;
		
		public CraftAdapter(Context context, int textViewResourceId,
				List<CraftItem> items) {
			super(context, textViewResourceId, items);
			
			mItems = items;
		}
		
		/* Return the correct view */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View v = convertView;
			
			// Inflate if not reusing
			if (convertView == null) {
				LayoutInflater li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				v = li.inflate(R.layout.crafting_list_item, null);
				
				TextView tv = (TextView) v.findViewById(R.id.crafting_list_item_name);
				tv.setText(mItems.get(position).getDescription());
				
				// Tag with the item
				v.setTag(mItems.get(position));
			}
			
			return v;
		}
	}
}
