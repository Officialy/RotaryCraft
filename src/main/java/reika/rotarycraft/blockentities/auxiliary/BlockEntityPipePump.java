///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.auxiliary;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraft.world.level.block.state.BlockState;
//import org.jetbrains.annotations.NotNull;
//import reika.rotarycraft.auxiliary.interfaces.PipeRenderConnector;
//import reika.rotarycraft.auxiliary.interfaces.PumpablePipe;
//import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
//import reika.rotarycraft.registry.MachineRegistry;
//
//public class BlockEntityPipePump extends BlockEntityPowerReceiver implements PipeRenderConnector {
//
//
//    public BlockEntityPipePump(BlockEntityType<?> type, BlockPos pos, BlockState state) {
//        super(type, pos, state);
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.PIPEPUMP;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    public void updateBlockEntity() {
//        super.updateBlockEntity();
//        this.getSummativeSidedPower();
//        //this.getIOSidesDefault(level, worldPosition);
//
//        if (this.tickcount == 0)
//            //ReikaWorldHelper.causeAdjacentUpdates(level, worldPosition);
//
//            if (power >= MINPOWER && omega >= MINSPEED) {
//                Direction dir = read.getOpposite();
//                Direction dir2 = dir.getOpposite();
//                BlockEntity te = getAdjacentBlockEntity(dir);
//                BlockEntity te2 = getAdjacentBlockEntity(dir2);
//                if (te instanceof PumpablePipe && te2 instanceof PumpablePipe) {
//                    PumpablePipe p1 = (PumpablePipe) te;
//                    PumpablePipe p2 = (PumpablePipe) te2;
//                    int lvl1 = p1.getFluidLevel();
//                    int lvl2 = p2.getFluidLevel();
//                    if (p2.canTransferTo(p1, dir)) {
//                        p1.transferFrom(p2, this.getTransferrableAmount(lvl2));
//                    }
//                }
//            }
//    }
//
//    private int getTransferrableAmount(int amt) {
//        return Math.min(amt, omega / 4);
//    }
//
//    @Override
//    public boolean canConnectToPipeOnSide(Direction dir) {
//        return dir == read || dir.getOpposite() == read;
//    }
//
//    @Override
//    public Block getBlockEntityBlockID() {
//        return null;
//    }
//
//    @Override
//    public void updateEntity(Level level, BlockPos blockPos) {
//
//    }
//
//    @Override
//    protected void animateWithTick(Level level, BlockPos blockPos) {
//
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return 0;
//    }
//
//    @Override
//    protected String getTEName() {
//        return null;
//    }
//
//    @Override
//    public boolean hasAnInventory() {
//        return false;
//    }
//
//    @Override
//    public boolean hasATank() {
//        return false;
//    }
//}
