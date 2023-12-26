package net.sideways_sky.geyserrecipefix;


import net.sideways_sky.geyserrecipefix.inventories.WorkstationGUI;
import net.sideways_sky.geyserrecipefix.inventories.anvil.Anvil;
import net.sideways_sky.geyserrecipefix.inventories.smithing.Smithing;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static net.sideways_sky.geyserrecipefix.utils.consoleSend;

public class Events implements Listener {

    @EventHandler
    public static void ServerLoad(ServerLoadEvent e){
        for (@NotNull Iterator<Recipe> it = Bukkit.recipeIterator(); it.hasNext(); ) {
            Recipe recipe = it.next();
            if(recipe instanceof SmithingRecipe r){
                Smithing.recipes.add(r);
            }
        }
        consoleSend("Loaded " + Smithing.recipes.size() + " smithing Recipes");
    }

    @EventHandler
    public static void test(PrepareAnvilEvent e){
        if(e.getInventory().getFirstItem() == null){
            return;
        }
        AnvilInventory inv = e.getInventory();
        if(e.getInventory().getFirstItem().getType() == Material.STRUCTURE_VOID){
            e.setResult(new ItemStack(Material.STRUCTURE_BLOCK));
        }
        consoleSend("PrepareAnvilEvent: " + inv.getRepairCost() + " / " + inv.getRepairCostAmount() + " | " + inv.getMaximumRepairCost() + " - " + inv.getRenameText());
    }

    @EventHandler
    public static void InventoryOpen(InventoryOpenEvent e){
        if((!Geyser_Recipe_Fix.GeyserInstance.isBedrockPlayer(e.getPlayer().getUniqueId())) || e.getInventory().getHolder() instanceof WorkstationGUI){
            return;
        }
        switch (e.getInventory().getType()){
            case SMITHING -> {
                e.setCancelled(true);
                new Smithing().open(e.getPlayer());
            }
            case ANVIL -> {
                AnvilInventory inv = (AnvilInventory) e.getInventory();
                e.setCancelled(true);
                new Anvil(inv).open(e.getPlayer());
            }
        }
    }

    @EventHandler
    public static void InventoryClick(InventoryClickEvent e){
        if(e.getClickedInventory() == null){return;}
        if(e.getView().getTopInventory().getHolder() instanceof WorkstationGUI inv){
            inv.onViewClick(e);
        }
    }

    @EventHandler
    public static void InventoryClose(InventoryCloseEvent e){
        if(e.getInventory().getHolder() instanceof WorkstationGUI inv){
            inv.onClose(e);
        }
    }
}
