package com.agreeya.kdmclienttestapp;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.Menu;
import android.view.View;

public class MainActivity extends FragmentActivity {

	private static final String[] CONTENT = new String[] { "Wi-fi", "Browser",
			"Settings", "Restriction", "Application", "Bluetooth", "Wi-fi",
			"Browser", "Settings", "Restriction", "Application", "Bluetooth" };

	public static final int[] COLORS = new int[] { Color.MAGENTA, Color.CYAN,
			Color.BLUE, Color.GRAY, Color.GREEN, Color.RED, Color.MAGENTA,
			Color.CYAN, Color.BLUE, Color.GRAY, Color.GREEN, Color.RED, };

	public static int display_width = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ActionBar actionBar = getActionBar(); 
		actionBar.setDisplayShowCustomEnabled(true);
		
		View customView = getLayoutInflater().inflate(
				R.layout.custom_action_bar, null);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setCustomView(customView);
		actionBar.setHomeButtonEnabled(false);

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		display_width = size.x;

		FragmentPagerAdapter adapter = new ScrollAdapter(
				getSupportFragmentManager());

		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);

		CustomisedScroll indicator = (CustomisedScroll) findViewById(R.id.indicator);
		indicator.setViewPager(pager);

	}

	class ScrollAdapter extends FragmentPagerAdapter {
		public ScrollAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return TestFragment.newInstance(CONTENT[position % CONTENT.length],
					COLORS[position % CONTENT.length]);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length].toUpperCase();
		}

		@Override
		public int getCount() {
			return CONTENT.length;
		}
	}
}
