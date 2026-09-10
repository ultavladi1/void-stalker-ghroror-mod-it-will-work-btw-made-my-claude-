package net.voidstalker.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.core.Registry;
import net.voidstalker.VoidStalkerMod;
import net.voidstalker.item.AncientVoidRelicItem;
import net.voidstalker.item.ModArmorMaterials;
import net.voidstalker.item.ModToolMaterials;
import net.voidstalker.item.VoidCompassItem;

public class ModItems {

    // --- Materials ---
    public static final Item VOID_SHARD = register("void_shard", Item::new, new Item.Properties());
    public static final Item CORRUPTED_ESSENCE = register("corrupted_essence", Item::new, new Item.Properties());
    public static final Item STALKER_EYE = register("stalker_eye", Item::new, new Item.Properties().rarity(net.minecraft.world.item.Rarity.EPIC));

    // --- Void tool/weapon set ---
    public static final Item VOID_SWORD = register("void_sword", p -> new SwordItem(ModToolMaterials.VOID, p), new Item.Properties().attributes(SwordItem.createAttributes(ModToolMaterials.VOID, 3, -2.4f)));
    public static final Item VOID_PICKAXE = register("void_pickaxe", p -> new PickaxeItem(ModToolMaterials.VOID, p), new Item.Properties().attributes(PickaxeItem.createAttributes(ModToolMaterials.VOID, 1, -2.8f)));
    public static final Item VOID_AXE = register("void_axe", p -> new AxeItem(ModToolMaterials.VOID, p), new Item.Properties().attributes(AxeItem.createAttributes(ModToolMaterials.VOID, 6.0f, -3.1f)));
    public static final Item VOID_SHOVEL = register("void_shovel", p -> new ShovelItem(ModToolMaterials.VOID, p), new Item.Properties().attributes(ShovelItem.createAttributes(ModToolMaterials.VOID, 1.5f, -3.0f)));
    public static final Item VOID_HOE = register("void_hoe", p -> new HoeItem(ModToolMaterials.VOID, p), new Item.Properties().attributes(HoeItem.createAttributes(ModToolMaterials.VOID, -2, 1.0f)));

    // --- Void armor set ---
    public static final Item VOID_HELMET = register("void_helmet", p -> new ArmorItem(ModArmorMaterials.VOID, ArmorItem.Type.HELMET, p), new Item.Properties());
    public static final Item VOID_CHESTPLATE = register("void_chestplate", p -> new ArmorItem(ModArmorMaterials.VOID, ArmorItem.Type.CHESTPLATE, p), new Item.Properties());
    public static final Item VOID_LEGGINGS = register("void_leggings", p -> new ArmorItem(ModArmorMaterials.VOID, ArmorItem.Type.LEGGINGS, p), new Item.Properties());
    public static final Item VOID_BOOTS = register("void_boots", p -> new ArmorItem(ModArmorMaterials.VOID, ArmorItem.Type.BOOTS, p), new Item.Properties());

    // --- Extra items ---
    public static final Item VOID_COMPASS = register("void_compass", VoidCompassItem::new, new Item.Properties().stacksTo(1));
    public static final Item ANCIENT_VOID_RELIC = register("ancient_void_relic", AncientVoidRelicItem::new, new Item.Properties().stacksTo(1).rarity(net.minecraft.world.item.Rarity.RARE));

    // --- Spawn eggs (creative-tab / testing convenience) ---
    // Recent versions moved spawn-egg base/highlight colors out of this
    // constructor and into a two-layer item texture instead (see the
    // *_spawn_egg item model note in the README). If your 26.2 jar still
    // exposes a color-taking SpawnEggItem constructor, either is fine.
    public static final Item STALKER_SPAWN_EGG = registerSpawnEgg("stalker_spawn_egg", ModEntities.STALKER);
    public static final Item VOIDLING_SPAWN_EGG = registerSpawnEgg("voidling_spawn_egg", ModEntities.VOIDLING);
    public static final Item VOID_BRUTE_SPAWN_EGG = registerSpawnEgg("void_brute_spawn_egg", ModEntities.VOID_BRUTE);
    public static final Item VOID_WATCHER_SPAWN_EGG = registerSpawnEgg("void_watcher_spawn_egg", ModEntities.VOID_WATCHER);

    public static <T extends Item> T register(String path, java.util.function.Function<Item.Properties, T> factory, Item.Properties properties) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(VoidStalkerMod.MOD_ID, path);
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
        T item = factory.apply(properties.setId(key));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    private static Item registerSpawnEgg(String path, EntityType<?> type) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(VoidStalkerMod.MOD_ID, path);
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
        SpawnEggItem egg = new SpawnEggItem((EntityType<? extends net.minecraft.world.entity.Mob>) type, new Item.Properties().setId(key));
        return Registry.register(BuiltInRegistries.ITEM, key, egg);
    }

    public static void register() {
        VoidStalkerMod.LOGGER.info("[Void Stalker] Registered items");
    }
}
