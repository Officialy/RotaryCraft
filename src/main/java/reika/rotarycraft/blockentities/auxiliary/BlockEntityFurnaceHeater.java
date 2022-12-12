///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.auxiliary;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.util.Mth;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.FurnaceBlock;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
//import org.jetbrains.annotations.NotNull;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.api.Interfaces.ThermalMachine;
//import reika.rotarycraft.auxiliary.RotaryAux;
//import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
//import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
//import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
//
//import reika.rotarycraft.registry.DifficultyEffects;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.SoundRegistry;
//
//public class BlockEntityFurnaceHeater extends BlockEntityPowerReceiver implements TemperatureTE, ConditionalOperation {
//
//    public static final int MAXTEMP = 2000;
//
//    private FrictionRecipe activeRecipe;
//    private BlockPos furnaceLocation;
//
//    private int temperature;
//    private int smeltTime = 0;
//    private int soundtick = 0;
//
//    public static boolean isHijacked(FurnaceBlockEntity furn) {
//        for (int i = 2; i < 6; i++) {
//            Direction dir = Direction.values()[i];
//            int dx = furn.getBlockPos().getX() + dir.getStepX();
//            int dz = furn.getBlockPos().getZ() + dir.getStepZ();
//            MachineRegistry m = MachineRegistry.getMachine(furn.getLevel(), new BlockPos(dx, furn.getBlockPos().getY(), dz));
//            if (m == MachineRegistry.FRICTION) {
//                BlockEntityFurnaceHeater te = (BlockEntityFurnaceHeater) furn.getLevel().getBlockEntity(new BlockPos(dx, furn.getBlockPos().getY(), dz));
//                if (te.furnaceLocation != null && te.furnaceLocation.equals(furn)) {
//                    if (te.isActive())
//                        return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public void updateTemperature(Level world, BlockPos pos) {
//        if (torque >= MINTORQUE && power >= MINPOWER && omega > 0 && this.hasHeatableMachine(world)) {
//            temperature += 3 * ReikaMathLibrary.logbase(omega, 2) * ReikaMathLibrary.logbase(torque, 2);
//        }
//        int Tamb = power > MINPOWER && torque > MINTORQUE ? 30 : ReikaWorldHelper.getAmbientTemperatureAt(world, pos); //prevent nether exploit
//        if (temperature > Tamb) {
//            temperature -= (temperature - Tamb) / 5;
//        } else {
//            temperature += (temperature - Tamb) / 5;
//        }
//        if (temperature - Tamb <= 4 && temperature > Tamb)
//            temperature--;
//        if (temperature > MAXTEMP)
//            temperature = MAXTEMP;
//        if (temperature >= MAXTEMP)
//            if (!world.isClientSide && RotaryConfig.COMMON.BLOCKDAMAGE.get() && DragonAPI.rand.nextInt(DifficultyEffects.FURNACEMELT.getInt()) == 0)
//                this.meltFurnace(world);
//        if (temperature < Tamb)
//            temperature = Tamb;
//    }
//
//    private boolean hasHeatableMachine(Level world) {
//        if (furnaceLocation == null)
//            return false;
//        Block id = world.getBlockState(furnaceLocation).getBlock();
//        if (id == Blocks.AIR)
//            return false;
//        if (id == Blocks.FURNACE || id == Blocks.BLAST_FURNACE)
//            return true;
//
//        MachineRegistry m = MachineRegistry.getMachine(world, new BlockPos(furnaceLocation.getX(), furnaceLocation.getY(), furnaceLocation.getZ()));
//        if (m != null && m.canBeFrictionHeated())
//            return true;
//        BlockEntity te = world.getBlockEntity(furnaceLocation);
////        if (ModList.THAUMICTINKER.isLoaded()) {
////            BlockEntity relay = TransvectorHandler.getRelayedTile(te);
////            while (relay != te && relay != null) {
////                te = relay;
////                relay = TransvectorHandler.getRelayedTile(te);
////            }
////            te = relay;
////            if (te != null) {
////                furnaceLocation = new BlockPos(te);
////            }
////        }
////        if (ModList.TINKERER.isLoaded()) {
////            if (TinkerSmelteryHandler.isSmelteryController(te))
////                return true;
////        }
////        if (ModList.THAUMCRAFT.isLoaded()) {
////            if (ReikaThaumHelper.isAlchemicalFurnace(te))
////                return true;
////        }
//        return te instanceof ThermalMachine;
//    }
//
//    @Override
//    public int getThermalDamage() {
//        return temperature * 5 / 1200;
//    }
//
//    //@Override
//    protected void animateWithTick(Level world, BlockPos pos) {
//        if (!this.isInWorld()) {
//            phi = 0;
//            return;
//        }
//        if (power < MINPOWER)
//            return;
//        if (torque < MINTORQUE)
//            return;
//        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.FRICTION;
//    }
//
//    @Override
//    public void updateBlockEntity() {
//        super.updateBlockEntity();
//        activeRecipe = null;
//        tickcount++;
//        //this.getIOSidesDefault(level, worldPosition);
//        this.getPower(false);
//        this.getFurnaceCoordinates(level, worldPosition);
//        if (tickcount >= 20) {
//            tickcount = 0;
//            this.updateTemperature(level, worldPosition);
//        }
//        if (!this.isActive())
//            return;
//
//        if (this.hasFurnace()) {
//            this.hijackFurnace(level, worldPosition);
//        } else {
//            BlockEntity te = level.getBlockEntity(furnaceLocation);
//			/*
//			if (ModList.THAUMICTINKER.isLoaded()) {
//				BlockEntity relay = TransvectorHandler.getRelayedTile(te);
//				while (relay != te && relay != null) {
//					te = relay;
//					relay = TransvectorHandler.getRelayedTile(te);
//				}
//				te = relay;
//				if (te != null) {
//					fx = te.xCoord;
//					fy = te.yCoord;
//					fz = te.zCoord;
//				}
//			}
//			 */
//            if (te instanceof ThermalMachine) {
//                this.heatMachine(level, worldPosition, (ThermalMachine) te);
//            } //else if (ModList.TINKERER.isLoaded() && TinkerSmelteryHandler.isSmelteryController(te)) {
////                SmelteryWrapper s = new SmelteryWrapper(te);
////                s.fuelLevel = 4000;
////                s.meltPower = temperature * 25 / 6; //that allows the friction heater to break pyrotheum temperatures at its 1200C (~800kW)
////                s.write(te);
////            } else if (ModList.THAUMCRAFT.isLoaded() && ReikaThaumHelper.isAlchemicalFurnace(te)) {
////                ReikaThaumHelper.setAlchemicalBurnTime(te, 1 + temperature / 20);
////            }
//        }
//    }
//
//    public boolean isActive() {
//        return power >= MINPOWER && torque >= MINTORQUE;
//    }
//
//    private void heatMachine(Level world, BlockPos pos, ThermalMachine te) {
//        if (te.canBeFrictionHeated()) {
//            int tdiff = Math.min(te.getMaxTemperature(), temperature) - te.getTemperature();
//            if (tdiff > 0 || (tdiff == 0 && temperature == te.getMaxTemperature())) {
//                te.addTemperature(Math.max(1, (int) (tdiff * te.getMultiplier())));
//                te.resetAmbientTemperatureTimer();
//            }
//
//            if (te.getTemperature() > te.getMaxTemperature()) {
//                te.onOverheat(world, furnaceLocation);
//            }
//
//            soundtick++;
//            if (soundtick > 49) {
//                SoundRegistry.FRICTION.playSoundAtBlock(world, pos, RotaryAux.isMuffled(this) ? 0.1F : 0.5F, 1);
//                soundtick = 0;
//            }
//        }
//    }
//
//    private boolean canTileMake(FurnaceBlockEntity tile, ItemStack is) {
//        ItemStack out = tile.getStackInSlot(2);
//        if (out == null)
//            return true;
//        return ReikaItemHelper.matchStacks(is, out) && is.getCount() + out.getCount() <= is.getMaxStackSize();
//    }
//
//    private void hijackFurnace(Level world, BlockPos pos) {
//        BlockEntity te = world.getBlockEntity(furnaceLocation);
//        FurnaceBlockEntity tile = (FurnaceBlockEntity) te;
//        boolean flag = tile.furnaceBurnTime > 0;
//        int burn = Math.max(this.getBurnTimeFromTemperature(), tile.furnaceBurnTime);
//        tile.currentItemBurnTime = burn;
//        tile.furnaceBurnTime = burn;
//        if (burn > 0 && !flag) {
//            FurnaceBlock.updateFurnaceBlockState(true, world, furnaceLocation);
//        }
//        ItemStack in = tile.getItem(0);
//        int fx = furnaceLocation.getX();
//        int fy = furnaceLocation.getY();
//        int fz = furnaceLocation.getZ();
//        if (in != null) {
//            ItemStack out = tile.getStackInSlot(2);
//            ItemStack smelt = FurnaceRecipes.smelting().getSmeltingResult(in);
//            FrictionRecipe special = RecipesFrictionHeater.getRecipes().getSmelting(in, temperature);
//            if (special != null && !this.canTileMake(tile, special.getOutput()))
//                special = null;
//            if (smelt != null || special != null) {
//                activeRecipe = special;
//                int factor = this.getSpeedFactorFromTemperature();
//                if (special != null)
//                    factor *= this.getAccelerationFactor(special);
//                smeltTime += 1 + factor;
//                int max = special != null ? special.duration : 200;
//                tile.furnaceCookTime = Math.min(smeltTime, max - 5) * 200 / max;
//                if (smeltTime >= max) {
//                    int xp = 0;
//                    if (smelt != null && tile.canSmelt()) {
//                        tile.smeltItem();
//                        xp = Mth.ceil(FurnaceRecipes.smelting().func_151398_b(smelt));
//                    } else if (special != null) {
//                        ItemStack out2 = special.getOutput();
//                        ReikaInventoryHelper.decrStack(0, tile, 1);
//                        int amt = out != null ? out.getCount() + out2.getCount() : out2.getCount();
//                        out = ReikaItemHelper.getSizedItemStack(out2, amt);
//                        tile.setInventorySlotContents(2, out);
//                        xp = 1;
//                    }
//                    if (xp > 0 && RotaryConfig.COMMON.FRICTIONXP.getState()) {
//                        ReikaWorldHelper.splitAndSpawnXP(world, fx + 0.5, fy + 0.6, fz + 0.5, xp, 600);
//                    }
//                    smeltTime = 0;
//                }
//            } else {
//                tile.furnaceCookTime = 0;
//            }
//        } else {
//            tile.furnaceCookTime = 0;
//        }
//        //ReikaJavaLibrary.pConsole(smeltTime+" , "+tile.furnaceCookTime);
//        soundtick++;
//        if (soundtick > 49) {
//            SoundRegistry.FRICTION.playSoundAtBlock(world, pos, RotaryAux.isMuffled(this) ? 0.1F : 0.5F, 1);
//            soundtick = 0;
//        }
//        // world.playLocalSound(x+0.5, y+0.5, z+0.5, "dig.gravel", 1F, 2F);
//        switch (meta) {
//            case 0:
//                world.addParticle("crit", x, fy + DragonAPI.rand.nextDouble(), fz + DragonAPI.rand.nextDouble(), -0.2 + 0.4 * DragonAPI.rand.nextDouble(), 0.4 * DragonAPI.rand.nextDouble(), -0.2 + 0.4 * DragonAPI.rand.nextDouble());
//                break;
//            case 1:
//                world.addParticle("crit", x + 1, fy + DragonAPI.rand.nextDouble(), fz + DragonAPI.rand.nextDouble(), -0.2 + 0.4 * DragonAPI.rand.nextDouble(), 0.4 * DragonAPI.rand.nextDouble(), -0.2 + 0.4 * DragonAPI.rand.nextDouble());
//                break;
//            case 2:
//                world.addParticle("crit", fx + DragonAPI.rand.nextDouble(), fy + DragonAPI.rand.nextDouble(), z, -0.2 + 0.4 * DragonAPI.rand.nextDouble(), 0.4 * DragonAPI.rand.nextDouble(), -0.2 + 0.4 * DragonAPI.rand.nextDouble());
//                break;
//            case 3:
//                world.addParticle("crit", fx + DragonAPI.rand.nextDouble(), fy + DragonAPI.rand.nextDouble(), z + 1, -0.2 + 0.4 * DragonAPI.rand.nextDouble(), 0.4 * DragonAPI.rand.nextDouble(), -0.2 + 0.4 * DragonAPI.rand.nextDouble());
//                break;
//        }
//    }
//
//    private float getAccelerationFactor(FrictionRecipe rec) {
//        float fac = temperature / (float) rec.requiredTemperature;
//        return Math.min(1, (fac * fac) - 1);
//    }
//
//    private void getFurnaceCoordinates(Level world, BlockPos pos) {
//        furnaceLocation = new BlockPos(this).offset(this.getReadDirection().getOpposite(), 1);
//    }
//
//    private void meltFurnace(Level world) {
//        Block id = furnaceLocation.getBlock(world);
//        if (id != Blocks.furnace && id != Blocks.lit_furnace)
//            return;
//        world.explode(null, furnaceLocation.xCoord + 0.5, furnaceLocation.yCoord + 0.5, furnaceLocation.zCoord + 0.5, 1F, false);
//        //world.setBlock(fx, fy, fz, Blocks.flowing_lava.blockID);
//        furnaceLocation.setBlock(world, Blocks.AIR);
//        //ItemStack cobb = new ItemStack(Blocks.cobblestone);
//        //for (int i = 0; i < 8; i++)
//        //	ReikaItemHelper.dropItem(world, fx+par5Random.nextDouble(), fy+par5Random.nextDouble(), fz+par5Random.nextDouble(), cobb);
//    }
//
//    public boolean hasFurnace() {
//        return furnaceLocation != null && furnaceLocation.getBlockEntity(level) instanceof BlockEntityFurnace;
//    }
//
//    private int getBurnTimeFromTemperature() {
//        if (temperature < 300)
//            return 0;
//        return (temperature - 300) * 2;
//    }
//
//    private int getSpeedFactorFromTemperature() {
//        if (temperature < 500)
//            return 1;
//        if (temperature == 2000)
//            return 2000;
//        return 1 + (int) Math.sqrt((Math.pow(2, ((temperature - 500) / 100F))));
//    }
//
//    public FrictionRecipe getActiveRecipe() {
//        return activeRecipe;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    //@Override
//    public int getRedstoneOverride() {
//        return temperature / 100;
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//        NBT.putInt("temp", temperature);
//
//        if (furnaceLocation != null)
//            furnaceLocation.saveAdditional("furnLoc", NBT);
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//        temperature = NBT.getInt("temp");
//
//        if (NBT.contains("furnLoc"))
//            furnaceLocation = BlockPos.load("furnLoc", NBT);
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
//    public void onEMP() {
//    }
//
//    @Override
//    public boolean areConditionsMet() {
//        return this.hasHeatableMachine(level);
//    }
//
//    @Override
//    public String getOperationalStatus() {
//        return this.areConditionsMet() ? "Operational" : "No Heatable Machine";
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
//        return 0;
//    }
//
//    @NotNull
//    @Override
//    public ItemStack getStackInSlot(int slot) {
//        return null;
//    }
//
//    @NotNull
//    @Override
//    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
//        return null;
//    }
//
//    @NotNull
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
//    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
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
