package com.zst.app.multiwindowsidebar.sidebar;

import java.util.List;

import com.zst.app.multiwindowsidebar.Common;
import com.zst.app.multiwindowsidebar.IntentUtil;
import com.zst.app.multiwindowsidebar.R;
import com.zst.app.multiwindowsidebar.Util;
import com.zst.app.multiwindowsidebar.adapter.AppChooserDialog;
import com.zst.app.multiwindowsidebar.adapter.AppChooserAdapter.AppItem;
import com.zst.app.multiwindowsidebar.adapter.AppListActivity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class SidebarMenuOptions {
	
	private static String mPkg0 = "";
	private static String mPkg1 = "";
	
	public static void launchEditApps(Context c) {
		Intent i = new Intent(c, AppListActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		c.startActivity(i);
	}
	
	public static void createGroupFromTop2(Context c) {
		mPkg0 = "";
		mPkg1 = "";
		
		ActivityManager am = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(5); 
		for (RunningTaskInfo info : list) {
			if (info.numActivities > 0) {
				if (TextUtils.isEmpty(mPkg0)) {
					mPkg0 = info.baseActivity.getPackageName();
				} else if (TextUtils.isEmpty(mPkg1)) {
					mPkg1 = info.baseActivity.getPackageName();
				} else {
					break;
				}
			}
		}
		showGroupCreatorDialog(null, c, true);
	}
	
	public static void showGroupCreatorDialog(final AppListActivity activity, final Context c, boolean use_values) {
		final Dialog dialog = new Dialog(activity != null ? activity : c);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_add_group);
		if (activity == null) {
			dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
		}
		// We are starting this from a service. Set the type to phone
		// so we do not need a activity token to show the dialog
		
		final TextView tV_extra = (TextView) dialog.findViewById(R.id.extra);
		final TextView tV_warning = (TextView) dialog.findViewById(R.id.warning);
		final TextView tV_pkg0 = (TextView) dialog.findViewById(R.id.tV_pkg0);
		final TextView tV_pkg1 = (TextView) dialog.findViewById(R.id.tV_pkg1);
		final Button btnCreate = (Button) dialog.findViewById(R.id.btn_create);
		final Button btn1st = (Button) dialog.findViewById(R.id.btn_choose0);
		final Button btn2nd = (Button) dialog.findViewById(R.id.btn_choose1);
		
		final AppChooserDialog acd = new AppChooserDialog(c) {
			@Override
			public void onListViewItemClick(AppItem info, int id) {
				if (id == R.id.btn_choose0) {
					mPkg0 = info.packageName;
					tV_pkg0.setText(info.packageName);
				} else if (id == R.id.btn_choose1) {
					mPkg1 = info.packageName;
					tV_pkg1.setText(info.packageName);
				}
			}
		};
		if (activity == null) {
			acd.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
		}
		
		if (IntentUtil.getLaunchMode() == IntentUtil.MODE_XHALO_FLOATINGWINDOW
			|| IntentUtil.getLaunchMode() == IntentUtil.MODE_XMULTI_WINDOW){
			tV_warning.setVisibility(View.GONE);
		}
		
		
		if (use_values) {
			tV_pkg0.setText(mPkg0);
			tV_pkg1.setText(mPkg1);
		} else {
			mPkg0 = "";
			mPkg1 = "";
			tV_extra.setVisibility(View.GONE);
		}
		
		final View.OnClickListener listener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btn_choose0:
				case R.id.btn_choose1:
					acd.show(v.getId());
					break;
				case R.id.btn_create:
					if (TextUtils.isEmpty((mPkg0).trim())) {
						Util.toast(c, R.string.create_group_empty1);
						break;
					} else if (TextUtils.isEmpty((mPkg1).trim())) {
						Util.toast(c, R.string.create_group_empty2);
						break;
					}
					SharedPreferences pref = c.getSharedPreferences(Common.KEY_PREFERENCE_APPS, Context.MODE_PRIVATE);
					Editor editor = pref.edit();
					editor.putInt(mPkg0 + Common.SEPARATOR_GROUP + mPkg1, pref.getAll().size());
					editor.commit();
					Util.toast(c, c.getResources().getString(R.string.create_group_done)
							+ "\n\n" + mPkg0 + "\n" + mPkg1);
					if (activity != null) {
						activity.updateList();
					} else {
						Util.refreshService(c);
					}
					dialog.dismiss();
					break;
				}
			}
		};
		btn1st.setOnClickListener(listener);
		btn2nd.setOnClickListener(listener);
		btnCreate.setOnClickListener(listener);
		
		dialog.show();
	}
}
