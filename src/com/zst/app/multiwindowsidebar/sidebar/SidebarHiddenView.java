package com.zst.app.multiwindowsidebar.sidebar;

import com.zst.app.multiwindowsidebar.R;
import com.zst.app.multiwindowsidebar.Util;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

@SuppressLint("ViewConstructor")
public class SidebarHiddenView extends LinearLayout {
	final SidebarService mService;
	final ImageView mTab;
	
	float mPreviousXTouch;
	float mPreviousYTouch;
	
	public SidebarHiddenView(SidebarService service) {
		super(service);
		mService = service;
		LayoutInflater.from(service).inflate(R.layout.sidebar_hidden, this);
		mTab = (ImageView) findViewById(android.R.id.button1);
	}
	
	public void refreshBarSide() {
		if (mService.mBarOnRight) {
			mTab.setImageResource(ThemeSetting.getDrawableResId(ThemeSetting.TAB_RIGHT_HIDDEN));
		} else {
			mTab.setImageResource(ThemeSetting.getDrawableResId(ThemeSetting.TAB_LEFT_HIDDEN));
		}
	}
	
	public void setMarginFromTop(int top) {
		WindowManager.LayoutParams params = (WindowManager.LayoutParams) this.getLayoutParams();
		params.y = top;
		mService.mWindowManager.updateViewLayout(this, params);
	}
	
	public void setTabSize(int dp) {
		LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) mTab.getLayoutParams();
		param.width = Util.dp(dp, getContext());;
		mTab.setLayoutParams(param);
	}
	
	public void setTabAlpha(float decimal) {
		((View) mTab).setAlpha(decimal);
	}
	
	public void animateView(boolean visible) {
		if (visible) {
			mService.addView(this);
			TranslateAnimation anim = new TranslateAnimation(
					Animation.RELATIVE_TO_PARENT,
					mService.mBarOnRight ? 1.0f : -1.0f,
					Animation.RELATIVE_TO_PARENT,
					0.0f,
					0, 0, 0, 0);
			anim.setDuration(mService.mAnimationTime);
			mTab.startAnimation(anim);
		} else {
			TranslateAnimation anim = new TranslateAnimation(
					Animation.RELATIVE_TO_PARENT,
					0.0f,
					Animation.RELATIVE_TO_PARENT,
					mService.mBarOnRight ? 1.0f : -1.0f,
					0, 0, 0, 0);
			anim.setDuration(mService.mAnimationTime);
			anim.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {}
				@Override
				public void onAnimationRepeat(Animation animation) {}
				@Override
				public void onAnimationEnd(Animation animation) {
					mService.safelyRemoveView(SidebarHiddenView.this);
				}
			});
			mTab.startAnimation(anim);
		}
	}
	
	final Runnable runnable = new Runnable () {
		@Override
		public void run() {
			mService.showBar();
		}
	};
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			mService.mHandler.postDelayed(runnable, 300);
			mPreviousXTouch = event.getRawX();
			mPreviousYTouch = event.getRawY();
			mService.tabTouchEvent(event, false);
			mTab.setImageState(new int[] { android.R.attr.state_pressed }, false);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			mPreviousYTouch = event.getRawY();
			mTab.setImageState(new int[] { android.R.attr.state_empty }, false);
			break;
		}
		if (mPreviousYTouch == -1 || moveRangeAboveLimit(event)) {
			if (mService.tabTouchEvent(event, false)) {
				mService.mHandler.removeCallbacks(runnable);
			}
		}
		;
		return true;
	}
	
	private boolean moveRangeAboveLimit(MotionEvent event) {
		if (Math.abs(mPreviousXTouch - event.getRawX()) > Util.dp(64, getContext())) {
			// user is swiping out, don't move tab
			return false;
		}
		
		final int range = Util.dp(16, getContext());
		boolean returnVal = false;
		
		if (Math.abs(mPreviousYTouch - event.getRawY()) > range) {
			returnVal = true;
		}
		
		if (returnVal) {
			mPreviousYTouch = -1;
		}
		return returnVal;
	}
}
