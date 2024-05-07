package io.github.RobbyGob.npc.events;

import io.github.RobbyGob.npc.NPC_Mod;
import io.github.RobbyGob.npc.commands.ControlCommands;
import io.github.RobbyGob.npc.test.GameTests;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.event.RegisterGameTestsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = NPC_Mod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEventListener {
    @SubscribeEvent
    public static void registerClientCommands(RegisterClientCommandsEvent event) {
        ControlCommands.register(event.getDispatcher());
    }
/*
    @SubscribeEvent
    public static void onRegisterGameTests(RegisterGameTestsEvent event) {
        event.register(GameTests.class);
    }

 */
}
