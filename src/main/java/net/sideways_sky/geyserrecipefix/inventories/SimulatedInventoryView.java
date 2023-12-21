package net.sideways_sky.geyserrecipefix.inventories;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

public class SimulatedInventoryView extends InventoryView {
    private final Inventory top;
    private final HumanEntity player;
    private String title;

    public SimulatedInventoryView(Inventory top, HumanEntity player, String title) {
        this.top = top;
        this.player = player;
        this.title = title;
    }

    @Override
    public @NotNull Inventory getTopInventory() {
        return top;
    }

    @Override
    public @NotNull Inventory getBottomInventory() {
        return player.getInventory();
    }

    @Override
    public @NotNull HumanEntity getPlayer() {
        return player;
    }

    @Override
    public @NotNull InventoryType getType() {
        return top.getType();
    }

    @Override
    public @NotNull String getTitle() {
        return title;
    }

    @Override
    public @NotNull String getOriginalTitle() {
        return title;
    }

    @Override
    public void setTitle(@NotNull String title) {
        this.title = title;
    }
}
