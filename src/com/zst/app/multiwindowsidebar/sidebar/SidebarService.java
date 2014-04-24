package com.zst.app.multiwindowsidebar.sidebar;

import com.zst.app.multiwindowsidebar.Common;
import com.zst.app.multiwindowsidebar.IntentUtil;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class SidebarService extends Service {
	// Thanks: https://github.com/EatHeat/FloatingExample

	public static final String KEY_END = "key_end";
	public static boolean isRunning;

	public WindowManager mWindowManager;
	public Handler mHandler;
	
	// Preference Settings
	public boolean mBarOnRight = true;
	public int mAnimationTime;
	public int mLabelSize;

	public SidebarHolderView mShownSidebar;
	public SidebarHiddenView mHiddenSidebar;
	
	@Override 
	public void onCreate() {
		super.onCreate();
		isRunning = true;
		
		mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		mHandler = new Handler();
		
		// Non-refreshable settings
		SharedPreferences main_prefs = getSharedPreferences(Common.KEY_PREFERENCE_MAIN, Context.MODE_PRIVATE);
		mBarOnRight = Integer.parseInt(main_prefs.getString(Common.PREF_KEY_SIDEBAR_POSITION,
				Common.PREF_DEF_SIDEBAR_POSITION)) == 1;
		
		mHiddenSidebar = new SidebarHiddenView(this);
		mShownSidebar = new SidebarHolderView(this);
		
		refreshSettings();
		
		hideBar();
	}
	
	// All the refreshable settings go here
	private void refreshSettings() {
		SharedPreferences app_prefs = getSharedPreferences(Common.KEY_PREFERENCE_APPS, Context.MODE_PRIVATE);
		SharedPreferences main_prefs = getSharedPreferences(Common.KEY_PREFERENCE_MAIN, Context.MODE_PRIVATE);
		//app_prefs.edit().putInt("com.android.mms&com.android.settings" , 6).commit();
		
		final int tab_size = main_prefs.getInt(Common.PREF_KEY_TAB_SIZE,
				Common.PREF_DEF_TAB_SIZE);
		mShownSidebar.setTabSize(tab_size);
		mHiddenSidebar.setTabSize(tab_size);
		
		final int speed = main_prefs.getInt(Common.PREF_KEY_ANIM_TIME,
				Common.PREF_DEF_ANIM_TIME);
		mAnimationTime = speed;
		
		final int mode = Integer.parseInt(main_prefs.getString(Common.PREF_KEY_LAUNCH_MODE,
				Common.PREF_DEF_LAUNCH_MODE));
		IntentUtil.setLaunchMode(mode);

		final int label_size = main_prefs.getInt(Common.PREF_KEY_LABEL_SIZE,
				Common.PREF_DEF_LABEL_SIZE);
		mLabelSize = label_size;
		
		mShownSidebar.addApps(app_prefs, getPackageManager());
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		if (intent != null && intent.getBooleanExtra(Common.EXTRA_REFRESH_SERVICE, false)) {
			refreshSettings();
		}
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		isRunning = false;
		
		mHiddenSidebar.animateView(false);
		mShownSidebar.animateView(false);
		new Handler().postDelayed(new Runnable () {
			@Override
			public void run() {
				safelyRemoveView(mHiddenSidebar);
				safelyRemoveView(mShownSidebar);
			}
		}, 500);
		
	}
	
	public void safelyRemoveView(View v) {
		try {
			mWindowManager.removeView(v);
		} catch (Exception e) {
			// so we dont crash
		};
	}
	
	public void addView(View v) {
		final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				v instanceof SidebarHolderView ? WindowManager.LayoutParams.MATCH_PARENT
						: WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
				PixelFormat.TRANSLUCENT);
		if (mBarOnRight) {
			params.gravity = Gravity.TOP | Gravity.RIGHT;
		} else {
			params.gravity = Gravity.TOP | Gravity.LEFT;
		}
		// TODO params.alpha
		try{
			mWindowManager.addView(v, params);
		} catch (Exception e) {
			// so we dont crash
		};
	}
	
	public void showBar() {
		mHiddenSidebar.animateView(false);
		mShownSidebar.animateView(true);
	}
	
	public void hideBar() {
		mHiddenSidebar.animateView(true);
		mShownSidebar.animateView(false);
		try {
			mShownSidebar.setMarginFromTop(mMargin);
			mHiddenSidebar.setMarginFromTop(mMargin);
		} catch (Exception e) {
		}
	}
	
	private int mOldMargin = 20;
	private int mMargin = 20;
	private float mInitialPosition;
	public boolean tabTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mInitialPosition = event.getRawY();
			mOldMargin = mMargin;
			break;
		case MotionEvent.ACTION_MOVE:
			float final_position = event.getRawY();
			if (final_position > getResources().getDisplayMetrics().heightPixels - 20) {
				final_position = getResources().getDisplayMetrics().heightPixels - 20;
			}
			mMargin = (int) (mOldMargin + final_position - mInitialPosition);
			if (mMargin < 0) {
				mMargin = 0;
			} // make sure it doesn't go beyond top of screen
			try {
				mShownSidebar.setMarginFromTop(mMargin);
				mHiddenSidebar.setMarginFromTop(mMargin);
			} catch (Exception e) {
			}
			if (Math.abs(mOldMargin - mMargin) > 25) {
				return true;
			}
			break;
		}
		return false;
	}
}