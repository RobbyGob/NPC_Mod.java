package io.github.RobbyGob.npc.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class tryMoveToGoal extends Goal {
    private final PathfinderMob mob;
    private  Vec3 vec3;
    private Boolean isStopped;

    public tryMoveToGoal(PathfinderMob mobInput, Vec3 vec3Input, boolean isStoppedInput) {
        this.mob = mobInput;
        this.vec3 = vec3Input;
        this.isStopped = isStoppedInput;
    }

    @Override
    public boolean canUse() {
        return vec3 != null;
    }

    public void tick() {
        if(isStopped){
            this.mob.getNavigation().stop();
        }
        else
        {
            this.mob.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, 1.5f);
        }
    }
}
