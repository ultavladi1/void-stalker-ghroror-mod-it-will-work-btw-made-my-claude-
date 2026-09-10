package net.voidstalker.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.voidstalker.config.VoidStalkerConfig;
import net.voidstalker.registry.ModSounds;

/**
 * The Stalker: a rare, mostly-passive horror entity. It does not chase the
 * player in the usual sense. It watches, sometimes stands dead still,
 * sometimes teleports to reposition itself, and vanishes rather than being
 * "defeated" when it decides an encounter is over.
 */
public class StalkerEntity extends Monster {

    private int watchTicks;
    private int nextDecisionTick;

    public StalkerEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.xpReward = 0;
        this.setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.MOVEMENT_SPEED, 0.18)
                .add(Attributes.ATTACK_DAMAGE, VoidStalkerConfig.get().stalkerAggressive ? 8.0 : 0.0)
                .add(Attributes.FOLLOW_RANGE, 48.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 32.0f));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
        // Deliberately no wander/attack goals by default: the Stalker mostly
        // holds still or repositions itself via teleportation in customServerAiStep.
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            this.watchTicks++;
        }
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        super.customServerAiStep(level);

        if (this.tickCount < this.nextDecisionTick) {
            return;
        }

        RandomSource random = this.random;
        // Roll again in 3-9 seconds.
        this.nextDecisionTick = this.tickCount + 60 + random.nextInt(120);

        Player nearest = level.getNearestPlayer(this, 40.0);
        if (nearest == null) {
            return;
        }

        double distance = this.distanceTo(nearest);

        if (distance < VoidStalkerConfig.get().stalkerVanishDistance && random.nextFloat() < 0.6f) {
            vanish(level);
            return;
        }

        if (random.nextFloat() < 0.12f) {
            teleportNear(level, nearest);
        }

        if (random.nextFloat() < VoidStalkerConfig.get().darknessEventChance) {
            triggerDarkness(nearest);
        }

        if (random.nextFloat() < 0.05f) {
            level.playSound(null, this.blockPosition(), ModSounds.STALKER_AMBIENT, SoundSource.HOSTILE, 0.8f, 0.9f + random.nextFloat() * 0.2f);
        }
    }

    private void teleportNear(ServerLevel level, Player target) {
        RandomSource random = this.random;
        for (int attempt = 0; attempt < 8; attempt++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double radius = 8 + random.nextDouble() * 10;
            double x = target.getX() + Math.cos(angle) * radius;
            double z = target.getZ() + Math.sin(angle) * radius;
            BlockPos pos = level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, BlockPos.containing(x, target.getY(), z));

            if (this.randomTeleport(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, false)) {
                level.sendParticles(ParticleTypes.PORTAL, this.getX(), this.getY() + 1.0, this.getZ(), 20, 0.3, 0.5, 0.3, 0.02);
                level.playSound(null, this.blockPosition(), ModSounds.STALKER_TELEPORT, SoundSource.HOSTILE, 0.7f, 1.0f);
                return;
            }
        }
    }

    private void triggerDarkness(Player target) {
        target.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 70, 0, false, false));
    }

    private void vanish(ServerLevel level) {
        level.sendParticles(ParticleTypes.SMOKE, this.getX(), this.getY() + 1.2, this.getZ(), 30, 0.4, 0.8, 0.4, 0.03);
        level.playSound(null, this.blockPosition(), ModSounds.STALKER_TELEPORT, SoundSource.HOSTILE, 0.6f, 0.6f);
        this.discard();
    }

    @Override
    public void checkDespawn() {
        // Despawns readily; this entity is meant to be glimpsed, not fought.
        if (this.watchTicks > 20 * 60 * 5) { // 5 minutes of existing
            this.discard();
            return;
        }
        super.checkDespawn();
    }

    @Override
    protected void playHurtSound(net.minecraft.world.damagesource.DamageSource source) {
        // The Stalker does not react to damage the way normal mobs do.
    }

    @Override
    protected float getSoundVolume() {
        return 0.5f;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return true;
    }
}
