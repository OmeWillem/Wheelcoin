package dev.willem.wheelcoin.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import dev.willem.wheelcoin.Wheelcoin;
import dev.willem.wheelcoin.database.DatabaseManager;
import dev.willem.wheelcoin.utils.ChatUtils;
import dev.willem.wheelcoin.utils.GuiUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;


public final class InsertGUI {

    private final PaginatedGui gui;
    private final DatabaseManager databaseManager = Wheelcoin.getInstance().getDatabaseManager();

    private final List<ItemStack> removedStacks = new ArrayList<>();
    private final List<ItemStack> addedStacks = new ArrayList<>();

    public InsertGUI(Player player ) {
        this.gui = Gui.paginated()
                .title(ChatUtils.parse("<yellow>Stel hier de items in."))
                .rows(6)
                .disableAllInteractions()
                .create();

        init();
        gui.open(player);
    }

    private void init() {
        gui.getFiller().fillBottom(ItemBuilder.from(Material.ORANGE_STAINED_GLASS_PANE)
                        .name(ChatUtils.parse("<gray>Dit is een filler item."))
                        .asGuiItem());

        gui.setItem(6, 3, ItemBuilder.from(Material.ARROW)
                .name(ChatUtils.parse("<yellow>Vorige pagina"))
                .asGuiItem(event -> gui.previous()));

        gui.setItem(6, 7, ItemBuilder.from(Material.ARROW)
                .name(ChatUtils.parse("<yellow>Volgende pagina"))
                .asGuiItem(event -> gui.next()));

        gui.setCloseGuiAction(event -> applyChanges());

        gui.setPlayerInventoryAction(event -> {
            if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) return;

            addedStacks.add(GuiUtils.clearItem(event.getCurrentItem()));
            gui.addItem(new GuiItem(event.getCurrentItem()));
            gui.update();
        });

        gui.setDefaultTopClickAction(event -> {
            if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) return;

            removedStacks.add(GuiUtils.clearItem(event.getCurrentItem()));
            gui.removePageItem(event.getCurrentItem());
        });

        databaseManager.getCachedStacks().stream().map(GuiItem::new).forEach(gui::addItem);
    }

    private void applyChanges() {
        if (!addedStacks.isEmpty()) {
            databaseManager.insertStack(addedStacks.toArray(ItemStack[]::new));
            addedStacks.clear();
        }

        if (!removedStacks.isEmpty()) {
            databaseManager.deleteStack(removedStacks.toArray(ItemStack[]::new));
            removedStacks.clear();
        }
    }
}
