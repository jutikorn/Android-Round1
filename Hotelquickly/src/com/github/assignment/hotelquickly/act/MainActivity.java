package com.github.assignment.hotelquickly.act;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.github.assignment.hotelquickly.R;
import com.github.assignment.hotelquickly.bean.ContentModelBean;
import com.github.assignment.hotelquickly.bean.HotelQuicklyMap;
import com.github.assignment.hotelquickly.common.URLs;
import com.github.assignment.hotelquickly.util.MyAsyncHttpResponseHAndler;
import com.github.assignment.hotelquickly.util.MyAsyncHttpResponseHAndler.CallBack;
import com.github.assignment.hotelquickly.util.NetWorkUtil;
import com.manuelpeinado.refreshactionitem.ProgressIndicatorType;
import com.manuelpeinado.refreshactionitem.RefreshActionItem;
import com.manuelpeinado.refreshactionitem.RefreshActionItem.RefreshActionListener;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class MainActivity extends HotelQuicklyActivity implements RefreshActionListener,CallBack {
	
	

	private static final String TAG_LIST = "web list";
	private ListView list_web;
	private RefreshActionItem  mRefreshActionItem;

	private NetworkReceiver networkReceiver;
	private List<ContentModelBean> webContentList;

	@Override
	protected int getLayoutResourceId() {
		return R.layout.activity_main;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		MenuItem item = menu.findItem(R.id.refresh_button);
		mRefreshActionItem = (RefreshActionItem) item.getActionView();
	    mRefreshActionItem.setMenuItem(item);
	    mRefreshActionItem.setProgressIndicatorType(ProgressIndicatorType.INDETERMINATE);
	    mRefreshActionItem.setRefreshActionListener(this);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void setWidgetResource() {
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void getContentCache(Bundle savedInstanceState) {
		if(savedInstanceState!=null){
			Object obj =  savedInstanceState.getSerializable(TAG_LIST);	
			if(obj instanceof List<?>)
				webContentList = (List<ContentModelBean>) obj;
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(outState!=null){
			outState.putSerializable(TAG_LIST, (Serializable) webContentList);
			
		}
	}
	@Override
	protected void initWidget() {
		list_web = (ListView)  findViewById(R.id.list_web);
	}
	@Override
	protected void setWidgetListener() {
		list_web.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MainActivity.this, WebContentActivity.class);
				intent.putExtra(WebContentActivity.TAG_CONTENT, webContentList.get(position));
				startActivity(intent);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		setNetworkReceiver();
	}
	
	
	@Override
	public void onPause() {
		super.onPause();
		
		try {
			if (networkReceiver == null)
				return;
			unregisterReceiver(networkReceiver);
		} catch (IllegalArgumentException e) {
		}

		

	}
	private void setNetworkReceiver() {
		final IntentFilter filters = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		if (networkReceiver == null)
			networkReceiver = new NetworkReceiver();
		registerReceiver(networkReceiver, filters);

	}

	
	public class NetworkReceiver extends BroadcastReceiver {


		public NetworkReceiver() {

		}

		@Override
		public void onReceive(Context context, Intent intent) {

			if (NetWorkUtil.isNetworkAvailable(MainActivity.this)) {
				if(webContentList == null){
					NetWorkUtil.connect(URLs.url, new MyAsyncHttpResponseHAndler(MainActivity.this, HotelQuicklyMap.class, TAG_LIST));
				}else{
					list_web.setAdapter(new WebContentListAdapter());
				}
				
			} else {
				Toast.makeText(getApplicationContext(), "Network Connection problem", Toast.LENGTH_SHORT).show();
				Crouton.makeText(MainActivity.this, "Network Connection problem", Style.ALERT).show();
			}
		}
	}


	


	private String replaceURLValue(String url) {
		if(!TextUtils.isEmpty(url)){
			
			url = url.replace(URLs.TAG_USERID, URLs.userId);
			url = url.replace(URLs.TAG_APPSECRETKEY, URLs.appSecretKey);
			url = url.replace(URLs.TAG_CURRENCYCODE, URLs.currencyCode);
			url = url.replace(URLs.TAG_OFFERID, URLs.offerId);
			url = url.replace(URLs.TAG_SELECTEDVOUCHERS, URLs.selectedVouchers);
		}
			
		return url;
	}
	
	@Override
	public void onSuccess(Object obj, String tag) {
		mRefreshActionItem.showProgress(false);
		if(TAG_LIST.equals(tag)){
			if(obj instanceof HotelQuicklyMap){
			HotelQuicklyMap hotelMap = (HotelQuicklyMap) obj;
			webContentList = new ArrayList<ContentModelBean>();
			
			for(String key : hotelMap.keySet() ) {
				ContentModelBean contentModelBean = hotelMap.get(key);
				contentModelBean.setKey(key);
				contentModelBean.setUrl(replaceURLValue(contentModelBean.getUrl()));
				webContentList.add(contentModelBean);
			}
				
			}
			if (webContentList!=null) 
				list_web.setAdapter(new WebContentListAdapter());
			 else 
				onResponseFailure(tag);
		}
	}

	@Override
	public void onNetworkFailure(String tag) {
		Toast.makeText(getApplicationContext(), "Network Connection problem", Toast.LENGTH_SHORT).show();
		Crouton.makeText(MainActivity.this, "Network Connection problem", Style.ALERT).show();
		mRefreshActionItem.showProgress(false);
	}


	@Override
	public void onResponseFailure(String tag) {
		Toast.makeText(getApplicationContext(), "Service unavailable", Toast.LENGTH_SHORT).show();
		Crouton.makeText(MainActivity.this, "Service unavailable", Style.ALERT).show();
		mRefreshActionItem.showProgress(false);
		
	}
	
	private class WebContentListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return webContentList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView ==null){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_row_listcontent, null);
				holder.txt_Key = (TextView) convertView.findViewById(R.id.txt_Key);
				holder.txt_Url = (TextView) convertView.findViewById(R.id.txt_Url);
				holder.txt_FilePath = (TextView) convertView.findViewById(R.id.txt_FilePath);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.txt_Key.setText(webContentList.get(position).getKey());
			holder.txt_Url.setText(webContentList.get(position).getUrl());
			holder.txt_FilePath.setText(webContentList.get(position).getFilePath());
			
			return convertView;
		}
		
		
		class ViewHolder {
			private TextView txt_Key;
			private TextView txt_Url;
			private TextView txt_FilePath;
		}
	}


	@Override
	public void onRefreshButtonClick(RefreshActionItem sender) {
		if(NetWorkUtil.isNetworkAvailable(this)){
			mRefreshActionItem.showProgress(true);
			NetWorkUtil.connect(URLs.url, new MyAsyncHttpResponseHAndler(MainActivity.this, HotelQuicklyMap.class, TAG_LIST));
		}else {
			onNetworkFailure(null);
		}
	}

	@Override
	public void onStartLoading(String tag) {
		if(mRefreshActionItem!=null)
		mRefreshActionItem.showProgress(true);
	}

	
}
