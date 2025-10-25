
package com.example.ema;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EMA.MODID);

    public static final RegistryObject<Item> EXTREME_PATTERN = ITEMS.register("extreme_pattern", ExtremePatternItem::new);
}
