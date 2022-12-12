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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import reika.dragonapi.base.CoreMenu;
import reika.rotarycraft.blockentities.transmission.BlockEntityGearbox;
import reika.rotarycraft.blockentities.weaponry.BlockEntityLandmine;
import reika.rotarycraft.registry.RotaryMenus;

public class LandmineMenu extends CoreMenu<BlockEntityLandmine> {

    public final BlockEntityLandmine landmine;

    //Client
    public LandmineMenu(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityLandmine) inv.player.level.getBlockEntity(data.readBlockPos()));
    }

    public LandmineMenu(int id, Inventory inv, BlockEntityLandmine te) {
        super(RotaryMenus.LANDMINE.get(), id, inv, te, null);
        this.landmine = te;
        Container ii = te;

        this.addSlot(new Slot(epInv, 0, 80, 34));

        this.addSlot(new Slot(ii, 1, 16, 25));
        this.addSlot(new Slot(ii, 2, 34, 25));
        this.addSlot(new Slot(ii, 3, 16, 43));
        this.addSlot(new Slot(ii, 4, 34, 43));

        this.addSlot(new Slot(ii, 5, 126, 25));
        this.addSlot(new Slot(ii, 6, 144, 25));
        this.addSlot(new Slot(ii, 7, 126, 43));
        this.addSlot(new Slot(ii, 8, 144, 43));

        this.addPlayerInventory(inv);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return false;
    }

}