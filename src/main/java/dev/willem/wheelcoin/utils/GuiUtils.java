package dev.willem.wheelcoin.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@UtilityClass
public final class GuiUtils {
    // TriumphGUI adds their own signature to a ItemStack in their GUIs, this means that every item becomes obsolete and unusable.
    // This just removes that, I would've used my own implementation of a GUI Library but I was too deep into the project.
    public ItemStack clearItem(ItemStack itemStack) {
        ItemStack newStack = itemStack.clone();
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().remove(new NamespacedKey("wheelcoin", "mf-gui"));
        newStack.setItemMeta(itemMeta);

        return newStack;
    }
}
