package com.github.assignment.hotelquickly.util;

import org.apache.http.Header;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class MyAsyncHttpResponseHAndler extends AsyncHttpResponseHandler {

	private CallBack callback;
	private Class<?> clazz;
	private String tag;

	public MyAsyncHttpResponseHAndler(CallBack callback, Class<?> clazz, String tag) {
		this.callback = callback;
		this.clazz = clazz;
		this.tag = tag;
	}

	@Override
	public void onStart() {
		super.onStart();
		callback.onStartLoading(tag);
		System.out.println("onStart Request");
	}

	@Override
	public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		callback.onNetworkFailure(tag);
	}

	@Override
	public void onSuccess(String response) {
		if (TextUtils.isEmpty(response))
			callback.onResponseFailure(tag);
		else {
			try {
				Gson gson = new Gson();
				Object obj = gson.fromJson(response, clazz); 
				if (obj != null)
					callback.onSuccess(obj, tag);
				else
					callback.onResponseFailure(tag);
			} catch (JsonSyntaxException e) {
				callback.onResponseFailure(tag);
			}
		}

	}

	public interface CallBack {
		
		public void onStartLoading(String tag);

		public void onSuccess(Object obj, String tag);

		public void onNetworkFailure(String tag);

		public void onResponseFailure(String tag);
	}

}