import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.FindIterable;
import org.bson.Document;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class MongoDatabaseImpl implements Database {
    private String uri;
    private String dbName = "construccion1";
    private MongoClient mongoClient;
    private MongoDatabase database;

    public MongoDatabaseImpl(String uri) {
        this.uri = uri;
        init();
    }

    private void init() {
        try {
            mongoClient = MongoClients.create(uri);
            database = mongoClient.getDatabase(dbName);
            System.out.println("MongoDB client initialized for DB: " + dbName);
        } catch (Exception e) {
            System.err.println("Error initializing MongoDB client: " + e.getMessage());
        }
    }

    @Override
    public Connection connect() {
        // This project expects a java.sql.Connection for SQL DBs.
        // For MongoDB we don't return a SQL Connection. Ensure client initialized and return null.
        if (database == null) {
            init();
        }
        return null;
    }

    public void ensureCollectionAndInsert(String jokeEn, String jokeEs) {
        try {
            MongoCollection<Document> collection = database.getCollection("ChistesBasto");
            if (collection == null) {
                database.createCollection("ChistesBasto");
                collection = database.getCollection("ChistesBasto");
            }
            long count = collection.countDocuments();
            Document doc = new Document("AUTOID", count + 1)
                    .append("chiste_en_ingles", jokeEn)
                    .append("chiste_en_espanol", jokeEs);
            collection.insertOne(doc);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Document> getChistes() {
        List<Document> res = new ArrayList<>();
        try {
            MongoCollection<Document> collection = database.getCollection("ChistesBasto");
            if (collection != null) {
                collection.find().into(res);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public void closeClient() {
        try {
            if (mongoClient != null) {
                mongoClient.close();
            }
        } catch (Exception e) {
            // ignore
        }
    }
}
