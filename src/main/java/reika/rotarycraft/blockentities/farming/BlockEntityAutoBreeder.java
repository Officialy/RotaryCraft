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
//import net.minecraft.world.entity.AgeableMob;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.animal.*;
//import net.minecraft.world.entity.npc.Villager;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.phys.AABB;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.ModList;
//import reika.dragonapi.auxiliary.trackers.ReflectiveFailureTracker;
//import reika.dragonapi.instantiable.data.KeyedItemStack;
//import reika.dragonapi.instantiable.data.maps.MultiMap;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
//import reika.rotarycraft.auxiliary.interfaces.IdleComparator;
//import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
//import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
//
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.RotaryItems;
//
//import java.util.List;
//
//public class BlockEntityAutoBreeder extends InventoriedPowerReceiver implements RangedEffect, ConditionalOperation, IdleComparator {
//
//    public static final int FALLOFF = 2048; //2kW per extra meter
//
//    private static final MultiMap<Class<?>, KeyedItemStack> feedItems = new MultiMap<>(MultiMap.CollectionType.HASHSET);
//
//    static {
//        addFeedItem(Sheep.class, Items.WHEAT);
//        addFeedItem(Cow.class, Items.WHEAT);
//        addFeedItem(Chicken.class, Items.WHEAT_SEEDS);
//        addFeedItem(Pig.class, Items.CARROT);
//        addFeedItem(Wolf.class, Items.PORKCHOP);
//        addFeedItem(Wolf.class, Items.COOKED_PORKCHOP);
//        addFeedItem(Wolf.class, Items.BEEF);
//        addFeedItem(Wolf.class, Items.COOKED_BEEF);
//        addFeedItem(Wolf.class, Items.CHICKEN);
//        addFeedItem(Wolf.class, Items.COOKED_CHICKEN);
////        addFeedItem(Wolf.class, Items.FISH);
////        addFeedItem(Wolf.class, Items.COOKED_FISHED);
////        addFeedItem(Ocelot.class, Items.FISH);
////        addFeedItem(Ocelot.class, Items.COOKED_FISHED);
//
//        addFeedItem(Sheep.class, RotaryItems.CANOLA_HUSKS.get());
//        addFeedItem(Cow.class, RotaryItems.CANOLA_HUSKS.get());
//        addFeedItem(Chicken.class, RotaryItems.CANOLA_HUSKS.get());
//        addFeedItem(Pig.class, RotaryItems.CANOLA_HUSKS.get());
//
//        if (ModList.TWILIGHT.isLoaded()) {
//            try {
//                addFeedItem(Class.forName("twilightforest.entity.passive.EntityTFBighorn"), Items.WHEAT);
//                addFeedItem(Class.forName("twilightforest.entity.passive.EntityTFBighorn"), RotaryItems.CANOLA_HUSKS.get());
//                addFeedItem(Class.forName("twilightforest.entity.passive.EntityTFDeer"), Items.WHEAT);
//                addFeedItem(Class.forName("twilightforest.entity.passive.EntityTFDeer"), RotaryItems.CANOLA_HUSKS.get());
//            } catch (Exception e) {
//                e.printStackTrace();
//                ReflectiveFailureTracker.instance.logModReflectiveFailure(ModList.TWILIGHT, e);
//            }
//        }
//
//        addFeedItem(Villager.class, Items.EMERALD);
//    }
//
//    private static void addFeedItem(Class entity, ItemStack food) {
//        addFeedItem(entity, new KeyedItemStack(food));
//    }
//
//    private static void addFeedItem(Class entity, Item food) {
//        addFeedItem(entity, new KeyedItemStack(food));
//    }
//
//    private static void addFeedItem(Class entity, KeyedItemStack food) {
//        food.setSimpleHash(true).setSized(false);
//        feedItems.addValue(entity, food);
//    }
//
//    @Override
//    public boolean isIdle() {
//        for (int i = 0; i < itemHandler.getSlots(); i++) {
//            ItemStack in = itemHandler.getStackInSlot(i);
//            if (in != null) {
//                if (feedItems.containsValue(new KeyedItemStack(in).setSimpleHash(true))) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//
//    public int getRange() {
//        int range = 8 + (int) ((power - MINPOWER) / FALLOFF);
//        if (range > this.getMaxRange())
//            return this.getMaxRange();
//        return range;
//    }
//
//    public int getMaxRange() {
//        return Math.max(24, RotaryConfig.COMMON.BREEDERRANGE.get());
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
//        //ReikaChunkHelper.emptyChunk(world, x, z);
//        this.getPowerBelow();
//        if (power < MINPOWER)
//            return;
//        List<AgeableMob> inrange = this.getEntities(world, pos);
//        this.breed(world, pos, inrange);
//    }
//
//    @Override
//    protected void animateWithTick(Level level, BlockPos blockPos) {
//
//    }
//
//    private boolean canBreed(Entity ent) {
//        return this.getFeedItem(ent) >= 0;
//    }
//
//    private int getFeedItem(Entity ent) {
//        for (KeyedItemStack kis : feedItems.get(ent.getClass())) {
//            int slot = ReikaInventoryHelper.locateInInventory(kis, inv);
//            if (slot != -1)
//                return slot;
//        }
//        return -1;
//    }
//
//    private void useFeedItem(Entity ent) {
//        int slot = this.getFeedItem(ent);
//        if (slot == -1)
//            return;
//        if (inv[slot] == null)
//            return;
//        ReikaInventoryHelper.decrStack(slot, inv);
//    }
//
//    private void breed(Level world, BlockPos pos, List<AgeableMob> inroom) {
//        boolean pathing = false;
//        if (tickcount >= 20) {
//            tickcount = 0;
//            pathing = true;
//        }
//        for (AgeableMob ent : inroom) {
//            //ReikaJavaLibrary.pConsole(this.canBreed(ent)+" for "+ent.getCommandSenderName());
//            if (this.canBreed(ent)) {
//                if (!(ent instanceof EntityTameable) || (ent instanceof EntityTameable && !((EntityTameable) ent).isSitting())) {
//                    if (pathing) {
//                        if (this.canDirectToMate(ent)) {
//                            ent.getNavigator().clearPathEntity();
//                            PathEntity path = ent.getNavigator().getPathToXYZ(pos);
//                            ent.getNavigator().setPath(path, 1F);
//                        } else {
//                            ent.getNavigator().clearPathEntity();
//                        }
//                    }
//                }
//                if (!ent.isChild() && ent.getGrowingAge() <= 0 && ReikaMathLibrary.py3d(x - ent.getY(), y - ent.getY(), z - ent.getZ()) <= 2.4) {
//                    if (!(ent instanceof EntityTameable) || (ent instanceof EntityTameable && ((EntityTameable) ent).isTamed())) {
//                        this.breed(ent);
//                        int n = 1 + DragonAPI.rand.nextInt(3);
//                        for (int i = 0; i < n; i++) {
//                            double var4 = DragonAPI.rand.nextGaussian() * 0.02D;
//                            double var6 = DragonAPI.rand.nextGaussian() * 0.02D;
//                            double var8 = DragonAPI.rand.nextGaussian() * 0.02D;
//                            ent.level.addParticle("heart", ent.getY + DragonAPI.rand.nextFloat() * ent.width * 2.0F - ent.width, ent.getY() + 0.5D + DragonAPI.rand.nextFloat() * ent.height, ent.posZ + DragonAPI.rand.nextFloat() * ent.width * 2.0F - ent.width, var4, var6, var8);
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private boolean canDirectToMate(AgeableMob ent) {
//        if (ent instanceof Animal && ((Animal) ent).isInLove())
//            return false;
//        if (ent instanceof Villager && ((Villager) ent).isMating())
//            return false;
//        return !ent.isChild() && ent.getGrowingAge() == 0;
//    }
//
//    private void breed(AgeableMob e) {
//        boolean flag = false;
//        if (e instanceof Animal) {
//            Animal ent = (Animal) e;
//            if (!ent.isInLove()) {
//                ent.inLove = 600;
//                flag = true;
//            }
//        } else if (e instanceof Villager) {
//            Villager ent = (Villager) e;
//            if (!ent.isMating()) {
//                ent.setMating(true);
//                flag = true;
//            }
//        }
//        if (flag)
//            this.useFeedItem(e);
//    }
//
//    private List<AgeableMob> getEntities(Level world, BlockPos pos) {
//        AABB room = this.getBox(pos, this.getRange());
//        List inroom = world.getEntitiesOfClass(AgeableMob.class, room);
//        return inroom;
//    }
//
//    private AABB getBox(BlockPos pos, int range) {
//        AABB box = new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).expandTowards(range, range, range);
//        return box;
//    }
//
//    private AABB getRoom(Level world, BlockPos pos) {
//        int minx = x;
//        int maxx = x;
//        int miny = y;
//        int maxy = y;
//        int minz = z;
//        int maxz = z;
//
//        boolean exit = false;
//        for (int i = 1; i < 15 && !exit; i++) {
//            if (world.getBlockState(x + i + 1, y, z).isOpaqueCube())
//                exit = true;
//            else
//                maxx = x + i;
//        }
//        exit = false;
//        for (int i = 1; i < 15 && !exit; i++) {
//            if (world.getBlockState(x - i, y, z).isOpaqueCube())
//                exit = true;
//            else
//                minx = x - i;
//        }
//        exit = false;
//        for (int i = 1; i < 15 && !exit; i++) {
//            if (world.getBlockState(x, y + i + 1, z).isOpaqueCube())
//                exit = true;
//            else
//                maxy = y + i;
//        }
//        exit = false;
//        for (int i = 1; i < 15 && !exit; i++) {
//            if (world.getBlockState(x, y - i, z).isOpaqueCube())
//                exit = true;
//            else
//                miny = x - i;
//        }
//        exit = false;
//        for (int i = 1; i < 15 && !exit; i++) {
//            if (world.getBlockState(pos + i + 1).isOpaqueCube())
//                exit = true;
//            else
//                maxz = z + i;
//        }
//        exit = false;
//        for (int i = 1; i < 15 && !exit; i++) {
//            if (world.getBlockState(pos - i).isOpaqueCube())
//                exit = true;
//            else
//                minz = z - i;
//        }
//        exit = false;
//        return new AABB(minx, miny, minz, maxx, maxy, maxz);
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
//        return MachineRegistry.AUTOBREEDER;
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int slot, ItemStack is) {
//        return feedItems.containsValue(new KeyedItemStack(is).setSimpleHash(true));
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return this.isIdle() ? 15 : 0;
//    }
//
//    @Override
//    protected String getTEName() {
//        return null;
//    }
//
//    @Override
//    public boolean areConditionsMet() {
//        return !ReikaInventoryHelper.isEmpty(inv);
//    }
//
//    @Override
//    public String getOperationalStatus() {
//        return this.areConditionsMet() ? "Operational" : "No Items";
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 18;
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
