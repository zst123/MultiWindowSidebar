package com.zst.app.multiwindowsidebar.preference;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.SeekBar;

public class TextSizeDialog extends SeekBarDialog {
	
	public TextSizeDialog(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void showDialog(Bundle state) {
		super.showDialog(state);
		int value = getSharedPreferences().getInt(mKey, mDefault);
		mValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, value);
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		super.onProgressChanged(seekBar, progress, fromUser);
		int realValue = progress + mMin;
		mValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, realValue);
	}
}
