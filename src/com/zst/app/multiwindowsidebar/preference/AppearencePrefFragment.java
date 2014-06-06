package com.zst.app.multiwindowsidebar.preference;

import com.zst.app.multiwindowsidebar.Common;
import com.zst.app.multiwindowsidebar.R;
import com.zst.app.multiwindowsidebar.Util;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.Preference.OnPreferenceChangeListener;

public class AppearencePrefFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesName(Common.KEY_PREFERENCE_MAIN);
		addPreferencesFromResource(R.xml.appearence_pref);
		
		findPreference(Common.PREF_KEY_SIDEBAR_THEME).setOnPreferenceChangeListener(
				new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				Util.refreshTheme(getActivity());
				return true;
			}
		});
		
		// Reset Change Listener
		findPreference(Common.PREF_KEY_ANIM_TIME).setOnPreferenceChangeListener(sResetChangeListener);
		
		// Refresh Change Listener
		findPreference(Common.PREF_KEY_SIDEBAR_POSITION).setOnPreferenceChangeListener(sRefreshChangeListener);
		findPreference(Common.PREF_KEY_TAB_SIZE).setOnPreferenceChangeListener(sRefreshChangeListener);
		findPreference(Common.PREF_KEY_LABEL_SIZE).setOnPreferenceChangeListener(sRefreshChangeListener);
		findPreference(Common.PREF_KEY_TAB_ALPHA_HIDDEN).setOnPreferenceChangeListener(sRefreshChangeListener);
		findPreference(Common.PREF_KEY_COLUMN_NUMBER).setOnPreferenceChangeListener(sRefreshChangeListener);
		findPreference(Common.PREF_KEY_LABEL_COLOR).setOnPreferenceChangeListener(sRefreshChangeListener);
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
