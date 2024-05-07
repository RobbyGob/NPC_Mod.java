package io.github.RobbyGob.npc.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;;
import io.github.RobbyGob.npc.entity.EntityNPC;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.List;
import java.util.Objects;

import static net.minecraft.world.level.Level.OVERWORLD;

public class ControlCommands {
    private static final double OFFSET_X = 0.5;
    private static final double OFFSET_Z = 0.5;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("npc")
                .then(Commands.literal("moveTo")
                        .then(Commands.argument("destination", Vec3Argument.vec3()).executes(ControlCommands::moveToPos))
                        .then(Commands.literal("player").executes(ControlCommands::moveToPlayer)))
                .then(Commands.literal("pause").executes(ControlCommands::pause))
                .then(Commands.literal("unpause").executes(ControlCommands::unpause)));
    }
    public static int moveToPlayer(CommandContext<CommandSourceStack> command)
    {
        Player player = getPlayer(command.getSource());
        if (player != null) {
            Vec3 playerPosition = player.position();
            sendDestinationMessage(player, playerPosition);

            List<EntityNPC> npcList = getNPCsInRange(Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer().getLevel(OVERWORLD)), player.blockPosition(), 10);
            sendNPCCountMessage(player, npcList.size());
            moveNPCsToTarget(npcList, playerPosition);
        }
        return Command.SINGLE_SUCCESS;
    }
    public static int moveToPos(CommandContext<CommandSourceStack> command) {
        Vec3 destination = Vec3Argument.getVec3(command, "destination");
        Player player = getPlayer(command.getSource());

        if (player != null) {
            sendDestinationMessage(player, destination);
            List<EntityNPC> npcList = getNPCsInRange(Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer().getLevel(OVERWORLD)), player.blockPosition(), 100);
            sendNPCCountMessage(player, npcList.size());
            moveNPCsToTarget(npcList, destination);
        }
        return Command.SINGLE_SUCCESS;
    }
    public static int pause(CommandContext<CommandSourceStack> command) {
        Player player = getPlayer(command.getSource());

        if (player != null) {
            sendControlMessage(player, "Pause");
            controlNPCs(player, false);
        }
        return Command.SINGLE_SUCCESS;
    }
    public static int unpause(CommandContext<CommandSourceStack> command) {
        Player player = getPlayer(command.getSource());

        if (player != null) {
            sendControlMessage(player, "Unpause");
            controlNPCs(player, true);
        }
        return Command.SINGLE_SUCCESS;
    }
    private static void controlNPCs(Player player, boolean continueNPCs) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        ServerLevel world = server.getLevel(OVERWORLD);

        List<EntityNPC> npcList = getNPCsInRange(world, player.blockPosition(), 10);
        sendNPCCountMessage(player, npcList.size());

        for (EntityNPC npc : npcList) {
            if (continueNPCs) {
                npc.continueNPC();
            } else {
                npc.stopNPC();
            }
        }
    }
    private static Player getPlayer(CommandSourceStack source) {
        Entity entity = source.getEntity();
        return (entity instanceof Player) ? (Player) entity : null;
    }
    private static void sendDestinationMessage(Player player, Vec3 position) {
        String destinationString = String.format("x: %.4f; z: %.4f; y: %.4f", position.x - OFFSET_X, position.y, position.z - OFFSET_Z);
        player.sendSystemMessage(Component.literal(destinationString));
    }
    private static List<EntityNPC> getNPCsInRange(ServerLevel world, BlockPos playerPosition, double range) {
        return world.getEntitiesOfClass(EntityNPC.class, new AABB(playerPosition).inflate(range));
    }
    private static void sendNPCCountMessage(Player player, int count) {
        player.sendSystemMessage(Component.literal("Number of NPCs: " + count));
    }
    private static void sendControlMessage(Player player, String message) {
        player.sendSystemMessage(Component.literal(message));
    }
    static void moveNPCsToTarget(List<EntityNPC> npcList, Vec3 target) {
        for (EntityNPC npc : npcList) {
            npc.setNewTarget(target);
        }
    }
}
