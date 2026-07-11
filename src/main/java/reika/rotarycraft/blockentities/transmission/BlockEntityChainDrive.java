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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;

/**
 * Chain drive: the heavy-duty belt hub -- higher torque/speed limits, but overtorque snaps the
 * chain (resets the link) and overspeed destroys the hub outright (legacy behaviour).
 */
public class BlockEntityChainDrive extends BlockEntityBeltHub {

    public BlockEntityChainDrive(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.CHAIN.get(), pos, state);
    }

    @Override
    public int getTorque(int input) {
        input = super.getTorque(input);
        if (input > this.getMaxTorque()) {
            if (level != null)
                level.playSound(null, worldPosition, SoundEvents.ITEM_BREAK.value(), SoundSource.BLOCKS, 1F, 1F);
            this.reset();
            this.resetOther();
        }
        return this.isSplitting() ? input / 2 : input;
    }

    @Override
    public int getOmega(int input) {
        input = super.getOmega(input);
        if (input > this.getMaxSmoothSpeed()) {
            Level world = level;
            this.resetOther();
            if (world != null) {
                world.removeBlock(worldPosition, false);
                world.explode(null, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, 2F, Level.ExplosionInteraction.BLOCK);
            }
        }
        return input;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.CHAIN;
    }

    @Override
    public net.minecraft.world.level.block.Block getBlockEntityBlockID() {
        return RotaryBlocks.CHAIN.get();
    }

    @Override
    public int[] getBeltColor() {
        return new int[]{80, 80, 80};
    }

    @Override
    public ItemStack getBeltItem() {
        return RotaryItems.CHAIN_LINK.get().getDefaultInstance();
    }

    @Override
    public int getMaxTorque() {
        return 16384;
    }

    @Override
    public int getMaxSmoothSpeed() {
        return 65536;
    }
}
