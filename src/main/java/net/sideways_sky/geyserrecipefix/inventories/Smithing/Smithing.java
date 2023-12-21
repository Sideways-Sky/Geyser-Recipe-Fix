package net.sideways_sky.geyserrecipefix.inventories.Smithing;

import net.kyori.adventure.text.Component;
import net.sideways_sky.geyserrecipefix.inventories.SimulatedInventoryView;
import net.sideways_sky.geyserrecipefix.inventories.WorkstationGUI;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.*;

import java.util.*;

import static net.sideways_sky.geyserrecipefix.utils.*;

public class Smithing extends WorkstationGUI {
    public static List<SmithingRecipe> recipes = new ArrayList<>();
    public Smithing () {
        inventory = Bukkit.createInventory(this, 27, Component.text("Smithing"));
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, Filler);
        }
        // 9 - template
        // 10 - base
        // 11 - addition
        inventory.setItem(12, Filler);
        inventory.setItem(13, Filler);
        // 14 - result
        for (int i = 15; i < 27; i++) {
            inventory.setItem(i, Filler);
        }
    }
    private static SmithingRecipe checkForRecipe(Inventory inv){
        ItemStack template = Objects.requireNonNullElseGet(inv.getItem(SmithingSlot.TEMPLATE.i), ItemStack::empty);
        ItemStack base = Objects.requireNonNullElseGet(inv.getItem(SmithingSlot.BASE.i), ItemStack::empty);
        ItemStack addition = Objects.requireNonNullElseGet(inv.getItem(SmithingSlot.ADDITION.i), ItemStack::empty);
        for(SmithingRecipe r : recipes){
            if(r instanceof SmithingTransformRecipe recipe){
                if(recipe.getTemplate().test(template) && recipe.getBase().test(base) && recipe.getAddition().test(addition)){
                    return recipe;
                }
            } else if (r instanceof SmithingTrimRecipe recipe) {
                if(recipe.getTemplate().test(template) && recipe.getBase().test(base) && recipe.getAddition().test(addition)){
                    return recipe;
                }
            } else {
                consoleSend("Unknown recipe type: " + r.toString());
            }
        }
        return null;
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
                consoleSend("Unknown recipe type: " + r.toString());
            }
        }
        return found;
    }
    @Override
    public void onViewClick(InventoryClickEvent e) {
        if(e.getClickedInventory().getHolder() != this){
            return;
        }
        if(e.getSlot() == SmithingSlot.RESULT.i){
            if(e.getCurrentItem() == null){
                e.setCancelled(true);
                return;
            }
            inventory.getItem(SmithingSlot.TEMPLATE.i).add(-1);
            inventory.getItem(SmithingSlot.BASE.i).add(-1);
            inventory.getItem(SmithingSlot.ADDITION.i).add(-1);
            return;
        }
        Inventory newInv = cloneInv(inventory, true);
        newInv.setItem(e.getSlot(), e.getCursor());

        if(!e.getCursor().isEmpty()){
            Set<SmithingSlot> goesIn = findSlotsForItem(e.getCursor());
            if(goesIn.isEmpty()){
                e.setCancelled(true);
                return;
            }
            if(!goesIn.contains(SmithingSlot.getSlot(e.getSlot()))){
                for(SmithingSlot slot : goesIn){
                    ItemStack Item = inventory.getItem(slot.i);
                    if(Item == null || Item.isEmpty()){
                        inventory.setItem(slot.i, e.getCursor());
                        e.setCursor(null);
                        break;
                    }
                }
            }
        }

        SmithingRecipe recipe = checkForRecipe(newInv);

        SimulatedSmithingInventory simInv = new SimulatedSmithingInventory(newInv, recipe);
        SimulatedInventoryView view = new SimulatedInventoryView(simInv, e.getWhoClicked(), e.getView().getOriginalTitle());

        ItemStack base = inventory.getItem(SmithingSlot.BASE.i);
        ItemStack result = recipe == null ? null : recipe.getResult();

        if(result != null && base != null && recipe.willCopyNbt()){
            result = mergeMeta(result, base);
        }

        PrepareSmithingEvent event = new PrepareSmithingEvent(view, result);
        Bukkit.getPluginManager().callEvent(event);

        inventory.setItem(SmithingSlot.RESULT.i, event.getResult());
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
