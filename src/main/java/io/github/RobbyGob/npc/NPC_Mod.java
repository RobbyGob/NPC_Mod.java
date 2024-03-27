package io.github.RobbyGob.npc;

import io.github.RobbyGob.npc.init.EntityInit;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(NPC_Mod.MODID)
public class NPC_Mod {
    public static final String MODID = "npcmod";
    public NPC_Mod(){
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        EntityInit.ENTITIES.register(bus);
        //testing
    }
}
