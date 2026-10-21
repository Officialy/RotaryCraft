/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.gui.screen.machine.inventory;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import reika.dragonapi.libraries.rendering.ReikaLiquidRenderer;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.MachineScreen;
import reika.rotarycraft.blockentities.processing.BlockEntityLavaMaker;
import reika.rotarycraft.gui.container.machine.inventory.ContainerLavaMaker;

/** Rock-melter GUI (1.7.10 GuiRockMelter): 3x3 feed grid + the product tank gauge. */
public class GuiLavaMaker extends MachineScreen<BlockEntityLavaMaker, ContainerLavaMaker> {

    private final BlockEntityLavaMaker lava;

    public GuiLavaMaker(ContainerLavaMaker container, Inventory inv, Component title) {
        super(container, inv, title, 176, 166);
        lava = (BlockEntityLavaMaker) inv.player.level().getBlockEntity(container.tile.getBlockPos());
        inventory = inv;
    }

    /** 1.7.10 GuiRockMelter: the product tank fills the gauge at (134, 9..73), 64 px tall. */
    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partial) {
        super.extractBackground(graphics, mouseX, mouseY, partial);
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;
        if (lava.getFluidLevel() <= 0)
            return;
        int frac = lava.getFluidLevel() * 64 / lava.getCapacity();
        ReikaLiquidRenderer.drawFluidIcon(graphics, lava.getContainedFluid(), j + 134, k + 73 - frac + 1, 16, frac);
        if (api.isMouseInBox(j + 133, j + 151, k + 9, k + 75, mouseX, mouseY)) {
            String sg = String.format("%s: %d/%d mB", lava.getContainedFluid().getFluidType().getDescription().getString(),
                    lava.getFluidLevel(), lava.getCapacity());
            graphics.setTooltipForNextFrame(font, Component.literal(sg), mouseX, mouseY);
        }
    }

    @Override
    protected boolean inventoryLabelLeft() {
        return true;
    }

    @Override
    protected void drawPowerTab(GuiGraphicsExtractor stack, int j, int k) {
        var loc = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/powertab.png");
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j, k + 4, 0, 4, 42, imageHeight - 4, 256, 256);

        long frac = lava.MINPOWER > 0 ? (lava.power * 29L) / lava.MINPOWER : (lava.power > 0 ? 29 : 0);
        if (frac > 29) frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 144, 0, 0, (int) frac, 4, 256, 256);

        frac = lava.MINSPEED > 0 ? (lava.omega * 29L) / lava.MINSPEED : (lava.omega > 0 ? 29 : 0);
        if (frac > 29) frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 84, 0, 0, (int) frac, 4, 256, 256);

        frac = lava.MINTORQUE > 0 ? (lava.torque * 29L) / lava.MINTORQUE : (lava.torque > 0 ? 29 : 0);
        if (frac > 29) frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 24, 0, 0, (int) frac, 4, 256, 256);

        api.drawCenteredStringNoShadow(stack, font, "Power:", imageWidth + j + 20, k + 9, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Speed:", imageWidth + j + 20, k + 69, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Torque:", imageWidth + j + 20, k + 129, 0xff000000);
    }

    @Override
    protected String getGuiTexture() {
        return "lavamakergui";
    }
}
