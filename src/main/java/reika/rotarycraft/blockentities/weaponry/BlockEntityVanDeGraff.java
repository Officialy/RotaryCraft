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
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import net.minecraft.world.phys.AABB;
import net.neoforged.common.NeoForge;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.instantiable.data.WeightedRandom;
import reika.dragonapi.interfaces.blockentity.AdjacentUpdateWatcher;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.api.interfaces.Shockable;
import reika.rotarycraft.api.event.VDGAttackEvent;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
import reika.rotarycraft.entities.EntityDischarge;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.SoundRegistry;

public class BlockEntityVanDeGraff extends BlockEntityPowerReceiver implements RangedEffect, AdjacentUpdateWatcher {

    private final WeightedRandom<Direction> sideMap = new WeightedRandom<>();

    //In coloumbs
    private int charge;

    public BlockEntityVanDeGraff(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.VAN_DE_GRAFF.get(), pos, state);
    }

    private void updateSidedMappings(Level world, BlockPos pos) {
        sideMap.clear();
        for (int i = 1; i < 6; i++) {
            Direction dir = Direction.values()[i];
            int dx = pos.getX() + dir.getStepX();
            int dy = pos.getY() + dir.getStepY();
            int dz = pos.getZ() + dir.getStepZ();
            Block b = world.getBlockState(new BlockPos(dx, dy, dz)).getBlock();
            if (b == Blocks.AIR || b == Blocks.BARRIER) {
                sideMap.addEntry(dir, 0);
            } else {
                MapColor mat = b.defaultBlockState().getMapColor(world, pos); //todo pos or dx,dy,dz?
                MachineRegistry m = MachineRegistry.getMachine(world, new BlockPos(dx, dy, dz));
                if (m == MachineRegistry.VANDEGRAFF) {
                    sideMap.addEntry(dir, 0);
                } else {
                    BlockEntity te = getAdjacentBlockEntity(dir);
                    if (te instanceof Shockable) {
                        sideMap.addEntry(dir, 1000);
                    } else if (mat == MapColor.METAL) { // || mat == Material.HEAVY_METAL) {// || InterfaceCache.BCPIPE.instanceOf(te)) {
                        sideMap.addEntry(dir, 50);
                    } else if (mat == MapColor.WATER) {
                        sideMap.addEntry(dir, 20);
                    } else if (b == Blocks.TNT) {
                        sideMap.addEntry(dir, 100);
                    }
                }
            }
        }
        //ReikaJavaLibrary.pConsole(sideMap, Dist.DEDICATED_SERVER);
        sideMap.addEntry(Direction.NORTH, 1); //todo direction.UNKNOWN
    }

    //    @Override
    protected void onFirstTick(Level world, BlockPos pos) {
        this.updateSidedMappings(world, pos);
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.VAN_DE_GRAFF.get();
    }

    //    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getPowerBelow();

        charge += 4 * Math.sqrt(power);

        int r = this.getRange();

        if (r > 0) {
            Direction dir = sideMap.getRandomEntry();
            //ReikaJavaLibrary.pConsole(dir+": "+sideMap, Dist.DEDICATED_SERVER);
            if (dir != null && dir != Direction.NORTH) { //todo direction.UNKNOWN
                this.shock(world, pos, dir);
                return;
            }

            for (int i = 2; i < 4; i++) {
                BlockEntity te = world.getBlockEntity(new BlockPos(pos.getX(), pos.getY() + i, pos.getZ()));
                if (te instanceof Shockable && ((Shockable) te).canDischargeLongRange()) {
                    this.dischargeToBlock(new BlockPos(pos.getX(), pos.getY() + i, pos.getZ()), (Shockable) te);
                    return;
                }
            }
        }
        if (charge <= 0)
            return;

        AABB box = new AABB(pos).expandTowards(r, r, r);
        LivingEntity e = ReikaWorldHelper.getClosestLivingEntityNoPlayers(world, worldPosition, box, true);
        if (e != null) {
            EntityDischarge d = new EntityDischarge(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, charge, e.getY(), e.getY() + e.getEyeHeight() * 0.8, e.getZ());
            if (!world.isClientSide) {
                this.shock(e);
                world.addFreshEntity(d);
            }
            charge = 0;
        }
        if (charge > 2097152 && !world.isClientSide) {
            this.detonate(world, pos);
        }

        if (world.isRaining()) {//&& world.canLightningStrikeAt(pos.above())) {
            charge *= 0.5;
        }
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    private void shock(Level world, BlockPos pos, Direction dir) {
        int dx = pos.getX() + dir.getStepX();
        int dy = pos.getY() + dir.getStepY();
        int dz = pos.getZ() + dir.getStepZ();
        BlockEntity te = getAdjacentBlockEntity(dir);
        Block b = world.getBlockState(new BlockPos(dx, dy, dz)).getBlock();
        if (b == Blocks.AIR || b == Blocks.BARRIER) {
            return;
        }
        this.dischargeToBlock(new BlockPos(dx, dy, dz), te instanceof Shockable ? (Shockable) te : null);
        if (b == Blocks.TNT) {
            world.setBlock(new BlockPos(dx, dy, dz), Blocks.AIR.defaultBlockState(), 0);
            PrimedTnt e = new PrimedTnt(world, dx + 0.5D, dy + 0.5D, dz + 0.5D, null);
            if (!world.isClientSide)
                world.addFreshEntity(e);
//            world.playSound(e, "DragonAPI.rand.fuse", 1.0F, 1.0F);
            world.addParticle(ParticleTypes.LAVA, dx + DragonAPI.rand.nextFloat(), dy + DragonAPI.rand.nextFloat(), dz + DragonAPI.rand.nextFloat(), 0, 0, 0);
        }
    }

    private void detonate(Level world, BlockPos pos) {
        //LightningBolt b = new LightningBolt(world, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5);
        LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
        //world.addFreshEntity(b);
        world.addFreshEntity(bolt);
        charge = 0;
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 0);
        world.explode(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), 4F, true, Level.ExplosionInteraction.BLOCK);
    }

    public void dischargeToBlock(BlockPos pos, Shockable s) {
        float dx = 0.5F;
        float dy = 0.5F;
        float dz = 0.5F;
        if (s != null) {
            int min = s.getMinDischarge();
            if (charge < min)
                return;
            s.onDischarge(charge, ReikaMathLibrary.py3d(pos.getX() - worldPosition.getX(), pos.getY() - worldPosition.getY(), pos.getZ() - worldPosition.getZ()));
            dx = s.getAimPos().getX();
            dy = s.getAimPos().getY();
            dz = s.getAimPos().getZ();
        }
        SoundRegistry.SPARK.playSoundAtBlock(level, worldPosition, 0.25F, 1F);
        EntityDischarge d = new EntityDischarge(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), charge, worldPosition.getX() + dx, worldPosition.getY() + dy, worldPosition.getZ() + dz);
        if (!level.isClientSide)
            level.addFreshEntity(d);
        charge = 0;
    }

    private void shock(LivingEntity e) {
        int dmg = this.getAttackDamage();

//        if (ReikaEntityHelper.isEntityWearingFullSuitOf(e, ArmorMaterials.CHAIN))
//            dmg = 0;
//        else if (ReikaEntityHelper.isEntityWearingFullSuitOf(e, ArmorMaterials.LEATHER))
//            dmg /= 2;

        if (dmg > 0) {
            RotaryCraft.shock.lastMachine = this;
            e.hurt(RotaryCraft.shock, dmg);
            if (e instanceof Creeper) {
                level.explode(e, e.blockPosition().getX(), e.blockPosition().getY(), e.blockPosition().getZ(), 3F, Level.ExplosionInteraction.BLOCK);
                e.hurt(e.damageSources().magic(), Integer.MAX_VALUE);
            }
        }
        NeoForge.EVENT_BUS.post(new VDGAttackEvent(this, charge, dmg));
        SoundRegistry.SPARK.playSoundAtBlock(level, worldPosition, 1.25F, 1F);
    }

    private int getAttackDamage() {
        return 1 + (int) (Math.pow(charge, 2) / (4194304 * 8));
    }

    public int getCharge() {
        return charge;
    }


    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.VANDEGRAFF;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getRange() {
        return Math.min(charge / 1024, this.getMaxRange());
    }

    @Override
    public int getMaxRange() {
        return 16;
    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);
        tag.putInt("c", charge);
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);
        charge = tag.getInt("c");
    }

    @Override
    protected String getTEName() {
        return null;
    }

    @Override
    public void onAdjacentUpdate(Level world, BlockPos pos, Block b) {
        this.updateSidedMappings(world, pos);
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
