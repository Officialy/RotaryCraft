///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.level;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.biome.Biome;
//import net.minecraft.world.level.biome.Biomes;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.material.Fluids;
//import net.neoforged.fluids.FluidStack;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.instantiable.ItemReq;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.ReikaPlayerAPI;
//import reika.dragonapi.libraries.java.ReikaJavaLibrary;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
//import reika.rotarycraft.auxiliary.interfaces.SelectableTiles;
//import reika.rotarycraft.base.blockentity.InventoriedPowerLiquidReceiver;
//
//import reika.rotarycraft.registry.DurationRegistry;
//import reika.rotarycraft.registry.MachineRegistry;
//
//import java.util.*;
//
//public class BlockEntityTerraformer extends InventoriedPowerLiquidReceiver implements SelectableTiles, DiscreteFunction {
//
//    private static final ObjectWeb<Biome> transforms = new ObjectWeb();
//    private static final HashMap<BiomeStep, Collection<ItemReq>> itemReqs = new HashMap();
//    private static final HashMap<BiomeStep, Integer> powerReqs = new HashMap();
//    private static final HashMap<BiomeStep, FluidStack> liquidReqs = new HashMap();
//
//    static {
//        addBiomeTransformation(Biomes.DESERT, Biomes.SAVANNA, 65536, Fluids.getFluidStack("water", 30), new ItemReq(Blocks.TALL_GRASS, 1, 0.5F), new ItemReq(Blocks.sapling, 4, 0.05F));
//        addBiomeTransformation(Biomes.SAVANNA, Biomes.PLAINS, 32768, Fluids.getFluidStack("water", 20), new ItemReq(Blocks.TALL_GRASS, 1, 0.3F));
//        addBiomeTransformation(Biomes.PLAINS, Biomes.FOREST, 131072, Fluids.getFluidStack("water", 10), new ItemReq(Blocks.sapling, 0, 0.5F), new ItemReq(Blocks.sapling, 2, 0.2F));
//        addBiomeTransformation(Biomes.FOREST, Biomes.JUNGLE, 262144, Fluids.getFluidStack("water", 50), new ItemReq(Blocks.sapling, 0, 0.4F), new ItemReq(Blocks.sapling, 0, 0.6F), new ItemReq(Blocks.TALL_GRASS, 2, 0.3F));
//        addBiomeTransformation(Biomes.PLAINS, Biomes.SWAMPLAND, 32768, Fluids.getFluidStack("water", 100), new ItemReq(Blocks.sapling, 0, 0.1F), new ItemReq(Blocks.red_mushroom, 0.05F), new ItemReq(Blocks.brown_mushroom, 0.15F));
//        addBiomeTransformation(Biomes.SWAMPLAND, Biomes.OCEAN, 131072, Fluids.getFluidStack("water", 500));
//        addBiomeTransformation(Biomes.OCEAN, Biomes.FROZENOCEAN, 1024, null, new ItemReq(Blocks.ICE, 1));
//        addBiomeTransformation(Biomes.PLAINS, Biomes.EXTREMEHILLS, 65536, null, new ItemReq(Blocks.sapling, 0, 0.05F));
//        addBiomeTransformation(Biomes.PLAINS, Biomes.ICEPLAINS, 8192, null, new ItemReq(Blocks.SNOW, 1), new ItemReq(Blocks.sapling, 0, 0.05F));
//        addBiomeTransformation(Biomes.ICEPLAINS, Biomes.PLAINS, 524288, null, new ItemReq(Blocks.TALL_GRASS, 1, 0.7F));
//        addBiomeTransformation(Biomes.OCEAN, Biomes.MUSHROOMISLAND, 1048576, null, new ItemReq(Blocks.DIRT, 1), new ItemReq(Blocks.mycelium, 1), new ItemReq(Blocks.red_mushroom, 0.9F), new ItemReq(Blocks.brown_mushroom, 0.9F)); //?
//        addBiomeTransformation(Biomes.MUSHROOMISLAND, Biomes.EXTREMEHILLS, 262144, null, new ItemReq(Blocks.GRASS, 0.125F), new ItemReq(Blocks.sapling, 0, 0.05F), new ItemReq(Blocks.TALL_GRASS, 1, 0.25F)); //?
//        addBiomeTransformation(Biomes.FOREST, Biomes.TAIGA, 131072, null, new ItemReq(Blocks.sapling, 1, 0.25F));
//        addBiomeTransformation(Biomes.FOREST, Biomes.COLDTAIGA, 131072, null, new ItemReq(Blocks.SNOW, 0.3F), new ItemReq(Blocks.sapling, 1, 0.25F));
//        addBiomeTransformation(Biomes.FOREST, Biomes.ROOFEDFOREST, 65536, Fluids.getFluidStack("water", 40), new ItemReq(Blocks.sapling, 5, 0.5F));
//        addBiomeTransformation(Biomes.FOREST, Biomes.BIRCHFOREST, 32768, null, new ItemReq(Blocks.sapling, 2, 0.25F));
//        addBiomeTransformation(Biomes.TAIGA, Biomes.COLDTAIGA, 32768, null, new ItemReq(Blocks.SNOW, 0.3F));
//        addBiomeTransformation(Biomes.TAIGA, Biomes.ICEPLAINS, 65536, null, new ItemReq(Blocks.SNOW, 1), new ItemReq(Blocks.sapling, 0, 0.05F));
//        addBiomeTransformation(Biomes.TAIGA, Biomes.forest, 131072, null, new ItemReq(Blocks.sapling, 0, 0.4F), new ItemReq(Blocks.sapling, 2, 0.1F));
//        addBiomeTransformation(Biomes.TAIGA, Biomes.MEGATAIGA, 32768, Fluids.getFluidStack("water", 20), new ItemReq(Blocks.sapling, 1, 0.1F));
//        addBiomeTransformation(Biomes.ICEPLAINS, Biomes.FROZENOCEAN, 32768, Fluids.getFluidStack("water", 100), new ItemReq(Blocks.ICE, 1));
//        addBiomeTransformation(Biomes.PLAINS, Biomes.SAVANNA, 65536, null, new ItemReq(Blocks.sapling, 4, 0.05F));
//        addBiomeTransformation(Biomes.SAVANNA, Biomes.DESERT, 65536, null, new ItemReq(Blocks.SAND, 1), new ItemReq(Blocks.SANDSTONE, 0.5F), new ItemReq(Blocks.cactus, 0.1F));
//        addBiomeTransformation(Biomes.FOREST, Biomes.PLAINS, 262144, null, new ItemReq(Blocks.TALL_GRASS, 1, 0.8F));
//        addBiomeTransformation(Biomes.JUNGLE, Biomes.FOREST, 65536, null, new ItemReq(Blocks.sapling, 0, 0.5F), new ItemReq(Blocks.sapling, 2, 0.2F));
//        addBiomeTransformation(Biomes.SWAMPLAND, Biomes.PLAINS, 262144, null, new ItemReq(Blocks.TALL_GRASS, 1, 0.8F), new ItemReq(Blocks.DIRT, 0.8F));
//        addBiomeTransformation(Biomes.OCEAN, Biomes.SWAMPLAND, 524288, null, new ItemReq(Blocks.sapling, 0, 0.1F), new ItemReq(Blocks.red_mushroom, 0.05F), new ItemReq(Blocks.brown_mushroom, 0.15F), new ItemReq(Blocks.GRASS, 0.125F));
//        addBiomeTransformation(Biomes.FROZENOCEAN, Biomes.OCEAN, 524288, null);
//        addBiomeTransformation(Biomes.EXTREMEHILLS, Biomes.PLAINS, 262144, null, new ItemReq(Blocks.TALL_GRASS, 1, 0.6F));
//        addBiomeTransformation(Biomes.ICEPLAINS, Biomes.TAIGA, 65536, null, new ItemReq(Blocks.sapling, 1, 0.4F));
//        addBiomeTransformation(Biomes.FROZENOCEAN, Biomes.ICEPLAINS, 65536, null, new ItemReq(Blocks.sapling, 0, 0.05F), new ItemReq(Blocks.DIRT, 1), new ItemReq(Blocks.GRASS, 0.125F));
//        addBiomeTransformation(Biomes.DESERT, Biomes.MESA, 32768, null, new ItemReq(Blocks.clay, 0.2F));
//        addBiomeTransformation(Biomes.OCEAN, Biomes.DEEPOCEAN, 1024, Fluids.getFluidStack("water", 200));
//        addBiomeTransformation(Biomes.MESA, Biomes.DESERT, 16384, null, new ItemReq(Blocks.SAND, 0.5F), new ItemReq(Blocks.SANDSTONE, 0.1F));
//        addBiomeTransformation(Biomes.ROOFEDFOREST, Biomes.FOREST, 32768, null, new ItemReq(Blocks.sapling, 0, 0.5F), new ItemReq(Blocks.sapling, 2, 0.2F));
//        addBiomeTransformation(Biomes.BIRCHFOREST, Biomes.FOREST, 32768, null, new ItemReq(Blocks.sapling, 0, 0.5F));
//    }
//
//    private final ColumnArray coords = new ColumnArray();
//    private Comparator<BlockPos> positionComparator;
//    private Biome target;
//
//    private static void addBiomeTransformation(Biome from, Biome to, int power, FluidStack liq, ItemReq... items) {
//        ArrayList<Biome> li = new ArrayList<>();
//        li.add(from);
//        li.addAll(ReikaBiomeHelper.getChildBiomes(from));
//        for (Biome from_ : li) {
//            BiomeStep step = new BiomeStep(from_, to);
//            transforms.addDirectionalConnection(from_, to);
//            itemReqs.put(step, ReikaJavaLibrary.makeListFromArray(items));
//            powerReqs.put(step, power);
//            liquidReqs.put(step, liq);
//        }
//    }
//
//    /**
//     * Returns the valid transformations registered to the terraformer.
//     */
//    public static ArrayList<BiomeTransform> getTransformList() {
//        ArrayList<BiomeTransform> li = new ArrayList<>();
//        for (int i = 0; i < Biome.biomeList.length; i++) {
//            Biome start = Biome.biomeList[i];
//            if (transforms.hasNode(start)) {
//                Collection<Biome> tgs = transforms.getChildren(start);
//                for (Biome to : tgs) {
//                    BiomeStep step = new BiomeStep(start, to);
//                    long power = powerReqs.get(step);
//                    FluidStack fs = liquidReqs.get(step);
//                    Collection<ItemReq> items = itemReqs.get(step);
//                    li.add(new BiomeTransform(step, power, fs, items));
//                }
//            }
//        }
//        return li;
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 54;
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int slot, ItemStack is) {
//        return true;
//    }
//
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.TERRAFORMER;
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
//    protected void onFirstTick(Level world, BlockPos pos) {
//        positionComparator = new PositionComparator(this);
//        this.getCoordsFromIAP(world, pos);
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        this.getSummativeSidedPower();
//        tickcount++;
//
//        //ReikaJavaLibrary.pConsoleOnlyIn(String.format("Tick %2d: ", tickcount)+coords, Dist.DEDICATED_SERVER);
//        //ReikaJavaLibrary.pConsole(this.getValidTargetBiomes(this.getCentralBiome()));
//        //ReikaJavaLibrary.pConsole(coords.getSize(), Dist.DEDICATED_SERVER);
//		/*
//		if (coords.isEmpty()) {
//			for (int i = -16; i <= 16; i++) {
//				for (int j = -16; j <= 16; j++) {
//					coords.add(x+i, z+j);
//				}
//			}
//			}*/
//
//        //ReikaJavaLibrary.pConsole(coords, Dist.DEDICATED_SERVER);
//
//        if (coords.isEmpty()) {
//            return;
//        }
//
//        if (!this.hasRedstoneSignal())
//            return;
//
//        //ReikaJavaLibrary.pConsole(String.format("Tick %2d: ", tickcount)+this.getOperationTime(), Dist.DEDICATED_SERVER);
//
//        if (tickcount >= this.getOperationTime()) {
//            int index = DragonAPI.rand.nextInt(coords.getSize());
//            BlockPos xz = coords.getNthColumn(index);
//            if (!world.isClientSide) {
//                if (this.setBiome(world, xz.xCoord, xz.zCoord)) {
//                    //ReikaJavaLibrary.pConsole(Arrays.toString(xz), Dist.DEDICATED_SERVER);
//                    //ReikaJavaLibrary.pConsole("Removing "+x+", "+z);
//                    coords.remove(index);
//                }
//                tickcount = 0;
//            }
//        }
//    }
//
//    private void getCoordsFromIAP(Level world, BlockPos pos) {
//        coords.clear();
//        for (int i = 2; i < 6; i++) {
//            Direction dir = dirs[i];
//            int dx = x + dir.getStepX();
//            int dy = y + dir.getStepY();
//            int dz = z + dir.getStepZ();
//            BlockEntity te = world.getBlockEntity(dx, dy, dz);
//            if (InterfaceCache.AREAPROVIDER.instanceOf(te)) {
//                IAreaProvider iap = (IAreaProvider) te;
//                for (int mx = iap.xMin(); mx <= iap.xMax(); mx++) {
//                    for (int mz = iap.zMin(); mz <= iap.zMax(); mz++) {
//                        this.addCoordinate(mx, mz, false);
//                    }
//                }
//                iap.removeFromWorld();
//                coords.sort(positionComparator);
//                return;
//            }
//        }
//    }
//
//    public int[] getUniqueID() {
//        return new int[]{xCoord, yCoord, zCoord};
//    }
//
//    private boolean setBiome(Level world, int x, int z) {
//        if (!world.isClientSide && !ReikaPlayerAPI.playerCanBreakAt((WorldServer) world, x, yCoord, z, this.getServerPlacer()))
//            return false;
//        Biome from = world.getBiomeGenForCoords(x, z);
//        if (!this.isValidTarget(from)) {
//            return false;
//        }
//        if (!DragonAPI.debugtest && !this.getReqsForTransform(from, target))
//            return false;
//        //ReikaJavaLibrary.pConsole("Setting biome @ "+x+", "+z+" to "+target.biomeName);
//        if (this.modifyBlocks())
//            ReikaWorldHelper.setBiomeAndBlocksForXZ(world, x, z, target);
//        else
//            ReikaWorldHelper.setBiomeForXZ(world, x, z, target);
//        return true;
//    }
//
//    public boolean modifyBlocks() {
//        return RotaryConfig.COMMON.BIOMEBLOCKS.get() && ReikaInventoryHelper.checkForItem(Items.diamond, inv);
//    }
//
//    private void addCoordinate(int x, int z, boolean sort) {
//        if (this.hasRedstoneSignal())
//            return;
//        Biome biome = level.getBiomeGenForCoords(x, z);
//        if (coords.add(x, z)) {
//            RotaryCraft.LOGGER.debug("Added coordinate " + x + "x, " + z + "z to " + this);
//            if (sort)
//                coords.sort(positionComparator);
//        }
//    }
//
//    public void addTile(BlockPos pos) {
//        this.addCoordinate(x, z, true);
//    }
//
//    @Override
//    public boolean canConnectToPipe(MachineRegistry m) {
//        return m.isStandardPipe();
//    }
//
//    private boolean isValidTarget(Biome from) {
//        return transforms.isDirectionallyConnectedTo(from, target);
//    }
//
//    public FluidStack getReqLiquidForTransform(Biome from, Biome to) {
//        BiomeStep li = new BiomeStep(from, to);
//        FluidStack liq = liquidReqs.get(li);
//        return liq;
//    }
//
//    public ArrayList<ItemStack> getItemsForTransform(Biome from, Biome to) {
//        BiomeStep li = new BiomeStep(from, to);
//        ArrayList<ItemStack> is = new ArrayList<>();
//        Collection<ItemReq> req = itemReqs.get(li);
//        if (req != null) {
//            for (ItemReq r : req) {
//                is.add(r.asItemStack());
//            }
//        }
//        return is;
//    }
//
//    private boolean getReqsForTransform(Biome from, Biome to) { //test and consume resources
//        BiomeStep li = new BiomeStep(from, to);
//        int min = powerReqs.get(li);
//        FluidStack liq = liquidReqs.get(li);
//        Collection<ItemReq> items = itemReqs.get(li);
//
//        if (power < min)
//            return false;
//
//        if (liq != null) {
//            if (tank.getLevel() < liq.amount)
//                return false;
//        }
//
//        if (items != null) {
//            for (ItemReq is : items) {
//                if (!ReikaInventoryHelper.checkForItemStack(is.itemID, is.metadata, inv))
//                    return false;
//            }
//
//            //We have everything by this point
//            for (ItemReq is : items) {
//                if (is.callAndConsume()) {
//                    int slot = ReikaInventoryHelper.locateInInventory(is.itemID, is.metadata, inv);
//                    ReikaInventoryHelper.decrStack(slot, inv);
//                }
//            }
//        }
//        if (liq != null)
//            tank.removeLiquid(liq.amount);
//        return true;
//    }
//
//    public Biome getTarget() {
//        return target;
//    }
//
//    public void setTarget(Biome tg) {
//        target = tg;
//    }
//
//    public Collection<Biome> getValidTargetBiomes(Biome start) {
//        return transforms.getChildren(start);
//    }
//
//    public Biome getCentralBiome() {
//        return level.getBiomeGenForCoords(xCoord, zCoord);
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        int tg = NBT.getInt("tg");
//        if (tg != -1)
//            target = Biome.biomeList[tg];
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//
//        if (target != null)
//            NBT.putInt("tg", target.biomeID);
//        else
//            NBT.putInt("tg", -1);
//    }
//
//    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//        return false;
//    }
//
//    @Override
//    public Fluid getInputFluid() {
//        return Fluids.WATER;
//    }
//
//    @Override
//    public int getCapacity() {
//        return 24000;
//    }
//
//    @Override
//    public boolean canReceiveFrom(Direction from) {
//        return true;
//    }
//
//    @Override
//    public int getOperationTime() {
//        return DurationRegistry.TERRAFORMER.getOperationTime(omega);
//    }
//
//    public static final class BiomeStep {
//        public final Biome start;
//        public final Biome finish;
//
//        private BiomeStep(Biome in, Biome out) {
//            start = in;
//            finish = out;
//        }
//
//        @Override
//        public int hashCode() {
//            return start.hashCode() ^ finish.hashCode();
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (o instanceof BiomeStep) {
//                BiomeStep b = (BiomeStep) o;
//                return b.start == start && b.finish == finish;
//            }
//            return false;
//        }
//    }
//
//    public static final class BiomeTransform {
//
//        public final BiomeStep change;
//        public final long power;
//        private final FluidStack fluid;
//        private final Collection<ItemReq> items;
//
//        private BiomeTransform(BiomeStep step, long power, FluidStack fs, Collection<ItemReq> li) {
//            change = step;
//            this.power = power;
//            fluid = fs;
//            items = li;
//        }
//
//        public FluidStack getFluid() {
//            return fluid != null ? fluid.copy() : null;
//        }
//
//        public Collection<ItemReq> getItems() {
//            return Collections.unmodifiableCollection(items);
//        }
//    }
//
//    private static class PositionComparator implements Comparator<BlockPos> {
//
//        private final BlockPos origin;
//
//        private PositionComparator(BlockEntityTerraformer te) {
//            origin = new BlockPos(te);
//        }
//
//        @Override
//        public int compare(BlockPos o1, BlockPos o2) {
//            if (o1.equals(o2))
//                return 0;
//            else if (o1.equals(origin))
//                return Integer.MAX_VALUE;
//            else if (o2.equals(origin))
//                return Integer.MIN_VALUE;
//            else
//                return (o1.xCoord + o1.zCoord) - (o2.xCoord + o2.zCoord);
//        }
//
//    }
//}
