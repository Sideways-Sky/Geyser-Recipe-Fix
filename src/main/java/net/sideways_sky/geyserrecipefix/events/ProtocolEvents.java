package net.sideways_sky.geyserrecipefix.events;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.sideways_sky.geyserrecipefix.inventories.AnvilSim;
import net.sideways_sky.geyserrecipefix.inventories.SimInventory;

import static net.sideways_sky.geyserrecipefix.Geyser_Recipe_Fix.*;


public class ProtocolEvents {

    public static void addListeners() {
        manager.addPacketListener(new PacketAdapter(instance, PacketType.Play.Server.OPEN_WINDOW) {
            @Override
            public void onPacketSending(PacketEvent event) {
                SimInventory sim = openMenus.get(event.getPacket().getIntegers().read(0));
                if(sim != null){
                    debugInfo(event.getPacketType() + ": " + event.getPacket().getModifier().getValues());
                    event.getPacket().getModifier().write(1, MenuType.GENERIC_9x3);
                }
            }
        });
        manager.addPacketListener(new PacketAdapter(instance, PacketType.Play.Server.CLOSE_WINDOW, PacketType.Play.Client.CLOSE_WINDOW) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                openMenus.remove(event.getPacket().getIntegers().read(0));
                debugInfo(event.getPacketType() + ": " + event.getPacket().getModifier().getValues());
            }

            @Override
            public void onPacketSending(PacketEvent event) {
                openMenus.remove(event.getPacket().getIntegers().read(0));
                debugInfo(event.getPacketType() + ": " + event.getPacket().getModifier().getValues());
            }
        });
        manager.addPacketListener(new PacketAdapter(instance,
                PacketType.Play.Server.WINDOW_ITEMS,
                PacketType.Play.Server.WINDOW_DATA,
                PacketType.Play.Server.SET_SLOT,
                PacketType.Play.Client.WINDOW_CLICK) {
            @Override
            public void onPacketSending(PacketEvent event) {
                SimInventory sim = openMenus.get(event.getPacket().getIntegers().read(0));
                if(sim != null){
                    debugInfo(event.getPacketType() + ": " + event.getPacket().getModifier().getValues());
                    if (event.getPacketType() == PacketType.Play.Server.WINDOW_ITEMS) {
                        event.getPacket().getItemListModifier().modify(0, sim::fromBackToFront);
                    } else if (event.getPacketType() == PacketType.Play.Server.WINDOW_DATA) {
                        if(sim instanceof AnvilSim anvilSim){
                            event.setCancelled(true);
                            anvilSim.setCost(event.getPacket().getIntegers().read(2), event.getPlayer());
                        }
                    } else if (event.getPacketType() == PacketType.Play.Server.SET_SLOT) {
                        int backSet = event.getPacket().getIntegers().read(2);
                        int frontSet = sim.getFrontIdxFromBackIdx(backSet);
                        debugInfo("back:" + backSet + " -> front: " + frontSet);
                        event.getPacket().getIntegers().write(2, frontSet);
                        debugInfo("set-finished: " + event.getPacket().getModifier().getValues());
                    }
                }
            }

            @Override
            public void onPacketReceiving(PacketEvent event) {
                SimInventory sim = openMenus.get(event.getPacket().getIntegers().read(0));
                if(sim != null){
                    debugInfo(event.getPacketType() + ": " + event.getPacket().getModifier().getValues());

                    Int2ObjectMap<ItemStack> changedSlots = (Int2ObjectMap<ItemStack>) event.getPacket().getModifier().read(6);
                    if(!changedSlots.isEmpty()){
                        Int2ObjectMap<ItemStack> mapped = new Int2ObjectOpenHashMap<>();

                        changedSlots.forEach((frontIdx, itemStack) -> {
                            int backIdx = sim.getBackIdxFromFrontIdx(frontIdx);
                            debugInfo("front:" + frontIdx + " -> back: " + backIdx);
                            mapped.put(backIdx, itemStack);
                        });

                        event.getPacket().getModifier().write(6, mapped);
                    }

                    int frontClick = event.getPacket().getIntegers().read(2);
                    if(frontClick == -999){
                        return;
                    }
                    int backClick = sim.getBackIdxFromFrontIdx(frontClick);
                    debugInfo("front:" + frontClick + " -> back: " + backClick);
                    if(backClick == -2){
                        if(AnvilSim.forwardEnabled && sim instanceof AnvilSim){
                            PaperEvents.openForward(event.getPlayer());
                        }
                    }
                    if(backClick == -1){
                        event.setCancelled(true);
                        sim.menu.sendAllDataToRemote();
                        return;
                    }
                    event.getPacket().getIntegers().write(2, backClick);

                    debugInfo("click-finished: " + event.getPacket().getModifier().getValues());
                }
            }
        });
    }
}
