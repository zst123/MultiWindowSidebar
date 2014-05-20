package com.zst.app.multiwindowsidebar;

import com.zst.app.multiwindowsidebar.adapter.AppListActivity;
import com.zst.app.multiwindowsidebar.sidebar.SidebarService;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;

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
				return new MainPreference();
			} else if (position == PAGES.APPEARENCE.position) {
				
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
	
	public static class MainPreference extends PreferenceFragment implements
			OnPreferenceClickListener, OnPreferenceChangeListener {
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			getPreferenceManager().setSharedPreferencesName(Common.KEY_PREFERENCE_MAIN);
			addPreferencesFromResource(R.xml.main_pref);
			findPreference(Common.PREF_KEY_TOGGLE_SERVICE).setOnPreferenceClickListener(this);
			findPreference(Common.PREF_KEY_SELECT_APPS).setOnPreferenceClickListener(this);
			findPreference(Common.PREF_KEY_KEEP_IN_BG).setOnPreferenceChangeListener(this);
			findPreference(Common.PREF_KEY_LAUNCH_MODE).setOnPreferenceChangeListener(this);
			findPreference(Common.PREF_KEY_SIDEBAR_POSITION).setOnPreferenceChangeListener(this);
			findPreference(Common.PREF_KEY_SIDEBAR_THEME).setOnPreferenceChangeListener(this);
			findPreference(Common.PREF_KEY_TAB_SIZE).setOnPreferenceChangeListener(this);
			findPreference(Common.PREF_KEY_LABEL_SIZE).setOnPreferenceChangeListener(this);
			findPreference(Common.PREF_KEY_TAB_ALPHA_HIDDEN).setOnPreferenceChangeListener(this);
			findPreference(Common.PREF_KEY_ANIM_TIME).setOnPreferenceChangeListener(this);
			findPreference(Common.PREF_KEY_COLUMN_NUMBER).setOnPreferenceChangeListener(this);
			findPreference(Common.PREF_KEY_LABEL_COLOR).setOnPreferenceChangeListener(this);
		}
		
		@Override
		public boolean onPreferenceClick(Preference p) {
			String k = p.getKey();
			if (k.equals(Common.PREF_KEY_SELECT_APPS)) {
				getActivity().startActivity(new Intent(getActivity(), AppListActivity.class));
				return true;
			} else if (k.equals(Common.PREF_KEY_TOGGLE_SERVICE)) {
				if (SidebarService.isRunning) {
					SidebarService.stopSidebar(getActivity());
				} else {
					getActivity().startService(new Intent(getActivity(), SidebarService.class));
				}
				return true;
			}
			return false;
		}
		
		@Override
		public boolean onPreferenceChange(Preference pref, Object newValue) {
			String k = pref.getKey();
			if (k.equals(Common.PREF_KEY_LAUNCH_MODE)) {
				Util.refreshService(getActivity());
				
				if (newValue instanceof String) {
					switch (Integer.parseInt((String) newValue)) {
					case IntentUtil.MODE_XHALO_FLOATINGWINDOW:
						if (!Util.isAppInstalled(getActivity().getPackageManager(),
								Common.PKG_XHALOFLOATINGWINDOW)) {
							Util.dialog(getActivity(), R.string.pref_launch_mode_error_xhfw);
						}
						break;
					case IntentUtil.MODE_XMULTI_WINDOW:
						if (!Util.isAppInstalled(getActivity().getPackageManager(),
								Common.PKG_XMULTIWINDOW)) {
							Util.dialog(getActivity(), R.string.pref_launch_mode_error_xmw);
						}
						break;
					}
				}
			} else if (k.equals(Common.PREF_KEY_TAB_SIZE)
					|| k.equals(Common.PREF_KEY_COLUMN_NUMBER)
					|| k.equals(Common.PREF_KEY_KEEP_IN_BG)
					|| k.equals(Common.PREF_KEY_LABEL_COLOR)
					|| k.equals(Common.PREF_KEY_TAB_ALPHA_HIDDEN)
					|| k.equals(Common.PREF_KEY_LABEL_SIZE)) {
				Util.refreshService(getActivity());
			} else if (k.equals(Common.PREF_KEY_SIDEBAR_POSITION) ||
					k.equals(Common.PREF_KEY_SIDEBAR_THEME) ||
					k.equals(Common.PREF_KEY_ANIM_TIME)) {
				Util.resetService(getActivity());
			}
			return true;
		}
	}
}
