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
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.base.IOMachineContainer;
import reika.rotarycraft.blockentities.transmission.BlockEntityAdvancedGear;
import reika.rotarycraft.registry.RotaryMenus;


public class ContainerCVT extends IOMachineContainer<BlockEntityAdvancedGear> {
    private final BlockEntityAdvancedGear cvt;

    //Client
    public ContainerCVT(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityAdvancedGear) inv.player.level.getBlockEntity(data.readBlockPos()));
    }

    public ContainerCVT(int id, Inventory playerInv, BlockEntityAdvancedGear par2BlockEntityAdvancedGear) {
        super(RotaryMenus.CVT.get(), id, playerInv, par2BlockEntityAdvancedGear);
        cvt = par2BlockEntityAdvancedGear;

        int x = 8;
        int y = 11;
        int k = 0;
        int a = 0;
        int b = 0;
        for (int i = 0; i < ReikaMathLibrary.logbase(32, 2); i++) {
            for (int j = 0; j < ReikaMathLibrary.intpow(2, i); j++) {
                if (k > 22) {
                    a = -144;
                    b = 18;
                }
                this.addSlot(new Slot((Container) cvt, k, x + j * 18 + a, y + i * 26 + b)); //todo check casting
                k++;
            }
        }
        int dx = 31;
        int dy = 77;
        for (int i = 0; i < 3; i++) {
            for (k = 0; k < 9; k++) {
                this.addSlot(new Slot(playerInv, k + i * 9 + 9, 8 + k * 18 + dx, 84 + i * 18 + dy));
            }
        }
        dy -= 4;
        for (int j = 0; j < 9; j++) {
            this.addSlot(new Slot(playerInv, j, 8 + j * 18 + dx, 142 + dy));
        }
    }
}
