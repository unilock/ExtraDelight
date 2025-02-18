//package com.lance5057.extradelight.recipe;
//
//import com.mojang.serialization.Codec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//
//import net.minecraft.core.RegistryAccess;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.util.ExtraCodecs;
//import net.minecraft.world.Container;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.CookingBookCategory;
//import net.minecraft.world.item.crafting.Ingredient;
//import net.minecraft.world.item.crafting.RecipeSerializer;
//import net.minecraft.world.item.crafting.SmeltingRecipe;
//
//public class DynamicNameSmeltingRecipe extends SmeltingRecipe {
//
//	public DynamicNameSmeltingRecipe(String p_250200_, CookingBookCategory p_251114_, Ingredient p_250340_,
//			ItemStack p_250306_, float p_249577_, int p_250030_) {
//		super(p_250200_, p_251114_, p_250340_, p_250306_, p_249577_, p_250030_);
//	}
//
//	@Override
//	public ItemStack assemble(Container pInv, RegistryAccess p_267063_) {
//		ItemStack stack = this.result.copy();
//		ItemStack stackIn = pInv.getItem(0);
//
//		if (stackIn.hasTag()) {
//			CompoundTag tag = stackIn.getTag();
//
//			stack.getOrCreateTag().put("ingredients", tag.get("ingredients"));
//		}
//
//		return stack;
//	}
//
//	public static class Serializer implements RecipeSerializer<DynamicNameSmeltingRecipe> {
//		private static final Codec<DynamicNameSmeltingRecipe> CODEC = RecordCodecBuilder.create(inst -> inst.group(
//				ExtraCodecs.strictOptionalField(Codec.STRING, "group", "")
//						.forGetter(DynamicNameSmeltingRecipe::getGroup),
//				CookingBookCategory.CODEC.fieldOf("category").forGetter(r -> r.category()),
//				Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(p_301068_ -> p_301068_.ingredient),
//
//				ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").forGetter(r -> r.result),
//				ExtraCodecs.strictOptionalField(Codec.FLOAT, "experience", 1f).forGetter(r -> r.getExperience()),
//				ExtraCodecs.strictOptionalField(Codec.INT, "time", 1).forGetter(r -> r.getCookingTime())
//
//		).apply(inst, DynamicNameSmeltingRecipe::new));
//
//		public DynamicNameSmeltingRecipe fromNetwork(FriendlyByteBuf pBuffer) {
//			String s = pBuffer.readUtf();
//			Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
//			ItemStack itemstack = pBuffer.readItem();
//			float f = pBuffer.readFloat();
//			int i = pBuffer.readVarInt();
//			return new DynamicNameSmeltingRecipe(s, CookingBookCategory.MISC, ingredient, itemstack, f, i);
//		}
//
//		public void toNetwork(FriendlyByteBuf pBuffer, DynamicNameSmeltingRecipe pRecipe) {
//			pBuffer.writeUtf(pRecipe.group);
//			pRecipe.ingredient.toNetwork(pBuffer);
//			pBuffer.writeItem(pRecipe.result);
//			pBuffer.writeFloat(pRecipe.experience);
//			pBuffer.writeVarInt(pRecipe.cookingTime);
//		}
//
//		@Override
//		public Codec<DynamicNameSmeltingRecipe> codec() {
//			return CODEC;
//		}
//	}
//}
