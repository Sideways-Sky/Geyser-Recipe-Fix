package net.sideways_sky.geyserrecipefix.inventories.anvil;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.Slot;
import net.sideways_sky.geyserrecipefix.Geyser_Recipe_Fix;
import net.sideways_sky.geyserrecipefix.inventories.WorkstationGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

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
            try {
                backInvContainer = Geyser_Recipe_Fix.nms.getAnvilContainer(backInv);
            } catch (IllegalAccessException e) {
                Bukkit.getLogger().warning("AnvilMenu injection Failed!");
                throw new RuntimeException(e);
            }
        }

        backInvContainer.setItemName("");

        inventory = Bukkit.createInventory(this, 27, Component.translatable("block.minecraft.anvil"));
        for (int i = 0; i < inventory.getSize(); i++) {
            if(AnvilSlot.getSlot(i) != null){
                continue;
            }
            inventory.setItem(i, filler);
        }
        inventory.setItem(AnvilSlot.COST.i, filler);
        inventory.setItem(AnvilSlot.FORWARD.i, new ItemStack(Material.ANVIL));
    }
    private boolean isMyBack(AnvilInventory inv){
        return backInv.equals(inv) || isOpeningBack;
    }
    private void openBack(HumanEntity human){
        isOpeningBack = true;
        InventoryView view = human.openAnvil(backInv.getLocation(), true);
        isOpeningBack = false;
        if(view == null){
            Bukkit.getLogger().severe("Unable to open default Anvil inventory");
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

        switch (clickedSlot){
            case FORWARD -> {
                e.setCancelled(true);
                openBack(e.getWhoClicked());
            }
            case COST -> {
                e.setCancelled(true);
            }
            case RESULT -> {
                if(e.getCurrentItem() == null){
                    e.setCancelled(true);
                    return;
                }
                try {
                    ServerPlayer player = Geyser_Recipe_Fix.nms.getServerPlayer((Player) e.getWhoClicked());
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

                } catch (InvocationTargetException | IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
            }
            case FIRST -> {
                if(!e.getCursor().isEmpty()){
                    Bukkit.getScheduler().runTaskLater(Geyser_Recipe_Fix.instance, () -> {
                        net.minecraft.world.item.ItemStack item = net.minecraft.world.item.ItemStack.fromBukkitCopy(inventory.getItem(AnvilSlot.FIRST.i));
                        if(item.hasCustomHoverName()){
                            backInvContainer.setItemName(item.getHoverName().getString());
                        }
                        updateWithBack(e.getWhoClicked());
                    }, 1);
                } else {
                    Bukkit.getScheduler().runTaskLater(Geyser_Recipe_Fix.instance, () -> updateWithBack(e.getWhoClicked()), 1);
                }
            }
            default -> Bukkit.getScheduler().runTaskLater(Geyser_Recipe_Fix.instance, () -> updateWithBack(e.getWhoClicked()), 1);
        }
    }

    private boolean canTake(HumanEntity human){
        try {
            ServerPlayer player = Geyser_Recipe_Fix.nms.getServerPlayer((Player) human);
            Slot resultSlot = backInvContainer.getSlot(backInvContainer.getResultSlot());
            return  resultSlot.mayPickup(player);
        } catch (InvocationTargetException | IllegalAccessException e) {
            return false;
        }
    }
    private void updateWithBack(HumanEntity human){
        backInv.setFirstItem(inventory.getItem(AnvilSlot.FIRST.i));
        backInv.setSecondItem(inventory.getItem(AnvilSlot.SECOND.i));

        inventory.setItem(AnvilSlot.RESULT.i, backInv.getResult());
        inventory.setItem(AnvilSlot.FIRST.i, backInv.getFirstItem());
        inventory.setItem(AnvilSlot.SECOND.i, backInv.getSecondItem());
        inventory.setItem(AnvilSlot.COST.i, costIndicator(backInv.getRepairCost(), canTake(human)));
    }
    @Override
    public void onClose(InventoryCloseEvent e) {
        for(AnvilSlot slot : List.of(AnvilSlot.FIRST, AnvilSlot.SECOND)){
            ItemStack item = inventory.getItem(slot.i);
            if(item == null){ continue;}
            e.getPlayer().getInventory().addItem(item);
        }
    }
    private ItemStack costIndicator(int repairCost, boolean canTake){
        if(repairCost <= 0){
            return filler;
        }

        ItemStack indicator = new ItemStack(canTake ? Material.LIME_STAINED_GLASS : Material.RED_STAINED_GLASS);
        ItemMeta indicatorMeta = indicator.getItemMeta();
        indicatorMeta.displayName(Component.translatable("container.repair.cost", "Enchantment Cost: %1$s", Component.text(repairCost)).style(Style.style(canTake ? NamedTextColor.GREEN : NamedTextColor.RED)));
        indicator.setItemMeta(indicatorMeta);
        return indicator;
    }
}