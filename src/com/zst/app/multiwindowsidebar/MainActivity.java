package com.zst.app.multiwindowsidebar;

import com.zst.app.multiwindowsidebar.preference.*;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;

public class MainActivity extends Activity {
		
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
}
