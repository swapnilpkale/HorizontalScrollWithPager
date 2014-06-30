package com.agreeya.kdmclienttestapp;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_activity);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Profile");
		//actionBar.setIcon(R.drawable.ic_action_profile_image);
		/*
		 * 
		 * actionBar.setDisplayShowCustomEnabled(true);
		 * 
		 * View customView = getLayoutInflater().inflate(
		 * R.layout.custom_action_bar, null);
		 * actionBar.setDisplayShowHomeEnabled(false);
		 * actionBar.setCustomView(customView);
		 * actionBar.setHomeButtonEnabled(false);
		 */

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
