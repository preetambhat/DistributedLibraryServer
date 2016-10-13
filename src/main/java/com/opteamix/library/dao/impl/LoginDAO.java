package com.opteamix.library.dao.impl;

import static com.mongodb.client.model.Filters.eq;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.opteamix.library.common.AddressVO;
import com.opteamix.library.common.LibraryConstants;
import com.opteamix.library.common.User;
import com.opteamix.library.dao.ILoginDAO;
import com.opteamix.library.exception.LibraryException;

@Repository
public class LoginDAO extends BaseDAO implements ILoginDAO{

	private static final Logger logger = Logger.getLogger(LoginDAO.class);
	
	@Override
	public boolean isUserExisting(User user) throws LibraryException {
		boolean exists = false;
		User dbUser = null;
		try{
			MongoDatabase dbConnection = getDBConnection();
			MongoCollection<Document> collection = dbConnection.getCollection("User");
		    Document resultObject   = collection.find(eq("userEmail", user.getUserEmail())).first();
			if(resultObject != null){
				dbUser = (new Gson()).fromJson(resultObject.toJson(),User.class); 
			}
			if(dbUser != null && dbUser.getUserId() != null){
				exists = true;
				throw new LibraryException(LibraryConstants.USER_EXISTS_WITH_THE_GIVEN_EMAIL,-1002);
			}
		}catch(LibraryException e){
			throw e;
		}catch(Exception e){
			e.printStackTrace();
			throw new LibraryException(LibraryConstants.UNEXPECTED_ERROR_MESSAGE,1001);
		}
		return exists;
	}

	@Override
	public User createUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateUser(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public User signUp(User user) throws LibraryException {
		try{
			isUserExisting(user);
			MongoDatabase dbConnection = getDBConnection();
			MongoCollection<Document> collection = dbConnection.getCollection("User");
			user.setUserId(getNextSequence(dbConnection, "userId"));
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(user);
			collection.insertOne(Document.parse(json));
		}catch(LibraryException e){
			throw e;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Exception in signUp : ",e);
			throw new LibraryException(LibraryConstants.UNEXPECTED_ERROR_MESSAGE,LibraryConstants.UNEXPECTED_ERROR_CODE);
		}
		return user;
	}

	@Override
	public User signIn(User user) throws LibraryException {
		User dbUser = null;
		try{
			MongoDatabase dbConnection = getDBConnection();
			MongoCollection<Document> collection = dbConnection.getCollection("User");
			Document document = new Document();
			document.append("userEmail", user.getUserEmail());
			document.append("password", user.getPassword());
			Document resultObject   = collection.find(document).first();
			if(resultObject != null){
				dbUser = (new Gson()).fromJson(resultObject.toJson(),User.class); 
			}
			if(dbUser == null){
				throw new LibraryException(LibraryConstants.INCORRECT_USERNAME_PASSWORD,-1003);
			}
		}catch(LibraryException e){
			throw e;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Exception in signIn : ",e);
			throw new LibraryException(LibraryConstants.UNEXPECTED_ERROR_MESSAGE,LibraryConstants.UNEXPECTED_ERROR_CODE);
		}
		return dbUser;
	}

	@Override
	public User getUserById(Integer userId) throws LibraryException {
		User user = null;
		try{
			System.out.println(userId);
			MongoDatabase dbConnection = getDBConnection();
			MongoCollection<Document> collection = dbConnection.getCollection("User");
		    Document resultObject   = collection.find(eq("userId", userId)).first();
		    System.out.println(resultObject);
			if(resultObject != null){
				user = (new Gson()).fromJson(resultObject.toJson(),User.class); 
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Exception in getUserById : ",e);
			throw new LibraryException(LibraryConstants.UNEXPECTED_ERROR_MESSAGE,LibraryConstants.UNEXPECTED_ERROR_CODE);
		}
		return user;
	}

	@Override
	public AddressVO addAddress(AddressVO address) throws LibraryException {
		try{
			MongoDatabase dbConnection = getDBConnection();
			MongoCollection<Document> collection = dbConnection.getCollection("Address");
			address.setAddressId(getNextSequence(dbConnection, "addressId"));
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(address);
			collection.insertOne(Document.parse(json));
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Exception in addAddress : ",e);
			throw new LibraryException(LibraryConstants.UNEXPECTED_ERROR_MESSAGE,LibraryConstants.UNEXPECTED_ERROR_CODE);
		}
		return address;
	}

}
