package net.voidstalker.client;

/**
 * Very small helper the SilencePayload handler calls into. Actually ducking
 * ambient/mob-sound category volume for N ticks would hook into
 * SoundManager / a mixin on volume lookup; left as a clear extension point
 * rather than a guessed-at mixin, since mixin targets are especially likely
 * to have shifted with the recent Vulkan/Blaze3D renderer changes in 26.2.
 */
public class AmbientDucker {
    private static int duckedTicksRemaining = 0;

    public static void duck(int ticks) {
        duckedTicksRemaining = ticks;
    }

    public static boolean isDucked() {
        return duckedTicksRemaining > 0;
    }

    public static void tick() {
        if (duckedTicksRemaining > 0) duckedTicksRemaining--;
    }
}
