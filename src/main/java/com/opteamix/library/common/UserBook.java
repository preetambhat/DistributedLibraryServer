package com.opteamix.library.common;

import java.util.Date;

public class UserBook {
	
	public Integer userId;
	private Integer bookId;
	private Date purchasedOn;
	private Date purchasedTill;
	private boolean available;
	
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getBookId() {
		return bookId;
	}
	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}
	public Date getPurchasedOn() {
		return purchasedOn;
	}
	public void setPurchasedOn(Date purchasedOn) {
		this.purchasedOn = purchasedOn;
	}
	public Date getPurchasedTill() {
		return purchasedTill;
	}
	public void setPurchasedTill(Date purchasedTill) {
		this.purchasedTill = purchasedTill;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}

	
	
}
