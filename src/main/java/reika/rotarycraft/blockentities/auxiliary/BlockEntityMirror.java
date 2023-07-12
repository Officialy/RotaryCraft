/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.auxiliary;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLLoader;
import reika.dragonapi.libraries.ReikaEntityHelper;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.mathsci.ReikaPhysicsHelper;
import reika.rotarycraft.auxiliary.SolarPlant;
import reika.rotarycraft.auxiliary.interfaces.SolarPlantBlock;
import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;

import java.util.List;

public class BlockEntityMirror extends RotaryCraftBlockEntity implements SolarPlantBlock {

    //2.3 kW/m^2 (392MW/170000) -> 2kW/block; sunlight is 15 kW per m^2, so thus efficiency of 13%


    public float theta;
    public boolean broken;

    private float targetTheta;

    private float targetPhi;
    private boolean rotatingLarge;

    private float aimFactor = 1;
    private float lastAimFactor;

    private SolarPlant plant;

    public BlockEntityMirror(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.MIRROR.get(), pos, state);
    }

    public void searchForPlant(Level world, BlockPos pos) {
        if (plant != null)
            return;
        plant = SolarPlant.build(world, pos);
    }

    public SolarPlant getPlant() {
        return plant;
    }

    public void setPlant(SolarPlant p) {
        plant = p;
    }


    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.MIRROR;
    }

    public float getAimingAccuracy() {
        return aimFactor;
    }


    public void updateBlockEntity(Level world, BlockPos pos) {
        if (broken)
            return;

        this.searchForPlant(world, pos);

        if (world.isClientSide && (this.tickcount < 400 || rotatingLarge || Math.abs(world.getDayTime() % 8) == Math.abs(System.identityHashCode(this) % 8))) {
            this.adjustAim(world, pos);
        }

        if (!world.isClientSide) {
            AABB above = new AABB(pos.getX() + 0.25, pos.getY() + 1, pos.getZ() + 0.25, pos.getX() + 0.75, pos.getY() + 1.5, pos.getZ() + 0.75);
            List<Entity> in = world.getEntitiesOfClass(Entity.class, above);
            for (Entity e : in) {
                if (ReikaEntityHelper.isSolidEntity(e)) {
                    double m = ReikaEntityHelper.getEntityMass(e);
                    //ReikaJavaLibrary.pConsole(m+" kg moving at "+e.motionY+" b/s, E: "+(m-e.motionY*20));
                    if (e.yo < -0.1 && m - e.yo * 20 > 80) { //todo check motion, was motionY
                        //ReikaPacketHelper.sendUpdatePacket(RotaryCraft.packetChannel, PacketRegistry.MIRROR.ordinal(), this, new PacketTarget.RadiusTarget(this, 32));
                        e.hurt(e.damageSources().cactus(), 1);
                        this.breakMirror(world, pos);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    public boolean isFunctional() {
        if (broken)
            return false;
        if (MachineRegistry.getMachine(level, worldPosition.above()) != null)
            return false;
		/*
		if (!level.canBlockSeeTheSky(xCoord, yCoord, zCoord))
			return false;
		 */
        //if (level.getPrecipitationHeight(xCoord, zCoord) > yCoord+1)
        //	return false;
        return plant != null && /*plant.canSeeTheSky(level, xCoord, yCoord, zCoord)*/ plant.canSeeTheSky(this);
    }


    private void adjustAim(Level world, BlockPos pos) {
        if (plant == null)
            return;

        BlockPos target = plant.getAimingPositionForMirror(this);
        if (target == null)
            return;

        float finalphi;
        float finaltheta;

        long tot = world.getDayTime();
        int time = (int) (tot % 12000);

        time = this.forceDuskDawnAiming(tot, time);

        float sunphi = time >= 6000 ? -90 : 90;
        float suntheta = ReikaWorldHelper.getSunAngle(world);

        //rises in +90 sets in 270 (+x, -x)
        float movespeed = 0.5F;

        double[] angs = ReikaPhysicsHelper.cartesianToPolar(pos.getX() - target.getX(), pos.getY() - target.getY(), pos.getX() - target.getZ());
        float targetphi = (float) angs[2];
        float targettheta = (float) angs[1];

        targettheta = Math.abs(targettheta) - 90;
        targettheta *= 0.5;

        sunphi = this.clampPhi(sunphi, time);
        boolean bool = time >= 6000 || targetphi > 270;

        //ReikaJavaLibrary.pConsole(targetphi+" clamped to "+this.clampPhi(targetphi, time)+"  :  "+bool);
        if (bool)
            targetphi = this.clampPhi(targetphi, time);

        if (time >= 6000) {
            finalphi = sunphi - (sunphi - targetphi) / 2F;
        } else {
            finalphi = sunphi + (targetphi - sunphi) / 2F; //These are mathematically equivalent...
        }

        float sunangle = time >= 6000 ? (float) (1 - Math.cos(Math.toRadians((time - 6000) * 90D / 6000D))) : (float) Math.cos(Math.toRadians(time * 90D / 6000D));


        finalphi = (finalphi * sunangle + (1 - sunangle) * targetphi);
        finalphi = this.clampPhi(finalphi, time);

        finaltheta = targettheta + (suntheta - targettheta) / 2F;

        //ReikaJavaLibrary.pConsole(targetphi);
        if (!(targetphi >= 0 && targetphi <= 90) && time >= 6000) {
            finalphi = -sunphi - (sunphi - targetphi) / 2F;
            finalphi = (finalphi * sunangle + (1 - sunangle) * targetphi);
        }

        finalphi = this.adjustPhiForClosestPath(finalphi);
        if (Math.abs(sunphi - targetphi) == 180) {
            //ReikaJavaLibrary.pConsole(x+", "+y+", "+z);
            finalphi = targetphi;
            finaltheta = Math.max(60 - suntheta, finaltheta);
        }

        if (finalphi - phi > 180)
            finalphi -= 360;

        //ReikaJavaLibrary.pConsole(String.format("TIME: %d     SUN: %.3f    TARGET: %.3f     FINAL: %.3f", time, sunphi, targetphi, finalphi));

        targetTheta = finaltheta;
        targetPhi = finalphi;

        if (phi < targetPhi)
            phi += movespeed;
        if (phi > targetPhi)
            phi -= movespeed;

        if (theta < targetTheta)
            theta += movespeed;
        if (theta > targetTheta)
            theta -= movespeed;

        float aim = (float) Math.max(0, 1 - ReikaMathLibrary.py3d(theta - targetTheta, 0, phi - targetPhi) / 20D);
        if (Math.abs(aimFactor - aim) > 0.05) {
            lastAimFactor = aimFactor;
            aimFactor = aim;
            //ReikaPacketHelper.sendSyncPacket(RotaryCraft.packetChannel, this, "aimFactor", true);
        }

        //ReikaJavaLibrary.pConsole(targetPhi+":"+phi);
        if (rotatingLarge) {
            rotatingLarge = Math.abs(targetPhi - phi) > 2;
        } else {
            rotatingLarge = Math.abs(targetPhi - phi) > 10;
        }
    }

    private int forceDuskDawnAiming(long tot, int time) {
        int day = (int) (tot % 24000);
        if (ReikaMathLibrary.isValueInsideBoundsIncl(12000, 13000, day))
            return 11999;
        if (ReikaMathLibrary.isValueInsideBoundsIncl(23000, 24000, day))
            return 0;
        return time;
    }

    public void breakMirror(Level world, BlockPos pos) {
        broken = true;
        if (FMLLoader.getDist() == Dist.CLIENT) {
            //ReikaRenderHelper.addModelledBlockParticles("/Reika/RotaryCraft/Textures/BlockEntityTex/", world, pos, this.getMachine().getBlockState(), Minecraft.getInstance().effectRenderer, ReikaJavaLibrary.makeListFrom(new double[]{0, 0, 1, 1}), RotaryCraft.class);
        }
        //ReikaSoundHelper.playBreakSound(world, pos, Blocks.GLASS);
    }

    public void repair(Level world, BlockPos pos) {
        broken = false;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    @Override
    public void updateEntity(Level level, BlockPos blockPos) {

    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putBoolean("broke", broken);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        broken = NBT.getBoolean("broke");
    }

    @Override
    protected String getTEName() {
        return null;
    }


    private float clampPhi(float phi, int time) {
        boolean afternoon = time >= 6000;
        if (afternoon) {
            if (phi >= 360)
                phi -= 360;
            if (phi < -360)
                phi += 360;
        } else {
            if (phi > 180)
                phi -= 360;
            if (phi <= -180)
                phi += 360;
        }
        return phi;
    }


    private float adjustPhiForClosestPath(float finalphi) {
        //ReikaJavaLibrary.pConsole(String.format("PHI: %.3f    TARGET: %.3f", phi, finalphi));
        if (!ReikaMathLibrary.isSameSign(finalphi, phi)) {
            if (finalphi < -180) {
                finalphi += 360;
            }
            if (finalphi > 180) {
                finalphi -= 360;
            }
            if (finalphi < 0 && finalphi < -90) {
                finalphi += 360;
            }
        }
        return finalphi;
    }

    @Override
    public void onEMP() {
    }

    @Override
    public void breakBlock() {
        if (plant != null)
            plant.invalidate(level);
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
