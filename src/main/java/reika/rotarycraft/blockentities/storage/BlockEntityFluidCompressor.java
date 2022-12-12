///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.storage;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.material.Fluid;
//import net.minecraft.world.level.material.Fluids;
//import net.minecraftforge.fluids.FluidStack;
//import net.minecraftforge.fluids.capability.IFluidHandler;
//import org.jetbrains.annotations.NotNull;
//import reika.dragonapi.instantiable.HybridTank;
//import reika.dragonapi.libraries.ReikaNBTHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.rotarycraft.auxiliary.interfaces.NBTMachine;
//import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
//import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
//import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.RotaryFluids;
//
//import java.util.ArrayList;
//
//public class BlockEntityFluidCompressor extends BlockEntityPowerReceiver implements IFluidHandler, PipeConnector, NBTMachine {
//
//    private static final ArrayList<FluidStack> creativeFluids = new ArrayList<>();
//    private final HybridTank tank = new HybridTank("gastank", 1000000000);
//    private final int timer = 0;
//
//    public static void initCreativeFluids() {
//        creativeFluids.clear();
////        addCreativeFluid("hydrofluoric acid");
////        addCreativeFluid("uranium hexafluoride");
//        addCreativeFluid(new FluidStack(RotaryFluids.AMMONIA.get(), -1));
//        addCreativeFluid(new FluidStack(RotaryFluids.CHLORINE.get(), -1));
////        addCreativeFluid("rc co2");
//        addCreativeFluid(new FluidStack(RotaryFluids.OXYGEN.get(), -1));
////        addCreativeFluid("rc deuterium");
////        addCreativeFluid("rc tritium");
//    }
//
//    private static void addCreativeFluid(FluidStack fluid) {
//        FluidStack f = fluid;
//        if (f != null)
//            creativeFluids.add(f);
//    }
//
//    public int getCapacity() {
//        return tank.isEmpty() ? 0 : this.getCapacity(tank.getActualFluid().getFluid());
//    }
//
//    public int getCapacity(Fluid f) {
//        if (power < MINPOWER || torque < MINTORQUE)
//            return 0;
//        int log2 = (int) (ReikaMathLibrary.logbase(torque, 2) / 2);
//        long power = ReikaMathLibrary.longpow(10, log2);
//        int factor = f.getAttributes().isGaseous() ? 8 : 1;
//        long frac = factor * (power / 40);
//        return (int) Math.min(frac, tank.getCapacity());
//    }
//
//    public FluidStack getFluid() {
//        return tank.getActualFluid();
//    }
//
//    public boolean isEmpty() {
//        return tank.isEmpty();
//    }
//
//    public int getFluidLevel() {
//        return tank.getLevel();
//    }
//
//    @Override
//    public Block getBlockEntityBlockID() {
//        return null;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        this.getPowerBelow();/*
//		if (this.getLevel() > this.getCapacity()) {
//			timer++;
//			//ReikaJavaLibrary.pConsole(timer, Dist.DEDICATED_SERVER);
//			if (timer > 400) {
//				world.setBlockToAir(pos);
//				world.explode(null, x+0.5, y+0.5, z+0.5, 8F, RotaryConfig.COMMON.BLOCKDAMAGE.getState());
//			}
//			else if (timer > 0) {
//				if (DragonAPI.rand.nextInt(410-timer) < 10) {
//					ReikaSoundHelper.playSoundAtBlock(world, pos, "DragonAPI.rand.fizz");
//					ReikaParticleHelper.SMOKE.spawnAroundBlock(world, pos, 8);
//				}
//			}
//		}
//		else
//			timer = 0;*/
//        //ReikaJavaLibrary.pConsole(tank.getLevel()/1000D+"/"+this.getCapacity()/1000D, Dist.DEDICATED_SERVER);
//    }
//
//    @Override
//    protected void animateWithTick(Level level, BlockPos blockPos) {
//
//    }
//
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.GASTANK;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return 0;
//    }
//
//    @Override
//    public int fill(Direction from, FluidStack resource, boolean doFill) {
//        if (resource.getFluid() == null)
//            return 0;
//        int toadd = Math.min(resource.getAmount(), this.getCapacity(resource.getFluid()) - tank.getLevel());
//        if (toadd <= 0)
//            return 0;
//        FluidStack fs = new FluidStack(resource.getFluid(), toadd);
//        return this.canFill(from, resource.getFluid()) ? tank.fill(fs, doFill) : 0;
//    }
//
//    @Override
//    public FluidStack drain(Direction from, FluidStack resource, boolean doDrain) {
//        return this.canDrain(from, resource.getFluid()) ? tank.drain(resource.getAmount(), doDrain) : null;
//    }
//
//    @Override
//    public FluidStack drain(Direction from, int maxDrain, boolean doDrain) {
//        return tank.drain(maxDrain, doDrain);
//    }
//
//    @Override
//    public boolean canFill(Direction from, Fluid fluid) {
//        return true;//fluid.isGaseous();
//    }
//
//    @Override
//    public boolean canDrain(Direction from, Fluid fluid) {
//        return ReikaFluidHelper.isFluidDrainableFromTank(fluid, tank);
//    }
//
//    @Override
//    public boolean canConnectToPipe(MachineRegistry m) {
//        return m.isStandardPipe() || m == MachineRegistry.HOSE || m == MachineRegistry.FUELLINE || m == MachineRegistry.VALVE;
//    }
//
//    @Override
//    public boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
//        return this.canConnectToPipe(p);
//    }
//
//    @Override
//    public int fill(Direction from, FluidStack resource, FluidAction action) {
//        return 0;
//    }
//
//    @Override
//    public Flow getFlowForSide(Direction side) {
//        return side == Direction.UP ? Flow.OUTPUT : Flow.INPUT;
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        tank.load(NBT);
//    }
//
//    @Override
//    protected String getTEName() {
//        return null;
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//
//        tank.saveAdditional(NBT);
//    }
//
//    @Override
//    public CompoundTag getTagsToWriteToStack() {
//        if (this.isEmpty())
//            return null;
//        CompoundTag NBT = new CompoundTag();
//        FluidStack f = this.getFluid();
//        int level = this.getFluidLevel();
//        ReikaNBTHelper.writeFluidToNBT(NBT, f);
//        NBT.putInt("lvl", level);
//        return NBT;
//    }
//
//    @Override
//    public void setDataFromItemStackTag(CompoundTag NBT) {
//        if (NBT == null) {
//            tank.empty();
//            return;
//        }
//        FluidStack f = ReikaNBTHelper.getFluidFromNBT(NBT);
//        int level = NBT.getInt("lvl");
//        if (f != null && level > 0)
//            tank.setContents(level, f.getFluid());
//    }
//
//    public ArrayList<CompoundTag> getCreativeModeVariants() {
//        ArrayList<CompoundTag> li = new ArrayList<>();
//        li.add(null);
//        for (int i = 0; i < creativeFluids.size(); i++) {
//            CompoundTag nbt = new CompoundTag();
//            nbt.putInt("lvl", 1000000000);
//            ReikaNBTHelper.writeFluidToNBT(nbt, creativeFluids.get(i));
//            li.add(nbt);
//        }
//        return li;
//    }
//
//    public ArrayList<String> getDisplayTags(CompoundTag nbt) {
//        ArrayList li = new ArrayList<>();
//        FluidStack f = ReikaNBTHelper.getFluidFromNBT(nbt);
//        if (f != null) {
//            String fluid = f.getDisplayName().getString();
//            int amt = nbt.getInt("lvl");
//            if (amt > 0) {
//                String amount = String.format("%d", amt / 1000);
//                String contents = "Contents: " + amount + "B of " + fluid;
//                li.add(contents);
//            }
//        }
//        return li;
//    }
//
//    @Override
//    public int getTanks() {
//        return 0;
//    }
//
//    @NotNull
//    @Override
//    public FluidStack getFluidInTank(int tank) {
//        return null;
//    }
//
//    @Override
//    public int getTankCapacity(int tank) {
//        return 0;
//    }
//
//    @Override
//    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
//        return false;
//    }
//
//    @Override
//    public int fill(FluidStack resource, FluidAction action) {
//        return 0;
//    }
//
//    @NotNull
//    @Override
//    public FluidStack drain(FluidStack resource, FluidAction action) {
//        return null;
//    }
//
//    @NotNull
//    @Override
//    public FluidStack drain(int maxDrain, FluidAction action) {
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
