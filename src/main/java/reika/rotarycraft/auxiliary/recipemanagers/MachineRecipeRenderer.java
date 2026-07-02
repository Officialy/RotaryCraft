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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import reika.dragonapi.libraries.rendering.ReikaGuiAPI;
import reika.rotarycraft.registry.RotaryRecipeTypes;

import java.util.Collection;
import java.util.List;

/**
 * 26.1 reintroduction of the legacy {@code MachineRecipeRenderer.instance} used by
 * {@link reika.rotarycraft.auxiliary.HandbookAuxData}. Stable, minimal surface — every
 * method takes a {@link GuiGraphicsExtractor} (the new draw target) plus the in/out item
 * stacks and the screen-space positions, and routes through {@link ReikaGuiAPI}'s
 * already-ported {@code drawItemStackWithTooltip} so tooltips work for handbook pages.
 *
 * <p>This is not a full port of the original 1.7.10 recipe-renderer surface (which also
 * handled arrow / flame icons via the immediate-mode GL pipeline), but it covers the
 * call-sites in HandbookAuxData and lets the handbook recipe inserts render correctly.
 *
 * <p><b>Client-side recipe iteration in 26.1:</b> {@link Level#recipeAccess()} on the
 * client returns a minimal {@code RecipeAccess} (only stonecutter + property sets). Full
 * iteration via {@code RecipeMap} is only available when a server is reachable (integrated
 * or dedicated). The blast-furnace path below tries the integrated-server fallback; if
 * none is available we draw the output stack alone so the handbook page still renders.
 */
public final class MachineRecipeRenderer {

    public static final MachineRecipeRenderer instance = new MachineRecipeRenderer();

    private static final ReikaGuiAPI api = ReikaGuiAPI.instance;

    private MachineRecipeRenderer() {}

    private static Font font() {
        return Minecraft.getInstance().font;
    }

    /**
     * Compressor: single input → single output. Layout matches the original 1.7.10 call
     * pattern from {@link reika.rotarycraft.auxiliary.HandbookAuxData} — the input is drawn
     * at {@code (inX, inY)}, the output at {@code (outX, outY)}, no decoration between.
     */
    public void drawCompressor(GuiGraphicsExtractor render, int inX, int inY, ItemStack in, int outX, int outY, ItemStack out) {
        if (in != null && !in.isEmpty())
            api.drawItemStackWithTooltip(render, font(), in, inX, inY);
        if (out != null && !out.isEmpty())
            api.drawItemStackWithTooltip(render, font(), out, outX, outY);
    }

    /**
     * Fermenter: same single-in / single-out layout as compressor.
     */
    public void drawFermenter(GuiGraphicsExtractor render, int inX, int inY, ItemStack in, int outX, int outY, ItemStack out) {
        if (in != null && !in.isEmpty())
            api.drawItemStackWithTooltip(render, font(), in, inX, inY);
        if (out != null && !out.isEmpty())
            api.drawItemStackWithTooltip(render, font(), out, outX, outY);
    }

    /**
     * Blast furnace recipe: looks up the matching shaped/shapeless blast-furnace recipe
     * for the given {@code outputStack} and draws its ingredients + result.
     *
     * <p>Layout: up to nine ingredient slots in a 3x3 grid starting at ({@code inX}, {@code inY})
     * with 18-pixel cell pitch, then the output stack at ({@code outX}, {@code outY}).
     *
     * <p>If recipe iteration isn't available (dedicated multiplayer client), we silently
     * fall back to drawing the output stack alone — the handbook is informational, not
     * functional.
     */
    public void drawBlastFurnaceRecipe(GuiGraphicsExtractor render, int inX, int inY, int outX, int outY, Level level, ItemStack outputStack) {
        if (level == null || outputStack == null || outputStack.isEmpty()) return;

        Recipe<?> match = findMatchingBlastFurnaceRecipe(level, outputStack);
        if (match != null) {
            List<Ingredient> ings;
            try {
                ings = match.placementInfo().ingredients();
            } catch (Throwable ignored) {
                ings = null;
            }
            if (ings != null) {
                for (int i = 0; i < ings.size() && i < 9; i++) {
                    Ingredient ing = ings.get(i);
                    if (ing == null) continue;
                    ItemStack[] items = ing.items().map(h -> h.value().getDefaultInstance()).toArray(ItemStack[]::new);
                    if (items.length == 0) continue;
                    // Animate through valid matches every 1.5s so the player sees all options.
                    ItemStack pick = items[((int)(System.nanoTime() / 1_500_000_000L) & 0x7fffffff) % items.length];
                    int gx = inX + (i % 3) * 18;
                    int gy = inY + (i / 3) * 18;
                    api.drawItemStackWithTooltip(render, font(), pick, gx, gy);
                }
            }
        }
        api.drawItemStackWithTooltip(render, font(), outputStack, outX, outY);
    }

    /**
     * Tries to find a matching blast-furnace recipe via the integrated server's recipe
     * manager. Returns {@code null} on dedicated clients or if the lookup throws.
     */
    private static Recipe<?> findMatchingBlastFurnaceRecipe(Level level, ItemStack out) {
        try {
            MinecraftServer server = level.getServer();
            if (server == null) return null; // dedicated client — recipes not iterable
            var rm = server.getRecipeManager();
            Collection<RecipeHolder<? extends Recipe<?>>> shaped = (Collection)
                    rm.recipeMap().byType(RotaryRecipeTypes.BLAST_FURNACE_SHAPED.get());
            for (RecipeHolder<? extends Recipe<?>> rh : shaped) {
                Recipe<?> r = rh.value();
                if (r instanceof ShapedBlastFurnaceRecipe sb && ItemStack.isSameItemSameComponents(sb.getOutput(), out)) {
                    return sb;
                }
            }
            Collection<RecipeHolder<? extends Recipe<?>>> shapeless = (Collection)
                    rm.recipeMap().byType(RotaryRecipeTypes.BLAST_FURNACE_SHAPELESS.get());
            for (RecipeHolder<? extends Recipe<?>> rh : shapeless) {
                Recipe<?> r = rh.value();
                if (r instanceof ShapelessBlastFurnaceRecipe sb && ItemStack.isSameItemSameComponents(sb.getOutput(), out)) {
                    return sb;
                }
            }
        } catch (Throwable ignored) {
            // Recipe surface in 26.1 has shifted a few times; never let an exception escape into
            // the GUI draw pipeline. Worst case we fall through and the caller draws the output
            // alone — the handbook page still works, it just shows fewer details.
        }
        return null;
    }
}
