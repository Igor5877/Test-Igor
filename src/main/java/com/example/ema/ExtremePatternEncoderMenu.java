
package com.example.ema;

import appeng.api.stacks.AEItemKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ExtremePatternEncoderMenu extends AbstractContainerMenu {

    private final ItemStackHandler inventory = new ItemStackHandler(84); // 9x9 grid + 1 output + 1 blank pattern + 1 encoded pattern

    public ExtremePatternEncoderMenu(@Nullable MenuType<?> type, int id, Inventory playerInventory) {
        super(type, id);

        addSlot(new Slot(inventory, 82, 188, 120));
        addSlot(new Slot(inventory, 83, 188, 140));

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(inventory, i * 9 + j, 8 + j * 18, 8 + i * 18));
            }
        }

        addSlot(new Slot(inventory, 81, 188, 80));

        // Player inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 174 + i * 18));
            }
        }

        // Player hotbar
        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 232));
        }
    }

    public List<AEItemKey> getInputs() {
        List<AEItemKey> inputs = new ArrayList<>();
        for (int i = 0; i < 81; i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                inputs.add(AEItemKey.of(stack));
            }
        }
        return inputs;
    }

    public AEItemKey getOutput() {
        ItemStack stack = inventory.getStackInSlot(81);
        if (!stack.isEmpty()) {
            return AEItemKey.of(stack);
        }
        return null;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 84) {
                if (!this.moveItemStackTo(itemstack1, 84, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 84, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
