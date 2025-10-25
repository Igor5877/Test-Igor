
package com.example.ema;

import appeng.api.crafting.IPatternDetails;
import appeng.api.stacks.AEItemKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExtremePatternDetails implements IPatternDetails {

    private final ItemStack pattern;

    public ExtremePatternDetails(ItemStack pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean isCrafting() {
        return true;
    }

    @Override
    public boolean isProcessing() {
        return false;
    }

    @Override
    public AEItemKey[] getInputs() {
        List<AEItemKey> inputs = PatternUtils.getInputs(pattern);
        return inputs.toArray(new AEItemKey[0]);
    }

    @Override
    public AEItemKey[] getOutputs() {
        AEItemKey output = PatternUtils.getOutput(pattern);
        return new AEItemKey[] { output };
    }

    @Override
    public ItemStack getDefinition() {
        return pattern;
    }

    @Nullable
    @Override
    public Level getLevel() {
        return null;
    }

    public int getGridSize() {
        return PatternUtils.getGridSize(pattern);
    }
}
