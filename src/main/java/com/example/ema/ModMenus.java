
package com.example.ema;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, EMA.MODID);

    public static final RegistryObject<MenuType<ExtremePatternEncoderMenu>> EXTREME_PATTERN_ENCODER_MENU = MENUS.register("extreme_pattern_encoder_menu", () -> IForgeMenuType.create((windowId, inv, data) -> new ExtremePatternEncoderMenu(ModMenus.EXTREME_PATTERN_ENCODER_MENU.get(), windowId, inv)));
}
