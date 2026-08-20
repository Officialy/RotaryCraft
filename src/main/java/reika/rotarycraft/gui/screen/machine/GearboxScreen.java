/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.gui.screen.machine;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.base.NonPoweredMachineScreen;
import reika.rotarycraft.blockentities.transmission.BlockEntityGearbox;
import reika.rotarycraft.gui.container.machine.GearboxContainer;

public class GearboxScreen extends NonPoweredMachineScreen<BlockEntityGearbox, GearboxContainer> {
    private final BlockEntityGearbox gbx;

    public GearboxScreen(GearboxContainer container, Inventory inv, Component component) {
        super(container, inv, component, 176, 84);
        gbx = (BlockEntityGearbox) inv.player.level().getBlockEntity(container.tile.getBlockPos());
        inventory = inv;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor stack, int pX, int pY, float pPartialTick) {
        super.extractBackground(stack, pX, pY, pPartialTick);
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;

        int i2 = gbx.getLubricantScaled(55);
        int u = gbx.isLiving() ? 186 : 176;
        stack.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(), j + 24, imageHeight / 2 + k + 34 - i2, u, 126 - i2, 8, i2, 256, 256);

        // foreground read-outs (legacy drawGuiContainerForegroundLayer, gui-relative coords offset by j,k)
        String s = gbx.isLiving() ? "Mana" : "Lubricant";
        stack.text(font, s, j + 5, k + 12, 0xFF404040);

        stack.text(font, "Damage:", j + 68, k + 60, 0xFF000000);
        int damage = gbx.getDamagePercent();
        if (damage < 10)
            stack.text(font, String.format("%5d%s", damage, "%"), j + 122, k + 60, 0xFF00FF00);
        if (damage < 25 && damage >= 10)
            stack.text(font, String.format("%5d%s", damage, "%"), j + 122, k + 60, 0xFF55FF00);
        if (damage < 50 && damage >= 25)
            stack.text(font, String.format("%5d%s", damage, "%"), j + 122, k + 60, 0xFFFFFF00);
        if (damage < 80 && damage >= 50)
            stack.text(font, String.format("%5d%s", damage, "%"), j + 122, k + 60, 0xFFFF5500);
        if (damage >= 80)
            stack.text(font, String.format("%5d%s", damage, "%"), j + 122, k + 60, 0xFFFF0000);

        stack.text(font, "Ratio:", j + 80, k + 24, 0xFF000000);
        stack.text(font, "Mode:", j + 80, k + 36, 0xFF000000);
        stack.text(font, "Power:", j + 74, k + 48, 0xFF000000);

        stack.text(font, String.format("%5d ", gbx.getRatio()), j + 127, k + 24, 0xFF000000);
        if (gbx.reduction)
            stack.text(font, "Torque", j + 115, k + 36, 0xFF000000);
        else
            stack.text(font, " Speed", j + 115, k + 36, 0xFF000000);

        String pw = RotaryAux.formatPower(gbx.power);
        stack.text(font, pw, j + 150 - font.width(pw), k + 48, 0xFF000000);

    }

    /**
     * The lubricant read-out used to be drawn straight into {@code extractBackground}, so the
     * handbook buttons - which are widgets, extracted afterwards - painted over half of it.
     * {@code extractTooltip} runs after {@code extractContents} and hands the text to the tooltip
     * layer, which draws above everything.
     *
     * <p>Coordinates here are raw screen coordinates with no pose translate, so the hit box stays
     * absolute (j/k based) exactly as the legacy {@code api.isMouseInBox} check had it.
     */
    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractTooltip(graphics, mouseX, mouseY);
        if (gbx.isLiving())
            return;
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;
        if (api.isMouseInBox(j + 23, j + 32, k + 20, k + 76, mouseX, mouseY)) {
            graphics.setTooltipForNextFrame(font, Component.literal(
                    String.format("%.1f/%d", gbx.getLubricant() / 1000F, gbx.getMaxLubricant() / 1000)),
                    mouseX, mouseY);
        }
    }

    @Override
    protected String getGuiTexture() {
        return "gearboxgui";
    }
}
