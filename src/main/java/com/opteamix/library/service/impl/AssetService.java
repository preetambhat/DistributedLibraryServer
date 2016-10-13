package com.opteamix.library.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opteamix.library.common.Asset;
import com.opteamix.library.common.BookTransactionVO;
import com.opteamix.library.common.LibraryConstants;
import com.opteamix.library.dao.IAssetDAO;
import com.opteamix.library.exception.LibraryException;
import com.opteamix.library.service.IAssetService;

@Service
public class AssetService implements IAssetService{

	private static final Logger logger = Logger.getLogger(AssetService.class);
	
	@Autowired
	public IAssetDAO assetDAO;
	
	@Override
	public Asset registerBooks(Asset asset) throws LibraryException {
//		Location location = new Location();
//		location.setType("Point");
//		double[] latLong = new double[2];
//		latLong[0] = Double.valueOf(asset.getLongitude());
//		latLong[1] = Double.valueOf(asset.getLatitude());
//		location.setCoordinates(latLong);
//		asset.setLocation(location);
		Asset responseAsset = assetDAO.registerBooks(asset);
		return responseAsset;
	}

	@Override
	public List<Asset> listBooks(Asset asset) throws LibraryException {
		try{
		List<Asset> assetList = assetDAO.listAssets(asset);
		String sortBy = asset.getSoryBy();
		if(sortBy == null){
			Collections.sort(assetList,new BookNameComparator());
			return assetList;
		}if(sortBy.equals("")){
			Collections.sort(assetList,new BookNameComparator());
			return assetList;
		}
		if(sortBy.equals(LibraryConstants.SORT_BY_DISTANCE)){
			Collections.sort(assetList,new DistanceComparator());
		}else if(sortBy.equals(LibraryConstants.SORT_BY_NAME)){
			Collections.sort(assetList,new BookNameComparator());
		}else if(sortBy.equals(LibraryConstants.SORT_BY_TITLE)){
			Collections.sort(assetList,new BookTitleComparator());
		}
			return assetList;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Exception in listBooks() :",e);
			throw new LibraryException(LibraryConstants.UNEXPECTED_ERROR_MESSAGE,LibraryConstants.UNEXPECTED_ERROR_CODE);
		}
	}

	@Override
	public void takeBook(BookTransactionVO bookTransactionVO) throws LibraryException {
		Asset responseAsset = assetDAO.getAssetByAssetId(bookTransactionVO.getAssetId());
		Integer bookCount = null;
		bookCount = responseAsset.getCount();
		if(bookCount > 0){
			BookTransactionVO responseBookTransaction = assetDAO.checkIfBookIsAlreadyTaken(bookTransactionVO);
			if(responseBookTransaction != null){
				Integer issueCount = responseBookTransaction.getCount();
				issueCount++;
				assetDAO.updateBookIssueCount(responseBookTransaction.getBookTransactionId(),issueCount);
			}else{
				assetDAO.takeBook(bookTransactionVO);
			}
			bookCount--;
			assetDAO.decreaseAssetCount(responseAsset.getAssetId(),bookCount);
		}else{
			throw new LibraryException(LibraryConstants.REQUESTED_BOOK_NOT_AVAILABLE,-1007);
		}
		
	}
	
	
	private class DistanceComparator implements Comparator<Asset>{
		@Override
		public int compare(Asset asset1, Asset asset2) {
			return (int) Math.round((asset1.getDistance()-asset2.getDistance()));
		}
	}
	
	
	private class BookNameComparator implements Comparator<Asset>{
		@Override
		public int compare(Asset asset1, Asset asset2) {
			return asset1.getBookDetail().getBook_id().compareTo(asset2.getBookDetail().getBook_id());
		}
	}
	
	
	private class BookTitleComparator implements Comparator<Asset>{
		@Override
		public int compare(Asset asset1, Asset asset2) {
			return asset1.getBookDetail().getTitle().compareTo(asset2.getBookDetail().getTitle());
		}
	}
}
