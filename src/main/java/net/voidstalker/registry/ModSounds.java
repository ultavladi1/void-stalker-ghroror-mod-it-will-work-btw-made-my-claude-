package net.voidstalker.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.voidstalker.VoidStalkerMod;

/**
 * Custom sound events. Each of these needs a matching entry in
 * assets/voidstalker/sounds.json and an .ogg file at the referenced path.
 * See the project README for the exact list of audio files to add.
 */
public class ModSounds {
    public static final SoundEvent STALKER_APPEAR = register("stalker_appear");
    public static final SoundEvent STALKER_AMBIENT = register("stalker_ambient");
    public static final SoundEvent STALKER_TELEPORT = register("stalker_teleport");
    public static final SoundEvent DISTANT_WHISPER = register("distant_whisper");
    public static final SoundEvent VOIDLING_AMBIENT = register("voidling_ambient");
    public static final SoundEvent VOID_BRUTE_AMBIENT = register("void_brute_ambient");
    public static final SoundEvent VOID_WATCHER_AMBIENT = register("void_watcher_ambient");
    public static final SoundEvent SHRINE_AMBIENT = register("shrine_ambient");
    public static final SoundEvent RANDOM_HORROR_STING = register("random_horror_sting");

    private static SoundEvent register(String path) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(VoidStalkerMod.MOD_ID, path);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void register() {
        // Static initializers above run on class load.
    }
}
