package com.opteamix.library.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.opteamix.library.common.Asset;
import com.opteamix.library.common.BookTransactionVO;
import com.opteamix.library.common.LibraryConstants;
import com.opteamix.library.exception.LibraryException;
import com.opteamix.library.service.IAssetService;

@Controller
@RequestMapping("/book")
public class AssetController extends AbstractBaseController{
	
	private static final Logger logger = Logger.getLogger(AssetController.class);
	
	@Autowired
	private IAssetService assetService;

	@RequestMapping(value="/registerBookWithISBN",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> registerBookWithISBN(@RequestBody Asset asset){
		Map<String,Object> responseMap = new HashMap<>();
		try {
			validateUserAccess(asset.getUserId(),asset.getAuthToken());
			Asset responseAsset = assetService.registerBooks(asset);
			responseMap.put(LibraryConstants.OBJECT,responseAsset);
			responseMap.put(LibraryConstants.STATUS,LibraryConstants.SUCCESS);
			responseMap.put(LibraryConstants.STATUS_CODE,LibraryConstants.SUCCESS_CODE);
		}catch(LibraryException e){
			logger.error("Library Exeption : ",e);
			responseMap.put(LibraryConstants.MESSAGE, e.getMessage());
			responseMap.put(LibraryConstants.ERROR_CODE, e.getErrorCode());
		}catch (Exception e) {
			logger.error("Generic Exeption : ",e);
			responseMap.put(LibraryConstants.MESSAGE, LibraryConstants.UNEXPECTED_ERROR_MESSAGE);
			responseMap.put(LibraryConstants.ERROR_CODE, LibraryConstants.UNEXPECTED_ERROR_CODE);
		}
		return responseMap;
	}
	
	@RequestMapping(value="/listBooks",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> listBooks(@RequestBody Asset asset){
		Map<String,Object> responseMap = new HashMap<>();
		try {
			validateUserAccess(asset.getUserId(),asset.getAuthToken());
			List<Asset> assetList = assetService.listBooks(asset);
			responseMap.put(LibraryConstants.OBJECT,assetList);
			responseMap.put(LibraryConstants.STATUS,LibraryConstants.SUCCESS);
			responseMap.put(LibraryConstants.STATUS_CODE,LibraryConstants.SUCCESS_CODE);
		}catch(LibraryException e){
			logger.error("Library Exeption : ",e);
			responseMap.put(LibraryConstants.MESSAGE, e.getMessage());
			responseMap.put(LibraryConstants.ERROR_CODE, e.getErrorCode());
		}catch (Exception e) {
			logger.error("Generic Exeption : ",e);
			responseMap.put(LibraryConstants.MESSAGE, LibraryConstants.UNEXPECTED_ERROR_MESSAGE);
			responseMap.put(LibraryConstants.ERROR_CODE, LibraryConstants.UNEXPECTED_ERROR_CODE);
		}
		return responseMap;
	}
	
	
	
	@RequestMapping(value="/takeBook",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> takeBook(@RequestBody BookTransactionVO bookTransactionVO){
		Map<String,Object> responseMap = new HashMap<>();
		try {
			validateUserAccess(bookTransactionVO.getUserId(),bookTransactionVO.getAuthToken());
			assetService.takeBook(bookTransactionVO);
//			responseMap.put(LibraryConstants.OBJECT,assetList);
			responseMap.put(LibraryConstants.STATUS,LibraryConstants.SUCCESS);
			responseMap.put(LibraryConstants.STATUS_CODE,LibraryConstants.SUCCESS_CODE);
		}catch(LibraryException e){
			logger.error("Library Exeption : ",e);
			responseMap.put(LibraryConstants.MESSAGE, e.getMessage());
			responseMap.put(LibraryConstants.ERROR_CODE, e.getErrorCode());
		}catch (Exception e) {
			logger.error("Generic Exeption : ",e);
			responseMap.put(LibraryConstants.MESSAGE, LibraryConstants.UNEXPECTED_ERROR_MESSAGE);
			responseMap.put(LibraryConstants.ERROR_CODE, LibraryConstants.UNEXPECTED_ERROR_CODE);
		}
		return responseMap;
	}
	
	
}
