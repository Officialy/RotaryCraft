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
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import reika.dragonapi.instantiable.data.immutable.BlockKey;
import reika.dragonapi.instantiable.data.immutable.BlockVector;
import reika.dragonapi.interfaces.blockentity.GuiController;
import reika.dragonapi.libraries.ReikaDirectionHelper;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.api.interfaces.GPRReactive;
import reika.rotarycraft.auxiliary.BlockColorMapper;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
import reika.rotarycraft.gui.container.machine.BlankContainer;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryAdvancements;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryMenus;

/**
 * The Ground Penetrating Radar scans a vertical cross-section of the world directly below itself and
 * paints it as a colored X-ray in its GUI. The plane is {@value #MAX_WIDTH} wide and {@value #MAX_HEIGHT}
 * deep; its width (and thus the scan cost) grows with input power. It can face along either horizontal
 * axis and be shifted around by the GUI ([, ], \ keys → shift/reset).
 * <p>
 * Sync note (faithful to 1.7.10): the discovered {@link BlockKey} array is <b>not</b> transmitted — it
 * would be enormous. Instead both sides re-scan their own copy of the (loaded) world: the server run
 * carries the side-effects (achievement grants + {@link GPRReactive} callbacks), the client run only
 * fills the display array so the GUI has data. Only the view offset + facing are synced.
 */
public class BlockEntityGPR extends BlockEntityPowerReceiver implements GuiController, RangedEffect, MenuProvider {

    public static final int MAX_HEIGHT = 96;//256;
    public static final int MAX_WIDTH = 81;

    /** A depth-by-width array of the discovered blocks, drawn downwards (first slots are top layer);
     * all coords relative to the tile. Display-only, re-scanned every scan tick, never persisted. */
    private final BlockKey[][] blocks = new BlockKey[MAX_HEIGHT][MAX_WIDTH];

    /** True means the rendered plane runs east-west, and the GUI "looks" northward. */
    private boolean xdir;

    private int offsetX;
    private int offsetY;
    private int offsetZ;

    /** Independent client-side scan cadence (the synced {@code tickcount} drives the server scan). */
    private int clientScanTimer = 0;

    public BlockEntityGPR(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.GPR.get(), pos, state);
    }

    public boolean isUseableByPlayer(Player par1Player) {
        return level.getBlockEntity(worldPosition) == this;
    }

    public BlockVector getLookDirection() {
        return new BlockVector(worldPosition.getX() + offsetX, worldPosition.getY() + offsetY, worldPosition.getZ() + offsetZ,
                ReikaDirectionHelper.getRightBy90(this.getGuiDirection()));
    }

    public void shift(Direction dir, int amt) {
        if (amt == 0) {
            offsetX = offsetY = offsetZ = 0;
        } else {
            offsetX += dir.getStepX() * amt;
            offsetY += dir.getStepY() * amt;
            offsetZ += dir.getStepZ() * amt;
        }
    }

    public void shiftInt(int amt) {
        this.shift(this.getGuiDirection(), amt);
    }

    public void resetOffset() {
        offsetX = offsetY = offsetZ = 0;
    }

    public Direction getGuiDirection() {
        return xdir ? Direction.SOUTH : Direction.EAST;
    }

    /** Cave-density sampler around the machine; unused by the base machine but part of the public API. */
    public double getSpongy(Level world, BlockPos pos) {
        int numcave = 0;
        int numsolid = 0;
        int range = this.getRange();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        for (int i = -range; i <= range; i++) {
            for (int j = -range; j <= range; j++) {
                for (int k = y; k >= world.getMinY(); k--) {
                    Block id = world.getBlockState(new BlockPos(x + i, k, z + j)).getBlock();
                    if (ReikaWorldHelper.caveBlock(id))
                        numcave++;
                    else
                        numsolid++;
                }
            }
        }
        return (double) numcave / (double) (numcave + numsolid);
    }

    /** Server tick: compute power, grant the build achievement, and run the full scan (with side effects). */
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getSummativeSidedPower();
        power = (long) omega * (long) torque;
        if (power < MINPOWER)
            return;
        RotaryAdvancements.GPR.triggerAchievement(this.getPlacer());
        if (tickcount <= 0) {
            this.scan(world, pos, true);
            tickcount = 20;
        }
        tickcount--;
    }

    /** Client tick: re-scan the local world copy (display only, no side effects) so the GUI has data. */
    public void clientTick(Level world, BlockPos pos) {
        if (power < MINPOWER)
            return;
        if (clientScanTimer <= 0) {
            this.scan(world, pos, false);
            clientScanTimer = 20;
        }
        clientScanTimer--;
    }

    private void scan(Level world, BlockPos pos, boolean serverSide) {
        Direction dir = ReikaDirectionHelper.getRightBy90(this.getGuiDirection());
        int r = this.getRange();
        int baseX = pos.getX() + offsetX;
        int baseZ = pos.getZ() + offsetZ;
        int topY = pos.getY(); //vertical offset does not change scan depth (faithful to 1.7.10)
        for (int j = -r; j <= r; j++) {
            for (int dd = 1; dd <= MAX_HEIGHT; dd++) {
                int dy = topY - dd;
                int dx = baseX + j * Math.abs(dir.getStepX());
                int dz = baseZ + j * Math.abs(dir.getStepZ());
                BlockPos bp = new BlockPos(dx, dy, dz);
                BlockKey bk = BlockKey.getAt(world, bp);
                blocks[dd - 1][j + r] = bk;
                if (serverSide)
                    this.handleBlock(world, bp, bk);
            }
        }
    }

    private void handleBlock(Level world, BlockPos pos, BlockKey bk) {
        Block b = bk.blockID.getBlock();
        if (b == Blocks.END_PORTAL || b == Blocks.END_PORTAL_FRAME)
            RotaryAdvancements.GPRENDPORTAL.triggerAchievement(this.getPlacer());
        else if (b == Blocks.SPAWNER)
            RotaryAdvancements.GPRSPAWNER.triggerAchievement(this.getPlacer());

        if (b instanceof GPRReactive gr)
            gr.onScanned(world, pos, b);
    }

    /** Args: relative X, relative depth. */
    public int getColor(int x, int y) {
        return this.getBlockColor(this.getBlock(x, y));
    }

    /** Args: relative X, relative depth. */
    public BlockKey getBlock(int x, int y) {
        int col = x + this.getRange();
        if (y < 1 || y > MAX_HEIGHT || col < 0 || col >= MAX_WIDTH)
            return null;
        return blocks[y - 1][col];
    }

    private int getBlockColor(BlockKey bk) {
        return bk != null ? BlockColorMapper.instance.getColorForBlock(bk.blockID) : BlockColorMapper.UNKNOWN_COLOR;
    }

    @Override
    public int getRange() {
        if (power <= MINPOWER)
            return 0;
        return Math.min(this.getMaxRange(), 2 * ReikaMathLibrary.logbase2(power - MINPOWER));
    }

    @Override
    public int getMaxRange() {
        return MAX_WIDTH / 2;
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.GPR;
    }

    @Override
    protected String getTEName() {
        return "gpr";
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.GPR.get();
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    public void setDirection(boolean x) {
        xdir = x;
        this.syncAllData(false);
        this.setChanged();
    }

    public void flipDirection() {
        this.setDirection(!xdir);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        offsetX = NBT.getIntOr("xoff", 0);
        offsetY = NBT.getIntOr("yoff", 0);
        offsetZ = NBT.getIntOr("zoff", 0);
        xdir = NBT.getBooleanOr("xd", false);
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putInt("xoff", offsetX);
        NBT.putInt("yoff", offsetY);
        NBT.putInt("zoff", offsetZ);
        NBT.putBoolean("xd", xdir);
    }

    // ==== MenuProvider: right-click opens the radar GUI (BlankContainer, no slots) ====

    @Override
    public Component getDisplayName() {
        return Component.literal("Ground Penetrating Radar");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new BlankContainer<>(RotaryMenus.GPR.get(), id, inv, this);
    }
}
