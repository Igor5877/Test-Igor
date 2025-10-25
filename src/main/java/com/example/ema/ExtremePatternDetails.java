
package com.example.ema;

import appeng.api.crafting.IPatternDetails;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ExtremePatternDetails implements IPatternDetails {

    private final ItemStack patternStack;
    private final IInput[] inputs;
    private final GenericStack[] outputs;

    public ExtremePatternDetails(ItemStack patternStack) {
        this.patternStack = patternStack;

        List<AEItemKey> inputKeys = PatternUtils.getInputs(patternStack);
        this.inputs = new IInput[inputKeys.size()];
        for (int i = 0; i < inputKeys.size(); i++) {
            this.inputs[i] = new Input(new GenericStack(inputKeys.get(i), 1));
        }

        AEItemKey outputKey = PatternUtils.getOutput(patternStack);
        if (outputKey != null) {
            this.outputs = new GenericStack[]{new GenericStack(outputKey, 1)};
        } else {
            this.outputs = new GenericStack[0];
        }
    }

    @Override
    public AEItemKey getDefinition() {
        return AEItemKey.of(patternStack);
    }

    @Override
    public IInput[] getInputs() {
        return this.inputs;
    }

    @Override
    public GenericStack[] getOutputs() {
        return this.outputs;
    }

    public int getGridSize() {
        return PatternUtils.getGridSize(patternStack);
    }

    private static class Input implements IInput {
        private final GenericStack stack;

        public Input(GenericStack stack) {
            this.stack = stack;
        }

        @Override
        public GenericStack[] getPossibleInputs() {
            return new GenericStack[]{stack};
        }

        @Override
        public long getMultiplier() {
            return 1;
        }

        @Nullable
        @Override
        public AEKey getRemainingKey(AEKey template) {
            return null;
        }

        @Override
        public boolean isValid(AEKey input, Level level) {
            return true;
        }
    }
}
