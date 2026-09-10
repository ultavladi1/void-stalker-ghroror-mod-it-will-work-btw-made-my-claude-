package net.voidstalker.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

/**
 * Points (via a chat hint, not a rotating needle model — that would need a
 * custom item model with an angle property, see README) toward the nearest
 * generated Void structure the game currently knows about.
 *
 * Simplified on purpose: real "nearest structure" search across ungenerated
 * chunks is expensive. This checks structures in already-generated chunks
 * around the player, which is enough for a horror-flavored hint item.
 */
public class VoidCompassItem extends Item {

    public static final TagKey<Structure> VOID_STRUCTURES = TagKey.create(
            Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath("voidstalker", "void_structures"));

    public VoidCompassItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, net.minecraft.world.entity.player.Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level instanceof ServerLevel serverLevel) {
            BlockPos origin = player.blockPosition();
            Optional<BlockPos> nearest = findNearestVoidStructure(serverLevel, origin);
            if (nearest.isPresent()) {
                BlockPos pos = nearest.get();
                double dx = pos.getX() - origin.getX();
                double dz = pos.getZ() - origin.getZ();
                String direction = describeDirection(dx, dz);
                double dist = Math.sqrt(dx * dx + dz * dz);
                player.displayClientMessage(Component.translatable("item.voidstalker.void_compass.hint", direction, (int) dist), true);
            } else {
                player.displayClientMessage(Component.translatable("item.voidstalker.void_compass.nothing"), true);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    private Optional<BlockPos> findNearestVoidStructure(ServerLevel level, BlockPos origin) {
        try {
            var registry = level.registryAccess().lookupOrThrow(Registries.STRUCTURE);
            var holders = registry.getTagOrEmpty(VOID_STRUCTURES);
            BlockPos best = null;
            double bestDist = Double.MAX_VALUE;
            for (var holder : holders) {
                Structure structure = holder.value();
                BlockPos found = level.getChunkSource().getGenerator()
                        .findNearestMapStructure(level, net.minecraft.core.HolderSet.direct(holder), origin, 100, false);
                if (found != null) {
                    double d = origin.distSqr(found);
                    if (d < bestDist) {
                        bestDist = d;
                        best = found;
                    }
                }
            }
            return Optional.ofNullable(best);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private String describeDirection(double dx, double dz) {
        double angle = Math.toDegrees(Math.atan2(dx, -dz));
        if (angle < 0) angle += 360;
        String[] dirs = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
        int index = (int) Math.round(angle / 45.0) % 8;
        return dirs[index];
    }
}
