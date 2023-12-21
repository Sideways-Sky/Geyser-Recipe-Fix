package net.sideways_sky.geyserrecipefix.inventories;
import net.kyori.adventure.text.Component;
import net.sideways_sky.geyserrecipefix.inventories.Smithing.Smithing;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class WorkstationGUI implements InventoryHolder {
    protected Inventory inventory;
    public static void open(HumanEntity player){
        player.openInventory(new Smithing().getInventory());
    }
    public abstract void onViewClick(InventoryClickEvent e);

    public abstract void onClose(InventoryCloseEvent e);
    protected static final ItemStack Filler = new ItemStack(Material.STRUCTURE_VOID);
    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
