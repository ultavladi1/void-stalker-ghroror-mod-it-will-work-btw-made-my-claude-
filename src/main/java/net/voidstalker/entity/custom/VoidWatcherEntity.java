package net.voidstalker.entity.custom;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.EnumSet;

/**
 * A floating, very rare creature. Rather than a projectile entity (extra
 * registration + client rendering work), its "unusual attack" is a creeping
 * Blindness/Nausea field it inflicts on players who linger too close while
 * it stares at them — an unsettling effect rather than raw damage.
 */
public class VoidWatcherEntity extends Monster {

    public VoidWatcherEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
        this.moveControl = new net.minecraft.world.entity.ai.control.FlyingMoveControl(this, 20, true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 18.0)
                .add(Attributes.MOVEMENT_SPEED, 0.1)
                .add(Attributes.FLYING_SPEED, 0.1)
                .add(Attributes.FOLLOW_RANGE, 32.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 24.0f));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new StareDreadGoal(this));
    }

    @Override
    public net.minecraft.world.entity.ai.navigation.PathNavigation createNavigation(Level level) {
        net.minecraft.world.entity.ai.navigation.FlyingPathNavigation navigation =
                new net.minecraft.world.entity.ai.navigation.FlyingPathNavigation(this, level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        return navigation;
    }

    /** Slowly builds dread on a nearby player instead of dealing melee damage. */
    private static class StareDreadGoal extends Goal {
        private final VoidWatcherEntity watcher;
        private int cooldown;

        StareDreadGoal(VoidWatcherEntity watcher) {
            this.watcher = watcher;
            this.setFlags(EnumSet.noneOf(Goal.Flag.class));
        }

        @Override
        public boolean canUse() {
            return watcher.getTarget() != null || watcher.level().getNearestPlayer(watcher, 10.0) != null;
        }

        @Override
        public void tick() {
            if (cooldown-- > 0) return;
            Player player = watcher.level().getNearestPlayer(watcher, 6.0);
            if (player != null) {
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0));
                cooldown = 100;
            }
        }
    }
}
