
package com.example.ema.client;

import com.example.ema.ModMenus;
import com.example.ema.ExtremePatternEncoderScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {
    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenus.EXTREME_PATTERN_ENCODER_MENU.get(), ExtremePatternEncoderScreen::new);
        });
    }
}
