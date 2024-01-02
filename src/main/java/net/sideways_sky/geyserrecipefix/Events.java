package net.sideways_sky.geyserrecipefix;


import net.sideways_sky.geyserrecipefix.inventories.WorkstationGUI;
import net.sideways_sky.geyserrecipefix.inventories.anvil.Anvil;
import net.sideways_sky.geyserrecipefix.inventories.smithing.Smithing;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static net.sideways_sky.geyserrecipefix.utils.consoleSend;

public class Events implements Listener {

    @EventHandler
    public static void onServerLoad(ServerLoadEvent e){
        for (@NotNull Iterator<Recipe> it = Bukkit.recipeIterator(); it.hasNext(); ) {
            Recipe recipe = it.next();
            if(recipe instanceof SmithingRecipe r){
                Smithing.recipes.add(r);
            }
        }
        consoleSend("Loaded " + Smithing.recipes.size() + " smithing Recipes");
    }

    @EventHandler
    public static void onInventoryOpen(InventoryOpenEvent e){
        if((!Geyser_Recipe_Fix.GeyserInstance.isBedrockPlayer(e.getPlayer().getUniqueId())) || e.getInventory().getHolder() instanceof WorkstationGUI){
            return;
        }
        if(e.getInventory() instanceof SmithingInventory inv){
            e.setCancelled(true);
            new Smithing(inv).open(e.getPlayer());
        } else if (e.getInventory() instanceof AnvilInventory inv) {
            Anvil res = Anvil.get(inv);
            if(res == null){
                e.setCancelled(true);
                Anvil.create(inv).open(e.getPlayer());
            }
        }
    }

    @EventHandler
    public static void onInventoryClick(InventoryClickEvent e){
        if(e.getClickedInventory() == null){return;}
        if(e.getView().getTopInventory().getHolder() instanceof WorkstationGUI inv){
            inv.onViewClick(e);
        }
    }

    @EventHandler
    public static void onInventoryClose(InventoryCloseEvent e){
        if(e.getInventory().getHolder() instanceof WorkstationGUI inv){
            inv.onClose(e);
        }
    }
}
