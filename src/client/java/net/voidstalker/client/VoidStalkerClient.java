package net.voidstalker.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.voidstalker.client.render.VoidBruteRenderer;
import net.voidstalker.client.render.VoidWatcherRenderer;
import net.voidstalker.client.render.VoidlingRenderer;
import net.voidstalker.client.render.StalkerRenderer;
import net.voidstalker.network.SilencePayload;
import net.voidstalker.registry.ModEntities;

public class VoidStalkerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.STALKER, StalkerRenderer::new);
        EntityRendererRegistry.register(ModEntities.VOIDLING, VoidlingRenderer::new);
        EntityRendererRegistry.register(ModEntities.VOID_BRUTE, VoidBruteRenderer::new);
        EntityRendererRegistry.register(ModEntities.VOID_WATCHER, VoidWatcherRenderer::new);

        ClientPlayNetworking.registerGlobalReceiver(SilencePayload.TYPE, (payload, context) -> {
            context.client().execute(() -> AmbientDucker.duck(payload.ticks()));
        });
    }
}
