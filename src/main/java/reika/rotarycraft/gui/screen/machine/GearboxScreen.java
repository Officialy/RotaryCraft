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

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.client.gui.ScreenUtils;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.base.NonPoweredMachineScreen;
import reika.rotarycraft.blockentities.transmission.BlockEntityGearbox;
import reika.rotarycraft.gui.container.machine.GearboxContainer;

public class GearboxScreen extends NonPoweredMachineScreen<BlockEntityGearbox, GearboxContainer> {
    private final BlockEntityGearbox gbx;

    public GearboxScreen(GearboxContainer container, Inventory inv, Component component) {
        super(container, inv, component);
        gbx = (BlockEntityGearbox) inv.player.level().getBlockEntity(container.tile.getBlockPos());
        inventory = inv;
        imageHeight = 84;
    }

    @Override
    public void render(GuiGraphics pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        drawGuiContainerForegroundLayer(pPoseStack, pMouseX, pMouseY);
    }

    //    @Override
    protected void drawGuiContainerForegroundLayer(GuiGraphics stack, int pMouseX, int pMouseY) {
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;

        String s = gbx.isLiving() ? "Mana" : "Lubricant";
        stack.drawString(font, s, 5, 12, 4210752);

        stack.drawString(font,  "Damage:", 68, 60, 0x000000);
        int damage = gbx.getDamagePercent();
        if (damage < 10)
            stack.drawString(font,  String.format("%5d%s", damage, "%"), 122, 60, 0x00ff00);
        if (damage < 25 && damage >= 10)
            stack.drawString(font,  String.format("%5d%s", damage, "%"), 122, 60, 0x55ff00);
        if (damage < 50 && damage >= 25)
            stack.drawString(font,  String.format("%5d%s", damage, "%"), 122, 60, 0xffff00);
        if (damage < 80 && damage >= 50)
            stack.drawString(font,  String.format("%5d%s", damage, "%"), 122, 60, 0xff5500);
        if (damage >= 80)
            stack.drawString(font,  String.format("%5d%s", damage, "%"), 122, 60, 0xff0000);

        stack.drawString(font,  "Ratio:", 80, 24, 0x000000);
        stack.drawString(font,  "Mode:", 80, 36, 0x000000);
        stack.drawString(font,  "Power:", 74, 48, 0x000000);

        stack.drawString(font,  String.format("%5d ", gbx.getRatio()), 127, 24, 0x000000);
        if (gbx.reduction)
            stack.drawString(font,  "Torque", 115, 36, 0x000000);
        else
            stack.drawString(font,  " Speed", 115, 36, 0x000000);

        String pw = RotaryAux.formatPower(gbx.power);
        stack.drawString(font,  pw, 150 - font.width(pw), 48, 0x000000);

       if (!gbx.isLiving() && api.isMouseInBox(j + 23, j + 32, k + 20, k + 76, pMouseX, pMouseY)) {
            int mx = pMouseX;
            int my = pMouseY;
            api.drawTooltipAt(stack, font, String.format("%.1f/%d", gbx.getLubricant() / 1000F, gbx.getMaxLubricant() / 1000), mx - j, my - k);
        }
    }

    @Override
    protected void renderBg(GuiGraphics poseStack, float par1, int par2, int par3) {
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;

        int i2 = gbx.getLubricantScaled(55);
        int i3 = 0;
        if (i2 != 0)
            i3 = 1;
        int u = gbx.isLiving() ? 186 : 176;
        ScreenUtils.drawTexturedModalRect(poseStack,j + 24, imageHeight / 2 + k + 34 - i2, u, 126 - i2, 8, i2, 0);
    }

    @Override
    protected String getGuiTexture() {
        return "gearboxgui";
    }
}
