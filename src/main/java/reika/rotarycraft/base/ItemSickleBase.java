//package reika.rotarycraft.base;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.util.Mth;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.item.ItemEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.enchantment.Enchantment;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.SugarCaneBlock;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.material.Material;
//import net.minecraft.world.phys.AABB;
//import net.minecraftforge.eventbus.api.Event;
//import reika.dragonapi.ModList;
//import reika.dragonapi.base.BlockTieredResource;
//import reika.dragonapi.instantiable.data.immutable.BlockKey;
//import reika.dragonapi.instantiable.data.maps.BlockMap;
//
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Locale;
//
//import net.minecraftforge.common.IPlantable;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
//import reika.dragonapi.interfaces.registry.TreeType;
//import reika.dragonapi.libraries.ReikaAABBHelper;
//import reika.dragonapi.libraries.ReikaEnchantmentHelper;
//import reika.dragonapi.libraries.ReikaEntityHelper;
//import reika.dragonapi.libraries.ReikaPlayerAPI;
//import reika.dragonapi.libraries.io.ReikaSoundHelper;
//import reika.dragonapi.libraries.java.ReikaRandomHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.dragonapi.libraries.registry.ReikaTreeHelper;
//import reika.dragonapi.modregistry.ModWoodList;
//import reika.rotarycraft.items.tools.bedrock.ItemBedrockShears;
//
//public abstract class ItemSickleBase extends ItemRotaryTool implements EnchantableItem {
//
//    private static final ArrayList<ScytheEffect> allEffects = new ArrayList<>();
//    private static final BasicCropEffect crops = new BasicCropEffect(1);
//    private final BlockMap<ScytheEffect> effects = new BlockMap<>();
//
//    static {
//        allEffects.add(new BasicPlantEffect(1));
//        allEffects.add(new SugarcaneEffect(0.5F));
//        allEffects.add(new BasicLeafEffect(1, 1));
//        allEffects.add(crops);
//        allEffects.add(new IPlantableEffect(1));
//        if (ModList.CHROMATICRAFT.isLoaded()) {
//            allEffects.add(new DyeLeafEffect(1));
//            allEffects.add(new RainbowLeafEffect(1));
//            allEffects.add(new DyeFlowerEffect(1));
//            allEffects.add(new DecoFlowerEffect(1));
//        }
//        if (ModList.BOP.isLoaded()) {
//            allEffects.add(new BopFlowerEffect(1));
//            allEffects.add(new BopCoralEffect(1));
//            allEffects.add(new BopFoliageEffect(1));
//        }
//        if (Loader.isModLoaded("Growthcraft|Apples")) {
//            allEffects.add(new AppleEffect(1));
//        }
//        Collections.sort(allEffects);
//    }
//
//    public ItemSickleBase(Properties properties) {
//        super(properties);
//    }
//
//    @Override
//    public final int getItemEnchantability() {
//        ItemStack ref = this.getEnchantabilityReference();
//        return ref != null ? ref.getItem().getItemEnchantability(ref) : 0;
//    }
//
//    public abstract ItemStack getEnchantabilityReference();
//
//    @Override
//    public final boolean onLeftClickEntity(ItemStack is, Player ep, Entity e) {
//        if (e instanceof LivingEntity) {
//            LivingEntity elb = (LivingEntity) e;
//            AABB box = ReikaAABBHelper.getEntityCenteredAABB(e, 2).expandTowards(2, 0, 2);
//            List<LivingEntity> li = ep.level.getEntitiesWithinAABB(LivingEntity.class, box);
//            Class<? extends LivingEntity> cat = ReikaEntityHelper.getEntityCategoryClass(elb);
//            for (LivingEntity e2 : li) {
//                if (e2 != e && e2 != ep && ReikaEntityHelper.getEntityCategoryClass(e2) == cat) {
//                    e2.hurt(DamageSource.causePlayerDamage(ep), damageVsEntity);
//                }
//            }
//            if (this.isBreakable()) {
//                is.damageItem(10 * li.size(), ep);
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public boolean onItemUseFirst(ItemStack is, Player ep, Level world, BlockPos pos, int s, float a, float b, float c) {
//        ItemSickleBase sc = (ItemSickleBase) is.getItem();
//        if (ModList.AGRICRAFT.isLoaded() && !world.isClientSide()) {
//            BlockEntity te = world.getBlockEntity(pos);
//            if (te instanceof ICrop) {
//                int r = this.getCropRange();
//                for (int i = -r; i <= r; i++) {
//                    for (int k = -r; k <= r; k++) {
//                        int dx = x + i;
//                        int dz = z + k;
//                        BlockEntity te2 = world.getBlockEntity(new BlockPos(dx, y, dz));
//                        if (te2 instanceof ICrop) {
//                            ICrop ic = (ICrop) te2;
//                            if (ic.hasWeed()) {
//                                ic.clearWeed();
//                                ReikaSoundHelper.playBreakSound(world, dx, y, dz, Blocks.tallgrass);
//                                double ch = this.isBreakable() ? 40 : 80;
//                                if (ReikaRandomHelper.doWithChance(ch))
//                                    this.dropItem(is, ep, world, dx + 0.5, y + 0.5, dz + 0.5, ReikaItemHelper.tallgrass.asItemStack());
//                            }
//                        }
//                    }
//                }
//                int mined = crops.onBreakAt(world, pos, te.getBlockType(), te.getBlockMetadata(), ep, is, sc, ReikaEnchantmentHelper.getEnchantmentLevel(Enchantment.fortune, is), false);
//                if (sc.isBreakable())
//                    is.damageItem(crops.doesDamagePerBlock(is, mined), ep);
//                return false;
//            }
//        }
//        if (!world.isClientSide() && this.tryApplyPlantShear(world, x, y, z, s, ep)) {
//            if (sc.isBreakable())
//                is.damageItem(1, ep);
//            return true;
//        }
//        return false;
//    }
//
//    private boolean tryApplyPlantShear(Level world, int x, int y, int z, int side, Player ep) {
//        if (!ReikaWorldHelper.softBlocks(world, x, y, z) && ReikaWorldHelper.getMaterial(world, x, y, z) != Material.water && ReikaWorldHelper.getMaterial(world, x, y, z) != Material.lava) {
//            if (side == 0)
//                --y;
//            if (side == 1)
//                ++y;
//            if (side == 2)
//                --z;
//            if (side == 3)
//                ++z;
//            if (side == 4)
//                --x;
//            if (side == 5)
//                ++x;
//        }
//        Block b = world.getBlock(x, y, z);
//        if (b instanceof ShearablePlant) {
//            if (ep.isSneaking()) {
//                int r = this.getPlantRange();
//                for (int i = -r; i <= r; i++) {
//                    for (int j = -r; j <= r; j++) {
//                        for (int k = -r; k <= r; k++) {
//                            int dx = x + i;
//                            int dy = y + j;
//                            int dz = z + k;
//                            if (world.getBlock(dx, dy, dz) == b)
//                                ((ShearablePlant) b).shearAll(world, dx, dy, dz, ep);
//                        }
//                    }
//                }
//            } else {
//                ((ShearablePlant) b).shearSide(world, x, y, z, Direction.VALID_DIRECTIONS[side].getOpposite(), ep);
//            }
//            ReikaSoundHelper.playBreakSound(world, x, y, z, b);
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean onBlockStartBreak(ItemStack is, int x, int y, int z, Player ep) {
//        Level world = ep.worldObj;
//        Block id = world.getBlock(x, y, z);
//        if (id instanceof BlockTieredResource)
//            return false;
//        int meta = world.getBlockMetadata(x, y, z);
//        ScytheEffect eff = this.getEffect(world, x, y, z, id, meta, Direction.UNKNOWN);
//        if (world.isClientSide())
//            return eff != null;
//        if (eff != null) {
//            int num = eff.onBreakAt(world, x, y, z, id, meta, ep, is, this, ReikaEnchantmentHelper.getEnchantmentLevel(Enchantment.fortune, is), ep.isSneaking());
//            if (this.isBreakable()) {
//                int dmg = eff.doesDamagePerBlock(is, num);
//                is.damageItem(dmg, ep);
//            }
//            return true;
//        }
//        return false;
//    }
//
//    private ScytheEffect getEffect(Level world, int x, int y, int z, Block id, Direction side) {
//        ScytheEffect get = effects.get(id, meta);
//        if (get != null)
//            return get;
//        for (ScytheEffect e : allEffects) {
//            if (e.isValidStartingBlock(world, x, y, z, id, side)) {
//                effects.put(id, e);
//                return e;
//            }
//        }
//        return null;
//    }
//
//    private void dropItems(ItemStack tool, Player ep, Level world, double x, double y, double z, ArrayList<ItemStack> drops) {
//        if (ModList.CHROMATICRAFT.isLoaded() && this.isAutoCollect(tool)) {
//            for (ItemStack is : drops) {
//                this.handleItem(is, ep);
//            }
//        } else {
//            ReikaItemHelper.dropItems(world, x, y, z, drops);
//        }
//    }
//
//    private void dropBlockAsItem(ItemStack tool, Player ep, Block b2, Level world, int x, int y, int z, int meta2, int fortune) {
//        if (ModList.CHROMATICRAFT.isLoaded() && this.isAutoCollect(tool)) {
//            this.setItemCollection(ep);
//        }
//        b2.dropBlockAsItem(world, x, y, z, meta2, fortune);
//    }
//
//    private void dropItem(ItemStack tool, Player ep, Level world, BlockPos pos, ItemStack drop) {
//        if (ModList.CHROMATICRAFT.isLoaded() && this.isAutoCollect(tool)) {
//            this.handleItem(drop, ep);
//        } else {
//            ReikaItemHelper.dropItem(world, pos, drop);
//        }
//    }
//
//    private void handleItem(ItemStack drop, Player ep) {
//        if (!MinecraftForge.EVENT_BUS.post(new EntityItemPickupEvent(ep, new ItemEntity(ep.level, ep.getX(), ep.getY(), ep.getZ(), drop))))
//            ReikaPlayerAPI.addOrDropItem(drop, ep);
//    }
//
//    public abstract int getLeafRange();
//
//    public abstract int getPlantRange();
//
//    public abstract int getCropRange();
//
//    public abstract boolean canActAsShears();
//
//    public abstract boolean isBreakable();
//
///*        @ModDependent(ModList.CHROMATICRAFT)
//        public final Result getEnchantValidity(Enchantment e, ItemStack is) {
//            if (is.getItem() == this) {
//                if (e == Enchantment.fortune || e == Enchantment.efficiency || e == Enchantment.unbreaking)
//                    return Result.ALLOW;
//                if (e == ChromaEnchants.AIRMINER.getEnchantment() || e == ChromaEnchants.AUTOCOLLECT.getEnchantment())
//                    return Result.ALLOW;
//            }
//            return Result.DEFAULT;
//        }
//
//        @ModDependent(ModList.CHROMATICRAFT)
//        private boolean isAutoCollect(ItemStack tool) {
//            return ChromaEnchants.AUTOCOLLECT.getLevel(tool) > 0;
//        }
//
//        @ModDependent(ModList.CHROMATICRAFT)
//        private void setItemCollection(Player ep) {
//            ChromaticEventManager.instance.collectItemPlayer = ep;
//        }*/
//
//    @Override
//    public final EnumEnchantmentType getEnchantingCategory() {
//        return EnumEnchantmentType.digger;
//    }
//
//    private static abstract class ScytheEffect implements Comparable<ScytheEffect> {
//
//        protected final BlockKey block;
//        protected final float rangeXZ;
//        protected final float rangeY;
//
//        protected ScytheEffect(BlockKey bk, float rx, float ry) {
//            block = bk;
//            rangeXZ = rx;
//            rangeY = ry;
//        }
//
//        protected abstract int getRange(ItemStack is, ItemSickleBase item);
//
//        protected boolean isValidStartingBlock(Level world, int x, int y, int z, Block b, Direction side) {
//            return block.match(b);
//        }
//
//        protected abstract int onBreakAt(Level world, int x, int y, int z, Block b, int meta, Player ep, ItemStack is, ItemSickleBase item, int fortune, boolean ignoreMeta);
//
//        protected int doesDamagePerBlock(ItemStack is, int mined) {
//            return 1;
//        }
//
//        protected int getPriority() {
//            return 0;
//        }
//
//        public final int compareTo(ScytheEffect e) {
//            return Integer.compare(this.getPriority(), e.getPriority());
//        }
//
//    }
//
//    private abstract static class MineSimilarEffect extends ScytheEffect {
//
//        protected MineSimilarEffect(BlockKey bk, float rx, float ry) {
//            super(bk, rx, ry);
//        }
//
//        protected boolean matchesBlock(Block src, Level world, BlockPos pos, Block b2) {
//            return this.matchesBlock(src, b2);
//        }
//
//        protected boolean matchesBlock(Block src, Block b) {
//            return b == src;
//        }
//
//        @Override
//        protected final int onBreakAt(Level world, int x, int y, int z, Block b, int meta, Player ep, ItemStack is, ItemSickleBase item, int fortune, boolean ignoreMeta) {
//            int ret = 0;
//            int r = this.getRange(is, item);
//            int rx = Mth.floor(rangeXZ * r);
//            int ry = Mth.floor(rangeY * r);
//            for (int i = -rx; i <= rx; i++) {
//                for (int j = -ry; j <= ry; j++) {
//                    for (int k = -rx; k <= rx; k++) {
//                        int dx = x + i;
//                        int dy = y + j;
//                        int dz = z + k;
//                        Block id2 = world.getBlockState(new BlockPos(dx, dy, dz)).getBlock();
//                        int meta2 = world.getBlockMetadata(dx, dy, dz);
//                        if (this.matchesBlock(b, meta, world, dx, dy, dz, id2, meta2, ignoreMeta)) {
//                            this.doDrops(world, dx, dy, dz, id2, meta2, ep, is, item, fortune);
//                            ReikaSoundHelper.playBreakSound(world, dx, dy, dz, id2);
//                            this.breakAt(world, dx, dy, dz, id2, meta2);
//                            ret++;
//                        }
//                    }
//                }
//            }
//            return ret;
//        }
//
//        protected void doDrops(Level world, int dx, int dy, int dz, Block id2, Player ep, ItemStack is, ItemSickleBase item, int fortune) {
//            ArrayList<ItemStack> items = new ArrayList<>();
//            if (this.collateItems()) {
//                ReikaItemHelper.addToList(items, id2.getDrops(world, dx, dy, dz, fortune));
//            } else if (this.allowShearing(world, dx, dy, dz, id2) && item.canActAsShears() && ItemBedrockShears.getHarvestResult(id2, meta2, ep, world, dx, dy, dz) == Event.Result.ALLOW) {
//                if (id2 instanceof IShearable) {
//                    ArrayList<ItemStack> li = ((IShearable) id2).onSheared(is, world, dx, dy, dz, fortune);
//                    item.dropItems(is, ep, world, dx + itemRand.nextDouble(), dy + itemRand.nextDouble(), dz + itemRand.nextDouble(), li);
//                } else {
//                    item.dropItem(is, ep, world, dx + 0.5, dy + 0.5, dz + 0.5, new ItemStack(id2, 1, ItemBedrockShears.getDroppedMeta(id2)));
//                }
//            } else {
//                item.dropBlockAsItem(is, ep, id2, world, dx, dy, dz, fortune);
//            }
//            if (!items.isEmpty())
//                item.dropItems(is, ep, world, dx, dy, dz, items);
//        }
//
//        protected void breakAt(Level world, int x, int y, int z, Block b) {
//            world.setBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), 3);
//        }
//
//        protected boolean allowShearing(Level world, int x, int y, int z, Block b, int meta) {
//            return false;
//        }
//
//        protected boolean collateItems() {
//            return false;
//        }
//
//    }
//
//    private static class IPlantableEffect extends MineSimilarEffect {
//
//        protected IPlantableEffect(float r) {
//            super(null, r, r);
//        }
//
//        @Override
//        protected boolean isValidStartingBlock(Level world, int x, int y, int z, Block b, int meta, Direction side) {
//            return b instanceof IPlantable;
//        }
//
//        @Override
//        protected int getRange(ItemStack is, ItemSickleBase item) {
//            return item.getPlantRange();
//        }
//
//        @Override
//        protected boolean allowShearing(Level world, int x, int y, int z, Block b, int meta) {
//            return true;//b instanceof IShearable;
//        }
//
//        @Override
//        protected int getPriority() {
//            return Integer.MAX_VALUE;
//        }
//
//    }
//
//    private static class BasicPlantEffect extends MineSimilarEffect {
//
//        protected BasicPlantEffect(float r) {
//            super(null, r, r);
//        }
//
//        @Override
//        protected boolean isValidStartingBlock(Level world, int x, int y, int z, Block b, int meta, Direction side) {
//            ReikaPlantHelper plant = ReikaPlantHelper.getPlant(b);
//            return plant != null && plant != ReikaPlantHelper.CROP && plant != ReikaPlantHelper.SUGARCANE;
//        }
//
//        @Override
//        protected int getRange(ItemStack is, ItemSickleBase item) {
//            return item.getPlantRange();
//        }
//
//        @Override
//        protected boolean allowShearing(Level world, int x, int y, int z, Block b, int meta) {
//            return ReikaPlantHelper.getPlant(b) != ReikaPlantHelper.NETHERWART;
//        }
//
//        @Override
//        protected boolean matchesBlock(Block src, int srcmeta, Block b, int meta, boolean ignoreMeta) {
//            return super.matchesBlock(src, srcmeta, b, meta, ignoreMeta || !this.needsMeta(ReikaPlantHelper.getPlant(b)));
//        }
//
//        private boolean needsMeta(ReikaPlantHelper p) {
//            if (p == null)
//                return true;
//            switch (p) {
//                case CACTUS:
//                case LILYPAD:
//                case VINES:
//                    return false;
//                default:
//                    return true;
//            }
//        }
//
//    }
//
//    private static class SugarcaneEffect extends MineSimilarEffect {
//
//        protected SugarcaneEffect(float r) {
//            super(null, r, r);
//        }
//
//        @Override
//        protected boolean isValidStartingBlock(Level world, BlockPos pos, Block b, Direction side) {
//            return (b instanceof SugarCaneBlock || b instanceof Reedlike) && world.getBlockState(pos.below()).getBlock() == b;
//        }
//
//        @Override
//        protected boolean matchesBlock(Block src, Level world, BlockPos pos, Block b2, int meta2, boolean ignoreMeta) {
//            return super.matchesBlock(src, world, pos, b2, true) && world.getBlockState(pos.below()).getBlock() == src;
//        }
//
//        @Override
//        protected int getRange(ItemStack is, ItemSickleBase item) {
//            return item.getCropRange();
//        }
//
//        @Override
//        protected int doesDamagePerBlock(ItemStack is, int mined) {
//            return Math.max(1, mined / 2);
//        }
//
//    }
//
//    private static class BasicLeafEffect extends MineSimilarEffect {
//
//        protected BasicLeafEffect(float rx, float ry) {
//            super(null, rx, ry);
//        }
//
//        @Override
//        protected boolean matchesBlock(Block src, int srcmeta, Block b, int meta, boolean ignoreMeta) {
//            return this.getTree(src) == this.getTree(b);
//        }
//
//        @Override
//        protected boolean isValidStartingBlock(Level world, int x, int y, int z, Block b, int meta, Direction side) {
//            return this.getTree(b) != null;
//        }
//
//        private TreeType getTree(Block b) {
//            TreeType t = ReikaTreeHelper.getTreeFromLeaf(b);
//            if (t == null)
//                t = ModWoodList.getModWoodFromLeaf(b);
//            return t;
//        }
//
//        @Override
//        protected int doesDamagePerBlock(ItemStack is, int mined) {
//            return Math.max(1, mined / 12);
//        }
//
//        @Override
//        protected int getRange(ItemStack is, ItemSickleBase item) {
//            return item.getLeafRange();
//        }
//
//        @Override
//        protected boolean allowShearing(Level world, int x, int y, int z, Block b, int meta) {
//            return false;
//        }
//
//    }
//
//    private static class BasicCropEffect extends MineSimilarEffect {
//
//        protected BasicCropEffect(float r) {
//            super(null, r, r / 3F);
//        }
//
//        @Override
//        protected boolean matchesBlock(Block src, int srcmeta, Level world, int x, int y, int z, Block b2, int meta2, boolean ignoreMeta) {
//            CropType c = this.getCrop(b2, meta2);
//            return this.getCrop(src, srcmeta) == c && c.isRipe(world, x, y, z);
//        }
//
//        @Override
//        protected boolean isValidStartingBlock(Level world, int x, int y, int z, Block b, int meta, Direction side) {
//            CropType c = this.getCrop(b, meta);
//            return c != null && c.isRipe(world, x, y, z);
//        }
//
//        private CropType getCrop(Block b, int meta) {
//            CropType t = ReikaCropHelper.getCrop(b);
//            if (t == null)
//                t = ModCropList.getModCrop(b, meta);
//            return t;
//        }
//
//        @Override
//        protected int doesDamagePerBlock(ItemStack is, int mined) {
//            return Math.max(1, mined / 2);
//        }
//
//        @Override
//        protected int getRange(ItemStack is, ItemSickleBase item) {
//            return item.getCropRange();
//        }
//
//        @Override
//        protected boolean allowShearing(Level world, int x, int y, int z, Block b, int meta) {
//            return false;
//        }
//
//        @Override
//        protected void doDrops(Level world, int x, int y, int z, Block b, int meta, Player ep, ItemStack is, ItemSickleBase item, int fortune) {
//            CropType c = this.getCrop(b, meta);
//            ArrayList<ItemStack> li = c.getDrops(world, x, y, z, fortune);
//            if (!c.destroyOnHarvest())
//                CropMethods.removeOneSeed(c, li);
//            item.dropItems(is, ep, world, x, y, z, li);
//        }
//
//        @Override
//        protected void breakAt(Level world, int x, int y, int z, Block b, int meta) {
//            CropType c = this.getCrop(b, meta);
//            if (c.destroyOnHarvest())
//                super.breakAt(world, x, y, z, b, meta);
//            else
//                c.setHarvested(world, x, y, z);
//        }
//
//    }
//
////        private static class DyeLeafEffect extends MineSimilarEffect {
////
////            protected DyeLeafEffect(float r) {
////                super(null, r, r);
////            }
////
////            @Override
////            protected boolean isValidStartingBlock(Level world, int x, int y, int z, Block b, int meta, Direction side) {
////                return b == ChromatiAPI.getAPI().trees().getDyeLeaf(true) || b == ChromatiAPI.getAPI().trees().getDyeLeaf(false);
////            }
////
////            @Override
////            protected boolean matchesBlock(Block src, int srcmeta, Block b, int meta, boolean ignoreMeta) {  //never jump metas
////                return (b == ChromatiAPI.getAPI().trees().getDyeLeaf(false) || b == ChromatiAPI.getAPI().trees().getDyeLeaf(true)) && srcmeta == meta;
////            }
////
////            @Override
////            protected int doesDamagePerBlock(ItemStack is, int mined) {
////                return Math.max(1, mined/12);
////            }
////
////            @Override
////            protected int getRange(ItemStack is, ItemSickleBase item) {
////                return item.getLeafRange();
////            }
////
////        }
//
////        private static class RainbowLeafEffect extends MineSimilarEffect {
////
////            protected RainbowLeafEffect(float r) {
////                super(null, r, r);
////            }
////
////            @Override
////            protected boolean matchesBlock(Block src, int srcmeta, Block b, int meta, boolean ignoreMeta) {  //always jump metas
////                return b == ChromatiAPI.getAPI().trees().getRainbowLeaf();
////            }
////
////            @Override
////            protected int doesDamagePerBlock(ItemStack is, int mined) {
////                return Math.max(1, mined/9);
////            }
////
////            @Override
////            protected boolean isValidStartingBlock(Level world, int x, int y, int z, Block b, int meta, Direction side) {
////                return b == ChromatiAPI.getAPI().trees().getRainbowLeaf();
////            }
////
////            @Override
////            protected int getRange(ItemStack is, ItemSickleBase item) {
////                return item.getLeafRange();
////            }
////
////            @Override
////            protected boolean collateItems() {
////                return true;
////            }
////
////        }
//
////        private static class DecoFlowerEffect extends MineSimilarEffect {
////
////            protected DecoFlowerEffect(float r) {
////                super(null, r, r);
////            }
////
////            @Override
////            protected boolean matchesBlock(Block src, int srcmeta, Block b, int meta, boolean ignoreMeta) {  //never jump metas
////                return b == ChromatiAPI.getAPI().trees().getDecoFlower() && srcmeta == meta;
////            }
////
////            @Override
////            protected boolean isValidStartingBlock(Level world, int x, int y, int z, Block b, int meta, Direction side) {
////                return b == ChromatiAPI.getAPI().trees().getDecoFlower();
////            }
////
////            @Override
////            protected int getRange(ItemStack is, ItemSickleBase item) {
////                return item.getPlantRange();
////            }
////
////            @Override
////            protected boolean allowShearing(Level world, int x, int y, int z, Block b, int meta) {
////                return true;
////            }
////
////        }
//
////        private static class AppleEffect extends MineSimilarEffect {
////
////            protected AppleEffect(float r) {
////                super(null, r, r/5F);
////            }
////
////            @Override
////            protected boolean isValidStartingBlock(Level world, int x, int y, int z, Block b, int meta, Direction side) {
////                return this.matchesBlock(b, meta, b, meta, false);
////            }
////
////            @Override
////            protected boolean matchesBlock(Block src, int srcmeta, Block b, int meta, boolean ignoreMeta) {
////                if (meta < 2)
////                    return false;
////                String n = b.getClass().getName().toLowerCase(Locale.ENGLISH);
////                return n.startsWith("growthcraft.apples") && n.endsWith("apple");
////            }
////
////            @Override
////            protected int getRange(ItemStack is, ItemSickleBase item) {
////                return item.getCropRange();
////            }
////
////            @Override
////            protected boolean allowShearing(Level world, int x, int y, int z, Block b, int meta) {
////                return false;
////            }
////
////        }
//
//    private static abstract class BlockMatchEffect extends MineSimilarEffect {
//
//        protected BlockMatchEffect(float rx, float ry) {
//            super(null, rx, ry);
//        }
//
//        @Override
//        protected boolean isValidStartingBlock(Level world, int x, int y, int z, Block b, int meta, Direction side) {
//            return this.matchBlock(b);
//        }
//
//        @Override
//        protected final boolean matchesBlock(Block src, int srcmeta, Block b, int meta, boolean ignoreMeta) {
//            return this.matchBlock(b) && (ignoreMeta || meta == srcmeta);
//        }
//
//        protected abstract boolean matchBlock(Block b);
//
//    }
//
////        private static class DyeFlowerEffect extends BlockMatchEffect {
////
////            protected DyeFlowerEffect(float r) {
////                super(r, r);
////            }
////
////            @Override
////            protected boolean matchBlock(Block b) {
////                return ChromatiAPI.getAPI().trees().getDyeFlower() == b;
////            }
////
////            @Override
////            protected int getRange(ItemStack is, ItemSickleBase item) {
////                return item.getPlantRange();
////            }
////
////            @Override
////            protected boolean allowShearing(Level world, int x, int y, int z, Block b, int meta) {
////                return true;
////            }
////
////        }
//
////        private static class BopFlowerEffect extends BlockMatchEffect {
////
////            protected BopFlowerEffect(float r) {
////                super(r, r);
////            }
////
////            @Override
////            protected boolean matchBlock(Block b) {
////                return BoPBlockHandler.getInstance().isFlower(b);
////            }
////
////            @Override
////            protected int getRange(ItemStack is, ItemSickleBase item) {
////                return item.getPlantRange();
////            }
////
////            @Override
////            protected boolean allowShearing(Level world, int x, int y, int z, Block b, int meta) {
////                return true;
////            }
////
////        }
//
////        private static class BopCoralEffect extends BlockMatchEffect {
////
////            protected BopCoralEffect(float r) {
////                super(r, r);
////            }
////
////            @Override
////            protected boolean matchBlock(Block b) {
////                return BoPBlockHandler.getInstance().isCoral(b);
////            }
////
////            @Override
////            protected int getRange(ItemStack is, ItemSickleBase item) {
////                return item.getPlantRange();
////            }
////
////            @Override
////            protected boolean allowShearing(Level world, int x, int y, int z, Block b, int meta) {
////                return true;
////            }
////
////        }
//
////        private static class BopFoliageEffect extends BlockMatchEffect {
////
////            protected BopFoliageEffect(float r) {
////                super(r, r);
////            }
////
////            @Override
////            protected boolean matchBlock(Block b) {
////                return BoPBlockHandler.getInstance().foliage == b;
////            }
////
////            @Override
////            protected int getRange(ItemStack is, ItemSickleBase item) {
////                return item.getPlantRange();
////            }
////
////            @Override
////            protected boolean allowShearing(Level world, int x, int y, int z, Block b, int meta) {
////                return true;
////            }
////
////        }
//}