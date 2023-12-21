package net.sideways_sky.geyserrecipefix.inventories.Smithing;

import net.sideways_sky.geyserrecipefix.inventories.SimulatedInventoryView;
import net.sideways_sky.geyserrecipefix.inventories.WorkstationGUI;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.*;

import java.util.*;

import static net.sideways_sky.geyserrecipefix.utils.*;

public class Smithing extends WorkstationGUI {
    public static List<SmithingRecipe> recipes = new ArrayList<>();
    public Smithing () {
        super(InventoryType.SMITHING, "Smithing");
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
    @Override
    public void onViewClick(InventoryClickEvent e) {
        if(e.getClickedInventory().getHolder() != this){
            return;
        }
        consoleSend("Hit:"+e.getSlot()+" - "+e.getCurrentItem()+" - "+e.getCursor());
        if(e.getSlot() == SmithingSlot.RESULT.i){
            e.setCancelled(true);
            return;
        }
        Inventory newInv = cloneInv(inventory, true);
        newInv.setItem(e.getSlot(), e.getCursor());
        SmithingRecipe recipe = checkForRecipe(newInv);

        SimulatedSmithingInventory simInv = new SimulatedSmithingInventory(newInv, recipe);
        SimulatedInventoryView view = new SimulatedInventoryView(simInv, e.getWhoClicked(), e.getView().getOriginalTitle());

        ItemStack base = inventory.getItem(SmithingSlot.BASE.i);
        ItemStack result = recipe == null ? null : recipe.getResult();

        if(result != null && base != null && recipe.willCopyNbt()){
            result.setItemMeta(mergeMeta(result.getItemMeta(), base.getItemMeta()));
        }

        PrepareSmithingEvent event = new PrepareSmithingEvent(view, result);
        Bukkit.getPluginManager().callEvent(event);

        inventory.setItem(SmithingSlot.RESULT.i, event.getResult());
    }

}
