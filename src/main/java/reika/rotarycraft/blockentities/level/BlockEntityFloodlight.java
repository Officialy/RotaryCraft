/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.level;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import reika.dragonapi.instantiable.data.blockstruct.BlockArray;
import reika.dragonapi.interfaces.block.SemiTransparent;
import reika.dragonapi.interfaces.blockentity.BreakAction;
import reika.dragonapi.libraries.ReikaDirectionHelper;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blockentity.BlockEntityBeamMachine;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.registry.*;

import java.util.ArrayList;

public class BlockEntityFloodlight extends BlockEntityBeamMachine implements RangedEffect, BreakAction {

    public static final int MAX_RANGE = Math.max(64, ConfigRegistry.FLOODLIGHTRANGE.getValue());
    private final BlockArray beam = new BlockArray();
    public boolean beammode = false;
    public boolean fresnel = false;
    private int lastRange = 0;

    public BlockEntityFloodlight(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.FLOODLIGHT.get(), pos, state);
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.FLOODLIGHT.get();
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getIOSides(world, pos, getBlockState().getValue(BlockRotaryCraftMachine.FACING));
        this.getPower(false);
        if (fresnel)
            beammode = false;
        if (power >= MINPOWER)
            RotaryAdvancements.FLOODLIGHT.triggerAchievement(this.getPlacer());
        power = (long) omega * (long) torque;
        if (!world.isClientSide) {
            if ((world.getDayTime() & 8) == 8) //almost unnoticeable light lag, but big FPS increase
                this.makeBeam(world, pos);
        }
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    protected void makeBeam(Level world, BlockPos pos) {
        //ReikaJavaLibrary.pConsole(lastRange+":"+this.getRange(), Dist.DEDICATED_SERVER);
        int r = this.getRange();
        if (lastRange != r) {
            RotaryCraft.LOGGER.debug("Updating " + this + " range from " + lastRange + " to " + r);
            //ReikaJavaLibrary.pConsole(beam);
            for (int i = 0; i < beam.getSize(); i++) {
                BlockPos c = beam.getNthBlock(i);
                Block b = world.getBlockState(c).getBlock();
                if (this.isLightBlock(b)) {
                    //ReikaJavaLibrary.pConsole(Arrays.toString(xyz));
                    world.setBlock(c, Blocks.AIR.defaultBlockState(), 3);
                    world.sendBlockUpdated(c, this.getBlockState(), this.getBlockState(), 3);
                }
            }
            beam.clear();
            if (r > 0) {
                if (fresnel) {
                    ArrayList<Direction> ds = ReikaDirectionHelper.getPerpendicularDirections(facing);
                    Direction d1 = ds.get(0);
                    Direction d2 = ds.get(1);
                    for (int d = 1; d <= r; d++) {
                        int w = (d - 1) / 3;
                        if (d > 1)
                            w++;
                        for (int a = -w; a <= w; a++) {
                            for (int b = -w; b <= w; b++) {
                                int dx = pos.getX() + facing.getStepX() * d + d1.getStepX() * a + d2.getStepX() * b;
                                int dy = pos.getY() + facing.getStepY() * d + d1.getStepY() * a + d2.getStepY() * b;
                                int dz = pos.getZ() + facing.getStepZ() * d + d1.getStepZ() * a + d2.getStepZ() * b;
                                beam.addIfClear(world, new BlockPos(dx, dy, dz));
                            }
                        }
                    }
                } else {
                    beam.addLineOfClear(world, pos, r, facing.getStepX(), facing.getStepY(), facing.getStepZ());
                }
            }
            lastRange = r;
        }

        for (int i = 0; i < beam.getSize(); i++) {
            BlockPos c = beam.getNthBlock(i);
            if (world.getBlockState(c).getBlock() == Blocks.AIR)
                world.setBlock(c, this.getPlacedBlockID().defaultBlockState(), 3);
            world.sendBlockUpdated(c, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    private Block getPlacedBlockID() {
        return /*todo beammode ? RotaryBlocks.BEAM.get() : */ Blocks.LIGHT;
    }

    private boolean isLightBlock(Block id) {
        return /*todo id == RotaryBlocks.BEAM.get() || */id == Blocks.LIGHT;
    }

    public void lightsOut(Level world, BlockPos pos) {
        world.sendBlockUpdated(pos, this.getBlockState(), this.getBlockState(), 3);
        world.updateNeighborsAt(pos, this.getBlockState().getBlock());
        for (int i = 0; i < beam.getSize(); i++) {
            BlockPos c = beam.getNthBlock(i);
            Block b = world.getBlockState(c).getBlock();
            if (this.isLightBlock(b)) {
                //ReikaJavaLibrary.pConsole(Arrays.toString(xyz));
                world.setBlock(c, Blocks.AIR.defaultBlockState(), 3);
                world.sendBlockUpdated(pos, this.getBlockState(), this.getBlockState(), 3);
                level.getLightEngine().checkBlock(worldPosition);
            }
        }
    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);
        tag.putBoolean("beam", beammode);
        tag.putBoolean("lens", fresnel);
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);
        beammode = tag.getBoolean("beam");
        fresnel = tag.getBoolean("lens");
    }

    @Override
    protected String getTEName() {
        return "floodlight";
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getRange() {
        //ReikaJavaLibrary.pConsole(r);
        if (power < MINPOWER)
            return 0;
        int ir = this.getMaxRange();
        for (int i = 1; i <= ir; i++) {
            int dx = worldPosition.getX() + i * facing.getStepX();
            int dy = worldPosition.getY() + i * facing.getStepY();
            int dz = worldPosition.getZ() + i * facing.getStepZ();
            Block b = level.getBlockState(new BlockPos(dx, dy, dz)).getBlock();
            if (b != Blocks.AIR) {
                if (b instanceof SemiTransparent sm) {
                    if (sm.isOpaque())
                        return i;
                } //else if (b.isOpaqueCube())
                //  return i;
            }
        }
        return ir;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.FLOODLIGHT;
    }

    @Override
    public int getMaxRange() {
        return fresnel ? 24 : MAX_RANGE;
    }

    @Override
    public void breakBlock() {
        this.lightsOut(level, worldPosition);
        if (fresnel) {
            ReikaItemHelper.dropItem(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, RotaryItems.LENS.get().getDefaultInstance());
        }
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

}
