package dev.willem.wheelcoin.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import dev.willem.wheelcoin.Wheelcoin;
import lombok.Getter;

@Getter
public final class DatabaseClient {

    private MongoClient mongoClient;
    private MongoDatabase database;
    private boolean initialized = false;

    public DatabaseClient(String connectionString, String databaseName) {
        if (connectionString.isEmpty()) {
            Wheelcoin.getInstance().getLogger().severe("The connectionString is null, the plugin can not work.");
            return;
        }

        mongoClient = MongoClients.create(connectionString);

        database = mongoClient.getDatabase(databaseName);
        Wheelcoin.getInstance().getLogger().info("Connected to database: " + databaseName);

        initialized = true;
    }

}