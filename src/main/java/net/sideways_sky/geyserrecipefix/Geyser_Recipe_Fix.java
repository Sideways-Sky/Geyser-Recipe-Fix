package net.sideways_sky.geyserrecipefix;

import net.sideways_sky.geyserrecipefix.inventories.WorkstationGUI;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.event.EventRegistrar;


public final class Geyser_Recipe_Fix extends JavaPlugin implements EventRegistrar {

    public static GeyserApi GeyserInstance;
    public static Geyser_Recipe_Fix instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
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
