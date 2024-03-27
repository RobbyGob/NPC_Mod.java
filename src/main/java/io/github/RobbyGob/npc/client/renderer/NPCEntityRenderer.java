package io.github.RobbyGob.npc.client.renderer;

import io.github.RobbyGob.npc.NPC_Mod;
import io.github.RobbyGob.npc.client.model.EntityNPCModel;
import io.github.RobbyGob.npc.entity.EntityNPC;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;


public class NPCEntityRenderer extends HumanoidMobRenderer<EntityNPC, HumanoidModel<EntityNPC>> {

    //Does not load the textures of the NPC. NEEDS FIXING, but god-damn I can't find the solution
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(NPC_Mod.MODID, "assets/npc/textures/entity/npc_skin.png");
/*Testing grounds*/
    public NPCEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new EntityNPCModel<>(ctx.bakeLayer(EntityNPCModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityNPC entity) {
        return TEXTURE;
    }
}
