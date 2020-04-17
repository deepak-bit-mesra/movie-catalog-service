package com.example.demo.models;

import java.util.List;

public class CatalogItemWrapper {
	private List<CatalogItem> catalogItems;
	private TraceInfo traceInfo;
	private String userId;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public CatalogItemWrapper() {
		super();
		// TODO Auto-generated constructor stub
	}
	public List<CatalogItem> getCatalogItems() {
		return catalogItems;
	}
	public void setCatalogItems(List<CatalogItem> catalogItems) {
		this.catalogItems = catalogItems;
	}
	
	public TraceInfo getTraceInfo() {
		return traceInfo;
	}
	public void setTraceInfo(TraceInfo traceInfo) {
		this.traceInfo = traceInfo;
	}
	
	
	
	
	
	
	
	
	
}
