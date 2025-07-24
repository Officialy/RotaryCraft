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
//import net.minecraft.nbt.ListTag;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//
//import net.minecraft.world.phys.AABB;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.api.distmarker.OnlyIn;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.ModList;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.registry.ReikaParticleHelper;
//import reika.dragonapi.modregistry.ModWoodList;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.blockentity.SprinklerBlock;
//
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.PacketRegistry;
//import reika.rotarycraft.registry.RotaryAdvancements;
//
//import java.util.*;
//
//public class BlockEntitySprinkler extends SprinklerBlock {
//
//    private static final String LOGGER_ID = "sprinkler_apple";
//
//    static {
//        ModularLogger.instance.addLogger(RotaryCraft.INSTANCE, LOGGER_ID);
//    }
//
//    private final FieldCache cache = new FieldCache();
//
//    private static boolean isOpaque(Block b) {
//        return b.isOpaqueCube() && !(b instanceof BlockLeavesBase);
//    }
//
//    @Override
//    public void performEffects(Level world, BlockPos pos) {
//        RotaryAdvancements.SPRINKLER.triggerAchievement(this.getPlacer());
//        if (!world.isClientSide) {
//            this.hydrate(world, pos);
//            if (ModList.REACTORCRAFT.isLoaded() && DragonAPI.rand.nextInt(2400) == 0)
//                this.clearRadiation(world, pos);
//        } else {
//            this.spawnParticles(world, pos);
//        }
//    }
//
//    @ModDependent(ModList.REACTORCRAFT)
//    private void clearRadiation(Level world, BlockPos pos) {
//        int r = this.getRange();
//        AABB box = ReikaAABBHelper.getBlockAABB(pos).offset(0, -4, 0).expand(r, 4, r);
//        List<EntityRadiation> li = world.getEntities(EntityRadiation.class, box);
//        for (EntityRadiation e : li) {
//            e.clean();
//            if (DragonAPI.rand.nextBoolean())
//                break;
//        }
//    }
//
//    public void hydrate(Level world, BlockPos pos) {
//        int range = this.getRange();
//        boolean flag = false;
//        for (int i = -range; i <= range; i++) {
//            for (int k = -range; k <= range; k++) {
//                int dx = x + i;
//                int dz = z + k;
//                float f = 0.5F + 1.5F * (i * i + k * k) / (range * range); //2x rate at center, 0.5x rate at edge
//                FieldColumn fe = cache.getOrFindLevel(world, dx, dz, y);
//                if (fe.tick(world, f)) {
//                    fe.recalulateLevel(world);
//                    flag = true;
//                }
//                //this.sendParticle(dx, fe.calculatedY, dz, fe.doDripParticles);
//            }
//        }
//        if (cache.tickDirty()) {
//            this.syncAllData(true);
//        }
//    }
//
//    private void sendParticle(int dx, int dy, int dz, boolean drip) {
//        ReikaPacketHelper.sendDataPacketWithRadius(RotaryCraft.packetChannel, PacketRegistry.SPRINKLER.ordinal(), this, 48, dx, dy, dz, drip ? 1 : 0);
//    }
//
//
//    public void doParticle(Level world, int dx, int dy, int dz, boolean drip) {
//        int d = Math.max(1, 5 - RotaryConfig.COMMON.SPRINKLER.get());
//        if (DragonAPI.rand.nextInt(d) == 0)
//            ReikaParticleHelper.RAIN.spawnAt(world, dx + DragonAPI.rand.nextDouble(), dy + 1, dz + DragonAPI.rand.nextDouble());
//        if (drip) {
//            ReikaParticleHelper.DRIPWATER.spawnAt(world, dx + DragonAPI.rand.nextDouble(), dy, dz + DragonAPI.rand.nextDouble());
//        }
//    }
//
//    @Override
//    public void saveAdditional(CompoundTag NBT) {
//        super.saveAdditional(NBT);
//
//        NBT.put("cache", cache.saveAdditional());
//    }
//
//	/*
//	private void sendParticle(ReikaParticleHelper type, double dx, double dy, double dz) {
//		ArrayList<Integer> li = ReikaJavaLibrary.makeListFrom(type.ordinal(), 1);
//		li.addAll(ReikaJavaLibrary.makeIntListFromArray(ReikaJavaLibrary.splitDoubleToInts(dx)));
//		li.addAll(ReikaJavaLibrary.makeIntListFromArray(ReikaJavaLibrary.splitDoubleToInts(dy)));
//		li.addAll(ReikaJavaLibrary.makeIntListFromArray(ReikaJavaLibrary.splitDoubleToInts(dz)));
//		ReikaPacketHelper.sendDataPacket(DragonAPI.packetChannel, PacketIDs.PARTICLEWITHPOS.ordinal(), new PacketTarget.RadiusTarget(this, 32), li);
//	}*/
//
//    @Override
//    public void load(CompoundTag NBT) {
//        super.load(NBT);
//
//        cache.load(NBT.getList("cache", NBTTypes.COMPOUND.ID));
//    }
//
//
//    public void spawnParticles(Level world, BlockPos pos) {
//
//        int d = Math.max(0, RotaryConfig.COMMON.SPRINKLER.get());
//        double ypos = y + 0.125;
//        double vel;
//        double r = this.getRange() / 10D;
//
//        double py = y - 0.1875D + 0.5;
//        int n = (DragonAPI.rand.nextInt(2) == 0 ? 1 : 0) + DragonAPI.rand.nextInt(1 + d);
//        for (int i = 0; i < n; i++) {
//            double px = x - 1 + 2 * DragonAPI.rand.nextFloat();
//            double pz = z - 1 + 2 * DragonAPI.rand.nextFloat();
//            world.addParticle("splash", px + 0.5, py, pz + 0.5, 0, 0, 0);
//        }
//
//        for (vel = 0; vel < r; vel += 0.1) {
//            py = y - 0.1875D + 0.5;
//            n = (DragonAPI.rand.nextInt(2) == 0 ? 1 : 0) + DragonAPI.rand.nextInt(1 + d * 4);
//            for (int i = 0; i < n; i++) {
//                double vx = vel * (-1 + DragonAPI.rand.nextFloat() * 2);
//                vx *= 1.05;
//                double vz = vel * (-1 + DragonAPI.rand.nextFloat() * 2);
//                vz *= 1.05;
//                double px = x - 1 + 2 * DragonAPI.rand.nextFloat();
//                double pz = z - 1 + 2 * DragonAPI.rand.nextFloat();
//                world.addParticle("splash", px + 0.5, py, pz + 0.5, vx, 0, vz);
//            }
//        }
//
//        cache.doParticles(world);
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
//        return MachineRegistry.SPRINKLER;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return 0;
//    }
//
//    @Override
//    public int getCapacity() {
//        return 180;
//    }
//
//    @Override
//    public int getWaterConsumption() {
//        return 3;
//    }
//
//    @Override
//    public Direction getPipeDirection() {
//        return Direction.UP;
//    }
//
//    private enum Effects {
//        FIREEXTINGUISH(20),
//        CROPTICK(80),
//        HYDRATEFARMLAND(15),
//        APPLETICK(40);
//
//        private static final Effects[] list = values();
//        private final int randomChance;
//
//        Effects(int c) {
//            randomChance = c;
//        }
//
//        /**
//         * Factor < 1 -> higher rate
//         */
//        private boolean doChance(Random rand, float factor) {
//            return rand.nextInt(Math.max(1, (int) (randomChance * factor))) == 0;
//        }
//
//        private void doEffect(Level world, BlockPos pos, Block b) {
//            switch (this) {
//                case APPLETICK:
//                    if (b.defaultBlockState().getMaterial() == Material.leaves) {
//                        if (ModularLogger.instance.isEnabled(LOGGER_ID))
//                            ModularLogger.instance.log(LOGGER_ID, "Found leaf @ " + x + ", " + y + ", " + z);
//                        Block b2 = world.getBlock(x, y - 1, z);
//                        if (b2.isAir(world, x, y - 1, z)) { //space for an apple; only stop moving down if this
//                            b.updateTick(world, pos, DragonAPI.rand);
//                            BlockTickEvent.fire(world, pos, b, UpdateFlags.FORCED.flag);
//                            if (ModularLogger.instance.isEnabled(LOGGER_ID))
//                                ModularLogger.instance.log(LOGGER_ID, "Ticked apple leaf @ " + x + ", " + y + ", " + z);
//                        }
//                    } else if (b.defaultBlockState().getMaterial() == Material.plants) {
//                        b.updateTick(world, pos, DragonAPI.rand);
//                        BlockTickEvent.fire(world, pos, b, UpdateFlags.FORCED.flag);
//                        if (ModularLogger.instance.isEnabled(LOGGER_ID))
//                            ModularLogger.instance.log(LOGGER_ID, "Ticked apple block @ " + x + ", " + y + ", " + z);
//                    }
//                    break;
//                case CROPTICK:
//                    CropType crop = ReikaCropHelper.getCrop(b);
//                    if (crop == null)
//                        crop = ModCropList.getModCrop(b, meta);
//                    if (crop != null) {
//                        b.updateTick(world, pos, DragonAPI.rand);
//                        BlockTickEvent.fire(world, pos, b, UpdateFlags.FORCED.flag);
//                    } else {
//                        ReikaPlantHelper p = ReikaPlantHelper.getPlant(b);
//                        if (p != null && p.grows()) {
//                            b.updateTick(world, pos, DragonAPI.rand);
//                            BlockTickEvent.fire(world, pos, b, UpdateFlags.FORCED.flag);
//                        } else if (p == null) {
//                            if (b instanceof BlockSapling || ModWoodList.isModSapling(b, meta)) {
//                                b.updateTick(world, pos, DragonAPI.rand);
//                                BlockTickEvent.fire(world, pos, b, UpdateFlags.FORCED.flag);
//                            }
//                        }
//                    }
//                    break;
//                case FIREEXTINGUISH:
//                    world.playLocalSound(pos, "DragonAPI.rand.fizz", 0.6F + 0.4F * DragonAPI.rand.nextFloat(), 0.5F + 0.5F * DragonAPI.rand.nextFloat());
//                    world.setBlockToAir(pos);
//                    break;
//                case HYDRATEFARMLAND:
//                    ReikaWorldHelper.hydrateFarmland(world, pos, false);
//                    break;
//            }
//        }
//    }
//
//    private static class FieldCache {
//
//        private final HashMap<BlockPos, FieldColumn> levels = new HashMap();
//        private int dirtyRate;
//
//        private FieldColumn getOrFindLevel(Level world, int dx, int dz, int y) {
//            BlockPos c = new BlockPos(dx, 0, dz);
//            FieldColumn f = levels.get(c);
//            if (f == null || f.age >= 20) {
//                if (f == null) {
//                    f = new FieldColumn(dx, dz, y);
//                    levels.put(c, f);
//                }
//            } else {
//                f.age++;
//            }
//            return f;
//        }
//
//        private void clear() {
//            levels.clear();
//        }
//
//
//        private void doParticles(Level world) {
//            for (FieldColumn f : levels.values()) {
//                f.doParticles(world);
//            }
//        }
//
//        private ListTag saveAdditional() {
//            ListTag li = new ListTag();
//            for (BlockPos c : levels.keySet()) {
//                FieldColumn f = levels.get(c);
//                CompoundTag tag = new CompoundTag();
//                tag.put("location", c.writeToTag());
//                tag.put("data", f.saveAdditional());
//                li.add(tag);
//            }
//            return li;
//        }
//
//        private void load(ListTag li) {
//            this.clear();
//            for (Object o : li) {
//                CompoundTag tag = (CompoundTag) o;
//                BlockPos c = BlockPos.load("location", tag);
//                FieldColumn f = FieldColumn.load(tag.getCompound("data"));
//                levels.put(c, f);
//            }
//        }
//
//        private boolean tickDirty() {
//            dirtyRate++;
//            if (dirtyRate >= 8) {
//                dirtyRate = 0;
//                return true;
//            }
//            return false;
//        }
//
//    }
//
//    private static class FieldColumn {
//
//        private final int xCoord;
//        private final int zCoord;
//        private final int sprinklerY;
//        private int age = 0;
//
//        //private boolean doDripParticles;
//        //private int calculatedY;
//        private ColumnAction[][] effectMap = new ColumnAction[256][];
//
//        private FieldColumn(int dx, int dz, int y) {
//            xCoord = dx;
//            zCoord = dz;
//            sprinklerY = y;
//        }
//
//        private static FieldColumn load(CompoundTag tag) {
//            int x = tag.getInt("x");
//            int y = tag.getInt("y");
//            int z = tag.getInt("z");
//
//            FieldColumn ret = new FieldColumn(x, z, y);
//            ListTag li = tag.getTagList("map", NBTTypes.COMPOUND.ID);
//            for (Object o : li.tagList) {
//                CompoundTag nbt = (CompoundTag) o;
//                int dy = nbt.getInt("y");
//                Collection<ColumnAction> cc = new HashSet();
//                ListTag li2 = nbt.getTagList("entries", NBTTypes.COMPOUND.ID);
//                for (Object o2 : li2.tagList) {
//                    CompoundTag nbt2 = (CompoundTag) o2;
//                    cc.add(ColumnAction.load(nbt2));
//                }
//                ret.effectMap[dy] = cc.toArray(new ColumnAction[cc.size()]);
//            }
//
//            return ret;
//        }
//
//        private CompoundTag saveAdditional() {
//            CompoundTag tag = new CompoundTag();
//            tag.putInt("x", xCoord);
//            tag.putInt("z", zCoord);
//            tag.putInt("y", sprinklerY);
//
//            ListTag li = new ListTag();
//            for (int y = 0; y < 256; y++) {
//                ColumnAction[] cc = effectMap[y];
//                if (cc == null)
//                    continue;
//                CompoundTag nbt = new CompoundTag();
//                nbt.putInt("y", y);
//                ListTag li2 = new ListTag();
//                for (ColumnAction ca : cc) {
//                    li2.add(ca.saveAdditional());
//                }
//                nbt.put("entries", li2);
//                li.add(nbt);
//            }
//            tag.put("map", li);
//            return tag;
//        }
//
//        private void recalulateLevel(Level world) {
//            age = 0;
//            int dy = sprinklerY;
//            boolean flag = false;
//            //doDripParticles = false;
//            effectMap = new ColumnAction[256][];
//            while (dy > 0 && sprinklerY - dy < 12 && !flag) {
//                Block b = world.getBlock(xCoord, dy, zCoord);
//                if (!b.isAir(world, xCoord, dy, zCoord)) {
//                    boolean stopMoving = false;
//                    if (b == Blocks.FIRE) {
//                        this.addEffectValue(dy, new ColumnAction(b, xCoord, dy, zCoord, Effects.FIREEXTINGUISH, false, false, false));
//                        stopMoving = true;
//                    } else if (b == Blocks.farmland) {
//                        this.addEffectValue(dy, new ColumnAction(b, xCoord, dy, zCoord, Effects.HYDRATEFARMLAND, false, false, true));
//                        stopMoving = true;
//                    }
//                    if (!stopMoving) {
//                        CropType crop = ReikaCropHelper.getCrop(b);
//                        if (crop == null)
//                            crop = ModCropList.getModCrop(b, meta);
//                        if (crop != null) {
//                            this.addEffectValue(dy, new ColumnAction(b, xCoord, dy, zCoord, Effects.CROPTICK, false, false, true));
//                            //stopMoving = true; continue downwards to hydrate farmland
//                        } else {
//                            ReikaPlantHelper p = ReikaPlantHelper.getPlant(b);
//                            if (p != null && p.grows()) {
//                                this.addEffectValue(dy, new ColumnAction(b, xCoord, dy, zCoord, Effects.CROPTICK, false, p == ReikaPlantHelper.SUGARCANE, true));
//                                stopMoving = p == ReikaPlantHelper.CACTUS || p == ReikaPlantHelper.SUGARCANE;
//                            } else if (p == null) {
//                                if (b instanceof BlockSapling || ModWoodList.isModSapling(b, meta)) {
//                                    b.updateTick(world, xCoord, dy, zCoord, DragonAPI.rand);
//                                    BlockTickEvent.fire(world, xCoord, dy, zCoord, b, UpdateFlags.FORCED.flag);
//                                    stopMoving = true;
//                                }
//                            }
//                        }
//                    }
//                    if (!stopMoving) {
//                        if (b.defaultBlockState().getMaterial() == Material.leaves || b.defaultBlockState().getMaterial() == Material.plants) {
//                            String n = b.getClass().getName().toLowerCase(Locale.ENGLISH);
//                            if (n.startsWith("growthcraft.apples")) {
//                                if (b.defaultBlockState().getMaterial() == Material.leaves && n.endsWith("leaves")) {
//                                    if (ModularLogger.instance.isEnabled(LOGGER_ID))
//                                        ModularLogger.instance.log(LOGGER_ID, "Found leaf @ " + xCoord + ", " + dy + ", " + zCoord);
//                                    Block b2 = world.getBlock(xCoord, dy - 1, zCoord);
//                                    if (isOpaque(b2)) {
//                                        stopMoving = true;
//                                    } else if (b2.isAir(world, xCoord, dy - 1, zCoord)) { //space for an apple; only stop moving down if this
//                                        this.addEffectValue(dy, new ColumnAction(b, xCoord, dy, zCoord, Effects.APPLETICK, true, false, true));
//                                        if (ModularLogger.instance.isEnabled(LOGGER_ID))
//                                            ModularLogger.instance.log(LOGGER_ID, "Ticked apple leaf @ " + xCoord + ", " + dy + ", " + zCoord);
//                                        stopMoving = true;
//                                    }
//                                    //doDripParticles = b2 != b && !isOpaque(b2);
//                                } else if (b.defaultBlockState().getMaterial() == Material.plants && n.endsWith("apple")) {
//                                    this.addEffectValue(dy, new ColumnAction(b, xCoord, dy, zCoord, Effects.APPLETICK, false, true, false));
//                                    stopMoving = true;
//                                    if (ModularLogger.instance.isEnabled(LOGGER_ID))
//                                        ModularLogger.instance.log(LOGGER_ID, "Ticked apple block @ " + xCoord + ", " + dy + ", " + zCoord);
//                                }
//                                if (ModularLogger.instance.isEnabled(LOGGER_ID))
//                                    ModularLogger.instance.log(LOGGER_ID, "Read GC block '" + n + "', flag2=" + stopMoving);
//                            }
//                        }
//                    }
//                    flag = isOpaque(b) || stopMoving;
//                }
//                if (!flag)
//                    dy--;
//            }
//            //ReikaJavaLibrary.pConsole(dy+" : "+world.getBlock(xCoord, dy, zCoord));
//        }
//
//        private void addEffectValue(int dy, ColumnAction ca) {
//            ColumnAction[] set = effectMap[dy];
//            int len = set == null ? 0 : set.length;
//            ColumnAction[] ret = new ColumnAction[len + 1];
//            if (set != null)
//                System.arraycopy(set, 0, ret, 0, len);
//            ret[ret.length - 1] = ca;
//            effectMap[dy] = ret;
//        }
//
//        private boolean tick(Level world, float chanceFactor) {
//            boolean flag = age >= 20;
//            for (int y = 0; y < effectMap.length; y++) {
//                ColumnAction[] map = effectMap[y];
//                if (map == null)
//                    continue;
//                Block b = world.getBlockState(xCoord, y, zCoord).getBlock();
//                for (ColumnAction ca : map) {
//                    if (ca.block == b) {
//                        if (ca.effect.doChance(DragonAPI.rand, chanceFactor)) {
//                            ca.effect.doEffect(world, xCoord, y, zCoord, b);
//                        }
//                    } else {
//                        flag = true;
//                    }
//                }
//            }
//            return flag;
//        }
//
//
//        private void doParticles(Level world) {
//            for (int y = 0; y < effectMap.length; y++) {
//                ColumnAction[] map = effectMap[y];
//                if (map == null)
//                    continue;
//                for (ColumnAction ca : map) {
//                    if (ca.doDripParticles && DragonAPI.rand.nextInt(8) == 0) {
//                        ReikaParticleHelper.DRIPWATER.spawnAt(world, ca.xCoord + DragonAPI.rand.nextDouble(), ca.yCoord, ca.zCoord + DragonAPI.rand.nextDouble());
//                    }
//                    if (ca.doDripParticlesUp && DragonAPI.rand.nextInt(8) == 0) {
//                        ReikaParticleHelper.DRIPWATER.spawnAt(world, ca.xCoord + DragonAPI.rand.nextDouble(), ca.yCoord + 0.9375, ca.zCoord + DragonAPI.rand.nextDouble());
//                    }
//                    if (ca.doSplashParticles) {
//                        int d = Math.max(1, 5 - RotaryConfig.COMMON.SPRINKLER.get());
//                        if (DragonAPI.rand.nextInt(d) == 0)
//                            ReikaParticleHelper.RAIN.spawnAt(world, ca.xCoord + DragonAPI.rand.nextDouble(), ca.yCoord + 1, ca.zCoord + DragonAPI.rand.nextDouble());
//                    }
//                }
//            }
//        }
//
//    }
//
//    private static class ColumnAction {
//
//        private final Block block;
//        private final int xCoord;
//        private final int yCoord;
//        private final int zCoord;
//        private final Effects effect;
//        private final boolean doDripParticles;
//        private final boolean doDripParticlesUp;
//        private final boolean doSplashParticles;
//
//        private ColumnAction(Block b, BlockPos pos, Effects e, boolean drip, boolean drip2, boolean splash) {
//            block = b;
//            xCoord = x;
//            yCoord = y;
//            zCoord = z;
//            effect = e;
//            doDripParticles = drip;
//            doDripParticlesUp = drip2;
//            doSplashParticles = splash;
//        }
//
//        private static ColumnAction load(CompoundTag tag) {
//            return new ColumnAction(Block.getBlockById(tag.getInt("block")), tag.getInt("x"), tag.getInt("y"), tag.getInt("z"), Effects.list[tag.getInt("effect")], tag.getBoolean("drip"), tag.getBoolean("drip2"), tag.getBoolean("splash"));
//        }
//
//        private CompoundTag saveAdditional() {
//            CompoundTag tag = new CompoundTag();
//            tag.putInt("x", xCoord);
//            tag.putInt("z", zCoord);
//            tag.putInt("y", yCoord);
//
//            tag.putInt("effect", effect.ordinal());
//            tag.putInt("block", Block.getIdFromBlock(block));
//
//            tag.putBoolean("drip", doDripParticles);
//            tag.putBoolean("drip2", doDripParticlesUp);
//            tag.putBoolean("splash", doSplashParticles);
//
//            return tag;
//        }
//
//    }
//}
