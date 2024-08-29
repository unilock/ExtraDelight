package com.lance5057.extradelight.worldgen.features;

import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.worldgen.features.trees.CinnamonTreeFeature;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ExtraDelightFeatures {

	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE,
			ExtraDelight.MOD_ID);

	public static final DeferredHolder<Feature<?>, WildCornFeature> PATCH_WILD_CORN = FEATURES.register("wild_corn",
			WildCornFeature::new);
	public static final DeferredHolder<Feature<?>, WildGingerFeature> PATCH_WILD_GINGER = FEATURES.register("wild_ginger",
			WildGingerFeature::new);
//	public static final DeferredHolder<Feature<?>, WildCoffeeFeature> PATCH_WILD_COFFEE = FEATURES.register("wild_coffee",
//			WildCoffeeFeature::new);
	
	public static final DeferredHolder<Feature<?>, CinnamonTreeFeature> PATCH_CINNAMON_TREE = FEATURES.register("cinnamon_tree",
			CinnamonTreeFeature::new);

	public static final DeferredHolder<Feature<?>, CornMazeFeature> CORN_MAZE_FEATURE = FEATURES.register("corn_maze_feature", CornMazeFeature::new);
	public static final DeferredHolder<Feature<?>, CornMazeRail> CORN_RAIL_FEATURE = FEATURES.register("corn_rail_feature", CornMazeRail::new);

//	public static final DeferredHolder<ConfiguredFeature<?, ?>, ConfiguredFeature<TreeConfiguration, Feature<TreeConfiguration>>> CINNAMON = FEATURES
//			.register("cinnamon",
//					() -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
//							BlockStateProvider.simple(ExtraDelightBlocks.CINNAMON_LOG.get()),
//							new BendingTrunkPlacer(2, 0, 0, 6, ConstantInt.of(1)),
//							BlockStateProvider.simple(ExtraDelightBlocks.CINNAMON_LEAVES.get()),
//							new RandomSpreadFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), ConstantInt.of(2), 12),
//							new TwoLayersFeatureSize(1, 0, 2)).build()));
//
//	public static final DeferredHolder<ConfiguredFeature<?, ?>, ConfiguredFeature<?, ?>> CINNAMON_SPAWN = FEATURES.register(
//			"cinnamon_spawn",
//			() -> new ConfiguredFeature<>(Feature.RANDOM_SELECTOR,
//					new RandomFeatureConfiguration(
//							List.of(new WeightedPlacedFeature(
//									ExtraDelightPlacedFeatures.CINNAMON_CHECKED.getHolder().get(), 0.5F)),
//							ExtraDelightPlacedFeatures.CINNAMON_CHECKED.getHolder().get())));
//
//	public static void register(IEventBus eventBus) {
//		CONFIGURED_FEATURES.register(eventBus);
//	}
}
