
package com.example.ema;

import appeng.api.crafting.IPatternDetails;
import appeng.api.crafting.IPatternDetailsDecoder;
import net.minecraft.world.item.ItemStack;

public class ExtremePatternDecoder implements IPatternDetailsDecoder {
    @Override
    public IPatternDetails decode(ItemStack stack) {
        if (stack.getItem() instanceof ExtremePatternItem) {
            return new ExtremePatternDetails(stack);
        }
        return null;
    }
}
