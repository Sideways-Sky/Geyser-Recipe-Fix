package net.sideways_sky.geyserrecipefix;

import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.event.EventBus;
import org.geysermc.geyser.api.event.EventRegistrar;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomItemsEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserLoadResourcePacksEvent;
import org.geysermc.geyser.api.item.custom.CustomItemData;
import org.geysermc.geyser.api.item.custom.CustomItemOptions;

import java.io.File;

import static net.sideways_sky.geyserrecipefix.utils.consoleSend;

public class GeyserEvents implements EventRegistrar {
    public GeyserEvents(){
        EventBus<EventRegistrar> bus = Geyser_Recipe_Fix.GeyserInstance.eventBus();
        bus.register(this, this);
        bus.subscribe(this, GeyserLoadResourcePacksEvent.class, this::onGeyserLoadResourcePacks);
        bus.subscribe(this, GeyserDefineCustomItemsEvent.class, this::onGeyserDefineCustomItems);
    }
    @Subscribe
    public void onGeyserDefineCustomItems(GeyserDefineCustomItemsEvent e){
        e.register("minecraft:structure_void", CustomItemData.builder().customItemOptions(
                CustomItemOptions.builder().customModelData(593721).build()
        ).name("empty").displayName(" ").textureSize(16).build());
         CustomItemOptions.builder().customModelData(593721).build();
    }
    @Subscribe
    public void onGeyserLoadResourcePacks(GeyserLoadResourcePacksEvent e){
        File rp = new File(Geyser_Recipe_Fix.instance.getDataFolder(), "pack.mcpack");
        if(!rp.exists()){
            Geyser_Recipe_Fix.instance.saveResource("pack.mcpack", true);
        }
        e.resourcePacks().add(rp.toPath());

        consoleSend("Packs: " + e.resourcePacks());
    }
}
