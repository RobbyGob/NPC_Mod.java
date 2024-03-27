package io.github.RobbyGob.npc.entity.inventory;

import io.github.RobbyGob.npc.entity.EntityNPC;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class NPCInventory implements Container {
    private final NonNullList<ItemStack> items;
    public final NonNullList<ItemStack> armor;
    public final NonNullList<ItemStack> offhand;
    public static final int[] ALL_ARMOR_SLOTS = new int[]{0, 1, 2, 3};
    public static final int[] HELMET_SLOT_ONLY = new int[]{3};
    private final EntityNPC owner;

    public NPCInventory(EntityNPC owner) {
        this.items = NonNullList.withSize(36, ItemStack.EMPTY);
        this.armor = NonNullList.withSize(4, ItemStack.EMPTY);
        this.offhand = NonNullList.withSize(1, ItemStack.EMPTY);
        this.owner = owner;
    }
    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return index >= 0 && index < this.items.size() ? this.items.get(index) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack stack = ContainerHelper.removeItem(this.items, index, count);
        if (!stack.isEmpty()) {
            this.setChanged();
        }
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack stack = this.items.get(index);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.items.set(index, ItemStack.EMPTY);
            return stack;
        }
    }
    public void dropAllItems(double x, double y, double z, Level level) {
        for (int slot = 0; slot < getContainerSize(); slot++) {
            ItemStack stack = getItem(slot);
            if (!stack.isEmpty()) {
                ItemEntity itemEntity = new ItemEntity(level, x, y, z, stack.copy());
                level.addFreshEntity(itemEntity);
                setItem(slot, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.items.set(index, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
        this.setChanged();
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    public boolean addItem(ItemStack stack) {
        for (int slot = 0; slot < getContainerSize(); slot++) {
            ItemStack existingStack = getItem(slot);
            if (existingStack.isEmpty()) {
                setItem(slot, stack.copy());
                return true;
            } else if (ItemStack.isSameItem(existingStack, stack) && ItemStack.isSameItemSameTags(existingStack, stack)) {
                int spaceLeft = existingStack.getMaxStackSize() - existingStack.getCount();
                if (spaceLeft > 0) {
                    int toAdd = Math.min(spaceLeft, stack.getCount());
                    existingStack.grow(toAdd);
                    stack.shrink(toAdd);
                    return true;
                }
            }
        }
        return false;
    }
    public void load(ListTag tagList) {
        for (int i = 0; i < tagList.size(); ++i) {
            CompoundTag itemTag = tagList.getCompound(i);
            int j = itemTag.getByte("Slot") & 255;
            if (j >= 0 && j < this.items.size()) {
                this.items.set(j, ItemStack.of(itemTag));
            }
        }
    }
    public ListTag save(ListTag tagList) {
        for (int i = 0; i < this.items.size(); ++i) {
            ItemStack stack = this.items.get(i);
            if (!stack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putByte("Slot", (byte) i);
                stack.save(itemTag);
                tagList.add(itemTag);
            }
        }
        return tagList;
    }
    public void spawnItemAtEntity(ItemStack stack, EntityNPC entity) {
        ItemEntity itemEntity = new ItemEntity(this.owner.level(), this.owner.getX(), this.owner.getY() + 1,
                this.owner.getZ(), stack);

    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public void setChanged() {

    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

}
