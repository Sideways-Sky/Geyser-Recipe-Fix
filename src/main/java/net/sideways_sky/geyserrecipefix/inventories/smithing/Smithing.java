package net.sideways_sky.geyserrecipefix.inventories.smithing;

import net.kyori.adventure.text.Component;
import net.sideways_sky.geyserrecipefix.Geyser_Recipe_Fix;
import net.sideways_sky.geyserrecipefix.inventories.WorkstationGUI;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Smithing extends WorkstationGUI {
    public static List<SmithingRecipe> recipes = new ArrayList<>();
    private final SmithingInventory backInv;
    public Smithing (SmithingInventory backInv) {
        this.backInv = backInv;
        inventory = Bukkit.createInventory(this, 27, Component.translatable("block.minecraft.smithing_table"));
        for (int i = 0; i < inventory.getSize(); i++) {
            if(SmithingSlot.getSlot(i) != null){
                continue;
            }
            inventory.setItem(i, filler);
        }
    }
    private static Set<SmithingSlot> findSlotsForItem(ItemStack item) {
        Set<SmithingSlot> found = new HashSet<>();
        for(SmithingRecipe r : recipes){
            if(r instanceof SmithingTransformRecipe recipe){
                if(recipe.getTemplate().test(item)){
                    found.add(SmithingSlot.TEMPLATE);
                }
                if(recipe.getBase().test(item)){
                    found.add(SmithingSlot.BASE);
                }
                if(recipe.getAddition().test(item)){
                    found.add(SmithingSlot.ADDITION);
                }
            } else if (r instanceof SmithingTrimRecipe recipe) {
                if(recipe.getTemplate().test(item)){
                    found.add(SmithingSlot.TEMPLATE);
                }
                if(recipe.getBase().test(item)){
                    found.add(SmithingSlot.BASE);
                }
                if(recipe.getAddition().test(item)){
                    found.add(SmithingSlot.ADDITION);
                }
            } else {
                Bukkit.getLogger().severe("Unknown recipe type: " + r.toString());
            }
        }
        return found;
    }
    @Nullable
    private SmithingSlot findSlotForItem(ItemStack item){
        Set<SmithingSlot> goesIn = findSlotsForItem(item);
        if(goesIn.isEmpty()){
            return null;
        }
        for(SmithingSlot slot : goesIn){
            ItemStack Item = inventory.getItem(slot.i);
            if(Item == null || Item.isEmpty()){
                return slot;
            }
        }
        return null;
    }
    @Override
    public void onViewClick(InventoryClickEvent e) {
        if(e.getClickedInventory().getHolder() != this){
            return;
        }
        SmithingSlot clickedSlot = SmithingSlot.getSlot(e.getSlot());
        if(clickedSlot == null){
            e.setCancelled(true);
            return;
        }
        if(clickedSlot == SmithingSlot.RESULT){
            if(e.getCurrentItem() == null){
                e.setCancelled(true);
                return;
            }
            for(SmithingSlot slot : SmithingSlot.values()){
                if(slot == SmithingSlot.RESULT){continue;}
                ItemStack item = inventory.getItem(slot.i);
                if(item == null){continue;}
                item.add(-1);
            }
        }

        Bukkit.getScheduler().runTaskLater(Geyser_Recipe_Fix.instance, () -> {
            ItemStack item = inventory.getItem(clickedSlot.i);
            if(item != null){
                SmithingSlot goesTo = findSlotForItem(item);
                if(goesTo != null && goesTo != clickedSlot){
                    inventory.setItem(clickedSlot.i, ItemStack.empty());
                    inventory.setItem(goesTo.i, item);
                }
            }

            updateWithBack();
        }, 1);
    }
    private void updateWithBack(){
        backInv.setResult(inventory.getItem(SmithingSlot.RESULT.i));
        backInv.setInputTemplate(inventory.getItem(SmithingSlot.TEMPLATE.i));
        backInv.setInputEquipment(inventory.getItem(SmithingSlot.BASE.i));
        backInv.setInputMineral(inventory.getItem(SmithingSlot.ADDITION.i));

        inventory.setItem(SmithingSlot.TEMPLATE.i, backInv.getInputTemplate());
        inventory.setItem(SmithingSlot.BASE.i, backInv.getInputEquipment());
        inventory.setItem(SmithingSlot.ADDITION.i, backInv.getInputMineral());
        inventory.setItem(SmithingSlot.RESULT.i, backInv.getResult());
    }
    @Override
    public void onClose(InventoryCloseEvent e){
        for(SmithingSlot slot : SmithingSlot.values()){
            ItemStack item = inventory.getItem(slot.i);
            if(item == null){ continue; }
            e.getPlayer().getInventory().addItem(item);
        }
    }

}
