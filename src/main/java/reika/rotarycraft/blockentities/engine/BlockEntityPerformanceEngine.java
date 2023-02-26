/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.engine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.gui.container.machine.inventory.PerformanceContainer;
import reika.rotarycraft.registry.*;

public class BlockEntityPerformanceEngine extends BlockEntityEngine {

    /**
     * Used in combustion power
     */
    public int additives;
    private boolean starvedengine;

    public BlockEntityPerformanceEngine(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.PERFORMANCE_ENGINE.get(), pos, state, false, false, true, true);
        type = EngineType.SPORT;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.PERFORMANCE_ENGINE;
    }

    @Override
    public int getMaxTemperature() {
        return 240;
    }

    @Override
    protected void consumeFuel() {
        fuel.removeLiquid(this.getConsumedFuel());
        if (DragonAPI.rand.nextInt(30) == 0)
            if (additives > 0)
                additives--;
    }

    @Override
    protected void internalizeFuel() {
        if (itemHandler.getStackInSlot(0) != ItemStack.EMPTY && fuel.getFluidLevel() + FluidType.BUCKET_VOLUME < FUELCAP) {
            if (itemHandler.getStackInSlot(0).getItem() == RotaryItems.ETHANOL.get()) {
                ReikaInventoryHelper.decrStack(0, itemHandler);
                fuel.addLiquid(1000, RotaryFluids.ETHANOL.get());
            }
        }
        if (itemHandler.getStackInSlot(1) != null && additives < FUELCAP / FluidType.BUCKET_VOLUME) { //additives
            Item id = itemHandler.getStackInSlot(1).getItem();
            if (id == Items.BLAZE_POWDER || id == Items.REDSTONE || id == Items.GUNPOWDER) {
                ReikaInventoryHelper.decrStack(1, itemHandler);
                if (id == Items.REDSTONE)
                    additives += 1;
                if (id == Items.GUNPOWDER)
                    additives += 2;
                if (id == Items.BLAZE_POWDER)
                    additives += 4;
            }
        }
    }

    @Override
    protected boolean getRequirements(Level world, BlockPos pos) {
        if (fuel.isEmpty())
            return false;
        starvedengine = additives <= 0;
        return true;
    }

    @Override
    protected void playSounds(Level world, BlockPos pos, float pitchMultiplier, float volume) {
        soundTick++;
        if (this.isMuffled(world, pos)) {
            volume *= 0.3125F;
        }

        if (soundTick < this.getSoundLength(1F / pitchMultiplier) && soundTick < 2000)
            return;
        soundTick = 0;

        SoundRegistry.CAR.playSoundAtBlock(world, pos, 0.33F * volume, 0.9F * pitchMultiplier);
    }

    @Override
    protected void offlineCooldown(Level world, BlockPos pos, int Tamb) {
        if (temperature > Tamb + 300)
            temperature -= (temperature - Tamb) / 100;
        else if (temperature > Tamb + 100)
            temperature -= (temperature - Tamb) / 50;
        else if (temperature > Tamb + 40)
            temperature -= (temperature - Tamb) / 10;
        else if (temperature > Tamb + 4)
            temperature -= (temperature - Tamb) / 2;
        else
            temperature = Tamb;
    }

    @Override
    public void updateTemperature(Level world, BlockPos pos) {
        super.updateTemperature(world, pos);

        int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
        if (temperature < Tamb)
            temperature += Math.max((Tamb - temperature) / 40, 1);
        if (omega > 0 && torque > 0) { //If engine is on
            temperature += 1;
            if (water.getFluidLevel() > 0 && temperature > Tamb) {
                water.removeLiquid(20);
                temperature--;
            }
            if (temperature > this.getMaxTemperature() / 2) {
                if (DragonAPI.rand.nextInt(10) == 0) {
                    world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0, 0);
//                    world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "DragonAPI.rand.fizz", 1F, 1F);
                }
            }
            if (temperature > this.getMaxTemperature() / 1.25) {
                if (DragonAPI.rand.nextInt(3) == 0) {
//                    world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "DragonAPI.rand.fizz", 1F, 1F);
                }
                world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.0, pos.getY() + 1.0625, pos.getZ() + 0.5, 0, 0, 0);
                world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 1.0625, pos.getZ() + 0.5, 0, 0, 0);
                world.addParticle(ParticleTypes.SMOKE, pos.getX() + 1, pos.getY() + 1.0625, pos.getZ() + 0.5, 0, 0, 0);
                world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.0, pos.getY() + 1.0625, pos.getZ() + 0, 0, 0, 0);
                world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.0, pos.getY() + 1.0625, pos.getZ() + 1, 0, 0, 0);
                world.addParticle(ParticleTypes.SMOKE, pos.getX() + 1, pos.getY() + 1.0625, pos.getZ() + 0, 0, 0, 0);
                world.addParticle(ParticleTypes.SMOKE, pos.getX() + 1, pos.getY() + 1.0625, pos.getZ() + 1, 0, 0, 0);
            }
        }
        if (temperature > this.getMaxTemperature()) {
            this.overheat(world, pos);
        }
    }

    @Override
    public void overheat(Level world, BlockPos pos) {
        temperature = this.getMaxTemperature();
        ReikaWorldHelper.overheat(world, pos.getX(), pos.getY(), pos.getZ(), RotaryItems.HSLA_STEEL_SCRAP.get().getDefaultInstance(), 0, 27, true, 1.5F, true, ConfigRegistry.BLOCKDAMAGE.getState(), 6F);
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 1);
    }

    @Override
    public int getFuelLevel() {
        return fuel.getFluidLevel();
    }

    public int getAdditivesScaled(int par1) {
        return (additives * par1 * 1000) / FUELCAP;
    }

    @Override
    protected int getMaxSpeed(Level world, BlockPos pos) {
        return starvedengine ? EngineType.GAS.getSpeed() : EngineType.SPORT.getSpeed();
    }

    @Override
    protected int getGenTorque(Level world, BlockPos pos) {
        return starvedengine ? EngineType.GAS.getTorque() : EngineType.SPORT.getTorque();
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);

        if (type.usesAdditives())
            tag.putInt("additive", additives);
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);

        if (type.usesAdditives())
            additives = tag.getInt("additive");
    }

    @Override
    protected String getTEName() {
        return "performanceengine";
    }

    @Override
    protected void affectSurroundings(Level world, BlockPos pos) {

    }

    @Override
    public boolean allowHeatExtraction() {
        return false;
    }

    @Override
    public int getAmbientTemperature() {
        return 0;
    }

    @Override
    public boolean canBeCooledWithFins() {
        return false;
    }

    @Override
    public boolean hasAnInventory() {
        return true;
    }

    @Override
    public boolean hasATank() {
        return true;
    }

    @Override
    public int fillPipe(Direction from, FluidStack resource, IFluidHandler.FluidAction action) {
        return 0;
    }

    @Override
    public FluidStack drainPipe(Direction from, int maxDrain, IFluidHandler.FluidAction doDrain) {
        return null;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Performance Engine");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        return new PerformanceContainer(p_39954_, p_39955_, this);
    }
}
