package com.zst.app.multiwindowsidebar.adapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;

import com.zst.app.multiwindowsidebar.Common;
import com.zst.app.multiwindowsidebar.R;
import com.zst.app.multiwindowsidebar.Util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class AppListAdapter extends BaseAdapter {
	
	final AppListActivity mActivity;
	final Handler mHandler;
	final PackageManager mPackageManager;
	final LayoutInflater mLayoutInflater;
	
	protected LinkedList<PackageItem> mApps = new LinkedList<PackageItem>();
		
	public AppListAdapter(AppListActivity act, Map<String, Integer> list) {
		mActivity = act;
		mHandler = new Handler();
		mPackageManager = act.getBaseContext().getPackageManager();
		mLayoutInflater = (LayoutInflater) act.getBaseContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		update(list);
	}
	
	public void update(final Map<String, Integer> app_array) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (mApps) {
					final PackageItem[] array = new PackageItem[app_array.size()*2];
					for (String pkg_name : app_array.keySet()) {
						if (pkg_name.contains(Common.SEPARATOR_GROUP)) {
							try {
								String[] str = pkg_name.split(Common.SEPARATOR_GROUP);
								ApplicationInfo ai0 = mPackageManager.getApplicationInfo(str[0], 0);
								ApplicationInfo ai1 = mPackageManager.getApplicationInfo(str[1], 0);
								final PackageItem item = new PackageItem();
								item.title =  mActivity.getResources().getString(R.string.group)
										+ " ("+ai0.loadLabel(mPackageManager) + " & "
										+ ai1.loadLabel(mPackageManager) + ")";
								
								final Drawable icon0 = ai0.loadIcon(mPackageManager).mutate();
								final Drawable icon1 = ai1.loadIcon(mPackageManager).mutate();
								
								item.icon = Util.layerTwoIcons(mActivity, icon0, icon1);
								item.packageName = pkg_name;
								array[app_array.get(pkg_name)] = item;
							} catch (Exception e) {
							}
						} else {
							try {
								ApplicationInfo ai = mPackageManager.getApplicationInfo(pkg_name, 0);
								final PackageItem item = new PackageItem();
								item.title = ai.loadLabel(mPackageManager);
								item.icon = ai.loadIcon(mPackageManager);
								item.packageName = ai.packageName;
								array[app_array.get(pkg_name)] = item;
							} catch (Exception e) {
							}
						}
					}
					
					final LinkedList<PackageItem> temp = new LinkedList<PackageItem>();
					for (int x = 0; x < array.length; x++) {
						if (array[x] != null) {
							temp.add(array[x]);
						}
					}
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							mApps.clear();
							mApps = temp;
							notifyDataSetChanged();
						}
					});
				}
			}
		}).start();
	}
	
	@Override
	public int getCount() {
		return mApps.size();
	}
	
	public LinkedList<PackageItem> getList() {
		return mApps;
	}
	
	@Override
	public PackageItem getItem(int position) {
		return mApps.get(position);
	}
	
	@Override
    public long getItemId(int position) {
        if (position < 0 || position >= mApps.size()) {
            return -1;
        }
        return mApps.get(position).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = mLayoutInflater.inflate(R.layout.view_package_list, parent, false);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(android.R.id.title);
			holder.icon = (ImageView) convertView.findViewById(android.R.id.icon);
			holder.pkg = (TextView) convertView.findViewById(android.R.id.message);
			holder.remove = (ImageButton) convertView.findViewById(R.id.removeButton);
			holder.up = (ImageButton) convertView.findViewById(R.id.upButton);
			holder.down = (ImageButton) convertView.findViewById(R.id.downButton);
			convertView.setTag(holder);
		}
		final PackageItem appInfo = getItem(position);
		
		holder.name.setText(appInfo.title);
		holder.pkg.setText(appInfo.packageName);
		holder.icon.setImageDrawable(appInfo.icon);
		holder.remove.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				onRemoveButtonPress(appInfo);
			}
		});
		holder.up.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Collections.swap(mApps, position, position - 1);
				notifyDataSetChanged();
				onSwappedListPositions();
			}
		});
		holder.down.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Collections.swap(mApps, position, position + 1);
				notifyDataSetChanged();
				onSwappedListPositions();
			}
		});
		return convertView;
	}
	
	public abstract void onRemoveButtonPress(PackageItem app_info);
	public abstract void onSwappedListPositions();
	
	public class PackageItem implements Comparable<PackageItem> {
		public CharSequence title;
		public String packageName;
		public Drawable icon;
		
		@Override
		public int compareTo(PackageItem another) {
			return this.title.toString().compareTo(another.title.toString());
		}
	}
	
	static class ViewHolder {
		TextView name;
		ImageView icon;
		TextView pkg;
		ImageButton remove;
		ImageButton up;
		ImageButton down;
	}
}
