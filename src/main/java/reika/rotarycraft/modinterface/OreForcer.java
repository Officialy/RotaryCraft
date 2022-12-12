/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.modinterface;

import java.lang.reflect.Field;
import java.util.ArrayList;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import reika.dragonapi.ModList;
import reika.dragonapi.exception.ModReflectionException;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.dragonapi.modregistry.ModOreList;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.RotaryCraft;

import reika.rotarycraft.registry.MachineRegistry;

public final class OreForcer {

	public static final OreForcer instance = new OreForcer();

	private OreForcer() {

	}

	public void forceCompatibility() {
		for (int i = 0; i < ModList.modList.length; i++) {
			ModList mod = ModList.modList[i];
			if (mod.isLoaded()) {
				try {
					this.force(mod);
				} catch (NullPointerException | ClassCastException | IndexOutOfBoundsException |
						 IllegalArgumentException e) {
					RotaryCraft.LOGGER.error("Could not force compatibility with "+mod+". Reason: ");
					e.printStackTrace();
				} catch (ModReflectionException e) {
					RotaryCraft.LOGGER.error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("incomplete-switch")
	private void force(ModList api) {
		if (!api.isReikasMod())
			RotaryCraft.LOGGER.info("Forcing compatibility with "+api);
		switch (api) {
//			case APPENG -> this.intercraftQuartz();
//			case FORESTRY -> this.intercraftApatite();
			/*case THAUMCRAFT -> {
				if (RotaryConfig.COMMON.MODORES.get())
					this.registerThaumcraft();
				this.intercraftThaumcraft();
			}*/
			case MFFS -> this.intercraftForcicium();
//			case MEKANISM -> this.registerOsmium();
			case DARTCRAFT -> {
//				if (RotaryConfig.COMMON.MODORES.get())
//					this.registerDart();
//				this.intercraftDart();
				this.breakForceWrench();
			}
			case ARSMAGICA -> {
//				if (RotaryConfig.COMMON.MODORES.get())
//					this.registerMagica();
				this.intercraftMagica();
			}
			case TRANSITIONAL -> this.intercraftMagmanite();
//			case RAILCRAFT -> this.intercraftFirestone();
//			case IC2 -> this.convertUranium();
			case MAGICCROPS -> {
//				if (RotaryConfig.COMMON.MODORES.get())
//					this.registerEssence();
//				this.intercraftEssence();
			}
//			case MIMICRY -> this.intercraftMimichite();
//			case BOP -> this.intercraftAmethyst();
//			case FACTORIZATION -> this.intercraftDarkIron();
			case QCRAFT -> {
//				if (RotaryConfig.COMMON.MODORES.get())
//					this.registerQuantum();
//				this.intercraftQuantum();
			}
			case PROJRED -> {
//				this.intercraftPRGems();
//				this.intercraftNikolite();
			}
//			case GALACTICRAFT -> this.intercraftSilicon();
//			case DRACONICEVO -> this.intercraftDraconium();
		}
	}

/*	private void intercraftDraconium() {
		ItemStack dust = ReikaItemHelper.lookupItem(ModList.DRACONICEVO, "draconiumDust", 0);
		if (dust == null)
			throw new ModReflectionException(RotaryCraft.getInstance(), ModList.DRACONICEVO, "Null Item for Draconium");
		GameRegistry.addShapelessRecipe(dust, RotaryItems.getModOreIngot(ModOreList.DRACONIUM));
	}*/

/*	private void intercraftSilicon() {
		Item id = GalacticCraftHandler.getInstance().basicItemID;
		if (id == null)
			throw new ModReflectionException(RotaryCraft.getInstance(), ModList.GALACTICRAFT, "Null Item for Silicon");
		ItemStack silicon = new ItemStack(id, 1, GalacticCraftHandler.siliconMeta);
		GameRegistry.addShapelessRecipe(silicon, RotaryItems.getModOreIngot(ModOreList.SILICON));
	}*/

/*
	private void registerQuantum() {
		QuantumOreHandler.getInstance().forceOreRegistration();
	}
*/

/*
	private void intercraftQuantum() {
		if (QuantumOreHandler.getInstance().dustID != null) {
			ItemStack ore = new ItemStack(QuantumOreHandler.getInstance().dustID, 1, 0);
			GameRegistry.addShapelessRecipe(ore, RotaryItems.getModOreIngot(ModOreList.QUANTUM));
			RotaryCraft.LOGGER.info("RotaryCraft quantum dust can now be crafted into QCraft quantum dust!");
		}
	}
*/

/*
	private void intercraftDarkIron() {
		if (FactorizationHandler.getInstance().ingotID != null) {
			ItemStack ingot = new ItemStack(FactorizationHandler.getInstance().ingotID, 1, 0);
			GameRegistry.addShapelessRecipe(ingot, RotaryItems.getModOreIngot(ModOreList.DARKIRON));
			RotaryCraft.LOGGER.info("RotaryCraft dark iron ingots can now be crafted into Factorization equivalents!");
		}
	}
*/

/*
	private void intercraftMimichite() {
		if (MimicryHandler.getInstance().itemID != null) {
			ItemStack ore = new ItemStack(MimicryHandler.getInstance().itemID, 1, 0);
			GameRegistry.addShapelessRecipe(ore, RotaryItems.getModOreIngot(ModOreList.MIMICHITE));
			RotaryCraft.LOGGER.info("RotaryCraft mimichite items can now be crafted into Mimicry mimichite!");
		}
	}
*/

/*	private void intercraftAmethyst() {
		if (BoPBlockHandler.getInstance().gemItem != null) {
			ItemStack ore = GemTypes.Amethyst.getItem();
			GameRegistry.addShapelessRecipe(ore, RotaryItems.getModOreIngot(ModOreList.AMETHYST));
			RotaryCraft.LOGGER.info("RotaryCraft amethyst items can now be crafted into BoP amethyst!");
		}
	}*/

/*	private void registerEssence() {
		ModHandlerBase h = ModList.MAGICCROPS.getHandler("Handler");
		if (h instanceof MagicCropHandler)
			((MagicCropHandler)h).registerEssence();
		if (h instanceof LegacyMagicCropHandler)
			((LegacyMagicCropHandler)h).registerEssence();
		if (h instanceof VeryLegacyMagicCropHandler)
			((VeryLegacyMagicCropHandler)h).registerEssence();
	}*/

//	private void intercraftEssence() {
//		ItemStack ore = EssenceType.ESSENCE.getEssence();
//		if (ore != null) {
//			GameRegistry.addShapelessRecipe(ore, RotaryItems.getModOreIngot(ModOreList.ESSENCE));
//			RotaryCraft.LOGGER.info("RotaryCraft essence items can now be crafted into Magic Crops essence!");
//		}
//		else {
//			RotaryCraft.LOGGER.error("Could not find essence item, cannot add intercraft recipe!");
//		}
//	}

//	private void convertUranium() {
//		ItemStack u = IC2Handler.IC2Stacks.PURECRUSHEDU.getItem();
//		if (u == null)
//			throw new ModReflectionException(RotaryCraft.getInstance(), ModList.IC2, "Null ItemStack for Uranium");
//		if (IC2Handler.IC2Stacks.IRIDIUM.getItem() == null)
//			throw new ModReflectionException(RotaryCraft.getInstance(), ModList.IC2, "Null Item for Iridium");
//		GameRegistry.addShapelessRecipe(IC2Handler.IC2Stacks.IRIDIUM.getItem(), RotaryItems.getModOreIngot(ModOreList.IRIDIUM));
//		RotaryCraft.LOGGER.info("RotaryCraft iridium ingots can now be crafted into IC2 Iridium items!");
//		GameRegistry.addShapelessRecipe(u, RotaryItems.getModOreIngot(ModOreList.URANIUM));
//		RotaryCraft.LOGGER.info("RotaryCraft uranium ingots can now be crafted into IC2 purified crushed uranium!");
//	}

//	private void intercraftFirestone() {
//		Item item = RailcraftHandler.getInstance().firestoneID;
//		if (item == null)
//			throw new ModReflectionException(RotaryCraft.getInstance(), ModList.RAILCRAFT, "Null ItemStack for Firestone");
//		GameRegistry.addShapelessRecipe(new ItemStack(item), RotaryItems.getModOreIngot(ModOreList.FIRESTONE));
//		RotaryCraft.LOGGER.info("RotaryCraft firestone can now be crafted into RailCraft firestone!");
//	}

	private void intercraftMagmanite() {
		Class trans = ModList.TRANSITIONAL.getItemClass();
		try {
			Field f = trans.getField("MagmaDrop");
			Item i = (Item)f.get(null);
			if (i == null)
				throw new ModReflectionException(RotaryCraft.getInstance(), ModList.TRANSITIONAL, "Null ItemStack for Magmanite Drop");
//			GameRegistry.addShapelessRecipe(new ItemStack(i), RotaryItems.getModOreIngot(ModOreList.MAGMANITE));
			RotaryCraft.LOGGER.info("RotaryCraft magmanite can now be crafted into Transitional Assistance magmanite!");
		}
		catch (NoSuchFieldException e) {
			RotaryCraft.LOGGER.error("Transitional Assistance item field not found!");
		}
		catch (SecurityException e) {
			RotaryCraft.LOGGER.error("Cannot read Transitional Assistance items (Security Exception)! Magmanite not convertible!"+e.getMessage());
			e.printStackTrace();
		}
		catch (IllegalArgumentException e) {
			RotaryCraft.LOGGER.error("Illegal argument for reading Transitional Assistance items!");
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			RotaryCraft.LOGGER.error("Illegal access exception for reading Transitional Assistance items!");
			e.printStackTrace();
		}
	}

	private void intercraftMagica() {
		RotaryCraft.LOGGER.info("Adding ore item conversion recipes!");
		for (int i = 0; i < ModOreList.oreList.length; i++) {
			ModOreList o = ModOreList.oreList[i];
			if (o.isArsMagica()) {
//				ItemStack is = ArsMagicaHandler.getInstance().getItem(o);
//				if (is == null)
//					throw new ModReflectionException(RotaryCraft.getInstance(), ModList.ARSMAGICA, "Null ItemStack for Ars Magica "+o);
//				GameRegistry.addShapelessRecipe(is, RotaryItems.getModOreIngot(o));
				RotaryCraft.LOGGER.info(o.displayName+" can now be crafted with RotaryCraft equivalents!");
			}
		}
	}

//	private void registerMagica() {
//		ArsMagicaHandler.getInstance().forceOreRegistration();
//	}

//	private void registerOsmium() {
//		if (MekanismHandler.getInstance().oreID == null)
//			throw new ModReflectionException(RotaryCraft.getInstance(), ModList.MEKANISM, "Null Item for Osmium");
//		OreDictionary.registerOre("oreOsmium", new ItemStack(MekanismHandler.getInstance().oreID, 1, MekanismHandler.osmiumMeta));
//	}

	private void breakForceWrench() {
		try {
			Class api = Class.forName("bluedart.api.DartAPI");
			Field blacklist = api.getField("tileBlacklist");
			ArrayList list = (ArrayList)blacklist.get(null);
			RotaryCraft.LOGGER.info("Breaking force wrench on RotaryCraft machines!");
			for (int i = 0; i < MachineRegistry.machineList.length; i++) {
				Class machine = MachineRegistry.machineList.get(i).getTEClass();
				list.add(machine);
				RotaryCraft.LOGGER.info("Force wrench no longer works on "+MachineRegistry.machineList.get(i).getName()+"!");
			}
			blacklist.set(null, list);
		}
		catch (ClassNotFoundException e) {
			RotaryCraft.LOGGER.error("DartAPI class not found!");
			e.printStackTrace();
		}
		catch (NoSuchFieldException e) {
			RotaryCraft.LOGGER.error("DartAPI TileBlackList field not found!");
			e.printStackTrace();
		}
		catch (SecurityException e) {
			RotaryCraft.LOGGER.error("DartAPI class threw security exception! "+e.getMessage());
			e.printStackTrace();
		}
		catch (IllegalArgumentException e) {
			RotaryCraft.LOGGER.error("Could not add argument to DartAPI list!");
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			RotaryCraft.LOGGER.error("DartAPI class fields not accessible!");
			e.printStackTrace();
		}
	}

/*	private void intercraftDart() {
		GameRegistry.addShapelessRecipe(DartOreHandler.getInstance().getForceGem(), RotaryItems.getModOreIngot(ModOreList.FORCE));
		RotaryCraft.LOGGER.info("RotaryCraft force gems can now be crafted into DartCraft force gems!");
	}*/

//	private void registerDart() {
//		DartOreHandler.getInstance().forceOreRegistration();
//	}

	private void intercraftForcicium() {
		try {
			Class mf = ModList.MFFS.getItemClass();
			Field item = mf.getField("MFFSitemForcicium");
			ItemStack forc = new ItemStack((Item)item.get(null));
//			GameRegistry.addShapelessRecipe(forc, RotaryItems.getModOreIngot(ModOreList.MONAZIT));
			RotaryCraft.LOGGER.info("RotaryCraft monazit can now be crafted into MFFS forcicium!");
		}
		catch (NoSuchFieldException e) {
			RotaryCraft.LOGGER.error("MFFS item field not found!");
		}
		catch (SecurityException e) {
			RotaryCraft.LOGGER.error("Cannot read MFFS items (Security Exception)! Monazit not convertible!"+e.getMessage());
			e.printStackTrace();
		}
		catch (IllegalArgumentException e) {
			RotaryCraft.LOGGER.error("Illegal argument for reading MFFS items!");
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			RotaryCraft.LOGGER.error("Illegal access exception for reading MFFS items!");
			e.printStackTrace();
		}
	}

//	private void intercraftQuartz() {
//		ItemStack quartz = AppEngHandler.getInstance().getCertusQuartz();
//		if (quartz == null)
//			throw new ModReflectionException(RotaryCraft.getInstance(), ModList.APPENG, "Null ItemStack for Certus Quartz");
//		GameRegistry.addShapelessRecipe(quartz, RotaryItems.getModOreIngot(ModOreList.CERTUSQUARTZ));
//		RotaryCraft.LOGGER.info("RotaryCraft certus quartz can now be crafted into AppliedEnergistics certus quartz!");
//	}

//	private void intercraftApatite() {
//		Item id = ForestryHandler.ItemEntry.APATITE.getItem();
//		if (id == null)
//			throw new ModReflectionException(RotaryCraft.getInstance(), ModList.FORESTRY, "Null Item for Apatite");
//		ItemStack apatite = new ItemStack(id, 1, 0);
//		GameRegistry.addShapelessRecipe(apatite, RotaryItems.getModOreIngot(ModOreList.APATITE));
//		RotaryCraft.LOGGER.info("RotaryCraft apatite can now be crafted into Forestry apatite!");
//	}

/*	private void intercraftThaumcraft() {
		RotaryCraft.LOGGER.info("Adding ore item conversion recipes!");
		for (int i = 0; i < ModOreList.oreList.length; i++) {
			ModOreList o = ModOreList.oreList[i];
			if (o.isThaumcraft()) {
				ItemStack is = ThaumOreHandler.getInstance().getItem(o);
				GameRegistry.addShapelessRecipe(is, RotaryItems.getModOreIngot(o));
				if (is == null)
					throw new ModReflectionException(RotaryCraft.getInstance(), ModList.THAUMCRAFT, "Null ItemStack for Thaumcraft's "+o);
				RotaryCraft.LOGGER.info(o.displayName+" can now be crafted with RotaryCraft equivalents!");
			}
		}
	}*/

//	private void registerThaumcraft() {
//		ThaumOreHandler.getInstance().forceOreRegistration();
//	}

}
