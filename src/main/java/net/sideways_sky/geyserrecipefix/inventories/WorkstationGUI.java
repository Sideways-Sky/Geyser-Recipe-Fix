package net.sideways_sky.geyserrecipefix.inventories;
import net.kyori.adventure.text.Component;
import net.sideways_sky.geyserrecipefix.inventories.Smithing.Smithing;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public abstract class WorkstationGUI implements InventoryHolder {
    protected Inventory inventory;
    public static void open(HumanEntity player){
        player.openInventory(new Smithing().getInventory());
    }
    protected WorkstationGUI(int size, String name) {
        this.inventory = Bukkit.createInventory(this, size, Component.text(name));
    }
    protected WorkstationGUI(InventoryType type, String name) {
        this.inventory = Bukkit.createInventory(this, type, Component.text(name));
    }
    public abstract void onViewClick(InventoryClickEvent e);

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
