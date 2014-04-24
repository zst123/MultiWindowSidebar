package com.zst.app.multiwindowsidebar.adapter;

import com.zst.app.multiwindowsidebar.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;

public abstract class AppChooserDialog extends Dialog {

	final AppChooserAdapter dAdapter;
	final ProgressBar dProgressBar;
	final ListView dListView;
	final EditText dSearch;
	final ImageButton dButton;
	
	private int mId;
	
	public AppChooserDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_app_chooser_list);
		
		dListView = (ListView) findViewById(R.id.listView1);
		dSearch = (EditText) findViewById(R.id.searchText);
		dButton = (ImageButton) findViewById(R.id.searchButton);
		dProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		
		dAdapter = new AppChooserAdapter(context) {
			@Override
			public void onStartUpdate() {
				dProgressBar.setVisibility(View.VISIBLE);
			}
			@Override
			public void onFinishUpdate() {
				dProgressBar.setVisibility(View.GONE);
			}
		};
		dListView.setAdapter(dAdapter);
		dListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
				AppChooserAdapter.AppItem info = (AppChooserAdapter.AppItem) av
						.getItemAtPosition(pos);
				onListViewItemClick(info, mId);
				dismiss();
			}
		});
		dButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dAdapter.getFilter().filter(dSearch.getText().toString());
			}
		});
		dAdapter.update();
	}
	
	public void show(int id) {
		mId = id;
		show();
	}
	
	public abstract void onListViewItemClick(AppChooserAdapter.AppItem info, int id);
}
