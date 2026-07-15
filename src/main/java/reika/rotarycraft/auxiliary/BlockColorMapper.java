/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.auxiliary;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;

import reika.dragonapi.libraries.rendering.ReikaColorAPI;
import reika.rotarycraft.RotaryCraft;

/**
 * Maps a block to the flat color the GPR paints for it on its cross-section readout. Ported
 * faithfully from the 1.7.10 table (RGB values unchanged); metadata-variant blocks that were a
 * single block+meta in 1.7.10 (wool, flowers, slabs, stained glass, logs) are now distinct blocks
 * and are each mapped individually.
 * <p>
 * Two intent-faithful additions over the original: the modern deep-underground palette (deepslate,
 * the deepslate ore variants, tuff, copper) which did not exist in 1.7.10 but is exactly the layer
 * the GPR scans through (96 blocks straight down). Without them the entire useful ore band would
 * render as the UNKNOWN color.
 * <p>
 * MOD-PORT: the cross-mod integrations (ModCropList / ModWoodList / ModOreList colouring, the
 * {@code BlockColorInterface} API, and the {@code RotaryCraft_CustomGPRColors.cfg} loader) are not
 * ported — those DragonAPI mod-registry classes and the RotaryCraft color API are not in this build.
 * They were purely additive tinting for other mods' blocks and do not affect the vanilla/RC readout.
 */
public class BlockColorMapper {

    public static final BlockColorMapper instance = new BlockColorMapper();

    public static final int UNKNOWN_COLOR = 0xD47EFF;
    public static final int AIR_COLOR = ReikaColorAPI.GStoHex(33);

    private final Map<Block, Integer> map = new HashMap<>();
    private final Map<Block, Block> mimics = new HashMap<>();

    private BlockColorMapper() {
        this.addBlockColor(Blocks.STONE, ReikaColorAPI.RGBtoHex(126, 126, 126));
        this.addBlockColor(Blocks.GRASS_BLOCK, ReikaColorAPI.RGBtoHex(104, 167, 65));
        this.addBlockColor(Blocks.DIRT, ReikaColorAPI.RGBtoHex(120, 85, 60));
        this.addBlockColor(Blocks.COARSE_DIRT, ReikaColorAPI.RGBtoHex(120, 85, 60));
        this.addBlockColor(Blocks.ROOTED_DIRT, ReikaColorAPI.RGBtoHex(120, 85, 60));
        this.addBlockColor(Blocks.PODZOL, ReikaColorAPI.RGBtoHex(97, 82, 104));
        this.addBlockColor(Blocks.COBBLESTONE, ReikaColorAPI.RGBtoHex(99, 99, 99));
        this.addBlockColor(Blocks.BEDROCK, ReikaColorAPI.RGBtoHex(50, 50, 50));
        this.addBlockColor(Blocks.WATER, ReikaColorAPI.RGBtoHex(0, 0, 255));
        this.addBlockColor(Blocks.LAVA, ReikaColorAPI.RGBtoHex(255, 40, 0));
        this.addBlockColor(Blocks.SAND, ReikaColorAPI.RGBtoHex(225, 219, 163));
        this.addBlockColor(Blocks.RED_SAND, ReikaColorAPI.RGBtoHex(190, 102, 33));
        this.addBlockColor(Blocks.GRAVEL, ReikaColorAPI.RGBtoHex(159, 137, 131));

        // Ores (surface / stone-hosted)
        this.addBlockColor(Blocks.GOLD_ORE, ReikaColorAPI.RGBtoHex(251, 237, 76));
        this.addBlockColor(Blocks.IRON_ORE, ReikaColorAPI.RGBtoHex(214, 173, 145));
        this.addBlockColor(Blocks.COAL_ORE, ReikaColorAPI.RGBtoHex(70, 70, 70));
        this.addBlockColor(Blocks.LAPIS_ORE, ReikaColorAPI.RGBtoHex(40, 98, 175));
        this.addBlockColor(Blocks.REDSTONE_ORE, ReikaColorAPI.RGBtoHex(215, 0, 0));
        this.addBlockColor(Blocks.DIAMOND_ORE, ReikaColorAPI.RGBtoHex(93, 235, 244));
        this.addBlockColor(Blocks.EMERALD_ORE, ReikaColorAPI.RGBtoHex(23, 221, 98));
        this.addBlockColor(Blocks.NETHER_QUARTZ_ORE, ReikaColorAPI.RGBtoHex(203, 191, 177));
        this.addBlockColor(Blocks.COPPER_ORE, ReikaColorAPI.RGBtoHex(224, 121, 79));
        this.addBlockColor(Blocks.NETHER_GOLD_ORE, ReikaColorAPI.RGBtoHex(251, 237, 76));

        this.addBlockColor(Blocks.OAK_LOG, ReikaColorAPI.RGBtoHex(103, 83, 53));
        this.addBlockColor(Blocks.SPRUCE_LOG, ReikaColorAPI.RGBtoHex(103, 83, 53));
        this.addBlockColor(Blocks.BIRCH_LOG, ReikaColorAPI.RGBtoHex(213, 201, 139));
        this.addBlockColor(Blocks.JUNGLE_LOG, ReikaColorAPI.RGBtoHex(103, 83, 53));
        this.addBlockColor(Blocks.ACACIA_LOG, ReikaColorAPI.RGBtoHex(103, 83, 53));
        this.addBlockColor(Blocks.DARK_OAK_LOG, ReikaColorAPI.RGBtoHex(103, 83, 53));
        this.addBlockColor(Blocks.OAK_LEAVES, ReikaColorAPI.RGBtoHex(87, 171, 65));
        this.addBlockMimic(Blocks.SPRUCE_LEAVES, Blocks.OAK_LEAVES);
        this.addBlockMimic(Blocks.BIRCH_LEAVES, Blocks.OAK_LEAVES);
        this.addBlockMimic(Blocks.JUNGLE_LEAVES, Blocks.OAK_LEAVES);
        this.addBlockMimic(Blocks.ACACIA_LEAVES, Blocks.OAK_LEAVES);
        this.addBlockMimic(Blocks.DARK_OAK_LEAVES, Blocks.OAK_LEAVES);
        this.addBlockColor(Blocks.OAK_PLANKS, ReikaColorAPI.RGBtoHex(178, 142, 90));
        this.addBlockMimic(Blocks.SPRUCE_PLANKS, Blocks.OAK_PLANKS);
        this.addBlockMimic(Blocks.BIRCH_PLANKS, Blocks.OAK_PLANKS);
        this.addBlockMimic(Blocks.JUNGLE_PLANKS, Blocks.OAK_PLANKS);
        this.addBlockMimic(Blocks.ACACIA_PLANKS, Blocks.OAK_PLANKS);
        this.addBlockMimic(Blocks.DARK_OAK_PLANKS, Blocks.OAK_PLANKS);
        this.addBlockColor(Blocks.OAK_SAPLING, ReikaColorAPI.RGBtoHex(0, 255, 0));

        this.addBlockColor(Blocks.SPONGE, ReikaColorAPI.RGBtoHex(204, 204, 71));
        this.addBlockColor(Blocks.WET_SPONGE, ReikaColorAPI.RGBtoHex(204, 204, 71));
        this.addBlockColor(Blocks.GLASS, ReikaColorAPI.RGBtoHex(190, 244, 254));
        this.addBlockColor(Blocks.LAPIS_BLOCK, ReikaColorAPI.RGBtoHex(21, 52, 188));
        this.addBlockColor(Blocks.DISPENSER, ReikaColorAPI.RGBtoHex(119, 119, 119));
        this.addBlockMimic(Blocks.DROPPER, Blocks.DISPENSER);
        this.addBlockColor(Blocks.SANDSTONE, ReikaColorAPI.RGBtoHex(212, 205, 153));
        this.addBlockColor(Blocks.NOTE_BLOCK, ReikaColorAPI.RGBtoHex(147, 90, 64));
        this.addBlockColor(Blocks.POWERED_RAIL, ReikaColorAPI.RGBtoHex(220, 182, 47));
        this.addBlockColor(Blocks.DETECTOR_RAIL, ReikaColorAPI.RGBtoHex(134, 0, 0));
        this.addBlockColor(Blocks.STICKY_PISTON, ReikaColorAPI.RGBtoHex(122, 190, 111));
        this.addBlockColor(Blocks.COBWEB, ReikaColorAPI.RGBtoHex(220, 220, 220));
        this.addBlockColor(Blocks.SHORT_GRASS, ReikaColorAPI.RGBtoHex(104, 167, 65));
        this.addBlockColor(Blocks.FERN, ReikaColorAPI.RGBtoHex(104, 167, 65));
        this.addBlockColor(Blocks.DEAD_BUSH, ReikaColorAPI.RGBtoHex(146, 99, 44));
        this.addBlockColor(Blocks.PISTON, ReikaColorAPI.RGBtoHex(178, 142, 90));
        this.addBlockColor(Blocks.PISTON_HEAD, UNKNOWN_COLOR);
        this.addBlockColor(Blocks.MOVING_PISTON, UNKNOWN_COLOR);

        this.addFlowers();

        this.addBlockColor(Blocks.BROWN_MUSHROOM, ReikaColorAPI.RGBtoHex(202, 151, 119));
        this.addBlockColor(Blocks.RED_MUSHROOM, ReikaColorAPI.RGBtoHex(225, 24, 25));
        this.addBlockColor(Blocks.GOLD_BLOCK, ReikaColorAPI.RGBtoHex(255, 240, 69));
        this.addBlockColor(Blocks.IRON_BLOCK, ReikaColorAPI.RGBtoHex(232, 232, 232));

        this.addSlabs();

        this.addBlockColor(Blocks.BRICKS, ReikaColorAPI.RGBtoHex(175, 91, 72));
        this.addBlockColor(Blocks.TNT, ReikaColorAPI.RGBtoHex(216, 58, 19));
        this.addBlockColor(Blocks.BOOKSHELF, ReikaColorAPI.RGBtoHex(186, 150, 98));
        this.addBlockColor(Blocks.MOSSY_COBBLESTONE, ReikaColorAPI.RGBtoHex(69, 143, 69));
        this.addBlockColor(Blocks.OBSIDIAN, ReikaColorAPI.RGBtoHex(62, 51, 86));
        this.addBlockColor(Blocks.CRYING_OBSIDIAN, ReikaColorAPI.RGBtoHex(62, 51, 86));
        this.addBlockColor(Blocks.TORCH, ReikaColorAPI.RGBtoHex(255, 214, 0));
        this.addBlockColor(Blocks.WALL_TORCH, ReikaColorAPI.RGBtoHex(255, 214, 0));
        this.addBlockColor(Blocks.FIRE, ReikaColorAPI.RGBtoHex(255, 170, 0));
        this.addBlockColor(Blocks.SPAWNER, ReikaColorAPI.RGBtoHex(39, 64, 81));
        this.addBlockMimic(Blocks.OAK_STAIRS, Blocks.OAK_PLANKS);
        this.addBlockMimic(Blocks.CHEST, Blocks.OAK_PLANKS);
        this.addBlockColor(Blocks.REDSTONE_WIRE, ReikaColorAPI.RGBtoHex(145, 0, 16));
        this.addBlockColor(Blocks.DIAMOND_BLOCK, ReikaColorAPI.RGBtoHex(104, 222, 217));
        this.addBlockMimic(Blocks.CRAFTING_TABLE, Blocks.OAK_PLANKS);
        this.addBlockColor(Blocks.WHEAT, ReikaColorAPI.RGBtoHex(4, 189, 18));
        this.addBlockColor(Blocks.FARMLAND, ReikaColorAPI.RGBtoHex(96, 55, 27));
        this.addBlockColor(Blocks.FURNACE, ReikaColorAPI.RGBtoHex(119, 119, 119));
        this.addBlockMimic(Blocks.OAK_SIGN, Blocks.OAK_PLANKS);
        this.addBlockMimic(Blocks.OAK_WALL_SIGN, Blocks.OAK_PLANKS);
        this.addBlockMimic(Blocks.OAK_DOOR, Blocks.OAK_PLANKS);
        this.addBlockColor(Blocks.LADDER, ReikaColorAPI.RGBtoHex(170, 134, 82));
        this.addBlockColor(Blocks.RAIL, ReikaColorAPI.RGBtoHex(170, 134, 82));
        this.addBlockMimic(Blocks.COBBLESTONE_STAIRS, Blocks.COBBLESTONE);
        this.addBlockColor(Blocks.LEVER, ReikaColorAPI.RGBtoHex(123, 98, 64));
        this.addBlockMimic(Blocks.STONE_PRESSURE_PLATE, Blocks.STONE);
        this.addBlockColor(Blocks.IRON_DOOR, ReikaColorAPI.RGBtoHex(222, 222, 222));
        this.addBlockMimic(Blocks.OAK_PRESSURE_PLATE, Blocks.OAK_PLANKS);
        this.addBlockColor(Blocks.REDSTONE_TORCH, ReikaColorAPI.RGBtoHex(173, 0, 0));
        this.addBlockColor(Blocks.REDSTONE_WALL_TORCH, ReikaColorAPI.RGBtoHex(173, 0, 0));
        this.addBlockMimic(Blocks.STONE_BUTTON, Blocks.STONE);
        this.addBlockMimic(Blocks.SNOW, Blocks.SNOW_BLOCK);
        this.addBlockColor(Blocks.ICE, ReikaColorAPI.RGBtoHex(117, 166, 255));
        this.addBlockColor(Blocks.PACKED_ICE, ReikaColorAPI.RGBtoHex(165, 195, 247));
        this.addBlockColor(Blocks.BLUE_ICE, ReikaColorAPI.RGBtoHex(117, 166, 255));
        this.addBlockColor(Blocks.SNOW_BLOCK, ReikaColorAPI.RGBtoHex(255, 255, 255));
        this.addBlockColor(Blocks.CACTUS, ReikaColorAPI.RGBtoHex(24, 126, 37));
        this.addBlockColor(Blocks.CLAY, ReikaColorAPI.RGBtoHex(171, 175, 191));
        this.addBlockColor(Blocks.SUGAR_CANE, ReikaColorAPI.RGBtoHex(168, 217, 115));
        this.addBlockColor(Blocks.JUKEBOX, ReikaColorAPI.RGBtoHex(147, 90, 64));
        this.addBlockMimic(Blocks.OAK_FENCE, Blocks.OAK_PLANKS);
        this.addBlockColor(Blocks.PUMPKIN, ReikaColorAPI.RGBtoHex(226, 142, 34));
        this.addBlockColor(Blocks.CARVED_PUMPKIN, ReikaColorAPI.RGBtoHex(226, 142, 34));
        this.addBlockColor(Blocks.NETHERRACK, ReikaColorAPI.RGBtoHex(163, 66, 66));
        this.addBlockColor(Blocks.SOUL_SAND, ReikaColorAPI.RGBtoHex(92, 74, 63));
        this.addBlockColor(Blocks.SOUL_SOIL, ReikaColorAPI.RGBtoHex(92, 74, 63));
        this.addBlockColor(Blocks.GLOWSTONE, ReikaColorAPI.RGBtoHex(248, 210, 154));
        this.addBlockColor(Blocks.NETHER_PORTAL, ReikaColorAPI.RGBtoHex(128, 0, 255));
        this.addBlockColor(Blocks.JACK_O_LANTERN, ReikaColorAPI.RGBtoHex(226, 142, 34));
        this.addBlockColor(Blocks.CAKE, ReikaColorAPI.RGBtoHex(165, 83, 37));
        this.addBlockColor(Blocks.REPEATER, ReikaColorAPI.RGBtoHex(145, 32, 48));

        this.addBlockColor(Blocks.OAK_TRAPDOOR, ReikaColorAPI.RGBtoHex(141, 106, 55));
        this.addBlockColor(Blocks.INFESTED_STONE, ReikaColorAPI.RGBtoHex(156, 156, 156));
        this.addBlockColor(Blocks.STONE_BRICKS, ReikaColorAPI.RGBtoHex(135, 135, 135));
        this.addBlockColor(Blocks.MOSSY_STONE_BRICKS, ReikaColorAPI.RGBtoHex(122, 135, 122));
        this.addBlockColor(Blocks.CRACKED_STONE_BRICKS, ReikaColorAPI.RGBtoHex(135, 135, 135));
        this.addBlockColor(Blocks.CHISELED_STONE_BRICKS, ReikaColorAPI.RGBtoHex(135, 135, 135));
        this.addBlockColor(Blocks.BROWN_MUSHROOM_BLOCK, ReikaColorAPI.RGBtoHex(148, 113, 90));
        this.addBlockColor(Blocks.RED_MUSHROOM_BLOCK, ReikaColorAPI.RGBtoHex(179, 34, 32));
        this.addBlockColor(Blocks.MUSHROOM_STEM, ReikaColorAPI.RGBtoHex(202, 196, 187));
        this.addBlockColor(Blocks.IRON_BARS, ReikaColorAPI.RGBtoHex(106, 104, 106));
        this.addBlockMimic(Blocks.GLASS_PANE, Blocks.GLASS);
        this.addBlockColor(Blocks.MELON, ReikaColorAPI.RGBtoHex(175, 173, 43));
        this.addBlockColor(Blocks.PUMPKIN_STEM, ReikaColorAPI.RGBtoHex(192, 128, 140));
        this.addBlockMimic(Blocks.MELON_STEM, Blocks.PUMPKIN_STEM);
        this.addBlockColor(Blocks.VINE, ReikaColorAPI.RGBtoHex(26, 139, 40));
        this.addBlockMimic(Blocks.OAK_FENCE_GATE, Blocks.OAK_FENCE);
        this.addBlockMimic(Blocks.BRICK_STAIRS, Blocks.BRICKS);
        this.addBlockMimic(Blocks.STONE_BRICK_STAIRS, Blocks.STONE_BRICKS);
        this.addBlockColor(Blocks.MYCELIUM, ReikaColorAPI.RGBtoHex(97, 82, 104));
        this.addBlockColor(Blocks.LILY_PAD, ReikaColorAPI.RGBtoHex(30, 53, 15));
        this.addBlockColor(Blocks.NETHER_BRICKS, ReikaColorAPI.RGBtoHex(73, 39, 46));
        this.addBlockMimic(Blocks.NETHER_BRICK_FENCE, Blocks.NETHER_BRICKS);
        this.addBlockMimic(Blocks.NETHER_BRICK_STAIRS, Blocks.NETHER_BRICKS);
        this.addBlockColor(Blocks.NETHER_WART, ReikaColorAPI.RGBtoHex(159, 41, 45));
        this.addBlockColor(Blocks.ENCHANTING_TABLE, ReikaColorAPI.RGBtoHex(160, 46, 45));
        this.addBlockColor(Blocks.BREWING_STAND, ReikaColorAPI.RGBtoHex(196, 186, 81));
        this.addBlockColor(Blocks.CAULDRON, ReikaColorAPI.RGBtoHex(59, 59, 59));
        this.addBlockColor(Blocks.END_PORTAL, ReikaColorAPI.RGBtoHex(0, 0, 0));
        this.addBlockColor(Blocks.END_PORTAL_FRAME, ReikaColorAPI.RGBtoHex(67, 114, 102));
        this.addBlockColor(Blocks.END_STONE, ReikaColorAPI.RGBtoHex(234, 247, 180));
        this.addBlockColor(Blocks.END_STONE_BRICKS, ReikaColorAPI.RGBtoHex(234, 247, 180));
        this.addBlockColor(Blocks.DRAGON_EGG, ReikaColorAPI.RGBtoHex(48, 5, 54));
        this.addBlockColor(Blocks.REDSTONE_LAMP, ReikaColorAPI.RGBtoHex(222, 147, 71));
        this.addBlockColor(Blocks.COCOA, ReikaColorAPI.RGBtoHex(177, 98, 28));
        this.addBlockMimic(Blocks.SANDSTONE_STAIRS, Blocks.SANDSTONE);
        this.addBlockColor(Blocks.EMERALD_BLOCK, ReikaColorAPI.RGBtoHex(63, 213, 102));
        this.addBlockColor(Blocks.ENDER_CHEST, ReikaColorAPI.RGBtoHex(43, 61, 63));
        this.addBlockMimic(Blocks.TRIPWIRE_HOOK, Blocks.OAK_PLANKS);
        this.addBlockColor(Blocks.TRIPWIRE, ReikaColorAPI.RGBtoHex(33, 33, 33)); // render tripwires as air
        this.addBlockColor(Blocks.SPRUCE_STAIRS, ReikaColorAPI.RGBtoHex(127, 94, 56));
        this.addBlockColor(Blocks.BIRCH_STAIRS, ReikaColorAPI.RGBtoHex(213, 201, 139));
        this.addBlockColor(Blocks.JUNGLE_STAIRS, ReikaColorAPI.RGBtoHex(182, 133, 99));
        this.addBlockColor(Blocks.COMMAND_BLOCK, ReikaColorAPI.RGBtoHex(199, 126, 79));
        this.addBlockColor(Blocks.BEACON, ReikaColorAPI.RGBtoHex(44, 197, 87));
        this.addBlockColor(Blocks.COBBLESTONE_WALL, ReikaColorAPI.RGBtoHex(99, 99, 99));
        this.addBlockColor(Blocks.FLOWER_POT, ReikaColorAPI.RGBtoHex(116, 63, 48));
        this.addBlockColor(Blocks.CARROTS, ReikaColorAPI.RGBtoHex(4, 189, 18));
        this.addBlockColor(Blocks.POTATOES, ReikaColorAPI.RGBtoHex(4, 189, 18));
        this.addBlockColor(Blocks.BEETROOTS, ReikaColorAPI.RGBtoHex(4, 189, 18));
        this.addBlockMimic(Blocks.OAK_BUTTON, Blocks.OAK_PLANKS);
        this.addBlockColor(Blocks.ANVIL, ReikaColorAPI.RGBtoHex(67, 67, 67));
        this.addBlockMimic(Blocks.TRAPPED_CHEST, Blocks.CHEST);
        this.addBlockMimic(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, Blocks.GOLD_BLOCK);
        this.addBlockMimic(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, Blocks.IRON_BLOCK);
        this.addBlockMimic(Blocks.COMPARATOR, Blocks.REPEATER);
        this.addBlockColor(Blocks.DAYLIGHT_DETECTOR, ReikaColorAPI.RGBtoHex(71, 61, 41));
        this.addBlockColor(Blocks.REDSTONE_BLOCK, ReikaColorAPI.RGBtoHex(255, 100, 0));
        this.addBlockColor(Blocks.HOPPER, ReikaColorAPI.RGBtoHex(75, 75, 75));
        this.addBlockColor(Blocks.QUARTZ_BLOCK, ReikaColorAPI.RGBtoHex(236, 232, 226));
        this.addBlockColor(Blocks.CHISELED_QUARTZ_BLOCK, ReikaColorAPI.RGBtoHex(236, 232, 226));
        this.addBlockColor(Blocks.QUARTZ_PILLAR, ReikaColorAPI.RGBtoHex(236, 232, 226));
        this.addBlockMimic(Blocks.QUARTZ_STAIRS, Blocks.QUARTZ_BLOCK);
        this.addBlockColor(Blocks.ACTIVATOR_RAIL, ReikaColorAPI.RGBtoHex(183, 12, 12));
        this.addBlockColor(Blocks.HAY_BLOCK, ReikaColorAPI.RGBtoHex(255, 209, 94));
        this.addBlockColor(Blocks.TERRACOTTA, ReikaColorAPI.RGBtoHex(158, 100, 73));
        this.addBlockColor(Blocks.COAL_BLOCK, ReikaColorAPI.RGBtoHex(21, 21, 21));

        this.addModernUnderground();
        this.addRotaryCraft();

        // MOD-PORT: addModOres() / addModWood() / addModCrops() / loadModData() and the config-file
        // custom-color loader are omitted — the DragonAPI ModOreList/ModWoodList/ModCropList
        // registries, the RotaryCraft BlockColorInterface API, and ReikaFileReader-based config
        // parsing are not part of this build. They only tinted other mods' blocks.
    }

    /** The modern deep-underground palette (did not exist in 1.7.10, but is where the GPR scans). */
    private void addModernUnderground() {
        this.addBlockColor(Blocks.DEEPSLATE, ReikaColorAPI.RGBtoHex(80, 80, 84));
        this.addBlockColor(Blocks.COBBLED_DEEPSLATE, ReikaColorAPI.RGBtoHex(77, 77, 80));
        this.addBlockColor(Blocks.TUFF, ReikaColorAPI.RGBtoHex(108, 109, 102));
        this.addBlockColor(Blocks.CALCITE, ReikaColorAPI.RGBtoHex(223, 224, 220));
        this.addBlockColor(Blocks.DRIPSTONE_BLOCK, ReikaColorAPI.RGBtoHex(134, 107, 92));
        this.addBlockColor(Blocks.SMOOTH_BASALT, ReikaColorAPI.RGBtoHex(72, 72, 78));
        this.addBlockColor(Blocks.BASALT, ReikaColorAPI.RGBtoHex(72, 72, 78));
        this.addBlockColor(Blocks.ANDESITE, ReikaColorAPI.RGBtoHex(136, 136, 138));
        this.addBlockColor(Blocks.DIORITE, ReikaColorAPI.RGBtoHex(188, 188, 190));
        this.addBlockColor(Blocks.GRANITE, ReikaColorAPI.RGBtoHex(153, 114, 99));
        // Deepslate ore variants scan-tinted the same as their stone hosts so the ore band reads clearly.
        this.addBlockColor(Blocks.DEEPSLATE_COAL_ORE, ReikaColorAPI.RGBtoHex(70, 70, 70));
        this.addBlockColor(Blocks.DEEPSLATE_IRON_ORE, ReikaColorAPI.RGBtoHex(214, 173, 145));
        this.addBlockColor(Blocks.DEEPSLATE_COPPER_ORE, ReikaColorAPI.RGBtoHex(224, 121, 79));
        this.addBlockColor(Blocks.DEEPSLATE_GOLD_ORE, ReikaColorAPI.RGBtoHex(251, 237, 76));
        this.addBlockColor(Blocks.DEEPSLATE_REDSTONE_ORE, ReikaColorAPI.RGBtoHex(215, 0, 0));
        this.addBlockColor(Blocks.DEEPSLATE_LAPIS_ORE, ReikaColorAPI.RGBtoHex(40, 98, 175));
        this.addBlockColor(Blocks.DEEPSLATE_DIAMOND_ORE, ReikaColorAPI.RGBtoHex(93, 235, 244));
        this.addBlockColor(Blocks.DEEPSLATE_EMERALD_ORE, ReikaColorAPI.RGBtoHex(23, 221, 98));
        this.addBlockColor(Blocks.RAW_IRON_BLOCK, ReikaColorAPI.RGBtoHex(214, 173, 145));
        this.addBlockColor(Blocks.RAW_COPPER_BLOCK, ReikaColorAPI.RGBtoHex(224, 121, 79));
        this.addBlockColor(Blocks.RAW_GOLD_BLOCK, ReikaColorAPI.RGBtoHex(251, 237, 76));
        this.addBlockColor(Blocks.AMETHYST_BLOCK, ReikaColorAPI.RGBtoHex(133, 97, 191));
        this.addBlockColor(Blocks.BUDDING_AMETHYST, ReikaColorAPI.RGBtoHex(133, 97, 191));
    }

    /** Every RotaryCraft block reads gray on the GPR, matching the legacy blanket mapping. */
    private void addRotaryCraft() {
        for (Block b : BuiltInRegistries.BLOCK) {
            Identifier id = BuiltInRegistries.BLOCK.getKey(b);
            if (id != null && RotaryCraft.MODID.equals(id.getNamespace())) {
                map.putIfAbsent(b, ReikaColorAPI.RGBtoHex(200, 200, 200));
            }
        }
    }

    private void addSlabs() {
        this.addBlockColor(Blocks.SMOOTH_STONE_SLAB, ReikaColorAPI.RGBtoHex(0xA3, 0xA3, 0xA3));
        this.addBlockColor(Blocks.STONE_SLAB, ReikaColorAPI.RGBtoHex(0xA3, 0xA3, 0xA3));
        this.addBlockColor(Blocks.SANDSTONE_SLAB, ReikaColorAPI.RGBtoHex(0xDC, 0xD3, 0xA0));
        this.addBlockColor(Blocks.OAK_SLAB, ReikaColorAPI.RGBtoHex(0xBC, 0x98, 0x62));
        this.addBlockColor(Blocks.COBBLESTONE_SLAB, ReikaColorAPI.RGBtoHex(0x96, 0x96, 0x96));
        this.addBlockColor(Blocks.BRICK_SLAB, ReikaColorAPI.RGBtoHex(0xA5, 0x5B, 0x47));
        this.addBlockColor(Blocks.STONE_BRICK_SLAB, ReikaColorAPI.RGBtoHex(0x79, 0x79, 0x79));
        this.addBlockColor(Blocks.NETHER_BRICK_SLAB, ReikaColorAPI.RGBtoHex(0x36, 0x18, 0x1E));
        this.addBlockColor(Blocks.QUARTZ_SLAB, ReikaColorAPI.RGBtoHex(0xE8, 0xE4, 0xDC));
    }

    private void addFlowers() {
        // 1.7.10 red_flower meta order: poppy, blue orchid, allium, azure bluet, red/orange/white tulip, oxeye daisy.
        this.addBlockColor(Blocks.POPPY, 0xF7070F);
        this.addBlockColor(Blocks.BLUE_ORCHID, 0x29AEFB);
        this.addBlockColor(Blocks.ALLIUM, 0xBF75FB);
        this.addBlockColor(Blocks.AZURE_BLUET, 0xF2F29C);
        this.addBlockColor(Blocks.RED_TULIP, 0xD33A17);
        this.addBlockColor(Blocks.ORANGE_TULIP, 0xE17124);
        this.addBlockColor(Blocks.WHITE_TULIP, 0xF3F3F3);
        this.addBlockColor(Blocks.PINK_TULIP, 0xEABEEA);
        this.addBlockColor(Blocks.OXEYE_DAISY, 0xD2C71E);
        this.addBlockColor(Blocks.DANDELION, ReikaColorAPI.RGBtoHex(255, 255, 0));
    }

    private void addBlockColor(Block b, int rgb) {
        if (b == null)
            return;
        if (map.containsKey(b))
            RotaryCraft.LOGGER.error("GPR Color Mapping - block " + b + " already mapped to a color!");
        else
            map.put(b, rgb);
    }

    private void addBlockMimic(Block mimic, Block target) {
        if (mimic == null || target == null || mimic == target)
            return;
        mimics.put(mimic, target);
    }

    public int getColorForBlock(BlockState state) {
        if (state == null)
            return UNKNOWN_COLOR;
        return this.lookupColorForBlock(state.getBlock(), state);
    }

    private int lookupColorForBlock(Block b, BlockState state) {
        if (b == Blocks.AIR || b == Blocks.CAVE_AIR || b == Blocks.VOID_AIR)
            return AIR_COLOR;
        if (b == null)
            return UNKNOWN_COLOR;
        Block mimic = mimics.get(b);
        if (mimic != null)
            return this.lookupColorForBlock(mimic, mimic.defaultBlockState());
        Integer c = map.get(b);
        if (c != null)
            return c;
        // Any unmapped fluid renders in its still-water/lava color band rather than UNKNOWN.
        FluidState fs = state != null ? state.getFluidState() : null;
        if (fs != null && !fs.isEmpty()) {
            return fs.getType().getFluidType().isLighterThanAir()
                    ? UNKNOWN_COLOR : ReikaColorAPI.RGBtoHex(0, 0, 255);
        }
        // Fallback: colour any block not in the explicit table by its map colour. This covers the
        // long tail — wool/concrete/terracotta colour variants, and every modded block — for free,
        // replacing the enormous hardcoded 1.7.10 colour list with the block's own map tint.
        MapColor mc = b.defaultMapColor();
        if (mc != null && mc != MapColor.NONE)
            return mc.col;
        return UNKNOWN_COLOR;
    }
}
