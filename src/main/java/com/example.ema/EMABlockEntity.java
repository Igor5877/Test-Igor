
package com.example.ema;

import appeng.api.networking.GridHelper;
import appeng.api.networking.IGridNode;
import appeng.api.networking.IGridNodeListener;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class EMABlockEntity extends BlockEntity implements IGridNodeListener {

    private IGridNode gridNode;

    public EMABlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.EMA_BLOCK_ENTITY.get(), pos, state);
    }

    @Nullable
    public IGridNode getGridNode() {
        if (gridNode == null) {
            gridNode = GridHelper.createGridNode(this);
        }
        return gridNode;
    }
}
