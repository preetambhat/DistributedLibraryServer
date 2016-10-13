package com.opteamix.library.dao;

import java.util.List;

import com.opteamix.library.common.Asset;
import com.opteamix.library.common.BookTransactionVO;
import com.opteamix.library.exception.LibraryException;

public interface IAssetDAO {
	
	public Asset registerBook(Asset asset) throws LibraryException;
	
	public Asset registerBooks(Asset asset) throws LibraryException;
	
	public List<Asset> listAssets(Asset asset) throws LibraryException;

	public Asset getAssetByAssetId(Integer assetId) throws LibraryException;

	public BookTransactionVO takeBook(BookTransactionVO bookTransactionVO) throws LibraryException;

	public BookTransactionVO checkIfBookIsAlreadyTaken(BookTransactionVO bookTransactionVO) throws LibraryException;

	public void updateBookIssueCount(Integer bookTransactionId, Integer issueCount) throws LibraryException;

	public void decreaseAssetCount(Integer assetId, Integer bookCount) throws LibraryException;


}
