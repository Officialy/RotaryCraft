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

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.ScreenUtils;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.MachineScreen;
import reika.rotarycraft.blockentities.processing.BlockEntityGrinder;
import reika.rotarycraft.gui.container.machine.inventory.ContainerGrinder;
import reika.rotarycraft.registry.RotaryMenus;

public class GuiGrinder extends MachineScreen<BlockEntityGrinder, ContainerGrinder> {
    private final BlockEntityGrinder grin;

    public GuiGrinder(ContainerGrinder container, Inventory inv, Component title) {
        super(container, inv, title);
        grin = (BlockEntityGrinder) inv.player.level().getBlockEntity(container.tile.getBlockPos());
        inventory = inv;
    }

    @Override
    protected void renderBg(GuiGraphics poseStack, float par1, int mouseX, int mouseY) {
        super.renderBg(poseStack, par1, mouseX, mouseY);
        int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int scaleHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        int j = (scaledWidth - imageWidth) / 2;
        int k = (scaleHeight - imageHeight) / 2;

        int i1 = Math.min(48, grin.getCookProgressScaled(48));
        ScreenUtils.drawTexturedModalRect(poseStack, j + 99, k + 34, 176, 14, (i1) + 1, 16, 0);

        int i2 = grin.getLubricantScaled(55);
        int i3 = 0;
        if (i2 != 0)
            i3 = 1;
        ScreenUtils.drawTexturedModalRect(poseStack, j + 24, scaleHeight / 2 + k - 7 - i2, 176, 126 - i2, 8, i2, 0);


        //foreground
        poseStack.drawString(font, "Lubricant", j + 5, k + 11, 4210752);

        if (api.isMouseInBox(j + 23, j + 32, k + 20, k + 76, mouseX, mouseY)) {
            api.drawTooltipAt(poseStack, font, String.format("%d/%d", grin.getFluidLevel(), BlockEntityGrinder.MAXLUBE), mouseX, mouseY);
        }
    }

    @Override
    protected void drawPowerTab(GuiGraphics stack, int j, int k) {
        var loc = new ResourceLocation(RotaryCraft.MODID, "textures/screen/powertab.png");
        stack.blit(loc, width + k, k + 4, 0, 4, 42, height - 4);

        long frac = (grin.power * 29L) / grin.MINPOWER;
        if (frac > 29)
            frac = 29;
        stack.blit(loc, width + k + 5, height + k - 144, 0, 0, (int) frac, 4);

        frac = (int) (grin.omega * 29L) / grin.MINSPEED;
        if (frac > 29)
            frac = 29;
        stack.blit(loc, width + k + 5, height + k - 84, 0, 0, (int) frac, 4);

        frac = (int) (grin.torque * 29L) / grin.MINTORQUE;
        if (frac > 29)
            frac = 29;
        stack.blit(loc, width + k + 5, height + k - 24, 0, 0, (int) frac, 4);

        api.drawCenteredStringNoShadow(stack, font, "Power:", width + k + 20, k + 9, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Speed:", width + k + 20, k + 69, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Torque:", width + k + 20, k + 129, 0xff000000);
        //this.drawCenteredStringNoShadow(font, String.format("%d/%d", grin.power, grin.MINPOWER), imageWidth+k+16, k+16, 0xff000000);
    }

    @Override
    protected String getGuiTexture() {
        return "grindergui";
    }
}
