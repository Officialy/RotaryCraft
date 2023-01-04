/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.weaponry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import reika.dragonapi.instantiable.data.immutable.BlockKey;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.mathsci.ReikaPhysicsHelper;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.dragonapi.libraries.registry.ReikaParticleHelper;
import reika.rotarycraft.base.blockentity.BlockEntityLaunchCannon;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;

public class BlockEntityBlockCannon extends BlockEntityLaunchCannon {

    public BlockEntityBlockCannon(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.BLOCK_CANNON.get(), pos, state);
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.BLOCKCANNON;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    //@Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getSummativeSidedPower();
        tickcount++;
        if (power < MINPOWER)
            return;
        if (tickcount < this.getOperationTime())
            return;
        tickcount = 0;
        if (this.fire(world, pos, 0)) {
//            world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), "DragonAPI.rand.explode", SoundSource.BLOCKS, 1, 1);
            ReikaParticleHelper.EXPLODE.spawnAt(world, pos.getX(), pos.getY(), pos.getZ());
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
    protected String getTEName() {
        return "cannon";
    }

    private double getBlockMass(BlockKey bk) {
        return ReikaPhysicsHelper.getBlockDensity(bk.blockID.getBlock());
    }

    private int getReqTorque(BlockKey bk) {
        double m = this.getBlockMass(bk);
        int base = ReikaMathLibrary.ceil2exp((int) (velocity * m)) / 4;
        return base;
    }

    private BlockToFire getNextToFire() {
        for (int i = 0; i < inv.length; i++) {
            if (inv[i] != null) {
                if (ReikaItemHelper.isBlock(inv[i])) {
                    ItemStack is = inv[i].copy();
                    BlockKey bk = new BlockKey(is);
                    if (torque >= this.getReqTorque(bk)) {
                        return new BlockToFire(inv[i], bk, i);
                    }
                } /*else if (RotaryItems.SPAWNER.matchItem(inv[i])) {
                    ItemStack is = inv[i].copy();
                    BlockKey bk = new BlockKey(Blocks.SPAWNER);
                    if (torque >= this.getReqTorque(bk)) {
                        return new BlockToFire(is, bk, i);
                    }
                } */ else {
                    FluidStack fs = FluidUtil.getFluidContained(inv[i]).get();
                    if (fs != null) {
                        Fluid f = fs.getFluid();
//                        if (f.canBePlacedInWorld()) {
                        BlockKey bk = new BlockKey(f.defaultFluidState().createLegacyBlock());
                        if (torque >= this.getReqTorque(bk)) {
                            return new BlockToFire(inv[i], bk, i);
//                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private void dropItem(ItemStack is) {
        for (int i = 2; i < 6; i++) {
            Direction dir = Direction.values()[i];
            BlockEntity te = getAdjacentBlockEntity(dir);
            if (te instanceof Container) {
                if (ReikaInventoryHelper.addToIInv(is, (Container) te)) {
                    return;
                }
            }
        }
        ReikaItemHelper.dropItem(level, new BlockPos(worldPosition.getX() + 0.5, worldPosition.getY() + 1, worldPosition.getZ() + 0.5), is);
    }

    private void fireBlock(BlockToFire b, Level world, BlockPos pos) {
        FallingBlockEntity e = FallingBlockEntity.fall(world, new BlockPos(pos.getX() + 0.5, pos.getY() + 1 + 0.5, pos.getZ() + 0.5), b.toFire.blockID);
        if (b.toFire.blockID == Blocks.SPAWNER.defaultBlockState()) {
            SpawnerBlockEntity spw = new SpawnerBlockEntity(pos, b.toFire.blockID); //todo check if this works
            //ReikaSpawnerHelper.setSpawnerFromItemNBT(b.referenceItem, spw, true);
            CompoundTag nbt = new CompoundTag();
            spw.serializeNBT(); //swp.save(nbt);
            e.blockData = nbt;
        }
        double[] vel = ReikaPhysicsHelper.polarToCartesian(velocity / 20D, theta, phi);
        e.xo = vel[0];
        e.yo = vel[1];
        e.zo = vel[2];
        //e.shouldDropItem = false;
        e.time = -10000;
        if (!world.isClientSide)
            world.addFreshEntity(e);
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    protected boolean fire(Level world, BlockPos pos, int slot) {
        BlockToFire b = this.getNextToFire();
        if (b == null)
            return false;
        if (inv[b.inventorySlot] == null)
            return false;
        //ReikaJavaLibrary.pConsole(this.getReqTorque(next));
        ReikaInventoryHelper.decrStack(b.inventorySlot, inv);
        this.dropContainers(world, pos, b.referenceItem);
        this.fireBlock(b, world, pos);
        return true;
    }

    private void dropContainers(Level world, BlockPos pos, ItemStack next) {
//        if (todo FluidContainerRegistry.isFilledContainer(next)) {
//            ItemStack cont = FluidContainerRegistry.drainFluidContainer(next);
//            if (cont != null)
//                this.dropItem(cont);
//        }
    }

    @Override
    public int getMaxLaunchVelocity() {
        if (power < MINPOWER)
            return 0;
        return 1000;
    }

    @Override
    public int getMaxTheta() {
        if (power < MINPOWER)
            return 0;
        return 1000;
    }

    @Override
    public double getMaxLaunchDistance() {
        if (power < MINPOWER)
            return 0;
        return 1000;
    }

//    @Override
//    public boolean isItemValid(int slot, @NotNull ItemStack is) {
//        if (ReikaItemHelper.isBlock(is))
//            return true;
//        return RotaryItems.SPAWNER.matchItem(is) || FluidContainerRegistry.getFluidForFilledItem(is) != null;
//    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getItem(int pIndex) {
        return null;
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        return null;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        return null;
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {

    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return false;
    }

    @Override
    public void clearContent() {

    }

    private static class BlockToFire {

        private final ItemStack referenceItem;
        private final BlockKey toFire;
        private final int inventorySlot;

        private BlockToFire(ItemStack is, BlockKey bk, int s) {
            referenceItem = is;
            toFire = bk;
            inventorySlot = s;
        }

    }

}