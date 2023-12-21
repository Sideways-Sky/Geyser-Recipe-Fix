package net.sideways_sky.geyserrecipefix;

import net.sideways_sky.geyserrecipefix.inventories.Smithing.Smithing;
import net.sideways_sky.geyserrecipefix.inventories.WorkstationGUI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.SmithingRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
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
        consoleSend("Loaded " + Smithing.recipes.size() + " Smithing Recipes");
    }

    @EventHandler
    public static void InventoryOpen(InventoryOpenEvent e){
        if((!Geyser_Recipe_Fix.GeyserInstance.isBedrockPlayer(e.getPlayer().getUniqueId())) || e.getInventory().getHolder() instanceof WorkstationGUI){
            return;
        }
        switch (e.getInventory().getType()){
            case SMITHING -> {
                e.setCancelled(true);
                Smithing.open(e.getPlayer());
            }
            case ANVIL -> {

            }
        }
    }

    @EventHandler
    public static void PrepareSmithing(PrepareSmithingEvent e){
        consoleSend("PrepareSmithing: " + Arrays.toString(e.getHandlers().getRegisteredListeners()));
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
