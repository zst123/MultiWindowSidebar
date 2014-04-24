package com.zst.app.multiwindowsidebar.preference;

import com.zst.app.multiwindowsidebar.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class SeekBarDialog extends DialogPreference implements SeekBar.OnSeekBarChangeListener {

	SeekBar mSeekBar;
	TextView mValue;

	String mKey = "";
	String mSuffix = "";
	int mMin;
	int mMax;
	int mDefault;
	
	public SeekBarDialog(Context context, AttributeSet attrs) {
		super(context, attrs);

		setDialogLayoutResource(R.layout.pref_seekbar);
		mDefault = Integer.parseInt(attrs.getAttributeValue(null, "defaultValue"));
		mMin = Integer.parseInt(attrs.getAttributeValue(null, "minimum"));
		mMax = Integer.parseInt(attrs.getAttributeValue(null, "maximum"));
		mSuffix = attrs.getAttributeValue(null, "suffix");
	}

	@Override
	protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
		builder.setNeutralButton(R.string.settings_default, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
	}

	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);

		mValue = (TextView) view.findViewById(R.id.value);
		mSeekBar = (SeekBar) view.findViewById(R.id.seekbar);
		mSeekBar.setOnSeekBarChangeListener(this);
	}

	@Override
	protected void showDialog(Bundle state) {
		super.showDialog(state);

		// can't use onPrepareDialogBuilder for this as we want the dialog
		// to be kept open on click
		AlertDialog d = (AlertDialog) getDialog();
		Button defaultsButton = d.getButton(DialogInterface.BUTTON_NEUTRAL);
		defaultsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mSeekBar.setProgress(mDefault - mMin);
			}
		});

		mKey = getKey();
		int value = getSharedPreferences().getInt(mKey, mDefault);
		value -= mMin;
		mSeekBar.setMax(mMax - mMin);
		mSeekBar.setProgress(value);

	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);

		if (positiveResult) {
			int realValue = mSeekBar.getProgress() + mMin;
			Editor editor = getEditor();
			editor.putInt(mKey, realValue);
			editor.commit();
			if (getOnPreferenceChangeListener() != null)
				getOnPreferenceChangeListener().onPreferenceChange(this, realValue);
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		int realValue = progress + mMin;
		mValue.setText(realValue + mSuffix);
		if (realValue == -1){
			mValue.setText(R.string.settings_default);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}
}
