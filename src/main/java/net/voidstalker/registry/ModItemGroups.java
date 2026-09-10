package net.voidstalker.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.voidstalker.VoidStalkerMod;

public class ModItemGroups {

    public static final ResourceKey<CreativeModeTab> VOID_STALKER_TAB = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB,
            ResourceLocation.fromNamespaceAndPath(VoidStalkerMod.MOD_ID, "void_stalker")
    );

    public static void register() {
        CreativeModeTab tab = FabricItemGroup.builder()
                .title(Component.translatable("itemGroup.voidstalker.void_stalker"))
                .icon(() -> new ItemStack(ModItems.STALKER_EYE))
                .displayItems((params, output) -> {
                    output.accept(ModItems.VOID_SHARD);
                    output.accept(ModItems.CORRUPTED_ESSENCE);
                    output.accept(ModItems.STALKER_EYE);

                    output.accept(ModItems.VOID_SWORD);
                    output.accept(ModItems.VOID_PICKAXE);
                    output.accept(ModItems.VOID_AXE);
                    output.accept(ModItems.VOID_SHOVEL);
                    output.accept(ModItems.VOID_HOE);

                    output.accept(ModItems.VOID_HELMET);
                    output.accept(ModItems.VOID_CHESTPLATE);
                    output.accept(ModItems.VOID_LEGGINGS);
                    output.accept(ModItems.VOID_BOOTS);

                    output.accept(ModBlocks.VOID_LANTERN.asItem());
                    output.accept(ModItems.VOID_COMPASS);
                    output.accept(ModItems.ANCIENT_VOID_RELIC);

                    output.accept(ModBlocks.VOID_STONE.asItem());
                    output.accept(ModBlocks.CORRUPTED_STONE.asItem());
                    output.accept(ModBlocks.VOID_CRYSTAL_ORE.asItem());
                    output.accept(ModBlocks.VOID_BRICKS.asItem());
                    output.accept(ModBlocks.ANCIENT_VOID_BLOCK.asItem());

                    output.accept(ModItems.STALKER_SPAWN_EGG);
                    output.accept(ModItems.VOIDLING_SPAWN_EGG);
                    output.accept(ModItems.VOID_BRUTE_SPAWN_EGG);
                    output.accept(ModItems.VOID_WATCHER_SPAWN_EGG);
                })
                .build();

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, VOID_STALKER_TAB, tab);
    }
}
