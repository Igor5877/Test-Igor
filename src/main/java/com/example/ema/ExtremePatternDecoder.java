
package com.example.ema;

import appeng.api.crafting.IPatternDetails;
import appeng.api.crafting.IPatternDetailsDecoder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ExtremePatternDecoder implements IPatternDetailsDecoder {

    @Nullable
    @Override
    public IPatternDetails decodePattern(ItemStack patternStack, Level level, boolean isServer) {
        if (patternStack.getItem() instanceof ExtremePatternItem) {
            return new ExtremePatternDetails(patternStack);
        }
        return null;
    }
}
