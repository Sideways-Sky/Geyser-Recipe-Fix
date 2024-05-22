package net.sideways_sky.geyserrecipefix.inventories.anvil;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.Slot;
import net.sideways_sky.geyserrecipefix.Events;
import net.sideways_sky.geyserrecipefix.Geyser_Recipe_Fix;
import net.sideways_sky.geyserrecipefix.config.WorkstationMode;
import net.sideways_sky.geyserrecipefix.inventories.SimInventoryView;
import net.sideways_sky.geyserrecipefix.inventories.WorkstationGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Anvil extends WorkstationGUI {
    public static WorkstationMode mode;

    private final AnvilInventory back;
    private final AnvilMenu backMenu;
    public Anvil(AnvilInventory backInv, InventoryView view){
        this.back = backInv;
        backMenu = (AnvilMenu) ((CraftInventoryView) view).getHandle();
        backMenu.setItemName("");
        ((CraftInventory) backInv).getInventory().onOpen((CraftHumanEntity) view.getPlayer());

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

    @Override
    public void onViewClick(InventoryClickEvent e) {
        SimInventoryView simView = new SimInventoryView(e.getWhoClicked(), back, backMenu);
        if(e.getClickedInventory().getHolder() != this){
            e.setCancelled(!new InventoryClickEvent(simView, e.getSlotType(), e.getRawSlot() - inventory.getSize() + back.getSize(), e.getClick(), e.getAction()).callEvent());
            return;
        }
        AnvilSlot clickedSlot = AnvilSlot.getSlot(e.getSlot());
        if(clickedSlot == null){
            e.setCancelled(true);
            return;
        }
        if(clickedSlot.OG_i != -1){
            if(!new InventoryClickEvent(simView, simView.getSlotType(clickedSlot.OG_i), clickedSlot.OG_i, e.getClick(), e.getAction()).callEvent()){
                e.setCancelled(true);
                return;
            }
        }

        switch (clickedSlot){
            case FORWARD -> {
                e.setCancelled(true);
                inventory.close();
                Events.ignoreEvents = true;
                InventoryView view = e.getWhoClicked().openAnvil(back.getLocation(), true);
                Events.ignoreEvents = false;
                if(view == null){
                    Geyser_Recipe_Fix.instance.getLogger().severe("Unable to open default Anvil inventory");
                }
                return;
            }
            case COST -> {
                e.setCancelled(true);
                return;
            }
            case RESULT -> {
                if(e.getCurrentItem() == null){
                    e.setCancelled(true);
                    return;
                }
                ServerPlayer player = ((CraftPlayer) e.getWhoClicked()).getHandle();
                Slot resultSlot = backMenu.getSlot(backMenu.getResultSlot());
                if(!resultSlot.mayPickup(player)){
                    e.setCancelled(true);
                    return;
                }
                net.minecraft.world.item.ItemStack item = net.minecraft.world.item.ItemStack.fromBukkitCopy(inventory.getItem(AnvilSlot.RESULT.i));
                resultSlot.onTake(player, item);
                Bukkit.getScheduler().runTaskLater(Geyser_Recipe_Fix.instance, () -> {
                    updateFront(e.getWhoClicked());
                }, 1);
            }
            case FIRST -> {
                Bukkit.getScheduler().runTaskLater(Geyser_Recipe_Fix.instance, () -> {
                    ItemStack item = inventory.getItem(AnvilSlot.FIRST.i);
                    if(item != null && !item.isEmpty()){
                        net.minecraft.world.item.ItemStack NMS_Item = net.minecraft.world.item.ItemStack.fromBukkitCopy(item);
                        net.minecraft.network.chat.Component name = NMS_Item.get(DataComponents.CUSTOM_NAME);
                        if(name != null){
                            backMenu.setItemName(name.getString());
                        }
                    }
                    back.setFirstItem(item == null? null : item.clone());
                    updateFront(simView, e.getWhoClicked());
                }, 1);
            }
            case SECOND -> Bukkit.getScheduler().runTaskLater(Geyser_Recipe_Fix.instance, () -> {
                ItemStack item = inventory.getItem(AnvilSlot.SECOND.i);
                back.setSecondItem(item == null? null : item.clone());
                updateFront(simView, e.getWhoClicked());
            }, 1);
        }
    }

    private void updateFront(HumanEntity human){
        inventory.setItem(AnvilSlot.RESULT.i, back.getResult());
        inventory.setItem(AnvilSlot.FIRST.i, back.getFirstItem());
        inventory.setItem(AnvilSlot.SECOND.i, back.getSecondItem());
        inventory.setItem(AnvilSlot.COST.i, costIndicator(back.getRepairCost(), canTakeResult(human)));
    }
    private void updateFront(SimInventoryView simView, HumanEntity human){
        new PrepareAnvilEvent(simView, back.getResult()).callEvent();
        updateFront(human);
    }

    private boolean canTakeResult(HumanEntity human){
        ServerPlayer player = ((CraftPlayer) human).getHandle();
        Slot resultSlot = backMenu.getSlot(backMenu.getResultSlot());
        return  resultSlot.mayPickup(player);
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