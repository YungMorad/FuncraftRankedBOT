package me.dolla.teamranked.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.dolla.teamranked.RankedTeam;
import me.dolla.teamranked.util.PropertiesReader;
import org.bson.Document;

public class MongoManager {
    public static MongoDatabase database;
    public static MongoClient client;
    public static MongoCollection<Document> teams, players, games;


    public static void init() {
        client = MongoClients.create(new PropertiesReader().getProperty("mongoUri"));
        database = client.getDatabase(new PropertiesReader().getProperty("mongoDatabase"));
        loadCollections();
    }

    public static void loadCollections() {
        teams = database.getCollection("Team");
        players = database.getCollection("Players");
        games = database.getCollection("Games");
    }
}
