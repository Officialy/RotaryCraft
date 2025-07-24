//package reika.rotarycraft;
//
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.block.Blocks;
//import net.neoforged.fml.loading.FMLLoader;
//import net.neoforged.registries.ForgeRegistries;
//import reika.dragonapi.ModList;
//import reika.dragonapi.auxiliary.trackers.ItemMaterialController;
//import reika.dragonapi.instantiable.ItemMaterial;
//import reika.dragonapi.instantiable.data.collections.OneWayCollections;
//import reika.dragonapi.instantiable.io.CustomRecipeList;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.auxiliary.RecyclingRecipe;
//import reika.rotarycraft.auxiliary.recipemanagers.RecipeHandler;
//import reika.rotarycraft.auxiliary.recipemanagers.RecipesGrinder;
//import reika.rotarycraft.registry.*;
//import reika.rotarycraft.registry.GearboxTypes.GearPart;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//
//public class RotaryRecipes {
//
//    private static void addProps() {
//        ItemMaterialController.instance.addItem(RotaryItems.HSLA_STEEL_SCRAP.get(), ItemMaterial.STEEL);
//        ItemMaterialController.instance.addItem(RotaryItems.IRON_SCRAP.get(), ItemMaterial.IRON);
//        ItemMaterialController.instance.addItem(RotaryBlocks.HSLA_STEEL_BLOCK.get().asItem(), ItemMaterial.STEEL);
//
//        ItemMaterialController.instance.addItem(RotaryItems.HSLA_STEEL_AXE.get(), ItemMaterial.STEEL);
//        ItemMaterialController.instance.addItem(RotaryItems.HSLA_STEEL_PICKAXE.get(), ItemMaterial.STEEL);
//        ItemMaterialController.instance.addItem(RotaryItems.HSLA_STEEL_SHOVEL.get(), ItemMaterial.STEEL);
//        ItemMaterialController.instance.addItem(RotaryItems.HSLA_HELMET.get(), ItemMaterial.STEEL);
//        ItemMaterialController.instance.addItem(RotaryItems.HSLA_LEGGINGS.get(), ItemMaterial.STEEL);
//        ItemMaterialController.instance.addItem(RotaryItems.HSLA_BOOTS.get(), ItemMaterial.STEEL);
//        ItemMaterialController.instance.addItem(RotaryItems.HSLA_CHESTPLATE.get(), ItemMaterial.STEEL);
//        ItemMaterialController.instance.addItem(RotaryItems.HSLA_STEEL_SWORD.get(), ItemMaterial.STEEL);
////        ItemMaterialController.instance.addItem(RotaryItems.HSLA_STEEL_SICKLE.get(), ItemMaterial.STEEL);
//        ItemMaterialController.instance.addItem(RotaryItems.HSLA_STEEL_SHEARS.get(), ItemMaterial.STEEL);
//    }
//
//    public static void addPostLoadRecipes() {
//
//
//        Object[] bin = getBlastFurnaceIngredients();
////        addOreRecipeToBoth(MachineRegistry.BLASTFURNACE.getCraftedProduct(), bin);
//        RotaryCraft.LOGGER.info("Blast Furnace materials set to " + Arrays.toString(bin));
//
//        bin = getWorktableIngredients();
////        addRecipeToBoth(MachineRegistry.WORKTABLE.getCraftedProduct(), bin);
//        RotaryCraft.LOGGER.info("Worktable materials set to " + Arrays.toString(bin));
//
//        addProps();
//
////        CustomRecipeList.addFieldLookup("rotarycraft_stack", RotaryItems.CLASS);
//        for (RecipeHandler h : recipeHandlers) {
//            h.addPostLoadRecipes();
//            h.loadCustomRecipeFiles();
//        }
//
//        //RecipesExtractor.recipes().addModRecipes();
//
////        MachineRegistry.COMPRESSOR.addCrafting("SsS", " G ", "CPC", 's', getConverterGatingItem(), 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'G', Blocks.GLASS, 'P', Blocks.PISTON, 'C', RotaryItems.COMPRESSOR);
//
////        MachineRegistry.PNEUENGINE.addCrafting("ppS", "sT ", "PPP", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 's', RotaryItems.HSLA_SHAFT, 'p', RotaryItems.PIPE, 'P', RotaryItems.BASE_PANEL, 'T', RotaryItems.IMPELLER);
//
//        ItemStack plate = ModList.RAILCRAFT.isLoaded() ? new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(ModList.RAILCRAFT.modid, "part.plate")), 1) : null; //todo modern railcraft?
//        MachineRegistry.STEAMTURBINE.addCrafting("sPs", "GTG", "ScS", 's', ConfigRegistry.HARDCONVERTERS.getState() && plate != null ? plate : RotaryItems.HSLA_STEEL_INGOT.get(), 'c', RotaryItems.DIAMOND_SHAFT_CORE, 'G', Blocks.GLASS, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'T', RotaryItems.TURBINE, 'P', RotaryItems.BASEPANEL);
//
////        MachineRegistry.BOILER.addCrafting("SPS", "G G", "sIs", 's', getConverterGatingItem(), 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'I', RotaryItems.IMPELLER, 'P', RotaryItems.PIPE, 'G', Blocks.GLASS);
//
////        MachineRegistry.GENERATOR.addCrafting("gpS", "iGs", "psp", 'S', getConverterGatingItem(), 'p', RotaryItems.BASE_PANEL, 'g', Items.GOLD_INGOT, 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'G', RotaryItems.GENERATOR, 'i', RotaryItems.IMPELLER, 's', RotaryItems.HSLA_SHAFT_CORE);
//
//        ItemStack cool = null;
//      /*  if (ModList.IC2.isLoaded()) {
//            if (ConfigRegistry.IC2BLAZECOMPRESS.getState()) {
//                changeIC2BlazePowderCompression();
//            }
//            cool = IC2Stacks.COOLANT6.getItem();
//        }*/
////        MachineRegistry.ELECTRICMOTOR.addCrafting("cGS", "BCB", "SGS", 'c', ConfigRegistry.HARDCONVERTERS.getState() && cool != null ? cool : RotaryItems.HSLA_STEEL_INGOT.get(), 'G', RotaryItems.GOLDCOIL, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'B', RotaryItems.BASEPANEL, 'C', RotaryItems.DIAMONDSHAFTCORE);
//
//        ItemStack coil = ModList.THERMALEXPANSION.isLoaded() ? GameRegistry.findItemStack(ModList.THERMALEXPANSION.modid, "powerCoilSilver", 1) : RotaryItems.POWER;
////        MachineRegistry.DYNAMO.addOreRecipe(" C ", "GiG", "IRI", 'C', coil, 'i', getConverterGatingItem(), 'I', RotaryItems.HSLA_STEEL_INGOT.get(), 'G', RotaryItems.STEELGEAR, 'R', Items.REDSTONE);
//        coil = ModList.THERMALEXPANSION.isLoaded() ? GameRegistry.findItemStack(ModList.THERMALEXPANSION.modid, "powerCoilGold", 1) : RotaryItems.GOLDCOIL;
//        Object ps = new PreferentialItemStack(Items.IRON_INGOT, "ingotLead").blockItem(RotaryItems.MODINGOTS.getItemInstance()).getItem();
//        Item crys = ModList.BUILDCRAFT.isLoaded() ? GameRegistry.findItem(ModList.BCSILICON.modid, "redstoneCrystal") : null;
//        MachineRegistry.MAGNETIC.addTagRecipe("LCl", "scs", "PSP", 'L', ConfigRegistry.HARDCONVERTERS.getState() && crys != null ? crys : ps, 'c', RotaryItems.CONDUCTIVE.getItem(), 'C', coil, 'P', RotaryItems.BASEPANEL, 'S', RotaryItems.DIAMONDSHAFTCORE, 'l', ps, 's', "ingotSilver");
//
//        ItemStack enderium = ModList.THERMALFOUNDATION.isLoaded() ? GameRegistry.findItemStack(ModList.THERMALFOUNDATION.modid, "ingotEnderium", 1) : RotaryItems.BEDROCK_ALLOY_INGOT.get();
//        ItemStack electrum = ModList.THERMALFOUNDATION.isLoaded() ? GameRegistry.findItemStack(ModList.THERMALFOUNDATION.modid, "ingotElectrum", 1) : RotaryItems.REDGOLDINGOT;
////        RotaryItems.UPGRADE.addMetaRecipe(Upgrades.FLUX.ordinal(), "BeB", "tEt", "BeB", 'e', electrum, 'B', RotaryItems.BASEPANEL, 'E', enderium, 't', RotaryItems.TUNGSTENINGOT);
//
////        ItemStack ender = ModList.BCSILICON.isLoaded() ? ItemRedstoneChipset.Chipset.PULSATING.getStack() : new ItemStack(Items.ENDER_PEARL);
////        ItemStack latch = ReikaItemHelper.lookupItem("ProjRed|Integration:projectred.integration.gate:17");
////        if (latch == null)
////            latch = new ItemStack(Items.CLOCK);
////        RotaryItems.UPGRADE.addMetaRecipe(9, "rQr", "qsq", "rEr", 'r', Items.REDSTONE, 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'q', Items.QUARTZ, 'E', ender, 'Q', latch);
//
//        /*if (ModList.TINKERER.isLoaded()) {
//            GameRegistry.addRecipe(BlockRegistry.DECOTANK.getCraftedMetadataProduct(4, 1), "SGS", "GGG", "SGS", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'G', new ItemStack(TinkerBlockHandler.getInstance().clearPaneID, 1, 0));
//
//            String f = "molten hsla";
//            int temp = 750;
//            ItemStack bk = RotaryItems.STEELBLOCK;
//            int base = SmelteryRecipeHandler.INGOT_AMOUNT;
//            SmelteryRecipeHandler.addIngotMelting(RotaryItems.HSLA_STEEL_INGOT.get(), bk, temp, f);
//            SmelteryRecipeHandler.addIngotCasting(RotaryItems.HSLA_STEEL_INGOT.get(), f, 40);
//            SmelteryRecipeHandler.addMelting(RotaryItems.STEELBLOCK, bk, temp, base * 9, f);
//            SmelteryRecipeHandler.addMelting(RotaryItems.SCRAP, bk, temp, base / 9, f);
//            SmelteryRecipeHandler.addMelting(RotaryItems.BALLBEARING, bk, temp, base / 4, f);
//            SmelteryRecipeHandler.addMelting(RotaryItems.WORMGEAR, bk, temp, base * 5 / DifficultyEffects.PARTCRAFT.getInt() + 2 * 27 / DifficultyEffects.PARTCRAFT.getInt(), f);
//            SmelteryRecipeHandler.addBlockCasting(RotaryItems.STEELBLOCK, base * 9, f, 120);
//            SmelteryRecipeHandler.addReversibleCasting(RotaryItems.GEARCAST, RotaryItems.STEELGEAR, bk, temp, f, base * 5 / DifficultyEffects.PARTCRAFT.getInt(), 80);
//            SmelteryRecipeHandler.addReversibleCasting(RotaryItems.DRILLCAST, RotaryItems.DRILL, bk, temp, f, base * 7, 80);
//            SmelteryRecipeHandler.addReversibleCasting(RotaryItems.PANELCAST, RotaryItems.BASEPANEL, bk, temp, f, base * 3 / DifficultyEffects.PARTCRAFT.getInt(), 80);
//            SmelteryRecipeHandler.addReversibleCasting(RotaryItems.SHAFTCAST, RotaryItems.SHAFTITEM, bk, temp, f, base * 3 / DifficultyEffects.PARTCRAFT.getInt(), 80);
//            SmelteryRecipeHandler.addReversibleCasting(RotaryItems.PROPCAST, RotaryItems.PROP, bk, temp, f, base * 3, 80);
//
//            SmelteryRecipeHandler.addMelting(RotaryItems.IRONSCRAP, new ItemStack(Blocks.IRON_BLOCK), 600, base, "iron.molten");
//
//            ArrayList<OreType> ores = ReikaJavaLibrary.makeListFrom(ModOreList.COPPER, ModOreList.TIN, ModOreList.ALUMINUM, ModOreList.SILVER, ModOreList.NICKEL, ModOreList.LEAD, ModOreList.PLATINUM, ModOreList.COBALT, ModOreList.ARDITE);
//            ores.addAll(0, ReikaJavaLibrary.makeListFromArray(ReikaOreHelper.oreList));
//
//            for (OreType ore : ores) {
//                if (ore.existsInGame()) {
//                    String suff = ore == ReikaOreHelper.EMERALD ? "liquid" : "molten";
//                    f = ore == ReikaOreHelper.REDSTONE ? "redstone" : ore.name().toLowerCase(Locale.ENGLISH) + "." + suff;
//                    if (FluidRegistry.isFluidRegistered(f)) {
//                        base = ore == ReikaOreHelper.EMERALD ? 640 : SmelteryRecipeHandler.INGOT_AMOUNT;
//                        int flakeYield = (int) (base * ore.getDropCount() * ConfigRegistry.getSmelteryFlakeYield());
//
//                        temp = ore == ModOreList.ALUMINUM || ore == ModOreList.TIN || ore == ModOreList.LEAD ? 300 : 600;
//                        if (ore == ModOreList.COBALT || ore == ModOreList.ARDITE)
//                            temp = 800;
//                        if (ore == ReikaOreHelper.GOLD || ore == ReikaOreHelper.REDSTONE)
//                            temp = 500;
//                        if (ore == ReikaOreHelper.EMERALD || ore == ReikaOreHelper.DIAMOND)
//                            temp = 700;
//                        ItemStack block = ore.getFirstOreBlock();
//                        if (ore instanceof ReikaOreHelper) {
//                            switch ((ReikaOreHelper) ore) {
//                                case COAL:
//                                    block = new ItemStack(Blocks.COAL_BLOCK);
//                                    break;
//                                case IRON:
//                                    block = new ItemStack(Blocks.IRON_BLOCK);
//                                    break;
//                                case GOLD:
//                                    block = new ItemStack(Blocks.GOLD_BLOCK);
//                                    break;
//                                case REDSTONE:
//                                    block = new ItemStack(Blocks.REDSTONE_BLOCK);
//                                    break;
//                                case LAPIS:
//                                    block = new ItemStack(Blocks.LAPIS_BLOCK);
//                                    break;
//                                case DIAMOND:
//                                    block = new ItemStack(Blocks.DIAMOND_BLOCK);
//                                    break;
//                                case EMERALD:
//                                    block = new ItemStack(Blocks.EMERALD_BLOCK);
//                                    break;
//                                case QUARTZ:
//                                    block = new ItemStack(Blocks.QUARTZ_BLOCK);
//                                    break;
//                            }
//                        } else if (ore instanceof ModOreList) {
//                            ArrayList<ItemStack> compact = OreDictionary.getOres("block" + ReikaStringParser.capFirstChar(ore.name()));
//                            if (!compact.isEmpty())
//                                block = compact.get(0);
//                        }
//                        if (ore instanceof ReikaOreHelper) {
//                            SmelteryRecipeHandler.addMelting(RotaryItems.GETFLAKE((ReikaOreHelper) ore), block, temp, flakeYield, f);
//                        } else if (ore instanceof ModOreList) {
//                            SmelteryRecipeHandler.addMelting(ExtractorModOres.getFlakeProduct((ModOreList) ore), block, temp, flakeYield, f);
//                            SmelteryRecipeHandler.addMelting(ExtractorModOres.getSmeltedIngot((ModOreList) ore), block, temp * 5 / 4, base, f);
//                        }
//                    }
//                }
//            }
//
//            //Bedrock parts
//            int id = ExtraConfigIDs.BEDROCKID.getValue();
//            int id2 = ExtraConfigIDs.HSLAID.getValue();
//            for (int i = 0; i < ToolParts.partList.length; i++) {
//                ToolParts p = ToolParts.partList[i];
//                if (TinkerMaterialHelper.instance.isPartEnabled(id, p)) {
//                    ItemStack is = p.getItem(id);
//                    if (is != null) {
//                        ItemStack part = p.getItem(id2);
//                        RecipesBlastFurnace.getRecipes().add3x3Crafting(is, 1000, 4, 0, " D ", "DPD", " D ", 'D', RotaryItems.BEDROCKDUST, 'P', part);
//                    }
//                }
//            }
//            for (int i = 0; i < WeaponParts.partList.length; i++) {
//                WeaponParts p = WeaponParts.partList[i];
//                if (TinkerMaterialHelper.instance.isPartEnabled(id, p)) {
//                    ItemStack is = p.getItem(id);
//                    if (is != null) {
//                        ItemStack part = p.getItem(id2);
//                        RecipesBlastFurnace.getRecipes().add3x3Crafting(is, 1000, 4, 0, " D ", "DPD", " D ", 'D', RotaryItems.BEDROCKDUST, 'P', part);
//                    }
//                }
//            }
//        }
//
//        if (ModList.PROJRED.isLoaded()) {
//            ItemStack saw = RotaryItems.BEDSAW.get();
//            ItemStack siliconWafer = GameRegistry.findItemStack(ModList.PROJRED.modid, "projectred.core.part", 1);
//            ItemStack siliconCylinder = GameRegistry.findItemStack(ModList.PROJRED.modid, "projectred.core.part", 1);
//            siliconWafer.setItemDamage(12);
//            siliconCylinder.setItemDamage(11);
//
//            GameRegistry.addRecipe(ReikaItemHelper.getSizedItemStack(siliconWafer, 16), "S", "s", 'S', saw, 's', siliconCylinder);
//        }
//
//        if (ModList.ENDERIO.isLoaded()) {
//            ItemStack eiosilicon = ReikaItemHelper.lookupItem("EnderIO:itemMaterial");
//            GameRegistry.addShapelessRecipe(eiosilicon, RotaryItems.SILICON);
//        }
//
//        if (ModList.THAUMCRAFT.isLoaded()) {
//            addThaumcraft();
//        }
//
//        if (ModList.BOTANIA.isLoaded()) {
//            ItemStack livingwood = new ItemStack(GameRegistry.findBlock(ModList.BOTANIA.modid, "livingwood"));
//            ItemStack livingwoodslab = new ItemStack(GameRegistry.findBlock(ModList.BOTANIA.modid, "livingwood0Slab"));
//            ItemStack livingrock = new ItemStack(GameRegistry.findBlock(ModList.BOTANIA.modid, "livingrock"));
//            ItemStack livingrockslab = new ItemStack(GameRegistry.findBlock(ModList.BOTANIA.modid, "livingrock0Slab"));
//
//            GameRegistry.addRecipe(GearboxTypes.LIVINGWOOD.getPart(GearboxTypes.GearPart.GEAR), " s ", "sss", " s ", 's', livingwood);
//            GameRegistry.addRecipe(GearboxTypes.LIVINGWOOD.getShaftUnitItem(), "  s", " s ", "s  ", 's', livingwood);
//
//            GameRegistry.addRecipe(ReikaItemHelper.getSizedItemStack(GearboxTypes.LIVINGROCK.getPart(GearboxTypes.GearPart.GEAR), 2), " s ", "sss", " s ", 's', livingrock);
//            GameRegistry.addRecipe(ReikaItemHelper.getSizedItemStack(GearboxTypes.LIVINGROCK.getShaftUnitItem(), 2), "  s", " s ", "s  ", 's', livingrock);
//        }
//
//        if (ModList.MINECHEM.isLoaded()) {
//            RecipeAPI.addDecompositionRecipe(RotaryItems.ETHANOL.get(), "8 ethanol");
//            RecipeAPI.addDecompositionRecipe(RotaryItems.HSLA_STEEL_INGOT.get().copy(), "8 Fe", "1 S", "0.25 P", "1 C", "0.5 Si");
//            RecipeAPI.addDecompositionRecipe(RotaryItems.ALUMINUMPOWDER.copy(), "1 Al");
//            MoleculeHelper.addMoleculeWithDecomposition("octane", "8 C", "18 H");
//            MoleculeHelper.addMoleculeWithDecomposition("hexane", "6 C", "14 H");
//            MoleculeHelper.addMoleculeWithDecomposition("decane", "10 C", "22 H");
//            MoleculeHelper.addMoleculeWithDecomposition("methylhexane", "7 C", "16 H");
//            MoleculeHelper.addMoleculeWithDecomposition("cyclohexane", "6 C", "12 H");
//            MoleculeHelper.addMoleculeWithDecomposition("methylcyclohexane", "7 C", "14 H");
//            MoleculeHelper.addMoleculeWithDecomposition("benzene", "6 C", "6 H");
//            MoleculeHelper.addMoleculeWithDecomposition("napthalene", "10 C", "8 H");
//            RecipeAPI.addDecompositionFluidRecipe(new FluidStack(FluidRegistry.getFluid("rc jet fuel"), 100), "4 octane", "2.2 hexane", "2.1 decane", "3.7 methylhexane", "1.2 cyclohexane", "2.3 methylcyclohexane", "0.5 benzene", "1.3 toluene", "0.5 napthalene");
//        }
//
//        if (ModList.IMMERSIVEENG.isLoaded()) {
//            RecipesBlastFurnace.getRecipes().add3x3Crafting(RotaryItems.BEDDRILL.get(), 1200, 2, 0, "bbb", "bdb", " b ", 'b', RotaryItems.BEDROCKDUST, 'd', RotaryItems.DRILL);
//        }
//
//        if (ModList.CHISEL.isLoaded()) {
//            RecipesBlastFurnace.getRecipes().add3x3Crafting(RotaryItems.BEDCHISEL.get(), 1200, 2, 0, " ob", " bo", "s  ", 'o', Blocks.OBSIDIAN, 'b', RotaryItems.BEDROCK_ALLOY_INGOT.get(), 's', RotaryItems.SHAFTITEM);
//        }
//
//        if (ModList.PROJRED.isLoaded() && ModList.APPENG.isLoaded()) {
//            MachineRegistry.BUNDLEDBUS.addCrafting("BrB", "CpF", "BrB", 'C', RotaryItems.PCB, 'B', RotaryItems.BASEPANEL, 'r', ReikaItemHelper.lookupItem("ProjRed|Transmission:projectred.transmission.wire:17"), 'p', AppEngHandler.getInstance().getGoldProcessor(), 'F', AppEngHandler.getInstance().getFluixCrystal());
//        }*/
//    }
//
//
//    public static ItemStack getConverterGatingItem() {
//        ItemStack ctr = RotaryItems.HSLA_STEEL_INGOT.get().getDefaultInstance();
//        switch (ConfigRegistry.LATEDYNAMO.getValue()) {
//            case 1:
//                ctr = RotaryItems.INDUCTIVE_INGOT.get().getDefaultInstance();
//                break;
//            case 2:
//                ctr = RotaryItems.TUNGSTEN_ALLOY_INGOT.get().getDefaultInstance();
//                break;
//            case 3:
//                ctr = RotaryItems.BEDROCK_ALLOY_INGOT.get().getDefaultInstance();
//                break;
//          /*todo
//               case 4:
//                if (ModList.REACTORCRAFT.isLoaded())
//                    ctr = CraftingItems.ALLOY.getItem();
//                break;
//            case 5:
//                if (ModList.REACTORCRAFT.isLoaded())
//                    ctr = ReactorStacks.maxMagnet;
//                break;*/
//            default:
//                break;
//        }
//        return ctr;
//    }
//
//
//    private static void addMachines() {
////        MachineRegistry.COMPACTOR.addCrafting("SPS", "PGP", "#P#", '#', RotaryItems.BASEPANEL, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'P', RotaryItems.PRESSHEAD, 'G', GearboxTypes.TUNGSTEN.getPart(GearPart.UNIT16));
//
////        MachineRegistry.FAN.addOreRecipe("WWW", "WIW", "#s#", '#', RotaryItems.BASEPANEL, 'W', "plankWood", 'I', RotaryItems.IMPELLER, 's', RotaryItems.SHAFTITEM);
//
//        MachineRegistry.AEROSOLIZER.addCrafting("BRB", "RIR", "BRB", 'B', RotaryItems.BASEPANEL, 'R', MachineRegistry.RESERVOIR.getCraftedProduct(), 'I', RotaryItems.IMPELLER);
//
//        MachineRegistry.HEATRAY.addCrafting("OOO", "BLb", "#P#", '#', RotaryItems.BASEPANEL, 'B', RotaryItems.BULB, 'b', RotaryItems.BARREL, 'P', RotaryItems.POWER, 'L', RotaryItems.LENS, 'O', Blocks.OBSIDIAN);
//
//        MachineRegistry.FLOODLIGHT.addCrafting("ISO", "Ggd", "I#O", '#', RotaryItems.BASEPANEL, 'I', Items.IRON_INGOT, 'd', Items.GOLD_INGOT, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'G', Blocks.GLASS, 'g', Blocks.GLOWSTONE, 'O', Blocks.OBSIDIAN);
//
//        if (ReikaItemHelper.oreItemsExist("ingotCopper", "ingotSilver"))
//            MachineRegistry.FLOODLIGHT.addTagRecipe("ISO", "Ggd", "I#O", '#', RotaryItems.BASEPANEL, 'I', "ingotSilver", 'd', "ingotCopper", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'G', Blocks.GLASS, 'g', Blocks.GLOWSTONE, 'O', Blocks.OBSIDIAN);
//
//        CompoundTag nbt = new CompoundTag();
//        nbt.putBoolean("cross", true);
////        MachineRegistry.CROSS_SHAFT.addNBTCrafting(nbt, " S ", "SSS", " M ", 'M', RotaryItems.MOUNT, 'S', RotaryItems.SHAFTITEM); //Shaft cross
//
//        //addRecipeToBoth(MachineRegistry.WORKTABLE.getCraftedProduct(), " C ", "SBS", "srs", 'r', Items.REDSTONE, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'B', Blocks.BRICK_BLOCK, 'C', Blocks.CRAFTING_TABLE, 's', ReikaItemHelper.stoneSlab);
//
//        MachineRegistry.BEVELGEARS.addSizedCrafting(4, "ISB", "SGB", "BBB", 'B', RotaryItems.BASEPANEL, 'I', RotaryItems.HSLA_STEEL_INGOT.get(), 'S', RotaryItems.SHAFTITEM, 'G', RotaryItems.STEELGEAR);
//
//        nbt = new CompoundTag();
//        nbt.putBoolean("bedrock", false);
//        MachineRegistry.SPLITTER.addSizedNBTCrafting(nbt, 2, "ISP", "SGP", "ISP", 'P', RotaryItems.BASEPANEL, 'I', RotaryItems.HSLA_STEEL_INGOT.get(), 'S', RotaryItems.SHAFTITEM, 'G', RotaryItems.STEELGEAR);
//
//        MachineRegistry.CLUTCH.addCrafting("S", "M", "R", 'M', RotaryItems.MOUNT, 'S', RotaryItems.SHAFTITEM, 'R', Items.REDSTONE);
//        //MachineRegistry.CLUTCH.addCrafting("S", "R", 'S', MachineRegistry.SHAFT.getCraftedMetadataProduct(2), 'R', Items.REDSTONE);
//
//        MachineRegistry.DYNAMOMETER.addSizedCrafting(2, " S ", " E ", " Ms", 's', RotaryItems.SCREEN, 'M', RotaryItems.MOUNT, 'S', RotaryItems.SHAFTITEM, 'E', Items.ENDER_PEARL);
//        MachineRegistry.DYNAMOMETER.addSizedCrafting(4, " S ", " E ", " Ms", 's', RotaryItems.SCREEN, 'M', RotaryItems.MOUNT, 'S', RotaryItems.SHAFTITEM, 'E', RotaryItems.SILICON);
//
////        MachineRegistry.BEDROCKBREAKER.addCrafting("BDt", "BSO", "BDt", 't', RotaryItems.TUNGSTENINGOT, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'D', Items.DIAMOND, 'O', Blocks.OBSIDIAN, 'B', RotaryItems.BASEPANEL);
//
////        MachineRegistry.FERMENTER.addCrafting("BPB", "PIP", "BPB", 'B', RotaryItems.HSLA_STEEL_INGOT.get(), 'I', RotaryItems.IMPELLER, 'P', RotaryItems.BASEPANEL);
////        MachineRegistry.FERMENTER.addOreRecipe("BPB", "PIP", "BPB", 'B', "ingotTin", 'I', RotaryItems.IMPELLER, 'P', RotaryItems.BASEPANEL);
//
//        MachineRegistry.GRINDER.addCrafting("B B", "SGS", "PPP", 'B', RotaryItems.HSLA_STEEL_INGOT.get(), 'G', RotaryItems.STEELGEAR, 'P', RotaryItems.BASEPANEL, 'S', RotaryItems.SAW);
//
//        MachineRegistry.RESERVOIR.addCrafting("B B", "B B", "BBB", 'B', RotaryItems.BASEPANEL);
//        nbt = new CompoundTag();
//        nbt.putBoolean("cover", true);
//        MachineRegistry.RESERVOIR.addNBTCrafting(nbt, "BPB", "B B", "BBB", 'B', RotaryItems.BASEPANEL, 'P', Blocks.GLASS_PANE);
//
////        MachineRegistry.FIREWORK.addCrafting("BEB", "BDB", "BRB", 'B', RotaryItems.BASEPANEL, 'R', Items.REDSTONE, 'E', Items.ENDER_EYE, 'D', Blocks.DISPENSER);
//
////        MachineRegistry.AUTOBREEDER.addCrafting("B B", "BBB", 'B', RotaryItems.BASEPANEL);
//
////        MachineRegistry.FRACTIONATOR.addCrafting("GFG", "GIG", "GPG", 'P', RotaryItems.BASEPANEL, 'I', RotaryItems.MIXER, 'G', Items.GOLD_INGOT, 'F', RotaryItems.FUELLINE);
//
////        MachineRegistry.BAITBOX.addCrafting("BBB", "BAB", "BBB", 'B', Blocks.IRON_BARS, 'A', MachineRegistry.AUTOBREEDER.getCraftedProduct());
//
//        MachineRegistry.WINDER.addCrafting(" ss", " hg", "ppp", 'h', RotaryItems.SHAFTITEM, 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'g', RotaryItems.GEARUNIT, 'p', RotaryItems.BASEPANEL);
//
////        MachineRegistry.ECU.addCrafting("IPI", "IGI", "IRI", 'I', RotaryItems.HSLA_STEEL_INGOT.get(), 'G', Items.GOLD_INGOT, 'P', RotaryItems.PCB, 'R', Items.REDSTONE);
//
////        if (ReikaItemHelper.oreItemExists("ingotElectrum"))
////            MachineRegistry.ECU.addOreRecipe("IPI", "IGI", "IRI", 'I', RotaryItems.HSLA_STEEL_INGOT.get(), 'G', "ingotElectrum", 'P', RotaryItems.PCB, 'R', Items.REDSTONE);
////        else if (ReikaItemHelper.oreItemExists("ingotCopper"))
////            MachineRegistry.ECU.addOreRecipe("IPI", "IGI", "IRI", 'I', RotaryItems.HSLA_STEEL_INGOT.get(), 'G', "ingotCopper", 'P', RotaryItems.PCB, 'R', Items.REDSTONE);
//
////        MachineRegistry.WOODCUTTER.addCrafting("IS ", "PGS", "PPI", 'I', RotaryItems.HSLA_STEEL_INGOT.get(), 'S', RotaryItems.SAW, 'P', RotaryItems.BASEPANEL, 'G', RotaryItems.GEARUNIT);
//
////        MachineRegistry.VACUUM.addCrafting("SwS", "wIw", "SCS", 'C', Blocks.CHEST, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'I', RotaryItems.IMPELLER, 'w', ReikaItemHelper.blackWool);
//
////        MachineRegistry.BORER.addCrafting("SSS", "DGC", "BBB", 'B', RotaryItems.BASEPANEL, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'D', RotaryItems.DRILL, 'G', RotaryItems.GEARUNIT, 'C', RotaryItems.PCB);
//
////        MachineRegistry.SPRINKLER.addSizedCrafting(4, " s ", " p ", " i ", 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'p', RotaryItems.PIPE, 'i', RotaryItems.IMPELLER);
////        MachineRegistry.SPRINKLER.addSizedOreRecipe(4, " s ", " p ", " i ", 's', "ingotTin", 'p', RotaryItems.PIPE, 'i', RotaryItems.IMPELLER);
//
////        MachineRegistry.SPAWNERCONTROLLER.addCrafting("PCP", "OGO", "g g", 'O', Blocks.OBSIDIAN, 'P', RotaryItems.BASEPANEL, 'G', Items.GOLD_INGOT, 'g', Blocks.GLOWSTONE, 'C', RotaryItems.PCB);
//
//        MachineRegistry.PLAYERDETECTOR.addCrafting("LRL", "OGO", "OPO", 'L', ReikaItemHelper.lapisDye, 'R', RotaryItems.RADAR, 'O', Blocks.OBSIDIAN, 'P', RotaryItems.BASEPANEL, 'G', Items.GOLD_INGOT);
//
//        MachineRegistry.OBSIDIAN.addCrafting("SpS", "PMP", "BBB", 'M', RotaryItems.MIXER, 'B', RotaryItems.BASEPANEL, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'p', Blocks.GLASS_PANE, 'P', RotaryItems.PIPE);
//        if (ReikaItemHelper.oreItemExists("ingotInvar"))
//            MachineRegistry.OBSIDIAN.addTagRecipe("SpS", "PMP", "BBB", 'M', RotaryItems.MIXER, 'B', RotaryItems.BASEPANEL, 'S', "ingotInvar", 'p', Blocks.GLASS_PANE, 'P', RotaryItems.PIPE);
//
//        MachineRegistry.HEATER.addCrafting("sBs", "prp", "scs", 'r', RotaryItems.TUNGSTENINGOT, 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'B', Blocks.IRON_BARS, 'p', RotaryItems.BASEPANEL, 'c', RotaryItems.COMBUSTOR);
//
////        MachineRegistry.GPR.addCrafting("SsS", "PCP", "SRS", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 's', RotaryItems.SCREEN, 'P', RotaryItems.BASEPANEL, 'R', RotaryItems.RADAR, 'C', RotaryItems.PCB);
////
////        MachineRegistry.PULSEJET.addCrafting("OCD", "PcO", "BBB", 'B', RotaryItems.BASEPANEL, 'O', Blocks.OBSIDIAN, 'C', RotaryItems.COMPRESSOR, 'D', RotaryItems.DIFFUSER, 'c', RotaryItems.COMBUSTOR, 'P', RotaryItems.PIPE);
////
////        MachineRegistry.EXTRACTOR.addOreRecipe("SWS", "siD", "PIN", 'D', RotaryItems.DRILL, 'P', RotaryItems.BASEPANEL, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'I', RotaryItems.SHAFTITEM, 's', Blocks.STONE, 'i', RotaryItems.IMPELLER, 'N', Blocks.NETHERRACK, 'W', "plankWood");
////
////        MachineRegistry.LIGHTBRIDGE.addCrafting("GgG", "BgS", "BBD", 'B', RotaryItems.BASEPANEL, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'D', Items.DIAMOND, 'G', Items.GOLD_INGOT, 'g', Blocks.GLASS);
////
////        MachineRegistry.PILEDRIVER.addCrafting("PGP", "gFg", "PDP", 'P', RotaryItems.BASEPANEL, 'G', RotaryItems.GEARUNIT8, 'g', RotaryItems.SHAFTITEM, 'F', Flywheels.TUNGSTEN.getCore(), 'D', RotaryItems.DRILL);
//
//        MachineRegistry.PUMP.addCrafting("SGS", "pIp", "PpP", 'P', RotaryItems.BASEPANEL, 'p', RotaryItems.PIPE, 'I', RotaryItems.IMPELLER, 'G', Blocks.GLASS_PANE, 'S', RotaryItems.HSLA_STEEL_INGOT.get());
//
////        MachineRegistry.MOBRADAR.addCrafting(" rs", " g ", "pcp", 'r', RotaryItems.RADAR, 's', RotaryItems.SCREEN, 'c', RotaryItems.PCB, 'g', RotaryItems.GEARUNIT, 'p', RotaryItems.BASEPANEL);
//
//        MachineRegistry.TNTCANNON.addCrafting("sgc", "pcp", "pCr", 'g', Blocks.REDSTONE_BLOCK, 'C', RotaryItems.COMPRESSOR, 'c', RotaryItems.SCREEN, 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'c', RotaryItems.PCB, 'r', Blocks.CHEST, 'p', RotaryItems.BASEPANEL);
//
////        MachineRegistry.SONICWEAPON.addCrafting("psp", "sts", "psp", 't', RotaryItems.TURBINE, 's', RotaryItems.SONAR, 'p', RotaryItems.BASEPANEL);
////
////        MachineRegistry.FORCEFIELD.addCrafting("lnl", "ddd", "sgs", 'd', Items.DIAMOND, 's', RotaryItems.BASEPANEL, 'n', Items.NETHER_STAR, 'g', Items.GOLD_INGOT, 'l', ReikaItemHelper.lapisDye);
//
//        MachineRegistry.MUSICBOX.addSizedCrafting(4, "sns", "ncn", "sns", 'n', Blocks.NOTEBLOCK, 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'c', RotaryItems.PCB);
//        MachineRegistry.MUSICBOX.addSizedOreRecipe(4, "sns", "ncn", "sns", 'n', Blocks.NOTEBLOCK, 's', "ingotSilver", 'c', RotaryItems.PCB);
//
////        MachineRegistry.WEATHERCONTROLLER.addCrafting("s s", "sls", "pcp", 'l', Blocks.DAYLIGHT_DETECTOR, 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'c', RotaryItems.PCB, 'p', RotaryItems.BASEPANEL);
//
//        MachineRegistry.MOBHARVESTER.addCrafting("shs", "sps", 'h', RotaryItems.IGNITER, 'p', Items.ENDER_PEARL, 's', RotaryItems.BASEPANEL);
//
////        MachineRegistry.PROJECTOR.addCrafting("sss", "gcl", "ppp", 'c', RotaryItems.PCB, 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'g', Blocks.GLASS, 'l', Blocks.GLOWSTONE, 'p', RotaryItems.BASEPANEL);
//
//        MachineRegistry.REFRESHER.addCrafting("ses", "epe", "ses", 'p', Items.ENDER_PEARL, 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'e', ReikaItemHelper.lapisDye);
//
//        MachineRegistry.CAVESCANNER.addCrafting("sps", "pcp", "sns", 'n', RotaryItems.SONAR, 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'c', RotaryItems.PCB, 'p', RotaryItems.BASEPANEL);
//
////        MachineRegistry.SCALECHEST.addCrafting("sss", "scs", "sss", 'c', Blocks.CHEST, 's', RotaryItems.HSLA_STEEL_INGOT.get());
//
//        MachineRegistry.SPILLER.addCrafting("sps", "s s", 'p', RotaryItems.PIPE, 's', RotaryItems.HSLA_STEEL_INGOT.get());
//
//        MachineRegistry.FILLER.addCrafting("sss", "sps", "s s", 'p', Blocks.CHEST, 's', RotaryItems.HSLA_STEEL_INGOT.get());
//
//        MachineRegistry.SMOKEDETECTOR.addCrafting(" S ", "RRR", " N ", 'S', ReikaItemHelper.stoneSlab, 'R', Items.REDSTONE, 'N', Blocks.NOTEBLOCK);
//
////        MachineRegistry.IGNITER.addCrafting("OGO", "GCG", "OGO", 'O', Blocks.OBSIDIAN, 'G', Items.GOLD_INGOT, 'C', RotaryItems.COMBUSTOR);
//
//        MachineRegistry.CONTAINMENT.addCrafting("lnl", "ddd", "sgs", 'd', Items.DIAMOND, 's', RotaryItems.BASEPANEL, 'n', Items.NETHER_STAR, 'g', Items.GOLD_INGOT, 'l', ReikaItemHelper.purpleDye);
//
////        MachineRegistry.MAGNETIZER.addCrafting("p p", "gmg", "prp", 'r', Items.REDSTONE, 'p', RotaryItems.BASEPANEL, 'm', RotaryItems.MOUNT, 'g', RotaryItems.GOLDCOIL);
////
////        MachineRegistry.FREEZEGUN.addCrafting(" ss", "iig", "sb ", 'b', RotaryItems.RAILBASE, 'i', Blocks.ICE, 'p', RotaryItems.BASEPANEL, 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'g', RotaryItems.GEARUNIT);
////
////        MachineRegistry.SCREEN.addCrafting("sss", "mcs", "ppp", 'p', RotaryItems.BASEPANEL, 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'm', RotaryItems.SCREEN, 'c', RotaryItems.PCB);
////
////        MachineRegistry.CCTV.addCrafting(" g ", "brs", " p ", 'p', RotaryItems.BASEPANEL, 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'b', Blocks.GLASS_PANE, 'r', Items.REDSTONE, 'g', Items.GOLD_INGOT);
////
////        MachineRegistry.PURIFIER.addCrafting("sbs", "prp", "sps", 'p', RotaryItems.BASEPANEL, 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'r', Items.REDSTONE, 'b', Blocks.IRON_BARS);
//
//        MachineRegistry.MIRROR.addCrafting("bmb", " g ", "pcp", 'b', BlockRegistry.BLASTGLASS.get(), 'p', RotaryItems.BASEPANEL, 'c', RotaryItems.PCB, 'm', RotaryItems.MIRROR, 'g', RotaryItems.STEELGEAR);
//
//        MachineRegistry.SOLARTOWER.addTagRecipe("pPp", "iPi", "pPp", 'p', RotaryItems.BASEPANEL, 'P', RotaryItems.PIPE, 'i', "dyeBlack");
//
////        MachineRegistry.RAILGUN.addCrafting(" H ", " A ", " B ", 'B', RotaryItems.RAILBASE, 'A', RotaryItems.RAILAIM, 'H', RotaryItems.RAILHEAD);
////
//        MachineRegistry.LASERGUN.addCrafting("CLB", "APG", " b ", 'b', RotaryItems.RAILBASE, 'C', RotaryItems.BULB, 'L', RotaryItems.LENS, 'P', RotaryItems.POWER, 'B', RotaryItems.BARREL, 'A', RotaryItems.RAILAIM, 'G', RotaryItems.GEARUNIT);
//
//        MachineRegistry.ITEMCANNON.addCrafting("s c", "pcp", "pCr", 'C', RotaryItems.COMPRESSOR, 'c', RotaryItems.SCREEN, 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'c', RotaryItems.GEARUNIT, 'r', Blocks.CHEST, 'p', RotaryItems.BASEPANEL);
//
//        MachineRegistry.BLOCKCANNON.addCrafting("s c", "pcp", "pCr", 'C', RotaryItems.COMPRESSOR, 'c', RotaryItems.SCREEN, 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'c', RotaryItems.PCB, 'r', Blocks.CHEST, 'p', RotaryItems.BASEPANEL);
//
////        MachineRegistry.FRICTION.addCrafting("S  ", "Sss", "SPP", 'P', RotaryItems.BASEPANEL, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 's', RotaryItems.SHAFTITEM);
//
//        MachineRegistry.LANDMINE.addCrafting(" P ", "RGR", "SIS", 'P', Blocks.STONE_PRESSURE_PLATE, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'I', RotaryItems.IGNITER, 'R', Items.REDSTONE, 'G', Items.GOLD_INGOT);
//
////        MachineRegistry.BUCKETFILLER.addCrafting("SPS", "PCP", "SPS", 'P', RotaryItems.PIPE, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'C', Blocks.CHEST);
//
////        MachineRegistry.SPYCAM.addCrafting("SCS", "PRP", "SGS", 'P', RotaryItems.BASEPANEL, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'C', RotaryItems.PCB, 'G', Blocks.GLASS_PANE, 'R', Items.REDSTONE);
//
//        MachineRegistry.COOLINGFIN.addSizedCrafting(3, "SSS", "SSS", "PPP", 'P', RotaryItems.BASEPANEL, 'S', RotaryItems.SHAFTITEM);
//        MachineRegistry.COOLINGFIN.addSizedOreRecipe(2, "SSS", "SSS", "PPP", 'P', "ingotTin", 'S', "ingotCopper");
//
//        MachineRegistry.SELFDESTRUCT.addCrafting("STS", "TCs", "STS", 'T', Blocks.TNT, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 's', RotaryItems.SHAFTITEM, 'C', RotaryItems.PCB);
//
////        MachineRegistry.DISPLAY.addCrafting("SES", "SCS", " P ", 'P', RotaryItems.BASEPANEL, 'E', RotaryItems.SILICON, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'C', RotaryItems.PCB);
////
////        MachineRegistry.LAMP.addCrafting("SGS", "GgG", "SGS", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'G', Blocks.GLASS, 'g', Blocks.GLOWSTONE);
//
//        MachineRegistry.MULTICLUTCH.addCrafting("PSP", "SGS", "RSR", 'R', Items.REDSTONE, 'I', RotaryItems.HSLA_STEEL_INGOT.get(), 'S', RotaryItems.SHAFTITEM, 'G', RotaryItems.GEARUNIT, 'P', RotaryItems.BASEPANEL);
//
////        MachineRegistry.FUELENHANCER.addCrafting("PGP", "gMg", "PGP", 'G', Blocks.GLASS_PANE, 'M', RotaryItems.MIXER, 'P', RotaryItems.BASEPANEL, 'g', Items.GOLD_INGOT);
//
//        MachineRegistry.LINEBUILDER.addCrafting("sbs", "sps", "PgP", 'g', RotaryItems.GEARUNIT, 'p', Blocks.PISTON, 'P', RotaryItems.BASEPANEL, 'b', RotaryItems.BEDROCK_ALLOY_INGOT.get(), 's', RotaryItems.HSLA_STEEL_INGOT.get());
//
////        MachineRegistry.TERRAFORMER.addCrafting("SsS", "ici", "PiP", 'i', RotaryItems.IMPELLER, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'c', RotaryItems.PCB, 'P', RotaryItems.BASEPANEL, 's', RotaryItems.SCREEN);
////
////        MachineRegistry.EMP.addCrafting("GDG", "GsG", "PnP", 'P', RotaryItems.BASEPANEL, 'n', Items.NETHER_STAR, 'G', RotaryItems.GOLDCOIL, 'D', Blocks.DIAMOND_BLOCK, 's', GearboxTypes.BEDROCK.getPart(GearPart.SHAFTCORE));
////
////        MachineRegistry.ARROWGUN.addCrafting("SSS", "BDB", "SBS", 'B', RotaryItems.BASEPANEL, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'D', Blocks.DISPENSER);
////
////        MachineRegistry.FERTILIZER.addCrafting("PIP", " S ", "BCB", 'P', RotaryItems.PIPE, 'S', RotaryItems.SHAFTITEM, 'I', RotaryItems.IMPELLER, 'C', Blocks.CHEST, 'B', RotaryItems.BASEPANEL);
////
////        MachineRegistry.LAVAMAKER.addCrafting("SRS", "PGP", "SsS", 's', RotaryItems.SHAFTITEM, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'R', MachineRegistry.RESERVOIR.getCraftedProduct(), 'P', RotaryItems.BASEPANEL, 'G', RotaryItems.STEELGEAR);
////
//        MachineRegistry.BEAMMIRROR.addCrafting(" m ", " s ", " p ", 'p', RotaryItems.BASEPANEL, 'm', RotaryItems.MIRROR, 's', RotaryItems.HSLA_STEEL_INGOT.get());
//
//        MachineRegistry.VALVE.addSizedCrafting(4, "sGs", "OGO", "sGs", 'O', Blocks.REDSTONE_BLOCK, 'G', Blocks.GLASS, 's', RotaryItems.HSLA_STEEL_INGOT.get());
//
//        MachineRegistry.BYPASS.addSizedCrafting(4, "OGO", "OGO", "OGO", 'O', Blocks.SANDSTONE, 'G', Blocks.GLASS, 's', RotaryItems.HSLA_STEEL_INGOT.get());
//
//        MachineRegistry.SEPARATION.addSizedCrafting(4, "sGs", "OGO", "sGs", 'O', Blocks.LAPIS_BLOCK, 'G', Blocks.GLASS, 's', RotaryItems.HSLA_STEEL_INGOT.get());
//
////        MachineRegistry.SONICBORER.addCrafting("ss ", "Icp", "bbb", 'p', RotaryItems.PIPE, 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'c', RotaryItems.COMPRESSOR, 'b', RotaryItems.BASEPANEL, 'I', Blocks.IRON_BARS);
////
////        MachineRegistry.AIRGUN.addCrafting("sps", "I S", "sps", 'I', RotaryItems.IMPELLER, 'p', RotaryItems.BASEPANEL, 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'S', RotaryItems.SONAR);
////
////        MachineRegistry.FUELENGINE.addCrafting("CGC", "fgs", "bIb", 'g', GearboxTypes.TUNGSTEN.getPart(GearPart.UNIT8), 'C', RotaryItems.ALUMINUMCYLINDER, 'G', RotaryItems.TUNGSTENINGOT, 'f', RotaryItems.GEARUNIT, 'b', RotaryItems.BASEPANEL, 'I', RotaryItems.IMPELLER, 's', RotaryItems.SHAFTCORE);
////
////        MachineRegistry.AGGREGATOR.addCrafting("SPS", "GCG", "SsS", 's', RotaryItems.HSLA_SHAFT, 'G', Blocks.GLASS_PANE, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'P', RotaryItems.BASEPANEL, 'C', RotaryItems.COMPRESSOR);
////
////        MachineRegistry.FILLINGSTATION.addCrafting("ppS", " iR", "ppB", 'B', RotaryItems.BASEPANEL, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'i', RotaryItems.IMPELLER, 'p', RotaryItems.PIPE, 'R', MachineRegistry.RESERVOIR.getCraftedProduct());
////
////        MachineRegistry.BELT.addSizedCrafting(2, "sBs", " G ", "sBs", 'B', RotaryItems.BASEPANEL, 'G', RotaryItems.HUB, 's', RotaryItems.HSLA_STEEL_INGOT.get());
//
//        MachineRegistry.VANDEGRAFF.addCrafting("shs", "gbg", "php", 'h', RotaryItems.HUB, 'p', RotaryItems.BASEPANEL, 'b', RotaryItems.BELT, 'g', Blocks.GLASS_PANE, 's', RotaryItems.HSLA_STEEL_INGOT.get());
//
////        MachineRegistry.DISTILLER.addCrafting("PGP", "gMg", "PGP", 'G', Blocks.GLASS_PANE, 'M', RotaryItems.MIXER, 'P', RotaryItems.BASEPANEL, 'g', Items.IRON_INGOT);
//
//        MachineRegistry.BIGFURNACE.addCrafting("SFS", "FRF", "SRS", 'S', RotaryItems.BASEPANEL, 'F', Blocks.FURNACE, 'R', MachineRegistry.RESERVOIR.getCraftedProduct());
//
//        MachineRegistry.SUCTION.addSizedCrafting(4, "SGS", "SGS", "SGS", 'S', Blocks.NETHER_BRICKS, 'G', Blocks.GLASS);
//
//        MachineRegistry.SORTING.addCrafting("SHS", " C ", "P P", 'P', RotaryItems.BASEPANEL, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'H', Blocks.HOPPER, 'C', RotaryItems.PCB);
//
////        MachineRegistry.CRYSTALLIZER.addCrafting("SFS", "FIF", "BBB", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'B', RotaryItems.BASEPANEL, 'F', MachineRegistry.COOLINGFIN.getBlockState().getBlock().asItem(), 'I', RotaryItems.IMPELLER);
////
////        MachineRegistry.POWERBUS.addSizedCrafting(4, "SMS", "MCM", "SMS", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'M', RotaryItems.BEARING, 'C', RotaryItems.BELT);
////
////        MachineRegistry.BUSCONTROLLER.addCrafting("SMS", "MCM", "SMS", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'M', RotaryItems.BEARING, 'C', RotaryItems.PCB);
//
//        MachineRegistry.PARTICLE.addSizedCrafting(4, "SDS", "PCP", "SIS", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'P', RotaryItems.BASEPANEL, 'C', RotaryItems.PCB, 'D', Blocks.DISPENSER, 'I', RotaryItems.IMPELLER);
//        MachineRegistry.PARTICLE.addSizedOreRecipe(4, "SDS", "PCP", "SIS", 'S', "ingotTin", 'P', RotaryItems.BASEPANEL, 'C', RotaryItems.PCB, 'D', Blocks.DISPENSER, 'I', RotaryItems.IMPELLER);
//
////        MachineRegistry.LAWNSPRINKLER.addCrafting("PPP", " P ", "BIB", 'I', RotaryItems.IMPELLER, 'P', RotaryItems.PIPE, 'B', RotaryItems.BASEPANEL);
//
//        MachineRegistry.GRINDSTONE.addCrafting("S S", "sBs", "ppp", 'p', RotaryItems.BASEPANEL, 's', RotaryItems.SHAFTITEM, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'B', Blocks.STONE);
//
//        MachineRegistry.BLOWER.addSizedCrafting(DifficultyEffects.PIPECRAFT.getInt(), "BBB", "PIP", "BBB", 'B', RotaryItems.BASEPANEL, 'I', RotaryItems.IMPELLER, 'P', RotaryItems.PIPE);
//
////        MachineRegistry.DEFOLIATOR.addCrafting("P P", "SPS", "BIB", 'B', RotaryItems.BASEPANEL, 'P', RotaryItems.PIPE, 'I', RotaryItems.IMPELLER, 'S', RotaryItems.HSLA_STEEL_INGOT.get());
//
//        MachineRegistry.REFRIGERATOR.addCrafting("SPS", "CcD", "pPp", 'p', RotaryItems.BASEPANEL, 'P', RotaryItems.PIPE, 'D', RotaryItems.DIFFUSER, 'C', RotaryItems.COMPRESSOR, 'c', RotaryItems.CONDENSER, 'S', RotaryItems.HSLA_STEEL_INGOT.get());
//
//        MachineRegistry.COMPOSTER.addCrafting(" S ", "S S", "BBB", 'B', RotaryItems.BASEPANEL, 'S', RotaryItems.HSLA_STEEL_INGOT.get());
//        MachineRegistry.COMPOSTER.addTagRecipe(" S ", "S S", "BBB", 'B', RotaryItems.BASEPANEL, 'S', "ingotTin");
//
////        MachineRegistry.GASTANK.addCrafting("SIS", "PRP", "PPP", 'P', RotaryItems.BASEPANEL, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'I', RotaryItems.IMPELLER, 'R', MachineRegistry.RESERVOIR.getCraftedProduct());
////
////        MachineRegistry.CRAFTER.addCrafting("SCS", "PcP", "SPS", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'C', Blocks.CRAFTING_TABLE, 'P', RotaryItems.BASEPANEL, 'c', RotaryItems.PCB);
////
////        MachineRegistry.ANTIAIR.addCrafting("sss", "ppc", " Ba", 'p', RotaryItems.PIPE, 'c', RotaryItems.COMPRESSOR, 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'a', RotaryItems.RAILAIM, 'B', RotaryItems.RAILBASE);
////
////        MachineRegistry.PIPEPUMP.addCrafting("BBB", "PIP", "BBB", 'B', RotaryItems.HSLA_STEEL_INGOT.get(), 'I', RotaryItems.IMPELLER, 'P', RotaryItems.PIPE);
////
////        MachineRegistry.CHAIN.addSizedCrafting(2, "sBs", " G ", "sBs", 'B', RotaryItems.BASEPANEL, 'G', RotaryItems.STEELGEAR, 's', RotaryItems.HSLA_STEEL_INGOT.get());
////
////        MachineRegistry.CENTRIFUGE.addCrafting("SGS", "S S", "PgP", 'P', RotaryItems.BASEPANEL, 'g', RotaryItems.GEARUNIT4, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'G', Blocks.GLASS_PANE);
////
////        MachineRegistry.DRYING.addCrafting("S S", "SPS", "S S", 'P', RotaryItems.BASEPANEL, 'S', RotaryItems.HSLA_STEEL_INGOT.get());
////
////        MachineRegistry.WETTER.addCrafting("S S", "gmg", "SPS", 'g', Blocks.GLASS_PANE, 'm', RotaryItems.MIXER, 'P', RotaryItems.BASEPANEL, 'S', RotaryItems.HSLA_STEEL_INGOT.get());
////
////        MachineRegistry.CHUNKLOADER.addCrafting("sSs", "BSB", "PGP", 'B', RotaryItems.HSLA_STEEL_INGOT.get(), 'S', RotaryItems.BEDROCKSHAFT, 's', Items.NETHER_STAR, 'P', RotaryItems.BASEPANEL, 'G', GearboxTypes.BEDROCK.getPart(GearPart.UNIT16));
////
////        MachineRegistry.DROPS.addCrafting("PSP", "PDP", "PSP", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'D', RotaryItems.DRILL, 'P', RotaryItems.BASEPANEL);
////
////        MachineRegistry.ITEMFILTER.addCrafting("sSs", "CCC", "PRP", 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'S', RotaryItems.SCREEN, 'C', RotaryItems.PCB, 'R', Items.REDSTONE, 'P', RotaryItems.BASEPANEL);
////
////        MachineRegistry.HYDRATOR.addOreRecipe("sls", "p p", "PpP", 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'p', "plankWood", 'l', Blocks.LADDER, 'P', RotaryItems.BASEPANEL);
////
////        MachineRegistry.GATLING.addCrafting("PPG", " GA", "  B", 'B', RotaryItems.RAILBASE, 'A', RotaryItems.RAILAIM, 'G', RotaryItems.STEELGEAR, 'P', RotaryItems.CYLINDER);
////
////        MachineRegistry.FLAMETURRET.addCrafting("FIP", "GFS", " FB", 'B', RotaryItems.RAILBASE, 'F', RotaryItems.FUELLINE, 'I', RotaryItems.IGNITER, 'P', RotaryItems.PIPE, 'g', RotaryItems.IMPELLER, 'S', RotaryItems.SHAFTITEM);
////
//        MachineRegistry.SPILLWAY.addCrafting("S  ", "PSP", "PpP", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'P', RotaryItems.BASEPANEL, 'p', RotaryItems.PIPE);
//
//        MachineRegistry.DISTRIBCLUTCH.addCrafting("sgs", "SGS", "PrP", 'r', RotaryItems.PCB, 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'P', RotaryItems.BASEPANEL, 'S', RotaryItems.SHAFTITEM, 'g', RotaryItems.STEELGEAR, 'G', RotaryItems.GEARUNIT);
//    }
//
//
//    private static void addToolItems() {
//        RotaryItems.SPRING.addRecipe(" S ", "S S", " S ", 'S', RotaryItems.HSLA_STEEL_INGOT.get());
//        RotaryItems.STRONGCOIL.addBlastRecipe(1000, 4, "SDS", "BCB", "SDS", 'S', RotaryItems.SPRINGINGOT, 'C', RotaryItems.SPRING.getMetadata(OreDictionary.WILDCARD_VALUE), 'B', RotaryItems.BEDROCKDUST, 'D', Items.DIAMOND);
//
//        RotaryItems.TARGET.addRecipe(" E ", "SRS", "SLS", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'R', Items.REDSTONE, 'E', Items.ENDER_PEARL, 'L', ReikaItemHelper.lapisDye);
//
//        if (ModList.HARVESTCRAFT.isLoaded()) {
//            RotaryCraft.LOGGER.info("HarvestCraft found, not loading iron screwdriver recipe.");
//        } else {
//            RotaryItems.SCREWDRIVER.addOreRecipe("I  ", " S ", "  W", 'S', "stickWood", 'I', Items.IRON_INGOT, 'W', "plankWood");
//            RotaryCraft.LOGGER.info("HarvestCraft not found, loading iron screwdriver recipe.");
//        }
//
//        RotaryItems.SCREWDRIVER.addOreRecipe("I  ", " S ", "  W", 'S', "stickWood", 'I', RotaryItems.HSLA_STEEL_INGOT.get(), 'W', "plankWood");
//        RotaryItems.METER.addOreRecipe(" W ", "WEW", "SSS", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'E', Items.REDSTONE, 'I', Items.IRON_INGOT, 'W', "plankWood");
//        RotaryItems.HANDBOOK.addRecipe("RSR", "PPP", "PPP", 'R', Items.REDSTONE, 'S', Items.IRON_INGOT, 'P', Items.PAPER);
//
//        RotaryItems.BEDPICK.addEnchantedBlastRecipe(1000, 4, "BBB", " S ", " S ", 'S', RotaryItems.SHAFTITEM, 'B', RotaryItems.BEDROCK_ALLOY_INGOT.get());
//        RotaryItems.BEDAXE.addBlastRecipe(1000, 4, "BB ", "BS ", " S ", 'S', RotaryItems.SHAFTITEM, 'B', RotaryItems.BEDROCK_ALLOY_INGOT.get());
//        RotaryItems.BEDSHOVEL.addBlastRecipe(1000, 4, "B", "S", "S", 'S', RotaryItems.SHAFTITEM, 'B', RotaryItems.BEDROCK_ALLOY_INGOT.get());
//        RotaryItems.BEDSWORD.addEnchantedBlastRecipe(1000, 4, "B", "B", "S", 'B', RotaryItems.BEDROCK_ALLOY_INGOT.get(), 'S', RotaryItems.SHAFTITEM);
//        RotaryItems.BEDHOE.addBlastRecipe(1000, 4, "II", " S", " S", 'S', RotaryItems.SHAFTITEM, 'I', RotaryItems.BEDROCK_ALLOY_INGOT.get());
//        RotaryItems.BEDHOE.addBlastRecipe(1000, 4, "II", "S ", "S ", 'S', RotaryItems.SHAFTITEM, 'I', RotaryItems.BEDROCK_ALLOY_INGOT.get());
//        RotaryItems.BEDSHEARS.addBlastRecipe(1000, 4, " B", "B ", 'B', RotaryItems.BEDROCK_ALLOY_INGOT.get());
//        RotaryItems.BEDSICKLE.addEnchantedBlastRecipe(1000, 4, " B ", "  B", "SB ", 'S', RotaryItems.SHAFTITEM, 'B', RotaryItems.BEDROCK_ALLOY_INGOT.get());
//        RotaryItems.BEDGRAFTER.addBlastRecipe(1000, 4, "  S", " s ", "s  ", 'S', RotaryItems.BEDROCK_ALLOY_INGOT.get(), 's', RotaryItems.SHAFTITEM);
//        RotaryItems.BEDSAW.addBlastRecipe(1000, 4, "sss", " SS", " bb", 'b', RotaryItems.BEDROCK_ALLOY_INGOT.get(), 's', RotaryItems.SHAFTITEM, 'S', Items.IRON_INGOT);
//        //RotaryItems.BEDKNIFE.addBlastRecipe(1000, 4, "  s", "qs ", "bb ", 'b', RotaryItems.BEDROCK_ALLOY_INGOT.get(), 's', RotaryItems.SHAFTITEM, 'q', AppEngHandler.getInstance().getCertusQuartz());
//        RotaryItems.BEDHELM.addEnchantedBlastRecipe(1200, 4, "III", "IaI", 'I', RotaryItems.BEDROCK_ALLOY_INGOT.get(), 'a', RotaryCraft.config.getBedrockArmorGatingMaterial(true, null));
//        RotaryItems.BEDBOOTS.addEnchantedBlastRecipe(1200, 4, "I I", "IaI", 'I', RotaryItems.BEDROCK_ALLOY_INGOT.get(), 'a', RotaryCraft.config.getBedrockArmorGatingMaterial(true, null));
//        RotaryItems.BEDCHEST.addEnchantedBlastRecipe(1200, 4, "IaI", "III", "III", 'I', RotaryItems.BEDROCK_ALLOY_INGOT.get(), 'a', RotaryCraft.config.getBedrockArmorGatingMaterial(true, null));
//        RotaryItems.BEDLEGS.addEnchantedBlastRecipe(1200, 4, "III", "IaI", "I I", 'I', RotaryItems.BEDROCK_ALLOY_INGOT.get(), 'a', RotaryCraft.config.getBedrockArmorGatingMaterial(true, null));
//
//        RotaryItems.STEELPICK.addRecipe(new ShapedOreRecipe(RotaryItems.HSLA_STEEL_PICKAXE.get(), "BBB", " S ", " S ", 'S', "stickWood", 'B', RotaryItems.HSLA_STEEL_INGOT.get()));
//        RotaryItems.STEELAXE.addRecipe(new ShapedOreRecipe(RotaryItems.STEELAXE.get(), "BB ", "BS ", " S ", 'S', "stickWood", 'B', RotaryItems.HSLA_STEEL_INGOT.get()));
//        RotaryItems.STEELSHOVEL.addRecipe(new ShapedOreRecipe(RotaryItems.STEELSHOVEL.get(), " B ", " S ", " S ", 'S', "stickWood", 'B', RotaryItems.HSLA_STEEL_INGOT.get()));
//        RotaryItems.STEELSWORD.addEnchantedRecipe("B", "B", "S", 'B', RotaryItems.HSLA_STEEL_INGOT.get(), 'S', Items.STICK);
//        RotaryItems.STEELHOE.addRecipe("II ", " S ", " S ", 'S', Items.STICK, 'I', RotaryItems.HSLA_STEEL_INGOT.get());
//        RotaryItems.STEELHOE.addRecipe(" II", " S ", " S ", 'S', Items.STICK, 'I', RotaryItems.HSLA_STEEL_INGOT.get());
//        RotaryItems.STEELSHEARS.addRecipe(" B", "B ", 'B', RotaryItems.HSLA_STEEL_INGOT.get());
//        RotaryItems.STEELSICKLE.addRecipe(" B ", "  B", "SB ", 'S', Items.STICK, 'B', RotaryItems.HSLA_STEEL_INGOT.get());
//
//        RotaryItems.GRAFTER.addRecipe("  S", "Ss ", "CS ", 'C', RotaryItems.HSLA_STEEL_SPRING.get(), 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 's', Items.STICK);
//
//        RotaryItems.STEELHELMET.addRecipe("III", "I I", 'I', RotaryItems.HSLA_STEEL_INGOT.get());
//        RotaryItems.STEELBOOTS.addRecipe("I I", "I I", 'I', RotaryItems.HSLA_STEEL_INGOT.get());
//        RotaryItems.STEELCHEST.addRecipe("I I", "III", "III", 'I', RotaryItems.HSLA_STEEL_INGOT.get());
//        RotaryItems.STEELLEGS.addRecipe("III", "I I", "I I", 'I', RotaryItems.HSLA_STEEL_INGOT.get());
//
//        //RotaryItems.NVH.addShapelessRecipe(Items.DIAMOND_HELMET, RotaryItems.NVG.get());
//
//        RotaryItems.ULTRASOUND.addRecipe(" n ", "scs", " s ", 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'c', RotaryItems.SCREEN, 'n', RotaryItems.SONAR);
//        RotaryItems.MOTION.addRecipe(" nr", "scs", " s ", 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'c', RotaryItems.SCREEN, 'n', RotaryItems.SONAR, 'r', RotaryItems.RADAR);
//        RotaryItems.VACUUM.addRecipe(" n ", "scs", " s ", 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'c', RotaryItems.IMPELLER, 'n', RotaryItems.DIFFUSER);
//        RotaryItems.STUNGUN.addRecipe(" n ", "scs", " s ", 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'c', RotaryItems.SONAR, 'n', RotaryItems.DIFFUSER);
//        RotaryItems.GRAVELGUN.addRecipe(" d ", "gcg", "sas", 'a', RotaryCraft.config.getGravelGunGatingMaterial(true, RotaryItems.HSLA_STEEL_INGOT.get().getDefaultInstance()), 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'c', Blocks.CHEST, 'd', Blocks.DISPENSER, 'g', RotaryItems.STEELGEAR);
//        RotaryItems.RANGEFINDER.addRecipe(" e ", "rGr", "sss", 'G', Blocks.GLOWSTONE, 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'r', Items.REDSTONE, 'e', Items.ENDER_PEARL);
//        RotaryItems.FIREBALL.addRecipe("b b", "scs", "srs", 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'c', RotaryItems.COMBUSTOR, 'r', Items.REDSTONE, 'b', Items.BLAZE_ROD);
//        RotaryItems.HANDHELD_CRAFTING_TABLE.addRecipe(" g ", "scs", " g ", 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'g', Items.GOLD_INGOT, 'c', Blocks.CRAFTING_TABLE);
//        RotaryItems.NVG.addRecipe("scs", "ese", 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'c', RotaryItems.SCREEN, 'e', Items.ENDER_EYE);
//
//        RotaryItems.IOGOGGLES.addRecipe("scs", "ese", 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'c', Items.ENDER_PEARL, 'e', Items.REDSTONE);
//
//        RotaryItems.KEY.addRecipe("s", "s", "P", 'P', RotaryItems.BASEPANEL, 's', RotaryItems.HSLA_STEEL_INGOT.get());
//
//        RotaryItems.TILESELECTOR.addRecipe(" l ", "srs", "ses", 'e', Items.ENDER_PEARL, 'r', Items.REDSTONE, 'l', ReikaItemHelper.lapisDye, 's', RotaryItems.HSLA_STEEL_INGOT.get());
//
//        RotaryItems.JETPACK.addRecipe("CRC", "cBc", "d d", 'R', MachineRegistry.RESERVOIR.getCraftedProduct(), 'B', RotaryItems.BASEPANEL, 'd', RotaryItems.DIFFUSER, 'c', RotaryItems.COMPRESSOR, 'C', RotaryItems.COMBUSTOR);
//
//        RotaryItems.PUMP.addRecipe(" sP", "sIs", "sRs", 'R', MachineRegistry.RESERVOIR.getCraftedProduct(), 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'P', RotaryItems.PIPE, 'I', RotaryItems.IMPELLER);
//
//        RotaryItems.HELDPISTON.addRecipe(" sP", "sRs", "gs ", 'g', Items.GLOWSTONE_DUST, 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'P', Blocks.PISTON, 'R', Blocks.REDSTONE_BLOCK);
//
//        RotaryItems.JUMP.addRecipe("GbG", "SgS", "B B", 'B', RotaryItems.BASEPANEL, 'G', RotaryItems.STEELGEAR, 'b', RotaryItems.STEELBOOTS.get(), 'g', RotaryItems.GEARUNIT, 'S', RotaryItems.SPRING.get());
//
//        RotaryItems.FUEL.addRecipe("SBS", "BGB", "SPS", 'P', RotaryItems.PIPE, 'B', RotaryItems.BASEPANEL, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'G', Blocks.GLASS);
//
//        RotaryItems.DISK.addSizedRecipe(4, "wRw", "RSR", "wRw", 'w', ReikaItemHelper.blackWool, 'R', Items.REDSTONE, 'S', RotaryItems.HSLA_STEEL_INGOT.get());
//
//        RotaryItems.CRAFTPATTERN.addSizedRecipe(4, " S ", " B ", " S ", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'B', RotaryItems.BASEPANEL);
//
//        RotaryItems.UPGRADE.addMetaRecipe(0, "sRs", "gGg", " b ", 's', RotaryItems.SILUMIN, 'b', RotaryItems.BASEPANEL, 'R', RotaryItems.RADIATOR, 'G', RotaryItems.GEARUNIT, 'g', Items.GOLD_INGOT);
//
//        RotaryItems.UPGRADE.addMetaRecipe(1, "gRg", "RER", "SGS", 'g', Items.GOLD_INGOT, 'G', RotaryItems.IMPELLER, 'R', Items.REDSTONE, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'E', RotaryItems.ETHANOL.get());
//        RotaryItems.UPGRADE.addMetaRecipe(2, "SCS", "ERE", "SCS", 'C', RotaryItems.REDGOLDINGOT, 'R', RotaryItems.GOLDCOIL, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'E', GearboxTypes.TUNGSTEN.getPart(GearPart.SHAFTCORE));
//        RotaryItems.UPGRADE.addMetaRecipe(3, "SES", "ERE", "ScS", 'c', RotaryItems.PCB, 'R', RotaryItems.TUNGSTENINGOT, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'E', RotaryItems.REDGOLDINGOT);
//        RotaryItems.UPGRADE.addMetaBlastRecipe(1000, 4, 4, "cEc", "ERE", "SES", 'c', MachineRegistry.COOLINGFIN.getCraftedProduct(), 'R', RotaryItems.BEDROCK_ALLOY_INGOT.get(), 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'E', RotaryItems.TUNGSTENINGOT);
//        RotaryItems.UPGRADE.addMetaBlastRecipe(1800, 8, 5, "SES", "ERE", "SES", 'R', RotaryItems.BEDROCKGEAR, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'E', RotaryItems.SPRINGTUNGSTEN);
//
//        RotaryItems.UPGRADE.addMetaRecipe(6, "SEI", "ERE", "SEI", 'R', RotaryItems.COMPOUNDTURB, 'S', RotaryItems.HIGHCOMBUSTOR, 'I', RotaryItems.IGNITER, 'E', RotaryItems.BEDROCKDUST);
//
//        if (!ModList.REACTORCRAFT.isLoaded())
//            RotaryItems.UPGRADE.addMetaBlastRecipe(1980, 32, Upgrades.EFFICIENCY.ordinal(), "IGI", "FTF", "BPB", 'G', RotaryItems.GENERATOR, 'I', RotaryItems.REDGOLDINGOT, 'B', RotaryItems.WATERPLATE, 'P', RotaryItems.POWER, 'F', RotaryItems.BEDROCK_ALLOY_INGOT.get(), 'T', RotaryItems.TUNGSTENINGOT);
//
//        RotaryItems.GEARUPGRADE.addMetaBlastRecipe(1200, 2, 0, "sSS", "SsS", "SSG", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'G', Blocks.GLASS_PANE, 's', RotaryItems.BEDROCKSHAFT);
//
//        for (int i = 1; i <= 4; i++) {
//            ItemStack gear = GearboxTypes.BEDROCK.getPart(GearPart.list[GearPart.UNIT2.ordinal() + i - 1]);
//            RotaryItems.GEARUPGRADE.addRecipe(ReikaRecipeHelper.getShapelessRecipeFor(RotaryItems.GEARUPGRADE.getMetadata(i), RotaryItems.GEARUPGRADE.getMetadata(0), gear));
//        }
//    }
//
//    private static void addMisc() {
//        RecipesBlastFurnace.getRecipes().add3x3Crafting(RotaryItems.BEDROCKDRILL, 1000, 8, 0, "BBB", "BBB", " B ", 'B', RotaryItems.BEDROCK_ALLOY_INGOT.get());
//
//        WorktableRecipes.getInstance().addRecyclingRecipe(new RecyclingRecipe(RotaryItems.STEELGEAR, 45 / DifficultyEffects.PARTCRAFT.getInt()));
//        WorktableRecipes.getInstance().addRecyclingRecipe(new RecyclingRecipe(RotaryItems.GEARUNIT, 48));
//        WorktableRecipes.getInstance().addRecyclingRecipe(new RecyclingRecipe(RotaryItems.GEARUNIT4, 114));
//        WorktableRecipes.getInstance().addRecyclingRecipe(new RecyclingRecipe(RotaryItems.GEARUNIT8, 180));
//        WorktableRecipes.getInstance().addRecyclingRecipe(new RecyclingRecipe(RotaryItems.GEARUNIT16, 244));
//        WorktableRecipes.getInstance().addRecyclingRecipe(new RecyclingRecipe(RotaryItems.SHAFTITEM, 27 / DifficultyEffects.PARTCRAFT.getInt()));
//        WorktableRecipes.getInstance().addRecyclingRecipe(new RecyclingRecipe(RotaryItems.BASEPANEL, 27 / DifficultyEffects.PARTCRAFT.getInt()));
//        WorktableRecipes.getInstance().addRecyclingRecipe(new RecyclingRecipe(RotaryItems.WORMGEAR, 45 / DifficultyEffects.PARTCRAFT.getInt() + 2 * 27 / DifficultyEffects.PARTCRAFT.getInt()));
//        WorktableRecipes.getInstance().addRecyclingRecipe(new RecyclingRecipe(9, RotaryItems.BALLBEARING, 4));
//        WorktableRecipes.getInstance().addRecyclingRecipe(new RecyclingRecipe(RotaryItems.MOUNT.get(), 9));
//        WorktableRecipes.getInstance().addRecyclingRecipe(new RecyclingRecipe(RotaryItems.SHAFTCORE, 15));
//        WorktableRecipes.getInstance().addRecyclingRecipe(new RecyclingRecipe(RotaryItems.WATERPLATE, 81));
//
//        WorktableRecipes.getInstance().addRecipe(RotaryItems.STEELBLOCK, RecipeHandler.RecipeLevel.PROTECTED, "BBB", "BBB", "BBB", 'B', RotaryItems.HSLA_STEEL_INGOT.get());
//    }
//
//
//    private static void addMultiTypes() {
//
//        MachineRegistry.ADVANCEDGEARS.addMetaCrafting(0, "SW ", " GS", " M ", 'M', RotaryItems.MOUNT, 'S', RotaryItems.SHAFTITEM, 'W', RotaryItems.WORMGEAR, 'G', RotaryItems.STEELGEAR); //Worm gear
//        MachineRegistry.ADVANCEDGEARS.addMetaCrafting(1, "BSB", "BSB", "sMc", 'c', RotaryItems.SCREEN, 's', RotaryItems.PCB, 'M', RotaryItems.MOUNT, 'S', RotaryItems.BEDROCKSHAFT, 'B', GearboxTypes.DIAMOND.getPart(GearPart.BEARING)); //CVT
//        MachineRegistry.ADVANCEDGEARS.addMetaCrafting(2, "BCS", " M ", 'M', RotaryItems.MOUNT, 'S', RotaryItems.SHAFTCORE, 'B', RotaryItems.BRAKE, 'C', RotaryItems.TENSCOIL); //Coil
//        NBTTagCompound NBT = new NBTTagCompound();
//        NBT.putBoolean("bedrock", true);
//        MachineRegistry.ADVANCEDGEARS.addNBTMetaCrafting(NBT, 2, "BCS", " M ", 'M', RotaryItems.MOUNT, 'S', GearboxTypes.BEDROCK.getPart(GearPart.SHAFTCORE), 'B', RotaryItems.BRAKE, 'C', RotaryItems.BEDROCKCOIL); //Coil
//        MachineRegistry.ADVANCEDGEARS.addMetaCrafting(3, "SGS", "SGS", "BMB", 'S', RotaryItems.BEDROCKSHAFT, 'B', GearboxTypes.TUNGSTEN.getPart(GearPart.BEARING), 'M', RotaryItems.MOUNT, 'G', GearboxTypes.BEDROCK.getPart(GearPart.UNIT16)); //256x
//
//        for (Flywheels f : Flywheels.list)
//            MachineRegistry.FLYWHEEL.addRecipe(f.getFlywheelItem(), "W", "M", 'W', f.getCore(), 'M', RotaryItems.MOUNT);
//
//        for (MaterialRegistry mat : MaterialRegistry.matList) {
//            Object m = mat.getMountItem();
//            ItemStack item = ReikaItemHelper.getSizedItemStack(mat.getShaftItem(), 8);
//            if (m == RotaryItems.MOUNT) {
//                MachineRegistry.SHAFT.addRecipe(new ShapedOreRecipe(item, "S", "M", 'M', RotaryItems.MOUNT, 'S', mat.getShaftUnitItem()));
//            } else {
//                MachineRegistry.SHAFT.addRecipe(new ShapedOreRecipe(item, "BSB", "BBB", 'B', m, 'S', mat.getShaftUnitItem()));
//            }
//        }
//
//        MachineRegistry.DC_ENGINE.addMetaCrafting(EngineType.DC.ordinal(), "SSS", "SRs", "PRP", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'R', Items.REDSTONE, 'P', RotaryItems.BASEPANEL, 's', RotaryItems.SHAFTITEM);
//        MachineRegistry.WIND_ENGINE.addSizedMetaCrafting(2, EngineType.WIND.ordinal(), "SSS", "SHS", "SSS", 'S', RotaryItems.PROP, 'H', RotaryItems.HUB);
//        MachineRegistry.STEAM_ENGINE.addMetaCrafting(EngineType.STEAM.ordinal(), "ccc", "CIs", "PGP", 'c', Blocks.COBBLESTONE, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'I', RotaryItems.IMPELLER, 'P', RotaryItems.BASEPANEL, 's', RotaryItems.SHAFTITEM, 'G', Items.GOLD_INGOT, 'C', RotaryItems.CONDENSER);
//
//        if (ReikaItemHelper.oreItemExists("ingotCopper"))
//            MachineRegistry.STEAM_ENGINE.addMetaOreRecipe(EngineType.STEAM.ordinal(), "ccc", "CIs", "PGP", 'c', Blocks.COBBLESTONE, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'I', RotaryItems.IMPELLER, 'P', RotaryItems.BASEPANEL, 's', RotaryItems.SHAFTITEM, 'G', "ingotCopper", 'C', RotaryItems.CONDENSER);
//        MachineRegistry.GAS_ENGINE.addMetaCrafting(EngineType.GAS.ordinal(), "CgC", "SGs", "PIP", 'g', Items.GOLD_INGOT, 'S', RotaryItems.IGNITER, 'I', RotaryItems.IMPELLER, 'P', RotaryItems.BASEPANEL, 's', RotaryItems.SHAFTITEM, 'G', RotaryItems.GEARUNIT, 'C', RotaryItems.CYLINDER);
//        MachineRegistry.AC_ENGINE.addMetaCrafting(EngineType.AC.ordinal(), "SSS", "SGs", "PRP", 'S', Items.GOLD_INGOT, 'R', Items.REDSTONE, 'P', RotaryItems.BASEPANEL, 's', RotaryItems.SHAFTITEM, 'G', RotaryItems.GOLDCOIL);
//
//        if (ReikaItemHelper.oreItemExists("ingotElectrum"))
//            MachineRegistry.AC_ENGINE.addMetaOreRecipe(EngineType.AC.ordinal(), "SSS", "SGs", "PRP", 'S', "ingotElectrum", 'R', Items.REDSTONE, 'P', RotaryItems.BASEPANEL, 's', RotaryItems.SHAFTITEM, 'G', RotaryItems.GOLDCOIL);
//        MachineRegistry.PERFORMANCE_ENGINE.addMetaCrafting(EngineType.SPORT.ordinal(), "CrC", "SGs", "PIP", 'C', RotaryItems.ALUMINUMCYLINDER, 'S', RotaryItems.IGNITER, 'I', RotaryItems.IMPELLER, 'P', RotaryItems.BASEPANEL, 's', RotaryItems.SHAFTITEM, 'r', RotaryItems.RADIATOR, 'G', RotaryItems.GEARUNIT);
////        MachineRegistry.HYDRO.addMetaCrafting(EngineType.HYDRO.ordinal(), "PPP", "PGP", "PPP", 'P', RotaryItems.WATERPLATE, 'G', RotaryItems.DIAMONDSHAFTCORE);
//        MachineRegistry.MICRO_TURBINE.addMetaCrafting(EngineType.MICRO.ordinal(), "CSS", "cTs", "PPP", 'S', RotaryItems.SILUMIN, 'C', RotaryItems.COMPRESSOR, 'c', RotaryItems.HIGHCOMBUSTOR, 'T', RotaryItems.TURBINE, 'P', RotaryItems.BASEPANEL, 's', RotaryItems.SHAFTITEM);
////        MachineRegistry.JET.addMetaCrafting(EngineType.JET.ordinal(), "DCS", "ScS", "PTs", 'S', RotaryItems.SILUMIN, 'D', RotaryItems.DIFFUSER, 'C', RotaryItems.COMPOUNDCOMPRESS, 'c', RotaryItems.HIGHCOMBUSTOR, 'T', RotaryItems.COMPOUNDTURB, 'P', RotaryItems.BASEPANEL, 's', RotaryItems.SHAFTITEM);
//
//        if (ConfigRegistry.ROTATEHOSE.getState()) {
//            MachineRegistry.HOSE.addSizedOreRecipe(DifficultyEffects.PIPECRAFT.getInt(), "WWW", "GGG", "WWW", 'G', Blocks.GLASS, 'W', "plankWood");
//            MachineRegistry.PIPE.addSizedCrafting(DifficultyEffects.PIPECRAFT.getInt(), "SSS", "GGG", "SSS", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'G', Blocks.GLASS);
//            MachineRegistry.FUELLINE.addSizedCrafting(DifficultyEffects.PIPECRAFT.getInt(), "OOO", "GGG", "OOO", 'O', Blocks.OBSIDIAN, 'G', Blocks.GLASS);
//            MachineRegistry.BEDPIPE.addSizedCrafting(DifficultyEffects.PIPECRAFT.getInt(), "BBB", "GGG", "BBB", 'B', RotaryItems.BEDROCK_ALLOY_INGOT.get(), 'G', RotaryBlocks.BLASTGLASS.get());
//        } else {
//            MachineRegistry.HOSE.addSizedOreRecipe(DifficultyEffects.PIPECRAFT.getInt(), "WGW", "WGW", "WGW", 'G', Blocks.GLASS, 'W', "plankWood");
//            MachineRegistry.PIPE.addSizedCrafting(DifficultyEffects.PIPECRAFT.getInt(), "SGS", "SGS", "SGS", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'G', Blocks.GLASS);
//            MachineRegistry.FUELLINE.addSizedCrafting(DifficultyEffects.PIPECRAFT.getInt(), "OGO", "OGO", "OGO", 'O', Blocks.OBSIDIAN, 'G', Blocks.GLASS);
//            MachineRegistry.BEDPIPE.addSizedCrafting(DifficultyEffects.PIPECRAFT.getInt(), "BGB", "BGB", "BGB", 'B', RotaryItems.BEDROCK_ALLOY_INGOT.get(), 'G', BlockRegistry.BLASTGLASS.get());
//        }
//
//        addGearboxes();
//    }
//
//    private static void addGearboxes() {
//        for (GearboxTypes gear : GearboxTypes.typeList) {
//            if (!gear.isLoadable())
//                continue;
//            Object m = gear.getMountItem();
//            for (int r = 2; r <= 16; r *= 2) {
//                ItemStack item = addDamageNBT(gear.getGearboxItem(r));
//                ItemStack unit = gear.getPart(GearPart.getGearUnitPartItemFromRatio(r));
//                if (m == RotaryItems.MOUNT) {
//                    MachineRegistry.GEARBOX.addRecipe(item, "G", "M", 'M', RotaryItems.MOUNT, 'G', unit);
//                } else {
//                    MachineRegistry.GEARBOX.addRecipe(new ShapedOreRecipe(item, new Object[]{"MGM", "MMM", 'M', m, 'G', unit}));
//                }
//            }
//        }
//    }
//
//    private static ItemStack addDamageNBT(ItemStack is) {
//        if (is.getTag() == null)
//            is.setTagCompound(new NBTTagCompound());
//        is.stackTagCompound.setInteger("damage", 0);
//        return is;
//    }
//
//    private static ItemStack addDiamondTypeTag(ItemStack is, String tag, boolean value) {
//        if (is.stackTagCompound == null)
//            is.setTagCompound(new NBTTagCompound());
//        is.stackTagCompound.putBoolean(tag, value);
//        return is;
//    }
//
//    private static void addOreRecipeToBoth(ItemStack out, Object... in) {
//        ShapedOreRecipe sr = new ShapedOreRecipe(out, in);
//        GameRegistry.addRecipe(sr);
//        WorktableRecipes.getInstance().addRecipe(sr, RecipeHandler.RecipeLevel.CORE);
//    }
//
//    private static void addRecipeToBoth(ItemStack out, Object... in) {
//        GameRegistry.addRecipe(out, in);
//        WorktableRecipes.getInstance().addRecipe(out, RecipeHandler.RecipeLevel.CORE, in);
//    }
//
//    public static Object[] getBlastFurnaceIngredients() {
//        Object[] c = RotaryCraft.config.getBlastFurnaceGatingMaterials(ConfigRegistry.GATEBLAST.getState(), Items.STONE_BRICKS, Items.STONE_BRICKS, Items.STONE_BRICKS, Items.STONE_BRICKS);
//
//        Object[] args = {
//                "BaB", "brc", "BdB", 'B', Items.STONE_BRICKS, 'r', Items.REDSTONE, 'a', c[0], 'b', c[1], 'c', c[2], 'd', c[3]
//        };
//        return args;
//    }
//
//    public static Object[] getWorktableIngredients() {
//        Object[] c = RotaryCraft.config.getBlastFurnaceGatingMaterials(ConfigRegistry.GATEWORK.getState() && !ConfigRegistry.TABLEMACHINES.getState(), null, null, Items.STONE_SLAB, Items.STONE_SLAB);
//
//        ArrayList<Object> li = new ArrayList<>(Arrays.asList("aCb", "SBS", "crd", 'r', Items.REDSTONE, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'B', Blocks.BRICKS, 'C', Blocks.CRAFTING_TABLE, 'a', c[0], 'b', c[1], 'c', c[2], 'd', c[3]));
//
//        if (c[0] == null)
//            li.set(0, ((String) li.get(0)).replace("a", " "));
//        if (c[1] == null)
//            li.set(0, ((String) li.get(0)).replace("b", " "));
//
//        for (int i = li.size() - 1; i >= 0; i -= 2) {
//            if (li.get(i) == null) {
//                li.remove(i);
//                li.remove(i - 1);
//            }
//        }
//
//        return li.toArray();
//    }
//
//    private static final Collection<RecipeHandler> recipeHandlers = new OneWayCollections.OneWayList<>();
//
//    public static void loadMachineRecipeHandlers() {
////        loadRecipeHandler(WorktableRecipes.getInstance());
////        loadRecipeHandler(RecipesBlastFurnace.getRecipes());
////        loadRecipeHandler(RecipesCentrifuge.getRecipes());
////        loadRecipeHandler(RecipesCompactor.getRecipes());
////        loadRecipeHandler(RecipesCrystallizer.getRecipes());
////        loadRecipeHandler(RecipesDryingBed.getRecipes());
////        loadRecipeHandler(RecipesFrictionHeater.getRecipes());
//        loadRecipeHandler(RecipesGrinder.getRecipes());
////        loadRecipeHandler(RecipesLavaMaker.getRecipes());
////        loadRecipeHandler(RecipesPulseFurnace.getRecipes());
////        loadRecipeHandler(RecipesWetter.getRecipes());
////        loadRecipeHandler(RecipesMagnetizer.getRecipes());
//    }
//
//    private static void loadRecipeHandler(RecipeHandler handler) {
//        recipeHandlers.add(handler);
//    }
//}
