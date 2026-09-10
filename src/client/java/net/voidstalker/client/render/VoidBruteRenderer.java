package net.voidstalker.client.render;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.ResourceLocation;
import net.voidstalker.entity.custom.VoidBruteEntity;

public class VoidBruteRenderer extends HumanoidMobRenderer<VoidBruteEntity, ZombieRenderState, HumanoidModel<ZombieRenderState>> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("voidstalker", "textures/entity/void_brute.png");

    public VoidBruteRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.ZOMBIE)), 0.6f);
    }

    @Override
    public ZombieRenderState createRenderState() {
        return new ZombieRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(ZombieRenderState state) {
        return TEXTURE;
    }

    @Override
    protected void scale(ZombieRenderState state, com.mojang.blaze3d.vertex.PoseStack poseStack) {
        poseStack.scale(1.6f, 1.5f, 1.6f);
    }
}
