package net.sideways_sky.geyserrecipefix.inventories;
import net.kyori.adventure.text.Component;
import net.sideways_sky.geyserrecipefix.inventories.Smithing.Smithing;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public abstract class WorkstationGUI implements InventoryHolder {
    protected final Inventory inv;
    public static void open(HumanEntity player){
        player.openInventory(new Smithing().getInventory());
    }
    protected WorkstationGUI(int size, String name) {
        this.inv = Bukkit.createInventory(this, size, Component.text(name));
    }
    public abstract void onViewClick(InventoryClickEvent e);
    public abstract void onClose(InventoryCloseEvent e);
    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }
}
