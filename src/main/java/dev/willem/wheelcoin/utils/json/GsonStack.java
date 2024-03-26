package dev.willem.wheelcoin.utils.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bson.Document;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public final class GsonStack {

    @Getter
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .setObjectToNumberStrategy(JsonReader::nextInt)
            .registerTypeHierarchyAdapter(ConfigurationSerializable.class, new ConfigurationSerializableAdapter())
            .registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter())
            .create();

    public Document toBson(ItemStack itemStack) {
        return Document.parse(gson.toJson(itemStack));
    }

    public ItemStack fromBson(Document document) {
        return gson.fromJson(document.toJson(), ItemStack.class);
    }

}

