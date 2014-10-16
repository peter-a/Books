package com.example.books.activities;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;

import com.example.books.R;
import com.example.books.database.HttpRequestSenderAsync;
import com.example.books.datalayer.Data;
import com.example.books.fragments.SignInFragment;
import com.example.books.fragments.SignUpFragment;
import com.example.books.util.Constants;

public class SignInActivity extends MainActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		final ActionBar actionBar = getActionBar();
		// for each of the sections in the activity add a tab to the action bar.
		actionBar.addTab(actionBar.newTab().setText(R.string.tab_sign_in)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.tab_sign_up)
				.setTabListener(this));
        Data.getInstance().getAllBooks();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
		int position = tab.getPosition();
		Fragment fragment;
		if (position == 0) {
			fragment = new SignInFragment();
		} else {
			fragment = new SignUpFragment();
		}
		getFragmentManager().beginTransaction()
				.replace(R.id.sign_in_fragment_container, fragment).commit();
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}
}
