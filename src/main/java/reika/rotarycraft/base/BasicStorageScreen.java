/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.base;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import reika.dragonapi.base.CoreContainer;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
import reika.rotarycraft.registry.PowerReceivers;

/**
 * 1.7.10 GuiBasicStorage, the GUI for any plain machine inventory with no screen of its own: a
 * chest-style "basicstorage" panel cut to the inventory's row count (rows*18+17 px of the top, then
 * the 96-px player half from v=126) and either the 24-px power-only tab or a full power tab whose
 * spacing shrinks with fewer rows. Its container must place the player rows as ContainerBasicStorage
 * did: {@code addPlayerInventoryWithOffset(inv, 0, 19 + (rows - 4) * 18)}.
 */
public abstract class BasicStorageScreen<E extends RotaryCraftBlockEntity, T extends CoreContainer<E>> extends MachineScreen<E, T> {

    private static final Identifier POWER_TAB = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/powertab.png");

    private final int inventoryRows;

    protected BasicStorageScreen(T container, Inventory inv, Component title, int slots) {
        super(container, inv, title, 176, 222 - 108 + (slots / 9) * 18);
        inventoryRows = slots / 9;
        inventory = inv;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partial) {
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;
        Identifier tex = this.getTextureIdentifier();
        int top = inventoryRows * 18 + 17;
        graphics.blit(RenderPipelines.GUI_TEXTURED, tex, j, k, 0, 0, imageWidth, top, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, tex, j, k + top, 0, 126, imageWidth, 96, 256, 256);
        if (this.isMinPowerOnly())
            this.drawPowerOnly(graphics, j, k);
        else if (tile instanceof BlockEntityPowerReceiver)
            this.drawPowerTab(graphics, j, k);
    }

    private boolean isMinPowerOnly() {
        PowerReceivers p = tile.getMachine().getPowerReceiverEntry();
        return p != null && p.isMinPowerOnly();
    }

    protected final void drawPowerOnly(GuiGraphicsExtractor graphics, int j, int k) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, POWER_TAB, imageWidth + j, k + 4, 0, 4, 42, 24, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, POWER_TAB, imageWidth + j, k + 4 + 23, 0, 157, 42, 6, 256, 256);
        long frac = Math.min(29, powerFrac(recv.power, recv.MINPOWER));
        graphics.blit(RenderPipelines.GUI_TEXTURED, POWER_TAB, imageWidth + j + 5, imageHeight + k - 53 - (imageHeight - 75), 0, 0, (int) frac, 4, 256, 256);
        api.drawCenteredStringNoShadow(graphics, font, "Power:", imageWidth + j + 20, k + 9, 0xff000000);
    }

    @Override
    protected void drawPowerTab(GuiGraphicsExtractor graphics, int j, int k) {
        int spacing = 60;
        int dy = -92;
        int u = 0;
        if (inventoryRows < 3) {
            dy -= 2;
            spacing -= -5 + 10 * (3 - inventoryRows);
            u = 169;
            if (inventoryRows == 1)
                u += 42;
        }
        graphics.blit(RenderPipelines.GUI_TEXTURED, POWER_TAB, imageWidth + j, k + 4, u, 4, 42, 145, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, POWER_TAB, imageWidth + j, k + 4 + 145, u, 157, 42, 6, 256, 256);
        long frac = Math.min(29, powerFrac(recv.power, recv.MINPOWER));
        graphics.blit(RenderPipelines.GUI_TEXTURED, POWER_TAB, imageWidth + j + 5, imageHeight + k + dy - inventoryRows * 18, 0, 0, (int) frac, 4, 256, 256);
        dy += spacing;
        frac = Math.min(29, powerFrac(recv.omega, recv.MINSPEED));
        graphics.blit(RenderPipelines.GUI_TEXTURED, POWER_TAB, imageWidth + j + 5, imageHeight + k + dy - inventoryRows * 18, 0, 0, (int) frac, 4, 256, 256);
        dy += spacing;
        frac = Math.min(29, powerFrac(recv.torque, recv.MINTORQUE));
        graphics.blit(RenderPipelines.GUI_TEXTURED, POWER_TAB, imageWidth + j + 5, imageHeight + k + dy - inventoryRows * 18, 0, 0, (int) frac, 4, 256, 256);
        dy = 9;
        api.drawCenteredStringNoShadow(graphics, font, "Power:", imageWidth + j + 20, k + dy, 0xff000000);
        dy += spacing;
        api.drawCenteredStringNoShadow(graphics, font, "Speed:", imageWidth + j + 20, k + dy, 0xff000000);
        dy += spacing;
        api.drawCenteredStringNoShadow(graphics, font, "Torque:", imageWidth + j + 20, k + dy, 0xff000000);
    }

    @Override
    protected Rect2i getPowerTabArea() {
        if (this.isMinPowerOnly())
            return new Rect2i(leftPos + imageWidth, topPos + 4, 42, 29);
        return tile instanceof BlockEntityPowerReceiver ? new Rect2i(leftPos + imageWidth, topPos + 4, 42, 151) : null;
    }

    @Override
    protected String getGuiTexture() {
        return "basicstorage";
    }
}
