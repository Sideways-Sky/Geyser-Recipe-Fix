package net.sideways_sky.geyserrecipefix.inventories.smithing;

import net.kyori.adventure.text.Component;
import net.minecraft.world.inventory.SmithingMenu;
import net.sideways_sky.geyserrecipefix.Geyser_Recipe_Fix;
import net.sideways_sky.geyserrecipefix.config.WorkstationMode;
import net.sideways_sky.geyserrecipefix.inventories.SimInventoryView;
import net.sideways_sky.geyserrecipefix.inventories.WorkstationGUI;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.*;

import java.util.*;

public class Smithing extends WorkstationGUI {
    public static WorkstationMode mode;

    private final SmithingInventory back;
    private final SmithingMenu backMenu;
    public Smithing (SmithingInventory backInv, InventoryView view) {
        this.back = backInv;
        backMenu = (SmithingMenu) ((CraftInventoryView) view).getHandle();
        ((CraftInventory) backInv).getInventory().onOpen((CraftHumanEntity) view.getPlayer());

        inventory = Bukkit.createInventory(this, 27, Component.translatable("block.minecraft.smithing_table"));
        for (int i = 0; i < inventory.getSize(); i++) {
            if(SmithingSlot.getSlot(i) != null){
                continue;
            }
            inventory.setItem(i, filler);
        }
    }

    @Override
    public void onViewClick(InventoryClickEvent e) {
        SimInventoryView simView = new SimInventoryView(e.getWhoClicked(), back, backMenu);
        if(e.getClickedInventory().getHolder() != this){
            e.setCancelled(!new InventoryClickEvent(simView, e.getSlotType(), e.getRawSlot() - inventory.getSize() + back.getSize(), e.getClick(), e.getAction()).callEvent());
            return;
        }
        SmithingSlot clickedSlot = SmithingSlot.getSlot(e.getSlot());
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

        if (clickedSlot == SmithingSlot.RESULT) {
            if (e.getCurrentItem() == null) {
                e.setCancelled(true);
                return;
            }
            for (SmithingSlot slot : SmithingSlot.values()) {
                //                Very Hacky way of dealing with taking result, need to hook deeper
                if (slot == SmithingSlot.RESULT) {
                    continue;
                }
                ItemStack item = inventory.getItem(slot.i);
                if (item == null) {
                    continue;
                }
                item.add(-1);
            }
        } else {
            ItemStack item = e.getCursor();
//            if (!item.isEmpty()) {
//                SmithingSlot goesTo = findSlotForItem(item); //Hook deeper. You built in slot finding
//                if (goesTo == null) {
//                    e.setCancelled(true);
//                    return;
//                } else if (goesTo != clickedSlot) {
//                    e.setCancelled(true);
////                        Bukkit.getScheduler().runTaskLater(Geyser_Recipe_Fix.instance, () -> {
////                            inventory.setItem(goesTo.i, item);
////                            updateFront(goesTo);
////                        }, 1);
//                    return;
//                }
//            }
            Bukkit.getScheduler().runTaskLater(Geyser_Recipe_Fix.instance, () -> updateFront(clickedSlot), 1);

        }
    }
    private void updateFront(SmithingSlot clicked){
        ItemStack clickedItem = Objects.requireNonNullElse(inventory.getItem(clicked.i), ItemStack.empty()).clone();
        switch (clicked){
            case TEMPLATE -> back.setInputTemplate(clickedItem);
            case BASE -> back.setInputEquipment(clickedItem);
            case ADDITION -> back.setInputMineral(clickedItem);
        }

        inventory.setItem(SmithingSlot.TEMPLATE.i, back.getInputTemplate());
        inventory.setItem(SmithingSlot.BASE.i, back.getInputEquipment());
        inventory.setItem(SmithingSlot.ADDITION.i, back.getInputMineral());
        inventory.setItem(SmithingSlot.RESULT.i, back.getResult());
    }
    @Override
    public void onClose(InventoryCloseEvent e){
        for(SmithingSlot slot : List.of(SmithingSlot.BASE, SmithingSlot.TEMPLATE, SmithingSlot.ADDITION)){
            ItemStack item = inventory.getItem(slot.i);
            if(item == null){ continue; }
            e.getPlayer().getInventory().addItem(item);
        }
    }

}
