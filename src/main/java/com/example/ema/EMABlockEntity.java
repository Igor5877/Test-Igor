
package com.example.ema;

import appeng.api.crafting.IPatternDetails;
import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.networking.*;
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

public class EMABlockEntity extends BlockEntity implements IGridNodeListener<EMABlockEntity>, ICraftingProvider, MenuProvider {

    private final IManagedGridNode mainNode;
    private final ItemStackHandler inventory = new ItemStackHandler(1);
    private final ItemStackHandler upgradeInventory = new ItemStackHandler(4);
    private final EnergyStorage energyStorage = new EnergyStorage(100000, 1000, 0);
    private final LazyOptional<IEnergyStorage> energyOptional = LazyOptional.of(() -> energyStorage);
    private ActiveCraft activeCraft;

    public EMABlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.EMA_BLOCK_ENTITY.get(), pos, state);
        this.mainNode = GridHelper.createManagedNode(this, this);
        this.mainNode.setFlags(GridFlags.REQUIRE_CHANNEL);
        this.mainNode.addService(ICraftingProvider.class, this);
    }

    public void tick() {
        if (level == null || level.isClientSide()) {
            return;
        }

        if (activeCraft != null) {
            if (energyStorage.getEnergyStored() > 0) {
                energyStorage.extractEnergy(10, false);
                IItemHandler automationInterface = activeCraft.getAutomationInterface();
                int outputSlot = automationInterface.getSlots() - 1;
                ItemStack result = automationInterface.extractItem(outputSlot, 1, true);

                if (!result.isEmpty()) {
                    automationInterface.extractItem(outputSlot, 1, false);
                    IGrid grid = this.mainNode.getGrid();
                    if (grid != null) {
                        IStorageService storage = grid.getStorageService();
                        storage.getInventory().insert(AEItemKey.of(result), result.getCount(), null, null);
                    }
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

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        this.mainNode.destroy();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        this.mainNode.destroy();
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
    public List<IPatternDetails> getAvailablePatterns() {
        List<IPatternDetails> patterns = new ArrayList<>();
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack patternStack = inventory.getStackInSlot(i);
            if (!patternStack.isEmpty()) {
                IPatternDetails details = PatternDetailsHelper.decodePattern(patternStack, this.level, false);
                if (details != null) {
                    patterns.add(details);
                }
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
                    for (KeyCounter counter : inputHolder) {
                        if (counter.key() instanceof AEItemKey itemKey) {
                            ItemStack stack = itemKey.toStack(Math.toIntExact(counter.amount()));
                            for (int i = 0; i < automationInterface.getSlots(); i++) {
                                stack = automationInterface.insertItem(i, stack, false);
                                if (stack.isEmpty()) {
                                    break;
                                }
                            }
                        }
                    }
                    activeCraft = new ActiveCraft(patternDetails, automationInterface);
                    EMA.LOGGER.info("Items transferred to Automation Interface!");
                    ICraftingProvider.requestUpdate(this.mainNode);
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

    @Override
    public void onSaveChanges(EMABlockEntity nodeOwner, IGridNode node) {
    }

    @Override
    public void onGridNodeStateChanged(EMABlockEntity nodeOwner, IGridNode node, State state) {
    }

    @NotNull
    public IGridNode getGridNode() {
        return this.mainNode.getNode();
    }
}
