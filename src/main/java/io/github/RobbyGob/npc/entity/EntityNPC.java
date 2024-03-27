package io.github.RobbyGob.npc.entity;

import io.github.RobbyGob.npc.goal.tryMoveToGoal;
import io.github.RobbyGob.npc.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;

import java.util.logging.Logger;

public class EntityNPC extends PathfinderMob
{

    private Vec3 vec3 = null;
    public EntityNPC(EntityType<EntityNPC> type, Level level) {
        super(type, level);
    }

    public EntityNPC(Level level, double x, double y, double z) {
        this(EntityInit.NPC_ENTITY.get(), level);
        setPos(x, y, z);
    }
    public EntityNPC(Level level, BlockPos position) {
        this(level, position.getX(), position.getY(), position.getZ());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new tryMoveToGoal(this, vec3));
        // leaving this part out, because it is easier to test the tryMoveToGoal and other future goals
        /*
        this.goalSelector.addGoal(1, new TemptGoal(this, 1.5D, Ingredient.of(Items.FISHING_ROD),false));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(3, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(5, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Mob.class, 1, false, true, (p_28879_) -> {
            return p_28879_ instanceof Enemy && !(p_28879_ instanceof Creeper);
        }));
         */
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50D)
                .add(Attributes.FOLLOW_RANGE, 50D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.1f)
                .add(Attributes.ATTACK_KNOCKBACK, 1f)
                .add(Attributes.ATTACK_SPEED, 2f)
                .add(Attributes.ATTACK_DAMAGE, 3f);
    }
    public void updateGoals()
    {
        registerGoals();
    }
    public void setTarget(Vec3 vec3) {
        this.vec3 = vec3;
    }

}
