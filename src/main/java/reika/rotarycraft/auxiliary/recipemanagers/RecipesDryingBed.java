/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.auxiliary.recipemanagers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import reika.dragonapi.Instantiable.Data.Maps.FluidHashMap;
import reika.dragonapi.instantiable.io.CustomRecipeList;
import reika.dragonapi.instantiable.io.LuaBlock;
import reika.dragonapi.libraries.ReikaFluidHelper;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.api.RecipeInterface;
import reika.rotarycraft.api.RecipeInterface.DryingBedManager;
import reika.rotarycraft.auxiliary.RotaryItems;
import reika.rotarycraft.registry.MachineRegistry;

public class RecipesDryingBed extends RecipeHandler implements DryingBedManager {

	private static final RecipesDryingBed DryingBase = new RecipesDryingBed();

	private final FluidHashMap<DryingRecipe> recipeList = new FluidHashMap();

	public static final RecipesDryingBed getRecipes()
	{
		return DryingBase;
	}

	private RecipesDryingBed() {
		super(MachineRegistry.DRYING);
		RecipeInterface.dryingbed = this;

		this.addRecipe(FluidRegistry.WATER, 250, RotaryItems.salt, RecipeLevel.CORE);
		this.addRecipe(FluidRegistry.LAVA, 1000, new ItemStack(Items.gold_nugget), RecipeLevel.PERIPHERAL);
		this.addRecipe("oil", 200, RotaryItems.tar, RecipeLevel.PERIPHERAL);
		this.addRecipe("for.honey", 400, new ItemStack(Items.slime_ball), RecipeLevel.PERIPHERAL);
		this.addRecipe("honey", 400, new ItemStack(Items.slime_ball), RecipeLevel.PERIPHERAL);

		this.addRecipe("chroma", 2000, new ItemStack(Items.emerald), RecipeLevel.MODINTERACT);
	}

	public static class DryingRecipe implements MachineRecipe {

		private final FluidStack input;
		private final ItemStack output;

		private DryingRecipe(FluidStack fs, ItemStack is) {
			input = fs;
			output = is;
		}

		@Override
		public String getUniqueID() {
			return input.getFluid().getName()+":"+input.amount+">"+fullID(output);
		}

		@Override
		public String getAllInfo() {
			return "Drying "+input.amount+" of "+input.getLocalizedName()+" into "+fullID(output);
		}

		@Override
		public Collection<ItemStack> getAllUsedItems() {
			return ReikaJavaLibrary.makeListFrom(ReikaFluidHelper.getFluidStackAsItem(input));
		}

		public ItemStack getOutput() {
			return output.copy();
		}

		public FluidStack getFluid() {
			return input.copy();
		}

	}

	public void addAPIRecipe(Fluid f, int amount, ItemStack out) {
		this.addRecipe(f, amount, out, RecipeLevel.API);
	}

	private void addRecipe(Fluid f, int amount, ItemStack out, RecipeLevel rl) {
		DryingRecipe rec = new DryingRecipe(new FluidStack(f, amount), out);
		recipeList.put(f, amount, rec);
		this.onAddRecipe(rec, rl);
	}

	private void addRecipe(String s, int amount, ItemStack out, RecipeLevel rl) {
		Fluid f = FluidRegistry.getFluid(s);
		if (f != null)
			this.addRecipe(f, amount, out, rl);
	}

	public ItemStack getDryingResult(FluidStack liquid) {
		Fluid f = liquid.getFluid();
		DryingRecipe cr = recipeList.getForValue(liquid);
		if (cr == null)
			return null;
		int req = cr.input.amount;
		if (req > liquid.amount)
			return null;
		return cr.output.copy();
	}

	public DryingRecipe getRecipe(ItemStack result) {
		for (DryingRecipe cr : recipeList.values()) {
			/*
			if (cr == null) {
				StringBuilder sb = new StringBuilder();
				sb.append("Looking up recipe for "+result+": "+ReikaFluidHelper.fluidStackToString(dr)+", despite being in the keyset of the map, returned null on get()!");
				sb.append("\nMap data: "+recipeList.toString());
				sb.append("\nReport this to Reika!");
				throw new IllegalStateException(sb.toString());
			}
			 */
			if (ReikaItemHelper.matchStacks(result, cr.output))
				return cr;
		}
		return null;
	}

	public int getRecipeConsumption(ItemStack result) {
		DryingRecipe cr = this.getRecipe(result);
		return cr != null ? cr.input.amount : 0;
	}

	public boolean isValidFluid(Fluid f) {
		return recipeList.containsKey(f);
	}

	public Collection<DryingRecipe> getAllRecipes() {
		HashSet<DryingRecipe> c = new HashSet();
		for (DryingRecipe cr : recipeList.values()) {
			c.add(cr);
		}
		return c;
	}

	@Override
	public void addPostLoadRecipes() {
		ArrayList<ItemStack> li = OreDictionary.getOres("rubber");
		if (li == null || li.isEmpty()) {
			li = OreDictionary.getOres("itemRubber");
		}
		if (li != null && !li.isEmpty())
			this.addRecipe("rc lubricant", 100, li.get(0), RecipeLevel.MODINTERACT);
	}

	@Override
	protected boolean removeRecipe(MachineRecipe recipe) {
		return recipeList.removeValue((DryingRecipe)recipe);
	}

	@Override
	protected boolean addCustomRecipe(String n, LuaBlock lb, CustomRecipeList crl) throws Exception {
		ItemStack out = crl.parseItemString(lb.getString("output"), lb.getChild("output_nbt"), false);
		this.verifyOutputItem(out);
		String fluid = lb.getString("input_fluid");
		Fluid f = FluidRegistry.getFluid(fluid);
		if (f == null)
			throw new IllegalArgumentException("Fluid '"+fluid+"' does not exist!");
		int amt = lb.getInt("input_amount");
		this.addRecipe(f, amt, out, RecipeLevel.CUSTOM);
		return true;
	}
}
