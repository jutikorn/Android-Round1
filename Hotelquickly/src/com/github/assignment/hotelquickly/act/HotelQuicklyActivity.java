package com.github.assignment.hotelquickly.act;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;

public abstract class HotelQuicklyActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getContentCache(savedInstanceState);
		setContentView(getLayoutResourceId());
		initWidget();
		setWidgetResource();
		setWidgetListener();
	}

	protected abstract void getContentCache(Bundle savedInstanceState);
	protected abstract int getLayoutResourceId();
	protected abstract void initWidget();
	protected abstract void setWidgetResource();
	protected abstract void setWidgetListener();
}
