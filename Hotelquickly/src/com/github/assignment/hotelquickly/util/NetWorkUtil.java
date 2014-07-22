package com.github.assignment.hotelquickly.util;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkUtil {

	public static boolean isNetworkAvailable(Context con) {
		if(con==null)
			return false;
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	public static void connect(String url, AsyncHttpResponseHandler handler){
		AsyncHttpClient client = new AsyncHttpClient();
		
		client.setTimeout(1*60*1000);
		client.get(url.trim(), handler);


	}
}
