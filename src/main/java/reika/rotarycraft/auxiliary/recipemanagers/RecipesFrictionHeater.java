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
import java.util.Collections;

import net.minecraft.world.item.ItemStack;
import reika.dragonapi.ModList;
import reika.dragonapi.instantiable.io.CustomRecipeList;
import reika.dragonapi.instantiable.io.LuaBlock;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.api.RecipeInterface;
import reika.rotarycraft.api.recipeinterface.FrictionHeaterManager;
import reika.rotarycraft.registry.MachineRegistry;
import reika.dragonapi.instantiable.data.maps.ItemHashMap;

public class RecipesFrictionHeater extends RecipeHandler implements FrictionHeaterManager {

	private static final RecipesFrictionHeater instance = new RecipesFrictionHeater();

	private final ItemHashMap<FrictionRecipe> recipes = new ItemHashMap();
	private final ItemHashMap<Collection<FrictionRecipe>> outputs = new ItemHashMap();

	public static RecipesFrictionHeater getRecipes() {
		return instance;
	}

	private RecipesFrictionHeater() {
		super(MachineRegistry.FRICTION);
		RecipeInterface.friction = this;

		this.addRecipe(RotaryItems.tungstenflakes, RotaryItems.tungsteningot, 1350, 600, RecipeLevel.CORE);
		this.addRecipe(RotaryItems.silicondust, RotaryItems.silicon, 800, 200, RecipeLevel.PROTECTED);
	}

	private void addRecipe(ItemStack in, ItemStack out, int temp, int time, RecipeLevel rl) {
		FrictionRecipe rec = new FrictionRecipe(in, out, temp, time);
		recipes.put(in, rec);
		Collection<FrictionRecipe> c = outputs.get(out);
		if (c == null) {
			c = new ArrayList();
			outputs.put(out, c);
		}
		c.add(rec);
		this.onAddRecipe(rec, rl);
	}

	public void addCoreRecipe(ItemStack in, ItemStack out, int temp, int time) {
		this.addRecipe(in, out, temp, time, RecipeLevel.CORE);
	}

	public void addAPIRecipe(ItemStack in, ItemStack out, int temp, int time) {
		this.addRecipe(in, out, temp, time, RecipeLevel.API);
	}

	public void addCustomRecipe(ItemStack in, ItemStack out, int temp, int time) {
		this.addRecipe(in, out, temp, time, RecipeLevel.CUSTOM);
	}

	public FrictionRecipe getSmelting(ItemStack in, int temperature) {
		FrictionRecipe rec = recipes.get(in);
		return rec != null ? (temperature >= rec.requiredTemperature ? rec : null) : null;
	}

	public Collection<FrictionRecipe> getRecipesByOutput(ItemStack out) {
		Collection<FrictionRecipe> c = outputs.get(out);
		return c != null ? Collections.unmodifiableCollection(c) : null;
	}

	public FrictionRecipe getRecipeByInput(ItemStack in) {
		return recipes.get(in);
	}

	public static final class FrictionRecipe implements MachineRecipe {

		public final int requiredTemperature;
		public final int duration;
		private final ItemStack input;
		private final ItemStack output;

		private FrictionRecipe(ItemStack in, ItemStack out, int temp, int time) {
			requiredTemperature = temp;
			duration = Math.abs(time);
			input = in;
			output = out;
		}

		public ItemStack getInput() {
			return input.copy();
		}

		public ItemStack getOutput() {
			return output.copy();
		}

		@Override
		public String getUniqueID() {
			return fullID(input)+">"+fullID(output)+"@"+requiredTemperature+"#"+duration;
		}

		@Override
		public String getAllInfo() {
			return "Smelting "+fullID(input)+" into "+fullID(output)+" @ "+requiredTemperature+"C over "+duration+" ticks";
		}

		@Override
		public Collection<ItemStack> getAllUsedItems() {
			return ReikaJavaLibrary.makeListFrom(input, output);
		}
	}

	public Collection<ItemStack> getAllSmeltables() {
		return Collections.unmodifiableCollection(recipes.keySet());
	}

	@Override
	public void addPostLoadRecipes() {
		if (ModList.IMMERSIVEENG.isLoaded() && ModList.THERMALEXPANSION.isLoaded()) {
			this.addRecipe(ReikaItemHelper.lookupItem("ImmersiveEngineering:material:13"), ReikaItemHelper.lookupItem("ThermalExpansion:Rockwool:12"), 600, 400, RecipeLevel.MODINTERACT);
		}
	}

	@Override
	protected boolean removeRecipe(MachineRecipe recipe) {
		return recipes.removeValue((FrictionRecipe)recipe) && outputs.get(((FrictionRecipe)recipe).output).remove(recipe);
	}

	@Override
	protected boolean addCustomRecipe(String n, LuaBlock lb, CustomRecipeList crl) throws Exception {
		ItemStack in = crl.parseItemString(lb.getString("input"), null, false);
		ItemStack out = crl.parseItemString(lb.getString("output"), lb.getChild("output_nbt"), false);
		this.verifyOutputItem(out);
		this.addCustomRecipe(in, out, lb.getInt("temperature"), lb.getInt("duration"));
		return true;
	}

}
