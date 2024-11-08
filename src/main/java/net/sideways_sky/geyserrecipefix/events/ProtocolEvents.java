package net.sideways_sky.geyserrecipefix.events;

import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCloseWindow;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import net.sideways_sky.geyserrecipefix.inventories.AnvilSim;
import net.sideways_sky.geyserrecipefix.inventories.SimInventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static net.sideways_sky.geyserrecipefix.Geyser_Recipe_Fix.*;


public class ProtocolEvents implements PacketListener {

    @Override
    public void onPacketReceive(PacketReceiveEvent event){
        switch (event.getPacketType()){
            case PacketType.Play.Client.CLOSE_WINDOW -> {
                WrapperPlayClientCloseWindow packet = new WrapperPlayClientCloseWindow(event);
                debugInfo("Client Close; Wid:" + packet.getWindowId());
                openMenus.remove(packet.getWindowId());
            }
            case PacketType.Play.Client.CLICK_WINDOW -> {
                WrapperPlayClientClickWindow packet = new WrapperPlayClientClickWindow(event);
                SimInventory sim = openMenus.get(packet.getWindowId());
                if(sim == null){
                    debugInfo("Client Click Non-Sim; Wid:"+packet.getWindowId());
                    return;}
                debugInfo("Client Click; Wid:" + packet.getWindowId() + " Slot:" + packet.getSlot() + " Slots:" + packet.getSlots());
                packet.getSlots().ifPresent(changedSlots -> {
                    Map<Integer, ItemStack> mapped = new HashMap<>();
                    changedSlots.forEach((slot, item) -> {
                        mapped.put(sim.getBackIdxFromFrontIdx(slot), item);
                    });

                    packet.setSlots(Optional.of(mapped));
                });

                if(packet.getSlot() == -999){
                    debugInfo("Client Click Outside");
                    return;
                }
                int backSlot = sim.getBackIdxFromFrontIdx(packet.getSlot());
                if(backSlot == -1){
                    event.setCancelled(true);
                    sim.menu.sendAllDataToRemote();
                    debugInfo("Client Click Filler");
                    return;
                } else if (backSlot == -2) {
                    if(AnvilSim.forwardEnabled && sim instanceof AnvilSim){
                            PaperEvents.openForward(event.getPlayer());
                        }
                }
                packet.setSlot(backSlot);
                debugInfo("Client Click Completed; Wid:" + packet.getWindowId() + " Slot:" + packet.getSlot() + " Slots:" + packet.getSlots());
            }
            default -> {}
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event){
        switch (event.getPacketType()){
            case PacketType.Play.Server.OPEN_WINDOW -> {
                WrapperPlayServerOpenWindow packet = new WrapperPlayServerOpenWindow(event);
                SimInventory sim = openMenus.get(packet.getContainerId());
                if(sim == null){
                    debugInfo("Server Open Non-Sim; Wid:" + packet.getContainerId());
                    return;}
                debugInfo("Server Open; Wid:" + packet.getContainerId() + " Type:" + packet.getType());
                packet.setType(2);
                debugInfo("Server Open Completed; Wid:" + packet.getContainerId() + " Type:" + packet.getType());
            }
            case PacketType.Play.Server.CLOSE_WINDOW -> {
                WrapperPlayServerCloseWindow packet = new WrapperPlayServerCloseWindow(event);
                debugInfo("Server Close; Wid:" + packet.getWindowId());
                openMenus.remove(packet.getWindowId());
            }
            case  PacketType.Play.Server.WINDOW_ITEMS -> {
                WrapperPlayServerWindowItems packet = new WrapperPlayServerWindowItems(event);
                SimInventory sim = openMenus.get(packet.getWindowId());
                if(sim == null){
                    debugInfo("Server Items Non-Sim; Wid:" + packet.getWindowId());
                    return;}
                debugInfo("Server Items; Wid:" + packet.getWindowId() + " Items:" + packet.getItems());
                packet.setItems(sim.fromBackToFront(packet.getItems()));
                debugInfo("Server Items Completed; Wid:" + packet.getWindowId() + " Items:" + packet.getItems());
            }
            case  PacketType.Play.Server.WINDOW_PROPERTY -> {
                WrapperPlayServerWindowProperty packet = new WrapperPlayServerWindowProperty(event);
                SimInventory sim = openMenus.get(packet.getContainerId());
                if(sim == null){
                    debugInfo("Server Property Non-Sim; Wid:" + packet.getContainerId());
                    return;}
                debugInfo("Server Property; Wid:" + packet.getContainerId() + " Value:" + packet.getValue());
                event.setCancelled(true);
                if(sim instanceof AnvilSim anvilSim){
                  anvilSim.setCost(packet.getValue(), event.getPlayer());
                }
            }
            case PacketType.Play.Server.SET_SLOT -> {
                WrapperPlayServerSetSlot packet = new WrapperPlayServerSetSlot(event);
                SimInventory sim = openMenus.get(packet.getWindowId());
                if(sim == null){
                    debugInfo("Server Slot Non-Sim; Wid:" + packet.getWindowId());
                    return;}
                debugInfo("Server Slot; Wid:" + packet.getWindowId() + " Slot:"+packet.getSlot());
                packet.setSlot(sim.getFrontIdxFromBackIdx(packet.getSlot()));
                debugInfo("Server Slot Completed; Wid:" + packet.getWindowId() + " Slot:"+packet.getSlot());
            }
            default -> {}
        }
    }
}
