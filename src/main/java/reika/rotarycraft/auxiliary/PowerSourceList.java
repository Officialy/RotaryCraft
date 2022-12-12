/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.auxiliary;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.instantiable.data.immutable.WorldLocation;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.api.power.PowerGenerator;
import reika.rotarycraft.api.power.PowerTracker;
import reika.rotarycraft.api.power.ShaftMerger;
import reika.rotarycraft.auxiliary.interfaces.PowerSourceTracker;

import java.util.ArrayList;
import java.util.HashSet;

public class PowerSourceList implements PowerTracker {

    private final HashSet<PowerWrapper> engines = new HashSet<>();
    private final HashSet<ShaftMerger> mergers = new HashSet<>();

    private ShaftMerger caller;

    private boolean isLooping = false;
    private final boolean isBedrock = false;
    private boolean errored = false;

    public static PowerSourceList getAllFrom(Level world, Direction dir, BlockPos pos, PowerSourceTracker io, ShaftMerger caller) {
        PowerSourceList pwr = new PowerSourceList();

        BlockEntity tile = world.getBlockEntity(pos);

        if (caller != null) {
            if (pwr.passesThrough(caller) || (tile instanceof ShaftMerger && pwr.passesThrough((ShaftMerger) tile)) || tile == caller) {
                pwr.isLooping = true;
                caller.onPowerLooped(pwr);
                return pwr;
            }
        }

        if (tile instanceof ShaftMerger) {
            pwr.mergers.add((ShaftMerger) tile);
            if (tile == caller) {
                pwr.isLooping = true;
            }
            //require ALL to be bedrock
            //pwr.isBedrock = tile instanceof BlockEntitySplitter && ((BlockEntitySplitter) tile).isBedrock();
        }

        if (pwr.errored) {
            fail(pwr, caller, world, pos);
        }

        try {
            if (tile instanceof BlockEntity) { //todo fix this shit lol
                //BlockEntityIOMachine te = (BlockEntityIOMachine) tile;
                //if (!te.isWritingTo(io) && !te.isWritingTo2(io)) {
                //    return pwr;
                //}
                //if (te.isReadingFrom(io) || te.isReadingFrom2(io) || te.isReadingFrom3(io) || te.isReadingFrom4(io)) {
                //    return pwr;
                //}
                //pwr.addAll(te.getPowerSources(io, caller));
            } else if (tile instanceof PowerSourceTracker) {
                pwr.addAll(((PowerSourceTracker) tile).getPowerSources(io, caller));
            } else if (tile instanceof PowerGenerator) {
                pwr.addSource((PowerGenerator) tile);
            }
            pwr.caller = caller;

            if (pwr.passesThrough(caller)) {
                pwr.isLooping = true;
            }

            return pwr;
        } catch (StackOverflowError e) {
            //e.printStackTrace();
            RotaryCraft.LOGGER.error("PowerSourceList SOE @ " + pwr + ", called from " + caller + "!");
            pwr.errored = true;
            //fail(pwr, caller, world, pos);
            return pwr;
        }
    }

    private static void fail(PowerSourceList pwr, ShaftMerger caller, Level world, BlockPos pos) {
        if (caller != null) {
            caller.fail();
        } else {
            try {
                world.setBlock(pos, Blocks.AIR.defaultBlockState(), 0);
                world.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3, Level.ExplosionInteraction.BLOCK);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public static PowerSourceList combine(PowerSourceList in1, PowerSourceList in2, ShaftMerger caller) {
        PowerSourceList psl = new PowerSourceList();
        psl.engines.addAll(in1.engines);
        psl.engines.addAll(in2.engines);

        psl.mergers.addAll(in1.mergers);
        psl.mergers.addAll(in2.mergers);

        if (psl.mergers.contains(caller)) {
            psl.isLooping = true;
        }

        psl.caller = caller;

        return psl;
    }

    public long getMaxGennablePower() {
        long pwr = 0;

        for (PowerWrapper eng : engines) {
            pwr += eng.generator.getMaxPower();
        }

        return pwr;
    }

    public long getRealMaxPower() {
        long pwr = 0;

        for (PowerWrapper eng : engines) {
            pwr += eng.generator.getCurrentPower();
        }
        return pwr;
    }

    public PowerSourceList addSource(PowerGenerator te) {
        engines.add(new PowerWrapper(te));
        return this;
    }

    public boolean isLooping() {
        return isLooping;
    }

    public void addAll(PowerSourceList pwr) {
        for (PowerWrapper te : pwr.engines) {
            this.addSource(te.generator);
        }
        errored = errored || pwr.errored;
    }

    @Override
    public String toString() {
        if (engines.isEmpty()) {
            return "[None]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (PowerWrapper gen : engines) {
            sb.append(gen.generator);
            //if (i < engines.size()-1)
            //	sb.append("\n");
        }
        return sb.toString();
    }

    public ArrayList<String> getMessages() {
        ArrayList<String> li = new ArrayList<>();
        if (engines.isEmpty()) {
            li.add("[None]");
        } else {
            for (PowerWrapper gen : engines) {
                li.add("  " + gen.generator.toString());
            }
        }
        return li;
    }

    public boolean contains(PowerGenerator te) {
        return engines.contains(new PowerWrapper(te));
    }

    public boolean calledFrom(ShaftMerger sm) {
        return caller == sm;
    }

    public boolean passesThrough(ShaftMerger sm) {
        return mergers.contains(sm);
    }

    public boolean isEngineSpam() {
		/*
		if (engines.size() < 8)
			return false;
		long last = engines.get(0).getMaxPower();
		Class c = engines.get(0).getClass();
		for (int i = 1; i < engines.size(); i++) {
			long pow = engines.get(i).getMaxPower();
			Class c2 = engines.get(i).getClass();
			if (pow != last || c2 != c)
				return false;
		}
		if (sum = )
			return true;*/
        if (this.isEmpty())
            return false;
        if (this.isBedrock())
            return false;
        long sum = this.getMaxGennablePower();
        long avg = sum / engines.size();
        return avg > 0 && sum / avg > 4;
    }

    public int size() {
        return engines.size();
    }

    public boolean isEmpty() {
        return engines.isEmpty();
    }

    public boolean isBedrock() {
        return isBedrock;
    }

    private static class PowerWrapper {

        private final PowerGenerator generator;

        private PowerWrapper(PowerGenerator gen) {
            generator = gen;
        }

        @Override
        public int hashCode() {
            return new WorldLocation((BlockEntity) generator).hashCode();
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof PowerWrapper && new WorldLocation((BlockEntity) ((PowerWrapper) o).generator).equals(new WorldLocation((BlockEntity) generator));
        }

    }

}
