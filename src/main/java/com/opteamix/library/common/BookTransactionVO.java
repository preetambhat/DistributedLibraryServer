package com.opteamix.library.common;

import java.util.Date;

public class BookTransactionVO extends BaseVO{
	
	private Integer bookTransactionId;
	private Integer assetId;
	private Date issueDate;
	private Date issuedTillDate;
	private Integer count;
	

	public Integer getBookTransactionId() {
		return bookTransactionId;
	}
	public void setBookTransactionId(Integer bookTransactionId) {
		this.bookTransactionId = bookTransactionId;
	}
	public Integer getAssetId() {
		return assetId;
	}
	public void setAssetId(Integer assetId) {
		this.assetId = assetId;
	}
	public Date getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}
	public Date getIssuedTillDate() {
		return issuedTillDate;
	}
	public void setIssuedTillDate(Date issuedTillDate) {
		this.issuedTillDate = issuedTillDate;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
		

}
