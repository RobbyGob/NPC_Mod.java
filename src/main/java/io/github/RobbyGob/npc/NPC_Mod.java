package io.github.RobbyGob.npc;

import com.mojang.brigadier.CommandDispatcher;
import io.github.RobbyGob.npc.commands.SetTryMoveToCommand;
import io.github.RobbyGob.npc.init.EntityInit;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.Commands;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(NPC_Mod.MODID)
public class NPC_Mod {
    public static final String MODID = "npcmod";
    public NPC_Mod(){
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        EntityInit.ENTITIES.register(bus);

    }
}
