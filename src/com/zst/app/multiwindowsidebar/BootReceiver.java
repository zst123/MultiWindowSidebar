package com.zst.app.multiwindowsidebar;

import com.zst.app.multiwindowsidebar.sidebar.SidebarService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class BootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences main_prefs = context.getSharedPreferences(Common.KEY_PREFERENCE_MAIN,
				Context.MODE_PRIVATE);
		if (main_prefs.getBoolean(Common.PREF_KEY_START_ON_BOOT, Common.PREF_DEF_START_ON_BOOT)) {
			Intent i = new Intent(context, SidebarService.class);
			context.startService(i);
		}
	}
}