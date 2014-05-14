package com.zst.app.multiwindowsidebar;

import com.zst.app.multiwindowsidebar.sidebar.SidebarService;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

public class Util {
	public static void toast(Context ctx, int resource_id) {
		toast(ctx, ctx.getResources().getString(resource_id));
	}
	
	public static void toast(Context ctx, String text) {
		Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show();
	}
	
	public static void dialog(Context ctx, int resource_id) {
		dialog(ctx, ctx.getResources().getString(resource_id));
	}
	public static void dialog(Context ctx, String text) {
		new AlertDialog.Builder(ctx)
			.setMessage(text)
			.setPositiveButton(android.R.string.yes, null)
			.show();
	}
		
	public static boolean isAppInstalled(PackageManager pm, String pkg) {
		try {
			pm.getPackageInfo(pkg, PackageManager.GET_ACTIVITIES);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}
	
	public static void refreshService(final Context ctx) {
		if (SidebarService.isRunning || SidebarService.isSidebarShown) {
			final Intent service = new Intent(ctx, SidebarService.class);
			service.putExtra(Common.EXTRA_REFRESH_SERVICE, true);
			ctx.startService(service);
		}
	}
	
	public static void resetService(final Context ctx) {
		if (!SidebarService.isRunning) return;
		final Intent service = SidebarService.stopSidebar(ctx);
		
		new Handler(ctx.getMainLooper()).postDelayed(new Runnable() {
			@Override
			public void run() {
				ctx.startService(service);
			}
		}, 1500);
	}
	
	public static int dp(int dp, Context c) {
		float scale = c.getResources().getDisplayMetrics().density;
		int pixel = (int) (dp * scale + 0.5f);
		return pixel;
	}
	
	/* Create a Border */
	public static ShapeDrawable makeOutline(int color, int thickness) {
		ShapeDrawable rectShapeDrawable = new ShapeDrawable(new RectShape());
		Paint paint = rectShapeDrawable.getPaint();
		paint.setColor(color);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(thickness);
		return rectShapeDrawable;
	}
	
	/* Set background drawable based on the API */
	@SuppressWarnings("deprecation")
	public static void setBackgroundDrawable(View view, Drawable drawable) {
		if (Build.VERSION.SDK_INT >= 16) {
			view.setBackground(drawable);
		} else {
			view.setBackgroundDrawable(drawable);
		}
	}
	
	public static Drawable layerTwoIcons(Context c, Drawable icon0, Drawable icon1) {
		LayerDrawable layer = new LayerDrawable(new Drawable[] { icon1, icon0 });
		int size = dp(16, c);
		layer.setLayerInset(0x0, 0, 0, size, size);
		layer.setLayerInset(0x1, size, size, 0, 0);
		return layer;
	}
	
	public static int parseColorFromString(String str, String defColorWithoutSymbols) {
		str.replaceAll("\\s+", "");
		// Remove all spaces
		if (str.equals("")) {
			str = defColorWithoutSymbols;
		}
		if (!str.startsWith("#")) {
			str = "#" + str;
		}
		try {
			return Color.parseColor(str);
		} catch (Exception e) {
			return Color.parseColor("#" + defColorWithoutSymbols);
		}
	}
}
