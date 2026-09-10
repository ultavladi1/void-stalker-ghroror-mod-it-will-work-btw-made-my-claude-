package net.voidstalker.client.render;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.ResourceLocation;
import net.voidstalker.entity.custom.StalkerEntity;

/**
 * Renders the Stalker by reusing vanilla's humanoid model + zombie render
 * state (which is populated generically from any LivingEntity, not
 * specifically zombies) with our own texture and a taller vertical scale.
 * This is the pragmatic option: a truly bespoke "very tall, faceless"
 * geometry needs a real modeled skeleton, which isn't something that can be
 * generated as code — see README for how to swap in a custom ModelLayerLocation
 * once you've made one (e.g. in Blockbench).
 */
public class StalkerRenderer extends HumanoidMobRenderer<StalkerEntity, ZombieRenderState, HumanoidModel<ZombieRenderState>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("voidstalker", "textures/entity/stalker.png");

    public StalkerRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.ZOMBIE)), 0.5f);
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
    public void extractRenderState(StalkerEntity entity, ZombieRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        // Rendered ~1.8x taller/thinner via the model transform below; the
        // hitbox set in ModEntities already reflects a tall, narrow entity.
    }

    @Override
    protected void scale(ZombieRenderState state, com.mojang.blaze3d.vertex.PoseStack poseStack) {
        poseStack.scale(0.8f, 1.9f, 0.8f);
    }
}
