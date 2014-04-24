package com.zst.app.multiwindowsidebar;

public class Common {
	// Package Names
	public static final String PKG_XMULTIWINDOW = "com.lovewuchin.xposed.xmultiwindow";
	public static final String PKG_XHALOFLOATINGWINDOW = "com.zst.xposed.halo.floatingwindow";
	public static final String PKG_THIS = Common.class.getPackage().getName();
	
	// Intent Launching
	public static final int FLAG_FLOATING_WINDOW = 0x00002000;
	public static final int FLAG_XMULTIWINDOW_UPVIEW = 0x001;
	public static final int FLAG_XMULTIWINDOW_DOWNVIEW = 0x002;
	public static final String EXTRA_XHALO_SNAP_SIDE = PKG_XHALOFLOATINGWINDOW + ".EXTRA_SNAP_SIDE";
	public static final String EXTRA_REFRESH_SERVICE = "refresh_service";
	
	// Sidebar
	public static final int TIMEOUT_HIDE_SIDEBAR = 4000;
	public static final int COLOR_OUTLINE = 0xFFBBBBBB;
	public static final int COLOR_HOLO_BLUE = 0xFF33b5e5;

	// Preference filename
	public static final String KEY_PREFERENCE_MAIN = "main_pref";
	public static final String KEY_PREFERENCE_APPS = "apps_pref";
	
	// Preference keys & default values
	public static final String PREF_KEY_TOGGLE_SERVICE = "toggle_service";
	public static final String PREF_KEY_SELECT_APPS = "select_apps";
	public static final String PREF_KEY_START_ON_BOOT = "start_on_boot";
	public static final boolean PREF_DEF_START_ON_BOOT = false;
	public static final String PREF_KEY_SIDEBAR_POSITION = "sidebar_position";
	public static final String PREF_DEF_SIDEBAR_POSITION = "0";
	public static final String PREF_KEY_LAUNCH_MODE = "launch_mode";
	public static final String PREF_DEF_LAUNCH_MODE = "0";
	public static final String PREF_KEY_TAB_SIZE = "tab_size";
	public static final int PREF_DEF_TAB_SIZE = 32;
	public static final String PREF_KEY_LABEL_SIZE = "label_size";
	public static final int PREF_DEF_LABEL_SIZE = 14;
	public static final String PREF_KEY_ANIM_TIME = "animation_time";
	public static final int PREF_DEF_ANIM_TIME = 300;

	// App List
	public static final String SEPARATOR_GROUP = "&";

}
