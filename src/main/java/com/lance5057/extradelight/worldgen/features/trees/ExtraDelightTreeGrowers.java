package com.lance5057.extradelight.worldgen.features.trees;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class ExtraDelightTreeGrowers {
	public static final AbstractTreeGrower CINNAMON = new AbstractTreeGrower() {
		@Override
		protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource randomSource, boolean b) {
			return ExtraDelightTreeFeatures.CINNAMON;
		}
	};
}
