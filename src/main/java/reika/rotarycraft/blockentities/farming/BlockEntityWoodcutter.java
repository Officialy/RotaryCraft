///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.farming;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.enchantment.Enchantment;
//import net.minecraft.world.item.enchantment.Enchantments;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.entity.BlockEntity;
//
//import net.minecraftforge.common.MinecraftForge;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.instantiable.data.blockstruct.TreeReader;
//import reika.dragonapi.instantiable.data.immutable.BlockKey;
//import reika.dragonapi.interfaces.registry.TreeType;
//import reika.dragonapi.libraries.ReikaEnchantmentHelper;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.java.ReikaRandomHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.dragonapi.libraries.registry.ReikaTreeHelper;
//import reika.dragonapi.libraries.rendering.ReikaColorAPI;
//import reika.dragonapi.modregistry.ModWoodList;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.auxiliary.MachineEnchantmentHandler;
//import reika.rotarycraft.auxiliary.interfaces.*;
//import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
//
//import reika.rotarycraft.registry.DurationRegistry;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.RotaryItems;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Comparator;
//
//public class BlockEntityWoodcutter extends InventoriedPowerReceiver implements EnchantableMachine, InertIInv, DiscreteFunction,
//        ConditionalOperation, DamagingContact, Cleanable, MultiOperational {
//
//    private static final int MAX_JAM = 20;
//    private final MachineEnchantmentHandler enchantments = new MachineEnchantmentHandler().addFilter(Enchantments.INFINITY).addFilter(Enchantments.FORTUNE).addFilter(Enchantments.EFFICIENCY);
//    private final TreeReader tree = new TreeReader();
//    public int editx;
//    public int edity;
//    public int editz;
//    public double dropx;
//    public double dropz;
//    /**
//     * For the 3x3 area of effect
//     */
//    public boolean varyx;
//    public boolean varyz;
//    public int stepx;
//    public int stepz;
//    private TreeReader treeCopy = new TreeReader();
//    private boolean cuttingTree;
//    private Comparator<BlockPos> leafPriority;
//    private int jam = 0;
//    private int jamColor = -1;
//
//    public int getJamColor() {
//        return jamColor;
//    }
//
//    public void clean() {
//        jam--;
//        if (jam <= 0) {
//            jam = 0;
//            jamColor = -1;
//        }
//    }
//
//    public boolean isJammed() {
//        return jam > MAX_JAM;
//    }
//
//    @Override
//    protected void onFirstTick(Level world, BlockPos pos) {
//        leafPriority = new LeafPrioritizer(world);
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        tickcount++;
//
//        tree.setWorld(world);
//        treeCopy.setWorld(world);
//
//        this.getIOSides(world, pos);
//        this.getPower(false);
//
//        if (power < MINPOWER || torque < MINTORQUE) {
//            return;
//        }
//
//        if (this.isJammed())
//            return;
//
//        if (world.isClientSide)
//            return;
//
//        if (tree.isEmpty() && this.hasWood()) {
//            tree.reset();
//            TreeType type = ReikaTreeHelper.getTree(world.getBlock(editx, edity, editz), world.getBlockMetadata(editx, edity, editz));
//            if (type == null)
//                type = ModWoodList.getModWood(world.getBlock(editx, edity, editz), world.getBlockMetadata(editx, edity, editz));
//
//            if (type != null) {
//                tree.setTree(type);
//                for (int i = -1; i <= 1; i++) {
//                    for (int j = -1; j <= 1; j++) {
//                        tree.addTree(world, editx + i, edity, editz + j);
//                    }
//                }
//            }
//
//            if (!tree.isEmpty()) {
//                cuttingTree = true;
//            }
//
//            this.checkAndMatchInventory();
//
//            tree.sortBlocksByHeight(false);
//            tree.reverseBlockOrder();
//            tree.sortBlocksByDistance(new BlockPos(this));
//            tree.sort(leafPriority);
//            treeCopy = (TreeReader) tree.copy();
//        }
//
//        Block b = world.getBlock(x, y + 1, z);
//        if (b != Blocks.AIR) {
//            if (b.defaultBlockState().getMaterial() == Material.wood || b.defaultBlockState().getMaterial() == Material.leaves) {
//                ReikaItemHelper.dropItems(world, dropx, y - 0.25, dropz, this.getDrops(world, x, y + 1, z, b, world.getBlockMetadata(pos)));
//                world.setBlockToAir(x, y + 1, z);
//            }
//        }
//
//        //RotaryCraft.LOGGER.debug(tree);
//
//        if (tree.isEmpty())
//            return;
//
//        if (tickcount < this.getOperationTime())
//            return;
//        tickcount = 0;
//
//        if (!cuttingTree && !tree.isValidTree()) {
//            tree.reset();
//            tree.clear();
//            return;
//        }
//
//        for (int i = 0; i < this.getNumberConsecutiveOperations(); i++) {
//            BlockPos c = tree.getNextAndMoveOn();
//            this.cutCoord(world, pos, c);
//            if (tree.isEmpty())
//                break;
//        }
//    }
//
//    private void cutCoord(Level world, BlockPos pos, BlockPos c) {
//        Block drop = c.getBlock(world);
//        int dropmeta = c.getBlockMetadata(world);
//
//        if (drop != Blocks.AIR) {
//            Material mat = ReikaWorldHelper.defaultBlockState().getMaterial(world, c.xCoord, c.yCoord, c.zCoord);
//            if (RotaryConfig.COMMON.INSTACUT.getState()) {
//                //ReikaItemHelper.dropItems(world, dropx, y-0.25, dropz, dropBlocks.getDrops(world, c.xCoord, c.yCoord, c.zCoord, dropmeta, 0));
//                this.cutBlock(world, pos, c, mat);
//
//                if (c.yCoord == edity && drop == tree.getTreeType().getLogID()) {
//                    Block idbelow = world.getBlock(c.xCoord, c.yCoord - 1, c.zCoord);
//                    Block root = TwilightForestHandler.BlockEntry.ROOT.getBlock();
//                    if (ReikaPlantHelper.SAPLING.canPlantAt(world, c.xCoord, c.yCoord, c.zCoord)) {
//                        BlockKey plant = this.getPlantedSapling();
//                        if (plant != null) {
//                            if (!itemHandler.getStackInSlot(0).isEmpty() && !enchantments.hasEnchantment(Enchantment.infinity))
//                                ReikaInventoryHelper.decrStack(0, inv);
//                            plant.place(world, c.xCoord, c.yCoord, c.zCoord);
//                        }
//                    } else if (tree.getTreeType() == ModWoodList.TIMEWOOD && (idbelow == root || idbelow == Blocks.AIR)) {
//                        BlockKey plant = this.getPlantedSapling();
//                        if (plant != null) {
//                            if (!itemHandler.getStackInSlot(0).isEmpty() && !enchantments.hasEnchantment(Enchantment.infinity))
//                                ReikaInventoryHelper.decrStack(0, inv);
//                            world.setBlock(c.xCoord, c.yCoord - 1, c.zCoord, Blocks.DIRT);
//                            plant.place(world, c.xCoord, c.yCoord, c.zCoord);
//                        }
//                    }
//                }
//            } else {
//                boolean fall = BlockSand.func_149831_e(world, c.xCoord, c.yCoord - 1, c.zCoord);
//                if (fall) {
//                    EntityFallingBlock e = new EntityFallingBlock(world, c.xCoord + 0.5, c.yCoord + 0.65, c.zCoord + 0.5, drop, dropmeta);
//                    e.field_145812_b = -5000;
//                    if (!world.isClientSide) {
//                        world.addFreshEntity(e);
//                    }
//                    c.setBlock(world, Blocks.AIR);
//                } else {
//                    this.cutBlock(world, pos, c, mat);
//                    if (c.yCoord == edity) {
//                        Block idbelow = world.getBlock(c.xCoord, c.yCoord - 1, c.zCoord);
//                        Block root = TwilightForestHandler.BlockEntry.ROOT.getBlock();
//                        if (ReikaPlantHelper.SAPLING.canPlantAt(world, c.xCoord, c.yCoord, c.zCoord)) {
//                            BlockKey plant = this.getPlantedSapling();
//                            if (plant != null) {
//                                if (!itemHandler.getStackInSlot(0).isEmpty() && !enchantments.hasEnchantment(Enchantment.infinity))
//                                    ReikaInventoryHelper.decrStack(0, inv);
//                                plant.place(world, c.xCoord, c.yCoord, c.zCoord);
//                            }
//                        } else if (tree.getTreeType() == ModWoodList.TIMEWOOD && (idbelow == root || idbelow == Blocks.AIR)) {
//                            BlockKey plant = this.getPlantedSapling();
//                            if (plant != null) {
//                                if (!itemHandler.getStackInSlot(0).isEmpty() && !enchantments.hasEnchantment(Enchantment.infinity))
//                                    ReikaInventoryHelper.decrStack(0, inv);
//                                world.setBlock(c.xCoord, c.yCoord - 1, c.zCoord, Blocks.DIRT);
//                                plant.place(world, c.xCoord, c.yCoord, c.zCoord);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private void cutBlock(Level world, BlockPos pos, BlockPos c, Material mat) {
//        //ReikaItemHelper.dropItems(world, dropx, y-0.25, dropz, dropBlocks.getDrops(world, c.xCoord, c.yCoord, c.zCoord, dropmeta, 0));
//        this.dropBlocks(world, c.xCoord, c.yCoord, c.zCoord);
//        c.setBlock(world, Blocks.AIR);
//
//        if (mat == Material.leaves)
//            world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "dig.grass", 0.5F + DragonAPI.rand.nextFloat() * 0.5F, 1F);
//        else
//            world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "dig.wood", 0.5F + DragonAPI.rand.nextFloat() * 0.5F, 1F);
//        if (tree.getTreeType() == ModWoodList.SLIME && c.getBlock(world) == tree.getTreeType().getLogID()) {
//            jam++;
//            jamColor = 0xff000000 | ReikaColorAPI.mixColors(ModWoodList.SLIME.logColor, 0xffffff, (float) jam / MAX_JAM);
//        }
//    }
//
//    private Collection<ItemStack> getDrops(Level world, BlockPos pos, Block b) {
//        float f = this.getYield(b, meta);
//        if (ReikaRandomHelper.doWithChance(f)) {
//            int fortune = enchantments.getEnchantment(Enchantment.fortune);
//            ArrayList<ItemStack> ret = b.getDrops(world, pos, meta, fortune);
//            MinecraftForge.EVENT_BUS.post(new HarvestDropsEvent(pos, world, b, meta, fortune, 1, ret, this.getPlacer(), false));
//            if (tree.getTreeType() == ModWoodList.SLIME) {
//                Block log = tree.getTreeType().getLogID();
//                if (b == log) {
//                    ret.clear();
//                    ret.add(new ItemStack(Items.slime_ball));
//                }
//            } else if (tree.getTreeType() == ReikaTreeHelper.OAK || tree.isDyeTree()) {
//                int n = 0;
//                if (fortune >= 5) {
//                    n = 4;
//                } else if (fortune >= 3) {
//                    n = 6;
//                } else if (fortune >= 2) {
//                    n = 10;
//                } else if (fortune >= 1) {
//                    n = 20;
//                }
//                if (n > 0 && DragonAPI.rand.nextInt(n) == 0) {
//                    ret.add(new ItemStack(Items.apple));
//                }
//            }
//            return ret;
//        } else
//            return new ArrayList<>();
//    }
//
//    private float getYield(Block b) {
//        if (tree.getTreeType() == ModWoodList.SLIME) {
//            Block log = tree.getTreeType().getLogID();
//            if (b == log) {
//                return 0.6F;
//            }
//        }
//        return 1;
//    }
//
//    private void checkAndMatchInventory() {
//        BlockKey sapling = null;
//        if (tree.isDyeTree()) {
//            sapling = new BlockKey(ChromatiAPI.trees.getDyeSapling(), tree.getDyeTreeMeta());
//        } else if (tree.getTreeType() != null) {
//            sapling = tree.getSapling();
//        }
//        if (!ReikaItemHelper.matchStacks(itemHandler.getStackInSlot(0), sapling)) {
//            this.dumpInventory();
//        }
//    }
//
//    private void dropBlocks(Level world, BlockPos pos) {
//        Block drop = world.getBlock(pos);
//        if (drop == Blocks.AIR)
//            return;
//        int dropmeta = world.getBlockMetadata(pos);
//        BlockKey sapling = tree.getSapling();
//        Block logID = tree.getTreeType().getLogID();
//
//        Collection<ItemStack> drops = this.getDrops(world, pos, drop, dropmeta);
//        if (drop == logID && logID != null) {
//            if (tree.getTreeType() != ModWoodList.SLIME) {
//                if (DragonAPI.rand.nextInt(3) == 0) {
//                    drops.add(ReikaItemHelper.getSizedItemStack(RotaryItems.sawdust.copy(), 1 + DragonAPI.rand.nextInt(4)));
//                }
//            }
//        }
//
//        for (ItemStack todrop : drops) {
//            if (ReikaItemHelper.matchStacks(todrop, sapling)) {
//                if (!itemHandler.getStackInSlot(0).isEmpty() && itemHandler.getStackInSlot(0).getCount() >= itemHandler.getStackInSlot(0).getMaxStackSize()) {
//                    this.chestCheck(todrop);
//                    if (todrop.getCount() > 0)
//                        ReikaItemHelper.dropItem(world, dropx, yCoord - 0.25, dropz, todrop);
//                } else
//                    ReikaInventoryHelper.addOrSetStack(todrop, inv, 0);
//            } else {
//                this.chestCheck(todrop);
//                if (todrop.getCount() > 0)
//                    ReikaItemHelper.dropItem(world, dropx, yCoord - 0.25, dropz, todrop);
//            }
//        }
//    }
//
//    private void chestCheck(ItemStack is) {
//        BlockEntity te = level.getBlockEntity(xCoord, yCoord - 1, zCoord);
//        if (te instanceof Container) {
//            Container ii = (Container) te;
//
//            //build in auto collation
//            int max = Math.min(ii.getInventoryStackLimit(), is.getMaxStackSize());
//            for (int i = 0; i < ii.getContainerSize(); i++) {
//                ItemStack in = ii.getStackInSlot(i);
//                if (in != null && ReikaItemHelper.areStacksCombinable(is, in, max)) {
//                    int fit = max - in.getCount();
//                    int add = Math.min(fit, is.getCount());
//                    if (add > 0) {
//                        is.getCount() -= add;
//                        in.getCount() += add;
//                    }
//                    if (is.getCount() <= 0)
//                        return;
//                }
//            }
//
//            if (ReikaInventoryHelper.addToIInv(is, ii)) {
//                is.getCount() = 0;
//            }
//        }
//    }
//
//    private void dumpInventory() {
//        if (itemHandler.getStackInSlot(0).isEmpty())
//            return;
//        ItemStack is = itemHandler.getStackInSlot(0).copy();
//        itemHandler.getStackInSlot(0) = null;
//        this.chestCheck(is);
//    }
//
//    public BlockKey getPlantedSapling() {
//        if (!this.shouldPlantSapling())
//            return null;
//        if (treeCopy.isDyeTree())
//            return new BlockKey(ChromatiAPI.trees.getDyeSapling(), treeCopy.getDyeTreeMeta());
//        else if (treeCopy.isRainbowTree())
//            return new BlockKey(ChromatiAPI.trees.getRainbowSapling());
//        else if (treeCopy.getTreeType() != null)
//            return treeCopy.getSapling();
//        else
//            return null;
//    }
//
//    private boolean shouldPlantSapling() {
//        if (enchantments.hasEnchantment(Enchantment.infinity))
//            return true;
//        if (treeCopy.isDyeTree()) {
//            return !itemHandler.getStackInSlot(0).isEmpty() && itemHandler.getStackInSlot(0).getCount() > 0 && ReikaItemHelper.matchStackWithBlock(itemHandler.getStackInSlot(0), ChromatiAPI.trees.getDyeSapling()) && itemHandler.getStackInSlot(0).getItemDamage() == treeCopy.getDyeTreeMeta();
//        } else if (treeCopy.getTreeType() != null) {
//            return !itemHandler.getStackInSlot(0).isEmpty() && itemHandler.getStackInSlot(0).getCount() > 0 && ReikaItemHelper.matchStacks(itemHandler.getStackInSlot(0), treeCopy.getSapling());
//        } else
//            return false;
//    }
//
//    public void getIOSides(Level world, BlockPos pos, int metadata) {
//        switch (metadata) {
//            case 0:
//                read = Direction.EAST;
//                editx = x - 1;
//                edity = y;
//                editz = z;
//                dropx = x + 1 + 0.125;
//                dropz = z + 0.5;
//                stepx = 1;
//                stepz = 0;
//                varyx = false;
//                varyz = true;
//                break;
//            case 1:
//                read = Direction.WEST;
//                editx = x + 1;
//                edity = y;
//                editz = z;
//                dropx = x - 0.125;
//                dropz = z + 0.5;
//                stepx = -1;
//                stepz = 0;
//                varyx = false;
//                varyz = true;
//                break;
//            case 2:
//                read = Direction.SOUTH;
//                editx = x;
//                edity = y;
//                editz = z - 1;
//                dropx = x + 0.5;
//                dropz = z + 1 + 0.125;
//                stepx = 0;
//                stepz = 1;
//                varyx = true;
//                varyz = false;
//                break;
//            case 3:
//                read = Direction.NORTH;
//                editx = x;
//                edity = y;
//                editz = z + 1;
//                dropx = x + 0.5;
//                dropz = z - 0.125;
//                stepx = 0;
//                stepz = -1;
//                varyx = true;
//                varyz = false;
//                break;
//        }
//        dropx = x + 0.5;
//        dropz = z + 0.5;
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
//        if (power < MINPOWER || torque < MINTORQUE || this.isJammed())
//            return;
//        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.WOODCUTTER;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        if (!this.hasWood())
//            return 15;
//        return 0;
//    }
//
//    private boolean hasWood() {
//        for (int i = -1; i <= 1; i++) {
//            for (int j = -1; j <= 1; j++) {
//                Block id = level.getBlock(editx + i, edity, editz + j);
//                if (id == Blocks.log || id == Blocks.log2)
//                    return true;
//                ModWoodList wood = ModWoodList.getModWood(id, meta);
//                //RotaryCraft.LOGGER.debug("Retrieved wood "+wood+" from "+id+":"+meta);
//                if (wood != null)
//                    return true;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//
//        NBT.putInt("jam", jam);
//        NBT.putInt("jamc", jamColor);
//
//        NBT.putBoolean("cutting", cuttingTree);
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        jam = NBT.getInt("jam");
//        jamColor = NBT.getInt("jamc");
//
//        cuttingTree = NBT.getBoolean("cutting");
//    }
//
//    @Override
//    public void saveAdditional(CompoundTag NBT) {
//        super.saveAdditional(NBT);
//
//        NBT.put("enchants", enchantments.saveAdditional());
//    }
//
//    @Override
//    public void load(CompoundTag NBT) {
//        super.load(NBT);
//
//        enchantments.load(NBT.getTagList("enchants", NBTTypes.COMPOUND.ID));
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
//        if (RotaryConfig.COMMON.INSTACUT.getState()) {
//            int base = DurationRegistry.WOODCUTTER.getOperationTime(omega);
//            float ench = ReikaEnchantmentHelper.getEfficiencyMultiplier(enchantments.getEnchantment(Enchantment.efficiency));
//            return (int) (base / ench);
//        }
//        return 0;
//    }
//
//    @Override
//    public int getNumberConsecutiveOperations() {
//        return DurationRegistry.WOODCUTTER.getNumberOperations(omega);
//    }
//
//    @Override
//    public boolean areConditionsMet() {
//        return this.hasWood();
//    }
//
//    @Override
//    public String getOperationalStatus() {
//        return this.areConditionsMet() ? "Operational" : "No Tree";
//    }
//
//    @Override
//    public int getContactDamage() {
//        return 4;
//    }
//
//    public boolean canDealDamage() {
//        return power >= MINPOWER && torque >= MINTORQUE;
//    }
//
//    @Override
//    public DamageSource getDamageType() {
//        return RotaryCraft.grind;
//    }
//
//    @Override
//    public MachineEnchantmentHandler getEnchantmentHandler() {
//        return enchantments;
//    }
//
//    private static class LeafPrioritizer extends BlockTypePrioritizer {
//
//        private LeafPrioritizer(Level world) {
//            super(world);
//        }
//
//        @Override
//        protected int compare(BlockKey b1, BlockKey b2) {
//            boolean l1 = this.isLeaf(b1);
//            boolean l2 = this.isLeaf(b2);
//            if (l1 && l2) {
//                return 0;
//            } else if (l1) {
//                return -1;
//            } else if (l2) {
//                return 1;
//            } else {
//                return 0;
//            }
//        }
//
//        private boolean isLeaf(BlockKey bk) {
//            if (bk.blockID.defaultBlockState().getMaterial() == Material.LEAVES)
//                return true;
//            if (bk.blockID == Blocks.LEAVES || bk.blockID == Blocks.leaves2)
//                return true;
//            return ModWoodList.isModLeaf(bk.blockID, bk.metadata);
//        }
//
//    }
//}
