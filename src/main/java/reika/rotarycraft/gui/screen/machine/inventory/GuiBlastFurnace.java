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

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.ScreenUtils;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.NonPoweredMachineScreen;
import reika.rotarycraft.blockentities.production.BlockEntityBlastFurnace;
import reika.rotarycraft.gui.container.machine.inventory.ContainerBlastFurnace;
import reika.rotarycraft.registry.PacketRegistry;

public class GuiBlastFurnace extends NonPoweredMachineScreen<BlockEntityBlastFurnace, ContainerBlastFurnace> {
    private final BlockEntityBlastFurnace blast;

    public GuiBlastFurnace(ContainerBlastFurnace container, Inventory inv, Component title) {
        super(container, inv, title);
        blast = (BlockEntityBlastFurnace) inv.player.level().getBlockEntity(container.tile.getBlockPos());
        imageWidth = 176;
        imageHeight = 166;
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;

        int u = blast.leaveLastItem ? 54 : 42;
        String tip = blast.leaveLastItem ? "Leave one item" : "Consume all items";
        int v = 96;
//        addRenderableWidget(new ImageButton(0, j + 124, k + 20, 12, 12, u, v, ResourceLocation.parse("Textures/GUI/buttons.png"), 16, 16, this::actionPerformed, Component.literal(tip)));
    }

    protected void actionPerformed(Button b) {
        blast.leaveLastItem = !blast.leaveLastItem;
        ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.BLASTLEAVEONE.ordinal(), blast, blast.leaveLastItem ? 1 : 0);
    }

    @Override
    protected void renderLabels(GuiGraphics poseStack, int a, int b) {
        super.renderLabels(poseStack, a, b);

        int c = 0;
        if (blast.getTemperature() >= 1000)
            c = 1;
        poseStack.drawString(font, blast.getTemperature() + "C", 17 + c, 6, 4210752, false);
    }

    //
//		/*
//		for (int i = 0; i < inventorySlots.inventorySlots.size(); i++) {
//			Slot s = (Slot)inventorySlots.inventorySlots.get(i);
//			if (s.inventory == blast && s.getClass() == Slot.class) {
//				int idx = s.slotNumber;
//				int clr = 0xff000000 | (blast.lockedSlots[idx] ? 0xff0000 : 0x00b000);
//				api.drawRectFrame(s.xDisplayPosition, s.yDisplayPosition, 16, 16, clr);
//			}
//		}
//		 */
//    }
//
    @Override
    protected void renderBg(GuiGraphics poseStack, float par1, int par2, int par3) {
        super.renderBg(poseStack, par1, par2, par3);
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;

        int i1 = (int) blast.getCookScaled(24);
        ScreenUtils.drawTexturedModalRect(poseStack, j + 119, k + 34, 176, 14, i1 + 1, 16, 0);
        int i2 = (int) blast.getTemperatureScaled(54);
        ScreenUtils.drawTexturedModalRect(poseStack, j + 11, k + 70 - i2, 176, 86 - i2, 10, i2, 0);

        /*for (int i = 0; i < menu.slots.size(); i++) {
            Slot s = menu.slots.get(i);
            if (s.container == blast && s.getClass() == Slot.class) {
                int idx = s.index;
                int clr = 0x50000000 | (blast.lockedSlots[idx] ? 0xff0000 : 0x00b000);
                poseStack.fill(j + s.x, k + s.y, j + s.x + 16, k + s.y + 16, clr);
            }
        }*/
    }

    @Override
    protected String getGuiTexture() {
        return "blastfurngui2";
    }
}
