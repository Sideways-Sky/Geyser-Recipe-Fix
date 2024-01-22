package net.sideways_sky.geyserrecipefix.hooks;

import net.sideways_sky.geyserrecipefix.Geyser_Recipe_Fix;
import org.bukkit.Bukkit;
import org.geysermc.floodgate.api.FloodgateApi;

import java.io.File;
import java.util.UUID;

public class FloodgateHook implements Hook {
    public FloodgateApi api;

    public FloodgateHook() throws ClassNotFoundException {
        if(!setup()) {
            throw new ClassNotFoundException();
        }
    }

    private boolean setup(){
        if(Bukkit.getPluginManager().getPlugin("floodgate") == null){
            return false;
        }
        api = FloodgateApi.getInstance();

        File rpf = new File(Geyser_Recipe_Fix.instance.getDataFolder(), "GeyserRecipeFix-Pack.mcpack");
        if(!rpf.exists()){
            Geyser_Recipe_Fix.instance.saveResource("GeyserRecipeFix-Pack.mcpack", true);
        }

        File mf = new File(Geyser_Recipe_Fix.instance.getDataFolder(), "GeyserRecipeFix-Mappings.json");
        if(!mf.exists()){
            Geyser_Recipe_Fix.instance.saveResource("GeyserRecipeFix-Mappings.json", true);
        }

        return true;
    }


    @Override
    public boolean isBedrockPlayer(UUID player) {
        return api.isFloodgatePlayer(player);
    }
}
