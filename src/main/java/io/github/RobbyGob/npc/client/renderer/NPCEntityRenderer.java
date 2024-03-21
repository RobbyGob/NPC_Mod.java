package io.github.RobbyGob.npc.client.renderer;

import io.github.RobbyGob.npc.NPC_Mod;
import io.github.RobbyGob.npc.client.model.EntityNPCModel;
import io.github.RobbyGob.npc.entity.EntityNPC;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;


public class NPCEntityRenderer extends HumanoidMobRenderer<EntityNPC, HumanoidModel<EntityNPC>> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(NPC_Mod.MODID, "assets/npc/textures/entity/npc_skin.png");

    public NPCEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new EntityNPCModel<>(ctx.bakeLayer(EntityNPCModel.LAYER_LOCATION)), 1.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityNPC entity) {
        return TEXTURE;
    }
}
