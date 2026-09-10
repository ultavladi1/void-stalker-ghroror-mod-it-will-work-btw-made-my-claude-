package net.voidstalker.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.voidstalker.VoidStalkerMod;
import net.voidstalker.entity.custom.StalkerEntity;
import net.voidstalker.entity.custom.VoidBruteEntity;
import net.voidstalker.entity.custom.VoidWatcherEntity;
import net.voidstalker.entity.custom.VoidlingEntity;

public class ModEntities {

    public static final EntityType<StalkerEntity> STALKER = register(
            "stalker",
            EntityType.Builder.of(StalkerEntity::new, MobCategory.MONSTER)
                    .sized(0.8f, 3.6f)
                    .clientTrackingRange(12)
                    .updateInterval(2)
                    .fireImmune()
    );

    public static final EntityType<VoidlingEntity> VOIDLING = register(
            "voidling",
            EntityType.Builder.of(VoidlingEntity::new, MobCategory.MONSTER)
                    .sized(0.5f, 0.6f)
                    .clientTrackingRange(8)
    );

    public static final EntityType<VoidBruteEntity> VOID_BRUTE = register(
            "void_brute",
            EntityType.Builder.of(VoidBruteEntity::new, MobCategory.MONSTER)
                    .sized(1.4f, 2.6f)
                    .clientTrackingRange(10)
    );

    public static final EntityType<VoidWatcherEntity> VOID_WATCHER = register(
            "void_watcher",
            EntityType.Builder.of(VoidWatcherEntity::new, MobCategory.MONSTER)
                    .sized(0.9f, 0.9f)
                    .clientTrackingRange(10)
                    .fireImmune()
    );

    private static <T extends net.minecraft.world.entity.Entity> EntityType<T> register(String path, EntityType.Builder<T> builder) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(VoidStalkerMod.MOD_ID, path);
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, id);
        EntityType<T> type = builder.build(key);
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, type);
    }

    public static void register() {
        VoidStalkerMod.LOGGER.info("[Void Stalker] Registered entities");
    }
}
