package net.sideways_sky.geyserrecipefix;

import org.bukkit.Bukkit;

public class utils {
    public static void consoleSend(String message) {
        Bukkit.getConsoleSender().sendMessage("[Geyser Recipe Fix] " + message);
    }
}