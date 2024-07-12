package net.sideways_sky.geyserrecipefix;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import net.sideways_sky.geyserrecipefix.config.WorkstationMode;
import net.sideways_sky.geyserrecipefix.events.PaperEvents;
import net.sideways_sky.geyserrecipefix.events.ProtocolEvents;
import net.sideways_sky.geyserrecipefix.hooks.FloodgateHook;
import net.sideways_sky.geyserrecipefix.hooks.GeyserHook;
import net.sideways_sky.geyserrecipefix.hooks.Hook;
import net.sideways_sky.geyserrecipefix.inventories.AnvilSim;
import net.sideways_sky.geyserrecipefix.inventories.SimInventory;
import net.sideways_sky.geyserrecipefix.inventories.SmithingSim;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class Geyser_Recipe_Fix extends JavaPlugin implements Listener {

    public static Geyser_Recipe_Fix instance;
    public static ProtocolManager manager;
    public static Map<Integer, SimInventory> openMenus = new HashMap<>();
    public static Hook geyserApi;

    private static Logger logger;
    private static boolean debug = false;

    public static void debugInfo(String message){
        if(debug){
            logger.info(message);
        }
    }

    @Override
    public void onEnable() {
        instance = this;
        manager = ProtocolLibrary.getProtocolManager();
        logger = getLogger();
        saveDefaultConfig();
        debug = getConfig().getBoolean("debug");
        AnvilSim.mode = WorkstationMode.valueOf(getConfig().getString("anvil.mode"));
        AnvilSim.forwardEnabled = getConfig().getBoolean("anvil.forward", true);
        SmithingSim.mode = WorkstationMode.valueOf(getConfig().getString("smithing.mode"));

        try {
            geyserApi = new GeyserHook();
        } catch (ClassNotFoundException e) {
            try {
                geyserApi = new FloodgateHook();
            } catch (ClassNotFoundException ex) {
                getLogger().severe("Geyser nor Floodgate found. Shutting down");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }
        if(geyserApi instanceof FloodgateHook){
            getLogger().warning("Geyser was not found but floodgate was. Take Mappings and Pack from this plugin's folder and add them to Geyser's");
        }

        getServer().getPluginManager().registerEvents(new PaperEvents(), this);
        ProtocolEvents.addListeners();
    }
}
