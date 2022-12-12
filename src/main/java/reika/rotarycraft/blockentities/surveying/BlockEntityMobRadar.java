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
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.entity.EntitySelector;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.AABB;
//import reika.dragonapi.interfaces.blockentity.GuiController;
//import reika.rotarycraft.api.Interfaces.RadarJammer;
//import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
//import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
//import reika.rotarycraft.registry.MachineRegistry;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public class BlockEntityMobRadar extends BlockEntityPowerReceiver implements GuiController, RangedEffect {
//
//    public static final int FALLOFF = 1024; //1kW per extra meter
//    private final ConfigurableEntitySelector selector = new ConfigurableEntitySelector();
//    public String owner;
//    public boolean hostile = true;
//    public boolean animal = true;
//    public boolean player = true;
//    private List<LivingEntity> inzone = new ArrayList<>();
//    private boolean isJammed;
//
//    public List<LivingEntity> getEntities() {
//        return Collections.unmodifiableList(inzone);
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        this.getPowerBelow();
//        this.getMobs(world, pos);
//    }
//
//    public int getRange() {
//        int range = (int) (8 + (power - MINPOWER) / FALLOFF);
//        return Math.min(range, this.getMaxRange());
//    }
//
//    public boolean isJammed() {
//        return isJammed;
//    }
//
//    private void getMobs(Level world, BlockPos pos) {
//        isJammed = false;
//        int range = this.getRange();
//        AABB zone = AABB.getBoundingBox(x - range, 0, z - range, x + 1 + range, 255, z + 1 + range);
//        inzone = world.selectEntitiesWithinAABB(LivingEntity.class, zone, this.getSelector());
//        for (LivingEntity ent : inzone) {
//            if (ent instanceof RadarJammer && ((RadarJammer) ent).jamRadar(level, xCoord, yCoord, zCoord)) {
//                isJammed = true;
//                break;
//            }
//            int ex = (int) ent.getY - x;
//            int ey = (int) ent.getY() - y;
//            int ez = (int) ent.posZ - z;
//        }
//    }
//
//    private EntitySelector getSelector() {
//        selector.animals = animal;
//        selector.players = player;
//        selector.hostiles = hostile;
//        return selector;
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
//        if (power < MINPOWER)
//            return;
//        //this.phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(this.omega+1, 2), 1.05);
//        phi += 4F;
//    }
//
//    @Override
//    public AABB getRenderBoundingBox() {
//        return INFINITE_EXTENT_AABB;
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//        if (owner != null && !owner.isEmpty())
//            NBT.setString("own", owner);
//        NBT.putBoolean("jam", isJammed);
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//        owner = NBT.getString("own");
//        isJammed = NBT.getBoolean("jam");
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.MOBRADAR;
//    }
//
//    @Override
//    public int getMaxRange() {
//        return 256;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return 0;
//    }
//
//}
