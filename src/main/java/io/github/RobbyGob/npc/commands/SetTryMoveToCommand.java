package io.github.RobbyGob.npc.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.RobbyGob.npc.entity.EntityNPC;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static net.minecraft.world.level.Level.OVERWORLD;

public class SetTryMoveToCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("npc")
                .then(Commands.literal("moveTo")
                        .then(Commands.argument("destination", Vec3Argument.vec3()).executes(SetTryMoveToCommand::moveToPos))));

    }
    public static int moveToPos(CommandContext<CommandSourceStack> command)
    {
        Vec3 vec3 = Vec3Argument.getVec3(command, "destination");
        String destinationString = String.format("x: %.0f; z: %.0f; y: %.0f", vec3.x-0.5, vec3.y, vec3.z-0.5);

        if(command.getSource().getEntity() instanceof Player player) {
            player.sendSystemMessage(Component.literal(destinationString));

            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            ServerLevel world = server.getLevel(OVERWORLD);

            List<EntityNPC> npcList = world.getEntitiesOfClass(EntityNPC.class, new AABB(player.blockPosition()).inflate(100));
            player.sendSystemMessage(Component.literal("Number of NPCs: " + npcList.size()));
            for (EntityNPC npc : npcList) {
                npc.setXTarget(vec3.x);
                npc.setYTarget(vec3.y);
                npc.setZTarget(vec3.z);
                npc.updateGoals();
            }
        }

        return Command.SINGLE_SUCCESS;
    }
}
