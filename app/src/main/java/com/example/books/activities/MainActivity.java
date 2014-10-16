package com.example.books.activities;

import android.app.ActionBar;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public abstract class MainActivity extends Activity implements TabListener {
	private final String SELECTED_STATE_NAVIGATION_MENU = "selected_state_item";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// set up the action bar of the main activity to show tabs.
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(false);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState.containsKey(SELECTED_STATE_NAVIGATION_MENU)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(SELECTED_STATE_NAVIGATION_MENU));
		}
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt(SELECTED_STATE_NAVIGATION_MENU, getActionBar()
				.getSelectedNavigationIndex());
		super.onSaveInstanceState(outState);
	}
}
