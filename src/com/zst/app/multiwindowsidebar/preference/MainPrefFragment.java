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

public class MainPrefFragment extends PreferenceFragment implements OnPreferenceClickListener,
		OnPreferenceChangeListener {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesName(Common.KEY_PREFERENCE_MAIN);
		addPreferencesFromResource(R.xml.main_pref);
		
		// Click
		findPreference(Common.PREF_KEY_SELECT_APPS).setOnPreferenceClickListener(this);
		
		// Change
		findPreference(Common.PREF_KEY_DRAG_LAUNCH_MODE).setOnPreferenceChangeListener(this);
		findPreference(Common.PREF_KEY_TAP_LAUNCH_MODE).setOnPreferenceChangeListener(this);

		// Refresh Change Listener
		findPreference(Common.PREF_KEY_KEEP_IN_BG).setOnPreferenceChangeListener(sRefreshChangeListener);
	}
	
	@Override
	public boolean onPreferenceClick(Preference p) {
		String k = p.getKey();
		if (k.equals(Common.PREF_KEY_SELECT_APPS)) {
			getActivity().startActivity(new Intent(getActivity(), AppListActivity.class));
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onPreferenceChange(Preference pref, Object newValue) {
		String k = pref.getKey();
		if (k.equals(Common.PREF_KEY_DRAG_LAUNCH_MODE)) {
			Util.refreshService(getActivity());
			
			if (newValue instanceof String) {
				switch (Integer.parseInt((String) newValue)) {
				case IntentUtil.DragMode.XHFW_PORTRAIT:
				case IntentUtil.DragMode.XHFW_LANDSCAPE:
					if (!Util.isAppInstalled(getActivity().getPackageManager(),
							Common.PKG_XHALOFLOATINGWINDOW)) {
						Util.dialog(getActivity(), R.string.pref_launch_mode_error_xhfw);
					}
					break;
				case IntentUtil.DragMode.XHFW3_PORTRAIT:
				case IntentUtil.DragMode.XHFW3_LANDSCAPE:
					if (!Util.isAppInstalled(getActivity().getPackageManager(),
							Common.PKG_XHFW3)) {
						Util.dialog(getActivity(), R.string.pref_launch_mode_error_xhfw3);
					}
					break;
				case IntentUtil.DragMode.XMULTI_WINDOW:
					if (!Util.isAppInstalled(getActivity().getPackageManager(),
							Common.PKG_XMULTIWINDOW)) {
						Util.dialog(getActivity(), R.string.pref_launch_mode_error_xmw);
					}
					break;
				}
			}
		} else if (k.equals(Common.PREF_KEY_TAP_LAUNCH_MODE)) {
			Util.refreshService(getActivity());
			
			if (newValue instanceof String) {
				switch (Integer.parseInt((String) newValue)) {
				case IntentUtil.TapMode.XHFW_TOP:
				case IntentUtil.TapMode.XHFW_BOTTOM:
				case IntentUtil.TapMode.XHFW_LEFT:
				case IntentUtil.TapMode.XHFW_RIGHT:
				case IntentUtil.TapMode.XHFW_CENTER:
					if (!Util.isAppInstalled(getActivity().getPackageManager(),
							Common.PKG_XHALOFLOATINGWINDOW)) {
						Util.dialog(getActivity(), R.string.pref_launch_mode_error_xhfw);
					}
					break;
				case IntentUtil.TapMode.XHFW3_TOP:
				case IntentUtil.TapMode.XHFW3_BOTTOM:
				case IntentUtil.TapMode.XHFW3_LEFT:
				case IntentUtil.TapMode.XHFW3_RIGHT:
				case IntentUtil.TapMode.XHFW3_CENTER:
					if (!Util.isAppInstalled(getActivity().getPackageManager(),
							Common.PKG_XHFW3)) {
						Util.dialog(getActivity(), R.string.pref_launch_mode_error_xhfw3);
					}
					break;
				case IntentUtil.TapMode.XMULTI_WINDOW_TOP:
				case IntentUtil.TapMode.XMULTI_WINDOW_BOTTOM:
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