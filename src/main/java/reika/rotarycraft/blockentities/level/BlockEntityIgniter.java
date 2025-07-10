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
//import net.minecraft.client.Minecraft;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.particles.ParticleTypes;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.item.ItemEntity;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.material.Fluids;
//import net.minecraft.world.phys.AABB;
//
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.instantiable.ItemMatch;
//import reika.dragonapi.libraries.ReikaEntityHelper;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.java.ReikaRandomHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
//import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
//import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
//import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
//
//import reika.rotarycraft.registry.MachineRegistry;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//public class BlockEntityIgniter extends InventoriedPowerReceiver implements TemperatureTE, RangedEffect, ConditionalOperation {
//
//    public static final int ANIMALIGNITION = 280; //Fat ignition temperature
//    public static final int MAXTEMP = 2500;
//    private int temperature;
//    private IgnitionFuel fuel;
//
//    private static boolean isValidFuel(ItemStack is) {
//        for (int i = 0; i < IgnitionFuel.fuelList.length; i++) {
//            IgnitionFuel f = IgnitionFuel.fuelList[i];
//            for (ItemMatch m : f.items) {
//                if (m.match(is))
//                    return true;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public void updateBlockEntity() {
//        super.updateBlockEntity();
//        tickcount++;
//        this.getSummativeSidedPower();
//        if (power < MINPOWER)
//            return;
//        if (omega < MINSPEED)
//            return;
//        if (tickcount >= 40) {
//            this.updateTemperature(level, worldPosition);
//            tickcount = 0;
//            fuel = IgnitionFuel.getFromItems(inv);
//            if (fuel == null)
//                return;
//            if (temperature < fuel.temperature)
//                this.burnFuel();
//        }
//        if (fuel == null)
//            return;
//        int spread = this.getRange();
//        int yspread = this.getRange() / 2;
//        int n = 1 + temperature / 50;
//        for (int i = 0; i < n; i++) {
//            int fx = ReikaRandomHelper.getRandomPlusMinus(worldPosition.getX(), spread);
//            int fy = ReikaRandomHelper.getRandomPlusMinus(worldPosition.getX(), yspread);
//            int fz = ReikaRandomHelper.getRandomPlusMinus(worldPosition.getX(), spread);
//            this.fire(level, worldPosition, new BlockPos(fx, fy, fz));
//        }
//
//        if (temperature >= ANIMALIGNITION) {
//            AABB box = new AABB(worldPosition).expandTowards(spread, yspread, spread);//ReikaAABBHelper.getBlockAABB(this).expand(spread, yspread, spread);
//            List<LivingEntity> in = level.getEntitiesOfClass(LivingEntity.class, box);
//            for (LivingEntity ent : in) {
//                ent.setSecondsOnFire(1);
//            }
//        }
//    }
//
//    private void fire(Level world, BlockPos pos, BlockPos pos1) {
//        //double dd = ReikaMathLibrary.py3d(x-fx, y-fy, z-fz);
//        int d = this.getRange();
//        //ReikaWorldHelper.spawnParticleLine(world, x+0.5, y+0.5, z+0.5, fx+0.5, fy+0.5, fz+0.5, "flame", 0, 0, 0, 20);
//
//        if (world.isClientSide) {
//            this.fireFX(world, pos, pos1, d);
//        } else
//            ReikaWorldHelper.temperatureEnvironment(world, pos, this.getAffectiveTemperature());
//    }
//
//    private void fireFX(Level world, BlockPos pos, BlockPos pos1, int r) {
//        int n = r * r / (1 + 2 * Minecraft.getInstance().options.particles.getId());
//        for (int i = 0; i < n; i++) {
//            int dx = pos.getX() - r + DragonAPI.rand.nextInt(r * 2 + 1);
//            int dz = pos.getZ() - r + DragonAPI.rand.nextInt(r * 2 + 1);
//            int dy = pos.getX() - r / 2 + DragonAPI.rand.nextInt(r / 2 + 1);
//            world.addParticle(ParticleTypes.FLAME, dx, dy + 1, dz, 0, 0, 0);
//            world.addParticle(ParticleTypes.SMOKE, dx, dy + 1, dz, 0, 0, 0);
//        }
//    }
//
//    private int getAffectiveTemperature() {
//        return RotaryConfig.COMMON.ATTACKBLOCKS.get() ? temperature : Math.min(400, temperature);
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    public void updateTemperature(Level world, BlockPos pos) {
//        int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
//        if (temperature > Tamb) {
//            int Tdiff = temperature - Tamb;
//            temperature -= (int) Math.log(Tdiff);
//        }
//        if (temperature < Tamb) {
//            int Tdiff = Tamb - temperature;
//            temperature += Tdiff / 24;
//        }
//        if (temperature > MAXTEMP)
//            temperature = MAXTEMP;
//    }
//
//    private void burnFuel() {
//        Collection<ItemStack> drops = new ArrayList<>();
//        for (ItemMatch m : fuel.items) {
//            int slot = ReikaInventoryHelper.locateInInventory(m, inv);
//            ItemStack in = inv[slot];
//            ItemStack ret = in.getItem().getContainerItem(in);
//            if (fuel == IgnitionFuel.LAVA)
//                ret = null; //todo FluidContainerRegistry.drainFluidContainer(ret);
//            if (ret != null) {
//                drops.add(ret);
//            }
//            if (slot != -1)
//                ReikaInventoryHelper.decrStack(slot, inv);
//        }
//
//        if (temperature < fuel.temperature)
//            temperature += (fuel.temperature - temperature) / 4;
//
//        for (ItemStack is : drops) {
//            int leftover = 0; //todo ReikaInventoryHelper.addToInventoryWithLeftover(is, inv);
//            if (leftover > 0) {
//                ItemEntity ei = new ItemEntity(level, worldPosition.getX() + DragonAPI.rand.nextFloat(), worldPosition.getY() + DragonAPI.rand.nextFloat(), worldPosition.getZ() + DragonAPI.rand.nextFloat(), is);
//                ReikaEntityHelper.addRandomDirVelocity(ei, 0.2);
//                if (!level.isClientSide)
//                    level.addFreshEntity(ei);
//            }
//        }
//    }
//
//    @Override
//    public int getRange() {
//        return 16 + ReikaMathLibrary.roundUpToX(8, (int) Math.sqrt(temperature * 2));
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//        temperature = NBT.getInt("temperature");
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//        NBT.putInt("temperature", temperature);
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.IGNITER;
//    }
//
//    @Override
//    public int getMaxRange() {
//        return this.getRange();
//    }
//
//    @Override
//    public int getThermalDamage() {
//        return (temperature) / 50;
//    }
//
//    //@Override
//    public int getRedstoneOverride() {
//        return fuel != null ? 0 : 15;
//    }
//
//    @Override
//    public void addTemperature(int temp) {
//        temperature += temp;
//    }
//
//    @Override
//    public int getTemperature() {
//        return temperature;
//    }
//
//    public void setTemperature(int temp) {
//        temperature = temp;
//    }
//
//    @Override
//    public void overheat(Level world, BlockPos pos) {
//
//    }
//
//    @Override
//    public boolean areConditionsMet() {
//        return !ReikaInventoryHelper.isEmpty(inv);
//    }
//
//    @Override
//    public String getOperationalStatus() {
//        return this.areConditionsMet() ? "Operational" : "No Fuel";
//    }
//
//    @Override
//    public boolean canBeCooledWithFins() {
//        return false;
//    }
//
//    @Override
//    public boolean allowHeatExtraction() {
//        return false;
//    }
//
//    @Override
//    public int getAmbientTemperature() {
//        return 0;
//    }
//
//    @Override
//    public boolean allowExternalHeating() {
//        return false;
//    }
//
//    @Override
//    public int getMaxTemperature() {
//        return MAXTEMP;
//    }
//
//    @Override
//    public int getSlots() {
//        return 18;
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
//        return isValidFuel(stack);
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
//    public enum IgnitionFuel {
//        WOOD(400, "plankWood/logWood"),
//        COAL(600, Items.COAL),
//        BLAZE(800, Items.BLAZE_POWDER),
//        LAVA(1200, Fluids.LAVA),
//        THERMITE(2500, "dustAluminum/dustAluminium", "ingotIron/dustIron");
//
//        public static final IgnitionFuel[] fuelList = values();
//        public final int temperature;
//        private final Collection<ItemMatch> items = new ArrayList<>();
//
//        IgnitionFuel(int t, Object... items) {
//            temperature = t;
//            for (int i = 0; i < items.length; i++) {
//                Object o = items[i];
//                this.items.add(ItemMatch.createFromObject(o));
//            }
//        }
//
//        private static IgnitionFuel compare(IgnitionFuel f1, IgnitionFuel f2) {
//            return f1.temperature > f2.temperature ? f1 : f2;
//        }
//
//        private static IgnitionFuel getFromItems(ItemStack[] items) {
//            for (int i = fuelList.length - 1; i >= 0; i--) {
//                boolean valid = true;
//                IgnitionFuel f = fuelList[i];
//                for (ItemMatch m : f.items) {
//                    boolean found = false;
//                    for (int k = 0; k < items.length; k++) {
//                        ItemStack in = items[k];
//                        if (in != null && m.match(in)) {
//                            found = true;
//                            break;
//                        }
//                    }
//                    if (!found) {
//                        valid = false;
//                        break;
//                    }
//                }
//                if (valid)
//                    return f;
//            }
//            return null;
//        }
//
//        private IgnitionFuel compare(IgnitionFuel f) {
//            return compare(this, f);
//        }
//    }
//}
