///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.entities;
//
//import reika.rotarycraft.registry.RotaryItems;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.libraries.registry.ReikaParticleHelper;
//import reika.rotarycraft.registry.EngineType;
//import net.minecraft.core.BlockPos;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.util.Mth;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.entity.vehicle.Minecart;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.phys.Vec3;
//import net.neoforged.common.NeoForge;
//
//public class EntityGasMinecart extends Minecart {
//
//    public double pushX;
//    public double pushZ;
//    private int fuel = 0;
//    private int fueltick = 0;
//
//    public EntityGasMinecart(final EntityType<? extends Minecart> entityType, Level level) {
//        super(EntityType.MINECART, level);
//    }
//
//    public EntityGasMinecart(Level par1World, double par2, double par4, double par6) {
//        super(par1World, par2, par4, par6);
//    }
//
//    @Override
//    public Type getMinecartType() {
//        return Type.FURNACE;
//    }
//
//    /**
//     * Called to update the entity's position/logic.
//     */
//    @Override
//    public void tick() {
//        super.tick();
//
//        fueltick++;
//
//        if (fueltick >= EngineType.GAS.getFuelUnitDuration() * 25) {
//            fueltick = 0;
//            if (fuel > 0)
//                --fuel;
//        }
//
//        if (fuel <= 0)
//            pushX = pushZ = 0.0D;
//
//        //this.setMinecartPowered(fuel > 0);
//
//        if (this.isMinecartPowered() && DragonAPI.rand.nextInt(4) == 0)
//            ReikaParticleHelper.SMOKE.spawnAt(level, getX(), getY() + 0.8D, getZ());
//    }
//
//    @Override
//    public void kill() {
//        super.kill();
//    }
//
//    @Override
//    protected void func_145821_a(int par1, int par2, int par3, double par4, double par6, Block par8, int par9) {
//        super.func_145821_a(par1, par2, par3, par4, par6, par8, par9);
//
//        double d2 = pushX * pushX + pushZ * pushZ;
//
//        if (d2 > 1.0E-4D && motionX * motionX + motionZ * motionZ > 0.001D) {
//            d2 = Mth.sqrt((float) d2);
//            pushX /= d2;
//            pushZ /= d2;
//
//            if (pushX * motionX + pushZ * motionZ < 0.0D) {
//                pushX = 0.0D;
//                pushZ = 0.0D;
//            } else {
//                pushX = motionX;
//                pushZ = motionZ;
//            }
//        }
//
//        int futurex = getBlockX();
//        int futurez = getBlockZ();
//
//        if (motionX > 0)
//            futurex++;
//        if (motionX < 0)
//            futurex--;
//        if (motionZ > 0)
//            futurez++;
//        if (motionZ < 0)
//            futurez--;
//
//        if (this.headingToCurve(futurex, getBlockY(), futurez)) {
//            if (motionX > 0)
//                motionX = 0.25;
//            if (motionX < 0)
//                motionX = -0.25;
//
//            if (motionZ > 0)
//                motionZ = 0.25;
//            if (motionZ < 0)
//                motionZ = -0.25;
//        } else {
//
//        }
//    }
//
//    private boolean headingToCurve(int futurex, int y, int futurez) {
//        Block id = level.getBlockState(new BlockPos(futurex, y, futurez)).getBlock();
//        Block id2 = level.getBlockState(new BlockPos(getX(), y, getZ())).getBlock();
//        if (id != Blocks.RAIL && id2 != Blocks.RAIL)
//            return false;
//        return false;
//    }
//
//    @Override
//    public void setDragAir(double value) {
//        double d0 = pushX * pushX + pushZ * pushZ;
//
//        if (d0 > 1.0E-4D) {
//            d0 = Mth.sqrt((float) d0);
//            pushX /= d0;
//            pushZ /= d0;
//            double d1 = 0.05D;
//            motionX *= 0.9800000011920929D;
//            motionY *= 0.0D;
//            motionZ *= 0.9800000011920929D;
//            motionX += pushX * d1;
//            motionZ += pushZ * d1;
//        } else {
//            motionX *= 0.9800000190734863D;
//            motionY *= 0.0D;
//            motionZ *= 0.9800000190734863D;
//        }
//        super.setDragAir(value);
//    }
//
//    /**
//     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
//     */
//    @Override
//    public boolean interactFirst(Player par1Player) {
//        if (NeoForge.EVENT_BUS.post(new MinecartInteractEvent(this, par1Player))) {
//            return true;
//        }
//        ItemStack itemstack = par1Player.inventory.getSelected();
//
//        if (itemstack != null && itemstack.getItem() == RotaryItems.ETHANOL.get()) {
//            if (--itemstack.getCount() == 0) {
//                par1Player.inventory.setInventorySlotContents(par1Player.inventory.currentItem, (ItemStack) null);
//            }
//
//            fuel += 1;
//        }
//
//        pushX = getY - par1Player.getY;
//        pushZ = posZ - par1Player.posZ;
//        return true;
//    }
//
//    /**
//     * (abstract) Protected helper method to write subclass entity data to NBT.
//     */
//    @Override
//    protected void writeEntityToNBT(CompoundTag par1CompoundTag) {
//        super.writeEntityToNBT(par1CompoundTag);
//        par1CompoundTag.putDouble("PushX", pushX);
//        par1CompoundTag.putDouble("PushZ", pushZ);
//        par1CompoundTag.putShort("Fuel", (short) fuel);
//    }
//
//    /**
//     * (abstract) Protected helper method to read subclass entity data from NBT.
//     */
//    @Override
//    protected void readEntityFromNBT(CompoundTag par1CompoundTag) {
//        super.readEntityFromNBT(par1CompoundTag);
//        pushX = par1CompoundTag.getDouble("PushX");
//        pushZ = par1CompoundTag.getDouble("PushZ");
//        fuel = par1CompoundTag.getShort("Fuel");
//    }
//
//    protected boolean isMinecartPowered() {
//        return fuel > 0;
//    }
//
//    @Override
//    public void moveMinecartOnRail(BlockPos pos, double par4) {
//        double d12 = motionX;
//        double d13 = motionZ;
//
//        par4 *= 2;
//
//        if (this.isMinecartPowered()) {
//            motionX *= 1.1;
//            motionZ *= 1.1;
//        }
//
//        if (d12 < -par4)
//            d12 = -par4;
//
//        if (d12 > par4)
//            d12 = par4;
//
//        if (d13 < -par4)
//            d13 = -par4;
//
//        if (d13 > par4)
//            d13 = par4;
//
//        this.moveEntity(d12, 0.0D, d13);
//    }
//
//    @Override
//    public void applyEntityCollision(Entity par1Entity) {
//        NeoForge.EVENT_BUS.post(new MinecartCollisionEvent(this, par1Entity));
//        if (getCollisionHandler() != null) {
//            getCollisionHandler().onEntityCollision(this, par1Entity);
//            return;
//        }
//        if (!level.isClientSide()) {
//            double d0 = par1Entity.getX() - getX();
//            double d1 = par1Entity.getZ() - getZ();
//            if (par1Entity instanceof Minecart) {
//                double d4 = par1Entity.getX() - getX();
//                double d5 = par1Entity.getZ() - getZ();
//                Vec3 vec3 = Vec3.createVectorHelper(d4, 0.0D, d5).normalize();
//                Vec3 vec31 = Vec3.createVectorHelper(Mth.cos(rotationYaw * (float) Math.PI / 180.0F), 0.0D, Mth.sin(rotationYaw * (float) Math.PI / 180.0F)).normalize();
//                double d6 = Math.abs(vec3.dot(vec31));
//
//                if (d6 < 0.800000011920929D) {
//                    return;
//                }
//
//                double d7 = par1Entity.motionX + motionX;
//                double d8 = par1Entity.motionZ + motionZ;
//
//                if (((Minecart) par1Entity).isPoweredCart() && !this.isPoweredCart()) {
//                    par1Entity.motionX *= 0.949999988079071D;
//                    par1Entity.motionZ *= 0.949999988079071D;
//                } else if (!((Minecart) par1Entity).isPoweredCart() && this.isPoweredCart()) {
//                    par1Entity.motionX *= 0.20000000298023224D;
//                    par1Entity.motionZ *= 0.20000000298023224D;
//                    par1Entity.addVelocity(motionX + d0, 0.0D, motionZ + d1);
//                } else {
//                    d7 /= 2.0D;
//                    d8 /= 2.0D;
//                    par1Entity.motionX *= 0.20000000298023224D;
//                    par1Entity.motionZ *= 0.20000000298023224D;
//                    par1Entity.addVelocity(d7 + d0, 0.0D, d8 + d1);
//                }
//            }
//        }
//    }
//
//    @Override
//    public boolean isPoweredCart() {
//        return true;
//    }
//
//
//}
