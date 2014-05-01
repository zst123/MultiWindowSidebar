package com.zst.app.multiwindowsidebar.sidebar;

import com.zst.app.multiwindowsidebar.R;

public class ThemeSetting {
	
	/*
	 * Drawables
	 * The first integer in the array is their main id
	 * Consequent integers are the resource id arranged in the order of the
	 * themes
	 */
	public static final int[] BACKGROUND_LEFT = { 0xA1,
		R.drawable.bg_left,
		R.drawable.inverted_bg_left };
	
	public static final int[] BACKGROUND_RIGHT = { 0xA2,
		R.drawable.bg_right,
		R.drawable.inverted_bg_right };
	
	public static final int[] TAB_LEFT_HIDDEN = { 0xB1,
		R.drawable.tab_left_hidden_selector,
		R.drawable.inverted_tab_left_hidden };
	
	public static final int[] TAB_LEFT_SHOWN = { 0xB2,
		R.drawable.tab_left_show_selector,
		R.drawable.inverted_tab_left_show };
	
	public static final int[] TAB_RIGHT_HIDDEN = { 0xB3,
		R.drawable.tab_right_hidden_selector,
		R.drawable.inverted_tab_right_hidden };
	
	public static final int[] TAB_RIGHT_SHOWN = { 0xB4,
		R.drawable.tab_right_show_selector, 
		R.drawable.inverted_tab_right_show };
	
	public static final int[] IC_EMPTY_ICON = { 0xC1,
		R.drawable.ic_empty_icon,
		R.drawable.ic_empty_icon };
	
	public static final int[] IC_MENU_CREATE_GROUP = { 0xC2,
		R.drawable.ic_menu_create_group,
		R.drawable.inverted_ic_menu_create_group };
	
	public static final int[] IC_MENU_EDIT = { 0xC3,
		R.drawable.ic_menu_edit,
		R.drawable.inverted_ic_menu_edit };
	
	public static final int[] IC_MORE_BUTTON = { 0xC4,
		R.drawable.ic_more,
		R.drawable.inverted_ic_more };
	
	/* Theme Number */
	public static final int THEME_UNKNOWN = 0;
	public static final int THEME_ORIGINAL = 1;
	public static final int THEME_INVERTED = 2;
	
	/* Static */
	private static int sTheme = THEME_UNKNOWN;
	
	public static void setTheme(int theme) {
		sTheme = theme;
	}
	
	public static int getDrawableResId(int[] drawable_number) {
		if (sTheme == THEME_UNKNOWN) {
			sTheme = THEME_ORIGINAL;
		}
		
		return drawable_number[sTheme];
	}
}
