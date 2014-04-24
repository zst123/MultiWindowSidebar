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
	
	public static final int MODE_UNKNOWN = -1;
	public static final int MODE_NONE = 0;
	public static final int MODE_PA_HALO = 1;
	public static final int MODE_XHALO_FLOATINGWINDOW = 2;
	public static final int MODE_XMULTI_WINDOW = 3;	
	
	public static int sLaunchMode = 3;	

	public static void setLaunchMode(int mode) {
		sLaunchMode = mode;
	}
	
	public static int getLaunchMode() {
		return sLaunchMode;
	}

	public static void launchIntent(Context context, Intent intent, int side) {
		switch (sLaunchMode) {
		case MODE_XHALO_FLOATINGWINDOW:
			if (side != SIDE_FULLSCREEN ||
				side != SIDE_PA_HALO ||
				side != SIDE_NONE) {
				intent.putExtra(Common.EXTRA_XHALO_SNAP_SIDE, side);
			}
			intent.addFlags(Common.FLAG_FLOATING_WINDOW);
			break;
		case MODE_PA_HALO:
			intent.addFlags(Common.FLAG_FLOATING_WINDOW);
			break;
		case MODE_XMULTI_WINDOW:
			if (side == SIDE_TOP || side == SIDE_LEFT) {
				intent.addFlags(Common.FLAG_XMULTIWINDOW_UPVIEW);
			} else if (side == SIDE_BOTTOM || side == SIDE_RIGHT) {
				intent.addFlags(Common.FLAG_XMULTIWINDOW_DOWNVIEW);
			}
			break;
		case MODE_UNKNOWN:
		case MODE_NONE:
		default:
			break;
		}
		context.startActivity(intent);
	}
}
