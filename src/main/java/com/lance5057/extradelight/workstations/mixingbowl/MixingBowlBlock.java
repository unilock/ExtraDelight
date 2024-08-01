package com.lance5057.extradelight.workstations.mixingbowl;

import com.lance5057.extradelight.ExtraDelightTags;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MixingBowlBlock extends Block implements EntityBlock {
	protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 6.0D, 14.0D);

	public MixingBowlBlock() {
		super(Properties.ofFullCopy(Blocks.ACACIA_WOOD).strength(0.5F, 1.0F).sound(SoundType.WOOD).noOcclusion());
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPE;
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState pState) {
		return true;
	}

	@Override
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}

	@Override
	public boolean isPathfindable(BlockState pState, PathComputationType pType) {
		return false;
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand,
			BlockHitResult pHit) {
		if (pLevel.isClientSide) {
			return ItemInteractionResult.SUCCESS;
		} else {
			BlockEntity tileEntity = pLevel.getBlockEntity(pPos);
			if (tileEntity instanceof MixingBowlBlockEntity mbe) {

				if (!mbe.complete) {
					if (pPlayer.isCrouching()) {
						mbe.extractItem(pPlayer);
						return ItemInteractionResult.SUCCESS;
					}
					if (pPlayer.getItemInHand(pHand).is(ExtraDelightTags.SPOONS)) {
						mbe.mix(pPlayer);
						return ItemInteractionResult.SUCCESS;
					} else {
						mbe.insertItem(pPlayer.getItemInHand(pHand));
					}

				} else {
					if (mbe.testContainerItem(pPlayer.getItemInHand(pHand))) {
						mbe.scoop(pPlayer, pHand);
						return ItemInteractionResult.SUCCESS;
					}
				}

			}
			return ItemInteractionResult.CONSUME;

		}

	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		// TODO Auto-generated method stub
		return new MixingBowlBlockEntity(pPos, pState);
	}

}
