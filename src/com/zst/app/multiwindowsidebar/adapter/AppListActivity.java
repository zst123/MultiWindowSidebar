package com.zst.app.multiwindowsidebar.adapter;

import java.util.Map;

import com.zst.app.multiwindowsidebar.Common;
import com.zst.app.multiwindowsidebar.R;
import com.zst.app.multiwindowsidebar.Util;
import com.zst.app.multiwindowsidebar.adapter.AppChooserAdapter.AppItem;
import com.zst.app.multiwindowsidebar.sidebar.SidebarMenuOptions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class AppListActivity extends Activity {
	
	/* others */
	static final int ID_ADD_APP = 1;
	static final int ID_CREATE_GROUP = 2;
	
	/* main stuff */
	SharedPreferences mPref;
	AppListAdapter mPkgAdapter;
	ListView mListView;
	
	/* app dialog stuff */
	AppChooserDialog dDialog;
	
	@Override
	@SuppressLint("WorldReadableFiles")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPref = getSharedPreferences(Common.KEY_PREFERENCE_APPS, MODE_PRIVATE);
		loadList();
		initAppList();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		// Avoid WindowLeaked Exception
		// http://publicstaticdroidmain.com/2012/01/avoiding-android-memory-leaks-part-1/
		if (dDialog != null && dDialog.isShowing()) {
			dDialog.dismiss();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem add = menu.add(Menu.NONE, ID_ADD_APP, 0, R.string.add_app);
		add.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		MenuItem create = menu.add(Menu.NONE, ID_CREATE_GROUP, 0, R.string.create_group);
		create.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ID_ADD_APP:
			dDialog.show(ID_ADD_APP);
			break;
		case ID_CREATE_GROUP:
			SidebarMenuOptions.showGroupCreatorDialog(this, this, false);
			break;
		}
		return false;
	}
	
	private void initAppList() {
		dDialog = new AppChooserDialog(this) {
			@Override
			public void onListViewItemClick(AppItem info, int id) {
				addApp(info.packageName);
			}
		};
	}
	
	private void loadList() {
		final Map<String, Integer> list = getSetStrings();
		mPkgAdapter = new AppListAdapter(this, list) {
			@Override
			public void onSwappedListPositions() {
				resaveAppPositions();
			}
			@Override
			public void onRemoveButtonPress(PackageItem app_info) {
				removeApp(app_info.packageName);
			}
		};
		mListView = new ListView(this);
		mListView.setAdapter(mPkgAdapter);
		setContentView(mListView);
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Integer> getSetStrings() {
		return (Map<String, Integer>) mPref.getAll();
	}
	
	public void removeApp(String pkg) {
		Editor editor = mPref.edit().clear();
		int x = 0;
		for (AppListAdapter.PackageItem item : mPkgAdapter.getList()) {
			if (!item.packageName.equals(pkg)) {
				editor.putInt(item.packageName, x);
				x++;
			}
		}
		editor.commit();
		updateList();
	}
	
	public void addApp(String pkg) {
		Editor editor = mPref.edit().clear();
		int x = 0;
		for (AppListAdapter.PackageItem item : mPkgAdapter.getList()) {
			editor.putInt(item.packageName, x);
			x++;
		}
		editor.putInt(pkg, x);
		editor.commit();
		updateList();		
	}

	public void resaveAppPositions() {
		Editor editor = mPref.edit().clear();
		int x = 0;
		for (AppListAdapter.PackageItem item : mPkgAdapter.getList()) {
			editor.putInt(item.packageName, x);
			x++;
		}
		editor.commit();
		updateList();		
	}
	
	public void updateList() {
		mPkgAdapter.update(getSetStrings());
		Util.refreshService(this);
	}
}
