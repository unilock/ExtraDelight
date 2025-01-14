package com.lance5057.extradelight.data;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import vectorwing.farmersdelight.data.advancement.FDAdvancementGenerator;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Advancements extends ForgeAdvancementProvider
{
	public Advancements(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, existingFileHelper, List.of(new FDAdvancementGenerator()));
	}
}