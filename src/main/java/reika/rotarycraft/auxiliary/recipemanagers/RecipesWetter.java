///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.auxiliary.recipemanagers;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.HashSet;
//
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.material.Fluid;
//import net.minecraftforge.fluids.FluidStack;
//
//import reika.dragonapi.instantiable.data.maps.ItemHashMap;
//import reika.dragonapi.instantiable.io.CustomRecipeList;
//import reika.dragonapi.instantiable.io.LuaBlock;
//import reika.dragonapi.libraries.java.ReikaJavaLibrary;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.api.RecipeInterface;
//import reika.rotarycraft.api.RecipeInterface.WetterManager;
//import reika.rotarycraft.registry.MachineRegistry;
//
//public class RecipesWetter extends RecipeHandler implements WetterManager {
//
//	private static final RecipesWetter WetterBase = new RecipesWetter();
//
//	private final ItemHashMap<HashMap<Fluid, WettingRecipe>> recipeList = new ItemHashMap();
//	private final HashSet<Fluid> fluids = new HashSet<>();
//
//	public static final RecipesWetter getRecipes()
//	{
//		return WetterBase;
//	}
//
//	private RecipesWetter() {
//		super(MachineRegistry.WETTER);
//		RecipeInterface.wetter = this;
//
//		this.addRecipe(new ItemStack(Blocks.SAND), "rc lubricant", 500, new ItemStack(Blocks.SOUL_SAND), 200, RecipeLevel.PROTECTED);
//		this.addRecipe(new ItemStack(Blocks.SAND), "oil", 125, new ItemStack(Blocks.SOUL_SAND), 50, RecipeLevel.MODINTERACT);
//		this.addRecipe(new ItemStack(Blocks.COBBLESTONE), "rc jet fuel", 20, new ItemStack(Blocks.NETHERRACK), 80, RecipeLevel.PERIPHERAL);
//		this.addRecipe(new ItemStack(Blocks.COBBLESTONE), "ender", 50, new ItemStack(Blocks.END_STONE), 80, RecipeLevel.MODINTERACT);
//	}
//
//	public void addAPIRecipe(ItemStack in, Fluid f, int amount, ItemStack out, int time) {
//		this.addRecipe(in, f, amount, out, time, RecipeLevel.API);
//	}
//
//	private void addRecipe(ItemStack in, String s, int amount, ItemStack out, int time, RecipeLevel rl) {
//		Fluid f = FluidRegistry.getFluid(s);
//		if (f != null)
//			this.addRecipe(in, f, amount, out, time, rl);
//	}
//
//	private void addRecipe(ItemStack in, Fluid f, int amount, ItemStack out, int time, RecipeLevel rl) {
//		WettingRecipe wr = new WettingRecipe(in, new FluidStack(f, amount), out, time);
//		HashMap<Fluid, WettingRecipe> map = recipeList.get(in);
//		if (map == null) {
//			map = new HashMap();
//			recipeList.put(in, map);
//		}
//		map.put(f, wr);
//		fluids.add(f);
//		this.onAddRecipe(wr, rl);
//	}
//
//	public WettingRecipe getRecipe(ItemStack is, FluidStack liquid) {
//		HashMap<Fluid, WettingRecipe> map = recipeList.get(is);
//		if (map == null)
//			return null;
//		Fluid f = liquid.getFluid();
//		WettingRecipe wr = map.get(f);
//		return liquid.amount >= wr.fluid.amount ? wr : null;
//	}
//
//	public boolean isValidFluid(Fluid f) {
//		return fluids.contains(f); //todo name
//	}
//
//	public Collection<WettingRecipe> getAllRecipes() {
//		Collection<WettingRecipe> c = new ArrayList();
//		for (ItemStack is : recipeList.keySet()) {
//			HashMap<Fluid, WettingRecipe> map = recipeList.get(is);
//			c.addAll(map.values());
//		}
//		return c;
//	}
//
//	public boolean isWettable(ItemStack is) {
//		return recipeList.containsKey(is);
//	}
//
//	public boolean isWettableWith(ItemStack is, Fluid f) {
//		HashMap<Fluid, WettingRecipe> c = recipeList.get(is);
//		return c != null && c.containsKey(f);
//	}
//
//	public static class WettingRecipe implements MachineRecipe {
//
//		public final int duration;
//		private final FluidStack fluid;
//		private final ItemStack input;
//		private final ItemStack output;
//
//		private WettingRecipe(ItemStack in, FluidStack fin, ItemStack out, int time) {
//			input = in;
//			output = out;
//			fluid = fin;
//			duration = time;
//		}
//
//		public FluidStack getFluid() {
//			return fluid.copy();
//		}
//
//		public ItemStack getInput() {
//			return input.copy();
//		}
//
//		public ItemStack getOutput() {
//			return output.copy();
//		}
//
//		@Override
//		public String getUniqueID() {
//			return fluid.getFluid().getFluidType().getDescription()+":"+fluid.getAmount()+"+"+fullID(input)+">"+fullID(output)+"#"+duration; //todo getdescription or getdescriptionid
//		}
//
//		@Override
//		public String getAllInfo() {
//			return "Mixing "+fluid.getAmount()+" of "+fluid.getFluid().getFluidType().getDescription()+" into "+fullID(input)+" for "+fullID(output)+" over "+duration+" ticks"; //todo was getlocalizedname
//		}
//
//		@Override
//		public Collection<ItemStack> getAllUsedItems() {
//			return ReikaJavaLibrary.makeListFrom(input, output);
//		}
//
//	}
//
//	public Collection<WettingRecipe> getRecipesByResult(ItemStack result) {
//		Collection<WettingRecipe> c = new ArrayList();
//		for (WettingRecipe r : this.getAllRecipes()) {
//			if (ReikaItemHelper.matchStacks(r.output, result)) {
//				c.add(r);
//			}
//		}
//		return c;
//	}
//
//	public Collection<WettingRecipe> getRecipesByFluid(Fluid fluid) {
//		Collection<WettingRecipe> c = new ArrayList();
//		for (WettingRecipe r : this.getAllRecipes()) {
//			if (r.fluid.getFluid() == fluid) {
//				c.add(r);
//			}
//		}
//		return c;
//	}
//
//	public Collection<WettingRecipe> getRecipesUsing(ItemStack ingredient) {
//		HashMap<Fluid, WettingRecipe> map = recipeList.get(ingredient);
//		return map != null ? Collections.unmodifiableCollection(map.values()) : null;
//	}
//
//	@Override
//	public void addPostLoadRecipes() {
//
//	}
//
//	@Override
//	protected boolean removeRecipe(MachineRecipe recipe) {
//		WettingRecipe wr = (WettingRecipe)recipe;
//		boolean flag = false;
//		Collection<ItemStack> rem = new ArrayList();
//		for (ItemStack is : recipeList.keySet()) {
//			HashMap<Fluid, WettingRecipe> map = recipeList.get(is);
//			flag |= ReikaJavaLibrary.removeValuesFromMap(map, wr);
//			if (map.isEmpty())
//				rem.add(is);
//		}
//		for (ItemStack is : rem) {
//			recipeList.remove(is);
//		}
//		return flag;
//	}
//
//	@Override
//	protected boolean addCustomRecipe(String n, LuaBlock lb, CustomRecipeList crl) throws Exception {
//		ItemStack in = crl.parseItemString(lb.getString("input"), null, false);
//		ItemStack out = crl.parseItemString(lb.getString("output"), lb.getChild("output_nbt"), false);
//		this.verifyOutputItem(out);
//		String fluid = lb.getString("input_fluid");
//		Fluid f = null;//todo FluidRegistry.getFluid(fluid);
//		if (f == null)
//			throw new IllegalArgumentException("Fluid '"+fluid+"' does not exist!");
//		int amt = lb.getInt("input_amount");
//		int time = lb.getInt("duration");
//		this.addRecipe(in, f, amt, out, time, RecipeLevel.CUSTOM);
//		return true;
//	}
//}
