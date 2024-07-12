package net.sideways_sky.geyserrecipefix.inventories;

import net.minecraft.world.inventory.AbstractContainerMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import java.util.ArrayList;
import java.util.List;

public abstract class SimInventory {
    protected static final int frontSize = 63;
    protected static final int playerInvSize = 36;
    protected static final int frontTopSize = 27; // frontSize - playerInvSize
    protected static final ItemStack filler;
    static {
        filler = new ItemStack(Material.STRUCTURE_VOID);
        ItemMeta meta = filler.getItemMeta();
        meta.setCustomModelData(593721);
        filler.setItemMeta(meta);
    }

    final int backSize;
    public AbstractContainerMenu menu;

    protected SimInventory(int backSize) {
        this.backSize = backSize;
    }

    protected abstract List<ItemStack> getFront(List<ItemStack> items);

    public List<ItemStack> fromBackToFront(List<ItemStack> items ) {
        if(items.size() != backSize){
            Bukkit.getLogger().warning("Back size: " + items.size() + " (Expected: " + backSize + ")");
        }
        List<ItemStack> res = new ArrayList<>();
        res.addAll(getFront(items.subList(0, backSize - playerInvSize)));
        res.addAll(items.subList(backSize - playerInvSize, backSize));
        if(res.size() != frontSize){
            Bukkit.getLogger().warning("Front size: " + res.size() + " (Expected: " + frontSize + ")");
        }
        return res;
    }

    public abstract int getBackIdxFromFrontIdx(int frontClick);
    public abstract int getFrontIdxFromBackIdx(int backClick);

}
