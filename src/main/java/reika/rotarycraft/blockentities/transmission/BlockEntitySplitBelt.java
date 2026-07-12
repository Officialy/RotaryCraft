/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.transmission;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;

/**
 * Split belt hub: a belt hub whose DRIVER end also passes shaft power straight through -- power is
 * split between the through-shaft and the belt run (the receiving end gets the belt half). Wet
 * belts still quarter the limits.
 *
 * <p>26.2 port notes: uses the port BeltHub's splitting model (power/torque halved between the
 * belt and the pass-through side); the newest-legacy fixed-takeoff model (64 Nm skimmed to the
 * belt) is not ported.</p>
 */
public class BlockEntitySplitBelt extends BlockEntityBeltHub {

    public BlockEntitySplitBelt(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.SPLITBELT.get(), pos, state);
    }

    @Override
    public boolean isSplitting() {
        // The driver (non-emitting) end splits between the belt and its through-shaft.
        return !this.isEmitting();
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.SPLITBELT;
    }

    @Override
    public net.minecraft.world.level.block.Block getBlockEntityBlockID() {
        return RotaryBlocks.SPLITBELT.get();
    }

    @Override
    public int[] getBeltColor() {
        return new int[]{48, 96, 64};
    }

    @Override
    public ItemStack getBeltItem() {
        return RotaryItems.BELT.get().getDefaultInstance();
    }
}
