package net.sideways_sky.geyserrecipefix.inventories;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public abstract class WorkstationGUI implements InventoryHolder {
    protected Inventory inventory;
    public void open(HumanEntity player){
        player.openInventory(getInventory());
    }
    public abstract void onViewClick(InventoryClickEvent e);
    public abstract void onClose(InventoryCloseEvent e);
    protected final static ItemStack filler;
    static {
        filler = new ItemStack(Material.STRUCTURE_VOID);
        ItemMeta meta = filler.getItemMeta();
        meta.setCustomModelData(593721);
        filler.setItemMeta(meta);
    }
    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}