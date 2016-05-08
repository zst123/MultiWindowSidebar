package com.zst.app.multiwindowsidebar;

import android.content.Context;
import android.content.Intent;

public class IntentUtil {
	
	public final static int SIDE_NONE = 0;
	public final static int SIDE_LEFT = 1;
	public final static int SIDE_TOP = 2;
	public final static int SIDE_RIGHT = 3;
	public final static int SIDE_BOTTOM = 4;
	public final static int SIDE_FULLSCREEN = 5;
	public final static int SIDE_PA_HALO = 6;
	
	public static final class DragMode {
		public static final int NONE = 00;
		public static final int PA_HALO = 10;
		public static final int XHFW_PORTRAIT = 20;
		public static final int XHFW_LANDSCAPE = 21;

		public static final int XHFW3_PORTRAIT = 120;
		public static final int XHFW3_LANDSCAPE = 121;

		public static final int XMULTI_WINDOW = 30;
	}
	
	public static final class TapMode {
		public static final int NONE = 00;
		public static final int PA_HALO = 10;
		public static final int XHFW_CENTER = 20;
		public static final int XHFW_TOP = 21;
		public static final int XHFW_BOTTOM = 22;
		public static final int XHFW_LEFT = 23;
		public static final int XHFW_RIGHT = 24;

		public static final int XHFW3_CENTER = 120;
		public static final int XHFW3_TOP = 121;
		public static final int XHFW3_BOTTOM = 122;
		public static final int XHFW3_LEFT = 123;
		public static final int XHFW3_RIGHT = 124;

		public static final int XMULTI_WINDOW_TOP = 30;
		public static final int XMULTI_WINDOW_BOTTOM = 31;
		//TODO Add menu
	}
	
	public static int sLaunchModeDrag = DragMode.NONE;
	public static int sLaunchModeTap = TapMode.NONE;

	public static void launchIntentDrag(Context context, Intent intent, int side) {
		switch (sLaunchModeDrag) {
		case DragMode.XHFW_PORTRAIT:
			if (side != SIDE_FULLSCREEN ||
				side != SIDE_PA_HALO ||
				side != SIDE_NONE) {
				intent.putExtra(Common.EXTRA_XHALO_SNAP_SIDE, side);
			}
			intent.addFlags(Common.FLAG_FLOATING_WINDOW);
			break;
		case DragMode.XHFW_LANDSCAPE:
			intent.addFlags(Common.FLAG_FLOATING_WINDOW);
			if (side != SIDE_FULLSCREEN ||
				side != SIDE_PA_HALO ||
				side != SIDE_NONE) {
				intent.putExtra(Common.EXTRA_XHALO_SNAP_SIDE, side);
			}
			break;
		case DragMode.XHFW3_PORTRAIT:
			if (side != SIDE_FULLSCREEN ||
				side != SIDE_PA_HALO ||
				side != SIDE_NONE) {
				intent.putExtra(Common.EXTRA_XHALO_SNAP_SIDE, side);
			}
			intent.addFlags(Common.FLAG_FLOATING_WINDOW_XHFW3);
			break;
		case DragMode.XHFW3_LANDSCAPE:
			intent.addFlags(Common.FLAG_FLOATING_WINDOW_XHFW3);
			if (side != SIDE_FULLSCREEN ||
				side != SIDE_PA_HALO ||
				side != SIDE_NONE) {
				intent.putExtra(Common.EXTRA_XHALO_SNAP_SIDE, side);
			}
			break;
		case DragMode.PA_HALO:
			intent.addFlags(Common.FLAG_FLOATING_WINDOW);
			break;
		case DragMode.XMULTI_WINDOW:
			if (side == SIDE_TOP || side == SIDE_LEFT) {
				intent.addFlags(Common.FLAG_XMULTIWINDOW_UPVIEW);
			} else if (side == SIDE_BOTTOM || side == SIDE_RIGHT) {
				intent.addFlags(Common.FLAG_XMULTIWINDOW_DOWNVIEW);
			}
			break;
		case DragMode.NONE:
		default:
			break;
		}
		context.startActivity(intent);
	}
	
	public static void launchIntentTap(Context context, Intent intent) {
		switch (sLaunchModeTap) {
		case TapMode.XMULTI_WINDOW_TOP:
			intent.addFlags(Common.FLAG_XMULTIWINDOW_UPVIEW);
			break;
		case TapMode.XMULTI_WINDOW_BOTTOM:
			intent.addFlags(Common.FLAG_XMULTIWINDOW_DOWNVIEW);
			break;
		case TapMode.XHFW_TOP:
			intent.putExtra(Common.EXTRA_XHALO_SNAP_SIDE, SIDE_TOP);
			intent.addFlags(Common.FLAG_FLOATING_WINDOW);
			break;
		case TapMode.XHFW_BOTTOM:
			intent.putExtra(Common.EXTRA_XHALO_SNAP_SIDE, SIDE_BOTTOM);
			intent.addFlags(Common.FLAG_FLOATING_WINDOW);
			break;
		case TapMode.XHFW_LEFT:
			intent.putExtra(Common.EXTRA_XHALO_SNAP_SIDE, SIDE_LEFT);
			intent.addFlags(Common.FLAG_FLOATING_WINDOW);
			break;
		case TapMode.XHFW_RIGHT:
			intent.putExtra(Common.EXTRA_XHALO_SNAP_SIDE, SIDE_RIGHT);
			intent.addFlags(Common.FLAG_FLOATING_WINDOW);
			break;
		case TapMode.XHFW_CENTER:
			intent.putExtra(Common.EXTRA_XHALO_SNAP_SIDE, SIDE_NONE);
			intent.addFlags(Common.FLAG_FLOATING_WINDOW);
			break;
		case TapMode.XHFW3_TOP:
			intent.putExtra(Common.EXTRA_XHALO_SNAP_SIDE, SIDE_TOP);
			intent.addFlags(Common.FLAG_FLOATING_WINDOW_XHFW3);
			break;
		case TapMode.XHFW3_BOTTOM:
			intent.putExtra(Common.EXTRA_XHALO_SNAP_SIDE, SIDE_BOTTOM);
			intent.addFlags(Common.FLAG_FLOATING_WINDOW_XHFW3);
			break;
		case TapMode.XHFW3_LEFT:
			intent.putExtra(Common.EXTRA_XHALO_SNAP_SIDE, SIDE_LEFT);
			intent.addFlags(Common.FLAG_FLOATING_WINDOW_XHFW3);
			break;
		case TapMode.XHFW3_RIGHT:
			intent.putExtra(Common.EXTRA_XHALO_SNAP_SIDE, SIDE_RIGHT);
			intent.addFlags(Common.FLAG_FLOATING_WINDOW_XHFW3);
			break;
		case TapMode.XHFW3_CENTER:
			intent.putExtra(Common.EXTRA_XHALO_SNAP_SIDE, SIDE_NONE);
			intent.addFlags(Common.FLAG_FLOATING_WINDOW_XHFW3);
			break;
		case TapMode.PA_HALO:
			intent.addFlags(Common.FLAG_FLOATING_WINDOW);
			break;
		case TapMode.NONE:
		default:
			break;
		}
		context.startActivity(intent);
	}
}
