
package com.example.ema;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class ExtremePatternEncoderScreen extends AbstractContainerScreen<ExtremePatternEncoderMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(EMA.MODID, "textures/gui/extreme_pattern_encoder.png");

    public ExtremePatternEncoderScreen(ExtremePatternEncoderMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 220;
        this.imageHeight = 256;
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(Button.builder(Component.literal("Encode"), this::encode).bounds(leftPos + 188, topPos + 100, 20, 20).build());
    }

    private void encode(Button button) {
        ItemStack blankPattern = menu.getSlot(82).getItem();
        if (blankPattern.is(ModItems.EXTREME_PATTERN.get())) {
            ItemStack encodedPattern = new ItemStack(ModItems.EXTREME_PATTERN.get());
            PatternUtils.setGridSize(encodedPattern, 9);
            PatternUtils.setInputs(encodedPattern, menu.getInputs());
            PatternUtils.setOutput(encodedPattern, menu.getOutput());
            menu.getSlot(83).set(encodedPattern);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int x, int y) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(TEXTURE, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
