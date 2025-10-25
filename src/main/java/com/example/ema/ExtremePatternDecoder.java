
package com.example.ema;

import appeng.api.crafting.IPatternDetails;
import appeng.api.crafting.IPatternDetailsDecoder;
import appeng.api.stacks.AEItemKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ExtremePatternDecoder implements IPatternDetailsDecoder {

    @Override
    public boolean isEncodedPattern(ItemStack stack) {
        return stack.getItem() instanceof ExtremePatternItem;
    }

    @Nullable
    @Override
    public IPatternDetails decodePattern(AEItemKey what, Level level) {
        // This version is not used in our case, but required by the interface.
        if (what != null && what.getItem() instanceof ExtremePatternItem) {
            return new ExtremePatternDetails(what.toStack());
        }
        return null;
    }

    @Nullable
    @Override
    public IPatternDetails decodePattern(ItemStack what, Level level, boolean tryRecovery) {
        if (what.getItem() instanceof ExtremePatternItem) {
            return new ExtremePatternDetails(what);
        }
        return null;
    }
}
