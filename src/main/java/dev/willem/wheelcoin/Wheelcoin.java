package dev.willem.wheelcoin;

import co.aikar.commands.PaperCommandManager;
import dev.willem.wheelcoin.commands.WheelcoinCommand;
import dev.willem.wheelcoin.database.DatabaseClient;
import dev.willem.wheelcoin.database.DatabaseManager;
import dev.willem.wheelcoin.listeners.InteractListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Wheelcoin extends JavaPlugin {

    @Getter
    private static Wheelcoin instance;

    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        // could be better ig
        DatabaseClient databaseClient = new DatabaseClient(
                getConfig().getString("connectionString", ""),
                getConfig().getString("databaseName", "database"));
        if (!databaseClient.isInitialized()) {
            getLogger().severe("Database Client is not initialized, check your config.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        databaseManager = new DatabaseManager(databaseClient);

        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new WheelcoinCommand());

        Bukkit.getPluginManager().registerEvents(new InteractListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
