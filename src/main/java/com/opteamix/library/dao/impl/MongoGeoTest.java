package com.opteamix.library.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.opteamix.library.common.Asset;

public class MongoGeoTest {

	public static void main(String[] args) {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase database = mongoClient.getDatabase("library");
		MongoCollection<Document> collection = database.getCollection("Asset");
		Document document = new Document();
		Asset existingAsset = null;
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
//		obj.add(new BasicDBObject("bookDetail.name", Pattern.compile("Basics")));
		obj.add(new BasicDBObject("bookDetail.isbn10", Pattern.compile("Basics")));
		obj.add(new BasicDBObject("bookDetail.author", Pattern.compile("Basics")));
		obj.add(new BasicDBObject("bookDetail.title", Pattern.compile("Basics")));
//		document.append("bookDetail.name", "Basics*");
//		document.append("bookDetail.isbn10", "Basics*");
//		document.append("bookDetail.author", "Basics*");
//		document.append("bookDetail.title", "Basics*");
		BasicDBObject andQuery = new BasicDBObject();

		andQuery.put("$or", obj);
		System.out.println(andQuery.toString());
		Document resultObject = collection.find(andQuery).first();
		if (resultObject != null) {
			System.out.println(resultObject);
			existingAsset = (new Gson()).fromJson(resultObject.toJson(), Asset.class);
		}
		// BasicDBObject myCmd = new BasicDBObject();
		// myCmd.append("geoNear", "Asset");
		// double[] location = {77.5792446,12.9365123};
		// myCmd.append("near", location);
		// myCmd.append("spherical", true);
		// myCmd.append("bookDetail.name", "Book");
		//// myCmd.append("maxDistance", 2000000);
		//// myCmd.append("distanceMultiplier", 6378137);
		// System.out.println(myCmd);
		// Document myResult = database.runCommand(myCmd);
		// System.out.println(myResult);
		// @SuppressWarnings("unchecked")
		// List<Document> documentList = (List<Document>)
		// myResult.get("results");
		// for(Document doc : documentList){
		// System.out.println(doc);
		// }
		mongoClient.close();
	}
}
