package com.github.assignment.hotelquickly.act;

import java.io.Serializable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.assignment.hotelquickly.R;
import com.github.assignment.hotelquickly.bean.ContentModelBean;
import com.github.assignment.hotelquickly.util.NetWorkUtil;

public class WebContentActivity extends HotelQuicklyActivity {
	
	protected static final String TAG_CONTENT = "content";
	private WebView webView;
	private ProgressBar progress;
	private ContentModelBean contentBean;
	
	@Override
	protected int getLayoutResourceId() {
		return R.layout.activity_web;
	}

	@Override
	protected void getContentCache(Bundle savedInstanceState) {
		if(savedInstanceState!=null){
			Object obj =  savedInstanceState.getSerializable(TAG_CONTENT);	
			if(obj instanceof ContentModelBean)
				contentBean = (ContentModelBean) obj;
		}
	}

	@Override
	protected void initWidget() {
		webView = (WebView) findViewById(R.id.webview);
		progress = (ProgressBar) findViewById(R.id.progress);
	}
	
	@Override
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	protected void setWidgetResource() {
		
		contentBean = (ContentModelBean) getIntent().getSerializableExtra(TAG_CONTENT);
		
		if(contentBean == null)
			return;
		
		if(contentBean.isCache()){
		setupWebViewCache();
		Toast.makeText(getApplicationContext(), "Cachable", Toast.LENGTH_SHORT).show();
		}else{
		Toast.makeText(getApplicationContext(), "Uncachable", Toast.LENGTH_SHORT).show();
		}
			
		webView.getSettings().setJavaScriptEnabled(true);
		
		webView.setWebViewClient(new WebViewClient());
		
		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progressLoad) {
				if (progressLoad < 100) {
					setProgress(progressLoad);
					progress.setVisibility(View.VISIBLE);
				} else if (progressLoad == 100) {
					progress.setVisibility(View.GONE);
				}
			}
		});
		
		if(NetWorkUtil.isNetworkAvailable(this) && contentBean.isCache())
			webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		else
			webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		
		webView.loadUrl(contentBean.getUrl());
		
	}

	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(outState!=null){
			outState.putSerializable(TAG_CONTENT, (Serializable) contentBean);
		}
	}
	

	private void setupWebViewCache() {
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setAppCachePath("/data/data/" + getPackageName() + "/cache");
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setAppCacheEnabled(true);
	}

	

	@Override
	protected void setWidgetListener() {
		
	}
	
	
}
