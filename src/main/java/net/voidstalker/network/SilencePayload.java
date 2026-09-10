package net.voidstalker.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.voidstalker.VoidStalkerMod;

public record SilencePayload(int ticks) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SilencePayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(VoidStalkerMod.MOD_ID, "silence"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SilencePayload> CODEC = StreamCodec.composite(
            net.minecraft.network.codec.ByteBufCodecs.VAR_INT, SilencePayload::ticks,
            SilencePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void registerC2SAndS2C() {
        PayloadTypeRegistry.playS2C().register(TYPE, CODEC);
    }

    public static void sendTo(ServerPlayer player, int ticks) {
        ServerPlayNetworking.send(player, new SilencePayload(ticks));
    }
}
