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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.mathsci.ReikaPhysicsHelper;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.api.interfaces.CannonExplosive;
import reika.rotarycraft.base.blockentity.BlockEntityLaunchCannon;
import reika.rotarycraft.entities.EntityCustomTNT;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;

import java.util.List;

public class BlockEntityTNTCannon extends BlockEntityLaunchCannon {

    public static final double gTNT = 7.5;    //Calculated from PrimedTnt; vy -= 0.04, *0.98, 20x a sec

    public static final double torquecap = 32768D;

    public int selectedFuse;

    public BlockEntityTNTCannon(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.TNT_CANNON.get(), pos, state);
    }

    //private final ArrayList<PrimedTnt> fired = new ArrayList<>();

    //Make torque affect max incline angle, speed max distance

    @Override
    public int getMaxLaunchVelocity() {
        return (int) Math.sqrt(power / 67.5D);
    }

    @Override
    public int getMaxTheta() {
        if (torque > torquecap)
            return 90;
        int ang = 2 * (int) Math.ceil(Math.toDegrees(Math.asin(torque / torquecap)));
        if (ang > 90)
            return 90;
        return ang;
    }

    @Override
    public double getMaxLaunchDistance() {
        double v = this.getMaxLaunchVelocity();
        double vy = v * Math.sin(Math.toRadians(45));
        double t = vy / 9.81D;
        return t * vy; //vx = vy @ 45
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    //    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getSummativeSidedPower();

        if (DragonAPI.debugtest)
            ReikaInventoryHelper.addToIInv(Blocks.TNT, itemHandler);

        if (power < MINPOWER)
            return;
        tickcount++;
        if (tickcount < this.getOperationTime())
            return;
        tickcount = 0;
        if (targetMode)
            this.calcTarget(world, pos);
        int slot = this.canFire();
        if (slot >= 0)
            this.fire(world, pos, slot);
        //this.syncTNTData(world, pos);
        if (targetMode) {
            AABB box = new AABB(pos, new BlockPos(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1)).expandTowards(256, 256, 256);
            List<PrimedTnt> in = world.getEntitiesOfClass(PrimedTnt.class, box);
            for (PrimedTnt tnt : in) {
                if (!tnt.isOnGround()) {
                    //todo if this actually does : Nullify air resistance
//                    tnt.motionX /= 0.869800000190734863D;
//                    tnt.motionZ /= 0.869800000190734863D;
                    tnt.setDeltaMovement(0.869800000190734863D, 0, 0.869800000190734863D);

//      todo              if (!world.isClientSide)
//                        tnt.velocityChanged = true;
                } else {
//                    tnt.motionX = 0;
//                    tnt.motionZ = 0;
                    tnt.setDeltaMovement(0, 0, 0);
//todo                    if (!world.isClientSide)
//                        tnt.velocityChanged = true;
                }
            }
        }
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {

    }
	/*
	private void syncTNTData(Level world, BlockPos pos) {
		if (!world.isClientSide)
			return;
		Iterator<PrimedTnt> it = fired.iterator();
		while (it.hasNext()) {
			PrimedTnt tnt = it.next();
			if (tnt.tickCount < this.getMinFuse()) {
				//ReikaJavaLibrary.pConsole(tnt+":"+this.getSide());
				tnt.fuse = this.getFuseTime();
			}
			else {
				if (tnt.fuse < 0)
					tnt.kill();
				if (tnt.isAlive())
					it.remove();
			}
		}
	}*/

    private int getMinFuse() {
        return 5;
    }

    private void calcTarget(Level world, BlockPos pos) {
        double dx = target[0] - pos.getX() - 0.5;
        double dy = target[1] - pos.getY() - 1;
        double dz = target[2] - pos.getZ() - 0.5;
        double dl = ReikaMathLibrary.py3d(dx, 0, dz); //Horiz distance
        double g = 8.4695 * ReikaMathLibrary.doubpow(dl, 0.2701);
        if (dy > 0)
            g *= (0.8951 * ReikaMathLibrary.doubpow(dy, 0.0601));
        velocity = 10;
        theta = 0;
        while (theta <= 0) {
            velocity++;
            double s = ReikaMathLibrary.intpow(velocity, 4) - g * (g * dl * dl + 2 * dy * velocity * velocity);
            double a = velocity * velocity + Math.sqrt(s);
            theta = (int) Math.toDegrees(Math.atan(a / (g * dl)));
            phi = (int) Math.toDegrees(Math.atan2(dz, dx));
        }
    }

    protected int canFire() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack is = itemHandler.getStackInSlot(i);
            if (is != null) {
                if (ReikaItemHelper.matchStackWithBlock(is, Blocks.TNT.defaultBlockState()))
                    return i;
                if (is.getItem() instanceof CannonExplosive)
                    return i;
            }
        }
        return -1;
    }

    @Override
    protected boolean fire(Level world, BlockPos pos, int slot) {
        ItemStack in = itemHandler.getStackInSlot(slot);
        ReikaInventoryHelper.decrStack(slot, itemHandler);
        world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 0.7F + 0.3F * DragonAPI.rand.nextFloat() * 12, 0.1F * DragonAPI.rand.nextFloat(), false);
//        world.addParticle("hugeexplosion", pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1.0D, 0.0D, 0.0D);
        double dx = pos.getX() + 0.5;
        double dy = pos.getY() + 1.5 - 0.0625;
        double dz = pos.getZ() + 0.5;
        int fuse = this.getFuseTime();

        Entity tnt = null;
        if (ReikaItemHelper.matchStackWithBlock(in, Blocks.TNT.defaultBlockState())) {
            tnt = new EntityCustomTNT(world, new BlockPos(dx, dy, dz), null, fuse);
        } else if (in.getItem() instanceof CannonExplosive) {
            tnt = ((CannonExplosive) in.getItem()).getExplosiveEntity(in);
            tnt.setPos(dx, dy, dz);
            ((CannonExplosive.ExplosiveEntity) tnt).setFuse(fuse);
        }
        if (tnt == null) {
            RotaryCraft.LOGGER.error("Invalid item in TNT cannon yet firing was attempted!");
            return false;
        }

        double[] xyz = ReikaPhysicsHelper.polarToCartesian(velocity / 20D, theta, phi);
        tnt.setDeltaMovement(xyz[0], xyz[1], xyz[2]);
        if (!world.isClientSide) {
//            todo tnt.velocityChanged = true;
            world.addFreshEntity(tnt);
        }
        //fired.add(tnt);
        return true;
    }

    private int getFuseTime() {
        return targetMode ? 50 : Math.max(this.getMinFuse(), selectedFuse);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);

        selectedFuse = NBT.getInt("selfuse");
    }

    @Override
    protected String getTEName() {
        return null;
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);

        NBT.putInt("selfuse", selectedFuse);
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }


    @Override
    public AABB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.TNTCANNON;
    }

    //    @Override
    public boolean isItemValidForSlot(int slot, ItemStack is) {
        return ReikaItemHelper.matchStackWithBlock(is, Blocks.TNT.defaultBlockState());
    }

    @Override
    public int getRedstoneOverride() {
        if (this.canFire() == -1)
            return 15;
        return 0;
    }

    @Override
    public int getContainerSize() {
        return 9;
    }

}
