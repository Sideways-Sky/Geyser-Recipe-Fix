package net.sideways_sky.geyserrecipefix.inventories;

import com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.bukkit.Material;

import org.bukkit.inventory.meta.ItemMeta;


import java.util.ArrayList;
import java.util.List;

import static net.sideways_sky.geyserrecipefix.Geyser_Recipe_Fix.logger;

public abstract class SimInventory {
    protected static final int frontSize = 63;
    protected static final int playerInvSize = 36;
    protected static final int frontTopSize = 27; // frontSize - playerInvSize
    protected static final ItemStack filler = new ItemStack.Builder().type(ItemTypes.STRUCTURE_VOID).component(ComponentTypes.CUSTOM_MODEL_DATA, 593721).build();
    protected static final org.bukkit.inventory.ItemStack bukkitFiller;
    static {
        bukkitFiller = new org.bukkit.inventory.ItemStack(Material.STRUCTURE_VOID);
        ItemMeta meta = bukkitFiller.getItemMeta();
        meta.setCustomModelData(593721);
        bukkitFiller.setItemMeta(meta);
    }

    final int backSize;
    public AbstractContainerMenu menu;

    protected SimInventory(int backSize) {
        this.backSize = backSize;
    }

    protected abstract List<ItemStack> getFront(List<ItemStack> items);

    public List<ItemStack> fromBackToFront(List<ItemStack> items ) {
        if(items.size() != backSize){
            logger.warning("Back size: " + items.size() + " (Expected: " + backSize + ")");
        }
        List<ItemStack> res = new ArrayList<>();
        res.addAll(getFront(items.subList(0, backSize - playerInvSize)));
        res.addAll(items.subList(backSize - playerInvSize, backSize));
        if(res.size() != frontSize){
            logger.warning("Front size: " + res.size() + " (Expected: " + frontSize + ")");
        }
        return res;
    }

    public abstract int getBackIdxFromFrontIdx(int frontClick);
    public abstract int getFrontIdxFromBackIdx(int backClick);

}
