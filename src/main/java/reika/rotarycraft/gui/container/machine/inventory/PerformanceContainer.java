/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.gui.container.machine.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import reika.rotarycraft.base.IOMachineContainer;
import reika.rotarycraft.blockentities.engine.BlockEntityPerformanceEngine;
import reika.rotarycraft.registry.RotaryMenus;

public class PerformanceContainer extends IOMachineContainer<BlockEntityPerformanceEngine> {
    private final BlockEntityPerformanceEngine engine;
    //Client
    public PerformanceContainer(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityPerformanceEngine) inv.player.level.getBlockEntity(data.readBlockPos()));
    }

    public PerformanceContainer(int id, Inventory inv, BlockEntityPerformanceEngine par2BlockEntityEngine) {
        super(RotaryMenus.PERFORMANCE_ENGINE.get(), id, inv, par2BlockEntityEngine);
        engine = par2BlockEntityEngine;
        int posX = engine.getBlockPos().getX();
        int posY = engine.getBlockPos().getY();
        int posZ = engine.getBlockPos().getZ();

        this.addSlot(new Slot(par2BlockEntityEngine, 0, 58, 36));
        this.addSlot(new Slot(par2BlockEntityEngine, 1, 103, 36));

        this.addPlayerInventory(inv);
    }

    /**
     * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
     */
    /*@Override
    public void broadcastChanges() {
        super.broadcastChanges();

        for (int i = 0; i < crafters.size(); i++) {
            ICrafting icrafting = (ICrafting) crafters.get(i);

            icrafting.sendProgressBarUpdate(this, 1, Engine.temperature);
            //icrafting.sendProgressBarUpdate(this, 2, Engine.getWater()/2);
            //icrafting.sendProgressBarUpdate(this, 3, Engine.getFuelLevel());
            icrafting.sendProgressBarUpdate(this, 4, Engine.additives);
        }
        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, Engine, "water");
        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, Engine, "fuel");
    }*/
    @Override
    public void setData(int par1, int par2) {
        switch (par1) {
            case 1 -> engine.temperature = par2;

            //case 2: Engine.setWater(par2*2); break;
            //case 3: Engine.setFuelLevel(par2); break;
            case 4 -> engine.additives = par2;
        }
    }

}
