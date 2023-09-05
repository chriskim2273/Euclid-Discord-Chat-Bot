package Customs;

import java.net.UnknownHostException;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class CustomMongoDB {
	private static String password = "pokemon227";
	
	public static String uri = "mongodb+srv://eucliduser:" + password + "@euclidbot.qvjvz.mongodb.net/<dbname>?retryWrites=true&w=majority";
	public static MongoClientURI clientURI = new MongoClientURI(uri);
	public static MongoClient mongoClient = new MongoClient(clientURI);
	public static MongoDatabase customDatabase = mongoClient.getDatabase("Customs");
	public static MongoCollection<Document> PlayerData = customDatabase.getCollection("PlayerData");
	public static DBCollection Settings;
	
	//Create new database for each server.
	
	public CustomMongoDB() throws UnknownHostException {
		clientURI = new MongoClientURI(uri);
		mongoClient = new MongoClient(clientURI);
		customDatabase = mongoClient.getDatabase("Customs");
		PlayerData = customDatabase.getCollection("PlayerData"); //Will not need due to collections for each server.
		//Settings = customDatabase.collect
	}
	
	public static MongoCollection<Document> createCollections(String serverId) {
		customDatabase.createCollection(serverId);
		MongoCollection<Document> createdCollection = customDatabase.getCollection(serverId);
		return createdCollection;
	}
	
	public static void retrievePlayers() {
		FindIterable<Document> Player = PlayerData.find();
		MongoCursor<Document> cursor = Player.cursor();
		while(cursor.hasNext()) {
			Document next = (Document) cursor.next();
			System.out.println(next);
			new CustomsPlayer(next.get("userID").toString(),next.get("Wins").toString(),next.get("Losses").toString(),next.get("MMR").toString());
		}
		System.out.println(CustomsPlayer.allCustomsPlayers.toString());
	}
	
	public static void retrieveServerPlayers(String serverId) {
		MongoCollection<Document> serverCollection = customDatabase.getCollection(serverId);
		FindIterable<Document> Player = serverCollection.find();
		MongoCursor<Document> cursor = Player.cursor();
		while(cursor.hasNext()) {
			Document next = (Document) cursor.next();
			System.out.println(next);
			new CustomsPlayer(next.get("userID").toString(),next.get("Wins").toString(),next.get("Losses").toString(),next.get("MMR").toString());
		}
		System.out.println(CustomsPlayer.allCustomsPlayers.toString());
	}
	
//	public static void retrieveSettings() {
//		DBCursor cursor = PlayerData.find();
//		while(cursor.hasNext()) {
//			DBObject next = cursor.next();
//			System.out.println(next);
//			new CustomsPlayer(next.get("userID").toString(),next.get("Wins").toString(),next.get("Losses").toString());
//		}
//	}
	
	public static void savePlayer(CustomsPlayer player) {
		Document oldPlayer = (Document) PlayerData.find(new Document("userID",player.getUserID())).first();//new BasicDBObject("userID",player.getUserID());
		Document playerObject = convert(player);
		Bson updateOperation = new Document("$set",playerObject);
		
		if(oldPlayer==null) {
			PlayerData.insertOne(playerObject);
		}else {
			PlayerData.updateOne(oldPlayer, updateOperation);
		}
	}
	
	public static Document convert(CustomsPlayer player) {
		return new Document("userID", player.getUserID())
				.append("Wins", player.getWins())
				.append("Losses", player.getLosses())
				.append("MMR", player.getMMR());
	}
}
