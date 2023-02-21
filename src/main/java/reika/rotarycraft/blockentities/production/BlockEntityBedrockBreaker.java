///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.production;
//
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.sounds.SoundSource;
//import net.minecraft.world.entity.item.ItemEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.phys.AABB;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.fml.loading.FMLLoader;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.ReikaPlayerAPI;
//import reika.dragonapi.libraries.java.ReikaRandomHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.dragonapi.libraries.registry.ReikaParticleHelper;
//import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
//import reika.rotarycraft.api.Interfaces.SurrogateBedrock;
//import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
//import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
//import reika.rotarycraft.registry.*;
//
//
//public class BlockEntityBedrockBreaker extends InventoriedPowerReceiver implements InertIInv, DiscreteFunction {
//
//    private Direction facing;
//    private int step = 1;
//
//    private double dropx;
//    private double dropy;
//    private double dropz;
//
//    public Direction getFacing() {
//        return facing != null ? facing : Direction.UNKNOWN;
//    }
//
//    @Override
//    public Block getBlockEntityBlockID() {
//        return null;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        tickcount++;
//        this.readPower(false);
//        this.getIOSides(world, pos, meta);
//        if (power >= MINPOWER && torque >= MINTORQUE) {
//            int time = this.getOperationTime();
//            if (time <= 1)
//                RotaryAdvancements.INSTANTBED.triggerAchievement(this.getPlacer());
//            if (tickcount >= time) {
//                this.process(world, pos);
//                tickcount = 0;
//            }
//            int hx = x + step * facing.getStepX();
//            int hy = y + step * facing.getStepY();
//            int hz = z + step * facing.getStepZ();
//            Block b = world.getBlock(hx, hy, hz);
//            if (b != Blocks.AIR) {
//                float f = step + this.getGrindFraction() - 0.5F;
//                for (int i = 0; i < 4; i++) {
//                    double px = x + 0.5 + facing.getStepX() * f;
//                    double py = y + 0.5 + facing.getStepY() * f;
//                    double pz = z + 0.5 + facing.getStepZ() * f;
//                    if (facing.getStepX() == 0) {
//                        px = ReikaRandomHelper.getRandomPlusMinus(px, 0.5);
//                    }
//                    if (facing.getStepY() == 0) {
//                        py = ReikaRandomHelper.getRandomPlusMinus(py, 0.5);
//                    }
//                    if (facing.getStepZ() == 0) {
//                        pz = ReikaRandomHelper.getRandomPlusMinus(pz, 0.5);
//                    }
//                    ReikaParticleHelper.CRITICAL.spawnAt(world, px, py, pz);
//                }
//                //ReikaParticleHelper.CRITICAL.spawnAroundBlock(world, hx, hy, hz, 4);
//                ReikaSoundHelper.playStepSound(world, hx, hy, hz, b, 0.5F + DragonAPI.rand.nextFloat(), 0.5F * DragonAPI.rand.nextFloat());
//                //ReikaSoundHelper.playSoundAtBlock(world, hx, hy, hz, "dig.stone", );
//            }
//        }
//    }
//
//    public void process(Level world, BlockPos pos) {
//        if (this.hasInventorySpace()) {
//            BlockPos c = this.getHeadLocation();
//            if (this.canBreakAt(world, c.getX(), c.getY(), c.getZ())) {
//                this.grind(world, pos, c.getX(), c.getY(), c.getZ());
//            }
//        }
//    }
//
//    public BlockPos getHeadLocation() {
//        return new BlockPos(this).offset(this.getFacing(), step);
//    }
//
//    private boolean canBreakAt(Level world, BlockPos pos) {
//        if (y < 0)
//            return false;
//        if (y > 255)
//            return false;
//        if (y == 0 && !RotaryConfig.COMMON.VOIDHOLE.getState())
//            return false;
//        return world.isClientSide || ReikaPlayerAPI.playerCanBreakAt((WorldServer) world, pos, this.getServerPlacer());
//    }
//
//    private boolean processBlock(Level world, BlockPos pos) {
//        Block b = world.getBlockState(pos).getBlock();
//        if (this.isBedrock(world, pos))
//            return true;
//        return b == RotaryBlocks.BEDROCKSLICE.get();
//    }
//
//    private boolean hasInventorySpace() {
//        if (itemHandler.getStackInSlot(0).isEmpty())
//            return true;
//        if (!ReikaItemHelper.matchStacks(itemHandler.getStackInSlot(0), RotaryItems.bedrockdust))
//            return false;
//        return itemHandler.getStackInSlot(0).getCount() + DifficultyEffects.BEDROCKDUST.getInt() <= itemHandler.getStackInSlot(0).getMaxStackSize();
//    }
//
//    public void readPower(boolean doublesided) {
//        if (!this.getReceptor(level, worldPosition, this.getBlockMetadata()))
//            return;
//        super.getPower(doublesided);
//        power = (long) omega * (long) torque;
//    }
//
//    public boolean getReceptor(Level world, BlockPos pos, int metadata) {
//        if (pos.getY() == 0 && !RotaryConfig.COMMON.VOIDHOLE.getState())
//            return false;
//        switch (metadata) {
//            case 0 -> read = Direction.WEST;
//            case 1 -> read = Direction.EAST;
//            case 2 -> read = Direction.NORTH;
//            case 3 -> read = Direction.SOUTH;
//            case 4 -> read = Direction.DOWN;
//            case 5 -> read = Direction.UP;
//        }
//        return true;
//    }
//
//    public void getIOSides(Level world, BlockPos pos, Direction dir) {
//        switch (dir) {
//            case 0:
//                dropx = x + 0.5;
//                dropy = y + 1.25;
//                dropz = z + 0.5;
//                facing = Direction.EAST;
//                break;
//            case 1:
//                dropx = x + 0.5;
//                dropy = y + 1.25;
//                dropz = z + 0.5;
//                facing = Direction.WEST;
//                break;
//            case 2:
//                dropx = x + 0.5;
//                dropy = y + 1.25;
//                dropz = z + 0.5;
//                facing = Direction.SOUTH;
//                break;
//            case 3:
//                dropx = x + 0.5;
//                dropy = y + 1.25;
//                dropz = z + 0.5;
//                facing = Direction.NORTH;
//                break;
//            case 4:
//                dropx = x + 0.5;
//                dropy = y + 1.25;
//                dropz = z + 0.5;
//                facing = Direction.UP;
//                break;
//            case 5:
//                dropx = x + 0.5;
//                dropy = y - 0.25;
//                dropz = z + 0.5;
//                facing = Direction.DOWN;
//                break;
//        }
//    }
//
//    private boolean isBedrock(Level world, BlockPos pos) {
//        Block id = world.getBlock(pos);
//        if (id == Blocks.bedrock)
//            return true;
//        if (id == FactorizationHandler.getInstance().bedrockID)
//            return true;
//        if (id instanceof SurrogateBedrock) {
//            return ((SurrogateBedrock) id).isBedrock(world, pos);
//        }
//        return false;
//    }
//
//    private void grind(Level world, int mx, int my, int mz, BlockPos pos) {
//        if (this.processBlock(world, pos)) {
//            if (this.isBedrock(world, pos)) {
//                world.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 0.5F, DragonAPI.rand.nextFloat() * 0.4F + 0.8F, false);
//                world.setBlock(pos, RotaryBlocks.BEDROCKSLICE.get(), 0, 3);
//                ((BlockEntityBedrockSlice) world.getBlockEntity(pos)).setDirection(this.getFacing().getOpposite());
//            } else {
//                int rockmetadata = world.getBlockMetadata(pos);
//                if (rockmetadata < 15) {
//                    world.playLocalSound(x + 0.5D, y + 0.5D, z + 0.5D, "dig.stone", 0.5F, DragonAPI.rand.nextFloat() * 0.4F + 0.8F);
//                    world.setBlockMetadataWithNotify(pos, rockmetadata + 1, 3);
//                    step--;
//                    this.incrementStep(world, mx, my, mz);
//                } else {
//                    world.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.BLAZE_HURT, SoundSource.BLOCKS, 0.5F, DragonAPI.rand.nextFloat() * 0.4F + 0.8F, false);
//                    ItemStack is = this.getDrops(world, pos);
//                    world.setBlockToAir(pos);
//                    if (!this.chestCheck(world, pos, is)) {
//                        if (this.isInventoryFull())
//                            ReikaItemHelper.dropItem(world, dropx, dropy, dropz, is);
//                        else
//                            ReikaInventoryHelper.addOrSetStack(is, inv, 0);
//                    }
//                    RotaryAdvancements.BEDROCKBREAKER.triggerAchievement(this.getPlacer());
//                    MinecraftForge.EVENT_BUS.post(new BedrockDigEvent(this, pos));
//                    if (world.isClientSide)
//                        this.incrementStep(world, mx, my, mz);
//                }
//            }
//        } else {
//            Block b = world.getBlockState(pos).getBlock();
//            if (b != Blocks.AIR && b.getBlockHardness(world, pos) >= 0) {
//                ReikaSoundHelper.playBreakSound(world, pos, b);
//                if (FMLLoader.getDist() == Dist.CLIENT)
//                    ReikaRenderHelper.spawnDropParticles(world, pos, b, world.getBlockMetadata(pos));
//                world.setBlockToAir(pos);
//            }
//            if (!world.isClientSide)
//                this.incrementStep(world, mx, my, mz);
//        }
//    }
//
//    private boolean chestCheck(Level world, BlockPos pos, ItemStack is) {
//        if (is == null)
//            return false;
//        if (world.isClientSide)
//            return false;
//        for (int i = 0; i < 6; i++) {
//            Direction dir = dirs[i];
//            BlockEntity te = getAdjacentBlockEntity(dir);
//            if (te instanceof Container) {
//                boolean flag = true;
//                if (te instanceof PartialInventory) {
//                    if (!((PartialInventory) te).hasInventory())
//                        flag = false;
//                }
//                if (flag) {
//                    if (ReikaInventoryHelper.addToIInv(is, (Container) te))
//                        return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    private void incrementStep(Level world, BlockPos pos) {
//        int max = step + 1;
//        for (int i = 1; i < max; i++) {
//            int dx = x + i * facing.getStepX();
//            int dy = y + i * facing.getStepY();
//            int dz = z + i * facing.getStepZ();
//            //ReikaJavaLibrary.pConsole(step+"; "+i+" @ "+dx+","+dy+","+dz+" : "+world.getBlock(dx, dy, dz), Dist.DEDICATED_SERVER);
//            if (!ReikaWorldHelper.softBlocks(world, dx, dy, dz)) {
//                step = i;
//                return;
//            }
//        }
//        step = max;
//    }
//
//    public void dropInventory() {
//        if (itemHandler.getStackInSlot(0).isEmpty())
//            return;
//        ItemEntity itementity = new ItemEntity(level, dropx, dropy, dropz, itemHandler.getStackInSlot(0));
//        itementity.delayBeforeCanPickup = 0;
//        itementity.motionX = -0.025 + 0.05 * DragonAPI.rand.nextFloat();    // 0-0.5 m/s
//        itementity.motionZ = -0.025 + 0.05 * DragonAPI.rand.nextFloat();
//        if (meta != 5)
//            itementity.motionY = 0.1 + 0.2 * DragonAPI.rand.nextFloat() + 0.25 * DragonAPI.rand.nextFloat() * DragonAPI.rand.nextInt(2);    // 2-6m/s up, + a 50/50 chance of 0-5 m/s more up
//        itementity.velocityChanged = true;
//        if (!level.isClientSide)
//            level.addFreshEntity(itementity);
//        level.playLocalSound(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, "DragonAPI.rand.pop", 0.2F, ((DragonAPI.rand.nextFloat() - DragonAPI.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
//        itemHandler.getStackInSlot(0) = null;
//    }
//
//    private ItemStack getDrops(Level world, BlockPos pos) {
//        return ReikaItemHelper.getSizedItemStack(RotaryItems.bedrockdust, this.getNumberDust(world, pos));
//    }
//
//    private int getNumberDust(Level world, BlockPos pos) {
//        float f = Math.min(1, ((BlockEntityBedrockSlice) world.getBlockEntity(pos)).dustYield);
//        return Math.max(1, (int) (f * DifficultyEffects.BEDROCKDUST.getInt()));
//        //return DifficultyEffects.BEDROCKDUST.getInt();
//    }
//
//    public int getContents() {
//        return !itemHandler.getStackInSlot(0).isEmpty() && ReikaItemHelper.matchStacks(itemHandler.getStackInSlot(0), RotaryItems.bedrockdust) ? itemHandler.getStackInSlot(0).getCount() : 0;
//    }
//
//    private void setContents(int num) {
//        itemHandler.getStackInSlot(0) = ReikaItemHelper.getSizedItemStack(RotaryItems.bedrockdust, num);
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    protected void animateWithTick(Level world, BlockPos pos) {
//        if (!this.isInWorld()) {
//            phi = 0;
//            return;
//        }
//        if (power < MINPOWER || torque < MINTORQUE)
//            return;
//        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.BEDROCKBREAKER;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return 0;
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 1;
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int slot, ItemStack is) {
//        return false;
//    }
//
//    public boolean isInventoryFull() {
//        if (itemHandler.getStackInSlot(0).isEmpty())
//            return false;
//        if (!ReikaItemHelper.matchStacks(RotaryItems.bedrockdust, itemHandler.getStackInSlot(0)))
//            return true;
//        return itemHandler.getStackInSlot(0).getCount() >= itemHandler.getStackInSlot(0).getMaxStackSize();
//    }
//
//    @Override
//    public void onEMP() {
//    }
//
//    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//        return false;
//    }
//
//    @Override
//    public int getOperationTime() {
//        return DurationRegistry.BEDROCK.getOperationTime(omega);
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//
//        NBT.putInt("step", step);
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        step = NBT.getInt("step");
//    }
//
//    @Override
//    protected String getTEName() {
//        return null;
//    }
//
//    public int getStep() {
//        return step;
//    }
//
//    @Override
//    public AABB getRenderBoundingBox() {
//        return INFINITE_EXTENT_AABB;
//    }
//
//    public float getGrindFraction() {
//        BlockEntity te = this.getHeadLocation().getBlockEntity(level);
//        if (te instanceof BlockEntityBedrockSlice) {
//            return te.getBlockMetadata() / 16F;
//        }
//        return 0;
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
//    public boolean hasAnInventory() {
//        return false;
//    }
//
//    @Override
//    public boolean hasATank() {
//        return false;
//    }
//}
