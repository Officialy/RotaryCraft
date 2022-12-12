///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.weaponry;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.entity.*;
//import net.minecraft.world.phys.AABB;
//import org.openjdk.nashorn.internal.runtime.regexp.joni.constants.NodeType;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.ModList;
//import reika.dragonapi.instantiable.data.blockstruct.BlockArray;
//import reika.dragonapi.instantiable.data.collections.ClassNameCache;
//import reika.dragonapi.instantiable.data.immutable.WorldLocation;
//import reika.dragonapi.libraries.java.ReikaRandomHelper;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.dragonapi.libraries.rendering.ReikaColorAPI;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.api.Interfaces.EMPControl;
//import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
//import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.PacketRegistry;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//
//public class BlockEntityEMP extends BlockEntityPowerReceiver implements RangedEffect {
//
//    public static final long BLAST_ENERGY = (long) (4.184e9);
//    public static final int MAX_RANGE = 64;
//    private static final ClassNameCache blacklist = new ClassNameCache();
//    private static final HashSet<WorldLocation> shutdownLocations = new HashSet();
//
//    static { //this list is horribly incomplete
//        addEntry(ChestBlockEntity.class);
//        addEntry(EnderChestBlockEntity.class);
//        addEntry(HopperBlockEntity.class);
//        addEntry(DropperBlockEntity.class);
//        addEntry(DispenserBlockEntity.class);
//        addEntry(BrewingStandBlockEntity.class);
//        addEntry(EnchantmentTableBlockEntity.class);
//        addEntry(TheEndPortalBlockEntity.class);
//        addEntry(SignBlockEntity.class);
//        addEntry(SkullBlockEntity.class);
//
//        addEntry("buildcraft.factory.TileTank", ModList.BCFACTORY);
//        addEntry("buildcraft.transport.PipeTransport", ModList.BCTRANSPORT);
//
//        addEntry("thermalexpansion.Blocks.conduit.TileConduitRoot", ModList.THERMALEXPANSION);
//
//        addEntry("ic2.core.Blocks.wiring.BlockEntityCable", ModList.IC2);
//
//        addEntry("codechicken.enderstorage.common.TileFrequencyOwner", ModList.ENDERSTORAGE);
//
//        addEntry("thaumcraft.common.tiles.*", ModList.THAUMCRAFT);
//
//        addEntry("forestry.core.tiles.TileNaturalistChest", ModList.FORESTRY);
//        addEntry("forestry.core.tiles.TileMill", ModList.FORESTRY);
//        addEntry("forestry.apiculture.multiblock.TileAlvearyPlain", ModList.FORESTRY);
//        addEntry("forestry.apiculture.tiles.*", ModList.FORESTRY);
//        addEntry("forestry.aboriculture.tiles.TileTreeContainer", ModList.FORESTRY);
//        addEntry("forestry.factory.tiles.TileWorktable", ModList.FORESTRY);
//
//        //addEntry("Reika.FurryKingdoms.TileEntities.BlockEntityFlag", ModList.FURRYKINGDOMS);
//
//        addEntry("Reika.ExpandedRedstone.TileEntities.*", ModList.EXPANDEDREDSTONE);
//        addEntry("Reika.ElectriCraft.TileEntities.BlockEntityWire", ModList.ELECTRICRAFT);
//    }
//
//    private final ArrayList<BlockPos> blocks = new ArrayList<>();
//    private final BlockArray check = new BlockArray();
//
//    public EMPEffect effectRender;
//    private boolean loading = true;
//    private boolean canLoad = true;
//    private long energy;
//    private boolean fired = false;
//
//    private static void addEntry(Class<? extends BlockEntity> cl) {
//        blacklist.add(cl.getName());
//        RotaryCraft.LOGGER.log("Adding " + cl.getName() + " to the EMP immunity list");
//    }
//
//    private static void addEntry(String name, ModList mod) {
//        if (!mod.isLoaded())
//            return;
//        blacklist.add(name);
//        RotaryCraft.LOGGER.log("Adding " + name + " to the EMP immunity list");
//    }
//
//    public static boolean isShutdown(BlockEntity te) {
//        return !shutdownLocations.isEmpty() && shutdownLocations.contains(new WorldLocation(te));
//    }
//
//    public static boolean isShutdown(Level world, BlockPos pos) {
//        return !shutdownLocations.isEmpty() && shutdownLocations.contains(new WorldLocation(world, pos));
//    }
//
//    public static void resetCoordinate(Level world, BlockPos pos) {
//        if (shutdownLocations.remove(new WorldLocation(world, pos)))
//            ReikaPacketHelper.sendDataPacketToEntireServer(RotaryCraft.packetChannel, PacketRegistry.SPARKLOC.ordinal(), world.provider.dimensionId, pos, 0);
//        if (shutdownLocations.isEmpty())
//            EMPTileWatcher.instance.unregisterTileWatcher();
//    }
//
//    private static void addShutdownLocation(BlockEntity te) {
//        shutdownLocations.add(new WorldLocation(te));
//        EMPTileWatcher.instance.registerTileWatcher();
//    }
//
//    @Override
//    protected void animateWithTick(Level world, BlockPos pos) {
//        if (world.isClientSide) {
//            if (effectRender != null) {
//                if (effectRender.tick())
//                    effectRender = null;
//            }
//        }
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.EMP;
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
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        tickcount++;
//
//        this.getPowerBelow();
//
//        if (fired)
//            return;
//
//        if (power >= MINPOWER)
//            energy += power;
//
//        if (canLoad && check.isEmpty()) {
//            int r = this.getRange();
//            for (int i = x - r; i <= x + r; i++) {
//                for (int k = z - r; k <= z + r; k++) {
//                    check.addBlockCoordinate(i, y, k);
//                    loading = true;
//                }
//            }
//        }
//
//        //ReikaJavaLibrary.pConsoleOnlyIn(check.getSize(), Dist.DEDICATED_SERVER);
//        //ReikaJavaLibrary.pConsole(blocks.size(), Dist.DEDICATED_SERVER);
//
//        if (!world.isClientSide)
//            this.createListing();
//
//        if (loading) {
//            for (int i = 0; i < 6; i++) {
//                double dx = DragonAPI.rand.nextDouble();
//                double dz = DragonAPI.rand.nextDouble();
//                world.addParticle("portal", x - 0.5 + dx * 2, y + DragonAPI.rand.nextDouble() + 0.4, z - 0.5 + dz * 2, -1 + dx * 2, 0.2, -1 + dz * 2);
//            }
//        }
//
//        //power = (long)BLAST_ENERGY+800;
//
//        if (energy / 20L >= BLAST_ENERGY && !loading) {
//            //if (world.isClientSide)
//            //	this.initEffect();
//            //else
//            this.fire(world, pos);
//        }
//    }
//
//
//    public void initEffect() {
//        effectRender = new EMPEffect();
//    }
//
//    private void createListing() {
//        Level world = level;
//        int num = 1 + 8 * RotaryConfig.COMMON.EMPLOAD.get();
//        for (int i = 0; i < num && loading; i++) {
//            int index = DragonAPI.rand.nextInt(check.getSize());
//            BlockPos b = check.getNthBlock(index);
//            int x = b.xCoord;
//            int z = b.zCoord;
//            for (int y = 0; y < world.provider.getHeight(); y++) {
//                BlockEntity te = world.getBlockEntity(pos);
//                if (this.canAffect(te)) {
//                    blocks.add(new BlockPos(te));
//                }
//            }
//            check.remove(b.xCoord, b.yCoord, b.zCoord);
//            if (check.isEmpty()) {
//                loading = false;
//                canLoad = false;
//            }
//        }
//    }
//
//    private boolean canAffect(BlockEntity te) {
//        if (te == null || te.isInvalid())
//            return false;
//        if (te instanceof IEnergyStorage)
//            return true;
//        if (te instanceof IEnergyProvider)
//            return true;
//        if (InterfaceCache.IC2POWERTILE.instanceOf(te))
//            return true;
//        if (InterfaceCache.NODE.instanceOf(te))
//            return true;
//        if (ModList.CHROMATICRAFT.isLoaded() && te instanceof BlockEntityCrystalPylon)
//            return true;
//        return te instanceof RotaryCraftBlockEntity;
//    }
//
//    private void fire(Level world, BlockPos pos) {
//        fired = true;
//        ReikaPacketHelper.sendDataPacketWithRadius(RotaryCraft.packetChannel, PacketRegistry.EMPEFFECT.ordinal(), this, 128);
//        for (int i = 0; i < blocks.size(); i++) {
//            BlockEntity te = blocks.get(i).getBlockEntity(world);
//            if (ModList.CHROMATICRAFT.isLoaded() && te instanceof BlockEntityCrystalPylon)
//                ((BlockEntityCrystalPylon) te).onEMP(this);
//            else if (InterfaceCache.NODE.instanceOf(te))
//                this.chargeNode((INode) te);
//            else
//                this.shutdownTE(te);
//        }
//        this.affectEntities(world, pos);
//        //destroySelf(world, pos);
//    }
//
//    private void destroySelf(Level world, BlockPos pos) {
//        world.setBlockToAir(pos);
//        world.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3F, true);
//        if (ReikaRandomHelper.doWithChance(50)) {
//            ReikaItemHelper.dropItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, this.getMachine().getCraftedProduct());
//        } else if (ReikaRandomHelper.doWithChance(50)) {
//            ArrayList<ItemStack> items = new ArrayList<ItemStack>();
//            items.add(new ItemStack(Items.nether_star));
//            items.add(new ItemStack(Items.diamond, 9, 0));
//            items.add(new ItemStack(Items.gold_ingot, 32, 0));
//            items.add(ReikaItemHelper.getSizedItemStack(RotaryItems.HSLA_STEEL_SCRAP, DragonAPI.rand.nextInt(16)));
//            ReikaItemHelper.dropItems(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, items);
//        } else {
//            ReikaItemHelper.dropItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, ReikaItemHelper.getSizedItemStack(RotaryItems.HSLA_STEEL_SCRAP, 8 + DragonAPI.rand.nextInt(16)));
//        }
//    }
//
//    private void affectEntities(Level world, BlockPos pos) {
//        AABB box = ReikaAABBHelper.getBlockAABB(pos).expand(128, 64, 128);
//        List<Entity> li = world.getEntities(Entity.class, box);
//        for (Entity e : li) {
//            if (InterfaceCache.BCROBOT.instanceOf(e)) {
//                world.explode(e, e.getY, e.getY(), e.posZ, 3, false);
//                e.kill();
//            } else if (e instanceof LivingEntity) {
//                if (ReikaEntityHelper.isEntityWearingPoweredArmor((LivingEntity) e)) {
//                    for (int i = 1; i <= 4; i++) {
//                        e.setCurrentItemOrArmor(i, null);
//                    }
//                    float f = (float) ReikaRandomHelper.getRandomBetween(3D, 10D);
//                    world.newExplosion(e, e.getY, e.getY(), e.posZ, f, true, true);
//                    e.motionX += ReikaRandomHelper.getRandomPlusMinus(0, 1.5);
//                    e.motionZ += ReikaRandomHelper.getRandomPlusMinus(0, 1.5);
//                    e.motionY += -ReikaRandomHelper.getRandomBetween(0.125, 1);
//                }
//            }
//        }
//    }
//
////    private void chargeNode(INode te) {
////        //ReikaJavaLibrary.pConsole(te.getNodeType()+":"+te.getAspects().aspects);
////        te.setNodeVisBase(Aspect.ENERGY, (short) 32000);
////        te.setNodeVisBase(Aspect.WEAPON, (short) 32000);
////        te.setNodeVisBase(Aspect.MECHANISM, (short) 32000);
////
////        te.addToContainer(Aspect.ENERGY, (short) 8000);
////        te.addToContainer(Aspect.WEAPON, (short) 1000);
////        te.addToContainer(Aspect.MECHANISM, (short) 2000);
////        switch (te.getNodeType()) {
////            case UNSTABLE:
////                if (DragonAPI.rand.nextInt(3) > 0)
////                    te.setNodeType(NodeType.DARK);
////                else
////                    te.setNodeType(NodeType.PURE);
////                break;
////            case DARK:
////                te.setNodeType(DragonAPI.rand.nextBoolean() ? NodeType.TAINTED : NodeType.HUNGRY);
////                break;
////            case NORMAL:
////                te.setNodeType(NodeType.UNSTABLE);
////                break;
////            default:
////                break;
////        }
////        //ReikaJavaLibrary.pConsole(te.getNodeType()+":"+te.getAspects().aspects);
////    }
//
//    private void shutdownTE(BlockEntity te) {
//        if (te == null)
//            return;
//        if (this.isBlacklisted(te))
//            return;
//        ReikaPacketHelper.sendDataPacketToEntireServer(RotaryCraft.packetChannel, PacketRegistry.SPARKLOC.ordinal(), te.level.provider.dimensionId, te.xCoord, te.yCoord, te.zCoord, 1);
//        if (te instanceof RotaryCraftBlockEntity) {
//            RotaryCraftBlockEntity rc = (RotaryCraftBlockEntity) te;
//            if (!rc.isShutdown())
//                rc.onEMP();
//        } else if (te instanceof EMPControl) {
//            ((EMPControl) te).onHitWithEMP(this);
//        } else {
//            addShutdownLocation(te);
//        }/*
//		else if (RotaryConfig.COMMON.ATTACKBLOCKS.getState())
//			this.shutdownFallback(te);*/
//    }
//
//    private boolean isBlacklisted(BlockEntity te) {
//        return blacklist.contains(te.getClass());
//    }
//
//    private void shutdownFallback(BlockEntity te) {
//        //shutdownLocations.add(new WorldLocation(te));
//
//        int x = te.xCoord;
//        int y = te.yCoord;
//        int z = te.zCoord;
//        Block id = level.getBlock(pos);
//        this.dropMachine(level, pos);
//		/*
//		;
//		ItemStack[] inv;
//		if (te instanceof Container) {
//			Container ii = (Container)te;
//			inv = new ItemStack[ii.getContainerSize()];
//			for (int i = 0; i < inv.length; i++) {
//				inv[i] = ii.getStackInSlot(i);
//			}
//		}
//		else {
//			inv = new ItemStack[0];
//		}
//		level.setBlock(pos, RotaryBlocks.DEADMACHINE.getBlock());
//		BlockEntityDeadMachine dead = (BlockEntityDeadMachine)level.getBlockEntity(pos);
//		dead.setBlock(b, id, meta);
//		dead.setInvSize(inv.length);
//		for (int i = 0; i < inv.length; i++) {
//			dead.setInventorySlotContents(i, inv[i]);
//		}*/
//    }
//
//    private void dropMachine(Level world, BlockPos pos) {
//        Block b = world.getBlockState(pos).getBlock();
//        if (b != null) {
//            //ReikaItemHelper.dropItems(world, x+0.5, y+0.5, z+0.5, b.getDrops(world, pos, meta, 0));
//            b.dropBlockAsItem(world, pos, meta, 0);
//        }
//        world.setBlockToAir(pos);
//    }
//
//    @Override
//    public int getRange() {
//        return 64;
//    }
//
//    @Override
//    public int getMaxRange() {
//        return MAX_RANGE;
//    }
//
//    public boolean isLoading() {
//        return loading;
//    }
//
//    public boolean usable() {
//        return !fired;
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//        NBT.putBoolean("load", loading);
//        NBT.putBoolean("cload", canLoad);
//        NBT.putBoolean("fire", fired);
//        NBT.putLong("e", energy);
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//        loading = NBT.getBoolean("load");
//        canLoad = NBT.getBoolean("cload");
//        fired = NBT.getBoolean("fire");
//
//        energy = NBT.getLong("e");
//    }
//
//    public void updateListing() {
//        canLoad = true;
//    }
//
//    public final double getMaxRenderDistanceSquared() {
//        return 16384D;
//    }
//
//    @Override
//    public final AABB getRenderBoundingBox() {
//        return INFINITE_EXTENT_AABB;
//    }
//
//    @Override
//    public String getName() {
//        return null;
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
//
//
//    public static class EMPEffect {
//
//        public static final int EXPAND_LIFESPAN = 20;
//        public static final int FADE_LIFESPAN = 10;
//        private int age;
//
//        private EMPEffect() {
//            age = 0;
//        }
//
//        public boolean tick() {
//            age++;
//            return age > EXPAND_LIFESPAN + FADE_LIFESPAN;
//        }
//
//        public double getRadius(float ptick) {
//            if (age >= EXPAND_LIFESPAN)
//                return MAX_RANGE + 2 * (age - EXPAND_LIFESPAN + ptick);
//            return Math.min(MAX_RANGE, 0.25 + (age + ptick) * MAX_RANGE / (double) EXPAND_LIFESPAN);
//        }
//
//        public float getBrightness() {
//            return age <= EXPAND_LIFESPAN ? 1 : 1 - ((age - EXPAND_LIFESPAN) / (float) FADE_LIFESPAN);
//        }
//
//        public int getColor1() {
//            return ReikaColorAPI.getColorWithBrightnessMultiplier(0xffBEFFFF, this.getBrightness());
//        }
//
//        public int getColor2() {
//            return ReikaColorAPI.getColorWithBrightnessMultiplier(0xff47F2E2, this.getBrightness());
//        }
//
//    }
//
//}
