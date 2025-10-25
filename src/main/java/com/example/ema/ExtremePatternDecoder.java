
package com.example.ema;

import appeng.api.crafting.IPatternDetails;
import appeng.api.crafting.IPatternDetailsDecoder;
import appeng.api.stacks.AEItemKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ExtremePatternDecoder implements IPatternDetailsDecoder {
    @Override
    public IPatternDetails decodePattern(AEItemKey key, Level level) {
        if (key.getItem() instanceof ExtremePatternItem) {
            return new ExtremePatternDetails(key.toStack());
        }
        return null;
    }

    @Override
    public boolean isEncodedPattern(ItemStack stack) {
        return stack.getItem() instanceof ExtremePatternItem;
    }
}
