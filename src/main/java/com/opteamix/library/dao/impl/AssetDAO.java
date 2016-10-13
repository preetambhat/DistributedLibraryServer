package com.opteamix.library.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.opteamix.library.common.Asset;
import com.opteamix.library.common.BookTransactionVO;
import com.opteamix.library.common.IsbnResponse;
import com.opteamix.library.common.LibraryConstants;
import com.opteamix.library.common.User;
import com.opteamix.library.dao.IAssetDAO;
import com.opteamix.library.dao.ILoginDAO;
import com.opteamix.library.exception.LibraryException;


@Repository
public class AssetDAO extends BaseDAO implements IAssetDAO{

	private static final Logger logger = Logger.getLogger(AssetDAO.class);
	
	@Autowired
	ILoginDAO loginDAO;
	

	@Override
	public List<Asset> listAssets(Asset asset) throws LibraryException {
		List<Asset> assetList = null;
		try{
			MongoDatabase dbConnection = getDBConnection();
//			BasicDBObject myCmd = new BasicDBObject();
			Integer userId = asset.getUserId();
			String searchText = asset.getSearchText();
			User loggedInUser = loginDAO.getUserById(userId);
			String longitude = loggedInUser.getAddress().getLongitude();
			String latitude = loggedInUser.getAddress().getLatitude();
			
			MongoCollection<Document> collection = dbConnection.getCollection("Asset");
			List<BasicDBObject> assetSearch = new ArrayList<BasicDBObject>();
			assetSearch.add(new BasicDBObject("bookDetail.name", Pattern.compile(searchText)));
			assetSearch.add(new BasicDBObject("bookDetail.isbn10", Pattern.compile(searchText)));
			assetSearch.add(new BasicDBObject("bookDetail.author", Pattern.compile(searchText)));
			assetSearch.add(new BasicDBObject("bookDetail.title", Pattern.compile(searchText)));
			BasicDBObject andQuery = new BasicDBObject();
			andQuery.put("$or", assetSearch);
			if(!asset.isGlobal()){
				andQuery.put("ownerId", userId);
			}
			logger.debug(andQuery.toString());
			FindIterable<Document> resultIterable = collection.find(andQuery);
			
			MongoCursor<Document> mongoCursor = resultIterable.iterator();
			logger.debug(mongoCursor.hasNext());
			if(mongoCursor != null){
				assetList = new ArrayList<>();
				while(mongoCursor.hasNext()){
					Document currentAsset = mongoCursor.next();
					Asset responseAsset = (new Gson()).fromJson(currentAsset.toJson(),Asset.class); 
					logger.debug(responseAsset);
					logger.debug(latitude+" : "+longitude+"asset latitude : "+asset);
					Double distance = distance(Double.valueOf(latitude),Double.valueOf(longitude),Double.valueOf(responseAsset.getLatitude()),Double.valueOf(responseAsset.getLongitude()),LibraryConstants.DISTANCE_IN_KILOMETERS);
					responseAsset.setDistance(distance);
					assetList.add(responseAsset);
				}
			}
//			document.append("bookDetail.isbn10", isbn);
//			Document resultObject   = collection.find(document).
//			double[] loc = {Double.valueOf(longitude),Double.valueOf(latitude)};
//			myCmd.append("geoNear", "Asset");
//			myCmd.append("near", loc);
//			myCmd.append("spherical", true);
//			myCmd.append("distanceMultiplier",  6378137);
//			Document myResult = dbConnection.runCommand(myCmd);
//			FindIterable<Document> documentIterable = collection.find(myCmd);
//			List<Document> documentList = (List<Document>) myResult.get("results");
//			if(documentList != null){
//				assetList = new ArrayList<>();
//				for(Document doc : documentList){
//					Double distance = doc.getDouble("dis");
//					Document documentAsset = (Document) doc.get("obj");
//					Asset asset = (new Gson()).fromJson(documentAsset.toJson(),Asset.class);
//					asset.setDistance(distance);
//					assetList.add(asset);
//				}
//			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Exception in listAssets : ",e);
			throw new LibraryException(LibraryConstants.UNEXPECTED_ERROR_MESSAGE,LibraryConstants.UNEXPECTED_ERROR_CODE);
		}
		return assetList;
	}
	
	@Override
	public Asset registerBooks(Asset asset) throws LibraryException{
		try{
			String isbn = null;
			Asset existingAsset = null;
			if(asset.getBookDetail() != null){
				isbn = asset.getBookDetail().getIsbn10();
			}
			
			MongoDatabase dbConnection = getDBConnection();
			MongoCollection<Document> collection = dbConnection.getCollection("Asset");
			if(isbn != null){
				existingAsset = checkIfBookExists(isbn,asset.getOwnerId());
			}
			logger.debug("Existing asset : "+existingAsset);
			if(existingAsset != null && existingAsset.getAssetId() != null){
				Integer count = existingAsset.getCount();
				count++;
				logger.debug("In registerBooks : updating existing count "+count);
				Bson filter = new Document("assetId", existingAsset.getAssetId());
				Bson newValue = new Document("count", count);
				Bson updateOperationDocument = new Document("$set", newValue);
				collection.updateOne(filter, updateOperationDocument);
				asset.setAssetId(existingAsset.getAssetId());
				asset.setCount(count);
				
			}else{
				asset.setAssetId(getNextSequence(dbConnection, "assetId"));
				asset.setCount(1);
				Document document = new Document();
				document.append("location", "2dsphere");
				collection.createIndex(document);
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(asset);
				collection.insertOne(Document.parse(json));
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Exception in registerBooks : ",e);
			throw new LibraryException(LibraryConstants.UNEXPECTED_ERROR_MESSAGE,LibraryConstants.UNEXPECTED_ERROR_CODE);
		}
		return asset;
	}
	
	
	private Asset checkIfBookExists(String isbn, Integer ownerId) throws LibraryException {
		Asset existingAsset = null;
		try{
			MongoDatabase dbConnection = getDBConnection();
			MongoCollection<Document> collection = dbConnection.getCollection("Asset");
			Document document = new Document();
			document.append("ownerId", ownerId);
			document.append("bookDetail.isbn10", isbn);
			Document resultObject   = collection.find(document).first();
			if(resultObject != null){
				existingAsset = (new Gson()).fromJson(resultObject.toJson(),Asset.class); 
			}
		}catch(Exception e){
			logger.error("Exception in checkIfBookExists : ",e);
			throw new LibraryException(LibraryConstants.UNEXPECTED_ERROR_MESSAGE,LibraryConstants.UNEXPECTED_ERROR_CODE);
		}
		return existingAsset;
	}

	@Override
	public Asset registerBook(Asset asset) throws LibraryException {
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
	    converters.add(new MappingJackson2HttpMessageConverter());
	    converters.add(new ByteArrayHttpMessageConverter());
	    converters.add(new FormHttpMessageConverter());
	    converters.add(new StringHttpMessageConverter());
	    restTemplate.setMessageConverters(converters);
	    try{
		    HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			String url = "http://isbndb.com/api/v2/json/RXB41B02/book/";
			url += asset.getIsbn();
			String responseString = restTemplate.getForObject(url,String.class);
			Gson gson = new Gson();
			IsbnResponse isbnResponse = gson.fromJson(responseString, IsbnResponse.class);
			logger.debug(isbnResponse);
			asset.setBookDetail(isbnResponse.getData().get(0));
	    }catch(Exception e){
	    	logger.error("Exception in registerBook : ",e);
	    	throw new LibraryException(LibraryConstants.UNEXPECTED_ERROR_MESSAGE,LibraryConstants.UNEXPECTED_ERROR_CODE);
	    }
	    return asset;
	}

	@Override
	public Asset getAssetByAssetId(Integer assetId) throws LibraryException {
		Asset existingAsset = null;
		try{
			MongoDatabase dbConnection = getDBConnection();
			MongoCollection<Document> collection = dbConnection.getCollection("Asset");
			Document document = new Document();
			document.append("assetId", assetId);
			Document resultObject   = collection.find(document).first();
			if(resultObject != null){
				existingAsset = (new Gson()).fromJson(resultObject.toJson(),Asset.class); 
			}
		}catch(Exception e){
			logger.error("Exception in checkIfBookExists : ",e);
			throw new LibraryException(LibraryConstants.UNEXPECTED_ERROR_MESSAGE,LibraryConstants.UNEXPECTED_ERROR_CODE);
		}
		return existingAsset;
	}

	@Override
	public BookTransactionVO takeBook(BookTransactionVO bookTransactionVO) throws LibraryException {
		MongoDatabase dbConnection = getDBConnection();
		MongoCollection<Document> collection = dbConnection.getCollection("BookTransaction");
		try{
			bookTransactionVO.setBookTransactionId(getNextSequence(dbConnection, "bookTransactionId"));
			bookTransactionVO.setCount(1);
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(bookTransactionVO);
			collection.insertOne(Document.parse(json));
		}catch(Exception e){
			logger.error("Exception in takeBook : ",e);
			throw new LibraryException(LibraryConstants.UNEXPECTED_ERROR_MESSAGE,LibraryConstants.UNEXPECTED_ERROR_CODE);
		}
		return bookTransactionVO;
	}

	@Override
	public BookTransactionVO checkIfBookIsAlreadyTaken(BookTransactionVO bookTransactionVO) throws LibraryException {
		BookTransactionVO existingBook = null;
		try{
			MongoDatabase dbConnection = getDBConnection();
			MongoCollection<Document> collection = dbConnection.getCollection("BookTransaction");
			Document document = new Document();
			document.append("userId", bookTransactionVO.getUserId());
			document.append("assetId",bookTransactionVO.getAssetId());
			Document resultObject   = collection.find(document).first();
			if(resultObject != null){
				existingBook = (new Gson()).fromJson(resultObject.toJson(),BookTransactionVO.class); 
			}
		}catch(Exception e){
			logger.error("Exception in checkIfBookIsAlreadyTaken : ",e);
			throw new LibraryException(LibraryConstants.UNEXPECTED_ERROR_MESSAGE,LibraryConstants.UNEXPECTED_ERROR_CODE);
		}
		return existingBook;
	}

	@Override
	public void updateBookIssueCount(Integer bookTransactionId, Integer issueCount) throws LibraryException {
		try{
			MongoDatabase dbConnection = getDBConnection();
			MongoCollection<Document> collection = dbConnection.getCollection("BookTransaction");
			Bson filter = new Document("bookTransactionId", bookTransactionId);
			Bson newValue = new Document("count", issueCount);
			Bson updateOperationDocument = new Document("$set", newValue);
			collection.updateOne(filter, updateOperationDocument);
		}catch(Exception e){
			logger.error("Exception in updateBookIssueCount : ",e);
			throw new LibraryException(LibraryConstants.UNEXPECTED_ERROR_MESSAGE,LibraryConstants.UNEXPECTED_ERROR_CODE);
		}
	}

	@Override
	public void decreaseAssetCount(Integer assetId, Integer bookCount) throws LibraryException {
		try{
			
		}catch(Exception e){
			logger.error("Exception in updateBookIssueCount : ",e);
			throw new LibraryException(LibraryConstants.UNEXPECTED_ERROR_MESSAGE,LibraryConstants.UNEXPECTED_ERROR_CODE);
		}
	}
	
	private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == "K") {
			dist = dist * 1.609344;
		} else if (unit == "N") {
			dist = dist * 0.8684;
		}

		return (dist);
	}
	
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	
	
}
