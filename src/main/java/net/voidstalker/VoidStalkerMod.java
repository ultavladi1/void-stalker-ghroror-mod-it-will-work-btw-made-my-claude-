package net.voidstalker;

import net.fabricmc.api.ModInitializer;
import net.voidstalker.config.VoidStalkerConfig;
import net.voidstalker.event.HorrorEventManager;
import net.voidstalker.registry.ModAttributes;
import net.voidstalker.registry.ModBlocks;
import net.voidstalker.registry.ModEntities;
import net.voidstalker.registry.ModItemGroups;
import net.voidstalker.registry.ModItems;
import net.voidstalker.registry.ModSounds;
import net.voidstalker.registry.ModSpawns;
import net.voidstalker.network.SilencePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VoidStalkerMod implements ModInitializer {
    public static final String MOD_ID = "voidstalker";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("[Void Stalker] Something stirs in the dark...");

        VoidStalkerConfig.load();

        ModSounds.register();
        ModBlocks.register();
        ModItems.register();
        ModEntities.register();
        ModAttributes.register();
        ModItemGroups.register();
        ModSpawns.register();

        SilencePayload.registerC2SAndS2C();
        HorrorEventManager.register();
    }
}
