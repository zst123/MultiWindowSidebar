package com.zst.app.multiwindowsidebar.sidebar;

import com.zst.app.multiwindowsidebar.Common;
import com.zst.app.multiwindowsidebar.IntentUtil;
import com.zst.app.multiwindowsidebar.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

@SuppressLint("ViewConstructor")
public class SidebarDraggedOutView extends ImageView {

	private static SidebarDraggedOutView mInstance;
	
	private final WindowManager mWindowManager;
	private final View mOutlineView;

	private SidebarItemView mItemView;
	private int mIconPosition;
	
	private SidebarDraggedOutView(SidebarItemView item) {
		super(item.mService);
		mItemView = item;
		mWindowManager = item.mService.mWindowManager;
		mOutlineView = createOutlineView(getContext(), Common.COLOR_OUTLINE);
	}
	
	public static SidebarDraggedOutView getInstance(SidebarItemView item) {
		if (mInstance == null) {
			if (item == null) return null;
			mInstance = new SidebarDraggedOutView(item);
		} else {
			if (item != null)
				mInstance.mItemView = item;
		}
		return mInstance;
	}

	public boolean setPosition(float x, float y, boolean show_outline) {
		WindowManager.LayoutParams param = (WindowManager.LayoutParams) getLayoutParams();
		param.x = Math.round(x - (getWidth() / 2));
		param.y = Math.round(y - getHeight());
		param.gravity = Gravity.TOP | Gravity.LEFT;
		try {
			mWindowManager.updateViewLayout(this, param);
			if (show_outline) {
				calculateOutlinePosition(x, y);
			} else {
				removeOutlines();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean showView(Drawable drawable) {
		if (drawable != null) {
			setImageDrawable(drawable);
		} else {
			setImageResource(android.R.drawable.sym_def_app_icon);
		}
		try {
			mWindowManager.addView(this, getParam());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// view already added
		}
	}
	
	public boolean hideView() {
		removeOutlines();
		try {
			mWindowManager.removeView(this);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// view already removed
		}
	}
	
	private void showOutlines(int[] outline_param) {
		WindowManager.LayoutParams paramz = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		paramz.gravity = outline_param[2];
		paramz.width = outline_param[0];
		paramz.height = outline_param[1];
		// TODO add in reflection param.privateFlags |= 0x00000040; //PRIVATE_FLAG_NO_MOVE_ANIMATION
		try {
			mWindowManager.updateViewLayout(mOutlineView, paramz);
		} catch (Exception e) {
			try {
				mWindowManager.addView(mOutlineView, paramz);
			} catch (Exception e2) {
			}
		}
	}
	
	private void removeOutlines() {
		try {
			mWindowManager.removeView(mOutlineView);
		} catch (Exception e) {
			// view already removed
		}
	}

	
	// create outline view with translucent filling
	private static View createOutlineView(Context ctx, int color) {
		FrameLayout outline = new FrameLayout(ctx);
		Util.setBackgroundDrawable(outline, Util.makeOutline(color, Util.dp(4, ctx)));
		
		View filling = new View(ctx);
		filling.setBackgroundColor(color);
		filling.setAlpha(0.5f);
		outline.addView(filling);
		
		outline.setFocusable(false);
		outline.setClickable(false);
		return outline;
	}
	public void calculateOutlinePosition(float x, float y) {
		final DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
		final int screen_width = dm.widthPixels;
		final int screen_height = dm.heightPixels;
		
		if (mItemView instanceof SidebarDualItemView) {
			// it is a group, the positions are already set
			mIconPosition = IntentUtil.SIDE_FULLSCREEN;
		} else {
			mIconPosition = WindowPositionOutline.getPositionOfTouch(x, y,
					screen_width, screen_height);
		}
		
		int[] outline_param = WindowPositionOutline.getOutlineParams(screen_width, screen_height,
				mIconPosition);
		if (outline_param[0] == -1000) {
			removeOutlines();
		} else {
			showOutlines(outline_param);
		}
	}
		
	public void launch() {
		mItemView.launchApp(mIconPosition);
	}
	
	public final WindowManager.LayoutParams getParam() {
		return new WindowManager.LayoutParams(
				Util.dp(48, getContext()),
				Util.dp(48, getContext()),
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
				PixelFormat.TRANSLUCENT);
	}
}
