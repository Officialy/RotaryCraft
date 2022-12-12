///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.modinterface;
//
//import reika.dragonapi.ModList;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.auxiliary.RotaryItems;
//
//public class RotaryAspectManager {
//
//	public static void addThaumAspects() {
//		RotaryCraft.LOGGER.info("Adding ThaumCraft aspects.");
//		ReikaThaumHelper.addAspects(RotaryItems.canolaSeeds, Aspect.EXCHANGE, 2, Aspect.CROP, 1, Aspect.MECHANISM, 1);
//		ReikaThaumHelper.addAspects(RotaryItems.denseCanolaSeeds, Aspect.EXCHANGE, 16, Aspect.CROP, 8, Aspect.MECHANISM, 8);
//		ReikaThaumHelper.addAspects(RotaryItems.canolaHusks, Aspect.EXCHANGE, 2, Aspect.CROP, 1, Aspect.MECHANISM, 1);
//
//		ReikaThaumHelper.addAspects(ItemRegistry.YEAST.get(), Aspect.EXCHANGE, 4);
//
//		ReikaThaumHelper.clearAspects(ItemRegistry.HANDBOOK.get());
//
//		ReikaThaumHelper.addAspects(ItemRegistry.BEDAXE.get(), Aspect.TOOL, 96);
//		ReikaThaumHelper.addAspects(ItemRegistry.BEDPICK.get(), Aspect.TOOL, 96);
//		ReikaThaumHelper.addAspects(ItemRegistry.BEDPICK.get(), Aspect.MINE, 48);
//		ReikaThaumHelper.addAspects(ItemRegistry.BEDHOE.get(), Aspect.TOOL, 80);
//		ReikaThaumHelper.addAspects(ItemRegistry.BEDHOE.get(), Aspect.HARVEST, 20);
//		ReikaThaumHelper.addAspects(ItemRegistry.BEDSWORD.get(), Aspect.TOOL, 80);
//		ReikaThaumHelper.addAspects(ItemRegistry.BEDSHEARS.get(), Aspect.TOOL, 80);
//		ReikaThaumHelper.addAspects(ItemRegistry.BEDSHOVEL.get(), Aspect.TOOL, 72);
//		ReikaThaumHelper.addAspects(ItemRegistry.BEDSICKLE.get(), Aspect.TOOL, 96);
//		ReikaThaumHelper.addAspects(ItemRegistry.BEDSICKLE.get(), Aspect.HARVEST, 20);
//		if (ModList.MULTIPART.isLoaded())
//			ReikaThaumHelper.addAspects(ItemRegistry.BEDSAW.get(), Aspect.TOOL, 80);
//		if (ModList.FORESTRY.isLoaded())
//			ReikaThaumHelper.addAspects(ItemRegistry.BEDGRAFTER.get(), Aspect.TOOL, 72);
//
//		ReikaThaumHelper.addAspects(ItemRegistry.STEELAXE.get(), Aspect.TOOL, 18);
//		ReikaThaumHelper.addAspects(ItemRegistry.STEELPICK.get(), Aspect.TOOL, 18);
//		ReikaThaumHelper.addAspects(ItemRegistry.STEELPICK.get(), Aspect.MINE, 9);
//		ReikaThaumHelper.addAspects(ItemRegistry.STEELHOE.get(), Aspect.TOOL, 16);
//		ReikaThaumHelper.addAspects(ItemRegistry.STEELHOE.get(), Aspect.HARVEST, 4);
//		ReikaThaumHelper.addAspects(ItemRegistry.STEELSWORD.get(), Aspect.TOOL, 14);
//		ReikaThaumHelper.addAspects(ItemRegistry.STEELSHEARS.get(), Aspect.TOOL, 14);
//		ReikaThaumHelper.addAspects(ItemRegistry.STEELSHOVEL.get(), Aspect.TOOL, 12);
//		ReikaThaumHelper.addAspects(ItemRegistry.STEELSICKLE.get(), Aspect.TOOL, 18);
//		ReikaThaumHelper.addAspects(ItemRegistry.STEELSICKLE.get(), Aspect.HARVEST, 4);
//
//		ReikaThaumHelper.addAspects(ItemRegistry.BEDLEGS.get(), Aspect.ARMOR, 140);
//		ReikaThaumHelper.addAspects(ItemRegistry.BEDHELM.get(), Aspect.ARMOR, 100);
//		ReikaThaumHelper.addAspects(ItemRegistry.BEDBOOTS.get(), Aspect.ARMOR, 80);
//		ReikaThaumHelper.addAspects(ItemRegistry.BEDCHEST.get(), Aspect.ARMOR, 160);
//		ReikaThaumHelper.addAspects(ItemRegistry.BEDREVEAL.get(), Aspect.ARMOR, 140, Aspect.SENSES, 20, Aspect.AURA, 20, Aspect.MAGIC, 20);
//
//		ReikaThaumHelper.addAspects(ItemRegistry.BEDROCK_ALLOY_CHESTPLATE.get(), Aspect.ARMOR, 160, Aspect.FLIGHT, 40);
//		ReikaThaumHelper.addAspects(ItemRegistry.HSLA_STEEL_PACK.get(), Aspect.ARMOR, 40, Aspect.FLIGHT, 40);
//		ReikaThaumHelper.addAspects(ItemRegistry.JETPACK.get(), Aspect.TOOL, 40, Aspect.FLIGHT, 40);
//
//		ReikaThaumHelper.addAspects(ItemRegistry.BEDJUMP.get(), Aspect.ARMOR, 120, Aspect.TRAVEL, 20);
//		ReikaThaumHelper.addAspects(ItemRegistry.JUMP.get(), Aspect.TOOL, 20, Aspect.TRAVEL, 20);
//
//		ReikaThaumHelper.addAspects(ItemRegistry.BUCKET.getStackOfMetadata(0), Aspect.VOID, 1, Aspect.METAL, 13, Aspect.MOTION, 2, Aspect.MECHANISM, 2);
//		ReikaThaumHelper.addAspects(ItemRegistry.BUCKET.getStackOfMetadata(1), Aspect.VOID, 1, Aspect.METAL, 13, Aspect.FIRE, 3, Aspect.ENERGY, 12);
//		ReikaThaumHelper.addAspects(ItemRegistry.BUCKET.getStackOfMetadata(2), Aspect.VOID, 1, Aspect.METAL, 13, Aspect.ENERGY, 7, Aspect.PLANT, 3);
//
//		ReikaThaumHelper.addAspects(ItemRegistry.SHELL.get(), Aspect.FIRE, 8, Aspect.WEAPON, 8);
//
//		ReikaThaumHelper.addAspects(RotaryItems.HSLA_STEEL_INGOT, Aspect.METAL, 10, Aspect.MECHANISM, 6);
//		ReikaThaumHelper.addAspects(RotaryItems.netherrackdust, Aspect.FIRE, 4);
//		ReikaThaumHelper.addAspects(RotaryItems.sludge, Aspect.ENERGY, 1);
//		ReikaThaumHelper.addAspects(RotaryItems.sawdust, Aspect.TREE, 1);
//		ReikaThaumHelper.addAspects(RotaryItems.anthracite, Aspect.FIRE, 4, Aspect.ENERGY, 4);
//		ReikaThaumHelper.addAspects(RotaryItems.coke, Aspect.FIRE, 2, Aspect.MECHANISM, 2);
//
//		ReikaThaumHelper.addAspects(ItemRegistry.HANDBOOK.get(), Aspect.MIND, 4, Aspect.MECHANISM, 3, Aspect.TREE, 1);
//
//		ReikaThaumHelper.addAspects(BlockRegistry.BLASTGLASS.get(), Aspect.CRYSTAL, 4, Aspect.ARMOR, 8, Aspect.FIRE, 2);
//		ReikaThaumHelper.addAspects(BlockRegistry.BLASTPANE.get(), Aspect.CRYSTAL, 2, Aspect.ARMOR, 4, Aspect.FIRE, 1);
//
//		MachineAspectMapper.instance.register();
//
//		if (ModList.SATISFORESTRY.isLoaded()) {
//			Aspect a = (Aspect)SFAPI.genericLookups.getAspect();
//			ReikaThaumHelper.addAspects(ItemRegistry.BEDAXE.get(), a, 16);
//			ReikaThaumHelper.addAspects(ItemRegistry.BEDPICK.get(), a, 16);
//			ReikaThaumHelper.addAspects(ItemRegistry.BEDSWORD.get(), a, 16);
//			ReikaThaumHelper.addAspects(ItemRegistry.BEDLEGS.get(), a, 16);
//			ReikaThaumHelper.addAspects(ItemRegistry.BEDHELM.get(), a, 16);
//			ReikaThaumHelper.addAspects(ItemRegistry.BEDBOOTS.get(), a, 16);
//			ReikaThaumHelper.addAspects(ItemRegistry.BEDCHEST.get(), a, 16);
//		}
//	}
//
//}
