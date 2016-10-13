package com.opteamix.library.common;

public class BaseVO {

	private Integer userId;
	private String authToken;
	private String searchText;
	private String soryBy;
	private boolean global;
	
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getAuthToken() {
		return authToken;
	}
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	public String getSearchText() {
		return searchText;
	}
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	public String getSoryBy() {
		return soryBy;
	}
	public void setSoryBy(String soryBy) {
		this.soryBy = soryBy;
	}
	public boolean isGlobal() {
		return global;
	}
	public void setGlobal(boolean global) {
		this.global = global;
	}
	
	
}
