///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.ChunkCoordIntPair;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//
//import reika.dragonapi.auxiliary.ChunkManager;
//import reika.dragonapi.interfaces.blockentity.ChunkLoadingTile;
//import reika.dragonapi.interfaces.blockentity.BreakAction;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
//import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
//
//import reika.rotarycraft.registry.MachineRegistry;
//
//import java.util.Collection;
//
//public class BlockEntityChunkLoader extends BlockEntityPowerReceiver implements ChunkLoadingTile, BreakAction, RangedEffect {
//
//    public static final int BASE_RADIUS = 0;
//    public static final int FALLOFF = 524288;
//    public static final int MAX_RADIUS = RotaryConfig.COMMON.CHUNKLOADERSIZE.get();
//    private boolean loaded;
//
//    public BlockEntityChunkLoader() {
//
//    }
//
//    //@Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        this.getPowerBelow();
//        if (!world.isClientSide) {
//            if (omega >= MINSPEED) {
//                this.load();
//            } else {
//                this.unload();
//            }
//        } else {
//            if (omega >= MINSPEED) {
//                this.doParticles(world, pos);
//            }
//        }
//    }
//
//
//    private void doParticles(Level world, BlockPos pos) {
//        double[] r = {0.3125, 0.25, 0.1875, 0.125};
//        double[] h = {0.25, 0.4375, 0.625, 0.8125};
//
//        for (int i = 0; i < 4; i++) {
//            double a = (4 - i) * phi + 40;
//            double dr = r[i] + 0.0625;
//            double px = x + 0.5 + dr * Math.cos(Math.toRadians(a));
//            double pz = z + 0.5 + dr * Math.sin(Math.toRadians(a));
//            double py = y + h[i];
//
//            double vy = 0.0625;
//            float s = 0.375F;
//
//            EntityFX fx = new EntityEnderFX(world, px, py, pz, 0, vy, 0, 0xffffff).setRapidExpand().setScale(s).setIcon(Items.nether_star.getIconFromDamage(0));
//            Minecraft.getInstance().effectRenderer.addEffect(fx);
//
//            a += 180;
//            px = x + 0.5 + dr * Math.cos(Math.toRadians(a));
//            pz = z + 0.5 + dr * Math.sin(Math.toRadians(a));
//            fx = new EntityEnderFX(world, px, py, pz, 0, vy, 0, 0xffffff).setRapidExpand().setScale(s);
//            Minecraft.getInstance().effectRenderer.addEffect(fx);
//        }
//    }
//
//    private void load() {
//        if (loaded)
//            return;
//        loaded = true;
//        ChunkManager.instance.loadChunks(this);
//    }
//
//    private void unload() {
//        if (loaded) {
//            loaded = false;
//            ChunkManager.instance.unloadChunks(this);
//        }
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    protected void animateWithTick(Level world, BlockPos pos) {
//        if (omega > 0)
//            phi -= 0.25F * ReikaMathLibrary.logbase(omega + 1, 2);
//    }
//
//    @Override
//    protected void onInvalidateOrUnload(Level world, BlockPos pos, boolean invalid) {
//        if (world.isClientSide)
//            return;
//        if (invalid) {
//            this.unload();
//        }
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.CHUNKLOADER;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return 0;
//    }
//
//    @Override
//    public Collection<ChunkCoordIntPair> getChunksToLoad() {
//        return ChunkManager.getChunkSquare(xCoord, zCoord, this.getLoadingRadius());
//    }
//
//    private int getLoadingRadius() {
//        return Math.min(MAX_RADIUS, BASE_RADIUS + (int) (power - MINSPEED) / FALLOFF);
//    }
//
//    @Override
//    public void breakBlock() {
//        if (!level.isClientSide)
//            this.unload();
//    }
//
//    @Override
//    public int getRange() {
//        return this.getLoadingRadius() * 16;
//    }
//
//    @Override
//    public int getMaxRange() {
//        return MAX_RADIUS * 16;
//    }
//
//    @Override
//    public int getSlots() {
//        return 0;
//    }
//
//    
//    @Override
//    public ItemStack getStackInSlot(int slot) {
//        return null;
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
//}
