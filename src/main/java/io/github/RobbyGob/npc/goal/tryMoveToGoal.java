package io.github.RobbyGob.npc.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

public class tryMoveToGoal extends Goal {
    private final PathfinderMob mob;
    private final double xTarget, yTarget, zTarget, range;
    /**
     *
     * @param mob the mob that will be walking to the target coordinate
     * @param x the x coordinate of the target
     * @param y the y coordinate of the target
     * @param z the z coordinate of the target
     * @param range how far away the mob is allowed to be from the target coordinates
     */
    public tryMoveToGoal(PathfinderMob mob, double x, double y, double z, double range) {
        this.mob = mob;
        this.xTarget = x;
        this.yTarget = y;
        this.zTarget = z;
        this.range = range;
    }

    /**
     * Checks if the target x, y, z coordinates are within the range of the npc
     * @return true if outside range, false if within the range
     */
    @Override
    public boolean canUse() {
        return !(Math.abs(mob.getX() - xTarget) <= range) || !(Math.abs(mob.getY() - zTarget) <= range) || !(Math.abs(mob.getZ() - yTarget) <= range);
    }

    public void tick() {
        this.mob.getNavigation().moveTo(xTarget, zTarget, yTarget, 1.2f);
    }
}
