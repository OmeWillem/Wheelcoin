package dev.willem.wheelcoin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import dev.willem.wheelcoin.Wheelcoin;
import dev.willem.wheelcoin.guis.InsertGUI;
import dev.willem.wheelcoin.utils.ChatUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

@CommandAlias("wheelcoin")
public final class WheelcoinCommand extends BaseCommand {
    @Subcommand("insert")
    @Description("Opens the insert-gui")
    @CommandPermission("wheelcoin.insert")
    public void insert(Player player) {
        new InsertGUI(player);
        player.sendMessage(ChatUtils.parse("<gray>Je hebt het insert-gui geopend."));
    }

    @Subcommand("fetch")
    @Description("Reloads database / fetches new information")
    @CommandPermission("wheelcoin.fetch")
    public void fetch(Player player){
        Wheelcoin.getInstance().getDatabaseManager().fetchStacks();
        player.sendMessage(ChatUtils.parse("<gray>Je hebt de database herladen."));
    }

    @Subcommand("convert")
    @Description("Converts item in hand into a useable wheelcoin")
    @CommandPermission("wheelcoin.convert")
    public void convert(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType().equals(Material.AIR)) {
            player.sendMessage(ChatUtils.parse("<gray>Dit is geen valid item."));
            return;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(
                new NamespacedKey("wheelcoin", "item"),
                PersistentDataType.BOOLEAN,
                true);
        itemStack.setItemMeta(itemMeta);

        // de neef moest van Jazz
        player.sendMessage(ChatUtils.parse("<gray>Je item is omgezet naar wheelcoin-item neef!"));
    }
}
