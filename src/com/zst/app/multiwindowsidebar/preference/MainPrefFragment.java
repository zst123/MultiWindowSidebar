package com.zst.app.multiwindowsidebar.preference;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;

import com.zst.app.multiwindowsidebar.Common;
import com.zst.app.multiwindowsidebar.IntentUtil;
import com.zst.app.multiwindowsidebar.R;
import com.zst.app.multiwindowsidebar.Util;
import com.zst.app.multiwindowsidebar.adapter.AppListActivity;
import com.zst.app.multiwindowsidebar.sidebar.SidebarService;

public class MainPrefFragment extends PreferenceFragment implements OnPreferenceClickListener,
		OnPreferenceChangeListener {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesName(Common.KEY_PREFERENCE_MAIN);
		addPreferencesFromResource(R.xml.main_pref);
		
		// Click
		findPreference(Common.PREF_KEY_TOGGLE_SERVICE).setOnPreferenceClickListener(this);
		findPreference(Common.PREF_KEY_SELECT_APPS).setOnPreferenceClickListener(this);
		
		// Change
		findPreference(Common.PREF_KEY_LAUNCH_MODE).setOnPreferenceChangeListener(this);

		// Refresh Change Listener
		findPreference(Common.PREF_KEY_KEEP_IN_BG).setOnPreferenceChangeListener(sRefreshChangeListener);
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
		}
		return true;
	}
	
	final OnPreferenceChangeListener sRefreshChangeListener = new OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			Util.refreshService(getActivity());
			return true;
		}
		
	};
	
	final OnPreferenceChangeListener sResetChangeListener = new OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			Util.resetService(getActivity());
			return true;
		}
		
	};
}