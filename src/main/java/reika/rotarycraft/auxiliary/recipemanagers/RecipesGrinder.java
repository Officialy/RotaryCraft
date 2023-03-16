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

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ForgeItemTagsProvider;
import net.minecraftforge.registries.ForgeRegistries;
import reika.dragonapi.ModList;
import reika.dragonapi.exception.MisuseException;
import reika.dragonapi.instantiable.data.maps.ItemHashMap;
import reika.dragonapi.instantiable.io.CustomRecipeList;
import reika.dragonapi.instantiable.io.LuaBlock;
import reika.dragonapi.interfaces.registry.OreType;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.dragonapi.libraries.registry.ReikaDyeHelper;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.dragonapi.libraries.registry.ReikaTreeHelper;
import reika.dragonapi.modregistry.ModOreList;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.api.RecipeInterface;
import reika.rotarycraft.api.RecipeInterface.GrinderManager;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryItems;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecipesGrinder extends RecipeHandler implements GrinderManager {

    public static final RecipesGrinder grinderRecipes = new RecipesGrinder();

    public static final int ore_rate = 3;

    private final ItemHashMap<GrinderRecipe> recipes = new ItemHashMap<>();

    public static final RecipesGrinder getRecipes() {
        return grinderRecipes;
    }

    private RecipesGrinder() {
        super(MachineRegistry.GRINDER);
        RecipeInterface.grinder = this;

        this.addRecipe(Blocks.STONE, new ItemStack(Blocks.COBBLESTONE), RecipeLevel.PERIPHERAL);
        this.addRecipe(Blocks.COBBLESTONE, new ItemStack(Blocks.GRAVEL), RecipeLevel.PERIPHERAL);
        this.addRecipe(Blocks.GRAVEL, new ItemStack(Blocks.SAND), RecipeLevel.PERIPHERAL);
        this.addRecipe(Blocks.GLASS, new ItemStack(Blocks.SAND), RecipeLevel.PERIPHERAL);
        this.addRecipe(Blocks.SANDSTONE, new ItemStack(Blocks.SAND), RecipeLevel.PERIPHERAL);
        this.addRecipe(Blocks.SANDSTONE_STAIRS, new ItemStack(Blocks.SAND, 6), RecipeLevel.PERIPHERAL);
        this.addRecipe(Blocks.GLOWSTONE, new ItemStack(Items.GLOWSTONE_DUST, 4), RecipeLevel.PERIPHERAL);
        this.addRecipe(Blocks.STONE_BRICKS, new ItemStack(Blocks.COBBLESTONE), RecipeLevel.PERIPHERAL);
        this.addRecipe(Blocks.FURNACE, new ItemStack(Blocks.COBBLESTONE, 8), RecipeLevel.PERIPHERAL);
        this.addRecipe(Blocks.BRICKS, new ItemStack(Items.CLAY_BALL, 4), RecipeLevel.PERIPHERAL);
        this.addRecipe(Blocks.CLAY, new ItemStack(Items.CLAY_BALL, 4), RecipeLevel.PERIPHERAL);
        this.addRecipe(Blocks.BRICK_STAIRS, new ItemStack(Items.CLAY_BALL, 6), RecipeLevel.PERIPHERAL);
        this.addRecipe(Items.BRICK, new ItemStack(Items.CLAY_BALL), RecipeLevel.PERIPHERAL);
        this.addRecipe(Blocks.STONE_STAIRS, new ItemStack(Blocks.GRAVEL, 2), RecipeLevel.PERIPHERAL);
        this.addRecipe(Blocks.STONE_BRICK_STAIRS, new ItemStack(Blocks.COBBLESTONE, 2), RecipeLevel.PERIPHERAL);

        this.addRecipe(Blocks.NETHERRACK, RotaryItems.NETHERRACK_DUST.get().getDefaultInstance(), RecipeLevel.CORE);
        this.addRecipe(Blocks.SOUL_SAND, RotaryItems.TAR.get().getDefaultInstance(), RecipeLevel.CORE);

        this.addRecipe(Items.WHEAT, ReikaItemHelper.getSizedItemStack(RotaryItems.FLOUR.get().getDefaultInstance(), 3), RecipeLevel.PERIPHERAL);

        this.addRecipe(RotaryItems.BEDROCK_ALLOY_INGOT.get(), ReikaItemHelper.getSizedItemStack(RotaryItems.BEDROCK_DUST.get().getDefaultInstance(), 4), RecipeLevel.PROTECTED);
        this.addRecipe(RotaryItems.ALUMINUM_ALLOY_INGOT.get().getDefaultInstance(), RotaryItems.ALUMINUM_ALLOY_POWDER.get().getDefaultInstance(), RecipeLevel.PROTECTED);

        this.addRecipe(Items.SUGAR_CANE, new ItemStack(Items.SUGAR, 3), RecipeLevel.PROTECTED);//, ReikaItemHelper.getSizedItemStack(RotaryItems.mulch, PlantMaterials.SUGARCANE.getPlantValue()));
        this.addRecipe(Items.BONE, new ItemStack(Items.BONE_MEAL, 9), RecipeLevel.PROTECTED);
        this.addRecipe(Items.BLAZE_ROD, new ItemStack(Items.BLAZE_POWDER, 6), RecipeLevel.PROTECTED);
        this.addRecipe(Blocks.ICE, new ItemStack(Items.SNOWBALL, 4), RecipeLevel.PROTECTED);

        for (int i = 0; i < ReikaTreeHelper.treeList.length; i++) {
            ReikaTreeHelper tree = ReikaTreeHelper.treeList[i];
            this.addRecipe(tree.getLog().asItemStack(), this.getSizedSawdust(16), RecipeLevel.PERIPHERAL);
            this.addRecipe(tree.getPlank().asItemStack(), this.getSizedSawdust(4), RecipeLevel.PERIPHERAL);
        }
        this.addRecipe(Blocks.NOTE_BLOCK, this.getSizedSawdust(32), RecipeLevel.PERIPHERAL);
        this.addRecipe(Blocks.JUKEBOX, this.getSizedSawdust(32), RecipeLevel.PERIPHERAL);
        this.addBlockTagRecipe(BlockTags.FENCE_GATES, this.getSizedSawdust(4), RecipeLevel.PERIPHERAL);
        this.addRecipe(Blocks.OAK_STAIRS, this.getSizedSawdust(6), RecipeLevel.PERIPHERAL);
        this.addRecipe(Blocks.BIRCH_STAIRS, this.getSizedSawdust(6), RecipeLevel.PERIPHERAL);
        this.addRecipe(Blocks.SPRUCE_STAIRS, this.getSizedSawdust(6), RecipeLevel.PERIPHERAL);
        this.addRecipe(Blocks.JUNGLE_STAIRS, this.getSizedSawdust(6), RecipeLevel.PERIPHERAL);
        this.addRecipe(Blocks.ACACIA_STAIRS, this.getSizedSawdust(6), RecipeLevel.PERIPHERAL);
        this.addRecipe(Blocks.DARK_OAK_STAIRS, this.getSizedSawdust(6), RecipeLevel.PERIPHERAL);
        this.addRecipe(Blocks.CHEST, this.getSizedSawdust(32), RecipeLevel.PERIPHERAL);
        this.addRecipe(Blocks.CRAFTING_TABLE, this.getSizedSawdust(16), RecipeLevel.PERIPHERAL);
        this.addRecipe(Blocks.LADDER, this.getSizedSawdust(4), RecipeLevel.PERIPHERAL);
        this.addBlockTagRecipe(BlockTags.WOODEN_PRESSURE_PLATES, this.getSizedSawdust(8), RecipeLevel.PERIPHERAL);
        this.addRecipe(Blocks.STONE_PRESSURE_PLATE, new ItemStack(Blocks.COBBLESTONE, 2), RecipeLevel.PERIPHERAL);
        this.addRecipe(Items.BOWL, this.getSizedSawdust(ModList.GREGTECH.isLoaded() ? 4 : 12), RecipeLevel.PERIPHERAL);
        this.addBlockTagRecipe(BlockTags.WOODEN_DOORS, this.getSizedSawdust(24), RecipeLevel.PERIPHERAL);
        this.addBlockTagRecipe(BlockTags.SIGNS, this.getSizedSawdust(24), RecipeLevel.PERIPHERAL);
        this.addRecipe(Items.STICK, this.getSizedSawdust(2), RecipeLevel.PERIPHERAL);
        this.addBlockTagRecipe(BlockTags.WOODEN_TRAPDOORS, this.getSizedSawdust(24), RecipeLevel.PERIPHERAL);
        this.addBlockTagRecipe(BlockTags.WOODEN_FENCES, this.getSizedSawdust(16), RecipeLevel.PERIPHERAL);

        this.addFlowerDyes();

        this.addRecipe(Items.COAL, RotaryItems.COAL_DUST.get().getDefaultInstance(), RecipeLevel.CORE);

        this.addRecipe(RotaryItems.CANOLA_SEEDS.get(), RotaryItems.CANOLA_HUSKS.get().getDefaultInstance(), RecipeLevel.CORE);

        this.addBlockTagRecipe(BlockTags.PLANKS, this.getSizedSawdust(4), RecipeLevel.PERIPHERAL);
        this.addBlockTagRecipe(BlockTags.LOGS, this.getSizedSawdust(16), RecipeLevel.PERIPHERAL);
    }

    private void addFlowerDyes() {
        Object[][] recipes = {
                {Blocks.DANDELION, Items.YELLOW_DYE},
                {Blocks.POPPY, Items.RED_DYE},
//                todo {Blocks.RED_FLOWER, Items.LIGHT_BLUE_DYE},
//                todo {Blocks.RED_FLOWER, Items.MAGENTA_DYE},
//                todo {Blocks.RED_FLOWER, Items.LIGHT_GRAY_DYE},
//                todo {Blocks.RED_FLOWER, Items.RED_DYE},
//                todo {Blocks.RED_FLOWER, Items.ORANGE_DYE},
//                todo {Blocks.RED_FLOWER, Items.LIGHT_GRAY_DYE},
//                todo {Blocks.RED_FLOWER, Items.PINK_DYE},
//                todo {Blocks.RED_FLOWER, Items.LIGHT_GRAY_DYE},
//                todo {Blocks.DOUBLE_PLANT, Items.MAGENTA_DYE},
//                todo {Blocks.DOUBLE_PLANT, Items.RED_DYE},
//                todo {Blocks.DOUBLE_PLANT, Items.PINK_DYE},
                {Blocks.CORNFLOWER, Items.BLUE_DYE},
        };

        for (Object[] r : recipes) {
            ItemStack is = new ItemStack((Block) r[0], 1);
            DyeItem out = (DyeItem) r[1];
            this.addRecipe(is, ReikaItemHelper.getSizedItemStack(out.getDefaultInstance(), 6));
        }
    }

    private static class GrinderRecipe implements MachineRecipe {

        private final ItemStack input;
        private final ItemStack output;

        private GrinderRecipe(ItemStack in, ItemStack out1) {
            input = in;
            if (in.isEmpty())
                throw new MisuseException("You cannot grind an empty item!");
            output = out1;
            if (out1.isEmpty())
                throw new MisuseException("You cannot grind to an empty item!");
        }

        public ItemStack getOutput() {
            return output.copy();
        }

        public boolean makesItem(ItemStack is) {
            return ReikaItemHelper.matchStacks(is, output);
        }

        @Override
        public String getUniqueID() {
            return fullID(input) + ">" + fullID(output);
        }

        @Override
        public String getAllInfo() {
            return "Grinding " + fullID(input) + " into " + fullID(output);
        }

        @Override
        public Collection<ItemStack> getAllUsedItems() {
            return ReikaJavaLibrary.makeListFrom(input, output);
        }

    }

    private ItemStack getSizedSawdust(int size) {
        return ReikaItemHelper.getSizedItemStack(RotaryItems.SAWDUST.get().getDefaultInstance(), size);
    }

    public boolean isGrindable(ItemStack item) {
        return this.getGrindingResult(item) != null;
    }

    public boolean isProduct(ItemStack item) {
        for (GrinderRecipe gr : recipes.values()) {
            if (gr.makesItem(item))
                return true;
        }
        return false;
    }

    public List<ItemStack> getSources(ItemStack out) {
        List<ItemStack> in = new ArrayList();
        for (ItemStack input : recipes.keySet()) {
            GrinderRecipe gr = recipes.get(input);
            if (gr.makesItem(out))
                in.add(input.copy());
        }
        return in;
    }

    public void addItemTagRecipe(TagKey<Item> in, ItemStack out, RecipeLevel rl) {
        List<ItemStack> li = ForgeRegistries.ITEMS.tags()
                .getTag(in)
                .stream()
                .map(ItemStack::new)
                .toList();
        for (ItemStack sin : li) {
            if (!recipes.containsKey(sin))
                this.addRecipe(sin, out, rl);
        }
    }

    public void addBlockTagRecipe(TagKey<Block> in, ItemStack out, RecipeLevel rl) {
        List<ItemStack> li = ForgeRegistries.BLOCKS.tags()
                .getTag(Tags.Blocks.FENCE_GATES_WOODEN)
                .stream()
                .map(ItemStack::new)
                .toList();

    }

    public void addAPIRecipe(ItemStack in, ItemStack out) {
        this.addRecipe(in, out, RecipeLevel.API);
    }

    private void addRecipe(Block in, ItemStack out, RecipeLevel rl) {
        this.addRecipe(new ItemStack(in), out, rl);
    }

    private void addRecipe(Item in, ItemStack out, RecipeLevel rl) {
        this.addRecipe(new ItemStack(in), out, rl);
    }

    public void addRecipe(ItemStack in, ItemStack out) {
        this.addRecipe(in, out, RecipeLevel.CORE);
    }

    private void addRecipe(ItemStack in, ItemStack out, RecipeLevel rl) {
        GrinderRecipe rec = new GrinderRecipe(in, out);
        recipes.put(in, rec);
        this.onAddRecipe(rec, rl);
    }

    public void addCustomRecipe(ItemStack in, ItemStack out) {
        this.addRecipe(in, out, RecipeLevel.CUSTOM);
    }

    public ItemStack getGrindingResult(ItemStack item) {
        GrinderRecipe ret = item != null && item.getItem() != null ? recipes.get(item) : null;
        return ret != null ? ret.output.copy() : null;
    }

    @Override
    public void addPostLoadRecipes() {
        this.addOreRecipes();
        //this.addMulchRecipes();

/*		if (ModList.APPENG.isLoaded()) {
			ItemStack cry = AppEngHandler.getInstance().getCertusQuartz();
			ItemStack dust = AppEngHandler.getInstance().getCertusQuartzDust();
			if (!cry.isEmpty() && !dust.isEmpty()) {
				this.addRecipe(cry, dust, RecipeLevel.MODINTERACT);
			}
			else {
				RotaryCraft.LOGGER.error("Could not add certus quartz grinding; null itemstack "+cry+", "+dust);
			}

			ItemStack fluix = AppEngHandler.getInstance().getFluixCrystal();
			ItemStack fluixdust = AppEngHandler.getInstance().getFluixDust();
			if (!fluix.isEmpty() && !fluixdust.isEmpty()) {
				this.addRecipe(fluix, fluixdust, RecipeLevel.MODINTERACT);
			}
			else {
				RotaryCraft.LOGGER.error("Could not add certus quartz grinding; null itemstack "+fluix+", "+fluixdust);
			}
		}*/

//      todo  ArrayList<ItemStack> obsididust = OreDictionary.getOres("dustObsidian");
//        if (!obsididust.isEmpty())
//            this.addRecipe(Blocks.OBSIDIAN, ReikaItemHelper.getSizedItemStack(obsididust.get(0), 6), RecipeLevel.MODINTERACT);

//todo        this.addDualItemTagRecipe("rodBlizz", "dustBlizz", 6, RecipeLevel.MODINTERACT);
//todo        this.addDualItemTagRecipe("rodBlitz", "dustBlitz", 6, RecipeLevel.MODINTERACT);
//todo        this.addDualItemTagRecipe("rodBasalz", "dustBasalz", 6, RecipeLevel.MODINTERACT);

        this.addRecipe(Blocks.NETHERRACK, RotaryItems.NETHERRACK_DUST.get().getDefaultInstance(), RecipeLevel.CORE);
        this.addRecipe(Blocks.SOUL_SAND, RotaryItems.TAR.get().getDefaultInstance(), RecipeLevel.CORE);
        this.addRecipe(Blocks.SOUL_SOIL, RotaryItems.TAR.get().getDefaultInstance(), RecipeLevel.CORE);

	/*	if (ModList.BOTANIA.isLoaded()) {
			Item petal = ForgeRegistries.ITEMS.getValue(new ResourceLocation(ModList.BOTANIA.modid, "petal"));
			Item dye = ForgeRegistries.ITEMS.getValue(new ResourceLocation(ModList.BOTANIA.modid, "dye"));
			Block flower = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(ModList.BOTANIA.modid, "flower"));
			Block tallflower1 = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(ModList.BOTANIA.modid, "doubleFlower1"));
			Block tallflower2 = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(ModList.BOTANIA.modid, "doubleFlower2"));
			for (int i = 0; i < 16; i++) {
				Block tall = i >= 8 ? tallflower2 : tallflower1;
				int tallm = i%8;
				this.addRecipe(new ItemStack(flower, 1, i), new ItemStack(petal, 6, i), RecipeLevel.MODINTERACT);
				this.addRecipe(new ItemStack(tall, 1, tallm), new ItemStack(petal, 12, i), RecipeLevel.MODINTERACT);
				this.addRecipe(new ItemStack(petal, 1, i), new ItemStack(dye, 3, i), RecipeLevel.MODINTERACT);
			}
		}*/

        ItemStack dust = ReikaItemHelper.lookupItem("exnihilo:dust");
        if (!dust.isEmpty()) {
            this.addRecipe(Blocks.SAND, dust, RecipeLevel.MODINTERACT);
        }

        ItemStack endDust = ReikaItemHelper.lookupItem("exnihilo:exnihilo.gravel_ender");
        if (!endDust.isEmpty()) {
            this.addRecipe(Blocks.END_STONE, endDust, RecipeLevel.MODINTERACT);
        }

//		if (ReikaItemHelper.oreItemExists("dustEnderPearl"))
//			this.addRecipe(Items.ENDER_PEARL, OreDictionary.getOres("dustEnderPearl").get(0), RecipeLevel.MODINTERACT);

        if (ModList.HARVESTCRAFT.isLoaded()) {
            ItemStack corn = ReikaItemHelper.lookupItem("harvestcraft:cornItem");
            ItemStack fla = ReikaItemHelper.lookupItem("harvestcraft:cornflakesItem");
            if (!corn.isEmpty() && !fla.isEmpty())
                this.addRecipe(corn, ReikaItemHelper.getSizedItemStack(fla, 3), RecipeLevel.MODINTERACT);
        }

/*		if (ModList.HEXCRAFT.isLoaded()) {
			for (BasicHexColor c : HexcraftHandler.getActiveHandler().getColors()) {
				Block in = c.getMonolith(false);
				Block in2 = c.getMonolith(true);
				Item i = c.getCrystal();
				this.addRecipe(in, new ItemStack(i, 9), RecipeLevel.MODINTERACT);
				this.addRecipe(in2, new ItemStack(i, 12), RecipeLevel.MODINTERACT);
			}
		}*/

//        todo this.addBlockTagRecipe("cropCinderpearl", new ItemStack(Items.BLAZE_POWDER, 3), RecipeLevel.MODINTERACT);
//      todo  this.addTagRecipe("cropShimmerleaf", ReikaItemHelper.getSizedItemStack(ExtractorModOres.getSmeltedIngot(ModOreList.CINNABAR), 3), RecipeLevel.MODINTERACT);
    }
	/*
	private void addMulchRecipes() {
		Collection<ItemStack> mulches = MulchMaterials.instance.getAllValidPlants();
		for (ItemStack is : mulches) {
			if (is.getItem() != Items.reeds) {
				int num = MulchMaterials.instance.getPlantValue(is);
				this.addRecipe(is, ReikaItemHelper.getSizedItemStack(RotaryItems.mulch, num));
				RotaryCraft.LOGGER.info("Adding grinder mulching recipe for "+is+", makes "+num);
			}
		}
	}
	 */


    private void addOreRecipes() {
        enum Ores {
            COAL("Coal", Blocks.COAL_ORE, Items.COAL.getDefaultInstance(), 1, BlockTags.COAL_ORES, Tags.Items.ORES_COAL, OreType.OreRarity.COMMON), //todo item tag for coal? missing idfk
            IRON("Iron", Blocks.IRON_ORE, Items.IRON_INGOT.getDefaultInstance(), 1, BlockTags.IRON_ORES, Tags.Items.INGOTS_IRON, OreType.OreRarity.AVERAGE),
            GOLD("Gold", Blocks.GOLD_ORE, Items.GOLD_INGOT.getDefaultInstance(), 1, BlockTags.GOLD_ORES, Tags.Items.INGOTS_GOLD, OreType.OreRarity.SCATTERED),
            REDSTONE("Redstone", Blocks.REDSTONE_ORE, Items.REDSTONE.getDefaultInstance(), 1, BlockTags.REDSTONE_ORES, Tags.Items.DUSTS_REDSTONE, OreType.OreRarity.COMMON),
            LAPIS("Lapis Lazuli", Blocks.LAPIS_ORE, Items.LAPIS_LAZULI.getDefaultInstance(), 1, BlockTags.LAPIS_ORES, Tags.Items.GEMS_LAPIS, OreType.OreRarity.SCARCE),
            DIAMOND("Diamond", Blocks.DIAMOND_ORE, Items.DIAMOND.getDefaultInstance(), 1, BlockTags.DIAMOND_ORES, Tags.Items.GEMS_DIAMOND, OreType.OreRarity.SCARCE),
            EMERALD("Emerald", Blocks.EMERALD_ORE, Items.EMERALD.getDefaultInstance(), 1, BlockTags.EMERALD_ORES, Tags.Items.GEMS_EMERALD, OreType.OreRarity.RARE),
            QUARTZ("Nether Quartz", Blocks.NETHER_QUARTZ_ORE, Items.QUARTZ.getDefaultInstance(), 1, Tags.Blocks.ORES_QUARTZ, Tags.Items.GEMS_QUARTZ, OreType.OreRarity.EVERYWHERE);

            private String name;
            private ItemStack drop;
            private Block ore;
            private TagKey<Block> oreDict;
            private TagKey<Item> dropOreDict;
            public final OreType.OreRarity rarity;
            public final int blockDrops;
            public static final Ores[] oreList = Ores.values();

            Ores(String n, Block b, ItemStack is, int nd, TagKey<Block> d, TagKey<Item> d2, OreType.OreRarity r) {
                name = n;
                ore = b;
                drop = is.copy();
                blockDrops = nd;
                oreDict = d;
                dropOreDict = d2;
                rarity = r;
            }
        }

      /* todo ore flakes
           for (int i = 0; i < 9; i++) {
            Ores ore = Ores.oreList[i];
            ItemStack is = ore.drop;
            if (recipes.containsKey(is)) {
                Ores mod = Ores.oreList[recipes.get(is).output.getItem().getItemDamage()];
                RotaryCraft.LOGGER.info("Ore " + is.getDisplayName() + " is being skipped for grinder registration as " + ore + " as it is already registered to " + mod);
            } else {
                ItemStack flake = RotaryItems.EXTRACTS.getCraftedMetadataProduct(ore_rate, 24 + ore.ordinal());
                this.addRecipe(is, ReikaItemHelper.getSizedItemStack(flake, ore_rate), RecipeLevel.CORE);
                int n = ore_rate;
                if (ore.rarity == OreType.OreRarity.RARE)
                    n *= 3;
                RotaryCraft.LOGGER.info("Adding " + n + "x grinder recipe for " + ore + " ore " + is);
            }
    }*/

        for (int i = 0; i < ModOreList.oreList.length; i++) {
            ModOreList ore = ModOreList.oreList[i];
            Collection<ItemStack> li = ore.getAllOreBlocks();
            int n = ore_rate;
            if (ore.isNetherOres())
                n *= 2;
            else if (ore.getRarity() == OreType.OreRarity.RARE)
                n *= 3;
            for (ItemStack is : li) {
             /*  todo modded ore extracts
                   if (recipes.containsKey(is)) {
                    OreType mod = ExtractorModOres.getOreFromExtract(recipes.get(is).output);
                    RotaryCraft.LOGGER.info("Ore " + is.getDisplayName() + " is being skipped for grinder registration as " + ore + " as it is already registered to " + mod);
                } else {
                    ItemStack flake = ExtractorModOres.getFlakeProduct(ore);
                    this.addRecipe(is, ReikaItemHelper.getSizedItemStack(flake, n), RecipeLevel.CORE);
                    RotaryCraft.LOGGER.info("Adding " + (n) + "x grinder recipe for " + ore + " ore " + is);
                }*/
            }
        }

		/*if (ModList.GREGTECH.isLoaded()) {
			this.loadGTOres();
		}*/
    }


/*	@ModDependent(ModList.GREGTECH)
	private void loadGTOres() {
		for (CounterpartOres ore : CounterpartOres.oreList) {
			for (ItemStack is : ore.getAllOreBlocks()) {
				int n = Math.max(1, (int)Math.round(ore_rate*ore.yieldFraction));
				OreType base = ore.counterpart;
				ItemStack flake = base instanceof ReikaOreHelper ? RotaryItems.EXTRACTS.getCraftedMetadataProduct(n, 24+base.ordinal()) : ExtractorModOres.getFlakeProduct((ModOreList)base);
				flake.stackSize = n;
				this.addRecipe(is, ReikaItemHelper.getSizedItemStack(flake, n), RecipeLevel.CORE);
				RotaryCraft.LOGGER.info("Adding "+(n)+"x grinder recipe for GT ore "+ore);
			}
		}
	}*/

    public Collection<ItemStack> getAllGrindables() {
        return recipes.keySet();
    }

    @Override
    protected boolean removeRecipe(MachineRecipe recipe) {
        return recipes.removeValue((GrinderRecipe) recipe);
    }

    @Override
    protected boolean addCustomRecipe(String n, LuaBlock lb, CustomRecipeList crl) throws Exception {
        ItemStack out = crl.parseItemString(lb.getString("output"), lb.getChild("output_nbt"), false);
        this.verifyOutputItem(out);
        String ore = lb.containsKey("ore_input") ? lb.getString("ore_input") : null;
       /* if (ore != null && !ore.isEmpty() && !ore.startsWith("[NULL")) {
            Collection<ItemStack> c = OreDictionary.getOres(ore);
            if (c.isEmpty()) {
                throw new IllegalArgumentException("Ore tag '" + ore + "' does not map to any existing OreDict tag!");
            }
            for (ItemStack in : c) {
                this.addCustomRecipe(in, out);
            }
            return true;
        }*/
        ItemStack in = crl.parseItemString(lb.getString("input"), lb.getChild("input_nbt"), false);
        this.addCustomRecipe(in, out);
        return true;
    }
}
