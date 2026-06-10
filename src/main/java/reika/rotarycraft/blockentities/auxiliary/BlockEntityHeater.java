/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.auxiliary;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
// 1.21.5: net.neoforged.common.ForgeHooks no longer exists; usages stubbed.
import reika.dragonapi.DragonAPI;
import reika.dragonapi.libraries.ReikaEntityHelper;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.api.interfaces.ThermalMachine;
import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
import reika.rotarycraft.registry.DurationRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;

import java.util.List;

public class BlockEntityHeater extends InventoriedPowerReceiver implements TemperatureTE, DiscreteFunction {

    public static final int MAXTEMP = 2000;
    public int temperature;
    public int setTemperature;
    public boolean idle = false;
    private int tickcount2 = 0;

    public BlockEntityHeater(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.HEATER.get(), pos, state);
    }

    public void testIdle() {
        int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(level, worldPosition);
        if (setTemperature <= Tamb) {
            idle = true;
            return;
        }
        if (this.findHottestUsefulItemTemp(setTemperature, false) == -1) {
            idle = true;
            return;
        }
        idle = false;
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        tickcount++;
        tickcount2++;
        this.getPowerBelow();
        if (tickcount2 >= 20) {
            this.updateTemperature(world, pos);
            tickcount2 = 0;
        }
        if (power < MINPOWER)
            return;
        this.testIdle();
        if (tickcount >= this.getOperationTime()) {
            this.addHeat();
            tickcount = 0;
        }
        this.transferHeat(world, pos.above());
        if (temperature >= 240) {
            this.ignite(world, pos);
        }
    }

    private void ignite(Level world, BlockPos pos) {
        AABB box = new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1);
        List<LivingEntity> inbox = world.getEntitiesOfClass(LivingEntity.class, box);
        for (LivingEntity hot : inbox) {
            hot.igniteForSeconds(temperature / 50);
        }
    }

    public void updateTemperature(Level world, BlockPos pos) {
        int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
        if (temperature > Tamb)
            temperature -= Math.max((temperature - Tamb) / 200, 1);
        if (temperature < Tamb)
            temperature += Math.max((Tamb - temperature) / 40, 1);
    }

    private void addHeat() {
        int tempdiff = setTemperature - temperature;
        if (tempdiff <= 0)
            return;
        int deltaT = this.findHottestUsefulItemTemp(tempdiff, true);
        if (deltaT != -1)
            temperature += deltaT * 1.5;

        if (temperature >= MAXTEMP * 0.9) {
            RotaryCraft.LOGGER.warn("WARNING: " + this + " is reaching a very high temperature!");
        }

        if (temperature > MAXTEMP) {
            this.overheat(level, worldPosition);
            temperature = MAXTEMP;
        }
    }

    public void overheat(Level world, BlockPos pos) {
        //todo ReikaWorldHelper.overheat(world, pos, RotaryItems.HSLA_STEEL_SCRAP.copy(), 0, 17, true, 1.2F, true, true, 1F);
        world.setBlock(pos, Blocks.LAVA.defaultBlockState(), 3);
        temperature = 0;
        setTemperature = 0;
    }

    private int findHottestUsefulItemTemp(int maxT, boolean consume) {
        ItemStack item = ItemStack.EMPTY;
        int itemheat = -1;
        int slot = -1;
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                //ReikaChatHelper.writeInt(BlockEntityFurnace.getItemBurnTime(itemHandler.getStackInSlot(i)));
                // 1.21.5: ForgeHooks.getBurnTime was replaced by Level#fuelValues() + FuelValues#burnDuration.
                // We previously fell back to a constant of 4; keep that fallback when level/fuel-values aren't
                // available (e.g. during early load) but otherwise look up the real burn duration.
                int heat = level != null ? level.fuelValues().burnDuration(itemHandler.getStackInSlot(i)) : 4;
                if (heat <= 0) heat = 4;
                if (heat <= maxT && heat > itemheat) {
                    itemheat = heat;
                    item = itemHandler.getStackInSlot(i);
                    slot = i;
                }
            }
        }
        if (slot != -1 && consume) {
            Item id = itemHandler.getStackInSlot(slot).getItem();
            ReikaInventoryHelper.decrStack(slot, itemHandler);
            if (id == Items.LAVA_BUCKET) {
                int leftover = ReikaInventoryHelper.addToInventoryWithLeftover(Items.BUCKET, 1, itemHandler);
                if (leftover > 0) {
                    ItemEntity ei = new ItemEntity(level, worldPosition.getX() + DragonAPI.rand.nextFloat(), worldPosition.getY() + DragonAPI.rand.nextFloat(), worldPosition.getZ() + DragonAPI.rand.nextFloat(), new ItemStack(Items.LAVA_BUCKET, leftover));
                    ReikaEntityHelper.addRandomDirVelocity(ei, 0.2);
                    if (!level.isClientSide())
                        level.addFreshEntity(ei);
                }
            }
        }
        //ReikaChatHelper.writeInt(itemheat);
        return itemheat;
    }

    private void transferHeat(Level world, BlockPos pos) {
        if (!world.isClientSide())
            ReikaWorldHelper.temperatureEnvironment(world, worldPosition.below(), temperature);
        MachineRegistry id = MachineRegistry.getMachine(world, pos);
        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof ThermalMachine th) {
            int tempdiff = temperature - th.getTemperature();
            if (tempdiff <= 0)
                return;
            if (tempdiff > 100) {
                th.addTemperature(tempdiff / 16);
                //this.temperature -= tempdiff/16;
            } else if (tempdiff > 16) {
                th.addTemperature(tempdiff / 8);
                //this.temperature -= tempdiff/8;
            } else if (tempdiff > 8) {
                th.addTemperature(tempdiff / 4);
                //this.temperature -= tempdiff/4;
            } else {
                th.addTemperature(tempdiff);
                //this.temperature -= tempdiff;
            }
            if (th.getTemperature() > th.getMaxTemperature())
                th.onOverheat(world, pos);
        }
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);
        temperature = tag.getIntOr("temperature", 0);
        setTemperature = tag.getIntOr("stemp", 0);
    }

    @Override
    protected String getTEName() {
        return null;
    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);
        tag.putInt("temperature", temperature);
        tag.putInt("stemp", setTemperature);
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }


    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.HEATER;
    }

    //@Override
    public boolean isItemValidForSlot(int slot, ItemStack is) {
        //return FurnaceBlockEntity.getItemBurnTime(is) > 0;
        return true;
    }

    @Override
    public int getThermalDamage() {
        return 0; // Done in TE code itself
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    @Override
    public int getRedstoneOverride() {
        if (idle)
            return 15;
        return 0;
    }

    @Override
    public void addTemperature(int temp) {

    }

    @Override
    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temp) {
        temperature = temp;
    }

    @Override
    public int getOperationTime() {
        return DurationRegistry.HEATER.getOperationTime(omega);
    }

    @Override
    public boolean canBeCooledWithFins() {
        return false;
    }

    @Override
    public boolean allowHeatExtraction() {
        return true;
    }

    @Override
    public int getAmbientTemperature() {
        return 0;
    }

    @Override
    public boolean allowExternalHeating() {
        return false;
    }

    @Override
    public int getMaxTemperature() {
        return MAXTEMP;
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    @Override
    public int getContainerSize() {
        return 18;
    }

}
