package net.sideways_sky.geyserrecipefix;

import net.sideways_sky.geyserrecipefix.inventories.WorkstationGUI;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.event.EventRegistrar;


public final class Geyser_Recipe_Fix extends JavaPlugin implements EventRegistrar {

    public static GeyserApi GeyserInstance;
    public static Geyser_Recipe_Fix instance;
    public static nmsManager nms;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        try {
            nms = new nmsManager();
        } catch (ClassNotFoundException | NoSuchFieldException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        GeyserInstance = GeyserApi.api();
        WorkstationGUI.init();
        getServer().getPluginManager().registerEvents(new Events(), this);
        new GeyserEvents();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
