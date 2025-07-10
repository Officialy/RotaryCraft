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
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.phys.AABB;
//
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.instantiable.data.immutable.BlockKey;
//import reika.dragonapi.libraries.java.ReikaArrayHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
//import reika.rotarycraft.base.blockentity.RemoteControlMachine;
//import reika.rotarycraft.registry.GuiRegistry;
//import reika.rotarycraft.registry.MachineRegistry;
//
//import java.util.Collections;
//import java.util.List;
//
//public class BlockEntitySpyCam extends RemoteControlMachine implements RangedEffect {
//
//    public static final int MAXRANGE = 24;
//    private final BlockKey[][] topBlocks = new BlockKey[2 * MAXRANGE + 1][2 * MAXRANGE + 1];
//    private final int[][] topY = new int[2 * MAXRANGE + 1][2 * MAXRANGE + 1];
//    private int tickcount2 = 0;
//    private int[][] mobs = new int[2 * MAXRANGE + 1][2 * MAXRANGE + 1];
//    private List<LivingEntity> inzone;
//
//    public BlockEntitySpyCam(BlockEntityType<?> type, BlockPos pos, BlockState state) {
//        super(type, pos, state);
//    }
//
//    public List<LivingEntity> getEntities() {
//        return Collections.unmodifiableList(inzone);
//    }
//
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.SPYCAM;
//    }
//
//    @Override
//    public Block getBlockEntityBlockID() {
//        return null;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        this.setColors();
//        if (!this.hasCoil()) {
//            on = false;
//            return;
//        }
//        on = true;
//        tickcount2++;
//        if (!DragonAPI.debugtest) {
//            if (tickcount2 > this.getUnwindTime()) {
//                ItemStack is = this.getDecrementedCharged();
//                itemHandler.getStackInSlot(0) = is;
//                tickcount2 = 0;
//            }
//        }
//        this.getTopBlocks(world, pos);
//        this.getMobs(world, pos);
//    }
//
//    @Override
//    protected void animateWithTick(Level level, BlockPos blockPos) {
//
//    }
//
//    private void getMobs(Level world, BlockPos pos) {
//        mobs = ReikaArrayHelper.fillMatrix(mobs, 0);
//        int range = this.getRange();
//        int maxrange = this.getMaxRange();
//        AABB zone = new AABB(pos.getX() - range, 0, pos.getZ() - range, pos.getX() + 1 + range, pos.getY() + 1, pos.getZ() + 1 + range);
//        inzone = world.getEntitiesOfClass(LivingEntity.class, zone);
//        for (LivingEntity ent : inzone) {
//            int ex = (int) ent.getY() - x;
//            int ey = (int) ent.getY() - y;
//            int ez = (int) ent.getZ() - z;
//            if (EntityList.getEntityID(ent) > 0 && Math.abs(ex) < range + 1 && Math.abs(ez) < range + 1 && ent.getY() >= ReikaWorldHelper.findTopBlockBelowY(world, (int) ent.getY, y, (int) ent.posZ)) {
//                //ReikaJavaLibrary.pConsole(ent.getCommandSenderName()+" @ "+ex+", "+ez);
//                mobs[(ez + range)][ex + range] = EntityList.getEntityID(ent);
//                //ReikaJavaLibrary.pConsole(mobs[ex+range][ez+range]+String.format("@ %d,  %d  from  %d", ex+range, ez+range, EntityList.getEntityID(ent)));
//            }
//        }
//    }
//
//    private void getTopBlocks(Level world, BlockPos pos) {
//        //topBlocks = ReikaArrayHelper.fillMatrix(topBlocks, 0);
//        int range = this.getRange();
//        int maxrange = this.getMaxRange();
//        for (int i = -range; i <= range; i++) {
//            for (int j = -range; j <= range; j++) {
//                int topy = ReikaWorldHelper.findTopBlockBelowY(world, x + i, y, z + j);
//                topY[i + range][j + range] = topy;
//                Block b = world.getBlockState(new BlockPos(pos.getX() + i, topy, pos.getZ() + j)).getBlock();
//                if (world.isClientSide)
//                    topBlocks[(i + range)][j + range] = new BlockKey(b);
//                if (world.getBlockState(new BlockPos(pos.getX() + i, pos.getY(), pos.getZ() + j)).getBlock() != Blocks.AIR) {
//                    //topBlocks[(i+range)][j+range] = 0;
//                }
//            }
//        }
//    }
//
//    public int[] getBounds() {
//        int range = this.getRange();
//        int mrange = this.getMaxRange();
//        int[] bounds = {mrange - range, mrange + range};
//        return bounds;
//    }
//
//    public int getMobAt(int i, int j) {
//        return mobs[i][j];
//    }
//
//
//    public int getTopBlockColorAt(int i, int j) {
//        return BlockColorMapper.instance.getColorForBlock(topBlocks[i][j]);
//    }
//
//    public int getHeightAt(int i, int j) {
//        return topY[i][j];
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return 0;
//    }
//
//    @Override
//    protected String getTEName() {
//        return null;
//    }
//
//    @Override
//    public int getRange() {
//        return this.getMaxRange();
//    }
//
//    @Override
//    public int getMaxRange() {
//        return MAXRANGE;
//    }
//
//    @Override
//    public void activate(Level world, Player ep, BlockPos pos) {
//        if (on)
//            ep.openMenu(RotaryCraft.INSTANCE, GuiRegistry.SPYCAM.ordinal(), world, pos);
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 0;
//    }
//
//    @Override
//    public boolean isEmpty() {
//        return false;
//    }
//
//    @Override
//    public ItemStack getItem(int pIndex) {
//        return null;
//    }
//
//    @Override
//    public ItemStack removeItem(int pIndex, int pCount) {
//        return null;
//    }
//
//    @Override
//    public ItemStack removeItemNoUpdate(int pIndex) {
//        return null;
//    }
//
//    @Override
//    public void setItem(int pIndex, ItemStack pStack) {
//
//    }
//
//    @Override
//    public boolean stillValid(Player pPlayer) {
//        return false;
//    }
//
//    @Override
//    public void clearContent() {
//
//    }
//
//    @Override
//    public int getSlots() {
//        return 0;
//    }
//
//    
//    @Override
//    public ItemStack insertItem(int slot,  ItemStack stack, boolean simulate) {
//        return null;
//    }
//
//    
//    @Override
//    public ItemStack extractItem(int slot, int amount, boolean simulate) {
//        return null;
//    }
//
//    @Override
//    public int getSlotLimit(int slot) {
//        return 0;
//    }
//
//    @Override
//    public boolean isItemValid(int slot,  ItemStack stack) {
//        return false;
//    }
//
//    @Override
//    public boolean hasAnInventory() {
//        return false;
//    }
//
//    @Override
//    public boolean hasATank() {
//        return false;
//    }
//}
