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

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.GuiLayer;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.api.interfaces.Fillable;
import reika.rotarycraft.items.tools.ItemJetPack;

/**
 * 26.1 HUD overlay that draws a fuel bar above the experience-level layer while the player
 * is wearing an {@link ItemJetPack}. Replaces the legacy {@code RenderGuiOverlayEvent} hook
 * (gone with the VanillaGuiOverlay API) — overlays are now registered via
 * {@link RegisterGuiLayersEvent} as {@link GuiLayer}s.
 */
@EventBusSubscriber(modid = RotaryCraft.MODID, value = Dist.CLIENT)
public final class JetpackFuelOverlay implements GuiLayer {

    public static final JetpackFuelOverlay instance = new JetpackFuelOverlay();
    private static final Identifier ID = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "jetpack_fuel");

    /** Width of the fuel bar in pixels (matches the vanilla EXP bar width). */
    private static final int BAR_WIDTH = 182;
    /** Bar height in pixels. */
    private static final int BAR_HEIGHT = 5;

    private JetpackFuelOverlay() {}

    @SubscribeEvent
    public static void register(RegisterGuiLayersEvent event) {
        // Draw above the experience-level layer so the bar sits in the same vertical strip.
        // 26.1 renamed the old EXPERIENCE_BAR layer; EXPERIENCE_LEVEL is the closest equivalent.
        event.registerAbove(VanillaGuiLayers.EXPERIENCE_LEVEL, ID, instance);
    }

    @Override
    public void render(GuiGraphicsExtractor gui, DeltaTracker delta) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        if (!(chest.getItem() instanceof ItemJetPack jp) || !(jp instanceof Fillable fillable)) return;

        int capacity = fillable.getCapacity(chest);
        if (capacity <= 0) return;
        int current = fillable.getCurrentFillLevel(chest);
        float ratio = Math.max(0F, Math.min(1F, current / (float) capacity));

        int screenW = gui.guiWidth();
        int screenH = gui.guiHeight();
        int x = (screenW - BAR_WIDTH) / 2;
        // Sit just above the EXP bar (which is at screenH - 32 -ish). We offset by 6 px so the
        // jetpack bar doesn't visually merge with vanilla experience.
        int y = screenH - 38;

        int filled = Math.round(BAR_WIDTH * ratio);
        // Background frame
        gui.fill(x - 1, y - 1, x + BAR_WIDTH + 1, y + BAR_HEIGHT + 1, 0xff202020);
        gui.fill(x, y, x + BAR_WIDTH, y + BAR_HEIGHT, 0xff404040);
        // Fill: orange when low (<25%), green otherwise.
        int colour = ratio < 0.25F ? 0xffd24f1f : 0xff3fbf3f;
        gui.fill(x, y, x + filled, y + BAR_HEIGHT, colour);

        // Compact numeric readout to the right of the bar.
        String label = current + " / " + capacity + " mB";
        gui.text(mc.font, Component.literal(label), x + BAR_WIDTH + 4, y - 1, 0xffffffff, true);
    }
}
