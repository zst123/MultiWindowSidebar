package com.zst.app.multiwindowsidebar;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class ToggleActivity extends Activity {
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		sendBroadcast(new Intent(Common.PKG_THIS + ".TOGGLE"));
		super.onCreate(savedInstanceState);
		finish();
	}
}
