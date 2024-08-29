package com.lance5057.extradelight;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ExtraDelightWorldGen {
	public static final ResourceKey<Level> CORNFIELD = ResourceKey.create(
			Registries.DIMENSION,
			ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "corn_dimension")
	);
	public static final ResourceKey<Biome> CORNFIELD_BIOME = ResourceKey.create(
			Registries.BIOME,
			ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "cornfield")
	);
	public static final ResourceKey<DimensionType> CORNFIELD_TYPE = ResourceKey.create(
			Registries.DIMENSION_TYPE,
			ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "corn")
	);
	public static final ResourceKey<LevelStem> CORNFIELD_STEM = ResourceKey.create(
			Registries.LEVEL_STEM,
			ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "cornfield")
	);
	public static final ResourceKey<NoiseGeneratorSettings> CORNFIELD_NOISE = ResourceKey.create(
			Registries.NOISE_SETTINGS,
			ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "cornfield")
	);

	public static final ResourceKey<ConfiguredFeature<?, ?>> CONFIGURED_CORN_MAZE = ResourceKey.create(
			Registries.CONFIGURED_FEATURE,
			ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "corn_maze_feature")
	);
	public static final ResourceKey<PlacedFeature> PLACED_CORN_MAZE = ResourceKey.create(
			Registries.PLACED_FEATURE,
			ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "corn_placer")
	);

	public static final ResourceKey<ConfiguredFeature<?, ?>> CONFIGURED_CORN_RAIL = ResourceKey.create(
			Registries.CONFIGURED_FEATURE,
			ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "corn_rail_feature")
	);
	public static final ResourceKey<PlacedFeature> PLACED_CORN_RAIL = ResourceKey.create(
			Registries.PLACED_FEATURE,
			ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "rail_placer")
	);

	public static final ResourceKey<ConfiguredFeature<?, ?>> CONFIGURED_CINNAMON_TREE = ResourceKey.create(
			Registries.CONFIGURED_FEATURE,
			ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "cinnamon")
	);
	public static final ResourceKey<PlacedFeature> PLACED_CINNAMON_TREE = ResourceKey.create(
			Registries.PLACED_FEATURE,
			ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "cinnamon")
	);
}
