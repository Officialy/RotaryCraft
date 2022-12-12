/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.surveying;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;

import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;

public class BlockEntityCaveFinder extends BlockEntityPowerReceiver implements RangedEffect {

    private final int rendermode = 0;
    private final boolean[][][] points = new boolean[this.getRange() * 2 + 1][this.getRange() * 2 + 1][this.getRange() * 2 + 1];
    private final Scanner scanner = new Scanner(this);
    public String owner;
    public boolean on;
    private int[] src = new int[3];
    private boolean needsCalc = true;

    public BlockEntityCaveFinder(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.CAVE_SCANNER.get(), pos, state);
    }

    //    @Override
    protected void onFirstTick(Level world, BlockPos pos) {
        if (src[0] == 0 && src[1] == 0 && src[2] == 0)
            this.setSrc(pos);
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getSummativeSidedPower();
        if (power < MINPOWER) {
            on = false;
            return;
        }
        on = true;

        if (rendermode == 0) {

        } else if (rendermode == 1) {
            Player ep = world.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), -1, true);
            if (ep == null)
                return;
            int px = (int) ep.getX();
            int py = (int) ep.getY();
            int pz = (int) ep.getZ();
            this.setSrc(new BlockPos(px, py, pz));
        }

        int t = this.getUpdateFrequency();
        if (needsCalc || (world.getDayTime() & t) == 0)
            this.calculatePoints();

        //ReikaJavaLibrary.pConsole(Arrays.deepToString(points));
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    private int getUpdateFrequency() {
        int r = this.getRange();
        if (r < 16)
            return 1;
        else if (r < 32)
            return 3;
        else if (r < 64)
            return 7;
        else if (r < 128)
            return 15;
        return 31;
    }

    private void calculatePoints() {
        Thread t = new Thread(scanner);
        t.start();
        needsCalc = false;
//        if (FMLLoader.getDist() == Dist.CLIENT) {
//            RenderCaveFinder rcf = (RenderCaveFinder) this.getRenderer();
//            rcf.removeListFor(this);
//        }
    }

    public boolean hasPointAt(int dx, int dy, int dz) {
        int r = this.getRange();
        if (Math.abs(dx) > r * 2 || Math.abs(dy) > r * 2 || Math.abs(dz) > r * 2) {
            //ReikaJavaLibrary.pConsole(dx+", "+dy+", "+dz);
            return false;
        }
        if (Math.abs(dx) < 0 || Math.abs(dy) < 0 || Math.abs(dz) < 0) {
            //ReikaJavaLibrary.pConsole(dx+", "+dy+", "+dz);
            return false;
        }
        try {
            return points[dx][dy][dz];
        } catch (Exception e) {
            RotaryCraft.LOGGER.error("Exception at " + dx + ", " + dy + ", " + dz + "!");
            return false;
        }
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }


//    @Override
//    public double getMaxRenderDistanceSquared() {
//        return 65536D;
//    }

    @Override
    public AABB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    public int getRange() {
        return Math.max(4, ConfigRegistry.CAVEFINDERRANGE.getValue());
    }

    public int getSourceX() {
        return src[0];
    }

    public int getSourceY() {
        return src[1];
    }

    public int getSourceZ() {
        return src[2];
    }

    public void setSrc(BlockPos pos) {
        src[0] = pos.getX();
        src[1] = pos.getY();
        src[2] = pos.getZ();
        needsCalc = true;
    }

    public void moveSrc(int num, Direction dir) {
        switch (dir) {
            case DOWN -> src[1] -= num;
            case UP -> src[1] += num;
            case WEST -> src[0] -= num;
            case EAST -> src[0] += num;
            case NORTH -> src[2] -= num;
            case SOUTH -> src[2] += num;
            default -> {
            }
        }
        needsCalc = true;
    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);
        tag.putIntArray("Source", src);
        tag.putBoolean("calc", needsCalc);
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);
        src = tag.getIntArray("Source");
        needsCalc = tag.getBoolean("calc");
    }

    @Override
    protected String getTEName() {
        return "cavefinder";
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.CAVESCANNER;
    }

    @Override
    public int getMaxRange() {
        return 128;
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    private final class Scanner implements Runnable {

        private final BlockEntityCaveFinder tile;

        private Scanner(BlockEntityCaveFinder te) {
            tile = te;
        }

        @Override
        public void run() {
            int r = tile.getRange();
            for (int i = -r; i <= r; i++) {
                for (int j = -r; j <= r; j++) {
                    for (int k = -r; k <= r; k++) {
                        int x = tile.getSourceX() + i;
                        int y = tile.getSourceY() + j;
                        int z = tile.getSourceZ() + k;
                        //ReikaJavaLibrary.pConsole(x+", "+y+", "+z);
                        //ReikaJavaLibrary.pConsole((i+r)+", "+(j+r)+", "+(k+r));
//                      todo  tile.points[i + r][j + r][k + r] = ReikaWorldHelper.cornerHasAirAdjacent(level, pos);
                    }
                }
            }
        }
    }

}
