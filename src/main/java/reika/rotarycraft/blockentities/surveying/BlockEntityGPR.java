///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.surveying;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Blocks;
//import reika.dragonapi.instantiable.data.immutable.BlockKey;
//import reika.dragonapi.instantiable.data.immutable.BlockVector;
//import reika.dragonapi.interfaces.blockentity.GuiController;
//import reika.dragonapi.libraries.ReikaDirectionHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.rotarycraft.api.Interfaces.GPRReactive;
//import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
//import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
//import reika.rotarycraft.registry.MachineRegistry;
//
//
//public class BlockEntityGPR extends BlockEntityPowerReceiver implements GuiController, RangedEffect {
//
//    public static final int MAX_HEIGHT = 96;//256;
//    public static final int MAX_WIDTH = 81;
//
//    /**
//     * A depth-by-width array of the discovered block IDs, materials, colors
//     * drawn downwards (first slots are top layer); all coords relative to tile
//     */
//    private final BlockKey[][] blocks = new BlockKey[MAX_HEIGHT][MAX_WIDTH]; //from 0-16 -> centred on 8 (8 above and below)
//    private final int oldmeta = 0;
//    /**
//     * True means the rendered plane runs east-west, and the GUI "looks" northward
//     */
//    private boolean xdir;
//
//
//    private static int getBlockColor(BlockKey bk) {
//        return bk != null ? BlockColorMapper.instance.getColorForBlock(bk.blockID) : BlockColorMapper.UNKNOWN_COLOR.getColor();
//    }
//
//    public boolean isUseableByPlayer(Player par1Player) {
//        return level.getBlockEntity(worldPosition) == this;
//    }
//
//    public BlockVector getLookDirection() {
//        return new BlockVector(xCoord + getStepX(), yCoord + getStepY(), zCoord + getStepZ(), ReikaDirectionHelper.getRightBy90(this.getGuiDirection()));
//    }
//
//    public void shift(Direction dir, int amt) {
//        if (amt == 0) {
//            getStepX() = getStepY() = getStepZ() = 0;
//        } else {
//            getStepX() += dir.getStepX() * amt;
//            getStepY() += dir.getStepY() * amt;
//            getStepZ() += dir.getStepZ() * amt;
//        }
//    }
//
//    public void shiftInt(int amt) {
//        this.shift(this.getGuiDirection(), amt);
//    }
//
//    public void resetOffset() {
//        getStepX() = getStepY() = getStepZ() = 0;
//    }
//
//    public Direction getGuiDirection() {
//        return xdir ? Direction.SOUTH : Direction.EAST;
//    }
//
//    public double getSpongy(Level world, BlockPos pos) {
//        int numcave = 0;
//        int numsolid = 0;
//        boolean dungeon = false;
//        boolean mineshaft = false;
//        boolean stronghold = false;
//
//        int range = this.getRange();
//        for (int i = -range; i <= range; i++) {
//            for (int j = -range; j <= range; j++) {
//                for (int k = y; k >= 0; k--) {
//                    Block id = (world.getBlock(x + i, k, z + j));
//                    if (ReikaWorldHelper.caveBlock(id))
//                        numcave++;
//                    else
//                        numsolid++;
//                    if (id == Blocks.COBWEB)
//                        mineshaft = true;
//                    if (id == Blocks.end_portal || id == Blocks.end_portal_frame)
//                        stronghold = true;
//                }
//            }
//        }
//        double ans = (double) numcave / (double) (numcave + numsolid);
//        return ans;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        this.getSummativeSidedPower();
//        power = (long) omega * (long) torque;
//        if (power < MINPOWER)
//            return;
//        RotaryAdvancements.GPR.triggerAchievement(this.getPlacer());
//        if (tickcount == 0) {
//            this.eval2(world, x + getStepX(), y + getStepY(), z + getStepZ(), meta);
//            tickcount = 20;
//        }
//        tickcount--;
//    }
//
//
//    /**
//     * Args: Relative X, relative depth
//     */
//    public int getColor(int x, int y) {
//        return getBlockColor(this.getBlock(x, y));
//    }
//
//    /**
//     * Args: Relative X, relative depth
//     */
//    public BlockKey getBlock(int x, int y) {
//        return blocks[y - 1][x + this.getRange()];
//    }
//
//    private void eval2(Level world, BlockPos pos) {
//        Direction dir = ReikaDirectionHelper.getRightBy90(this.getGuiDirection());
//        int r = this.getRange();
//        for (int j = -r; j <= r; j++) {
//            for (int dd = 1; dd <= MAX_HEIGHT; dd++) {
//                int dy = yCoord - dd;
//                int dx = x + j * Math.abs(dir.getStepX());
//                int dz = z + j * Math.abs(dir.getStepZ());
//                BlockKey bk = BlockKey.getAt(world, dx, dy, dz);
//                blocks[dd - 1][j + r] = bk;
//                this.handleBlock(world, dx, dy, dz, bk);
//            }
//        }
//    }
//
//    private void handleBlock(Level world, BlockPos pos, BlockKey bk) {
//        if (bk.blockID == Blocks.END_PORTAL || bk.blockID == Blocks.END_PORTAL_FRAME)
//            RotaryAdvancements.GPRENDPORTAL.triggerAchievement(this.getPlacer());
//        else if (bk.blockID == Blocks.MOB_SPAWNER)
//            RotaryAdvancements.GPRSPAWNER.triggerAchievement(this.getPlacer());
//
//        if (bk.blockID instanceof GPRReactive) {
//            ((GPRReactive) bk.blockID).onScanned(world, pos, bk.blockID, bk.metadata);
//        }
//    }
//
//    /*
//    public int[] getBounds() { //Returns [low, hi]
//        int[] val = {40,40};
//
//        int range = this.getRange();
//        if (range <= 0)
//            return val;
//        val[0] -= range;
//        val[1] += range;
//        if (val[0] < 0)
//            val[0] = 0;
//        if (val[1] >= 80)
//            val[1] = 80;
//        return val;
//    }*/
//	/*
//	public int[] getHorizontalInterval() {
//		int[] val = {xdir ? xCoord : zCoord, xdir ? xCoord : zCoord};
//
//		int range = this.getRange();
//		if (range <= 0)
//			return val;
//		val[0] -= range;
//		val[1] += range;
//		return val;
//	}
//
//	public int[] getVerticalInterval() {
//		int[] val = {yCoord-1, yCoord-MAX_HEIGHT};
//		return val;
//	}
//	 */
//    public int getRange() {
//        return Math.min(this.getMaxRange(), 2 * ReikaMathLibrary.logbase2(power - MINPOWER));
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.GPR;
//    }
//
//    @Override
//    public int getMaxRange() {
//        return MAX_WIDTH / 2;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return 0;
//    }
//
//    @Override
//    public void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        getStepX() = NBT.getInt("xoff");
//        getStepY() = NBT.getInt("yoff");
//        getStepZ() = NBT.getInt("zoff");
//
//        xdir = NBT.getBoolean("xd");
//    }
//
//    @Override
//    public void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//
//        NBT.putInt("xoff", getStepX());
//        NBT.putInt("yoff", getStepY());
//        NBT.putInt("zoff", getStepZ());
//
//        NBT.putBoolean("xd", xdir);
//    }
//
//    public void setDirection(boolean x) {
//        xdir = x;
//        this.syncAllData(false);
//        this.triggerBlockUpdate();
//    }
//
//    public void flipDirection() {
//        this.setDirection(!xdir);
//    }
//}
