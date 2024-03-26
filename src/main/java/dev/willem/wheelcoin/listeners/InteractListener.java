package dev.willem.wheelcoin.listeners;

import dev.willem.wheelcoin.guis.CoinGUI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public final class InteractListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!event.getHand().equals(EquipmentSlot.HAND)) return;
        if (event.getItem() == null || event.getItem().getType().equals(Material.AIR)) return;
        if (!event.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey("wheelcoin", "item"))) return;

        new CoinGUI(event.getPlayer());
        event.getItem().setAmount(event.getItem().getAmount()-1);
    }
}
