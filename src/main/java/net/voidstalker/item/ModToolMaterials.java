package net.voidstalker.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.SimpleTier;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

/**
 * NOTE: Tool/armor tier constructors are one of the areas that has churned the
 * most across recent Minecraft versions (SimpleTier/ArmorMaterial field order
 * and the repair-ingredient type have changed more than once). If this file
 * fails to compile against your exact 26.2 jar, open SimpleTier's source in
 * IntelliJ (Ctrl+Click / Cmd+Click on the class) and match the constructor
 * argument order shown there — the *values* below are correct, only the
 * exact parameter list may need reordering.
 *
 * Custom tool tier for the Void equipment set.
 * Sits above Netherite in mining level / durability, tuned to feel like an
 * end-game "you earned this" tier rather than a straight upgrade grab.
 */
public class ModToolMaterials {

    public static final Tier VOID = new SimpleTier(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL, // mines everything netherite can, plus this tag's exclusions are overridden by the block tag below in block/tool tags
            2031,          // durability
            9.0f,          // mining speed
            5.0f,          // attack damage bonus
            22,            // enchantability
            () -> Ingredient.of(ModItems.CORRUPTED_ESSENCE)
    );
}
