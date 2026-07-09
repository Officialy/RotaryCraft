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

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

import reika.dragonapi.base.CoreContainer;
import reika.rotarycraft.blockentities.production.BlockEntityBorer;
import reika.rotarycraft.registry.RotaryMenus;

/**
 * Menu for the Boring Machine's cut-shape editor. The borer has no item slots — just the 7x5 grid of
 * toggle cells and a drops on/off switch. Toggles come in as {@code clickMenuButton} presses (button id
 * = {@code col*ROWS + row} for a cell, {@link #BUTTON_DROPS} for the drops toggle, {@link #BUTTON_ALL}
 * / {@link #BUTTON_NONE} for select-all / clear), applied server-side to the BE.
 */
public class ContainerBorer extends CoreContainer<BlockEntityBorer> {

    public static final int BUTTON_DROPS = 100;
    public static final int BUTTON_ALL = 101;
    public static final int BUTTON_NONE = 102;

    private final BlockEntityBorer borer;

    // Client
    public ContainerBorer(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityBorer) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ContainerBorer(int id, Inventory inv, BlockEntityBorer te) {
        super(RotaryMenus.BORER.get(), id, inv, te);
        borer = te;
        this.addPlayerInventoryWithOffset(inv, 0, 32);
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id == BUTTON_DROPS) {
            borer.drops = !borer.drops;
        } else if (id == BUTTON_ALL) {
            for (boolean[] col : borer.cutShape)
                java.util.Arrays.fill(col, true);
        } else if (id == BUTTON_NONE) {
            for (boolean[] col : borer.cutShape)
                java.util.Arrays.fill(col, false);
        } else if (id >= 0 && id < BlockEntityBorer.COLS * BlockEntityBorer.ROWS) {
            int col = id / BlockEntityBorer.ROWS;
            int row = id % BlockEntityBorer.ROWS;
            borer.cutShape[col][row] = !borer.cutShape[col][row];
        } else {
            return false;
        }
        borer.setChanged();
        return true;
    }

    public BlockEntityBorer getBorer() {
        return borer;
    }
}
