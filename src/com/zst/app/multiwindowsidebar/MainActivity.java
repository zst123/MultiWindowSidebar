package com.zst.app.multiwindowsidebar;

import com.zst.app.multiwindowsidebar.preference.*;
import com.zst.app.multiwindowsidebar.sidebar.SidebarService;

import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;

public class MainActivity extends Activity {
	public static final int MENU_TOGGLE = 1;
		
	public enum PAGES {
		MAIN(0),
		APPEARENCE(1);

		final int position;
		PAGES(int pos) {
			position = pos;
		}
	};
	
	ViewPager mViewPager;
	PageAdapter mPageAdapter;
	
	// Thanks: http://just-another-blog.net/programming/how-to-implement-horizontal-view-swiping-with-tabs/
	// Thanks: http://stackoverflow.com/questions/15845632/adding-preferencefragment-to-fragmentpageradapter
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewpager);
		// Display the fragment as the main content.
		
		mPageAdapter = new PageAdapter(getFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mPageAdapter);
	}
	
	public class PageAdapter extends FragmentPagerAdapter {
		public PageAdapter(FragmentManager fm) {
			super(fm);
			
		}
		
		@Override
		public Fragment getItem(int position) {
			if (position == PAGES.MAIN.position) {
				return new MainPrefFragment();
			} else if (position == PAGES.APPEARENCE.position) {
				return new AppearencePrefFragment();
			}
			return new Fragment();
		}
		
		@Override
		public int getCount() {
			return PAGES.values().length;
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			if (position == PAGES.MAIN.position) {
				return getString(R.string.pref_main_header);
			} else if (position == PAGES.APPEARENCE.position) {
				return getString(R.string.pref_appearance_header);
			}
			return "?";
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, MENU_TOGGLE, 0, R.string.pref_toggle_service_title)
			.setIcon(SidebarService.isRunning ?
					R.drawable.ic_menu_toggle_off : R.drawable.ic_menu_toggle_on)
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_TOGGLE:
			if (SidebarService.isRunning) {
				SidebarService.stopSidebar(this);
				item.setIcon(R.drawable.ic_menu_toggle_on);
			} else {
				startService(new Intent(this, SidebarService.class));
				item.setIcon(R.drawable.ic_menu_toggle_off);
			}
		}
		return super.onOptionsItemSelected(item);
	}
}
