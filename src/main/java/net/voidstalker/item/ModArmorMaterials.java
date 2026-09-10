package net.voidstalker.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.voidstalker.VoidStalkerMod;

import java.util.EnumMap;
import java.util.Map;

/**
 * NOTE (see also ModToolMaterials): armor materials moved to a fully
 * data-driven "equipment asset" system in recent versions. This still needs:
 *   assets/voidstalker/equipment/void.json
 * describing the layer textures. See the README for its exact contents and
 * the texture files it points to. If ArmorMaterial's constructor shape
 * differs slightly on your exact build, Ctrl+Click into ArmorMaterial to
 * check field order.
 */
public class ModArmorMaterials {

    public static final ResourceKey<EquipmentAsset> VOID_EQUIPMENT_ASSET = ResourceKey.create(
            Registries.EQUIPMENT_ASSET,
            ResourceLocation.fromNamespaceAndPath(VoidStalkerMod.MOD_ID, "void")
    );

    public static final ArmorMaterial VOID = new ArmorMaterial(
            13, // durability multiplier, roughly between diamond (33) and turtle-shell tier balanced down since Void is craft-gated, not tier-gated
            defenseMap(3, 6, 8, 3), // boots, leggings, chestplate, helmet
            18, // enchantment value
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            2.5f,  // toughness
            0.1f,  // knockback resistance
            () -> Ingredient.of(ModItems.STALKER_EYE),
            VOID_EQUIPMENT_ASSET
    );

    private static Map<ArmorItem.Type, Integer> defenseMap(int boots, int leggings, int chestplate, int helmet) {
        Map<ArmorItem.Type, Integer> map = new EnumMap<>(ArmorItem.Type.class);
        map.put(ArmorItem.Type.BOOTS, boots);
        map.put(ArmorItem.Type.LEGGINGS, leggings);
        map.put(ArmorItem.Type.CHESTPLATE, chestplate);
        map.put(ArmorItem.Type.HELMET, helmet);
        map.put(ArmorItem.Type.BODY, chestplate); // for mob-worn body armor slot, if applicable
        return map;
    }
}
