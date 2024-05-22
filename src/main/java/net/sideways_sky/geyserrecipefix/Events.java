package net.sideways_sky.geyserrecipefix;


import net.sideways_sky.geyserrecipefix.inventories.SimInventoryView;
import net.sideways_sky.geyserrecipefix.inventories.WorkstationGUI;
import net.sideways_sky.geyserrecipefix.inventories.anvil.Anvil;
import net.sideways_sky.geyserrecipefix.inventories.smithing.Smithing;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;


public class Events implements Listener {

    public static boolean ignoreEvents = false;

    @EventHandler
    public static void onInventoryOpen(InventoryOpenEvent e){
        if(ignoreEvents){return;}
        if((!Geyser_Recipe_Fix.geyserApi.isBedrockPlayer(e.getPlayer().getUniqueId())) || e.getInventory().getHolder() instanceof WorkstationGUI){
            return;
        }
        if(e.getInventory() instanceof SmithingInventory inv && Smithing.mode.test(e.getPlayer())){
            e.setCancelled(true);
            new Smithing(inv, e.getView()).open(e.getPlayer());
        } else if (e.getInventory() instanceof AnvilInventory inv && Anvil.mode.test(e.getPlayer())) {
            e.setCancelled(true);
            new Anvil(inv, e.getView()).open(e.getPlayer());
        }
    }

    @EventHandler
    public static void onInventoryClick(InventoryClickEvent e){
        if(ignoreEvents){return;}
        if(e.getView() instanceof SimInventoryView){return;}
        if(e.getClickedInventory() == null){return;}
        if(e.getView().getTopInventory().getHolder() instanceof WorkstationGUI inv){
            inv.onViewClick(e);
        }
    }

    @EventHandler
    public static void onInventoryClose(InventoryCloseEvent e){
        if(ignoreEvents){return;}
        if(e.getInventory().getHolder() instanceof WorkstationGUI inv){
            inv.onClose(e);
        }
    }
}
