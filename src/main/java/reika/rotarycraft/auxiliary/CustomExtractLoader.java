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

import reika.rotarycraft.RotaryCraft;
import reika.dragonapi.interfaces.registry.OreType;
import reika.dragonapi.io.ReikaFileReader;
import reika.dragonapi.libraries.java.ReikaStringParser;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.commons.codec.Charsets;

import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.util.*;

public class CustomExtractLoader {

    public static final CustomExtractLoader instance = new CustomExtractLoader();

    private final ArrayList<CustomExtractEntry> data = new ArrayList<>();

    private CustomExtractLoader() {

    }

    private static void writeCommentLine(PrintWriter p, String line) {
        p.append("// " + line + "\n");
    }

    private final String getSaveFileName() {
        return "RotaryCraft_CustomExtracts.cfg";
    }

    private final File getFullSavePath() {
        //return new File(RotaryCraft.config.getConfigFolder(), this.getSaveFileName());
        return new File(FMLLoader.getGamePath().toString(), this.getSaveFileName());
    }

    public void loadFile() {
        RotaryCraft.LOGGER.info("Loading custom extract config.");
        File f = this.getFullSavePath();
        if (!f.exists())
            if (!this.createOreFile(f))
                return;
        try (BufferedReader p = ReikaFileReader.getReader(f, Charsets.UTF_8)) {
            String line = "";
            while (line != null) {
                line = p.readLine();
                if (line != null && !line.isEmpty() && !line.startsWith("//")) {
                    try {
                        CustomExtractEntry entry = this.parseString(line);
                        if (entry != null) {
                            data.add(entry);
                            //ExtractorBonus.addCustomOreDelegate(entry);
                            RotaryCraft.LOGGER.info("Added extract entry " + entry);
                        } else {
                            RotaryCraft.LOGGER.error("Malformed custom extract entry: " + line);
                        }
                    } catch (Exception e) {
                        RotaryCraft.LOGGER.error("Malformed custom extract entry [" + e.getLocalizedMessage() + "]: '" + line + "'");
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            RotaryCraft.LOGGER.info(e.getMessage() + ", and it caused the read to fail!");
            e.printStackTrace();
        }
    }

    private boolean createOreFile(File f) {
        try {
            f.createNewFile();
            PrintWriter p = new PrintWriter(f);
            writeCommentLine(p, "-------------------------------");
            writeCommentLine(p, " RotaryCraft Custom Extract Loader ");
            writeCommentLine(p, "-------------------------------");
            writeCommentLine(p, "");
            writeCommentLine(p, "Use this file to add custom ores and extracts to the extractor.");
            writeCommentLine(p, "Specify one per line, and format them as 'Name, Rarity, Product Type, Product Ore Name, Number, Color 1, Color 2, Native Ore, OreDictionary Name(s)'");
            writeCommentLine(p, "");
            writeCommentLine(p, "Ore rarity is the rarity of the ore blocks in the world, and affects the multiplication rates.");
            writeCommentLine(p, "Valid Rarity Values:");
            for (OreType.OreRarity o : OreType.OreRarity.values()) {
                writeCommentLine(p, "\t" + o.name() + " - " + o.desc + ", like " + o.examples);
            }
            writeCommentLine(p, "");
            writeCommentLine(p, "Valid Product Types:");
            for (ProductType o : ProductType.values()) {
                writeCommentLine(p, "\t" + o.displayName + " - " + o.desc);
            }
            writeCommentLine(p, "");
            writeCommentLine(p, "Native ore is the native ore type of the output if you wish for the custom ore to produce the same smelted products as a native ore.");
            writeCommentLine(p, "Use 'null' for none to have the custom ore produce a unique smelted product.");
            writeCommentLine(p, "Valid Native Ores:");
            /*for (ReikaOreHelper o : ReikaOreHelper.values()) {
                writeCommentLine(p, "\t" + o.name() + " - " + o.getName());
            }
            for (ModOreList o : ModOreList.values()) {
                if (!o.isNetherOres())
                    writeCommentLine(p, "\t" + o.name() + " - " + o.displayName + " " + o.getTypeName());
            }*/
            writeCommentLine(p, "");
            writeCommentLine(p, "Capitalization for the ore dictionary names matters, but is ignored for rarities, types, and native ores.");
            writeCommentLine(p, "Ensure your OreDict names are correct; not all mods follow the 'oreName' and 'productName' convention.");
            writeCommentLine(p, "");
            writeCommentLine(p, "Colors must be hex codes; try to avoid conflicts with existing ores, including those natively handled by RC.");
            writeCommentLine(p, "");
            writeCommentLine(p, "'Number' is the number of items normally obtained from the ore block, such as 1 for coal and 4 for redstone,");
            writeCommentLine(p, "and controls the number of items produced when smelting the flake. Use direct harvesting/smelting, not other processing.");
            writeCommentLine(p, "");
            writeCommentLine(p, "Sample Lines:");
            writeCommentLine(p, "\tSample Ore 1, SCARCE, INGOT, ingotSample, 1, 0xffffff, 0x73cc12, null, oreSample");
            writeCommentLine(p, "\tSample Ore 2, Common, dust, dustMetal, 4, 0x77003b, 0xb1a700, null, oreNotSample, oreSecondName, oreHasLotsOfVariants");
            writeCommentLine(p, "\tSample Ore 3, EVerYwHEre, gEm, ImproperIngot, 3, 0x1487a6, 0x27c61a, null, PoorlyNamedOre");
            writeCommentLine(p, "\tSample Ore 4, rare, Ingot, ingotEndCopper, 1, 0x16723d, 0xcb6faa, COPPER, oreEndCopper");
            writeCommentLine(p, "");
            writeCommentLine(p, "Entries missing names, rarities, types, products, or colors, or having less than one Ore Dictionary name, are incorrect.");
            writeCommentLine(p, "Incorrectly formatted lines will be ignored and will log an error in the console.");
            writeCommentLine(p, "Lines beginning with '//' are comments and will be ignored, as will empty lines. Spaces are stripped.");
            writeCommentLine(p, "");
            writeCommentLine(p, "NOTE WELL: It is your responsibility to choose the ore blocks appropriately.");
            writeCommentLine(p, "\tWhile you can theoretically make anything processable in the Extractor,");
            writeCommentLine(p, "\tnull or missing blocks, and non-blocks are likely to crash and corrupt the");
            writeCommentLine(p, "\tworld. You may also create duplication exploits. No support will be provided in this case.");
            writeCommentLine(p, "====================================================================================");
            p.append("\n");
            p.close();
            return true;
        } catch (Exception e) {
            RotaryCraft.LOGGER.error("Could not generate CustomExtract Config.");
            e.printStackTrace();
            return false;
        }
    }

    private CustomExtractEntry parseString(String s) throws Exception {
        String[] parts = s.split(",");
        for (int i = 1; i < parts.length; i++)
            parts[i] = ReikaStringParser.stripSpaces(parts[i]);
        if (parts.length < 8)
            throw new IllegalArgumentException("Invalid parameter count.");
        String name = parts[0];
        if (name.isEmpty())
            throw new IllegalArgumentException("Empty name is invalid.");
        OreType.OreRarity rarity = OreType.OreRarity.valueOf(parts[1].toUpperCase());
        ProductType type = ProductType.valueOf(parts[2].toUpperCase());
        String prod = parts[3];
        int smelt = Integer.parseInt(parts[4]);
        if (parts[5].startsWith("0x"))
            parts[5] = parts[5].substring(2);
        if (parts[6].startsWith("0x"))
            parts[6] = parts[6].substring(2);
        int c1 = Integer.parseInt(parts[5], 16);
        int c2 = Integer.parseInt(parts[6], 16);
        OreType ore = this.parseOreType(parts[7]);
        String[] ores = new String[parts.length - 8];
        System.arraycopy(parts, 8, ores, 0, ores.length);
        return new CustomExtractEntry(data.size(), name, rarity, type, prod, smelt, c1, c2, ore, ores);
    }

    private OreType parseOreType(String tag) {
        if (tag.equals("null"))
            return null;
        OreType type = null;
        try {
            //type = ModOreList.valueOf(tag.toUpperCase());
        } catch (IllegalArgumentException e) {

        }
        if (type == null) {
            try {
                //type = ReikaOreHelper.valueOf(tag.toUpperCase());
            } catch (IllegalArgumentException e) {

            }
        }
        if (type == null)
            throw new IllegalArgumentException("Native ore type '" + tag + "' is invalid.");
        return type;
    }

    public List<CustomExtractEntry> getEntries() {
        return Collections.unmodifiableList(data);
    }

    public CustomExtractEntry getEntryFromOreBlock(ItemStack is) {
        for (CustomExtractEntry e : data) {
            //if (ReikaItemHelper.collectionContainsItemStack(e.getAllOreBlocks(), is))
            return e;
        }
        return null;
    }

    public enum ProductType {
        INGOT("Ingots like Iron and Copper"),
        DUST("Dusts like Redstone and Sulfur"),
        GEM("Gems like Diamonds and Amethyst"),
        ITEM("Anything else, like ThaumCraft shards");

        public final String displayName;
        private final String desc;

        ProductType(String d) {
            displayName = ReikaStringParser.capFirstChar(this.name());
            desc = d;
        }
    }

    public static class CustomExtractEntry implements OreType {

        public final String displayName;
        public final OreRarity rarity;

        public final int color1;
        public final int color2;
        public final ProductType type;
        public final String productName;
        public final OreType nativeOre;
        public final int numberSmelted;
        public final int ordinal;

        private CustomExtractEntry(int idx, String name, OreRarity r, ProductType t, String prod, int n, int c1, int c2, OreType mod, String... ores) {
            displayName = name;
            rarity = r;
            color1 = c1;
            color2 = c2;
            productName = prod;
            type = t;
            nativeOre = mod;
            numberSmelted = n;
            ordinal = idx;
        }

        @Override
        public OreRarity getRarity() {
            return rarity;
        }

        @Override
        public Collection<ItemStack> getAllOreBlocks() {
            //return Collections.unmodifiableCollection(oreItems);
            return Collections.EMPTY_LIST;
        }

        @Override
        public ItemStack getFirstOreBlock() {
            //return oreItems.get(0).copy();
            return Items.DIRT.getDefaultInstance();
        }

        @Override
        public boolean canGenerateIn(Block b) {
            return false;
        }

        public EnumSet<OreLocation> getOreLocations() {
            return EnumSet.noneOf(OreLocation.class);
        }

        @Override
        public boolean existsInGame() {
            return true;
        }

        public int ordinal() {
            return ordinal;
        }

        @Override
        public String getProductOreDictName() {
            return productName;
        }

        @Override
        public String name() {
            return displayName;
        }

        public int getDisplayColor() {
            return color1;
        }

        @Override
        public int getDropCount() {
            return 1;
        }

        @Override
        public String getDisplayName() {
            return displayName;
        }
    }

}
