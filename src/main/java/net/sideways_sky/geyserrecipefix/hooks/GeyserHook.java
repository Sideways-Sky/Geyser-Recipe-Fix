package net.sideways_sky.geyserrecipefix.hooks;

import org.bukkit.Bukkit;
import org.geysermc.geyser.api.GeyserApi;

import java.util.UUID;

public class GeyserHook implements Hook {
    public GeyserApi api;
    public GeyserEvents events;

    public GeyserHook() throws ClassNotFoundException {
        if (!setup()) {
            throw new ClassNotFoundException();
        }
    }

    private boolean setup() {
        if(Bukkit.getPluginManager().getPlugin("Geyser-Spigot") == null){
            return false;
        }
        api = GeyserApi.api();
        events = new GeyserEvents(this);
        return true;
    }


    @Override
    public boolean isBedrockPlayer(UUID player) {
        return api.isBedrockPlayer(player);
    }
}
