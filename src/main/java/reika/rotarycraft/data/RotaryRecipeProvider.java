package reika.rotarycraft.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.ItemStack;
import java.util.HashMap;
import java.util.function.Consumer;

public class RotaryRecipeProvider extends RecipeProvider {

    //list of items for recipe, 9 items and one output
    private static final HashMap<ItemStack[], ItemStack> recipe = new HashMap<>();

    public RotaryRecipeProvider(PackOutput p_248933_) {
        super(p_248933_);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {

/*
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, pillar, 3)
                .pattern("S")
                .pattern("S")
                .pattern("S")
                .define('S', smooth)
                .unlockedBy("has_stone", has(smooth))
                .save(consumer, new ResourceLocation(RotaryCraft.MODID, RoCDataProviders.name()));
*/


    }

}
/*

    private static void addCraftItems() {
        if (ConfigRegistry.CRAFTABLEBEDROCK.getState())
            GameRegistry.addRecipe(new ItemStack(Blocks.bedrock), new Object[]{
                    "DDD", "DSD", "DDD", 'D', RotaryItems.bedrockdust, 'S', Blocks.stone});

        GameRegistry.addRecipe(RotaryItems.denseCanolaSeeds, new Object[]{"DDD", "DDD", "DDD", 'D', RotaryItems.canolaSeeds});

        GameRegistry.addShapelessRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.canolaSeeds, 9), RotaryItems.denseCanolaSeeds);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.torch, 8, 0), "C", "S", 'C', RotaryItems.coke, 'S', "stickWood"));

        GameRegistry.addRecipe(new ReservoirComboRecipe());
        GameRegistry.addShapelessRecipe(MachineRegistry.RESERVOIR.getCraftedProduct(), MachineRegistry.RESERVOIR.getCraftedProduct()); //empty
        GameRegistry.addShapelessRecipe(RotaryItems.CRAFTPATTERN.getStackOf(), RotaryItems.CRAFTPATTERN.getStackOf()); //empty

        //GameRegistry.addRecipe(new ScrapCombinationRecipe());

        GameRegistry.addRecipe(new ShapedOreRecipe(ReikaItemHelper.oakWood.asItemStack(), new Object[]{
                "WW", "WW", 'W', "dustWood"}));
        GameRegistry.addRecipe(new ShapedOreRecipe(ReikaItemHelper.spruceWood.asItemStack(), new Object[]{
                "WWD", "WW ", 'W', "dustWood", 'D', ReikaDyeHelper.BLACK.getOreDictName()}));
        GameRegistry.addRecipe(new ShapedOreRecipe(ReikaItemHelper.birchWood.asItemStack(), new Object[]{
                "WWD", "WW ", 'W', "dustWood", 'D', ReikaDyeHelper.WHITE.getOreDictName()}));
        GameRegistry.addRecipe(new ShapedOreRecipe(ReikaItemHelper.jungleWood.asItemStack(), new Object[]{
                "WWD", "WW ", 'W', "dustWood", 'D', ReikaDyeHelper.RED.getOreDictName()}));
        GameRegistry.addRecipe(new ShapedOreRecipe(ReikaItemHelper.acaciaWood.asItemStack(), new Object[]{
                "WWD", "WW ", 'W', "dustWood", 'D', ReikaDyeHelper.ORANGE.getOreDictName()}));
        GameRegistry.addRecipe(new ShapedOreRecipe(ReikaItemHelper.darkOakWood.asItemStack(), new Object[]{
                "WWD", "WW ", 'W', "dustWood", 'D', ReikaDyeHelper.BROWN.getOreDictName()}));

        GameRegistry.addRecipe(new ItemStack(Items.paper, 8, 0), new Object[]{
                " W ", "SSS", "RRR", 'R', Blocks.stone, 'S', RotaryItems.sawdust, 'W', Items.water_bucket});

        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.dirt), Blocks.sand, RotaryItems.compost);

        GameRegistry.addRecipe(BlockRegistry.BLASTPANE.getCraftedProduct(16), new Object[]{
                "OOO", "OOO", 'O', BlockRegistry.BLASTGLASS.getStackOf()});

        GameRegistry.addRecipe(new ItemStack(Blocks.tnt, 2), "sns", "nSn", "sns", 's', Items.sugar, 'S', Blocks.sand, 'n', RotaryItems.nitrate);

        //GameRegistry.addRecipe(new ItemStack(Items.diamond, 5), "ggg", "g g", "ggg", 'g', RotaryItems.diamondgear);
        GameRegistry.addShapelessRecipe(new ItemStack(Items.diamond), RotaryItems.diamondshaft);

        for (GearboxTypes gear : GearboxTypes.typeList) {
            //for (int k = 2; k < 16; k *= 2) {
            //	GameRegistry.addShapelessRecipe(ReikaItemHelper.getSizedItemStack(mat.getGearUnitItem(k), 2), mat.getGearUnitItem(k*2));
            //}
            //anything else is not an even split
            GameRegistry.addShapelessRecipe(ReikaItemHelper.getSizedItemStack(gear.getPart(GearPart.UNIT4), 2), gear.getPart(GearPart.UNIT16));
            GameRegistry.addShapelessRecipe(ReikaItemHelper.getSizedItemStack(gear.getPart(GearPart.UNIT2), 2), gear.getPart(GearPart.UNIT4));
            GameRegistry.addShapelessRecipe(ReikaItemHelper.getSizedItemStack(gear.getPart(GearPart.GEAR), 2), gear.getPart(GearPart.UNIT2));
        }
        GameRegistry.addRecipe(RotaryItems.anthrablock, "BBB", "BBB", "BBB", 'B', RotaryItems.anthracite);
        GameRegistry.addRecipe(RotaryItems.lonsblock, "BBB", "BBB", "BBB", 'B', RotaryItems.lonsda);
        GameRegistry.addRecipe(RotaryItems.bedingotblock, "BBB", "BBB", "BBB", 'B', RotaryItems.bedingot);
        GameRegistry.addRecipe(RotaryItems.cokeblock, "BBB", "BBB", "BBB", 'B', RotaryItems.coke);

        GameRegistry.addRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.shieldblock, 4), " S ", "SOS", " S ", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'O', Blocks.obsidian);

        GameRegistry.addShapelessRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.HSLA_STEEL_INGOT.get(), 9), RotaryItems.steelblock);
        GameRegistry.addShapelessRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.anthracite, 9), RotaryItems.anthrablock);
        GameRegistry.addShapelessRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.lonsda, 9), RotaryItems.lonsblock);
        GameRegistry.addShapelessRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.bedingot, 9), RotaryItems.bedingotblock);
        GameRegistry.addShapelessRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.coke, 9), RotaryItems.cokeblock);

        GameRegistry.addRecipe(new ShapelessOreRecipe(RotaryItems.silveriodide, RotaryItems.salt, "ingotSilver"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.nitrate, 4), Items.gunpowder, "dustRedstone", "dustSalt", "dustCoal"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.nitrate, 4), Items.gunpowder, "dustRedstone", "foodSalt", "dustCoal"));
        if (ReikaItemHelper.oreItemExists("dustGold"))
            GameRegistry.addRecipe(new ShapelessOreRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.redgolddust, 2), Items.redstone, "dustGold"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.redgolddust, 2), Items.redstone, RotaryItems.goldoreflakes));

        GameRegistry.addRecipe(RotaryItems.RAILGUN.getCraftedProduct(3), new Object[]{
                "ss ", "s  ", 's', RotaryItems.HSLA_STEEL_INGOT.get()});

        int amt = DifficultyEffects.RAILGUNCRAFT.getInt();
        GameRegistry.addRecipe(new ShapedOreRecipe(RotaryItems.RAILGUN.getCraftedMetadataProduct(amt, 1), new Object[]{
                "p  ", " s ", "  p", 's', RotaryItems.RAILGUN.getStackOfMetadata(0), 'p', "plankWood"}));
        GameRegistry.addRecipe(new ShapedOreRecipe(RotaryItems.RAILGUN.getCraftedMetadataProduct(amt, 2), new Object[]{
                "p p", " s ", "p p", 's', RotaryItems.RAILGUN.getStackOfMetadata(1), 'p', "plankWood"}));
        GameRegistry.addRecipe(new ShapedOreRecipe(RotaryItems.RAILGUN.getCraftedMetadataProduct(amt, 3), new Object[]{
                "ppp", "psp", "ppp", 's', RotaryItems.RAILGUN.getStackOfMetadata(2), 'p', "plankWood"}));
        GameRegistry.addRecipe(RotaryItems.RAILGUN.getCraftedMetadataProduct(amt, 4), new Object[]{
                "p  ", " s ", "  p", 's', RotaryItems.RAILGUN.getStackOfMetadata(3), 'p', Blocks.stone});
        GameRegistry.addRecipe(RotaryItems.RAILGUN.getCraftedMetadataProduct(amt, 5), new Object[]{
                "p p", " s ", "p p", 's', RotaryItems.RAILGUN.getStackOfMetadata(4), 'p', Blocks.stone});
        GameRegistry.addRecipe(RotaryItems.RAILGUN.getCraftedMetadataProduct(amt, 6), new Object[]{
                "ppp", "psp", "ppp", 's', RotaryItems.RAILGUN.getStackOfMetadata(5), 'p', Blocks.stone});
        GameRegistry.addRecipe(RotaryItems.RAILGUN.getCraftedMetadataProduct(amt, 7), new Object[]{
                "p  ", " s ", "  p", 's', RotaryItems.RAILGUN.getStackOfMetadata(6), 'p', Items.iron_ingot});
        GameRegistry.addRecipe(RotaryItems.RAILGUN.getCraftedMetadataProduct(amt, 8), new Object[]{
                "p p", " s ", "p p", 's', RotaryItems.RAILGUN.getStackOfMetadata(7), 'p', Items.iron_ingot});
        GameRegistry.addRecipe(RotaryItems.RAILGUN.getCraftedMetadataProduct(amt, 9), new Object[]{
                "ppp", "psp", "ppp", 's', RotaryItems.RAILGUN.getStackOfMetadata(8), 'p', Items.iron_ingot});
        GameRegistry.addRecipe(RotaryItems.RAILGUN.getCraftedMetadataProduct(amt, 10), new Object[]{
                "p  ", " s ", "  p", 's', RotaryItems.RAILGUN.getStackOfMetadata(9), 'p', Items.gold_ingot});
        GameRegistry.addRecipe(RotaryItems.RAILGUN.getCraftedMetadataProduct(amt, 11), new Object[]{
                "p p", " s ", "p p", 's', RotaryItems.RAILGUN.getStackOfMetadata(10), 'p', Items.gold_ingot});
        GameRegistry.addRecipe(RotaryItems.RAILGUN.getCraftedMetadataProduct(amt, 12), new Object[]{
                "ppp", "psp", "ppp", 's', RotaryItems.RAILGUN.getStackOfMetadata(11), 'p', Items.gold_ingot});
        GameRegistry.addRecipe(RotaryItems.RAILGUN.getCraftedMetadataProduct(amt, 13), new Object[]{
                "p  ", " s ", "  p", 's', RotaryItems.RAILGUN.getStackOfMetadata(12), 'p', RotaryItems.bedingot});
        GameRegistry.addRecipe(RotaryItems.RAILGUN.getCraftedMetadataProduct(amt, 14), new Object[]{
                "p p", " s ", "p p", 's', RotaryItems.RAILGUN.getStackOfMetadata(13), 'p', RotaryItems.bedingot});
        GameRegistry.addRecipe(RotaryItems.RAILGUN.getCraftedMetadataProduct(amt, 15), new Object[]{
                "ppp", "psp", "ppp", 's', RotaryItems.RAILGUN.getStackOfMetadata(14), 'p', RotaryItems.bedingot});

        GameRegistry.addRecipe(RotaryItems.MINECART.getCraftedProduct(1), new Object[]{
                "g", "m", 'g', EngineType.GAS.getCraftedProduct(), 'm', new ItemStack(Items.minecart)});

        GameRegistry.addRecipe(RotaryItems.SHELL.getCraftedProduct(16), new Object[]{
                " s ", "sns", " s ", 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'n', RotaryItems.nitrate});
        if (!ModList.RAILCRAFT.isLoaded()) {
            GameRegistry.addRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.ironscrap, 3), new Object[]{
                    "rrr", "rrr", "rr ", 'r', Blocks.rail});
        }
        GameRegistry.addRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.ironscrap, 3), new Object[]{
                "rrr", "rrr", "rr ", 'r', Blocks.iron_bars});

        GameRegistry.addRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.ironscrap, 3), new Object[]{
                "b", 'b', Items.bucket});

        GameRegistry.addRecipe(RotaryItems.steelblock, "BBB", "BBB", "BBB", 'B', RotaryItems.HSLA_STEEL_INGOT.get());
        GameRegistry.addRecipe(RotaryItems.impeller, " S ", "SGS", " S ", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'G', RotaryItems.steelgear);
        GameRegistry.addRecipe(new ShapedOreRecipe(RotaryItems.impeller, " S ", "SGS", " S ", 'S', "ingotTin", 'G', RotaryItems.steelgear));
        GameRegistry.addRecipe(RotaryItems.compressor, "SSS", "SGS", "SSS", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'G', RotaryItems.steelgear);
        GameRegistry.addRecipe(RotaryItems.turbine, "sss", "sGs", "sss", 's', RotaryItems.prop, 't', RotaryItems.tungsteningot, 'G', RotaryItems.compressor);
        GameRegistry.addRecipe(RotaryItems.diffuser, " SS", "S  ", " SS", 'S', RotaryItems.HSLA_STEEL_INGOT.get());
        GameRegistry.addRecipe(RotaryItems.radiator, "GGG", "PPP", "SSS", 'G', Items.gold_ingot, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'P', RotaryItems.pipe);
        GameRegistry.addRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.condenser, DifficultyEffects.SMALLERCRAFT.getInt()), "SPS", "PSP", "SPS", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'P', RotaryItems.pipe);
        GameRegistry.addRecipe(RotaryItems.goldcoil, "GGG", "GSG", "GGG", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'G', Items.gold_ingot);

        ReikaRecipeHelper.addOreRecipe(RotaryItems.goldcoil, "GGG", "GSG", "GGG", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'G', "ingotElectrum");

        GameRegistry.addRecipe(RotaryItems.combustor, "SSS", "SRS", "SGS", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'G', RotaryItems.igniter, 'R', Items.redstone);
        //GameRegistry.addRecipe(RotaryItems.highcombustor, "SiS", "iRi", "SGS", 'i', RotaryItems.redgoldingot, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'G', RotaryItems.igniter, 'R', Items.redstone);
        RecipesBlastFurnace.getRecipes().add3x3Crafting(RotaryItems.highcombustor, 1100, 1, 0, "SiS", "iRi", "SGS", 'i', RotaryItems.redgoldingot, 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'G', RotaryItems.igniter, 'R', Items.redstone);

        GameRegistry.addRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.cylinder, 2), new Object[]{
                "SSS", "S S", "SSS", 'S', RotaryItems.HSLA_STEEL_INGOT.get()});
        GameRegistry.addRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.aluminumcylinder, 2), new Object[]{
                "SSS", "S S", "SSS", 'S', RotaryItems.silumin});

        GameRegistry.addRecipe(RotaryItems.compoundturb, new Object[]{
                " tS", "tst", "St ", 'S', RotaryItems.turbine, 's', GearboxTypes.TUNGSTEN.getPart(GearPart.SHAFTCORE), 't', RotaryItems.tungsteningot});
        GameRegistry.addRecipe(RotaryItems.compoundcompress, new Object[]{
                " tS", "tst", "St ", 'S', RotaryItems.compressor, 's', GearboxTypes.TUNGSTEN.getPart(GearPart.SHAFTCORE), 't', RotaryItems.tungsteningot});

        for (GearboxTypes gear : GearboxTypes.typeList) {
            GameRegistry.addRecipe(new ShapedOreRecipe(gear.getPart(GearPart.SHAFTCORE), new Object[]{"  s", " S ", "s  ", 'S', gear.getBaseItem(), 's', gear.getShaftUnitItem()}));
        }

        GameRegistry.addRecipe(RotaryItems.igniter, new Object[]{
                "G G", "SRS", "SSS", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'R', Items.redstone, 'G', Items.gold_ingot});

        ReikaRecipeHelper.addOreRecipe(RotaryItems.igniter, "G G", "SRS", "SSS", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'R', Items.redstone, 'G', "ingotElectrum");

        GameRegistry.addRecipe(RotaryItems.waterplate, new Object[]{
                "PPP", "PPP", "SSS", 'P', RotaryItems.basepanel, 'S', RotaryItems.springingot});

        GameRegistry.addRecipe(RotaryItems.prop, new Object[]{
                " S ", " I ", " P ", 'P', RotaryItems.basepanel, 'S', RotaryItems.shaftitem, 'I', RotaryItems.HSLA_STEEL_INGOT.get()});
        GameRegistry.addRecipe(RotaryItems.prop, new Object[]{
                " P ", " I ", " S ", 'P', RotaryItems.basepanel, 'S', RotaryItems.shaftitem, 'I', RotaryItems.HSLA_STEEL_INGOT.get()});

        GameRegistry.addRecipe(RotaryItems.hub, new Object[]{
                "  B", " C ", "G  ", 'G', RotaryItems.steelgear, 'B', RotaryItems.bearing, 'C', RotaryItems.shaftcore});
        GameRegistry.addRecipe(RotaryItems.mirror, new Object[]{
                "GGG", "III", 'G', Blocks.glass, 'I', Items.iron_ingot});

        ReikaRecipeHelper.addOreRecipe(RotaryItems.mirror, "GGG", "III", 'G', Blocks.glass, 'I', "ingotSilver");

        GameRegistry.addRecipe(RotaryItems.railhead, new Object[]{
                "LLL", "LGL", "LLL", 'G', RotaryItems.power, 'L', RotaryItems.lim});
        GameRegistry.addRecipe(RotaryItems.railbase, new Object[]{
                " S ", "PGP", 'P', RotaryItems.basepanel, 'G', RotaryItems.gearunit, 'S', RotaryItems.steelgear});
        GameRegistry.addRecipe(RotaryItems.railaim, new Object[]{
                "sds", "CRC", "sgs", 'R', RotaryItems.radar, 'C', RotaryItems.pcb, 's', RotaryItems.HSLA_STEEL_INGOT.get(), 'd', Items.diamond, 'g', RotaryItems.generator});
		*/
/*
		GameRegistry.addRecipe(RotaryItems.bedingot, new Object[]{
				" B ", "BSB", " B ", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'B', RotaryItems.bedrockdust});*//*


        GameRegistry.addRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.basepanel, DifficultyEffects.PARTCRAFT.getInt()), new Object[]{
                "SSS", 'S', RotaryItems.HSLA_STEEL_INGOT.get()});

        GameRegistry.addRecipe(RotaryItems.mount, "S S", "SBS", 'B', RotaryItems.basepanel, 'S', RotaryItems.HSLA_STEEL_INGOT.get());
        GameRegistry.addRecipe(new ShapedOreRecipe(RotaryItems.mount, "S S", "SBS", 'B', RotaryItems.basepanel, 'S', "ingotTin"));

        GameRegistry.addRecipe(RotaryItems.drill, new Object[]{
                "SSS", "SSS", " S ", 'S', RotaryItems.HSLA_STEEL_INGOT.get()});
        GameRegistry.addRecipe(RotaryItems.presshead, new Object[]{
                "SOD", "ODB", "DBB", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'D', Items.diamond, 'O', Blocks.obsidian, 'B', RotaryItems.bedrockdust});
        GameRegistry.addRecipe(RotaryItems.screen, new Object[]{
                "SGS", "SCS", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'C', RotaryItems.pcb, 'G', Blocks.glass_pane});
        GameRegistry.addRecipe(RotaryItems.mixer, new Object[]{
                " S ", "SIS", " S ", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'I', RotaryItems.impeller});
        GameRegistry.addRecipe(RotaryItems.saw, new Object[]{
                "S S", " C ", "S S", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'C', RotaryItems.steelgear});
        GameRegistry.addRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.pcb, 2), new Object[]{
                "PGP", "RER", "GPG", 'P', RotaryItems.HSLA_STEEL_INGOT.get(), 'G', Items.gold_ingot, 'R', Items.redstone, 'E', Items.ender_pearl});
        GameRegistry.addRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.pcb, 3), new Object[]{
                "PGP", "RER", "GPG", 'P', RotaryItems.HSLA_STEEL_INGOT.get(), 'G', Items.gold_ingot, 'R', Items.redstone, 'E', RotaryItems.silicon});

        ReikaRecipeHelper.addOreRecipe(RotaryItems.pcb, "PGP", "RER", "GPG", 'P', RotaryItems.HSLA_STEEL_INGOT.get(), 'G', "ingotElectrum", 'R', Items.redstone, 'E', "ingotCopper");

        GameRegistry.addRecipe(RotaryItems.sonar, new Object[]{
                " S ", "SNS", "RCR", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'R', Items.redstone, 'N', Blocks.noteblock, 'C', RotaryItems.pcb});
        GameRegistry.addRecipe(RotaryItems.radar, new Object[]{
                "SSS", " G ", "RMR", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'R', Items.redstone, 'G', Items.gold_ingot, 'M', EngineType.DC.getCraftedProduct()});

        GameRegistry.addRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.belt, DifficultyEffects.BELTCRAFT.getInt()), new Object[]{
                "LLL", "LSL", "LLL", 'L', Items.leather, 'S', RotaryItems.HSLA_STEEL_INGOT.get()});

        GameRegistry.addRecipe(GearboxTypes.STONE.getPart(GearPart.BEARING), new Object[]{
                "LLL", "LSL", "LLL", 'L', RotaryItems.ballbearing, 'S', RotaryItems.stonegear});
        GameRegistry.addRecipe(GearboxTypes.STEEL.getPart(GearPart.BEARING), new Object[]{
                "LLL", "LSL", "LLL", 'L', RotaryItems.ballbearing, 'S', RotaryItems.HSLA_STEEL_INGOT.get()});
        GameRegistry.addRecipe(GearboxTypes.TUNGSTEN.getPart(GearPart.BEARING), new Object[]{
                "LLL", "LSL", "LLL", 'L', RotaryItems.ballbearing, 'S', RotaryItems.springtungsten});
        GameRegistry.addRecipe(GearboxTypes.DIAMOND.getPart(GearPart.BEARING), new Object[]{
                "LLL", "LSL", "LLL", 'L', RotaryItems.ballbearing, 'S', RotaryItems.diamondgear});
        RecipesBlastFurnace.getRecipes().add3x3Crafting(GearboxTypes.BEDROCK.getPart(GearPart.BEARING), 1000, 2, 0, "LLL", "LSL", "LLL", 'L', RotaryItems.bedrockdust, 'S', RotaryItems.bearing);

        GameRegistry.addShapelessRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.ballbearing, 4), RotaryItems.HSLA_STEEL_INGOT.get());
        GameRegistry.addRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.ballbearing, 16), "SS", "SS", 'S', RotaryItems.HSLA_STEEL_INGOT.get());

        GameRegistry.addRecipe(RotaryItems.brake, new Object[]{
                " g ", "SBS", " G ", 'g', RotaryItems.gearunit, 'G', RotaryItems.steelgear, 'S', RotaryItems.shaftitem, 'B', RotaryItems.bearing});
        GameRegistry.addRecipe(RotaryItems.tenscoil, new Object[]{
                "WWW", "WSW", "WWW", 'W', RotaryItems.SPRING.getStackOf(), 'S', RotaryItems.shaftitem});
        GameRegistry.addRecipe(RotaryItems.bedrockcoil, new Object[]{
                "WWW", "WSW", "WWW", 'W', RotaryItems.STRONGCOIL.getStackOf(), 'S', RotaryItems.shaftitem});

        GameRegistry.addRecipe(RotaryItems.LENS.get(), new Object[]{
                " D ", "DGD", " D ", 'D', Items.DIAMOND, 'G', BlockRegistry.BLASTGLASS.getStackOf()});
        GameRegistry.addRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.power, 2), new Object[]{
                "RER", "GGG", "SSS", 'R', Items.REDSTONE, 'G', Items.gold_ingot, 'E', Items.ender_eye, 'S', RotaryItems.HSLA_STEEL_INGOT.get()});
        GameRegistry.addRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.power, 3), new Object[]{
                "RER", "GGG", "SSS", 'R', Items.REDSTONE, 'G', Items.gold_ingot, 'E', RotaryItems.silicon, 'S', RotaryItems.HSLA_STEEL_INGOT.get()});

        ReikaRecipeHelper.addOreRecipe(RotaryItems.power, "RER", "GGG", "SSS", 'R', Items.redstone, 'G', "ingotElectrum", 'E', "ingotCopper", 'S', RotaryItems.HSLA_STEEL_INGOT.get());

        GameRegistry.addRecipe(RotaryItems.BARREL, new Object[]{
                "OOO", "gtG", "OOO", 't', RotaryItems.TUNGSTEN_INGOT.get(), 'O', Blocks.obsidian, 'G', BlockRegistry.BLASTGLASS.getStackOf(), 'g', Blocks.glowstone});
        GameRegistry.addRecipe(RotaryItems.BULB, new Object[]{
                "GGG", "BDB", "BRB", 'D', Items.nether_star, 'G', Blocks.glowstone, 'R', Items.redstone, 'B', Items.blaze_rod});


        for (GearboxTypes gear : GearboxTypes.typeList) {
            if (!gear.isLoadable())
                continue;
            GameRegistry.addRecipe(gear.getPart(GearPart.UNIT2), new Object[]{
                    " GB", "BG ", 'B', gear.getShaftUnitItem(), 'G', gear.getPart(GearPart.GEAR)});
            GameRegistry.addRecipe(gear.getPart(GearPart.UNIT4), new Object[]{
                    " GB", "BG ", 'B', gear.getShaftUnitItem(), 'G', gear.getPart(GearPart.UNIT2)});

            GameRegistry.addRecipe(gear.getPart(GearPart.UNIT8), new Object[]{
                    " gB", "BG ", 'B', gear.getShaftUnitItem(), 'G', gear.getPart(GearPart.UNIT4), 'g', gear.getPart(GearPart.UNIT2)});
            GameRegistry.addRecipe(gear.getPart(GearPart.UNIT8), new Object[]{
                    " gB", "BG ", 'B', gear.getShaftUnitItem(), 'G', gear.getPart(GearPart.UNIT2), 'g', gear.getPart(GearPart.UNIT4)});
            GameRegistry.addRecipe(gear.getPart(GearPart.UNIT16), new Object[]{
                    " gB", "BG ", 'B', gear.getShaftUnitItem(), 'G', gear.getPart(GearPart.UNIT8), 'g', gear.getPart(GearPart.UNIT2)});
            GameRegistry.addRecipe(gear.getPart(GearPart.UNIT16), new Object[]{
                    " gB", "BG ", 'B', gear.getShaftUnitItem(), 'G', gear.getPart(GearPart.UNIT2), 'g', gear.getPart(GearPart.UNIT8)});

            GameRegistry.addRecipe(gear.getPart(GearPart.UNIT16), new Object[]{
                    " GB", "BG ", 'B', gear.getShaftUnitItem(), 'G', gear.getPart(GearPart.UNIT4)});
        }

        GameRegistry.addRecipe(new ShapedOreRecipe(RotaryItems.woodgear, new Object[]{
                " W ", "WWW", " W ", 'W', "plankWood"}));
        GameRegistry.addRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.stonegear, 2), new Object[]{
                " W ", "WWW", " W ", 'W', Blocks.stone});
        GameRegistry.addRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.steelgear, DifficultyEffects.PARTCRAFT.getInt()), new Object[]{
                " B ", "BBB", " B ", 'B', RotaryItems.HSLA_STEEL_INGOT.get()});
        GameRegistry.addRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.tungstengear, 5 * DifficultyEffects.PARTCRAFT.getInt() / 3), new Object[]{
                " W ", "WWW", " W ", 'W', RotaryItems.springtungsten});
        //GameRegistry.addRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.diamondgear, 8*DifficultyEffects.PARTCRAFT.getInt()/3), new Object[]{
        //		" W ", "WGW", " W ", 'W', Items.diamond, 'G', RotaryItems.tungstengear});
        RecipesBlastFurnace.getRecipes().add3x3Crafting(ReikaItemHelper.getSizedItemStack(RotaryItems.diamondgear, 6 * DifficultyEffects.PARTCRAFT.getInt() / 3), 1400, 1, 0, " W ", "WGW", " W ", 'W', Items.diamond, 'G', RotaryItems.tungstengear);

        GameRegistry.addRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.stonerod, 2), new Object[]{
                "  B", " B ", "B  ", 'B', Blocks.stone});
        GameRegistry.addRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.shaftitem, DifficultyEffects.PARTCRAFT.getInt()), new Object[]{
                "  B", " B ", "B  ", 'B', RotaryItems.HSLA_STEEL_INGOT.get()});
        GameRegistry.addRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.tungstenshaft, DifficultyEffects.PARTCRAFT.getInt()), new Object[]{
                "  B", " B ", "B  ", 'B', RotaryItems.springtungsten});
        GameRegistry.addRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.diamondshaft, DifficultyEffects.PARTCRAFT.getInt()), new Object[]{
                "  B", " B ", "B  ", 'B', Items.diamond});

        Object[] params = new Object[]{" D ", "DSD", " D ", 'D', RotaryItems.bedrockdust, 'S', RotaryItems.shaftitem};
        //GameRegistry.addRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.bedrockshaft, 4), params);
        RecipesBlastFurnace.getRecipes().add3x3Crafting(ReikaItemHelper.getSizedItemStack(RotaryItems.bedrockshaft, 4), 1000, 2, 0, params);

        GameRegistry.addRecipe(RotaryItems.wormgear, new Object[]{
                "S  ", " G ", "  S", 'S', RotaryItems.shaftitem, 'G', RotaryItems.steelgear});

        params = new Object[]{"bWb", "WWW", "bWb", 'b', RotaryItems.bedrockdust, 'W', RotaryItems.HSLA_STEEL_INGOT.get()};
        //GameRegistry.addRecipe(new ItemStack(RotaryItems.bedrockgear, 8, RotaryItems.bedrockgear.getItemDamage()), params);
        RecipesBlastFurnace.getRecipes().add3x3Crafting(ReikaItemHelper.getSizedItemStack(RotaryItems.bedrockgear, 8), 1000, 2, 0, params);

        GameRegistry.addRecipe(RotaryItems.lim, new Object[]{
                "WRW", "NNN", 'W', RotaryItems.goldcoil, 'N', RotaryItems.HSLA_STEEL_INGOT.get(), 'R', Items.redstone});

        GameRegistry.addRecipe(RotaryItems.generator, new Object[]{
                "  G", " C ", "G  ", 'G', RotaryItems.goldcoil, 'C', RotaryItems.shaftcore});

        GameRegistry.addRecipe(ReikaItemHelper.getSizedItemStack(RotaryItems.CHAIN_LINK.get().getDefaultInstance(), 4), "s s", " s ", "s s", 's', RotaryItems.HSLA_STEEL_INGOT.get());
        GameRegistry.addRecipe(BlockRegistry.DECOTANK.getCraftedProduct(4), "SGS", "GGG", "SGS", 'S', RotaryItems.HSLA_STEEL_INGOT.get(), 'G', Blocks.GLASS_PANE);
        GameRegistry.addRecipe(new DecoTankSettingsRecipe());

        for (Flywheels f : Flywheels.list) {
            Object raw = f.getRawMaterial();
            if (raw instanceof String) {
                if (OreDictionary.getOres((String) raw).isEmpty())
                    raw = null;
            }
            if (raw != null)
                GameRegistry.addRecipe(new ShapedOreRecipe(f.getCore(), "WWW", "WGW", "WWW", 'W', raw, 'G', RotaryItems.STEELGEAR));
        }

    }

    private static void addFurnace() {
        ReikaRecipeHelper.addSmelting(RotaryItems.aluminumpowder, RotaryItems.aluminumingot, 0.4F);
        ReikaRecipeHelper.addSmelting(RotaryItems.cleansludge, RotaryItems.ETHANOL.getStackOf(), 0.5F);

        ReikaRecipeHelper.addSmelting(RotaryItems.EXTRACTS.getStackOfMetadata(24), new ItemStack(Items.COAL, 1, 0), 0.1F);
        ReikaRecipeHelper.addSmelting(RotaryItems.EXTRACTS.getStackOfMetadata(25), new ItemStack(Items.IRON_INGOT, 1, 0), 0.7F);
        ReikaRecipeHelper.addSmelting(RotaryItems.EXTRACTS.getStackOfMetadata(26), new ItemStack(Items.GOLD_INGOT, 1, 0), 1F);
        ReikaRecipeHelper.addSmelting(RotaryItems.EXTRACTS.getStackOfMetadata(27), new ItemStack(Items.REDSTONE, 4, 0), 0.5F);
        ReikaRecipeHelper.addSmelting(RotaryItems.EXTRACTS.getStackOfMetadata(28), new ItemStack(Items.DYE, 6, 4), 0.6F);
        ReikaRecipeHelper.addSmelting(RotaryItems.EXTRACTS.getStackOfMetadata(29), new ItemStack(Items.DIAMOND, 1, 0), 1F);
        ReikaRecipeHelper.addSmelting(RotaryItems.EXTRACTS.getStackOfMetadata(30), new ItemStack(Items.EMERALD, 1, 0), 1F);
        ReikaRecipeHelper.addSmelting(RotaryItems.EXTRACTS.getStackOfMetadata(31), new ItemStack(Items.QUARTZ, 1, 0), 1F);
        ReikaRecipeHelper.addSmelting(RotaryItems.EXTRACTS.getStackOfMetadata(32), RotaryItems.silveringot.copy(), 1F);
        ReikaRecipeHelper.addSmelting(RotaryItems.FLOUR, new ItemStack(Items.BREAD), 0.2F);

        ExtractorModOres.addSmelting();
        ReikaRecipeHelper.addSmelting(RotaryItems.IRON_SCRAP, new ItemStack(Items.IRON_INGOT), 0.4F);
    }
  */
/*  private static void addSlideRecipes() {
        GameRegistry.addRecipe(RotaryItems.SLIDE.getCraftedProduct(4), new Object[]{
                "PPP", "PGP", "PPP", 'G', Blocks.glass_pane, 'P', Items.paper});

        ItemStack is = RotaryItems.SLIDE.getCraftedMetadataProduct(2, 24);
        is.stackTagCompound.setString("file", "[NO FILE]");
        GameRegistry.addRecipe(is, new Object[]{
                "rPr", "PGP", "rPr", 'G', Blocks.GLASS_PANE, 'P', Items.PAPER, 'r', Items.REDSTONE});

        Random r = new Random();
        HashMap<Integer, Integer> colors = new HashMap<Integer, Integer>();
        boolean[] hasMapping = new boolean[16];
        for (int i = 0; i < 16; i++) {
            int randVal = r.nextInt(16);
            while (hasMapping[randVal]) {
                randVal = r.nextInt(16);
            }
            colors.put(i, randVal);
            hasMapping[randVal] = true;
            //ReikaJavaLibrary.pConsole("Color "+i+" registered to value "+randVal);
        }
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 24; j++) {
                int color = colors.get(i);
                while (color+j >= 24) {
                    color -= 24;
                }
                if (color+j < 0)
                    throw new RuntimeException("Color mapping < 0 at color "+color+" and dye color "+i+" for slide "+j);
                GameRegistry.addShapelessRecipe(RotaryItems.SLIDE.getCraftedMetadataProduct(1, color+j), new ItemStack(Items.dye, 1, i), RotaryItems.SLIDE.getStackOfMetadata(j));
                //ReikaJavaLibrary.pConsole("Registering recipe with slide "+j+" and color "+i+" to result slide "+(color+j));
            }
        }
    }*//*


}*/
