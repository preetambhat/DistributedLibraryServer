package com.opteamix.library.service;

import java.util.List;

import com.opteamix.library.common.Asset;
import com.opteamix.library.common.BookTransactionVO;
import com.opteamix.library.exception.LibraryException;

public interface IAssetService {
	
	public Asset registerBooks(Asset asset) throws LibraryException;
	
	public List<Asset> listBooks(Asset asset) throws LibraryException;

	public void takeBook(BookTransactionVO bookTransactionVO) throws LibraryException;

}
