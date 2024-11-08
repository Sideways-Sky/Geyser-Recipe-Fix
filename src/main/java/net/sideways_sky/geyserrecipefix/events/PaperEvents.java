package net.sideways_sky.geyserrecipefix.events;

import net.minecraft.world.inventory.AbstractContainerMenu;

import net.sideways_sky.geyserrecipefix.Geyser_Recipe_Fix;
import net.sideways_sky.geyserrecipefix.inventories.AnvilSim;
import net.sideways_sky.geyserrecipefix.inventories.SimInventory;
import net.sideways_sky.geyserrecipefix.inventories.SmithingSim;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.SmithingInventory;

import java.util.HashSet;
import java.util.Set;

import static net.sideways_sky.geyserrecipefix.Geyser_Recipe_Fix.*;

public class PaperEvents implements Listener {

    private static final Set<HumanEntity> forwardSkips = new HashSet<>();

    public static void openForward(HumanEntity player) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(instance, () -> {
            player.closeInventory();
            forwardSkips.add(player);
            player.openAnvil(null, true);
        }, 1);
    }

    @EventHandler
    public static void onOpenInv(InventoryOpenEvent e){
        if(!Geyser_Recipe_Fix.geyserApi.isBedrockPlayer(e.getPlayer().getUniqueId())){
            return;
        }
        if(forwardSkips.contains(e.getPlayer())){
            forwardSkips.remove(e.getPlayer());
            return;
        }
        if((
                e.getInventory().getType() == InventoryType.SMITHING &&
                e.getInventory() instanceof SmithingInventory &&
                SmithingSim.mode.test(e.getPlayer())
        ) || (
                e.getInventory().getType() == InventoryType.ANVIL &&
                e.getInventory() instanceof AnvilInventory &&
                AnvilSim.mode.test(e.getPlayer())
        )){

            AbstractContainerMenu menu = ((CraftInventoryView) e.getView()).getHandle();
            SimInventory sim = openMenus.get(menu.containerId);
            if(sim == null){
                debugInfo("New Sim; Wid:"+menu.containerId);
                sim = e.getInventory().getType() == InventoryType.ANVIL ? new AnvilSim() : new SmithingSim();
            }
            sim.menu = menu;
            openMenus.put(menu.containerId, sim);
        }
    }
}
