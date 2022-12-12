/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.production;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.loading.FMLLoader;
import org.jetbrains.annotations.NotNull;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.instantiable.HybridTank;
import reika.dragonapi.instantiable.data.blockstruct.BlockArray;
import reika.dragonapi.libraries.ReikaEntityHelper;
import reika.dragonapi.libraries.ReikaFluidHelper;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
import reika.rotarycraft.registry.*;

import java.util.List;

public class BlockEntityPump extends BlockEntityPowerReceiver implements PipeConnector, IFluidHandler, DiscreteFunction {

    public final static int CAPACITY = 24 * 1000;
    /**
     * Rate of conversion - one power++ = one tick-- per operation
     */
    public static final int FALLOFF = 256; //256W per 1 kPa
    private final BlockArray blocks = new BlockArray();
    private final HybridTank tank = new HybridTank("pump", CAPACITY);
    public int duplicationAmount;
    private int soundtick = 200;
    private int damage = 0;

    public BlockEntityPump(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.PUMP.get(), pos, state);
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.PUMP.get();
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        soundtick++;
        tickcount++;
//        this.getIOSides(world, pos);
        this.getPower(true);
        power = (long) omega * (long) torque;
        BlockState idbelow = world.getBlockState(worldPosition.below());
        if (idbelow == Blocks.AIR.defaultBlockState())
            return;
        Fluid f = ReikaFluidHelper.lookupFluidForBlock(idbelow);
        if (f == null)
            return;
        if (blocks.isEmpty()) {
            blocks.recursiveAddLiquidWithBounds(world, pos.getX(), pos.getY() - 1, pos.getZ(), pos.getX() - 16, pos.getY() - 2, pos.getZ() - 16, pos.getX() + 16, pos.getY() - 1, pos.getZ() + 16, f);
            blocks.reverseBlockOrder();
        }
        if (damage > 400)
            power = 0;
        //ReikaJavaLibrary.pConsole(FMLLoader.getDist()+" for "+blocks.getSize());
        if (blocks.isEmpty())
            return;
        if (power >= MINPOWER && torque >= MINTORQUE && this.getFluidLevel() < CAPACITY && tickcount >= this.getOperationTime()) {
//            int loc[] = this.findSourceBlock(world, pos);
            BlockPos loc = blocks.getNextAndMoveOn();
            ReikaJavaLibrary.pConsole(loc.getX()+"  "+loc.getY()+"  "+loc.getZ()+"  for side "+ FMLLoader.getDist());
            this.harvest(world, pos, loc);
            tickcount = 0;
            //ModLoader.getMinecraftInstance().ingameGUI.addChatMessage(String.format("%d", this.liquidID));
        }

        if (power > MINPOWER && torque >= MINTORQUE && soundtick >= 100) {
            soundtick = 0;
            SoundRegistry.PUMP.playSoundAtBlock(world, pos, 0.5F, 1);
        }
        if (power > MINPOWER && torque >= MINTORQUE)
            this.suckUpMobs(world, pos);
    }

    private void suckUpMobs(Level world, BlockPos pos) {
        AABB box = new AABB(pos.getX(), pos.getY() - 1, pos.getZ(), pos.getX() + 1, pos.getY(), pos.getZ() + 1);
        List<LivingEntity> inbox = world.getEntitiesOfClass(LivingEntity.class, box);
        for (LivingEntity e : inbox) {
            e.hurt(DamageSource.GENERIC, 5);
        }
        if (inbox.size() > 0 && !ReikaEntityHelper.allAreDead(inbox, false))
            damage++;
        if (damage >= 400)
            this.breakPump(world, pos);
    }

    public boolean isBroken() {
        return damage >= 400;
    }

    private void breakPump(Level world, BlockPos pos) {
//        world.playLocalSound(pos, "DragonAPI.rand.break", 1F, 1F);
//        for (int i = 0; i < 8; i++)
        ReikaItemHelper.dropItem(world, new BlockPos(pos.getX() + DragonAPI.rand.nextDouble(), pos.getY() + DragonAPI.rand.nextDouble(), pos.getZ() + DragonAPI.rand.nextDouble()), RotaryItems.HSLA_STEEL_SCRAP.get().getDefaultInstance());
    }

    public void harvest(Level world, BlockPos pos, BlockPos loc) {
        if (world.isClientSide)
            return;
        FluidStack fs = ReikaWorldHelper.getDrainableFluid(world, loc);
        if (fs == null)
            return;
        if (fs == null || !tank.canTakeIn(fs))
            return;
        Fluid f = fs.getFluid();
//        ModLoader.getMinecraftInstance().ingameGUI.addChatMessage(String.format("%d  %d  %d  %d", loc.xCoord, loc.yCoord, loc.zCoord, world.getBlock(loc.xCoord, loc.yCoord, loc.zCoord)));
        if (f != Fluids.LAVA)//|| !ReikaWorldHelper.is1p9InfiniteLava(world, loc))
            world.setBlock(loc, Blocks.AIR.defaultBlockState(), 1);
        int mult = 1;
        if (this.canMultiply(f)) {
            if (power / MINPOWER >= 16)
                mult *= 2;
            if (power / MINPOWER >= 64)
                mult *= 2;
            if (power / MINPOWER >= 256)
                mult *= 2;
            if (power / MINPOWER >= 1024)
                mult *= 2;
            if (power / MINPOWER >= 4096)
                mult *= 2;
        }
        if (f.equals(Fluids.WATER))
//            RotaryAdvancements.PUMP.triggerAchievement(this.getPlacer());
            duplicationAmount = (int) (mult * ConfigRegistry.FREEWATER.getValue());
        tank.addLiquid(fs.getAmount() * mult, f);
        world.blockUpdated(loc, world.getBlockState(pos).getBlock());
    }

    private boolean canMultiply(Fluid fluid) {
        return fluid.equals(Fluids.WATER);
    }

    public boolean isSource(Level world, BlockPos pos) {
        //ModLoader.getMinecraftInstance().ingameGUI.addChatMessage(String.format("%d, %d, %d, %d", x,y,z,(int)id));
        //ReikaWorldHelper.legacySetBlockWithNotify(world, pos, 49);
        Block liqid = world.getBlockState(pos).getBlock();
        if (!(/*liqid instanceof BlockFluidBase || */liqid instanceof LiquidBlock))
            return false;
//        boolean srcmeta = liqid instanceof BlockFluidFinite ? world.getBlockMetadata(pos) == 7 : world.getBlockMetadata(pos) == 0;
        Fluid f2 = ReikaFluidHelper.lookupFluidForBlock(liqid.defaultBlockState());
        FluidStack f = tank.getActualFluid();
        if (f2 == null)
            return false;
        boolean liq = f2.equals(f) || f == null;
        //ModLoader.getMinecraftInstance().ingameGUI.addChatMessage(String.valueOf(liq)+"  "+String.valueOf(dmg0));
        return /*srcmeta &&*/ liq;
    }

//    public void getIOSides(Level world, BlockPos pos) {
//        switch (metadata) {
//            case 1:
//                read = Direction.EAST;
//                read2 = Direction.WEST;
//                break;
//            case 0:
//                read = Direction.NORTH;
//                read2 = Direction.SOUTH;
//                break;
//        }
//    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        tank.writeToNBT(tag);
        tag.putInt("dmg", damage);
        super.writeSyncTag(tag);
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        tank.readFromNBT(tag);
        damage = tag.getInt("dmg");
        super.readSyncTag(tag);
    }

    @Override
    protected String getTEName() {
        return "pump";
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
        if (!this.isInWorld()) {
            phi = 0;
            return;
        }
        if (power < MINPOWER || torque < MINTORQUE)
            return;
        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.PUMP;
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return m.isStandardPipe();
    }

    @Override
    public boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
        return side.getStepY() == 0;
    }

    @Override
    public int fill(Direction from, FluidStack resource, FluidAction action) {
        return 0;
    }

    @Override
    public void onEMP() {
    }

//    @Override
//    public FluidStack drain(Direction from, FluidStack resource, boolean doDrain) {
//        return this.canDrain(from, resource.getFluid()) ? tank.drain(resource.amount, doDrain) : null;
//    }

    @Override
    public FluidStack drain(Direction from, int maxDrain, boolean doDrain) {
        if (from.getStepY() != 0)
            return null;
        return tank.drain(maxDrain, doDrain ? FluidAction.EXECUTE : FluidAction.SIMULATE);
    }


//    @Override
//    public boolean canDrain(Direction from, Fluid fluid) {
//        return from.getStepY() == 0 && ReikaFluidHelper.isFluidDrainableFromTank(fluid, tank);
//    }

    public int getFluidLevel() {
        return tank.getLevel();
    }

    public FluidStack getLiquid() {
        return tank.getActualFluid();
    }

    public void removeLiquid(int amt) {
        tank.removeLiquid(amt);
    }

    public void setEmpty() {
        tank.empty();
    }

    public void addLiquid(int amt, Fluid f) {
        tank.addLiquid(amt, f);
    }

    @Override
    public Flow getFlowForSide(Direction side) {
        return side.getStepY() == 0 ? Flow.OUTPUT : Flow.NONE;
    }

    @Override
    public int getOperationTime() {
        return DurationRegistry.PUMP.getOperationTime(omega);
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return null;
    }

    @Override
    public int getTankCapacity(int tank) {
        return 24000;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return false;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 1000;
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return null;
    }

    @NotNull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return null;
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return true;
    }
}
