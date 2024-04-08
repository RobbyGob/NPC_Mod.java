package io.github.RobbyGob.npc.entity;

import io.github.RobbyGob.npc.entity.inventory.NPCInventory;
import io.github.RobbyGob.npc.goal.tryMoveToGoal;
import io.github.RobbyGob.npc.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;

import java.util.ArrayList;
import java.util.Collection;

public class EntityNPC extends PathfinderMob
{
    private final NPCInventory inventory;
    private Vec3 vec3 = null;
    private boolean isStopped = false;

    public EntityNPC(EntityType<EntityNPC> type, Level level) {
        super(type, level);
        this.inventory = new NPCInventory(this);
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
        this.goalSelector.addGoal(1, new tryMoveToGoal(this, vec3, isStopped));
        /* leaving this part out, because it is easier to test the tryMoveToGoal and other future goals

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
    public void setNewTarget(Vec3 vec3) {
        this.vec3 = vec3;
        registerGoals();
    }
    public void stopNPC()
    {
        this.isStopped = true;
        registerGoals();
    }
    public void continueNPC()
    {
        this.isStopped = false;
        registerGoals();
    }

    // Method to check for nearby items and pick them up
    private void pickUpItems() {
        if (this.isAlive()) {
            AABB pickupRange = new AABB(getX() - 2.0D, getY() - 1.0D, getZ() - 2.0D, getX() + 2.0D, getY() + 1.0D, getZ() + 2.0D);
            level().getEntitiesOfClass(ItemEntity.class, pickupRange).forEach(itemEntity -> {
                ItemStack stack = itemEntity.getItem();
                if (!stack.isEmpty()) {
                    // Try adding the item to the NPC's inventory
                    if (inventory.addItem(stack)) {
                        itemEntity.remove(RemovalReason.UNLOADED_TO_CHUNK);
                    }
                }
            });
        }
    }

    // Override tick method to periodically check for nearby items
    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            pickUpItems();
        }
    }

    @Override
    protected void dropAllDeathLoot(DamageSource pDamageSource) {
        Entity entity = pDamageSource.getEntity();
        int lootingLevel = ForgeHooks.getLootingLevel(this, entity, pDamageSource);
        this.captureDrops(new ArrayList<>());
        boolean killedByPlayer = this.lastHurtByPlayerTime > 0;

        if (this.shouldDropLoot() && this.level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            this.dropFromLootTable(pDamageSource, killedByPlayer);
            this.dropCustomDeathLoot(pDamageSource, lootingLevel, killedByPlayer);
        }

        this.dropEquipment();
        this.dropExperience();

        // Drop the NPC's inventory
        inventory.dropAllItems(this.getX(), this.getY(), this.getZ(), this.level());

        Collection<ItemEntity> drops = this.captureDrops((Collection<ItemEntity>) null);
        if (!ForgeHooks.onLivingDrops(this, pDamageSource, drops, lootingLevel, killedByPlayer)) {
            drops.forEach(e -> this.level().addFreshEntity(e));
        }
    }
}
