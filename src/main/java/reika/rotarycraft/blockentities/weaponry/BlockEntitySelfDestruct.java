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
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;

public class BlockEntitySelfDestruct extends BlockEntityPowerReceiver {

    private boolean lastHasPower;

    public BlockEntitySelfDestruct(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.SELF_DESTRUCT.get(), pos, state);
    }


    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.SELFDESTRUCT;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    //    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getSummativeSidedPower();
        boolean hasPower = power > 0;
        if (lastHasPower && !hasPower)
            this.destroy(world, pos);
        else
            lastHasPower = hasPower;
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
        return null;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    public void destroy(Level world, BlockPos pos) {
        if (!world.isClientSide) {
            tickcount++;
            int n = 6;
            int count = 32;
            double rx = pos.getX() + 0.5 + DragonAPI.rand.nextInt(2 * n + 1) - n;
            double ry = pos.getY() + 0.5 + DragonAPI.rand.nextInt(2 * n + 1) - n;
            double rz = pos.getZ() + 0.5 + DragonAPI.rand.nextInt(2 * n + 1) - n;
            int irx = Mth.floor(rx);
            int iry = Mth.floor(ry);
            int irz = Mth.floor(rz);
//            if (ReikaPlayerAPI.playerCanBreakAt((ServerLevel) level, irx, iry, irz, this.getServerPlacer()))
//                world.explode(null, rx, ry, rz, 3F, Level.ExplosionInteraction.BLOCK);
            for (int i = 0; i < 32; i++)
                world.addParticle(ParticleTypes.LAVA, rx + DragonAPI.rand.nextInt(7) - 3, ry + DragonAPI.rand.nextInt(7) - 3, rz + DragonAPI.rand.nextInt(7) - 3, 0, 0, 0);
            if (tickcount > count) {
                world.explode(null, pos.getX(), pos.getY(), pos.getZ(), 12F, true, Level.ExplosionInteraction.BLOCK);
                ReikaWorldHelper.temperatureEnvironment(world, pos, 1000);
            }/*
		Creeper e = new Creeper(world);
		e.getY = rx;
		e.posZ = rz;
		e.getY() = world.getTopSolidOrLiquidBlock((int)rx, (int)rz)+1;
		e.addMobEffect(new MobEffect(Potion.resistance.id, 10, 10));
		world.addFreshEntity(e);*/
            MachineRegistry m = this.getMachine();
            MachineRegistry m2 = MachineRegistry.getMachine(world, pos);
            if (m != m2 && tickcount <= count) {
                world.setBlock(pos, m.getBlockState(), 3);
                BlockEntitySelfDestruct te = (BlockEntitySelfDestruct) world.getBlockEntity(pos);
                te.lastHasPower = true;
                te.tickcount = tickcount;
            }
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
