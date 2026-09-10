package net.voidstalker.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * A rare, mysterious relic. Grants brief Night Vision + Resistance when used
 * (it "protects" the holder for a moment) with a long cooldown, so it's a
 * situational panic button rather than a permanent buff stick.
 */
public class AncientVoidRelicItem extends Item {

    private static final int COOLDOWN_TICKS = 20 * 60 * 3; // 3 minutes

    public AncientVoidRelicItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        }

        if (!level.isClientSide) {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 20 * 30, 0));
            player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 20 * 10, 1));
            level.playSound(null, player.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 0.6f, 1.4f);
            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
