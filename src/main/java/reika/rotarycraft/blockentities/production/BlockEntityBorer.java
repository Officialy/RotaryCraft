/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.production;

import java.util.Arrays;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.io.ReikaSoundHelper;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.auxiliary.MachineEnchantmentHandler;
import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
import reika.rotarycraft.auxiliary.interfaces.EnchantableMachine;
import reika.rotarycraft.base.blockentity.BlockEntityBeamMachine;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.base.blocks.entity.BlockMiningPipe;
import reika.rotarycraft.gui.container.machine.ContainerBorer;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.DurationRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * The Boring Machine: an infinite horizontal tunnel-borer. Each operation it advances one block along
 * its facing, breaking the selected cross-section (a 7-wide x 5-tall grid, the {@link #cutShape} mask
 * the GUI edits) and lining the shaft behind it with {@link BlockMiningPipe}. It skips over pipe it has
 * already laid, so it keeps digging fresh ground rather than re-mining its own shaft. If the required
 * power/torque to break the next slice exceeds what it's fed, it jams. Port of the legacy
 * {@code TileEntityBorer}, minus the 1.7-only chunk-anticipation worldgen and mod-protection hooks.
 */
public class BlockEntityBorer extends BlockEntityBeamMachine implements EnchantableMachine, DiscreteFunction {

    /** Power required to break a block, per 0.1F hardness. */
    public static final int DIGPOWER = (int) (64 * ConfigRegistry.getBorerPowerMult());

    public static final int COLS = 7;
    public static final int ROWS = 5;

    private final MachineEnchantmentHandler enchantments = new MachineEnchantmentHandler()
            .addFilter(Enchantments.FORTUNE).addFilter(Enchantments.EFFICIENCY)
            .addFilter(Enchantments.SILK_TOUCH).addFilter(Enchantments.SHARPNESS);

    public boolean drops = true;
    /** 7 cols x 5 rows mask of which cross-section cells to bore. Defaults to the full tunnel. */
    public boolean[][] cutShape = new boolean[COLS][ROWS];

    private int step = 1;
    private int reqpow;
    private int mintorque;
    private boolean jammed = false;
    private boolean nodig = false;
    private boolean isMiningAir = false;
    private int durability = ConfigRegistry.BORERMAINTAIN.getState() ? 256 : Integer.MAX_VALUE;

    public BlockEntityBorer(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.BORER.get(), pos, state);
        for (boolean[] col : cutShape)
            Arrays.fill(col, true);
    }

    @Override
    protected void makeBeam(Level world, BlockPos pos) {
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.BORER;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.BORER.get();
    }

    @Override
    protected String getTEName() {
        return "borer";
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
    }

    public boolean isJammed() {
        return jammed;
    }

    public void reset() {
        step = 1;
    }

    public boolean repair() {
        if (durability > 0)
            return false;
        durability = ConfigRegistry.BORERMAINTAIN.getState() ? 256 : Integer.MAX_VALUE;
        return true;
    }

    private void setJammed(boolean jam) {
        jammed = jam;
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        tickcount++;
        this.getIOSides(world, pos, this.getBlockState().getValue(BlockRotaryCraftMachine.FACING));
        this.getPower(false);

        power = (long) omega * (long) torque;
        if (power <= 0) {
            this.setJammed(false);
            this.reset();
            return;
        }

        if (durability <= 0)
            return;

        nodig = this.isNoDig();
        if (nodig)
            return;
        if (omega <= 0)
            return;

        if (tickcount == 1 || step == 1)
            isMiningAir = this.checkMiningAir(world, pos);

        if (!world.isClientSide() && tickcount >= this.getOperationTime() || (isMiningAir && tickcount % 5 == 0)) {
            this.skipMiningPipes(world, pos, 0, 128);
            this.calcReqPower(world, pos);
            if (power >= reqpow && reqpow != -1) {
                this.setJammed(false);
                if (!world.isClientSide()) {
                    this.dig(world, pos);
                    if (!isMiningAir)
                        durability--;
                }
            } else {
                this.setJammed(true);
            }
            tickcount = 0;
            isMiningAir = false;
        }
    }

    private boolean isNoDig() {
        for (int i = 0; i < COLS; i++)
            for (int j = 0; j < ROWS; j++)
                if (cutShape[i][j])
                    return false;
        return true;
    }

    // --- cross-section geometry ---------------------------------------------

    /**
     * World position of cross-section cell (i,j) at tunnel depth {@code depth}. Column {@code i} (0..6)
     * runs along the horizontal axis perpendicular to facing, row {@code j} (0..4) runs up the Y axis
     * (borer at the bottom). Faithful to the legacy {@code x+step*facing + a*(i-3)}, {@code y+(4-j)}.
     */
    // NOTE: the borer tunnels indefinitely along its facing. The 1.7 chunk-anticipation worldgen was
    // deliberately not ported, so a shaft crossing into an unloaded chunk will force a sync chunk load
    // via getBlockState — acceptable, but the source of any "borer stutters at chunk borders" reports.
    private BlockPos readPos(BlockPos pos, int depth, int i, int j) {
        Direction facing = this.getFacing();
        int w = i - 3;
        int h = 4 - j;
        BlockPos base = pos.relative(facing, depth);
        Direction.Axis fax = facing.getAxis();
        Direction.Axis widthAxis = (fax == Direction.Axis.X) ? Direction.Axis.Z : Direction.Axis.X;
        int dx = 0, dy = 0, dz = 0;
        if (widthAxis == Direction.Axis.X) dx += w; else dz += w;
        if (fax == Direction.Axis.Y) dz += h; else dy += h; // vertical borer: height on Z
        return base.offset(dx, dy, dz);
    }

    private boolean active(int i, int j) {
        return cutShape[i][j] || step == 1;
    }

    private boolean checkMiningAir(Level world, BlockPos pos) {
        for (int i = 0; i < COLS; i++)
            for (int j = 0; j < ROWS; j++)
                if (active(i, j) && !world.getBlockState(readPos(pos, step, i, j)).isAir())
                    return false;
        return true;
    }

    private void skipMiningPipes(Level world, BlockPos pos, int stepped, int max) {
        if (stepped >= max)
            return;
        boolean allpipe = true;
        boolean haspipe = false;
        Direction.Axis boreAxis = this.getFacing().getAxis();
        for (int i = 0; i < COLS; i++) {
            for (int j = 0; j < ROWS; j++) {
                if (!active(i, j))
                    continue;
                BlockState bs = world.getBlockState(readPos(pos, step, i, j));
                if (bs.getBlock() instanceof BlockMiningPipe) {
                    haspipe = true;
                    BlockMiningPipe.Shape shape = BlockMiningPipe.shapeOf(bs);
                    // A junction cap, or a pipe running along the bore axis, is "already tunnelled".
                    if (!shape.isJunction() && shape.axis() != boreAxis)
                        allpipe = false;
                } else {
                    allpipe = false;
                }
            }
        }
        if (haspipe && allpipe) {
            step++;
            this.skipMiningPipes(world, pos, stepped + 1, max);
        }
    }

    // --- power calc ----------------------------------------------------------

    private void calcReqPower(Level world, BlockPos pos) {
        reqpow = 0;
        mintorque = 0;
        for (int i = 0; i < COLS; i++) {
            for (int j = 0; j < ROWS; j++) {
                if (!active(i, j))
                    continue;
                if (!this.reqPowAdd(world, readPos(pos, step, i, j)))
                    return; // reqpow set to -1 -> blocked
            }
        }
        if (torque < mintorque)
            reqpow = -1;
    }

    /** Adds the cost of the block at {@code p}; returns false (and sets reqpow=-1) if unbreakable. */
    private boolean reqPowAdd(Level world, BlockPos p) {
        if (step > 30_000_000) {
            reqpow = -1;
            return false;
        }
        if (this.ignoreBlockExistence(world, p))
            return true;
        BlockState bs = world.getBlockState(p);
        float hard = bs.getDestroySpeed(world, p);
        if (hard < 0) { // unbreakable
            reqpow = -1;
            return false;
        }
        reqpow += (int) (DIGPOWER * 10 * hard);
        int sharp = enchantments.getEnchantment(Enchantments.SHARPNESS);
        mintorque += calculateTorqueForHardness(hard, sharp);
        return true;
    }

    public static int calculateTorqueForHardness(float hard, int sharp) {
        float c = 10 - 0.5F * sharp;
        int add = ceil2exp((int) (c * hard));
        if (sharp > 0)
            add = Math.min(add, 1 << Math.max(0, 10 - sharp / 3));
        return Math.max(1, add);
    }

    private static int ceil2exp(int v) {
        int p = 1;
        while (p < v)
            p <<= 1;
        return p;
    }

    private boolean ignoreBlockExistence(Level world, BlockPos p) {
        BlockState bs = world.getBlockState(p);
        if (bs.isAir())
            return true;
        FluidState fs = bs.getFluidState();
        return !fs.isEmpty();
    }

    // --- digging -------------------------------------------------------------

    private void dig(Level world, BlockPos pos) {
        this.support(world, pos);
        BlockMiningPipe pipeBlock = (BlockMiningPipe) RotaryBlocks.MININGPIPE.get();
        Direction.Axis boreAxis = this.getFacing().getAxis();
        for (int i = 0; i < COLS; i++) {
            for (int j = 0; j < ROWS; j++) {
                if (!active(i, j))
                    continue;
                BlockPos p = readPos(pos, step, i, j);
                BlockState bs = world.getBlockState(p);
                if (this.dropBlocks(world, pos, p, bs)) {
                    ReikaSoundHelper.playBreakSound(world, p, bs.getBlock());
                    // The borer face gets a full-cube junction cap; the shaft behind gets an axis pipe.
                    BlockState pipe = step == 1 ? pipeBlock.junctionState() : pipeBlock.stateForAxis(boreAxis);
                    world.setBlock(p, pipe, 3);
                }
            }
        }
        step++;
    }

    /** Sand/gravel directly above a bored cell is petrified so the tunnel roof doesn't cave in. */
    private void support(Level world, BlockPos pos) {
        for (int i = 0; i < COLS; i++) {
            for (int j = 0; j < ROWS; j++) {
                if (!active(i, j))
                    continue;
                BlockPos above = readPos(pos, step, i, j).above();
                Block b = world.getBlockState(above).getBlock();
                if ((b == Blocks.SAND || b == Blocks.GRAVEL) && this.checkTop(i, j)) {
                    world.setBlock(above, b == Blocks.SAND ? Blocks.SANDSTONE.defaultBlockState() : Blocks.STONE.defaultBlockState(), 3);
                }
            }
        }
    }

    private boolean checkTop(int i, int j) {
        while (j > 0) {
            j--;
            if (cutShape[i][j])
                return false;
        }
        return true;
    }

    /** Break {@code p}, routing drops to an adjacent chest or the world. Returns false to skip it. */
    private boolean dropBlocks(Level world, BlockPos pos, BlockPos p, BlockState bs) {
        if (bs.is(Blocks.BEDROCK) || bs.is(Blocks.END_PORTAL_FRAME))
            return false;
        BlockEntity tile = world.getBlockEntity(p);
        if (tile != null && !(tile instanceof Container))
            return false;
        if (drops && !bs.isAir()) {
            List<ItemStack> items = Block.getDrops(bs, (ServerLevel) world, p, tile);
            for (ItemStack is : items) {
                if (!this.chestCheck(world, is))
                    ReikaItemHelper.dropItem(world, pos.getX() + 0.5, pos.getY() + 1.125, pos.getZ() + 0.5, is);
            }
        }
        return true;
    }

    private boolean chestCheck(Level world, ItemStack is) {
        if (is.isEmpty() || world.isClientSide())
            return false;
        for (Direction dir : Direction.values()) {
            BlockEntity te = world.getBlockEntity(worldPosition.relative(dir));
            if (te instanceof Container c && ReikaInventoryHelper.addToIInv(is.copy(), c))
                return true;
        }
        return false;
    }

    // --- misc ----------------------------------------------------------------

    @Override
    public int getRedstoneOverride() {
        return this.isJammed() ? 15 : 0;
    }

    @Override
    public int getOperationTime() {
        int base = DurationRegistry.BORER.getOperationTime(omega);
        int eff = enchantments.getEnchantment(Enchantments.EFFICIENCY);
        return eff > 0 ? Math.max(1, base / (eff + 1)) : base;
    }

    @Override
    public MachineEnchantmentHandler getEnchantmentHandler() {
        return enchantments;
    }

    public String getCurrentRequiredPower() {
        if (reqpow < 0)
            return "Infinity - Blocked";
        return String.format("Required Power: %dW; Required Torque: %dNm", reqpow, mintorque);
    }

    public int getRequiredTorque() {
        return mintorque;
    }

    public long getRequiredPower() {
        return reqpow;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new ContainerBorer(id, inv, this);
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    // Everything (including cutShape/drops) rides the sync tag — the DragonAPI base folds writeSyncTag
    // into both the disk save and the client update packet, so this persists AND reaches the GUI.
    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putInt("step", step);
        NBT.putBoolean("jam", jammed);
        NBT.putInt("dura", durability);
        NBT.putInt("reqpow", reqpow);
        NBT.putInt("reqtrq", mintorque);
        NBT.putBoolean("drops", drops);
        long bits = 0;
        for (int i = 0; i < COLS; i++)
            for (int j = 0; j < ROWS; j++)
                if (cutShape[i][j])
                    bits |= 1L << (i * ROWS + j);
        NBT.putLong("cut", bits);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        step = NBT.getIntOr("step", 1);
        jammed = NBT.getBooleanOr("jam", false);
        durability = NBT.getIntOr("dura", Integer.MAX_VALUE);
        mintorque = NBT.getIntOr("reqtrq", 0);
        reqpow = NBT.getIntOr("reqpow", 0);
        drops = NBT.getBooleanOr("drops", true);
        if (NBT.contains("cut")) {
            long bits = NBT.getLongOr("cut", 0L);
            for (int i = 0; i < COLS; i++)
                for (int j = 0; j < ROWS; j++)
                    cutShape[i][j] = (bits & (1L << (i * ROWS + j))) != 0;
        }
    }
}
