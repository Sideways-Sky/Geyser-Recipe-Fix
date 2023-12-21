package net.sideways_sky.geyserrecipefix;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

public class utils {
    public static void consoleSend(String message) {
        Bukkit.getConsoleSender().sendMessage("[Geyser Recipe Fix] " + message);
    }
    public static Inventory cloneInv(Inventory inventory){
        return cloneInv(inventory, false);
    }
    public static Inventory cloneInv(Inventory inventory, boolean useType){
        Inventory inv = useType ? Bukkit.createInventory(inventory.getHolder(), inventory.getType()) : Bukkit.createInventory(inventory.getHolder(), inventory.getSize());
        inv.setContents(inventory.getContents().clone());
        return inv;
    }
    public static ItemMeta mergeMeta(ItemMeta base, ItemMeta addon){
        return base;
    }
}
