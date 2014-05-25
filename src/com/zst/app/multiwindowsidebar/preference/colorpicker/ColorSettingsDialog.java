/*
 * Copyright (C) 2010 Daniel Nilsson
 * Copyright (C) 2012 The CyanogenMod Project
 * Copyright (C) 2013 XuiMod
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zst.app.multiwindowsidebar.preference.colorpicker;

import java.util.Locale;

import com.zst.app.multiwindowsidebar.R;
import com.zst.app.multiwindowsidebar.Util;
import com.zst.app.multiwindowsidebar.preference.colorpicker.ColorPickerView.OnColorChangedListener;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ColorSettingsDialog extends AlertDialog implements
ColorPickerView.OnColorChangedListener {
	
	private ColorPickerView mColorPicker;
	
	private ColorPanelView mOldColor;
	private ColorPanelView mNewColor;
	private EditText mHexColor;
	private Button mHexButton;
	
	private LayoutInflater mInflater;
	private OnColorChangedListener mListener;
	
	public ColorSettingsDialog(Context context, int initialColor, final String defColor) {
		super(context);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		// To fight color banding.
		setUp(initialColor, defColor);
	}
	
	private void setUp(int color, final String defColor) {
		mInflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View layout = mInflater.inflate(R.layout.view_dialog_colorpicker, null);
		
		mColorPicker = (ColorPickerView) layout.findViewById(R.id.color_picker_view);
		mOldColor = (ColorPanelView) layout.findViewById(R.id.old_color_panel);
		mNewColor = (ColorPanelView) layout.findViewById(R.id.new_color_panel);
		mHexColor = (EditText) layout.findViewById(R.id.current_hex_text);
		
		mColorPicker.setOnColorChangedListener(this);
		mOldColor.setColor(color);
		mColorPicker.setColor(color, true);
		
		mHexButton = (Button) layout.findViewById(R.id.color_apply);
		mHexButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				int color = Util.parseColorFromString(mHexColor.getText().toString(), defColor);
				mColorPicker.setColor(color);
				colorChange(color);
			}
		});
		
		setView(layout);
	}
	
	@Override
	public void onColorChanged(int color) {
		colorChange(color);
	}
	
	private void colorChange(int color){
		mNewColor.setColor(color);
		mHexColor.setText((String.format("%08X", (0xFFFFFFFF & color))).toUpperCase(Locale.ENGLISH));
		if (mListener != null) {
			mListener.onColorChanged(color);
		}
	}
	
	public void setAlphaSliderVisible(boolean visible) {
		mColorPicker.setAlphaSliderVisible(visible);
	}
	
	public String getColorString() {
		return String.format("%08X", (0xFFFFFFFF & mColorPicker.getColor()));
	}
	
	public int getColor() {
		return mColorPicker.getColor();
	}
	
}