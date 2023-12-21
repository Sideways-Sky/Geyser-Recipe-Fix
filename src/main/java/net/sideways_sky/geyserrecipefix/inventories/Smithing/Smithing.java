package net.sideways_sky.geyserrecipefix.inventories.Smithing;

import net.sideways_sky.geyserrecipefix.inventories.WorkstationGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.*;

import java.util.*;

import static net.sideways_sky.geyserrecipefix.utils.cloneInv;
import static net.sideways_sky.geyserrecipefix.utils.consoleSend;

public class Smithing extends WorkstationGUI {
    public static List<SmithingRecipe> recipes = new ArrayList<>();
    public Smithing () {
        super(27, "Smithing");

        for (int i = 0; i < 9; i++) {
            inv.setItem(i, new ItemStack(Material.STRUCTURE_VOID));
        }
        // 9 - template
        // 10 - base
        // 11 - addition
        inv.setItem(12, new ItemStack(Material.STRUCTURE_VOID));
        inv.setItem(13, new ItemStack(Material.STRUCTURE_VOID));
        // 14 - result
        for (int i = 15; i < 27; i++) {
            inv.setItem(i, new ItemStack(Material.STRUCTURE_VOID));
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
                consoleSend("Unknown recipe type: " + r.toString());
            }
        }
        return found;
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

    private SmithingInventory toSmithingInv(){
        SmithingInventory res = (SmithingInventory) Bukkit.createInventory(this, InventoryType.SMITHING);
        res.setResult(inv.getItem(SmithingSlot.RESULT.i));
        res.setInputTemplate(inv.getItem(SmithingSlot.TEMPLATE.i));
        res.setInputEquipment(inv.getItem(SmithingSlot.BASE.i));
        res.setInputMineral(inv.getItem(SmithingSlot.ADDITION.i));
        return res;
    }
    public static Smithing fromSmithingInv(SmithingInventory inv){
        Smithing res = new Smithing();
        res.inv.setItem(SmithingSlot.RESULT.i, inv.getResult());
        res.inv.setItem(SmithingSlot.TEMPLATE.i, inv.getInputTemplate());
        res.inv.setItem(SmithingSlot.BASE.i, inv.getInputEquipment());
        res.inv.setItem(SmithingSlot.ADDITION.i, inv.getInputMineral());
        return res;
    }
    @Override
    public void onViewClick(InventoryClickEvent e){
        if(e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.STRUCTURE_VOID){
            e.setCancelled(true);
            return;
        }

        if(e.getClickedInventory().getHolder() instanceof Smithing){
            SmithingSlot smithingSlot = SmithingSlot.getSlot(e.getSlot());
            if(smithingSlot == null || smithingSlot == SmithingSlot.RESULT){
                e.setCancelled(true);
                return;
            }
            Inventory newInv = cloneInv(inv);
            newInv.setItem(smithingSlot.i, e.getCursor());

            if(!e.getCursor().isEmpty()){
                Set<SmithingSlot> goesIn = findSlotsForItem(e.getCursor());
                if(goesIn.isEmpty()){
                    e.setCancelled(true);
                    return;
                }
                if(!goesIn.contains(smithingSlot)){
                    for(SmithingSlot slot : goesIn){
                        ItemStack Item = inv.getItem(slot.i);
                        if(Item == null || Item.isEmpty()){
                            inv.setItem(slot.i, e.getCursor());
                            e.setCursor(null);
                            break;
                        }
                    }
                }
            }

            SmithingRecipe recipe = checkForRecipe(newInv);
            if(recipe == null){
                inv.setItem(SmithingSlot.RESULT.i, null);
            } else {
                inv.setItem(SmithingSlot.RESULT.i, recipe.getResult());
            }

        }
    }
    @Override
    public void onClose(InventoryCloseEvent e){
        for (SmithingSlot slot : SmithingSlot.values()){
            ItemStack item = inv.getItem(slot.i);
            if(item == null){ continue; }
            e.getPlayer().getInventory().addItem(item);
        }
    }

}
