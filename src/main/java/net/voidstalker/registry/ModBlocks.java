package net.voidstalker.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.voidstalker.VoidStalkerMod;

public class ModBlocks {

    public static final Block VOID_STONE = register(
            "void_stone",
            props -> new Block(props),
            BlockBehaviour.Properties.of()
                    .strength(2.0f, 6.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.DEEPSLATE),
            true
    );

    public static final Block CORRUPTED_STONE = register(
            "corrupted_stone",
            props -> new Block(props),
            BlockBehaviour.Properties.of()
                    .strength(2.5f, 6.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.DEEPSLATE)
                    .lightLevel(state -> 2),
            true
    );

    public static final Block VOID_CRYSTAL_ORE = register(
            "void_crystal_ore",
            props -> new DropExperienceBlock(UniformInt.of(3, 7), props),
            BlockBehaviour.Properties.of()
                    .strength(4.5f, 8.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.AMETHYST_CLUSTER)
                    .lightLevel(state -> 6),
            true
    );

    public static final Block VOID_BRICKS = register(
            "void_bricks",
            props -> new Block(props),
            BlockBehaviour.Properties.of()
                    .strength(3.0f, 6.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE),
            true
    );

    public static final Block ANCIENT_VOID_BLOCK = register(
            "ancient_void_block",
            props -> new Block(props),
            BlockBehaviour.Properties.of()
                    .strength(50.0f, 1200.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.NETHERITE_BLOCK)
                    .lightLevel(state -> 4),
            true
    );

    public static final Block VOID_LANTERN = register(
            "void_lantern",
            props -> new net.minecraft.world.level.block.LanternBlock(props),
            BlockBehaviour.Properties.of()
                    .strength(0.5f)
                    .sound(SoundType.LANTERN)
                    .lightLevel(state -> 15)
                    .noOcclusion(),
            true
    );

    private interface BlockFactory {
        Block create(BlockBehaviour.Properties props);
    }

    private static Block register(String path, BlockFactory factory, BlockBehaviour.Properties properties, boolean withItem) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(VoidStalkerMod.MOD_ID, path);
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, id);
        Block block = factory.create(properties.setId(key));
        Registry.register(BuiltInRegistries.BLOCK, key, block);

        if (withItem) {
            ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);
            Item.Properties itemProps = new Item.Properties().setId(itemKey);
            Registry.register(BuiltInRegistries.ITEM, itemKey, new BlockItem(block, itemProps));
        }

        return block;
    }

    public static void register() {
        VoidStalkerMod.LOGGER.info("[Void Stalker] Registered blocks");
    }
}
