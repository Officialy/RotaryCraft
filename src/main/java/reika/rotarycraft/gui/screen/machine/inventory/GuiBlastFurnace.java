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
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import reika.dragonapi.instantiable.gui.ImagedGuiButton;
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
    }

    @Override
    protected void init() {
        super.init();
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;

        int u = blast.leaveLastItem ? 54 : 42;
        String tip = blast.leaveLastItem ? "Leave one item" : "Consume all items";
        int v = 96;
        Identifier file = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/buttons.png");
        addRenderableWidget(new ImagedGuiButton(0, j + 124, k + 20, 12, 12, u, v, file, tip, 0xffffff, false, b -> actionPerformed(b)));
    }

    protected void actionPerformed(Button b) {
        blast.leaveLastItem = !blast.leaveLastItem;
        ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.BLASTLEAVEONE.ordinal(), blast, blast.leaveLastItem ? 1 : 0);
        this.init();
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor poseStack, int a, int b) {
        super.extractLabels(poseStack, a, b);

        int c = 0;
        if (blast.getTemperature() >= 1000)
            c = 1;
        poseStack.text(font, blast.getTemperature() + "C", 17 + c, 6, 4210752, false);
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
    public void extractBackground(GuiGraphicsExtractor poseStack, int par2, int par3, float par1) {
        super.extractBackground(poseStack, par2, par3, par1);
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;

        int i1 = (int) blast.getCookScaled(24);
        poseStack.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(), j + 119, k + 34, 176, 14, i1 + 1, 16, 256, 256);
        int i2 = (int) blast.getTemperatureScaled(54);
        poseStack.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(), j + 11, k + 70 - i2, 176, 86 - i2, 10, i2, 256, 256);

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
