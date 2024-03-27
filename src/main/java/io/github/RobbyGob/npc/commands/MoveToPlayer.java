package io.github.RobbyGob.npc.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import io.github.RobbyGob.npc.entity.EntityNPC;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.List;

import static net.minecraft.world.level.Level.OVERWORLD;

public class MoveToPlayer {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("npc")
                .then(Commands.literal("moveTo")
                        .then(Commands.literal("player").executes(MoveToPosCommand::moveToPos))));

    }
    public static int moveToPos(CommandContext<CommandSourceStack> command)
    {
        Vec3 vec3 = command.getSource().getEntity().position();
        String destinationString = String.format("x: %.4f; z: %.4f; y: %.4f", vec3.x-0.5, vec3.y, vec3.z-0.5);

        if(command.getSource().getEntity() instanceof Player player) {
            player.sendSystemMessage(Component.literal(destinationString));

            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            ServerLevel world = server.getLevel(OVERWORLD);

            List<EntityNPC> npcList = world.getEntitiesOfClass(EntityNPC.class, new AABB(player.blockPosition()).inflate(100));
            player.sendSystemMessage(Component.literal("Number of NPCs: " + npcList.size()));
            for (EntityNPC npc : npcList) {
                npc.setTarget(vec3);
                npc.updateGoals();
            }
        }

        return Command.SINGLE_SUCCESS;
    }
}
