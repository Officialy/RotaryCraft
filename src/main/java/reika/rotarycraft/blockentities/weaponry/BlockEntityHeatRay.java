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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.NeoForge;
// 1.21.5: net.neoforged.common.extensions.IForgeBlockEntity no longer exists; usages stubbed.
import reika.dragonapi.DragonAPI;
import reika.dragonapi.instantiable.data.immutable.BlockKey;
import reika.dragonapi.interfaces.block.SemiTransparent;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.api.interfaces.Laserable;
import reika.rotarycraft.api.event.HeatRayNetherDetonationEvent;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blockentity.BlockEntityBeamMachine;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;

import java.util.HashMap;
import java.util.List;

public class BlockEntityHeatRay extends BlockEntityBeamMachine implements RangedEffect {

    /**
     * Rate of conversion - one power++ = 1/falloff ++ blocks range
     */
    public static final int FALLOFF = 256;

    private static final HashMap<BlockKey, LaserEffect> blockEffects = new HashMap();
    private static final LaserEffect deletionEffect = new DeletionLaserEffect();
    private static final LaserEffect meltEffectLava = new MeltLaserEffect(Fluids.LAVA);
    private static final LaserEffect meltEffectWater = new MeltLaserEffect(Fluids.WATER);
    private static final LaserEffect igniteEffect = new IgnitionLaserEffect();
    private static final LaserEffect glassifyEffect = new BlockChangeLaserEffect(Blocks.GLASS);
    private static final LaserEffect sandifyEffect = new BlockChangeLaserEffect(Blocks.SAND);

    static {
        addBlockEffect(Blocks.TALL_GRASS, deletionEffect);
        addBlockEffect(Blocks.TALL_SEAGRASS, deletionEffect);
        addBlockEffect(Blocks.RED_MUSHROOM, deletionEffect);
        addBlockEffect(Blocks.BROWN_MUSHROOM, deletionEffect);
        addBlockEffect(Blocks.POPPY, deletionEffect);
        addBlockEffect(Blocks.DANDELION, deletionEffect);
        addBlockEffect(Blocks.WHEAT, deletionEffect);
        addBlockEffect(Blocks.PUMPKIN_STEM, deletionEffect);
        addBlockEffect(Blocks.MELON_STEM, deletionEffect);
        addBlockEffect(Blocks.POTATOES, deletionEffect);
        addBlockEffect(Blocks.CARROTS, deletionEffect);
        addBlockEffect(Blocks.DEAD_BUSH, deletionEffect);
        addBlockEffect(Blocks.VINE, deletionEffect);
        addBlockEffect(Blocks.LILY_PAD, deletionEffect);
        addBlockEffect(Blocks.BRAIN_CORAL_BLOCK, deletionEffect);
        addBlockEffect(Blocks.DEAD_BRAIN_CORAL, deletionEffect);
        addBlockEffect(Blocks.TUBE_CORAL_BLOCK, deletionEffect);
        addBlockEffect(Blocks.DEAD_TUBE_CORAL_BLOCK, deletionEffect);
        addBlockEffect(Blocks.HORN_CORAL_BLOCK, deletionEffect);
        addBlockEffect(Blocks.DEAD_HORN_CORAL_BLOCK, deletionEffect);
        addBlockEffect(Blocks.POWDER_SNOW, deletionEffect);

        addBlockEffect(Blocks.WATER, deletionEffect);

        addBlockEffect(Blocks.ACACIA_LEAVES, igniteEffect);
        addBlockEffect(Blocks.AZALEA_LEAVES, igniteEffect);
        addBlockEffect(Blocks.BIRCH_LEAVES, igniteEffect);
        addBlockEffect(Blocks.DARK_OAK_LEAVES, igniteEffect);
        addBlockEffect(Blocks.FLOWERING_AZALEA_LEAVES, igniteEffect);
        addBlockEffect(Blocks.JUNGLE_LEAVES, igniteEffect);
        addBlockEffect(Blocks.OAK_LEAVES, igniteEffect);
        addBlockEffect(Blocks.SPRUCE_LEAVES, igniteEffect);

        addBlockEffect(Blocks.ACACIA_LOG, igniteEffect);
        addBlockEffect(Blocks.BIRCH_LOG, igniteEffect);
        addBlockEffect(Blocks.DARK_OAK_LOG, igniteEffect);
        addBlockEffect(Blocks.JUNGLE_LOG, igniteEffect);
        addBlockEffect(Blocks.OAK_LOG, igniteEffect);
        addBlockEffect(Blocks.SPRUCE_LOG, igniteEffect);

        addBlockEffect(Blocks.STRIPPED_ACACIA_LOG, igniteEffect);
        addBlockEffect(Blocks.STRIPPED_BIRCH_LOG, igniteEffect);
        addBlockEffect(Blocks.STRIPPED_DARK_OAK_LOG, igniteEffect);
        addBlockEffect(Blocks.STRIPPED_JUNGLE_LOG, igniteEffect);
        addBlockEffect(Blocks.STRIPPED_OAK_LOG, igniteEffect);
        addBlockEffect(Blocks.STRIPPED_SPRUCE_LOG, igniteEffect);


        addBlockEffect(Blocks.COBBLESTONE, meltEffectLava);
        addBlockEffect(Blocks.STONE, meltEffectLava);
        addBlockEffect(Blocks.SANDSTONE, meltEffectLava);
        addBlockEffect(Blocks.STONE_BRICKS, meltEffectLava);

        addBlockEffect(Blocks.ICE, meltEffectWater);
        addBlockEffect(Blocks.SNOW_BLOCK, meltEffectWater);

        addBlockEffect(Blocks.GRAVEL, new BlockChangeLaserEffect(Blocks.COBBLESTONE));
        addBlockEffect(Blocks.MOSSY_COBBLESTONE, new BlockChangeLaserEffect(Blocks.COBBLESTONE));

        addBlockEffect(Blocks.GRASS_BLOCK, new BlockChangeLaserEffect(Blocks.DIRT));
        addBlockEffect(Blocks.MYCELIUM, new BlockChangeLaserEffect(Blocks.DIRT));

        addBlockEffect(Blocks.DIRT, sandifyEffect);
        addBlockEffect(Blocks.FARMLAND, sandifyEffect);

        addBlockEffect(Blocks.SAND, glassifyEffect);

        addBlockEffect(Blocks.TNT, new LaserEffect() {
            @Override
            public boolean doEffect(Level world, BlockPos pos, long power, int range, int tickcount, BlockEntityHeatRay te) {
                world.setBlock(pos, Blocks.AIR.defaultBlockState(), 1);
                PrimedTnt var6 = new PrimedTnt(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, null);
                if (!world.isClientSide())
                    world.addFreshEntity(var6);
//                world.playSound(var6, "DragonAPI.rand.fuse", 1.0F, 1.0F);
                world.addParticle(ParticleTypes.LAVA, pos.getX() + DragonAPI.rand.nextFloat(), pos.getY() + DragonAPI.rand.nextFloat(), pos.getZ() + DragonAPI.rand.nextFloat(), 0, 0, 0);
                return false;
            }

            @Override
            public int getDelayTick(Block b, long surplus, int dist) {
                return 0;
            }
        });

        addBlockEffect(Blocks.NETHERRACK, new LaserEffect() {
            @Override
            public boolean doEffect(Level world, BlockPos pos, long power, int range, int tickcount, BlockEntityHeatRay te) {
                if (!world.isClientSide()) {
                    world.explode(null, pos.getX(), pos.getY(), pos.getZ(), 5F, true, Level.ExplosionInteraction.BLOCK);
                    NeoForge.EVENT_BUS.post(new HeatRayNetherDetonationEvent(world, pos));
                    if (world.dimension() == Level.NETHER && range >= 500) {
//                        RotaryAdvancements.NETHERHEATRAY.triggerAchievement(te.getPlacer());
                    }
                }
                return true;
            }

            @Override
            public int getDelayTick(Block b, long surplus, int dist) {
                return 6;
            }
        });
    }

    public BlockEntityHeatRay(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.HEAT_RAY.get(), pos, state);
    }

    private static void addBlockEffect(Block b, LaserEffect e) {
        blockEffects.put(new BlockKey(b), e);
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        tickcount++;
        power = (long) omega * (long) torque;
//        this.getIOSides(world, pos);
        this.getPower(false);
        //if ((world.getOverworldClockTime()&2) == 2) //halves load
        this.makeBeam(world, pos);
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
        return "heatray";
    }

    protected void makeBeam(Level world, BlockPos pos) {
        boolean blocked = false;
        int step;
        if (power >= MINPOWER) { //2MW+ (real military laser)
            int maxdist = this.getRange();
            for (step = 1; step < maxdist && (step < this.getMaxRange() || this.getMaxRange() == -1) && !blocked; step++) {
                int dx = pos.getX() + step * facing.getStepX();
                int dy = pos.getY() + step * facing.getStepY();
                int dz = pos.getZ() + step * facing.getStepZ();
                BlockState id = world.getBlockState(new BlockPos(dx, dy, dz));
                if (id != Blocks.AIR.defaultBlockState() && id.isFlammable(world, new BlockPos(dx, dy, dz), Direction.UP))
                    ReikaWorldHelper.ignite(world, new BlockPos(dx, dy, dz));
                //ReikaJavaLibrary.pConsole(Blocks.blocksList[id]);
                if (this.affectBlock(world, new BlockPos(dx, dy, dz), step, id.getBlock(), maxdist)) {
                    blocked = true;
                }
                if (id.getBlock() instanceof SemiTransparent st) {
                    if (st.isOpaque())
                        blocked = true;
                } //else if (todo id.isOpaqueCube())
                //blocked = true; //break loop
            }
            AABB zone = this.getBurnZone(step);
            List<Entity> inzone = level.getEntitiesOfClass(Entity.class, zone);
            for (Entity caught : inzone) {
                if (!(caught instanceof ItemEntity)) //Do not burn drops
                    caught.igniteForSeconds(this.getBurnTime());    // 1 Hearts worth of fire at min power, +1 heart for every 65kW extra
                if (caught instanceof PrimedTnt)
                    world.addParticle(ParticleTypes.LAVA, caught.getX() + DragonAPI.rand.nextFloat(), caught.getY() + DragonAPI.rand.nextFloat(), caught.getZ() + DragonAPI.rand.nextFloat(), 0, 0, 0);
                if (caught instanceof Laserable) {
                    ((Laserable) caught).whenInBeam(world, new BlockPos(Mth.floor(caught.getX()), Mth.floor(caught.getY()), Mth.floor(caught.getZ())), power, step);
                }
            }
        }
    }

    public int getBurnTime() {
        return 2 + (int) (16L * power / MINPOWER);
    }

    public int getRange() {
        int r = (int) (8L + (power - MINPOWER) / FALLOFF);
        if (r > this.getMaxRange())
            return this.getMaxRange();
        return r;
    }

    private AABB getBurnZone(int step) {
        int x = worldPosition.getX();
        int y = worldPosition.getY();
        int z = worldPosition.getZ();
        int sx = facing.getStepX();
        int sy = facing.getStepY();
        int sz = facing.getStepZ();
        int ex = x + sx * step;
        int ey = y + sy * step;
        int ez = z + sz * step;
        int minx = Math.min(x + (sx > 0 ? 1 : 0), ex);
        int miny = Math.min(y + (sy > 0 ? 1 : 0), ey);
        int minz = Math.min(z + (sz > 0 ? 1 : 0), ez);
        int maxx = Math.max(x + (sx < 0 ? 0 : 1), ex + (sx != 0 ? 0 : 1));
        int maxy = Math.max(y + (sy < 0 ? 0 : 1), ey + (sy != 0 ? 0 : 1));
        int maxz = Math.max(z + (sz < 0 ? 0 : 1), ez + (sz != 0 ? 0 : 1));
        return new AABB(minx, miny, minz, maxx, maxy, maxz);
    }

    private boolean affectBlock(Level world, BlockPos pos, int step, Block id, int maxdist) {
        if (id == Blocks.AIR)
            return false;
        // 1.21.5: IForgeBlockEntity gate is gone (all BlockEntities use the unified API now),
        // so the legacy `if (block.hasBlockEntity())` short-circuit isn't needed — just check
        // whether the tile at this position is a Laserable.
        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof Laserable laserable) {
            laserable.whenInBeam(world, pos, power, step);
            if (laserable.blockBeam(world, pos, power))
                return true;
        }
        if (id instanceof Laserable) {
            ((Laserable) id).whenInBeam(world, pos, power, step);
            return ((Laserable) id).blockBeam(world, pos, power);
        } else if (ConfigRegistry.ATTACKBLOCKS.getState()) {
            LaserEffect e = blockEffects.get(new BlockKey(id));
            if (e != null) {
                long surp = power / MINPOWER;//ReikaMathLibrary.logbase2(this.power-)
                int n = e.getDelayTick(id, surp, step);
                if (tickcount >= n) {
                    tickcount = 0;
                    return e.doEffect(world, pos, power, step, tickcount, this);
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }


    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.HEATRAY;
    }

    @Override
    public int getMaxRange() {
        return Math.max(64, ConfigRegistry.HEATRAYRANGE.getValue());
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    private interface LaserEffect {

        int getDelayTick(Block b, long surplus, int dist);

        boolean doEffect(Level world, BlockPos pos, long power, int range, int tickcount, BlockEntityHeatRay te);

    }

    private static class BlockChangeLaserEffect implements LaserEffect {

        private final BlockKey replacement;

        private BlockChangeLaserEffect(Block bk) {
            this(new BlockKey(bk));
        }

        private BlockChangeLaserEffect(BlockKey bk) {
            replacement = bk;
        }

        @Override
        public boolean doEffect(Level world, BlockPos pos, long power, int range, int tickcount, BlockEntityHeatRay te) {
            replacement.place(world, pos);
            return true;
        }

        @Override
        public int getDelayTick(Block b, long surplus, int dist) {
            return (int) Math.min(Integer.MAX_VALUE, (4L * dist) / surplus);
        }

    }

    private static class IgnitionLaserEffect implements LaserEffect {

        @Override
        public boolean doEffect(Level world, BlockPos pos, long power, int range, int tickcount, BlockEntityHeatRay te) {
            ReikaWorldHelper.ignite(world, pos);
            return false;
        }

        @Override
        public int getDelayTick(Block b, long surplus, int dist) {
            return 0;
        }

    }

    private static class MeltLaserEffect implements LaserEffect {

        private final Fluid fluid;

        private MeltLaserEffect(Fluid f) {
            fluid = f;
        }

        @Override
        public boolean doEffect(Level world, BlockPos pos, long power, int range, int tickcount, BlockEntityHeatRay te) {
            world.setBlock(pos, this.getBlockState(), 0, 3);
//            world.getBlockState(pos).getBlock().onNeighborBlockChange(world, pos, Blocks.AIR);
            if (fluid == Fluids.LAVA)
                world.addParticle(ParticleTypes.LAVA, pos.getX() + DragonAPI.rand.nextFloat(), pos.getY() + DragonAPI.rand.nextFloat(), pos.getZ() + DragonAPI.rand.nextFloat(), 0, 0, 0);
//            world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), "DragonAPI.rand.fizz", 0.5F, 2.6F + (DragonAPI.rand.nextFloat() - DragonAPI.rand.nextFloat()) * 0.8F);
            return false;
        }

        private BlockState getBlockState() {
            if (fluid == Fluids.WATER)
                return Blocks.WATER.defaultBlockState();
            if (fluid == Fluids.LAVA)
                return Blocks.LAVA.defaultBlockState();
            return fluid.defaultFluidState().createLegacyBlock();
        }

        @Override
        public int getDelayTick(Block b, long surplus, int dist) {
            int d = 2;
            if (fluid == Fluids.WATER)
                d = 8;
            return (int) Math.min(Integer.MAX_VALUE, (4L * dist / (d * surplus)));
        }

    }

    private static class DeletionLaserEffect implements LaserEffect {

        @Override
        public boolean doEffect(Level world, BlockPos pos, long power, int range, int tickcount, BlockEntityHeatRay te) {
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 0);
//            world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), "DragonAPI.rand.fizz", 0.5F, 2.6F + (DragonAPI.rand.nextFloat() - DragonAPI.rand.nextFloat()) * 0.8F);
            return false;
        }

        @Override
        public int getDelayTick(Block b, long surplus, int dist) {
            return (int) Math.min(Integer.MAX_VALUE, (4L * dist / (8 * surplus)));
        }

    }
}
