package dev.willem.wheelcoin.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dev.willem.wheelcoin.utils.json.GsonStack;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class DatabaseManager {

    private final DatabaseClient databaseClient;
    private List<ItemStack> cachedStacks;

    public DatabaseManager(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;

        cachedStacks = fetchStacks();
    }

    public void insertStack(ItemStack... stacks) {
        List<Document> documents = new ArrayList<>();
        for (ItemStack stack : stacks) {
            documents.add(GsonStack.toBson(stack));
            cachedStacks.add(stack);
        }

        MongoDatabase database = databaseClient.getDatabase();
        MongoCollection<Document> collection = database.getCollection("items");

        collection.insertMany(documents);
    }

    public void deleteStack(ItemStack... stacks) {
        MongoDatabase database = databaseClient.getDatabase();
        MongoCollection<Document> collection = database.getCollection("items");

        for (ItemStack stack : stacks) {
            collection.findOneAndDelete(GsonStack.toBson(stack));
            cachedStacks.remove(stack);
        }
    }

    public List<ItemStack> fetchStacks() {
        List<ItemStack> stacks = new ArrayList<>();
        MongoDatabase database = databaseClient.getDatabase();
        MongoCollection<Document> collection = database.getCollection("items");

        // blame mongo for this...
        collection.find().map(GsonStack::fromBson).forEach(stacks::add);

        cachedStacks = stacks;
        return stacks;
    }

}
