package com.example.demo.models;

import java.util.List;

public class CatalogItemWrapper {
	private List<CatalogItem> catalogItems;
	private String userId;
	public List<CatalogItem> getCatalogItems() {
		return catalogItems;
	}
	public void setCatalogItems(List<CatalogItem> catalogItems) {
		this.catalogItems = catalogItems;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public CatalogItemWrapper(List<CatalogItem> catalogItems, String userId) {
		super();
		this.catalogItems = catalogItems;
		this.userId = userId;
	}
	public CatalogItemWrapper() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
	
	
	
}
