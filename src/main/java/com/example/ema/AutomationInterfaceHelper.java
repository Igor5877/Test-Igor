
package com.example.ema;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

public class AutomationInterfaceHelper {

    public static IItemHandler findAutomationInterface(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockEntity be = level.getBlockEntity(pos.relative(direction));
            if (be != null) {
                IItemHandler handler = be.getCapability(ForgeCapabilities.ITEM_HANDLER, direction.getOpposite()).orElse(null);
                if (handler != null) {
                    return handler;
                }
            }
        }
        return null;
    }
}
