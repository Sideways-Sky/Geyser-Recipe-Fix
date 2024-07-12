package net.sideways_sky.geyserrecipefix.inventories;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AnvilMenu;
import net.sideways_sky.geyserrecipefix.events.PaperEvents;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.sideways_sky.geyserrecipefix.config.WorkstationMode;

import java.util.ArrayList;
import java.util.List;

public class AnvilSim extends SimInventory {
    public static WorkstationMode mode;
    public static boolean forwardEnabled;

    public enum AnvilSlot {
        FIRST(10, 0),
        SECOND(13, 1),
        RESULT(16, 2),
        COST(25, -1),
        FORWARD(26, -1);

        public final int frontIdx;
        public final int backIdx;
        AnvilSlot(int front, int back) {
            this.frontIdx = front;
            this.backIdx = back;
        }

        public static AnvilSlot getFromFrontIdx(int i){
            for (AnvilSlot slot : values()){
                if(slot.frontIdx == i){
                    return slot;
                }
            }
            return null;
        }
        public static AnvilSlot getFromBackIdx(int i){
            for (AnvilSlot slot : values()){
                if(slot.backIdx == i){
                    return slot;
                }
            }
            return null;
        }
    }

    protected static final int backSize = 39;
    protected static final int backTopSize = 3; // backSize - playerInvSize

    public AnvilSim() {
        super(backSize);
    }

    @Override
    protected List<ItemStack> getFront(List<ItemStack> items) {
        List<ItemStack> res = new ArrayList<>();
        for (int i = 0; i < frontTopSize; i++) {
            AnvilSlot slot = AnvilSlot.getFromFrontIdx(i);
            if(slot == AnvilSlot.COST){
                res.add(filler);
                continue;
            }
            if(slot == AnvilSlot.FORWARD){
                res.add(forwardEnabled ? new ItemStack(Material.ANVIL) : filler);
                continue;
            }
            res.add( slot == null ? filler : items.get(slot.backIdx));
        }
        return res;
    }

    @Override
    public int getBackIdxFromFrontIdx(int frontIdx) {
        if(frontIdx >= frontTopSize ){
            return frontIdx - (frontTopSize - backTopSize); //idx in player inventory
        }
        AnvilSlot slot = AnvilSlot.getFromFrontIdx(frontIdx);
        if(slot == AnvilSlot.COST){return -1;}
        if(slot == AnvilSlot.FORWARD){
            if(forwardEnabled){
                PaperEvents.openForward(menu.getBukkitView().getPlayer());
            }
            return -1;
        }
        return slot == null ? -1 : slot.backIdx;
    }

    @Override
    public int getFrontIdxFromBackIdx(int backIdx) {
        if(backIdx >= backTopSize){
            return backIdx + (frontTopSize - backTopSize); //idx in player inventory
        }
        AnvilSlot slot = AnvilSlot.getFromBackIdx(backIdx);
        return slot == null ? -1 : slot.frontIdx;
    }

    public void setCost(int cost, Player player){
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        AnvilMenu advMenu = (AnvilMenu) menu;
        boolean canTake = advMenu.getSlot(advMenu.getResultSlot()).mayPickup(serverPlayer);
        serverPlayer.containerSynchronizer.sendSlotChange(menu, AnvilSlot.COST.backIdx, net.minecraft.world.item.ItemStack.fromBukkitCopy(getCostIndicator(cost, canTake)));
    }
    private ItemStack getCostIndicator(int cost, boolean canTake){
        if(cost <= 0){
            return filler;
        }
        ItemStack indicator = new ItemStack(canTake ? Material.LIME_STAINED_GLASS : Material.RED_STAINED_GLASS);
        ItemMeta indicatorMeta = indicator.getItemMeta();
        indicatorMeta.displayName(Component.translatable("container.repair.cost", "Enchantment Cost: %1$s", Component.text(cost)).style(Style.style(canTake ? NamedTextColor.GREEN : NamedTextColor.RED)));
        indicator.setItemMeta(indicatorMeta);
        return indicator;
    }
}
