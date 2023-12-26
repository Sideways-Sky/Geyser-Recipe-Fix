package net.sideways_sky.geyserrecipefix.inventories.anvil;

import net.kyori.adventure.text.Component;
import net.sideways_sky.geyserrecipefix.inventories.SimulatedInventoryView;
import net.sideways_sky.geyserrecipefix.inventories.WorkstationGUI;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static net.sideways_sky.geyserrecipefix.utils.cloneInv;

public class Anvil extends WorkstationGUI {
    private final AnvilInventory backInv;
    public Anvil(AnvilInventory backInv){
        this.backInv = backInv;
        inventory = Bukkit.createInventory(this, 27, Component.text("Anvil"));
        for (int i = 0; i < 11; i++) {
            inventory.setItem(i, Filler);
        }
        for (int i = 0; i < 11; i++) {
            inventory.setItem(i, Filler);
        }

        // 11 - base
        inventory.setItem(12, Filler);
        // 13 - addition
        inventory.setItem(14, Filler);
        // 15 - result

        for (int i = 16; i < 27; i++) {
            inventory.setItem(i, Filler);
        }
    }
    @Override
    public void onViewClick(InventoryClickEvent e) {
        if(e.getClickedInventory().getHolder() != this){
            return;
        }
        Inventory newInv = cloneInv(inventory, true);
        newInv.setItem(e.getSlot(), e.getCursor());

        backInv.setFirstItem(newInv.getItem(AnvilSlot.FIRST.i));
        backInv.setSecondItem(newInv.getItem(AnvilSlot.SECOND.i));

        SimulatedInventoryView simView = new SimulatedInventoryView(backInv, e.getWhoClicked(), "Anvil");
        PrepareAnvilEvent event = new PrepareAnvilEvent(simView, backInv.getResult());
        event.callEvent();

        inventory.setItem(AnvilSlot.RESULT.i, event.getResult());
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        backInv.close();
        for(AnvilSlot slot : AnvilSlot.values()){
            ItemStack item = inventory.getItem(slot.i);
            if(item == null){ continue; }
            e.getPlayer().getInventory().addItem(item);
        }
    }
}
