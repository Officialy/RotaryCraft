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
import reika.rotarycraft.blockentities.level.BlockEntityAerosolizer;
import reika.rotarycraft.registry.RotaryMenus;

public class ContainerAerosolizer extends IOMachineContainer<BlockEntityAerosolizer> {
    private BlockEntityAerosolizer aerosolizer;

    //Client
    public ContainerAerosolizer(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityAerosolizer) inv.player.level.getBlockEntity(data.readBlockPos()));
        aerosolizer = (BlockEntityAerosolizer) inv.player.level.getBlockEntity(data.readBlockPos());
    }

    public ContainerAerosolizer(int id, Inventory player, BlockEntityAerosolizer par2BlockEntityAerosolizer) {
        super(RotaryMenus.AEROSOLIZER.get(), id, player, par2BlockEntityAerosolizer);
        aerosolizer = par2BlockEntityAerosolizer;
        int var3;
        int var4;

        for (var3 = 0; var3 < 3; ++var3) {
            for (var4 = 0; var4 < 3; ++var4) {
                this.addSlot(new Slot(par2BlockEntityAerosolizer, var4 + var3 * 3, 62 + var4 * 18, 17 + var3 * 18));
            }
        }
        //addSlot(new SlotFurnace(par1InventoryPlayer.player, par2BlockEntityAerosolizer, 2, 116, 35));

        this.addPlayerInventory(player);
    }

}
