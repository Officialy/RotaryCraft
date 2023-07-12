/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2018
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.auxiliary;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.lang3.tuple.ImmutablePair;
import reika.dragonapi.instantiable.data.maps.ValueSortedMap;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.auxiliary.interfaces.SolarPlantBlock;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityMirror;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;


public class SolarPlant {

    private static final float TOWER_FALLOFF = 0.72F;
    private static final TreeMap<Integer, Integer> towerRounding = new TreeMap<>();
    public static int MAX_TOWER_HEIGHT = 32;
    public static int MAX_TOWER_VALUE = 96;

    static {
        for (int i = 0; i <= 6; i++) {
            towerRounding.put(i, i);
        }
        for (int i = 8; i <= 12; i += 2) {
            towerRounding.put(i, i);
        }
        for (int i = 16; i <= 24; i += 4) {
            towerRounding.put(i, i);
        }
        towerRounding.put(MAX_TOWER_HEIGHT, MAX_TOWER_HEIGHT);
    }

    private final ValueSortedMap<BlockPos, SolarTower> towers = new ValueSortedMap<>();
    private final HashMap<BlockPos, BlockPos> mirrors = new HashMap<>();

    //private final SolarSkyCache skyCache = new SolarSkyCache();
    private final HashMap<BlockPos, Integer> mirrorLevels = new HashMap<>();

    public static SolarPlant build(Level world, BlockPos pos) {
        SolarPlant p = new SolarPlant();
        HashMap<Block, Block> blocks = new HashMap<>();
//        blocks.recursiveAdd(world, pos, RotaryBlocks.SOLAR.get());
        HashMap<BlockPos, ImmutablePair<Integer, Integer>> towerLocations = new HashMap<>();
        ArrayList<BlockPos> li = new ArrayList<>();
//        while (blocks.size() > 0) {
//            BlockPos c = blocks.getNextAndMoveOn();
//            SolarPlantBlock b = (SolarPlantBlock) world.getBlockEntity(c);
//            b.setPlant(p);
//            MachineRegistry m = MachineRegistry.getMachine(world, c);
//            if (m == MachineRegistry.MIRROR) {
//                li.add(c);
//            } else if (m == MachineRegistry.SOLARTOWER) {
//                ImmutablePair<Integer, Integer> get = towerLocations.get(c.to2D());
//                int val1 = get != null ? get.left.intValue() : Integer.MAX_VALUE;
//                int val2 = get != null ? get.right.intValue() : Integer.MIN_VALUE;
//                val1 = Math.min(val1, c.getY());
//                val2 = Math.max(val2, c.getY());
//                towerLocations.put(c.to2D(), new ImmutablePair<>(val1, val2));
//            }
//        }
        for (BlockPos c : towerLocations.keySet()) {
            ImmutablePair<Integer, Integer> ys = towerLocations.get(c);
            int dy = ys.left;
            int h = 0;
//            while (MachineRegistry.getMachine(world, new BlockPos(c.getX(), dy, c.getZ())) == MachineRegistry.SOLARTOWER && dy <= ys.left + MAX_TOWER_HEIGHT) {
//                dy++;
//
//                if (ReikaWorldHelper.checkForAdjBlock(world, new BlockPos(c.getX(), dy, c.getZ()), MachineRegistry.MIRROR.getBlockState().getBlock()) != null) {
//                    h = 0;
//                }
//
//                h++;
//            }
//            SolarTower s = new SolarTower(c, h, ys.left, ys.right);
//            p.towers.put(c, s);
        }
        for (BlockPos c : li) {
            p.addMirror(c, getClosestTower(c, towerLocations.keySet()));
        }
        //ReikaJavaLibrary.pConsole("Added mirrors "+p.mirrors.keySet(), Dist.DEDICATED_SERVER);
        //ReikaJavaLibrary.pConsole("Added towers "+p.towers.keySet(), Dist.DEDICATED_SERVER);
        return p;
    }

    private static BlockPos getClosestTower(BlockPos c, Collection<BlockPos> locs) {
        double dist = Double.POSITIVE_INFINITY;
        BlockPos closest = null;
        for (BlockPos c2 : locs) {
            double dd = ReikaMathLibrary.py3d(c);
            if (dd < dist) {
                dist = dd;
                closest = c2;
            }
        }
        return closest;
    }

    private void addMirror(BlockPos c, BlockPos tower) {
        mirrors.put(c, tower);
        Integer get = mirrorLevels.get(new BlockPos(c.getX(), 0, c.getZ()));
        int val = get != null ? get : -1;
        int max = Math.max(val, c.getY());
        mirrorLevels.put(new BlockPos(c.getX(), 0, c.getZ()), max);
    }

    private boolean isHighestMirror(BlockEntityMirror te) {
        Integer get = mirrorLevels.get(new BlockPos(te.getBlockPos().getX(), 0, te.getBlockPos().getZ()));
        return get != null && te.getBlockPos().getY() >= get;
    }

    public int towerCount() {
        return towers.size();
    }

    public int mirrorCount() {
        return mirrors.size();
    }

    public int rawTowerBlocks() {
        int ret = 0;
        for (SolarTower val : towers.values())
            ret += val.effectiveHeight;
        return ret;
    }

    public int getEffectiveTowerBlocks() {
        float f = 1;
        int sum = 0;
        for (SolarTower val : towers.values()) {
            sum += val.effectiveHeight * f;
            f *= TOWER_FALLOFF;
        }
        sum = towerRounding.floorEntry(sum).getValue();
        return sum;
    }

    public BlockPos getPrimaryTower() {
        return towers.isEmpty() ? null : towers.getFirst().location;
    }

    public BlockPos getAimingPositionForMirror(BlockEntityMirror te) {
        if (towers.isEmpty())
            return null;
        BlockPos c = mirrors.get(te.getBlockPos());
        return c.offset(0, towers.get(c).topBlock, 0);
    }

    public float getOverallBrightness(Level world) {
        float f = 0;
        for (BlockPos c : mirrors.keySet()) {
            BlockEntity te = world.getBlockEntity(c);
            if (te instanceof BlockEntityMirror tm) {
                if (tm.isFunctional())
                    f++;
            }
        }
        f /= this.mirrorCount();
        return f * this.getLightLevel(world) / 15F;
    }

    public float getLightLevel(Level world) {
        if (world.dimension() == Level.NETHER || world.dimension() == Level.END)
            return 0;
        //if (world.hasNoSky)
        //    return 0;
        double sun = ReikaWorldHelper.getSunIntensity(world, true, 0);//todo * PlanetDimensionHandler.getSunIntensity(world);
        if (sun > 0.21) {
            return (int) (15 * sun);
        }
        int moon = world.getMoonPhase();
        float phase = switch (moon) {
            case 0 -> 1;
            case 1, 7 -> 0.8F;
            case 2, 6 -> 0.5F;
            case 3, 5 -> 0.2F;
            case 4 -> 0.05F;
            default -> 0;
        };
        //ReikaJavaLibrary.pConsole(phase);
        return 15 * 0.2F * phase;
    }

    public int getTowerMultiplier() {
        return Math.min(this.getEffectiveTowerBlocks(), MAX_TOWER_VALUE);
    }

    public void invalidate(Level world) {
        for (BlockPos c : mirrors.keySet()) {
            SolarPlantBlock b = (SolarPlantBlock) world.getBlockEntity(c);
            b.setPlant(null);
        }
        for (SolarTower s : towers.values()) {
            for (int y = s.bottomBlock; y <= s.topBlock; y++) {
                SolarPlantBlock b = (SolarPlantBlock) world.getBlockEntity(s.location.offset(0, y, 0));
                b.setPlant(null);
            }
        }
        towers.clear();
        mirrors.clear();
    }

    public boolean canSeeTheSky(BlockEntityMirror te) {
        //return skyCache.canSeeTheSky(world, pos);
        return te.getLevel().canSeeSky(te.getBlockPos().above()) && this.isHighestMirror(te);
    }

    private static class SolarTower implements Comparable<SolarTower> {

        /**
         * 2D only
         */
        private final BlockPos location;

        /**
         * Effective since tower blocks with a mirror >= their y coordinate do not count
         */
        private final int effectiveHeight;
        private final int bottomBlock;
        private final int topBlock;

        private SolarTower(BlockPos c, int h, int bottom, int top) {
            location = c;
            effectiveHeight = h;
            bottomBlock = bottom;
            topBlock = top;
        }

        @Override
        public int hashCode() {
            return location.hashCode() | (effectiveHeight << 24);
        }

        @Override
        public int compareTo(SolarTower o) {
            int ret = -Integer.compare(effectiveHeight, o.effectiveHeight); //to put bigger at the beginning
            return ret != 0 ? ret : Integer.compare(this.hashCode(), o.hashCode());
        }

    }

}
