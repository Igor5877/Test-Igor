
package com.example.ema;

import appeng.api.stacks.AEItemKey;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PatternUtils {
    public static CompoundTag getTag(ItemStack stack) {
        return stack.getOrCreateTag();
    }

    public static void setGridSize(ItemStack stack, int size) {
        getTag(stack).putInt("gridSize", size);
    }

    public static int getGridSize(ItemStack stack) {
        return getTag(stack).getInt("gridSize");
    }

    public static void setInputs(ItemStack stack, List<AEItemKey> inputs) {
        ListTag list = new ListTag();
        for (AEItemKey input : inputs) {
            list.add(input.toTag());
        }
        getTag(stack).put("inputs", list);
    }

    public static List<AEItemKey> getInputs(ItemStack stack) {
        List<AEItemKey> inputs = new ArrayList<>();
        ListTag list = getTag(stack).getList("inputs", 10);
        for (int i = 0; i < list.size(); i++) {
            inputs.add(AEItemKey.fromTag(list.getCompound(i)));
        }
        return inputs;
    }

    public static void setOutput(ItemStack stack, AEItemKey output) {
        getTag(stack).put("output", output.toTag());
    }

    public static AEItemKey getOutput(ItemStack stack) {
        return AEItemKey.fromTag(getTag(stack).getCompound("output"));
    }
}
