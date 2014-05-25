package com.zst.app.multiwindowsidebar.sidebar;

import com.zst.app.multiwindowsidebar.IntentUtil;
import com.zst.app.multiwindowsidebar.R;
import com.zst.app.multiwindowsidebar.Util;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.LayoutInflater;

@SuppressLint("ViewConstructor")
public abstract class SidebarDualItemView extends SidebarItemView {
	private String mPkg1;
	private String mPkg2;
	
	public SidebarDualItemView(final SidebarService service, LayoutInflater inflator) {
		super(service, inflator);
	}
	
	@Override
	public void launchApp(final int side) {
		if (TextUtils.isEmpty(mPkg1) || TextUtils.isEmpty(mPkg2)) {
			Util.toast(getContext(), getResources().getString(R.string.app_launch_error)
					+ "\n" + mPkg2 + "\n" + mPkg2);
		} else {
			try {
				final PackageManager pm = getContext().getPackageManager();
				final Intent intent1 = new Intent(pm.getLaunchIntentForPackage(mPkg1));
				final Intent intent2 = new Intent(pm.getLaunchIntentForPackage(mPkg2));

				final int width = getContext().getResources().getDisplayMetrics().widthPixels;
				final int height = getContext().getResources().getDisplayMetrics().heightPixels;
				final boolean landscape = width > height;
				
				IntentUtil.launchIntentDrag(getContext(), intent1, landscape ? IntentUtil.SIDE_LEFT : IntentUtil.SIDE_TOP);
				IntentUtil.launchIntentDrag(getContext(), intent2, landscape ? IntentUtil.SIDE_RIGHT : IntentUtil.SIDE_BOTTOM);
			} catch (NullPointerException e) {
				Util.toast(getContext(), getResources()
						.getString(R.string.app_launch_error) + "\n" + mPkg2 + "\n" + mPkg2);
			}
		}
	}
	
	public void setPkg(String pkg1, String pkg2) {
		mPkg1 = pkg1;
		mPkg2 = pkg2;
	}
	
	@Override 
	@Deprecated
	public void setPkg(String s) {}
}