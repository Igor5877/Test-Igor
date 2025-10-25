
package com.example.ema;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import net.minecraftforge.eventbus.api.IEventBus;
import appeng.api.crafting.PatternDetailsHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import com.example.ema.client.ClientSetup;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Logger;

@Mod(EMA.MODID)
public class EMA {
    public static final String MODID = "ema";
    public static final Logger LOGGER = LogManager.getLogger();

    public EMA() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlocks.BLOCK_ENTITIES.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(ClientSetup::init);

        LOGGER.info("Extreme Molecular Assembler is loading!");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        PatternDetailsHelper.registerDecoder(new ExtremePatternDecoder());
    }
}
