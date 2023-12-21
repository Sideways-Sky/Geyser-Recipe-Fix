package net.sideways_sky.geyserrecipefix;

import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.geyser.api.GeyserApi;

public final class Geyser_Recipe_Fix extends JavaPlugin {

    public static GeyserApi GeyserInstance;
    public static Geyser_Recipe_Fix instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        GeyserInstance = GeyserApi.api();
        getServer().getPluginManager().registerEvents(new Events(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
