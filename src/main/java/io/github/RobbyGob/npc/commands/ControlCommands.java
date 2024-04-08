package io.github.RobbyGob.npc.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;;
import io.github.RobbyGob.npc.entity.EntityNPC;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.List;
import java.util.function.Predicate;

import static net.minecraft.world.level.Level.OVERWORLD;

public class ControlCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("npc")
                .then(Commands.literal("moveTo")
                        .then(Commands.argument("destination", Vec3Argument.vec3()).executes(ControlCommands::moveToPos))
                        .then(Commands.literal("player").executes(ControlCommands::moveToPlayer))
                        .then(Commands.literal("stop").executes(ControlCommands::stop)))
                .then(Commands.literal("pause").executes(ControlCommands::pause))
                .then(Commands.literal("unpause").executes(ControlCommands::unpause)));
    }
    public static int moveToPlayer(CommandContext<CommandSourceStack> command)
    {
        if(command.getSource().getEntity() instanceof Player player) {
            Vec3 vec3 = player.position();
            String destinationString = String.format("x: %.4f; z: %.4f; y: %.4f", vec3.x-0.5, vec3.y, vec3.z-0.5);
            player.sendSystemMessage(Component.literal(destinationString));

            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            ServerLevel world = server.getLevel(OVERWORLD);

            List<EntityNPC> npcList = world.getEntitiesOfClass(EntityNPC.class, new AABB(player.blockPosition()).inflate(100));
            player.sendSystemMessage(Component.literal("Number of NPCs: " + npcList.size()));
            for (EntityNPC npc : npcList) {
                npc.setNewTarget(vec3);
            }
        }

        return Command.SINGLE_SUCCESS;
    }
    public static int moveToPos(CommandContext<CommandSourceStack> command)
    {
        Vec3 vec3 = Vec3Argument.getVec3(command, "destination");
        String destinationString = String.format("x: %.4f; z: %.4f; y: %.4f", vec3.x-0.5, vec3.y, vec3.z-0.5);

        if(command.getSource().getEntity() instanceof Player player) {
            player.sendSystemMessage(Component.literal(destinationString));

            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            ServerLevel world = server.getLevel(OVERWORLD);

            List<EntityNPC> npcList = world.getEntitiesOfClass(EntityNPC.class, new AABB(player.blockPosition()).inflate(100));
            player.sendSystemMessage(Component.literal("Number of NPCs: " + npcList.size()));
            for (EntityNPC npc : npcList) {
                npc.setNewTarget(vec3);
            }
        }

        return Command.SINGLE_SUCCESS;
    }
    public static int pause(CommandContext<CommandSourceStack> command)
    {

        if(command.getSource().getEntity() instanceof Player player) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            ServerLevel world = server.getLevel(OVERWORLD);
            List<EntityNPC> npcList = world.getEntitiesOfClass(EntityNPC.class, new AABB(player.blockPosition()).inflate(100));

            player.sendSystemMessage(Component.literal("Pause"));
            player.sendSystemMessage(Component.literal("Number of NPCs: " + npcList.size()));

            for (EntityNPC npc : npcList) {
                npc.stopNPC();
            }
        }
        return Command.SINGLE_SUCCESS;
    }
    public static int unpause(CommandContext<CommandSourceStack> command)
    {
        if(command.getSource().getEntity() instanceof Player player) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            ServerLevel world = server.getLevel(OVERWORLD);
            List<EntityNPC> npcList = world.getEntitiesOfClass(EntityNPC.class, new AABB(player.blockPosition()).inflate(100));

            player.sendSystemMessage(Component.literal("Unpause"));
            player.sendSystemMessage(Component.literal("Number of NPCs: " + npcList.size()));

            for (EntityNPC npc : npcList) {
                npc.continueNPC();
            }
        }
        return Command.SINGLE_SUCCESS;
    }
    public static int stop(CommandContext<CommandSourceStack> command)
    {
        if(command.getSource().getEntity() instanceof Player player) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            ServerLevel world = server.getLevel(OVERWORLD);
            List<EntityNPC> npcList = world.getEntitiesOfClass(EntityNPC.class, new AABB(player.blockPosition()).inflate(100));

            player.sendSystemMessage(Component.literal("Stop"));
            player.sendSystemMessage(Component.literal("Number of NPCs: " + npcList.size()));

            for (EntityNPC npc : npcList) {

            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
