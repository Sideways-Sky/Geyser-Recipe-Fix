package net.sideways_sky.geyserrecipefix.inventories;

import net.minecraft.world.inventory.AbstractContainerMenu;

import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;


public class SimInventoryView extends CraftInventoryView {
    public SimInventoryView(HumanEntity player, Inventory viewing, AbstractContainerMenu container) {
        super(player, viewing, container);
    }
}
