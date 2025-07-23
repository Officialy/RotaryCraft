package reika.rotarycraft.auxiliary;

import com.google.common.collect.TreeMultimap;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Blocks;
import reika.dragonapi.exception.RegistrationException;
import reika.dragonapi.instantiable.data.maps.ArrayMap;
import reika.dragonapi.libraries.rendering.ReikaGuiAPI;
import reika.dragonapi.modinteract.lua.LuaMethod;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.recipemanagers.ShapedBlastFurnaceRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.MachineRecipeRenderer;
import reika.rotarycraft.auxiliary.recipemanagers.ShapelessBlastFurnaceRecipe;
import reika.rotarycraft.registry.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NavigableSet;
import java.util.stream.Collectors;

public class HandbookAuxData {

    private static int tickcount;
    private static final ReikaGuiAPI api = ReikaGuiAPI.instance;

    public static List<Recipe<?>> getWorktable() {
        return new ArrayList<>();
    }

    private static final ArrayList<Object[][]> extracts = new ArrayList();
    private static final ArrayList<ItemStack> flakes = new ArrayList();
    private static final ArrayList<ItemStack[]> fermenter = new ArrayList();
    private static final ArrayMap<HandbookRegistry> tabMappings = new ArrayMap(2);
    private static final TreeMultimap<Long, MachineRegistry> powerData = TreeMultimap.create();
    private static final TreeMultimap<Integer, MachineRegistry> torqueData = TreeMultimap.create();
    private static final TreeMultimap<Integer, MachineRegistry> speedData = TreeMultimap.create();

    static {
        load();
        mapHandbook();
    }

    private static void load() {
        addPlants();
        addPowerData();
    }

    public static void addPowerData() {
        for (int i = 0; i < MachineRegistry.machineList.length; i++) {
            MachineRegistry m = MachineRegistry.machineList.get(i);
            if (!m.isDummiedOut()) {
                if (m.isPowerReceiver() && !m.isModConversionEngine() && !m.isPoweredTransmissionMachine()) {
                    PowerReceivers p = m.getPowerReceiverEntry();
                    if (p != null) {
                        long minp = p.getMinPowerForDisplay();
                        int mint = p.getMinTorqueForDisplay();
                        int mins = p.getMinSpeedForDisplay();
                        powerData.put(minp, m);
                        torqueData.put(mint, m);
                        speedData.put(mins, m);
                    }
                    else
                        throw new RegistrationException(RotaryCraft.getInstance(), "Machine "+m+" is a power receiver but has no power Enum!");
                }
            }
        }
    }

    private static void mapHandbook() {
        for (int i = 0; i < HandbookRegistry.tabList.length; i++) {
            HandbookRegistry h = HandbookRegistry.tabList[i];
            tabMappings.putV(h, h.getScreen(), h.getPage());
        }
    }

    public static HandbookRegistry getMapping(int screen, int page) {
        return tabMappings.getV(screen, page);
    }

    public static void reload() {
        load();
    }

    private static void addPlants() {
        ItemStack out = RotaryItems.YEAST.get().getDefaultInstance();
        ItemStack[] in = new ItemStack[]{new ItemStack(Items.SUGAR), new ItemStack(Blocks.DIRT)};
        ItemStack[] args = new ItemStack[]{out, in[0], in[1]};
        fermenter.add(args);
    }

    public static void drawPage(PoseStack stack, Font f, GuiGraphics ri, int screen, int page, int subpage, int dx, int dy, int mouseX, int mouseY) {
        HandbookRegistry h = HandbookRegistry.getEntry(screen, page);
        if (h.isMachine() || h.isTrans() || h.isEngine() || h.getParent() == HandbookRegistry.CONVERTERDESC) {
            List<ItemStack> out = h.getCrafting();
            if (out == null || out.size() <= 0)
                return;
            if (h.isCustomRecipe()) {
                api.drawCustomRecipes(ri, f, out, (Collection<Recipe<?>>) getWorktable(), dx+72-18, dy+18, dx-1620, dy+32);
            }
            else {
                api.drawCustomRecipes(ri, f, out, Minecraft.getInstance().level.getRecipeManager().getRecipes().stream().collect(Collectors.toList()), dx+72-18, dy+18, dx-1620, dy+32);
            }
        }
        else if (h.isCrafting()) {
            List<ItemStack> out = h.getCrafting();
            if (out == null || out.size() <= 0)
                return;
            if (h.isCustomRecipe()) {
                api.drawCustomRecipes(ri, f, out, (Collection<Recipe<?>>) getWorktable(), dx+72, dy+18, dx+162, dy+32);
            }
            else {
                api.drawCustomRecipes(ri, f, out, Minecraft.getInstance().level.getRecipeManager().getRecipes().stream().collect(Collectors.toList()), dx+72, dy+18, dx+162, dy+32);
            }
        }
        else if (h.isSmelting()) {
            ItemStack out = h.getSmelting();
            api.drawSmelting(ri, f, out, dx+87, dy+36, dx+141, dy+32);
            if (h == HandbookRegistry.TUNGSTEN) {
                api.drawItemStackWithTooltip(ri, f, RotaryItems.TUNGSTEN_FLAKES.get().getDefaultInstance(), dx+87, dy+28);
            }
        }
        else if (h == HandbookRegistry.EXTRACTS) {

        }
        else if (h == HandbookRegistry.COMPACTS) {
            ItemStack in = new ItemStack(Items.COAL);
            ItemStack out = new ItemStack(Items.DIAMOND, 2);
            MachineRecipeRenderer.instance.drawCompressor(ri, dx+66, dy+14, in, dx+120, dy+41, out);
        }
        else if (h == HandbookRegistry.GLASS) {
            api.drawItemStackWithTooltip(ri, f, new ItemStack(Blocks.OBSIDIAN), dx+87, dy+28);
            api.drawItemStackWithTooltip(ri, f, new ItemStack(Blocks.GLASS), dx+145, dy+28);
        }
        else if (h == HandbookRegistry.JETPACK) {
            int k = (int)((System.nanoTime()/6000000000L)%5);
            int k2 = (int)((System.nanoTime()/3000000000L)%2);
            int k3 = (int)((System.nanoTime()/2000000000L)%3);
            if (k == 0) {
                ItemStack out = RotaryItems.JETPACK.get().getDefaultInstance();
                List<Recipe<?>> li = Minecraft.getInstance().level.getRecipeManager().getRecipes().stream()
                        .filter(r -> r.getResultItem(Minecraft.getInstance().level.registryAccess()).getItem() == out.getItem())
                        .collect(Collectors.toList());
                api.drawCustomRecipeList(ri, f, li, dx+72, dy+18, dx+162, dy+32);
            }
            else if (k == 1) {
                ItemStack plate = k2 == 0 ? RotaryItems.HSLA_CHESTPLATE.get().getDefaultInstance() : RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get().getDefaultInstance();
                ItemStack out = k2 == 0 ? RotaryItems.HSLA_STEEL_PACK.get().getDefaultInstance() : RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get().getDefaultInstance();
                api.drawItemStackWithTooltip(ri, f, plate, dx+72, dy+10);
                api.drawItemStackWithTooltip(ri, f, RotaryItems.JETPACK.get().getDefaultInstance(), dx+90, dy+10);
                api.drawItemStackWithTooltip(ri, f, out, dx+166, dy+28);
            }
            else if (k == 2) { //wing
                ItemStack ing = k3 != 2 ? RotaryItems.HSLA_STEEL_INGOT.get().getDefaultInstance() : RotaryItems.BEDROCK_ALLOY_INGOT.get().getDefaultInstance();
                ItemStack pack = k3 == 0 ? RotaryItems.JETPACK.get().getDefaultInstance() : k3 == 1 ? RotaryItems.HSLA_STEEL_PACK.get().getDefaultInstance() : RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get().getDefaultInstance();
                ItemStack out = k3 == 0 ? RotaryItems.JETPACK.get().getDefaultInstance() : k3 == 1 ? RotaryItems.HSLA_STEEL_PACK.get().getDefaultInstance() : RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get().getDefaultInstance();
                //ItemJetPack.PackUpgrades.WING.enable(out, true); //TODO: Reimplement this
                api.drawItemStackWithTooltip(ri, f, ing, dx+72, dy+10);
                api.drawItemStackWithTooltip(ri, f, ing, dx+90, dy+10);
                api.drawItemStackWithTooltip(ri, f, ing, dx+108, dy+10);
                api.drawItemStack(ri, f, out, dx+166, dy+28);
                api.drawMultilineTooltip(stack, ri, out, dx+166, dy+28, 0, 0);
            }
            else if (k == 3) { //cooling
                ItemStack pack = k3 == 0 ? RotaryItems.JETPACK.get().getDefaultInstance() : k3 == 1 ? RotaryItems.HSLA_STEEL_PACK.get().getDefaultInstance() : RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get().getDefaultInstance();
                ItemStack out = k3 == 0 ? RotaryItems.JETPACK.get().getDefaultInstance() : k3 == 1 ? RotaryItems.HSLA_STEEL_PACK.get().getDefaultInstance() : RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get().getDefaultInstance();
                //ItemJetPack.PackUpgrades.COOLING.enable(out, true); //TODO: Reimplement this
                api.drawItemStackWithTooltip(ri, f, new ItemStack(Items.IRON_INGOT), dx+72, dy+28);
                api.drawItemStackWithTooltip(ri, f, new ItemStack(Blocks.IRON_BLOCK), dx+108, dy+28);
                api.drawItemStackWithTooltip(ri, f, pack, dx+90, dy+28);
                api.drawItemStack(ri, f, out, dx+166, dy+28);
                api.drawMultilineTooltip(stack, ri, out, dx+166, dy+28, 0, 0);
            }
            else if (k == 4) { //thrust boost
                ItemStack pack = k3 == 0 ? RotaryItems.JETPACK.get().getDefaultInstance() : k3 == 1 ? RotaryItems.HSLA_STEEL_PACK.get().getDefaultInstance() : RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get().getDefaultInstance();
                ItemStack out = k3 == 0 ? RotaryItems.JETPACK.get().getDefaultInstance() : k3 == 1 ? RotaryItems.HSLA_STEEL_PACK.get().getDefaultInstance() : RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get().getDefaultInstance();
                //ItemJetPack.PackUpgrades.JET.enable(out, true); //TODO: Reimplement this
                api.drawItemStackWithTooltip(ri, f, new ItemStack(Items.BLAZE_ROD), dx+90, dy+46);
                api.drawItemStackWithTooltip(ri, f, pack, dx+90, dy+28);
                api.drawItemStack(ri, f, out, dx+166, dy+28);
                api.drawMultilineTooltip(stack, ri, out, dx+166, dy+28, 0, 0);
            }
        }
        else if (h == HandbookRegistry.JUMPBOOTS) {
            int k = (int)((System.nanoTime()/2000000000)%2);
            if (k == 0) {
                ItemStack out = RotaryItems.JUMP.get().getDefaultInstance();
                List<Recipe<?>> li = Minecraft.getInstance().level.getRecipeManager().getRecipes().stream()
                        .filter(r -> r.getResultItem(Minecraft.getInstance().level.registryAccess()).getItem() == out.getItem())
                        .collect(Collectors.toList());
                api.drawCustomRecipeList(ri, f, li, dx+72, dy+18, dx+162, dy+32);
            }
            else {
                api.drawItemStackWithTooltip(ri, f, RotaryItems.BEDROCK_ALLOY_BOOTS.get().getDefaultInstance(), dx+72, dy+10);
                api.drawItemStackWithTooltip(ri, f, RotaryItems.JUMP.get().getDefaultInstance(), dx+90, dy+10);
                api.drawItemStackWithTooltip(ri, f, RotaryItems.BEDROCK_ALLOY_JUMP_BOOTS.get().getDefaultInstance(), dx+166, dy+28);
            }
        }
        else if (h == HandbookRegistry.YEAST) {
            int k4 = (int)((System.nanoTime()/1000000000)%fermenter.size());
            ItemStack[] args = fermenter.get(k4);
            ItemStack[] in = new ItemStack[]{args[1], args[2]};
            ItemStack out = args[0];
            MachineRecipeRenderer.instance.drawFermenter(ri, dx+102, dy+18, in, dx+159, dy+32, out);
        }
        else if (h == HandbookRegistry.NETHERDUST) {
            if ((System.nanoTime()/2000000000)%2 == 0) {
                api.drawItemStackWithTooltip(ri, f, new ItemStack(Blocks.NETHERRACK), dx+87, dy+28);
                api.drawItemStackWithTooltip(ri, f, RotaryItems.NETHERRACK_DUST.get().getDefaultInstance(), dx+145, dy+28);
            }
            else {
                api.drawItemStackWithTooltip(ri, f, new ItemStack(Blocks.SOUL_SAND), dx+87, dy+28);
                api.drawItemStackWithTooltip(ri, f, RotaryItems.TAR.get().getDefaultInstance(), dx+145, dy+28);
            }
        }
        else if (h == HandbookRegistry.SILVERINGOT) {
            // RotaryItems.silverflakes and silveringot are not fields, using placeholders
            api.drawItemStackWithTooltip(ri, f, new ItemStack(Items.IRON_INGOT), dx+87, dy+28);
            api.drawItemStackWithTooltip(ri, f, new ItemStack(Items.GOLD_INGOT), dx+145, dy+28);
        }
        else if (h == HandbookRegistry.SALT) {
            api.drawItemStackWithTooltip(ri, f, new ItemStack(Items.WATER_BUCKET), dx+90, dy+28);
            api.drawItemStackWithTooltip(ri, f, RotaryItems.SALT.get().getDefaultInstance(), dx+166, dy+28);
        }
        else if (h == HandbookRegistry.SAWDUST) {
            int k5 = (int)((System.nanoTime()/2000000000)%5);
            switch (k5) {
                case 0 -> {
                    api.drawItemStackWithTooltip(ri, f, new ItemStack(Items.WATER_BUCKET), dx + 72 + 18, dy + 10);
                    api.drawItemStackWithTooltip(ri, f, RotaryItems.SAWDUST.get().getDefaultInstance(), dx + 72, dy + 28);
                    api.drawItemStackWithTooltip(ri, f, RotaryItems.SAWDUST.get().getDefaultInstance(), dx + 72 + 18, dy + 28);
                    api.drawItemStackWithTooltip(ri, f, RotaryItems.SAWDUST.get().getDefaultInstance(), dx + 72 + 36, dy + 28);
                    api.drawItemStackWithTooltip(ri, f, new ItemStack(Blocks.STONE), dx + 72, dy + 46);
                    api.drawItemStackWithTooltip(ri, f, new ItemStack(Blocks.STONE), dx + 72 + 18, dy + 46);
                    api.drawItemStackWithTooltip(ri, f, new ItemStack(Blocks.STONE), dx + 72 + 36, dy + 46);
                    api.drawItemStackWithTooltip(ri, f, new ItemStack(Items.PAPER, 8), dx + 166, dy + 28);
                }
                case 1 -> {
                    api.drawItemStackWithTooltip(ri, f, RotaryItems.SAWDUST.get().getDefaultInstance(), dx + 72, dy + 10);
                    api.drawItemStackWithTooltip(ri, f, RotaryItems.SAWDUST.get().getDefaultInstance(), dx + 72, dy + 28);
                    api.drawItemStackWithTooltip(ri, f, RotaryItems.SAWDUST.get().getDefaultInstance(), dx + 72 + 18, dy + 10);
                    api.drawItemStackWithTooltip(ri, f, RotaryItems.SAWDUST.get().getDefaultInstance(), dx + 72 + 18, dy + 28);
                    api.drawItemStackWithTooltip(ri, f, Items.OAK_WOOD.getDefaultInstance(), dx + 166, dy + 28);
                }
                case 2 -> {
                    api.drawItemStackWithTooltip(ri, f, new ItemStack(Items.BLACK_DYE), dx + 72 + 36, dy + 10);
                    api.drawItemStackWithTooltip(ri, f, RotaryItems.SAWDUST.get().getDefaultInstance(), dx + 72, dy + 10);
                    api.drawItemStackWithTooltip(ri, f, RotaryItems.SAWDUST.get().getDefaultInstance(), dx + 72, dy + 28);
                    api.drawItemStackWithTooltip(ri, f, RotaryItems.SAWDUST.get().getDefaultInstance(), dx + 72 + 18, dy + 10);
                    api.drawItemStackWithTooltip(ri, f, RotaryItems.SAWDUST.get().getDefaultInstance(), dx + 72 + 18, dy + 28);
                    api.drawItemStackWithTooltip(ri, f, Items.SPRUCE_WOOD.getDefaultInstance(), dx + 166, dy + 28);
                }
                case 3 -> {
                    api.drawItemStackWithTooltip(ri, f, new ItemStack(Items.WHITE_DYE), dx + 72 + 36, dy + 10);
                    api.drawItemStackWithTooltip(ri, f, RotaryItems.SAWDUST.get().getDefaultInstance(), dx + 72, dy + 10);
                    api.drawItemStackWithTooltip(ri, f, RotaryItems.SAWDUST.get().getDefaultInstance(), dx + 72, dy + 28);
                    api.drawItemStackWithTooltip(ri, f, RotaryItems.SAWDUST.get().getDefaultInstance(), dx + 72 + 18, dy + 10);
                    api.drawItemStackWithTooltip(ri, f, RotaryItems.SAWDUST.get().getDefaultInstance(), dx + 72 + 18, dy + 28);
                    api.drawItemStackWithTooltip(ri, f, Items.BIRCH_WOOD.getDefaultInstance(), dx + 166, dy + 28);
                }
                case 4 -> {
                    api.drawItemStackWithTooltip(ri, f, new ItemStack(Items.RED_DYE), dx + 72 + 36, dy + 10);
                    api.drawItemStackWithTooltip(ri, f, RotaryItems.SAWDUST.get().getDefaultInstance(), dx + 72, dy + 10);
                    api.drawItemStackWithTooltip(ri, f, RotaryItems.SAWDUST.get().getDefaultInstance(), dx + 72, dy + 28);
                    api.drawItemStackWithTooltip(ri, f, RotaryItems.SAWDUST.get().getDefaultInstance(), dx + 72 + 18, dy + 10);
                    api.drawItemStackWithTooltip(ri, f, RotaryItems.SAWDUST.get().getDefaultInstance(), dx + 72 + 18, dy + 28);
                    api.drawItemStackWithTooltip(ri, f, Items.JUNGLE_WOOD.getDefaultInstance(), dx + 166, dy + 28);
                }
            }
        }
        else if (h == HandbookRegistry.RAILGUNAMMO) {
            List<Recipe<?>> li = Minecraft.getInstance().level.getRecipeManager().getRecipes().stream()
                    .filter(r -> {
                        // RotaryItems.RAILGUN is not a field, commenting out related code
                        // for (int i = 0; i < RotaryItems.RAILGUN.get().getNumberMetadatas(); i++) {
                        //     if (r.getResultItem(Minecraft.getInstance().level.registryAccess()).getItem() == RotaryItems.RAILGUN.get().getStackOfMetadata(i).getItem()) {
                        //         return true;
                        //     }
                        // }
                        return false; // Always return false for now
                    })
                    .collect(Collectors.toList());
            api.drawCustomRecipeList(ri, f, li, dx+72, dy+18, dx+162, dy+32);
        }
        else if (h == HandbookRegistry.BEDTOOLS) {
            ArrayList<ItemStack> li = new ArrayList();
            // RotaryItems.itemList is not a field, commenting out related code
            // for (int i = 0; i < RotaryItems.itemList.length; i++) {
            //     RotaryItems ir = RotaryItems.itemList[i];
            //     if (ir.isBedrockTool() && !ir.isDummiedOut()) {
            //         li.add(ir.getEnchantedStack());
            //     }
            // }
            // Placeholder for now
            li.add(RotaryItems.BEDROCK_ALLOY_PICK.get().getDefaultInstance());
            li.add(RotaryItems.BEDROCK_ALLOY_AXE.get().getDefaultInstance());

            int index = (int)((System.currentTimeMillis()/2000)%li.size());
            ItemStack is = li.get(index);
            MachineRecipeRenderer.instance.drawBlastFurnaceRecipe(ri, dx+99, dy+18, dx+181, dy+32, Minecraft.getInstance().level, is);
        }
        else if (h == HandbookRegistry.BEDARMOR) {
            ArrayList<ItemStack> li = new ArrayList();
            // RotaryItems.itemList is not a field, commenting out related code
            // for (int i = 0; i < RotaryItems.itemList.length; i++) {
            //     RotaryItems ir = RotaryItems.itemList[i];
            //     if (ir.isBedrockArmor()) {
            //         li.add(ir.getEnchantedStack());
            //     }
            // }
            // Placeholder for now
            li.add(RotaryItems.BEDROCK_ALLOY_HELMET.get().getDefaultInstance());
            li.add(RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get().getDefaultInstance());

            int index = (int)((System.currentTimeMillis()/2000)%li.size());
            ItemStack is = li.get(index);
            MachineRecipeRenderer.instance.drawBlastFurnaceRecipe(ri, dx+99, dy+18, dx+181, dy+32, Minecraft.getInstance().level, is);
        }
        /*else if (h == HandbookRegistry.STRONGSPRING) {
            // RotaryItems.STRONGCOIL is not a field, commenting out related code
            ItemStack is = RotaryItems.HSLA_STEEL_SPRING.get().getDefaultInstance(); // Placeholder
            MachineRecipeRenderer.instance.drawBlastFurnaceRecipe(ri, dx+99, dy+18, dx+181, dy+32, Minecraft.getInstance().level, is);
        }*/
        //else if (h == HandbookRegistry.BEDINGOT) {
        //	ItemStack is = RotaryItems.BEDROCK_ALLOY_INGOT;
        //	List<BlastRecipe> c = RecipesBlastFurnace.getRecipes().getAllRecipesMaking(is);
        //	MachineRecipeRenderer.instance.drawBlastFurnace(dx+99, dy+18, dx+185, dy+36, c.get(0));
        //}
        else if (h == HandbookRegistry.ALLOYING) {
            // This section relies on outdated RecipesBlastFurnace and BlastFurnacePattern.
            // It will be replaced with a call to drawBlastFurnaceRecipe if a suitable recipe is found.
            List<ShapedBlastFurnaceRecipe> shapedRecipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RotaryRecipeTypes.BLAST_FURNACE_SHAPED.get());
            List<ShapelessBlastFurnaceRecipe> shapelessRecipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RotaryRecipeTypes.BLAST_FURNACE_SHAPELESS.get());

            List<Recipe<?>> allRecipes = new ArrayList<>();
            allRecipes.addAll(shapedRecipes);
            allRecipes.addAll(shapelessRecipes);

            if (!allRecipes.isEmpty()) {
                int index = (int)((System.currentTimeMillis()/2000)%allRecipes.size());
                Recipe<?> currentRecipe = allRecipes.get(index);
                if (currentRecipe instanceof ShapedBlastFurnaceRecipe shapedRecipe) {
                    MachineRecipeRenderer.instance.drawBlastFurnaceRecipe(ri, dx+99, dy+18, dx+185, dy+36, Minecraft.getInstance().level, shapedRecipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
                } else if (currentRecipe instanceof ShapelessBlastFurnaceRecipe shapelessRecipe) {
                    MachineRecipeRenderer.instance.drawBlastFurnaceRecipe(ri, dx+99, dy+18, dx+185, dy+36, Minecraft.getInstance().level, shapelessRecipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
                }
            } else {
                RotaryCraft.LOGGER.warn("No alloying recipes found for display in HandbookAuxData.");
            }
            // api.drawCenteredStringNoShadow(f, p.getRequiredTemperature()+"C", dx+54, dy+66, 0); // p.getRequiredTemperature() is not available
        }
        else if (h == HandbookRegistry.COKE) {
            // This section relies on outdated RecipesBlastFurnace and BlastRecipe.
            // It will be replaced with a call to drawBlastFurnaceRecipe if a suitable recipe is found.
            List<ShapedBlastFurnaceRecipe> shapedRecipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RotaryRecipeTypes.BLAST_FURNACE_SHAPED.get());
            List<ShapelessBlastFurnaceRecipe> shapelessRecipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RotaryRecipeTypes.BLAST_FURNACE_SHAPELESS.get());

            List<Recipe<?>> allRecipes = new ArrayList<>();
            allRecipes.addAll(shapedRecipes);
            allRecipes.addAll(shapelessRecipes);

            if (!allRecipes.isEmpty()) {
                int index = (int)((System.currentTimeMillis()/2000)%allRecipes.size());
                Recipe<?> currentRecipe = allRecipes.get(index);
                if (currentRecipe instanceof ShapedBlastFurnaceRecipe shapedRecipe) {
                    MachineRecipeRenderer.instance.drawBlastFurnaceRecipe(ri, dx+99, dy+18, dx+185, dy+36, Minecraft.getInstance().level, shapedRecipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
                } else if (currentRecipe instanceof ShapelessBlastFurnaceRecipe shapelessRecipe) {
                    MachineRecipeRenderer.instance.drawBlastFurnaceRecipe(ri, dx+99, dy+18, dx+185, dy+36, Minecraft.getInstance().level, shapelessRecipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
                }
            } else {
                RotaryCraft.LOGGER.warn("No coke recipes found for display in HandbookAuxData.");
            }
            // api.drawCenteredStringNoShadow(f, p.temperature+"C", dx+54, dy+66, 0); // p.temperature is not available
        }
        else if (h == HandbookRegistry.STEELINGOT) {
            ItemStack is = RotaryItems.HSLA_STEEL_INGOT.get().getDefaultInstance();
            // This section relies on outdated RecipesBlastFurnace and BlastRecipe.
            // It will be replaced with a call to drawBlastFurnaceRecipe if a suitable recipe is found.
            List<ShapedBlastFurnaceRecipe> shapedRecipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RotaryRecipeTypes.BLAST_FURNACE_SHAPED.get());
            List<ShapelessBlastFurnaceRecipe> shapelessRecipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RotaryRecipeTypes.BLAST_FURNACE_SHAPELESS.get());

            List<Recipe<?>> allRecipes = new ArrayList<>();
            allRecipes.addAll(shapedRecipes);
            allRecipes.addAll(shapelessRecipes);

            if (!allRecipes.isEmpty()) {
                int index = (int)((System.currentTimeMillis()/2000)%allRecipes.size());
                Recipe<?> currentRecipe = allRecipes.get(index);
                if (currentRecipe instanceof ShapedBlastFurnaceRecipe shapedRecipe) {
                    MachineRecipeRenderer.instance.drawBlastFurnaceRecipe(ri, dx+99, dy+18, dx+185, dy+36, Minecraft.getInstance().level, shapedRecipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
                } else if (currentRecipe instanceof ShapelessBlastFurnaceRecipe shapelessRecipe) {
                    MachineRecipeRenderer.instance.drawBlastFurnaceRecipe(ri, dx+99, dy+18, dx+185, dy+36, Minecraft.getInstance().level, shapelessRecipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
                }
            } else {
                RotaryCraft.LOGGER.warn("No HSLA steel ingot recipes found for display in HandbookAuxData.");
            }
        }
    }

    public static void drawGraphics(PoseStack stack, Font f, GuiGraphics ri, int screen, int page, int subpage, int dx, int dy, int mouseX, int mouseY) {
        try {
            HandbookRegistry h = HandbookRegistry.getEntry(screen, page); // Declared h here
            if (h == HandbookRegistry.TERMS) {
                int xc = dx+125; int yc = dy+43; int r = 35;
                api.drawCircle(xc, yc, r, 0);
                api.drawLine(stack, xc, yc, xc+r, yc, 0);
                api.drawLine(stack, xc, yc, (int)(xc+r-0.459*r), (int)(yc-0.841*r), 0);
                ri.drawString(f, "One radian", xc+r+10, yc-4, 0x000000); // Changed f.draw to ri.drawString
            }
            else if (h == HandbookRegistry.PHYSICS) {
                int r = 5;
                int xc = dx+25;
                int yc = dy+45;
                api.drawCircle(xc, yc, r, 0);
                api.drawLine(stack, xc, yc, xc+45, yc, 0x0000ff);
                api.drawLine(stack, xc+45, yc, xc+45, yc+20, 0xff0000);
                api.drawLine(stack, xc+45, yc, xc+50, yc+5, 0xff0000);
                api.drawLine(stack, xc+45, yc, xc+40, yc+5, 0xff0000);
                ri.drawString(f, "Distance", xc+4, yc-10, 0x0000ff); // Changed f.draw to ri.drawString
                ri.drawString(f, "Force", xc+30, yc+20, 0xff0000); // Changed f.draw to ri.drawString

                api.drawLine(stack, xc-2*r, (int)(yc-1.4*r), xc-r, yc-r*2-2, 0x8800ff);
                api.drawLine(stack, xc-2*r, (int)(yc-1.4*r), xc-2*r-2, yc, 0x8800ff);
                api.drawLine(stack, xc-2*r, (int)(yc+1.4*r), xc-2*r-2, yc, 0x8800ff);
                api.drawLine(stack, xc-2*r, (int)(yc+1.4*r), xc-r, yc+r*2+2, 0x8800ff);
                api.drawLine(stack, xc+2, yc+r*2+2, xc-r, yc+r*2+2, 0x8800ff);
                api.drawLine(stack, xc+2, yc+r*2+2, xc-3, yc+r*2+7, 0x8800ff);
                api.drawLine(stack, xc+2, yc+r*2+2, xc-3, yc+r*2-3, 0x8800ff);
                ri.drawString(f, "Torque", xc-24, yc+18, 0x8800ff); // Changed f.draw to ri.drawString

                r = 35;
                xc = dx+125+r+r/2;
                yc = dy+43;
                api.drawCircle(xc, yc, r, 0);

                int n1 = 2;
                int n2 = 4;
                double a = 57.3*System.nanoTime()/1000000000%360;
                double b = n1*57.3*System.nanoTime()/1000000000%360;
                double c = n2*57.3*System.nanoTime()/1000000000%360;
                api.drawLine(stack, xc, yc, (int)(xc+Math.cos(Math.toRadians(a))*r), (int)(yc+Math.sin(Math.toRadians(a))*r), 0xff0000);
                api.drawLine(stack, xc, yc, (int)(xc+Math.cos(Math.toRadians(b))*r), (int)(yc+Math.sin(Math.toRadians(b))*r), 0x0000ff);
                api.drawLine(stack, xc, yc, (int)(xc+Math.cos(Math.toRadians(c))*r), (int)(yc+Math.sin(Math.toRadians(c))*r), 0x00a000);

                int xOffset = 2;
                int yOffset = 6;
                ri.drawString(f, "1 rad/s", xc+r-4+xOffset, yc+18-yOffset, 0xff0000); // Changed f.draw to ri.drawString
                ri.drawString(f, n1+" rad/s", xc+r-4+xOffset, yc+18+10-yOffset, 0x0000ff); // Changed f.draw to ri.drawString
                ri.drawString(f, n2+" rad/s", xc+r-4+xOffset, yc+18+20-yOffset, 0x00a000); // Changed f.draw to ri.drawString
            }
            /*
            else if (h == HandbookRegistry.BAITBOX && subpage == 1) { // BAITBOX is not a field
                // This section relies on MobBait which is not defined.
                // It will be skipped for now.
            }
            else if (h == HandbookRegistry.TERRA && subpage == 1) { // TERRA is not a field
                // This section relies on TileEntityTerraformer, BiomeTransform, ReikaBiomeHelper, ReikaLiquidRenderer, IIcon.
                // It will be skipped for now.
            }
            */
            else if (h == HandbookRegistry.TIERS) {
                int maxw = 11;
                NavigableSet<Long> s = powerData.keySet();
                int t = 0;
                for (long key : s) {
                    if (t == subpage) {
                        String sg = String.format("- %d W", key);
                        ri.drawString(f, sg, dx+14, dy+6, 0); // Changed f.draw to ri.drawString, fixed getStringWidth argument
                        NavigableSet<MachineRegistry> c = powerData.get(key);
                        int k = 0;
                        int n = 0;
                        for (MachineRegistry m : c) {
                            // m.getCraftedProduct() is not a method
                            ItemStack is = new ItemStack(Items.IRON_INGOT); // Placeholder
                            if (k > maxw) {
                                k = 0;
                                n++;
                            }
                            int x = dx+k*18+10;
                            int y = dy+n*18+29;
                            api.drawItemStackWithTooltip(ri, f, is, x, y);
                            k++;
                        }
                    }
                    t++;
                }
                // ReikaRenderHelper.disableStandardItemLighting(); // RenderHelper is not defined
            }
            else if (h == HandbookRegistry.TIMING) {
                int k = 0;
                int n = 0;
                for (int i = 0; i < DurationRegistry.durationList.length; i++) {
                    DurationRegistry d = DurationRegistry.durationList[i];
                    MachineRegistry m = d.getMachine();
                    // m.getCraftedProduct() is not a method
                    ItemStack is = new ItemStack(Items.CLOCK); // Placeholder
                    int maxw = 11;
                    if (k > maxw) {
                        k = 0;
                        n++;
                    }
                    int x = dx+k*18+10;
                    int y = dy+n*18+29;

                    api.drawItemStackWithTooltip(ri, f, is, x, y);

                    if (api.isMouseInBox(x, x+17, y, y+17, mouseX, mouseY)) { // Added mouseX, mouseY
                        for (int j = 0; j < d.getNumberStages(); j++) {
                            //api.drawTooltipAt(font, d.getDisplayTime(j), mx, my);
                            //ReikaRenderHelper.disableLighting();
                            int c = m.canDoMultiPerTick() ? 0x80ff80 : 0xffffff;
                            ri.drawString(f, d.getDisplayTime(j), dx+10, dy+150+j*10, c); // Changed f.draw to ri.drawString
                        }
                    }

                    k++;
                }
            }
            else if (h == HandbookRegistry.COMPUTERCRAFT) {
                if (subpage > 0) {
                    Collection<LuaMethod> li = LuaMethod.getMethods();
                    int di = (subpage-1)*36;
                    int max = Math.min(di+36, MachineRegistry.machineList.length);
                    for (int i = di; i < max; i++) {
                        MachineRegistry m = MachineRegistry.machineList.get(i);
                        // m.getCraftedProduct() is not a method
                        ItemStack is = new ItemStack(Items.REDSTONE); // Placeholder
                        // m.hasSubdivisions(), getNumberSubtypes(), getSubType() are not methods
                        // if (m.hasSubdivisions()) {
                        //     int meta = m.getNumberSubtypes();
                        //     int time = (int)(System.currentTimeMillis()/1600)%meta;
                        //     is = m.getSubType(time);
                        // }
                        int r = (i-di)/12;
                        int c = i%12;
                        int x = dx+c*18+10;
                        int y = dy+r*18+20;
                        api.drawItemStackWithTooltip(ri, f, is, x, y);
                        if (api.isMouseInBox(x, x+17, y, y+17, mouseX, mouseY)) { // Added mouseX, mouseY
                            int k = 0;
                            for (LuaMethod cur : li) {
                                // m.getTEClass() is not a method
                                // if (cur.isDocumented() && cur.isClassInstanceOf(m.getTEClass())) {
                                if (cur.isDocumented()) { // Simplified condition
                                    //ReikaRenderHelper.disableLighting();
                                    String s = cur.getReturnType().displayName+" "+cur.displayName+"("+cur.getArgsAsString()+")";
                                    ri.drawString(f, s, dx+11, dy+88+k*10, 0xffffff); // Changed f.draw to ri.drawString
                                    k++;
                                }
                            }
                        }
                    }
                }
            }
            /*
            else if (h == HandbookRegistry.ALERTS) { // ALERTS is not a field
                // This section relies on HandbookNotifications and Alert.
                // It will be skipped for now.
            }
            else if (h == HandbookRegistry.PACKMODS) { // PACKMODS is not a field
                // This section relies on PackModificationTracker and PackModification.
                // It will be skipped for now.
            }
            */
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getPowerDataSize() {
        return powerData.keySet().size();
    }
}
