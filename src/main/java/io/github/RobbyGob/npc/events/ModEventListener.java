package io.github.RobbyGob.npc.events;

import io.github.RobbyGob.npc.NPC_Mod;
import io.github.RobbyGob.npc.commands.MoveToPlayer;
import io.github.RobbyGob.npc.commands.MoveToPosCommand;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = NPC_Mod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEventListener {
    @SubscribeEvent
    public static void registerClientCommands(RegisterClientCommandsEvent event) {
        MoveToPosCommand.register(event.getDispatcher());
        //MoveToPlayer.register(event.getDispatcher());
    }
}
