/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.gui.container.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.IContainerFactory;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.IOMachineMenu;
import reika.rotarycraft.blockentities.transmission.BlockEntityGearbox;
import reika.rotarycraft.registry.RotaryMenus;

public class GearboxMenu extends IOMachineMenu<BlockEntityGearbox> {
    private final BlockEntityGearbox gearbox;

    //Client
    public GearboxMenu(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityGearbox) inv.player.level.getBlockEntity(data.readBlockPos()));
    }

    public GearboxMenu(int id, Inventory inv, BlockEntityGearbox te) {
        super(RotaryMenus.GEARBOX.get(), id, inv, te);
        gearbox = te;
        int posX = gearbox.getBlockPos().getX();
        int posY = gearbox.getBlockPos().getY();
        int posZ = gearbox.getBlockPos().getZ();
    }

    /**
     * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
     */
    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

/*todo        for (int i = 0; i < crafters.size(); i++) {
            ICrafting icrafting = (ICrafting) crafters.get(i);
            //icrafting.sendProgressBarUpdate(this, 1, gearbox.getLubricant());
            icrafting.sendProgressBarUpdate(this, 2, gearbox.getDamage());
            icrafting.sendProgressBarUpdate(this, 3, gearbox.omega);
            icrafting.sendProgressBarUpdate(this, 4, gearbox.torque);
        }*/

        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, gearbox, "tank");
    }

    @Override
    public void setData(int par1, int par2) {
        switch (par1) {
            case 1 -> gearbox.setLubricant(par2);
            case 2 -> gearbox.setDamage(par2);
            case 3 -> gearbox.omega = Math.max(0, par2);
            case 4 -> gearbox.torque = par2;
        }
    }

}
