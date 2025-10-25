
package com.example.ema;

import appeng.api.crafting.IPatternDetails;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class ExtremePatternDetails implements IPatternDetails {

    private final ItemStack pattern;

    public ExtremePatternDetails(ItemStack pattern) {
        this.pattern = pattern;
    }

    @Override
    public AEItemKey getDefinition() {
        return AEItemKey.of(pattern);
    }

    @Override
    public IInput[] getInputs() {
        List<AEItemKey> inputs = PatternUtils.getInputs(pattern);
        IInput[] result = new IInput[inputs.size()];
        for (int i = 0; i < inputs.size(); i++) {
            result[i] = new Input(inputs.get(i));
        }
        return result;
    }

    @Override
    public List<GenericStack> getOutputs() {
        AEItemKey output = PatternUtils.getOutput(pattern);
        if (output != null) {
            return Collections.singletonList(new GenericStack(output, 1));
        }
        return Collections.emptyList();
    }

    public int getGridSize() {
        return PatternUtils.getGridSize(pattern);
    }

    private static class Input implements IInput {
        private final AEItemKey key;

        public Input(AEItemKey key) {
            this.key = key;
        }

        @Override
        public AEItemKey[] getPossibleInputs() {
            return new AEItemKey[] { key };
        }

        @Override
        public long getMultiplier() {
            return 1;
        }

        @Override
        public boolean isRequired() {
            return true;
        }
    }
}
