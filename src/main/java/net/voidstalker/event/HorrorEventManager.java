package net.voidstalker.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.voidstalker.config.VoidStalkerConfig;
import net.voidstalker.registry.ModEntities;
import net.voidstalker.registry.ModSounds;

import java.util.HashMap;
import java.util.Map;

/**
 * Central "something happens occasionally" system. Runs once per second per
 * player (not per tick) to keep this cheap, and enforces a per-player
 * cooldown so events stay rare regardless of how the individual chances are
 * tuned in the config.
 */
public class HorrorEventManager {

    private static final Map<ServerPlayer, Integer> COOLDOWNS = new HashMap<>();

    public static void register() {
        ServerTickEvents.END_WORLD_TICK.register(HorrorEventManager::onWorldTick);
    }

    private static void onWorldTick(ServerLevel level) {
        if (level.getGameTime() % 20 != 0) return; // once per second

        VoidStalkerConfig config = VoidStalkerConfig.get();
        RandomSource random = level.random;

        for (ServerPlayer player : level.players()) {
            int cooldown = COOLDOWNS.getOrDefault(player, 0);
            if (cooldown > 0) {
                COOLDOWNS.put(player, cooldown - 20);
                continue;
            }

            // horrorEventChancePerMinute is a chance per minute; we check once per
            // second, so scale down accordingly.
            if (random.nextDouble() < config.horrorEventChancePerMinute / 60.0) {
                triggerRandomEvent(level, player, config, random);
                COOLDOWNS.put(player, config.horrorEventCooldownTicks);
            }
        }
    }

    private static void triggerRandomEvent(ServerLevel level, ServerPlayer player, VoidStalkerConfig config, RandomSource random) {
        double roll = random.nextDouble();
        double total = config.darknessEventChance + config.stalkerEventChance + config.silenceEventChance
                + config.distantSoundEventChance + config.voidEventChance;
        if (total <= 0) return;
        roll *= total;

        double acc = 0;
        if ((acc += config.darknessEventChance) > roll) {
            darknessEvent(player);
        } else if ((acc += config.stalkerEventChance) > roll) {
            stalkerEvent(level, player, config, random);
        } else if ((acc += config.silenceEventChance) > roll) {
            silenceEvent(player);
        } else if ((acc += config.distantSoundEventChance) > roll) {
            distantSoundEvent(level, player, random);
        } else {
            voidEvent(level, player, random);
        }
    }

    private static void darknessEvent(ServerPlayer player) {
        player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100, 0, false, false));
    }

    private static void stalkerEvent(ServerLevel level, ServerPlayer player, VoidStalkerConfig config, RandomSource random) {
        long nearbyStalkers = level.getEntities(ModEntities.STALKER, player.getBoundingBox().inflate(64), e -> true).size();
        if (nearbyStalkers >= config.maxStalkersNearby) return;

        double angle = random.nextDouble() * Math.PI * 2;
        double dist = 16 + random.nextDouble() * 12;
        double x = player.getX() + Math.cos(angle) * dist;
        double z = player.getZ() + Math.sin(angle) * dist;
        var pos = level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING,
                net.minecraft.core.BlockPos.containing(x, player.getY(), z));

        // Constructed directly rather than via EntityType#create(...) — the
        // exact spawn-reason enum/overload for EntityType.create has moved
        // around between versions, and we have direct access to the class.
        var stalker = new net.voidstalker.entity.custom.StalkerEntity(ModEntities.STALKER, level);
        if (stalker != null) {
            stalker.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, random.nextFloat() * 360f, 0f);
            level.addFreshEntity(stalker);
            level.playSound(null, pos, ModSounds.STALKER_APPEAR, SoundSource.HOSTILE, 0.5f, 1.0f);
        }
    }

    private static void silenceEvent(ServerPlayer player) {
        // There's no clean vanilla/Fabric hook to selectively mute ambient
        // sounds server-side. This sends a custom payload the client mod
        // (see VoidStalkerClient / SilencePayload) receives and uses to duck
        // ambient sound volume for a few seconds on that client only.
        net.voidstalker.network.SilencePayload.sendTo(player, 20 * 6);
    }

    private static void distantSoundEvent(ServerLevel level, ServerPlayer player, RandomSource random) {
        double angle = random.nextDouble() * Math.PI * 2;
        double dist = 12 + random.nextDouble() * 20;
        double x = player.getX() + Math.cos(angle) * dist;
        double z = player.getZ() + Math.sin(angle) * dist;
        level.playSound(null, x, player.getY(), z, ModSounds.DISTANT_WHISPER, SoundSource.AMBIENT, 1.0f, 0.8f + random.nextFloat() * 0.4f);
    }

    private static void voidEvent(ServerLevel level, ServerPlayer player, RandomSource random) {
        level.sendParticles(ParticleTypes.REVERSE_PORTAL,
                player.getX() + (random.nextDouble() - 0.5) * 6,
                player.getY() + 1 + random.nextDouble(),
                player.getZ() + (random.nextDouble() - 0.5) * 6,
                8, 0.2, 0.4, 0.2, 0.01);
        level.playSound(null, player.blockPosition(), ModSounds.RANDOM_HORROR_STING, SoundSource.AMBIENT, 0.4f, 1.0f);
    }
}
