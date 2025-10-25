
package com.example.ema;

import appeng.api.crafting.IPatternDetails;
import appeng.api.networking.GridHelper;
import appeng.api.networking.IGridNode;
import appeng.api.networking.IGridNodeListener;
import appeng.api.networking.GridFlags;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.networking.storage.IStorageService;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.KeyCounter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class EMABlockEntity extends BlockEntity implements IGridNodeListener, ICraftingProvider, MenuProvider {

    private IGridNode gridNode;
    private final ItemStackHandler inventory = new ItemStackHandler(1);
    private final ItemStackHandler upgradeInventory = new ItemStackHandler(4);
    private final EnergyStorage energyStorage = new EnergyStorage(100000, 1000, 0);
    private final LazyOptional<IEnergyStorage> energyOptional = LazyOptional.of(() -> energyStorage);
    private ActiveCraft activeCraft;

    public EMABlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.EMA_BLOCK_ENTITY.get(), pos, state);
    }

    public void tick() {
        if (activeCraft != null) {
            if (energyStorage.getEnergyStored() > 0) {
                energyStorage.extractEnergy(10, false);
                IItemHandler automationInterface = activeCraft.getAutomationInterface();
                ItemStack result = automationInterface.extractItem(automationInterface.getSlots() - 1, 1, true);
                if (!result.isEmpty()) {
                    automationInterface.extractItem(automationInterface.getSlots() - 1, 1, false);
                    IStorageService storage = getGridNode().getGrid().getStorageService();
                    storage.getInventory().insert(AEItemKey.of(result), result.getCount(), null, null);
                    activeCraft = null;
                    EMA.LOGGER.info("Crafting complete!");
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("inventory", inventory.serializeNBT());
        tag.put("upgrades", upgradeInventory.serializeNBT());
        tag.put("energy", energyStorage.serializeNBT());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        inventory.deserializeNBT(tag.getCompound("inventory"));
        upgradeInventory.deserializeNBT(tag.getCompound("upgrades"));
        energyStorage.deserializeNBT(tag.get("energy"));
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return energyOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energyOptional.invalidate();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getGridNode() != null) {
            getGridNode().updateState();
        }
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        if (gridNode != null) {
            gridNode.destroy();
        }
    }

    @Nullable
    public IGridNode getGridNode() {
        if (gridNode == null) {
            gridNode = GridHelper.createGridNode(this);
            gridNode.setFlags(EnumSet.of(GridFlags.REQUIRE_CHANNEL));
        }
        return gridNode;
    }

    @Override
    public void onGridNodeDestroyed(IGridNode iGridNode) {

    }

    @Override
    public void onGridNodeStateChanged(IGridNode iGridNode, IGridNodeListener.State a) {

    }

    @Override
    public List<IPatternDetails> getAvailablePatterns() {
        List<IPatternDetails> patterns = new ArrayList<>();
        for (int i = 0; i < inventory.getSlots(); i++) {
            IPatternDetails details = PatternDetailsHelper.getPatternDetails(inventory.getStackInSlot(i), level);
            if (details != null) {
                patterns.add(details);
            }
        }
        return patterns;
    }

    @Override
    public int getPatternPriority() {
        return 0;
    }

    @Override
    public boolean pushPattern(IPatternDetails patternDetails, KeyCounter[] inputHolder) {
        if (isBusy()) {
            return false;
        }

        if (patternDetails instanceof ExtremePatternDetails extremePattern) {
            IItemHandler automationInterface = AutomationInterfaceHelper.findAutomationInterface(level, worldPosition);
            if (automationInterface != null) {
                int gridSize = extremePattern.getGridSize();
                if (automationInterface.getSlots() >= gridSize * gridSize) {
                    for (int i = 0; i < inputHolder.length; i++) {
                        AEItemKey itemKey = (AEItemKey) inputHolder[i].getKey();
                        ItemStack stack = itemKey.toStack((int) inputHolder[i].getAmount());
                        automationInterface.insertItem(i, stack, false);
                    }
                    activeCraft = new ActiveCraft(patternDetails, automationInterface);
                    EMA.LOGGER.info("Items transferred to Automation Interface!");
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isBusy() {
        return activeCraft != null;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Extreme Molecular Assembler");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
        return new ExtremePatternEncoderMenu(ModMenus.EXTREME_PATTERN_ENCODER_MENU.get(), windowId, playerInventory);
    }
}
