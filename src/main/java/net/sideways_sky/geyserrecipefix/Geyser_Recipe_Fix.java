package net.sideways_sky.geyserrecipefix;

import net.sideways_sky.geyserrecipefix.hooks.FloodgateHook;
import net.sideways_sky.geyserrecipefix.hooks.GeyserHook;
import net.sideways_sky.geyserrecipefix.hooks.Hook;
import net.sideways_sky.geyserrecipefix.inventories.WorkstationGUI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public final class Geyser_Recipe_Fix extends JavaPlugin {

    public static Geyser_Recipe_Fix instance;
    public static nmsManager nms;
    public static Hook geyserApi;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        try {
            nms = new nmsManager();
        } catch (ClassNotFoundException | NoSuchFieldException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        getServer().getPluginManager().registerEvents(new Events(), this);
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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
