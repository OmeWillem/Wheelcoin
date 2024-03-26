package dev.willem.wheelcoin.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.willem.wheelcoin.Wheelcoin;
import dev.willem.wheelcoin.database.DatabaseManager;
import dev.willem.wheelcoin.tasks.AnimationTask;
import dev.willem.wheelcoin.utils.ChatUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.Random;

public final class CoinGUI {

    @Getter
    private final Gui gui;
    private final DatabaseManager databaseManager = Wheelcoin.getInstance().getDatabaseManager();
    private final Random rand = new Random();
    private final Player player;

    @Getter
    private boolean gaveItem = false;

    @Getter
    private ItemStack wonStack;
    @Getter
    private LinkedList<ItemStack> pricePool;

    public CoinGUI(Player player) {
        this.gui = Gui.gui()
                .title(ChatUtils.parse("<white>Wheelcoin"))
                .type(GuiType.DISPENSER)
                .disableAllInteractions()
                .create();
        this.player = player;
        init();

        gui.open(player);
        startRolling();
    }

    private void init() {
        pricePool = fetchPool();
        wonStack = fetchWinner();

        gui.setItem(4, ItemBuilder.from(Material.ARROW)
                .name(ChatUtils.parse("<yellow>Klik om te draaien!"))
                .asGuiItem());

        gui.setCloseGuiAction(event -> {
            if (gaveItem) return;

            gaveItem = true;
            player.getInventory().addItem(wonStack);
        });

        for (ItemStack stack : pricePool) {
            gui.addItem(new GuiItem(stack));
        }
    }

    private void startRolling() {
        new AnimationTask(this, (done) -> {
            if (done) {
                gaveItem = true;
                player.getInventory().addItem(wonStack);
                player.closeInventory();
            }
        }).runTaskTimer(Wheelcoin.getInstance(), 0, 2);
    }

    public void shiftPool() {
        ItemStack lastStack = pricePool.removeLast();
        pricePool.addFirst(lastStack);

        int[] array = {0,1,2,3,5,6,7,8};
        for (int i = 0; i<array.length; i++) {
            gui.setItem(array[i], new GuiItem(pricePool.get(i)));
        }

        gui.update();
    }

    private LinkedList<ItemStack> fetchPool() {
        LinkedList<ItemStack> pricePool = new LinkedList<>();
        for (int i = 0; i<8; i++) {
            if (databaseManager.getCachedStacks().isEmpty()) {
                pricePool.add(new ItemStack(Material.STONE));
                Wheelcoin.getInstance().getLogger().warning("There are no specified items in the database. Using placeholder item...");

                continue;
            }

            pricePool.add(databaseManager.getCachedStacks().get(rand.nextInt(databaseManager.getCachedStacks().size())));
        }

        return pricePool;
    }

    private ItemStack fetchWinner() {
        return pricePool.get(rand.nextInt(pricePool.size()));
    }

}
