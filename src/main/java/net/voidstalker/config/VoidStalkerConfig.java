package net.voidstalker.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.voidstalker.VoidStalkerMod;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Plain JSON config in config/voidstalker.json. Kept deliberately simple
 * (no config-screen library dependency) so it has no extra mod dependencies.
 */
public class VoidStalkerConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static VoidStalkerConfig instance;

    // --- Stalker ---
    public double stalkerSpawnChance = 0.002;      // per-eligible-tick chance
    public double stalkerVanishDistance = 14.0;
    public boolean stalkerAggressive = false;      // if true, Stalker can actually attack
    public int maxStalkersNearby = 1;

    // --- Void monsters ---
    public double voidlingSpawnWeight = 40.0;
    public double voidBruteSpawnWeight = 4.0;
    public double voidWatcherSpawnWeight = 2.0;

    // --- Horror events ---
    public double horrorEventChancePerMinute = 0.15;
    public double darknessEventChance = 0.01;
    public double stalkerEventChance = 0.05;
    public double silenceEventChance = 0.05;
    public double distantSoundEventChance = 0.35;
    public double voidEventChance = 0.1;
    public int horrorEventCooldownTicks = 20 * 45; // 45 seconds minimum between events

    // --- Structures ---
    public double structureRarityMultiplier = 1.0;

    public static VoidStalkerConfig get() {
        if (instance == null) {
            load();
        }
        return instance;
    }

    public static void load() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve("voidstalker.json");
        try {
            if (Files.exists(path)) {
                try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                    instance = GSON.fromJson(reader, VoidStalkerConfig.class);
                }
            }
        } catch (IOException e) {
            VoidStalkerMod.LOGGER.warn("[Void Stalker] Failed to read config, using defaults", e);
        }

        if (instance == null) {
            instance = new VoidStalkerConfig();
        }

        save(path);
    }

    private static void save(Path path) {
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                GSON.toJson(instance, writer);
            }
        } catch (IOException e) {
            VoidStalkerMod.LOGGER.warn("[Void Stalker] Failed to write config", e);
        }
    }
}
