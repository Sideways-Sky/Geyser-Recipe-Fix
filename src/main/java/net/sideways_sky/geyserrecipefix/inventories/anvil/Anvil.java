package net.sideways_sky.geyserrecipefix.inventories.anvil;

import net.kyori.adventure.text.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.Slot;
import net.sideways_sky.geyserrecipefix.Geyser_Recipe_Fix;
import net.sideways_sky.geyserrecipefix.inventories.WorkstationGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftInventoryAnvil;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
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
                return item;
            }
        }
        return null;
    }
    private final AnvilInventory backInv;
    private AnvilMenu backInvContainer;
    private boolean isOpeningBack = false;
    private Anvil(AnvilInventory backInv){
        this.backInv = backInv;

        if(backInv.getRenameText() == null){
            CraftInventoryAnvil craftAnvil = (CraftInventoryAnvil) backInv;
            try {
                Field field = craftAnvil.getClass().getDeclaredField("container");
                field.setAccessible(true);
                backInvContainer = (AnvilMenu) field.get(craftAnvil);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                Bukkit.getLogger().warning("AnvilMenu injection Failed!");
                throw new RuntimeException(e);
            }
        }

        backInvContainer.setItemName("");

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
            inv.setFirstItem(inventory.getItem(AnvilSlot.FIRST.i));
            inv.setSecondItem(inventory.getItem(AnvilSlot.SECOND.i));
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
        } else
        if(clickedSlot == AnvilSlot.FORWARD){
            e.setCancelled(true);
            openBack(e.getWhoClicked());
            return;
        } else
        if(clickedSlot == AnvilSlot.RESULT){
            if(e.getCurrentItem() == null){
                e.setCancelled(true);
                return;
            }
            ServerPlayer player = ((CraftPlayer) e.getWhoClicked()).getHandle();
            Slot resultSlot = backInvContainer.getSlot(backInvContainer.getResultSlot());
            if(!resultSlot.mayPickup(player)){
                e.setCancelled(true);
                return;
            }
            net.minecraft.world.item.ItemStack item = net.minecraft.world.item.ItemStack.fromBukkitCopy(inventory.getItem(AnvilSlot.RESULT.i));
            resultSlot.onTake(player, item);
            // Result is taken by player
            inventory.setItem(AnvilSlot.FIRST.i, backInv.getFirstItem());
            inventory.setItem(AnvilSlot.SECOND.i, backInv.getSecondItem());
        }

        Bukkit.getScheduler().runTaskLater(Geyser_Recipe_Fix.instance, this::updateWithBack, 1);
    }
    private void updateWithBack(){
        backInv.setResult(inventory.getItem(AnvilSlot.RESULT.i));
        backInv.setFirstItem(inventory.getItem(AnvilSlot.FIRST.i));
        backInv.setSecondItem(inventory.getItem(AnvilSlot.SECOND.i));

        inventory.setItem(AnvilSlot.RESULT.i, backInv.getResult());
        inventory.setItem(AnvilSlot.FIRST.i, backInv.getFirstItem());
        inventory.setItem(AnvilSlot.SECOND.i, backInv.getSecondItem());
    }
    @Override
    public void onClose(InventoryCloseEvent e) {
        if(isOpeningBack){return;}
        for(AnvilSlot slot : AnvilSlot.values()){
            if(!slot.open){continue;}
            ItemStack item = inventory.getItem(slot.i);
            if(item == null){ continue; }
            e.getPlayer().getInventory().addItem(item);
        }
    }
}