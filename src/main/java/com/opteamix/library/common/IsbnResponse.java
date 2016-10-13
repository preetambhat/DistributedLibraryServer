package com.opteamix.library.common;

import java.util.List;

public class IsbnResponse {

	private List<Book> data;

	public List<Book> getData() {
		return data;
	}

	public void setData(List<Book> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "IsbnResponse [data=" + data + "]";
	}
	
}
