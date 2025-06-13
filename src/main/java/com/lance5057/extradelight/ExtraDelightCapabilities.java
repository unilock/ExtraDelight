package com.lance5057.extradelight;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;

public class ExtraDelightCapabilities {
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {

		event.registerItem(Capabilities.FluidHandler.ITEM, (i, c) -> new FluidHandlerItemStack(i, 1000),
				ExtraDelightItems.JAR.get());
	}
}
