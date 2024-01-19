package net.sideways_sky.geyserrecipefix;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AnvilMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.AnvilInventory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class nmsManager {
    private final String CRAFTBUKKIT_PACKAGE = Bukkit.getServer().getClass().getPackage().getName();
    public nmsManager() throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException {
        CraftInventoryAnvilContainer = cbClass("inventory.CraftInventoryAnvil").getDeclaredField("container");
        CraftInventoryAnvilContainer.setAccessible(true);
        CraftPlayerGetHandle = cbClass("entity.CraftPlayer").getMethod("getHandle");
    }
    public Class<?> cbClass(String clazz) throws ClassNotFoundException {
        return Class.forName(CRAFTBUKKIT_PACKAGE + "." + clazz);
    }

    private final Field CraftInventoryAnvilContainer;
    private final Method CraftPlayerGetHandle;
    public AnvilMenu getAnvilContainer(AnvilInventory Inv) throws IllegalAccessException {
        return (AnvilMenu) CraftInventoryAnvilContainer.get(Inv);
    }
    public ServerPlayer getServerPlayer(Player player) throws InvocationTargetException, IllegalAccessException {
        return (ServerPlayer) CraftPlayerGetHandle.invoke(player);
    }

}
