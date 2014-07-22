package com.github.assignment.hotelquickly.bean;

import java.io.Serializable;
import java.util.List;

public class ContentModelBean implements Serializable {

	private static final long serialVersionUID = 1800905231535590357L;
	private String key;
	private String url;
	private String filePath;
	private String namespace;
	private boolean cache;
	private List<String> params;
	private String pageTitle;
	private TemplateLastUpdated templateLastUpdated;
	
	public TemplateLastUpdated getTemplateLastUpdated() {
		return templateLastUpdated;
	}
	public void setTemplateLastUpdated(TemplateLastUpdated templateLastUpdated) {
		this.templateLastUpdated = templateLastUpdated;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public boolean isCache() {
		return cache;
	}
	public void setCache(boolean cache) {
		this.cache = cache;
	}
	public List<String> getParams() {
		return params;
	}
	public void setParams(List<String> params) {
		this.params = params;
	}
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	
	public class TemplateLastUpdated implements Serializable {
		
		
		private static final long serialVersionUID = 5981148305379367240L;
		
		private long unixTimestamp;
		private String dateTime;
		
		public long getUnixTimestamp() {
			return unixTimestamp;
		}
		public void setUnixTimestamp(long unixTimestamp) {
			this.unixTimestamp = unixTimestamp;
		}
		public String getDateTime() {
			return dateTime;
		}
		public void setDateTime(String dateTime) {
			this.dateTime = dateTime;
		}
		
	}
}
