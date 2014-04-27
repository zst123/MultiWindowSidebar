package com.zst.app.multiwindowsidebar;

import com.zst.app.multiwindowsidebar.sidebar.SidebarService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RestartReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context, SidebarService.class);
		if (intent.getAction().equals(Common.PKG_THIS + ".START")) {
			context.startService(i);
		} else if (intent.getAction().equals(Common.PKG_THIS + ".STOP")) {
			SidebarService.stopSidebar(context);
		} else if (intent.getAction().equals(Common.PKG_THIS + ".TOGGLE")) {
			if (SidebarService.isRunning) {
				SidebarService.stopSidebar(context);
			} else if (SidebarService.isSidebarShown) {
				context.startService(i);
				SidebarService.stopSidebar(context);
			} else {
				context.startService(i);
			}
		}
	}
}