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

import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.MachineScreen;
import reika.rotarycraft.blockentities.weaponry.BlockEntityMachineGun;
import reika.rotarycraft.gui.container.machine.inventory.ContainerMachineGun;

/** 27-slot arrow magazine GUI on the shared basic-storage background. */
public class GuiMachineGun extends MachineScreen<BlockEntityMachineGun, ContainerMachineGun> {

    private final BlockEntityMachineGun gun;

    public GuiMachineGun(ContainerMachineGun container, Inventory inv, Component title) {
        super(container, inv, title, 176, 176);
        gun = (BlockEntityMachineGun) inv.player.level().getBlockEntity(container.tile.getBlockPos());
        inventory = inv;
    }

    @Override
    protected void drawPowerTab(GuiGraphicsExtractor stack, int j, int k) {
        var loc = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/powertab.png");
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j, k + 4, 0, 4, 42, imageHeight - 4, 256, 256);

        long frac = gun.MINPOWER > 0 ? (gun.power * 29L) / gun.MINPOWER : (gun.power > 0 ? 29 : 0);
        if (frac > 29) frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 144, 0, 0, (int) frac, 4, 256, 256);

        frac = gun.MINSPEED > 0 ? (gun.omega * 29L) / gun.MINSPEED : (gun.omega > 0 ? 29 : 0);
        if (frac > 29) frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 84, 0, 0, (int) frac, 4, 256, 256);

        frac = gun.MINTORQUE > 0 ? (gun.torque * 29L) / gun.MINTORQUE : (gun.torque > 0 ? 29 : 0);
        if (frac > 29) frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 24, 0, 0, (int) frac, 4, 256, 256);

        api.drawCenteredStringNoShadow(stack, font, "Power:", imageWidth + j + 20, k + 9, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Speed:", imageWidth + j + 20, k + 69, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Torque:", imageWidth + j + 20, k + 129, 0xff000000);
    }

    @Override
    protected String getGuiTexture() {
        return "basicstorage";
    }
}
