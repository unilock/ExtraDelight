package com.lance5057.extradelight.blocks.crops.corn;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

import javax.annotation.Nullable;

import com.lance5057.extradelight.ExtraDelightBlocks;
import com.lance5057.extradelight.ExtraDelightItems;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CornBottom extends CropBlock {
	public static final int MAX_AGE = 3;
	public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] { Block.box(4.0D, 0.0D, 4.0D, 12.0D, 4.0D, 12.0D),
			Block.box(4.0D, 0.0D, 4.0D, 12.0D, 6.0D, 12.0D), Block.box(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D),
			Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D) };

	public CornBottom(BlockBehaviour.Properties pProperties) {
		super(pProperties);
		this.registerDefaultState(this.stateDefinition.any().setValue(this.getAgeProperty(), 0)
				.setValue(CornProperties.DIMENSION, false));
	}

	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPE_BY_AGE[pState.getValue(this.getAgeProperty())];
	}

	@Override
	protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
		return pState.is(Blocks.FARMLAND);
	}

	public IntegerProperty getAgeProperty() {
		return AGE;
	}

	public int getMaxAge() {
		return MAX_AGE;
	}

	public int getAge(BlockState pState) {
		return pState.getValue(this.getAgeProperty());
	}

	public BlockState getStateForAge(int pAge) {
		return this.defaultBlockState().setValue(this.getAgeProperty(), Integer.valueOf(pAge));
	}

//	public boolean isMaxAge(BlockState pState) {
//		return pState.getValue(this.getAgeProperty()) >= this.getMaxAge();
//	}

	/**
	 * @return whether this block needs random ticking.
	 */
	public boolean isRandomlyTicking(BlockState pState) {
		return true;
	}

	/**
	 * Performs a random tick on a block.
	 */
	public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
		if (!pLevel.isAreaLoaded(pPos, 1))
			return; // Forge: prevent loading unloaded chunks when checking neighbor's light

		if (!this.isMaxAge(pState)) {
			if (pLevel.getRawBrightness(pPos, 0) >= 9) {
				int i = this.getAge(pState);
				if (i < this.getMaxAge()) {
					float f = CropBlock.getGrowthSpeed(pState, pLevel, pPos);
					if (net.neoforged.neoforge.common.CommonHooks.canCropGrow(pLevel, pPos, pState,
							pRandom.nextInt((int) (25.0F / f) + 1) == 0)) {
						this.growCrops(pLevel, pPos, pState);
						net.neoforged.neoforge.common.CommonHooks.fireCropGrowPost(pLevel, pPos, pState);
					}
				}
			}

		}

	}

	public void growCrops(Level pLevel, BlockPos pPos, BlockState pState) {
		int i = this.getAge(pState) + this.getBonemealAgeIncrease(pLevel);
		int j = this.getMaxAge();
		if (i > j) {
			i = j;

		}

		if (i == this.getMaxAge()) {
			if (!checkAboveCorn(pLevel, pPos)) {
				if (checkAboveAir(pLevel, pPos)) {
					pLevel.setBlock(pPos.above(), ExtraDelightBlocks.CORN_TOP.get().getStateForAge(0), 2);
				}
			}
		}

		pLevel.setBlock(pPos, this.getStateForAge(i), 2);
	}

	protected int getBonemealAgeIncrease(Level pLevel) {
		return 1;
	}

//	protected static float getGrowthSpeed(Block pBlock, BlockGetter pLevel, BlockPos pPos) {
//		float f = 1.0F;
//		BlockPos blockpos = pPos.below();
//
//		for (int i = -1; i <= 1; ++i) {
//			for (int j = -1; j <= 1; ++j) {
//				float f1 = 0.0F;
//				BlockState blockstate = pLevel.getBlockState(blockpos.offset(i, 0, j));
//				if (blockstate.canSustainPlant(p_52274_, blockpos.offset(i, 0, j), net.minecraft.core.Direction.UP,
//						(net.neoforged.neoforge.common.IPlantable) p_52273_)) {
//					f1 = 1.0F;
//					f1 = 1.0F;
//					if (blockstate.isFertile(pLevel, pPos.offset(i, 0, j))) {
//						f1 = 3.0F;
//					}
//				}
//
//				if (i != 0 || j != 0) {
//					f1 /= 4.0F;
//				}
//
//				f += f1;
//			}
//		}
//
//		BlockPos blockpos1 = pPos.north();
//		BlockPos blockpos2 = pPos.south();
//		BlockPos blockpos3 = pPos.west();
//		BlockPos blockpos4 = pPos.east();
//		boolean flag = pLevel.getBlockState(blockpos3).is(pBlock) || pLevel.getBlockState(blockpos4).is(pBlock);
//		boolean flag1 = pLevel.getBlockState(blockpos1).is(pBlock) || pLevel.getBlockState(blockpos2).is(pBlock);
//		if (flag && flag1) {
//			f /= 2.0F;
//		} else {
//			boolean flag2 = pLevel.getBlockState(blockpos3.north()).is(pBlock)
//					|| pLevel.getBlockState(blockpos4.north()).is(pBlock)
//					|| pLevel.getBlockState(blockpos4.south()).is(pBlock)
//					|| pLevel.getBlockState(blockpos3.south()).is(pBlock);
//			if (flag2) {
//				f /= 2.0F;
//			}
//		}
//
//		return f;
//	}

	public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
		if (pLevel instanceof WorldGenRegion) {
			if (pLevel.getBlockState(pPos.below()).getBlock() == Blocks.GRASS_BLOCK)
				return true;
		} else if (pState.getValue(CornProperties.DIMENSION))
			return true;
		return (pLevel.getRawBrightness(pPos, 0) >= 8 || pLevel.canSeeSky(pPos))
				&& super.canSurvive(pState, pLevel, pPos);
	}

//	public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
//		if (pEntity instanceof Ravager
//				&& net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(pLevel, pEntity)) {
//			pLevel.destroyBlock(pPos, true, pEntity);
//		}
//		if (pState.getValue(CornProperties.DIMENSION)) {
//			if (pEntity.isSprinting())
//				pEntity.hurt(DamageSource.SWEET_BERRY_BUSH, 1);
//			pEntity.makeStuckInBlock(pState, new Vec3((double) 0.8F, 0.75D, (double) 0.4F));
//
//		}
//
//		super.entityInside(pState, pLevel, pPos, pEntity);
//	}

	private static boolean isHalloween() {
		LocalDate localdate = LocalDate.now();
		int i = localdate.get(ChronoField.DAY_OF_MONTH);
		int j = localdate.get(ChronoField.MONTH_OF_YEAR);
		return j == 10 && i >= 1 || j == 11 && i <= 10;
	}

	@Override
	public boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
		return false;
	}

	protected ItemLike getBaseSeedId() {
		return ExtraDelightItems.CORN_SEEDS.get();
	}

	public ItemStack getCloneItemStack(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
		return new ItemStack(this.getBaseSeedId());
	}

	/**
	 * @return whether bonemeal can be used on this block
	 */
	public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState state) {
		boolean b = checkAboveCorn(pLevel, pPos);
		if (b) {
			boolean r = ((CropBlock) this.getAboveCorn(pLevel, pPos).getBlock()).isValidBonemealTarget(pLevel,
					pPos.above(), pLevel.getBlockState(pPos.above()));
			return r;
		}

		return this.checkAboveAir(pLevel, pPos);
	}

	private BlockState getAboveCorn(BlockGetter pLevel, BlockPos pPos) {
		BlockState b = pLevel.getBlockState(pPos.above());
		if (b.getBlock() instanceof CornTop top)
			return b;
		return null; // Shouldn't happen
	}

	private boolean checkAboveCorn(BlockGetter pLevel, BlockPos pPos) {
		if (pLevel.getBlockState(pPos.above()).getBlock() instanceof CornTop) {
			return true;
		}
		return false;
	}

	private boolean checkAboveAir(BlockGetter pLevel, BlockPos pPos) {
		if (pLevel.getBlockState(pPos.above()).isAir()) {
			return true;
		}
		return false;
	}

	public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
		return true;
	}

	public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
		if (pLevel.getBlockState(pPos.above()).getBlock() instanceof CornTop top) {
			top.growCrops(pLevel, pPos.above(), pLevel.getBlockState(pPos.above()));
		} else
			this.growCrops(pLevel, pPos, pState);
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(AGE, CornProperties.DIMENSION);
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
			BlockHitResult hitResult) {

		if (this.checkAboveCorn(level, pos))
			return this.getAboveCorn(level, pos).useWithoutItem(level, player, hitResult);

		return InteractionResult.PASS;
	}

	@Nullable
	public static void placeAt(LevelAccessor pLevel, BlockState pState, BlockPos pPos, int pFlags) {
		pLevel.setBlock(pPos.above(),
				ExtraDelightBlocks.CORN_TOP.get().defaultBlockState().setValue(AGE, 3).setValue(CornProperties.DIMENSION, true), 0);
		pLevel.setBlock(pPos,
				ExtraDelightBlocks.CORN_BOTTOM.get().defaultBlockState().setValue(AGE, 3).setValue(CornProperties.DIMENSION, true), 0);

	}

	@Override
	public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
		BlockPos blockpos = pPos.above();
	}

	@Override
	public void destroy(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
		if (pState.getValue(CornProperties.DIMENSION)) {
			pLevel.setBlock(pPos, pState, 4);
		}
	}
}
