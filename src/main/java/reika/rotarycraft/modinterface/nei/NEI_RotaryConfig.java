///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.modinterface.nei;
//
//import java.util.ArrayList;
//
//import net.minecraft.block.Block;
//import net.minecraft.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.neoforged.oredict.OreDictionary;
//
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.dragonapi.modregistry.ModOreList;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.auxiliary.CustomExtractLoader;
//import reika.rotarycraft.auxiliary.recipemanagers.ExtractorModOres;
//import reika.rotarycraft.gui.machine.inventory.GuiWorktable;
//import reika.rotarycraft.registry.BlockRegistry;
//import reika.rotarycraft.registry.ExtractorBonus;
//import reika.rotarycraft.registry.GearboxTypes;
//import reika.rotarycraft.registry.GearboxTypes.GearPart;
//import reika.rotarycraft.registry.RotaryItems;
//import reika.rotarycraft.registry.MachineRegistry;
//
//import codechicken.nei.api.API;
//import codechicken.nei.api.IConfigureNEI;
//import codechicken.nei.recipe.DefaultOverlayHandler;
//
//public class NEI_RotaryConfig implements IConfigureNEI {
//
//	private static final WorktableRecipeHandler worktable = new WorktableRecipeHandler();
//	private static final ToolChargingHandler toolCharge = new ToolChargingHandler();
//	private static final ToolCraftingHandler toolCrafting = new ToolCraftingHandler();
//	private static final BlastFurnaceHandler blastFurnace = new BlastFurnaceHandler();
//	private static final GrinderHandler grinder = new GrinderHandler();
//	private static final ExtractorHandler extractor = new ExtractorHandler();
//	private static final FermenterHandler fermenter = new FermenterHandler();
//	private static final CompactorHandler compactor = new CompactorHandler();
//	private static final PulseJetHandler pulsejet = new PulseJetHandler();
//	private static final FractionHandler fractionator = new FractionHandler();
//	private static final CrystallizerHandler crystallizer = new CrystallizerHandler();
//	private static final FridgeHandler fridge = new FridgeHandler();
//	private static final FillingStationHandler filling = new FillingStationHandler();
//	//private static final RightClickHandler rightclick = new RightClickHandler();
//	private static final FrictionHandler friction = new FrictionHandler();
//	private static final ComposterHandler compost = new ComposterHandler();
//	private static final LavaMakerHandler melter = new LavaMakerHandler();
//	private static final CentrifugeHandler centrifuge = new CentrifugeHandler();
//	private static final DryingBedHandler dryingbed = new DryingBedHandler();
//	private static final WetterHandler wetter = new WetterHandler();
//	private static final FuelEnhancerHandler fuelenhancer = new FuelEnhancerHandler();
//	private static final DropProcessorHandler dropprocessor = new DropProcessorHandler();
//
//	private static final NEITabOccluder occlusion = new NEITabOccluder();
//
//	@Override
//	public void loadConfig() {
//		RotaryCraft.LOGGER.info("Loading NEI Compatibility!");
//
//		API.registerNEIGuiHandler(occlusion);
//
//		API.registerRecipeHandler(worktable);
//		API.registerUsageHandler(worktable);
//		API.registerGuiOverlayHandler(GuiWorktable.class, new DefaultOverlayHandler(), "crafting");
//
//		API.registerRecipeHandler(toolCharge);
//		API.registerUsageHandler(toolCharge);
//
//		API.registerRecipeHandler(toolCrafting);
//		API.registerUsageHandler(toolCrafting);
//
//		API.registerRecipeHandler(blastFurnace);
//		API.registerUsageHandler(blastFurnace);
//
//		API.registerRecipeHandler(grinder);
//		API.registerUsageHandler(grinder);
//
//		API.registerRecipeHandler(extractor);
//		API.registerUsageHandler(extractor);
//
//		API.registerRecipeHandler(fermenter);
//		API.registerUsageHandler(fermenter);
//
//		API.registerRecipeHandler(compactor);
//		API.registerUsageHandler(compactor);
//
//		API.registerRecipeHandler(pulsejet);
//		API.registerUsageHandler(pulsejet);
//
//		API.registerRecipeHandler(fractionator);
//		API.registerUsageHandler(fractionator);
//
//		API.registerRecipeHandler(crystallizer);
//		API.registerUsageHandler(crystallizer);
//
//		API.registerRecipeHandler(fridge);
//		API.registerUsageHandler(fridge);
//
//		API.registerRecipeHandler(filling);
//		API.registerUsageHandler(filling);
//
//		//API.registerRecipeHandler(rightclick);
//		//API.registerUsageHandler(rightclick);
//
//		API.registerRecipeHandler(friction);
//		API.registerUsageHandler(friction);
//
//		API.registerRecipeHandler(compost);
//		API.registerUsageHandler(compost);
//
//		API.registerRecipeHandler(melter);
//		API.registerUsageHandler(melter);
//
//		API.registerRecipeHandler(centrifuge);
//		API.registerUsageHandler(centrifuge);
//
//		API.registerRecipeHandler(dryingbed);
//		API.registerUsageHandler(dryingbed);
//
//		API.registerRecipeHandler(wetter);
//		API.registerUsageHandler(wetter);
//
//		API.registerRecipeHandler(fuelenhancer);
//		API.registerUsageHandler(fuelenhancer);
//
//		API.registerRecipeHandler(dropprocessor);
//		API.registerUsageHandler(dropprocessor);
//
//		RotaryCraft.LOGGER.info("Hiding technical blocks from NEI!");
//		for (int i = 0; i < BlockRegistry.blockList.length; i++) {
//			if (BlockRegistry.blockList[i].isTechnical())
//				this.hideBlock(BlockRegistry.blockList[i].get());
//		}
//
//		for (int i = 0; i < RotaryItems.itemList.length; i++) {
//			RotaryItems ir = RotaryItems.itemList[i];
//			if (ir.isContinuousCreativeMetadatas()) {
//				int max = ir.getNumberMetadatas()-1;
//				Item id = ir.get();
//				ArrayList<ItemStack> li = new ArrayList<>();
//				for (int k = 0; k < ir.getNumberMetadatas(); k++) {
//					li.add(ir.getStackOfMetadata(k));
//				}
//				API.setItemListEntries(id, li);
//			}
//		}
//
//		ArrayList<ItemStack> li = new ArrayList<>();
//		ArrayList<ItemStack> li2 = new ArrayList<>();
//		for (GearboxTypes gear : GearboxTypes.typeList) {
//			if (gear.isLoadable()) {
//				li.add(gear.getGearboxItem(2));
//				li.add(gear.getGearboxItem(4));
//				li.add(gear.getGearboxItem(8));
//				li.add(gear.getGearboxItem(16));
//
//				for (GearPart p : GearPart.list) {
//					li2.add(gear.getPart(p));
//				}
//			}
//		}
//		API.setItemListEntries(RotaryItems.GEARBOX.get(), li);
//		API.setItemListEntries(RotaryItems.GEARCRAFT.get(), li2);
//
//		li = new ArrayList<>();
//		for (int i = 0; i < MachineRegistry.machineList.length; i++) {
//			MachineRegistry m = MachineRegistry.machineList.get(i);
//			if (m.isAvailableInCreativeInventory() && !m.hasCustomPlacerItem())
//				li.add(m.getCraftedProduct());
//		}
//		API.setItemListEntries(RotaryItems.MACHINE.get(), li);
//
//		if (RotaryCraft.getInstance().isLocked()) {
//			for (int i = 0; i < RotaryItems.itemList.length; i++) {
//				RotaryItems ir = RotaryItems.itemList[i];
//				API.hideItem(new ItemStack(ir.get(), 1, OreDictionary.WILDCARD_VALUE));
//			}
//		}
//
//		for (int i = 0; i < ModOreList.oreList.length; i++) {
//			ModOreList ore = ModOreList.oreList[i];
//			if (!ore.existsInGame() && !ExtractorBonus.isGivenAsBonus(ore)) {
//				API.hideItem(ExtractorModOres.getDustProduct(ore));
//				API.hideItem(ExtractorModOres.getSlurryProduct(ore));
//				API.hideItem(ExtractorModOres.getSolutionProduct(ore));
//				API.hideItem(ExtractorModOres.getFlakeProduct(ore));
//
//				ItemStack out = RotaryItems.MODINGOTS.getStackOfMetadata(ore.ordinal()); //NOT SMELTED INGOT!
//				if (!ReikaItemHelper.isVanillaItem(out))
//					API.hideItem(out);
//				RotaryCraft.LOGGER.info("Hiding ore "+ore+" Extractor products from NEI, as the ore is unused or does not exist.");
//			}
//		}
//
//		if (CustomExtractLoader.instance.getEntries().isEmpty()) {
//			API.hideItem(RotaryItems.CUSTOMEXTRACT.getStackOfMetadata(0));
//			API.hideItem(RotaryItems.CUSTOMINGOT.getStackOfMetadata(0));
//		}
//
//		RotaryCraft.LOGGER.info("Done loading NEI compatibility.");
//	}
//
//	private void hideBlock(Block b) {
//		API.hideItem(new ItemStack(b, 1, OreDictionary.WILDCARD_VALUE));
//	}
//
//	@Override
//	public String getName() {
//		return "RotaryCraft NEI Handlers";
//	}
//
//	@Override
//	public String getVersion() {
//		return "Gamma";
//	}
//
//}
