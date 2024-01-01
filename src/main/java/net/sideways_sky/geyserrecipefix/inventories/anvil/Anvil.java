package net.sideways_sky.geyserrecipefix.inventories.anvil;

import net.kyori.adventure.text.Component;
import net.sideways_sky.geyserrecipefix.Geyser_Recipe_Fix;
import net.sideways_sky.geyserrecipefix.inventories.WorkstationGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static net.sideways_sky.geyserrecipefix.utils.consoleSend;

public class Anvil extends WorkstationGUI {
    public static List<WeakReference<Anvil>> instances = new ArrayList<>();
    public static Anvil create(AnvilInventory backInv) {
        Anvil o = new Anvil(backInv);
        instances.add(new java.lang.ref.WeakReference<>(o));
        return o;
    }
    public static Anvil get(AnvilInventory backInv){
        for (WeakReference<Anvil> instance : instances) {
            Anvil item = instance.get();
            if (item != null && item.isMyBack(backInv)) {
                consoleSend("Got it - get");
                return item;
            }
        }
        return null;
    }
    private AnvilInventory backInv;
    private boolean isOpeningBack = false;
    private Anvil(AnvilInventory backInv){
        this.backInv = backInv;
        inventory = Bukkit.createInventory(this, 27, Component.text("Anvil"));
        for (int i = 0; i < inventory.getSize(); i++) {
            if(AnvilSlot.getSlot(i) != null){
                continue;
            }
            inventory.setItem(i, Filler);
        }
        inventory.setItem(AnvilSlot.FORWARD.i, new ItemStack(Material.ANVIL));
    }
    private boolean isMyBack(AnvilInventory inv){
        return backInv.equals(inv) || isOpeningBack;
    }
    private void openBack(HumanEntity player){
        isOpeningBack = true;
        InventoryView view = player.openAnvil(backInv.getLocation(), false);
        isOpeningBack = false;
        if(view == null){
            open(player);
            return;
        }
        if(view.getTopInventory() instanceof AnvilInventory inv){
            consoleSend("Update back!");
            inv.setFirstItem(inventory.getItem(AnvilSlot.FIRST.i));
            inv.setSecondItem(inventory.getItem(AnvilSlot.SECOND.i));
            backInv = inv;
        } else {
            consoleSend("Unknown Anvil inventory type: " + view.getTopInventory());
        }
    }
    @Override
    public void onViewClick(InventoryClickEvent e) {
        if(e.getClickedInventory().getHolder() != this){
            return;
        }
        AnvilSlot clickedSlot = AnvilSlot.getSlot(e.getSlot());
        if(clickedSlot == null){
            e.setCancelled(true);
            return;
        }
        if(clickedSlot == AnvilSlot.FORWARD){
            e.setCancelled(true);
            openBack(e.getWhoClicked());
            return;
        }
        if(clickedSlot == AnvilSlot.RESULT && e.getCurrentItem() == null){
            e.setCancelled(true);
            return;
        }

        Bukkit.getScheduler().runTaskLater(Geyser_Recipe_Fix.instance, this::updateWithBack, 1);
    }

    private void updateWithBack(){
        backInv.setFirstItem(inventory.getItem(AnvilSlot.FIRST.i));
        backInv.setSecondItem(inventory.getItem(AnvilSlot.SECOND.i));
        backInv.setResult(inventory.getItem(AnvilSlot.RESULT.i));

        inventory.setItem(AnvilSlot.RESULT.i, backInv.getResult());
        inventory.setItem(AnvilSlot.FIRST.i, backInv.getFirstItem());
        inventory.setItem(AnvilSlot.SECOND.i, backInv.getSecondItem());
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        if(isOpeningBack){return;}
        for(AnvilSlot slot : AnvilSlot.values()){
            if(slot == AnvilSlot.FORWARD){continue;}
            ItemStack item = inventory.getItem(slot.i);
            if(item == null){ continue; }
            e.getPlayer().getInventory().addItem(item);
        }
    }
}
