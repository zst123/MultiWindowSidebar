package com.zst.app.multiwindowsidebar.sidebar;

import com.zst.app.multiwindowsidebar.IntentUtil;
import com.zst.app.multiwindowsidebar.R;
import com.zst.app.multiwindowsidebar.Util;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("ViewConstructor")
public abstract class SidebarItemView extends LinearLayout {
	
	final static DecelerateInterpolator sDecelerator = new DecelerateInterpolator();
	final static OvershootInterpolator sOvershooter = new OvershootInterpolator(10f);
    
	final SidebarService mService;
	final TextView mLabel;
	final ImageView mIcon;
	
	private boolean isLongPressVerified;
	private Drawable mDrawableIcon;
	private String mPkg;

	private final Runnable mLongPressVerifiedRunnable = new Runnable () {
		@Override
		public void run() {
			isLongPressVerified = true;
		}
	};
	
	public SidebarItemView(final SidebarService service, LayoutInflater inflator) {
		super(service);
		mService = service;
		
		inflator.inflate(R.layout.sidebar_item, this);
		mLabel = (TextView) findViewById(android.R.id.text1);
		mIcon = (ImageView) findViewById(android.R.id.icon);
		
		mLabel.setFocusable(false);
		mIcon.setFocusable(false);
		
		if (service.mLabelSize == 0) {
			mLabel.setVisibility(View.GONE);
		} else {
			mLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, service.mLabelSize);
		}
		
		mLabel.setTextColor(service.mLabelColor);
		
		setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					isLongPressVerified = false;
					mService.mHandler.postDelayed(mLongPressVerifiedRunnable, 250);
					//TODO custom drag out delay
					animate().setInterpolator(sDecelerator).scaleY(.9f).scaleX(.9f)
							.setDuration(100);
					break;
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:
					mService.mHandler.removeCallbacks(mLongPressVerifiedRunnable);
					animate().setInterpolator(sOvershooter).scaleX(1f).scaleY(1f).setDuration(100);
					break;
				}
				touchEventHelper(event, isLongPressVerified);
				return false;
			}
		});
        setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!isLongPressVerified)
					// if it's a long press & not a click, we shouldn't launch
					tappedApp();
			}
		});
	}
	
	public abstract void touchEventHelper(MotionEvent event, boolean long_press_verified);
	
	public void launchApp(int side) {
		if (TextUtils.isEmpty(mPkg)) {
			Util.toast(getContext(), getResources().getString(R.string.app_launch_error) + mPkg);
		} else {
			try {
				final Intent intent1 = new Intent(getContext().getPackageManager()
						.getLaunchIntentForPackage(mPkg));
				IntentUtil.launchIntentDrag(getContext(), intent1, side);
			} catch (NullPointerException e) {
				Util.toast(getContext(), getResources().getString(R.string.app_launch_error) + mPkg);
			}
		}	
	}
	
	public void tappedApp() {
		if (TextUtils.isEmpty(mPkg)) {
			Util.toast(getContext(), getResources().getString(R.string.app_launch_error) + mPkg);
		} else {
			try {
				final Intent intent1 = new Intent(getContext().getPackageManager()
						.getLaunchIntentForPackage(mPkg));
				IntentUtil.launchIntentTap(getContext(), intent1);
			} catch (NullPointerException e) {
				Util.toast(getContext(), getResources().getString(R.string.app_launch_error) + mPkg);
			}
		}	
	}
	
	public void setIcon(Drawable drawable) {
		if (drawable != null) {
			mIcon.setImageDrawable(drawable);
		} else {
			mIcon.setImageResource(android.R.drawable.sym_def_app_icon);
		}
		mDrawableIcon = mIcon.getDrawable();
	}
	
	public Drawable getIcon() {
		return mIcon.getDrawable();	
	}
	
	public void setToEmptyIcon(boolean yes) {
		if (mDrawableIcon == null) {
			mDrawableIcon = mIcon.getDrawable();
		}
		
		if (!yes) {
			mIcon.setImageDrawable(mDrawableIcon);
		} else {
			mIcon.setImageResource(ThemeSetting.getDrawableResId(ThemeSetting.IC_EMPTY_ICON));
		}
	}
	
	public void setLabel(CharSequence str) {
		if (TextUtils.isEmpty(str)) {
			mLabel.setText(getResources().getString(android.R.string.unknownName));
		} else {
			mLabel.setText(str);
		}
	}
	
	public void setPkg(String pkg) {
		mPkg = pkg;
	}
}