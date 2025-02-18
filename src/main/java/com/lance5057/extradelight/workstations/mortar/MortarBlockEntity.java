package com.lance5057.extradelight.workstations.mortar;

import java.util.Optional;

import javax.annotation.Nonnull;

import com.lance5057.extradelight.ExtraDelightBlockEntities;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.util.BlockEntityUtils;
import com.lance5057.extradelight.workstations.mortar.recipes.MortarRecipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;

public class MortarBlockEntity extends SyncedBlockEntity implements RecipeCraftingHolder {
//	private final LazyOptional<IItemHandlerModifiable> handler = LazyOptional.of(this::createHandler);

	private int grinds = 0;

	public static final String TAG = "inv";

	private final ItemStackHandler items = createHandler();
	private final Lazy<IItemHandlerModifiable> itemHandler = Lazy.of(() -> items);
	public static final int NUM_SLOTS = 1;

	public static final String FLUID_TAG = "fluid";
	private final FluidTank tank = createFluidHandler();

	private FluidTank createFluidHandler() {
		FluidTank tank = new FluidTank(FluidType.BUCKET_VOLUME) {
			@Override
			protected void onContentsChanged() {
				MortarBlockEntity.this.requestModelDataUpdate();
				MortarBlockEntity.this.getLevel().sendBlockUpdated(MortarBlockEntity.this.getBlockPos(),
						MortarBlockEntity.this.getBlockState(), MortarBlockEntity.this.getBlockState(), 3);
				MortarBlockEntity.this.setChanged();
			}
		};

		return tank;
	}

	public MortarBlockEntity(BlockPos pPos, BlockState pState) {
		super(ExtraDelightBlockEntities.MORTAR.get(), pPos, pState);
	}

//	@Nonnull
//	@Override
//	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
//		if (side != Direction.DOWN)
//			if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
//				return handler.cast();
//			}
//		return super.getCapability(cap, side);
//	}

//	@SubscribeEvent
//	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
//		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ExtraDelightBlockEntities.MORTAR.get(),
//				(be, context) -> {
//					return be.inventory;
//				});
//	}

	public IItemHandlerModifiable getItemHandler() {
		return itemHandler.get();
	}

	private ItemStackHandler createHandler() {
		return new ItemStackHandler(1) {
			@Override
			protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
				return 4;
			}

			@Override
			protected void onContentsChanged(int slot) {
				zeroProgress();
				updateInventory();
			}
		};
	}

	public void insertItem(ItemStack in) {
		BlockEntityUtils.Inventory.insertItem(in, items, NUM_SLOTS);
		this.updateInventory();
	}

	public void extractItem(ItemStack out) {
		BlockEntityUtils.Inventory.extractItem(items, NUM_SLOTS);
		this.updateInventory();
	}

	// External extract handler
//	public void extractItem(Player playerEntity) {
//		inventory.extractItem(playerEntity, inventory);
//	}
//
//	// External insert handler
//	public void insertItem(ItemStack heldItem) {
//		inventory.insertItem(inventory, heldItem);
//	}

	public void zeroProgress() {
		grinds = 0;
	}

	public void updateInventory() {
		requestModelDataUpdate();
		this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
		this.setChanged();
	}

	public ItemStack getInsertedItem() {
		return items.getStackInSlot(0);
	}

	public int getGrind() {
		return grinds;
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		CompoundTag nbt = super.getUpdateTag(registries);

		writeNBT(nbt, registries);

		return nbt;
	}

	@Override
	public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
		readNBT(tag, registries);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
//		CompoundTag tag = new CompoundTag();
//
//		writeNBT(tag);

		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider registries) {
		CompoundTag tag = pkt.getTag();
		// InteractionHandle your Data
		readNBT(tag, registries);
	}

	void readNBT(CompoundTag nbt, HolderLookup.Provider registries) {
		if (nbt.contains(TAG)) {
			items.deserializeNBT(registries, nbt.getCompound(TAG));
		}

		this.grinds = nbt.getInt("Grinds");
	}

	CompoundTag writeNBT(CompoundTag tag, HolderLookup.Provider registries) {

		tag.put(TAG, items.serializeNBT(registries));

		tag.putInt("Grinds", this.grinds);

		return tag;
	}

	@Override
	public void loadAdditional(@Nonnull CompoundTag nbt, HolderLookup.Provider registries) {
		super.loadAdditional(nbt, registries);
		readNBT(nbt, registries);
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag nbt, HolderLookup.Provider registries) {
		super.saveAdditional(nbt, registries);
		writeNBT(nbt, registries);
	}

	public Optional<RecipeHolder<MortarRecipe>> matchRecipe(ItemStack itemstack) {
		if (this.level != null) {
			return level.getRecipeManager().getRecipeFor(ExtraDelightRecipes.MORTAR.get(),
					new SingleRecipeInput(itemstack), level);
		}
		return Optional.empty();

	}

	public InteractionResult grind(Player Player) {
		Optional<RecipeHolder<MortarRecipe>> recipeOptional = matchRecipe(getInsertedItem());
		recipeOptional.ifPresent(recipe -> {
//			MortarRecipe recipe = recipeOptional.get();

			if ((this.grinds + 1) < recipe.value().getGrinds()) {
				grinds++;

				for (int i = 0; i < 1 + level.random.nextInt(4); i++)
					level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, getInsertedItem()),
							worldPosition.getX() + 0.25f + level.random.nextDouble() / 2,
							worldPosition.getY() - 0.5f - level.random.nextDouble(),
							worldPosition.getZ() + 0.25f + level.random.nextDouble() / 2, 0, 0, 0);

				level.playSound(Player, worldPosition, SoundEvents.STONE_HIT, SoundSource.BLOCKS, 1, 1);
			} else {
				ItemStack in = getInsertedItem();

				for (int i = 0; i < in.getCount(); i++) {

					ItemStack r = recipe.value().getResultItem(this.level.registryAccess()).copy();

					level.addFreshEntity(new ItemEntity(level, getBlockPos().getX(), getBlockPos().getY() + 0.5f,
							getBlockPos().getZ(), r));
				}
				items.setStackInSlot(0, ItemStack.EMPTY);
			}
			updateInventory();
		});

		return InteractionResult.SUCCESS;
	}

	@Override
	public void setRecipeUsed(RecipeHolder<?> p_300902_) {
		// TODO Auto-generated method stub

	}

	@Override
	public RecipeHolder<?> getRecipeUsed() {
		// TODO Auto-generated method stub
		return null;
	}
}
