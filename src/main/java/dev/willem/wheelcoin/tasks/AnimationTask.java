package dev.willem.wheelcoin.tasks;

import dev.willem.wheelcoin.Wheelcoin;
import dev.willem.wheelcoin.guis.CoinGUI;
import dev.willem.wheelcoin.utils.GuiUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public final class AnimationTask extends BukkitRunnable {

    private final CoinGUI gui;
    private final Consumer<Boolean> returnConsumer;

    private int iterations = 0;
    private int standbyIterations = 0;
    private boolean startFinding = false;
    private boolean found = false;

    public AnimationTask(CoinGUI gui, Consumer<Boolean> returnConsumer) {
        this.gui = gui;
        this.returnConsumer = returnConsumer;
    }

    @Override
    public void run() {
        if (gui.isGaveItem()) {
            returnConsumer.accept(false);
            return;
        }

        if (iterations >= 20) {
            startFinding = true;
        }

        if (standbyIterations >= 20) {
            returnConsumer.accept(true);
            this.cancel();
            return;
        }

        if (startFinding) {
            if (found) {
                standbyIterations++;
                return;
            } else {
                ItemStack finalItem = GuiUtils.clearItem(gui.getGui().getGuiItem(2).getItemStack());
                if (finalItem.isSimilar(gui.getWonStack())) {
                    found = true;
                    return;
                }
            }
        }

        gui.shiftPool();
        iterations++;
    }
}
