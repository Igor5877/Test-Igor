
package com.example.ema;

import appeng.api.crafting.IPatternDetails;
import net.minecraftforge.items.IItemHandler;

public class ActiveCraft {
    private final IPatternDetails pattern;
    private final IItemHandler automationInterface;

    public ActiveCraft(IPatternDetails pattern, IItemHandler automationInterface) {
        this.pattern = pattern;
        this.automationInterface = automationInterface;
    }

    public IPatternDetails getPattern() {
        return pattern;
    }

    public IItemHandler getAutomationInterface() {
        return automationInterface;
    }
}
