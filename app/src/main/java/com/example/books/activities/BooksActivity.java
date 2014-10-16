package com.example.books.activities;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.example.books.R;
import com.example.books.database.HttpRequestSenderAsync;
import com.example.books.database.OnRequestCompleteListener;
import com.example.books.fragments.AllBooksFragment;
import com.example.books.fragments.MyBooksFragment;
import com.example.books.fragments.NotificationsFragment;
import com.example.books.util.Constants;

public class BooksActivity extends MainActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_books);
		final ActionBar actionBar = getActionBar();
		actionBar.addTab(actionBar.newTab().setText(R.string.tab_all_books)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.tab_my_books)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.tab_notifications)
				.setTabListener(this));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		int tabPosition = tab.getPosition();
		Fragment fragment;
		if (tabPosition == 0) {
			fragment = new AllBooksFragment();
		} else if (tabPosition == 1) {
			fragment = new MyBooksFragment();
		} else {
			fragment = new NotificationsFragment();
		}
		getFragmentManager().beginTransaction()
				.replace(R.id.main_fragment_container, fragment).commit();

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
	}
}
