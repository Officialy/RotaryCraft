/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************//*

package reika.rotarycraft.auxiliary.recipemanagers;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import reika.dragonapi.ModList;
import reika.dragonapi.instantiable.data.maps.ItemHashMap;
import reika.dragonapi.instantiable.io.CustomRecipeList;
import reika.dragonapi.instantiable.io.LuaBlock;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.dragonapi.libraries.mathsci.ReikaThermoHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.api.RecipeInterface;
import reika.rotarycraft.api.RecipeInterface.RockMelterManager;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryItems;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class RecipesLavaMaker extends RecipeHandler implements RockMelterManager {

	private static final RecipesLavaMaker recipes = new RecipesLavaMaker();

	public static final RecipesLavaMaker getRecipes() {
		return recipes;
	}

	private final ItemHashMap<MeltingRecipe> recipeList = new ItemHashMap();

	private RecipesLavaMaker() {
		super(MachineRegistry.LAVAMAKER);
		RecipeInterface.rockmelt = this;

		this.addRecipe(Blocks.STONE, Fluids.LAVA, 1000, 1000, ReikaThermoHelper.ROCK_MELT_ENERGY, RecipeLevel.PROTECTED);
		this.addRecipe(Blocks.COBBLESTONE, Fluids.LAVA, 500, 1000, 3120000, RecipeLevel.PROTECTED);
		this.addRecipe(Blocks.NETHERRACK, Fluids.LAVA, 2000, 600, 480000, RecipeLevel.PROTECTED);
		this.addRecipe(Blocks.STONE_BRICKS, Fluids.LAVA, 1000, 1200, 4160000, RecipeLevel.PROTECTED);

		this.addRecipe("stone", Fluids.LAVA, 1000, 1000, 5200000, RecipeLevel.PROTECTED);
		this.addRecipe("cobblestone", Fluids.LAVA, 500, 1000, 2820000, RecipeLevel.PROTECTED);

		this.addRecipe(RotaryItems.ETHANOL.get(), "rc ethanol", 1000, 180, 6000, RecipeLevel.CORE);
//todo		this.addRecipe(RotaryItems.CLEAN_SLUDGE, "rc ethanol", 1000, 180, 9000, RecipeLevel.CORE);
	}

	private static class MeltingRecipe implements MachineRecipe {

		private final ItemStack input;
		private final FluidStack output;
		private final int temperature;
		private final long requiredEnergy;

		private MeltingRecipe(ItemStack is, FluidStack fs, int t, long e) {
			input = is;
			output = fs;
			temperature = t;
			requiredEnergy = e;
		}

		@Override
		public String getUniqueID() {
			return fullID(input)+"@"+temperature+"#"+output.getFluid().toString()+":"+output.getAmount();
		}

		@Override
		public String getAllInfo() {
			return "Melting "+fullID(input)+"into "+output.getAmount()+" of "+output.toString()+" @ "+temperature+"C using "+requiredEnergy+" J";
		}

		@Override
		public Collection<ItemStack> getAllUsedItems() {
			return ReikaJavaLibrary.makeListFrom(input);
		}
	}

	private void addRecipe(String in, String out, int amt, int temperature, long energy, RecipeLevel rl) {
		if (this.validateFluid(out)) {
			ArrayList<ItemStack> li = OreDictionary.getOres(in);
			for (int i = 0; i < li.size(); i++)
				this.addRecipe(li.get(i), new FluidStack(FluidRegistry.getFluid(out), amt), temperature, energy, rl);
		}
	}

	private void addRecipe(String in, Fluid out, int amt, int temperature, long energy, RecipeLevel rl) {
		ArrayList<ItemStack> li = OreDictionary.getOres(in);
		for (ItemStack sin : li) {
			if (!recipeList.containsKey(sin))
				this.addRecipe(sin, new FluidStack(out, amt), temperature, energy, rl);
		}
	}

	private void addRecipe(ItemStack in, String out, int amt, int temperature, long energy, RecipeLevel rl) {
		if (this.validateFluid(out))
			this.addRecipe(in, new FluidStack(FluidRegistry.getFluid(out), amt), temperature, energy, rl);
	}

	private void addRecipe(Item in, String out, int amt, int temperature, long energy, RecipeLevel rl) {
		if (this.validateFluid(out))
			this.addRecipe(new ItemStack(in), new FluidStack(FluidRegistry.getFluid(out), amt), temperature, energy, rl);
	}

	private void addRecipe(Block in, String out, int amt, int temperature, long energy, RecipeLevel rl) {
		if (this.validateFluid(out))
			this.addRecipe(new ItemStack(in), new FluidStack(FluidRegistry.getFluid(out), amt), temperature, energy, rl);
	}

	private void addRecipe(Block in, Fluid out, int amt, int temperature, long energy, RecipeLevel rl) {
		this.addRecipe(new ItemStack(in), new FluidStack(out, amt), temperature, energy, rl);
	}

	private void addRecipe(Item in, Fluid out, int amt, int temperature, long energy, RecipeLevel rl) {
		this.addRecipe(new ItemStack(in), new FluidStack(out, amt), temperature, energy, rl);
	}

	public void addAPIRecipe(ItemStack in, FluidStack out, int temperature, long energy) {
		this.addRecipe(in, out, temperature, energy, RecipeLevel.API);
	}

	private void addRecipe(Block in, FluidStack out, int temperature, long energy, RecipeLevel rl) {
		this.addRecipe(new ItemStack(in), out, temperature, energy, rl);
	}

	private void addRecipe(Item in, FluidStack out, int temperature, long energy, RecipeLevel rl) {
		this.addRecipe(new ItemStack(in), out, temperature, energy, rl);
	}

	private void addRecipe(ItemStack in, FluidStack out, int temperature, long energy, RecipeLevel rl) {
		if (in != null) {
			MeltingRecipe rec = new MeltingRecipe(in, out, temperature, energy);
			recipeList.put(in, rec);
			this.onAddRecipe(rec, rl);
		}
		else {
			RotaryCraft.LOGGER.error("Null itemstack for recipe for "+out+"!");
		}
	}


	public FluidStack getMelting(ItemStack is) {
		MeltingRecipe r = recipeList.get(is);
		return r != null ? r.output.copy() : null;
	}

	public int getMeltTemperature(ItemStack is) {
		MeltingRecipe r = recipeList.get(is);
		return r != null ? r.temperature : Integer.MIN_VALUE;
	}

	public long getMeltingEnergy(ItemStack is) {
		MeltingRecipe r = recipeList.get(is);
		return r != null ? r.requiredEnergy : Integer.MIN_VALUE;
	}

	public boolean isValidFuel(ItemStack is) {
		return recipeList.containsKey(is);
	}

	public ArrayList<ItemStack> getSourceItems(Fluid f) {
		ArrayList<ItemStack> li = new ArrayList();
		for (ItemStack key : recipeList.keySet()) {
			MeltingRecipe r = recipeList.get(key);
			if (r.output.getFluid().equals(f))
				li.add(key.copy());
		}
		return li;
	}

	public Collection<ItemStack> getAllRecipes() {
		return Collections.unmodifiableCollection(recipeList.keySet());
	}

	@Override
	public void addPostLoadRecipes() {
		this.addRecipe("dustGlowstone", "glowstone", 250, 400, 80000, RecipeLevel.MODINTERACT);
		this.addRecipe(Blocks.GLOWSTONE, "glowstone", 1000, 500, 320000, RecipeLevel.MODINTERACT);
		this.addRecipe("dustRedstone", "redstone", 100, 600, 120000, RecipeLevel.MODINTERACT);
		this.addRecipe(Blocks.REDSTONE_BLOCK, "redstone", 900, 750, 1080000, RecipeLevel.MODINTERACT);
		this.addRecipe(Items.ENDER_PEARL, "ender", 250, 400, 240000, RecipeLevel.MODINTERACT);
		this.addRecipe("blockEnder", "ender", 1000, 400, 240000, RecipeLevel.MODINTERACT);
		this.addRecipe("dustCoal", "coal", ModList.ENDERIO.isLoaded() ? 90 : 100, 300, 60000, RecipeLevel.MODINTERACT);

		this.addRecipe(RotaryItems.DRY_ICE.get(), "rc co2", 200, 0, 6000, RecipeLevel.PERIPHERAL);

	*/
/*	if (ModList.THERMALFOUNDATION.isLoaded()) {
			ItemStack pyro = GameRegistry.findItemStack(ModList.THERMALFOUNDATION.modLabel, "dustPyrotheum", 1);
			this.addRecipe(pyro, "pyrotheum", 250, 1800, 9000000, RecipeLevel.MODINTERACT);

			ItemStack cryo = GameRegistry.findItemStack(ModList.THERMALFOUNDATION.modLabel, "dustCryotheum", 1);
			this.addRecipe(cryo, "cryotheum", 250, -200, 2000, RecipeLevel.MODINTERACT);

			ItemStack petro = GameRegistry.findItemStack(ModList.THERMALFOUNDATION.modLabel, "dustPetrotheum", 1);
			this.addRecipe(petro, "petrotheum", 250, 800, 12000000, RecipeLevel.MODINTERACT);

			ItemStack aero = GameRegistry.findItemStack(ModList.THERMALFOUNDATION.modLabel, "dustAerotheum", 1);
			this.addRecipe(aero, "aerotheum", 250, 400, 40000, RecipeLevel.MODINTERACT);
		}*//*


		this.addRecipe("shardCrystal", "potion crystal", 8000, 500, 80000, RecipeLevel.MODINTERACT);

//		if (ModList.MAGICCROPS.isLoaded() && MagicCropHandler.EssenceType.XP.getEssence() != null)
//			this.addRecipe(MagicCropHandler.EssenceType.XP.getEssence(), "mobessence", 200, 600, 360000, RecipeLevel.MODINTERACT);
//
//		if (ModList.GEOSTRATA.isLoaded()) {
//			this.addLavaRock();
//		}
	}

*/
/*	@ModDependent(ModList.GEOSTRATA)
	private void addLavaRock() {
		for (int i = 0; i < 4; i++) {
			ItemStack is = new ItemStack(GeoBlocks.LAVAROCK.getBlockInstance(), 1, i);
			this.addRecipe(is, new FluidStack(Fluids.LAVA, 1000), 900-200*i, ReikaThermoHelper.ROCK_MELT_ENERGY/(i+1), RecipeLevel.MODINTERACT);
		}
	}*//*


	@Override
	protected boolean removeRecipe(MachineRecipe recipe) {
		return recipeList.removeValue((MeltingRecipe)recipe);
	}

	@Override
	protected boolean addCustomRecipe(String n, LuaBlock lb, CustomRecipeList crl) throws Exception {
		ItemStack in = crl.parseItemString(lb.getString("input"), null, false);
		LuaBlock fluid = lb.getChild("output_fluid");
		String s = fluid.getString("type");
//todo		Fluid f = FluidRegistry.getFluid(s);
//		if (f == null)
//			throw new IllegalArgumentException("Fluid '"+s+"' does not exist!");
//		this.verifyOutputFluid(f);
//		FluidStack fs = new FluidStack(f, fluid.getInt("amount"));
		int temp = lb.getInt("temperature");
		long energy = lb.getLong("energy");
//		this.addRecipe(in, fs, temp, energy, RecipeLevel.CUSTOM);
		return true;
	}

}
*/
