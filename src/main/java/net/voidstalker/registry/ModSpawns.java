package net.voidstalker.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.entity.MobCategory;
import net.voidstalker.config.VoidStalkerConfig;

/**
 * Natural spawn weights. Kept low on purpose — see MOD CONCEPT: encounters
 * should feel rare. Call ModSpawns.register() once during mod init (added
 * from VoidStalkerMod).
 */
public class ModSpawns {
    public static void register() {
        VoidStalkerConfig config = VoidStalkerConfig.get();

        BiomeModifications.addSpawn(
                BiomeSelectors.foundInOverworld(),
                MobCategory.MONSTER,
                ModEntities.VOIDLING,
                (int) config.voidlingSpawnWeight, 2, 4
        );

        BiomeModifications.addSpawn(
                BiomeSelectors.foundInOverworld(),
                MobCategory.MONSTER,
                ModEntities.VOID_BRUTE,
                (int) config.voidBruteSpawnWeight, 1, 1
        );

        BiomeModifications.addSpawn(
                BiomeSelectors.foundInOverworld(),
                MobCategory.MONSTER,
                ModEntities.VOID_WATCHER,
                (int) config.voidWatcherSpawnWeight, 1, 1
        );

        // The Stalker deliberately does NOT get a normal biome spawn entry —
        // it only appears via HorrorEventManager's stalkerEvent, so its
        // rarity is governed entirely by horrorEventChancePerMinute /
        // stalkerEventChance in the config rather than vanilla spawn rolls.
    }
}
