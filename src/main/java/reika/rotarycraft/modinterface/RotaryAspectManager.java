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
//		ReikaThaumHelper.addAspects(RotaryItems.YEAST.get(), Aspect.EXCHANGE, 4);
//
//		ReikaThaumHelper.clearAspects(RotaryItems.HANDBOOK.get());
//
//		ReikaThaumHelper.addAspects(RotaryItems.BEDAXE.get(), Aspect.TOOL, 96);
//		ReikaThaumHelper.addAspects(RotaryItems.BEDPICK.get(), Aspect.TOOL, 96);
//		ReikaThaumHelper.addAspects(RotaryItems.BEDPICK.get(), Aspect.MINE, 48);
//		ReikaThaumHelper.addAspects(RotaryItems.BEDHOE.get(), Aspect.TOOL, 80);
//		ReikaThaumHelper.addAspects(RotaryItems.BEDHOE.get(), Aspect.HARVEST, 20);
//		ReikaThaumHelper.addAspects(RotaryItems.BEDSWORD.get(), Aspect.TOOL, 80);
//		ReikaThaumHelper.addAspects(RotaryItems.BEDSHEARS.get(), Aspect.TOOL, 80);
//		ReikaThaumHelper.addAspects(RotaryItems.BEDSHOVEL.get(), Aspect.TOOL, 72);
//		ReikaThaumHelper.addAspects(RotaryItems.BEDSICKLE.get(), Aspect.TOOL, 96);
//		ReikaThaumHelper.addAspects(RotaryItems.BEDSICKLE.get(), Aspect.HARVEST, 20);
//		if (ModList.MULTIPART.isLoaded())
//			ReikaThaumHelper.addAspects(RotaryItems.BEDSAW.get(), Aspect.TOOL, 80);
//		if (ModList.FORESTRY.isLoaded())
//			ReikaThaumHelper.addAspects(RotaryItems.BEDGRAFTER.get(), Aspect.TOOL, 72);
//
//		ReikaThaumHelper.addAspects(RotaryItems.STEELAXE.get(), Aspect.TOOL, 18);
//		ReikaThaumHelper.addAspects(RotaryItems.STEELPICK.get(), Aspect.TOOL, 18);
//		ReikaThaumHelper.addAspects(RotaryItems.STEELPICK.get(), Aspect.MINE, 9);
//		ReikaThaumHelper.addAspects(RotaryItems.STEELHOE.get(), Aspect.TOOL, 16);
//		ReikaThaumHelper.addAspects(RotaryItems.STEELHOE.get(), Aspect.HARVEST, 4);
//		ReikaThaumHelper.addAspects(RotaryItems.STEELSWORD.get(), Aspect.TOOL, 14);
//		ReikaThaumHelper.addAspects(RotaryItems.STEELSHEARS.get(), Aspect.TOOL, 14);
//		ReikaThaumHelper.addAspects(RotaryItems.STEELSHOVEL.get(), Aspect.TOOL, 12);
//		ReikaThaumHelper.addAspects(RotaryItems.STEELSICKLE.get(), Aspect.TOOL, 18);
//		ReikaThaumHelper.addAspects(RotaryItems.STEELSICKLE.get(), Aspect.HARVEST, 4);
//
//		ReikaThaumHelper.addAspects(RotaryItems.BEDLEGS.get(), Aspect.ARMOR, 140);
//		ReikaThaumHelper.addAspects(RotaryItems.BEDHELM.get(), Aspect.ARMOR, 100);
//		ReikaThaumHelper.addAspects(RotaryItems.BEDBOOTS.get(), Aspect.ARMOR, 80);
//		ReikaThaumHelper.addAspects(RotaryItems.BEDCHEST.get(), Aspect.ARMOR, 160);
//		ReikaThaumHelper.addAspects(RotaryItems.BEDREVEAL.get(), Aspect.ARMOR, 140, Aspect.SENSES, 20, Aspect.AURA, 20, Aspect.MAGIC, 20);
//
//		ReikaThaumHelper.addAspects(RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get(), Aspect.ARMOR, 160, Aspect.FLIGHT, 40);
//		ReikaThaumHelper.addAspects(RotaryItems.HSLA_STEEL_PACK.get(), Aspect.ARMOR, 40, Aspect.FLIGHT, 40);
//		ReikaThaumHelper.addAspects(RotaryItems.JETPACK.get(), Aspect.TOOL, 40, Aspect.FLIGHT, 40);
//
//		ReikaThaumHelper.addAspects(RotaryItems.BEDJUMP.get(), Aspect.ARMOR, 120, Aspect.TRAVEL, 20);
//		ReikaThaumHelper.addAspects(RotaryItems.JUMP.get(), Aspect.TOOL, 20, Aspect.TRAVEL, 20);
//
//		ReikaThaumHelper.addAspects(RotaryItems.BUCKET.getStackOfMetadata(0), Aspect.VOID, 1, Aspect.METAL, 13, Aspect.MOTION, 2, Aspect.MECHANISM, 2);
//		ReikaThaumHelper.addAspects(RotaryItems.BUCKET.getStackOfMetadata(1), Aspect.VOID, 1, Aspect.METAL, 13, Aspect.FIRE, 3, Aspect.ENERGY, 12);
//		ReikaThaumHelper.addAspects(RotaryItems.BUCKET.getStackOfMetadata(2), Aspect.VOID, 1, Aspect.METAL, 13, Aspect.ENERGY, 7, Aspect.PLANT, 3);
//
//		ReikaThaumHelper.addAspects(RotaryItems.SHELL.get(), Aspect.FIRE, 8, Aspect.WEAPON, 8);
//
//		ReikaThaumHelper.addAspects(RotaryItems.HSLA_STEEL_INGOT, Aspect.METAL, 10, Aspect.MECHANISM, 6);
//		ReikaThaumHelper.addAspects(RotaryItems.netherrackdust, Aspect.FIRE, 4);
//		ReikaThaumHelper.addAspects(RotaryItems.sludge, Aspect.ENERGY, 1);
//		ReikaThaumHelper.addAspects(RotaryItems.sawdust, Aspect.TREE, 1);
//		ReikaThaumHelper.addAspects(RotaryItems.anthracite, Aspect.FIRE, 4, Aspect.ENERGY, 4);
//		ReikaThaumHelper.addAspects(RotaryItems.coke, Aspect.FIRE, 2, Aspect.MECHANISM, 2);
//
//		ReikaThaumHelper.addAspects(RotaryItems.HANDBOOK.get(), Aspect.MIND, 4, Aspect.MECHANISM, 3, Aspect.TREE, 1);
//
//		ReikaThaumHelper.addAspects(BlockRegistry.BLASTGLASS.get(), Aspect.CRYSTAL, 4, Aspect.ARMOR, 8, Aspect.FIRE, 2);
//		ReikaThaumHelper.addAspects(BlockRegistry.BLASTPANE.get(), Aspect.CRYSTAL, 2, Aspect.ARMOR, 4, Aspect.FIRE, 1);
//
//		MachineAspectMapper.instance.register();
//
//		if (ModList.SATISFORESTRY.isLoaded()) {
//			Aspect a = (Aspect)SFAPI.genericLookups.getAspect();
//			ReikaThaumHelper.addAspects(RotaryItems.BEDAXE.get(), a, 16);
//			ReikaThaumHelper.addAspects(RotaryItems.BEDPICK.get(), a, 16);
//			ReikaThaumHelper.addAspects(RotaryItems.BEDSWORD.get(), a, 16);
//			ReikaThaumHelper.addAspects(RotaryItems.BEDLEGS.get(), a, 16);
//			ReikaThaumHelper.addAspects(RotaryItems.BEDHELM.get(), a, 16);
//			ReikaThaumHelper.addAspects(RotaryItems.BEDBOOTS.get(), a, 16);
//			ReikaThaumHelper.addAspects(RotaryItems.BEDCHEST.get(), a, 16);
//		}
//	}
//
//}
