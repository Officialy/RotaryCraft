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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.gui.container.machine.SteamContainer;
import reika.rotarycraft.registry.*;

public class BlockEntitySteamEngine extends BlockEntityEngine {

    private int dryTicks = 0;

    public BlockEntitySteamEngine(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.STEAM_ENGINE.get(), pos, state);
        type = EngineType.STEAM;
    }

    @Override
    public boolean canConsumeFuel() {
        return water.getFluidLevel() > 0 && temperature >= 100;
    }

    @Override
    public int getMaxTemperature() {
        return 150;
    }

    @Override
    protected void consumeFuel() {
        water.removeLiquid(this.getConsumedFuel());
    }

    @Override
    protected void offlineCooldown(Level world, BlockPos pos, int Tamb) {

    }

    @Override
    protected int getConsumedFuel() {
        int amt = 10;
        if (temperature >= 130) {
            amt = 75;
        } else if (temperature >= 125) {
            amt = 60;
        } else if (temperature >= 120) {
            amt = 50;
        } else if (temperature >= 110) {
            amt = 25;
        }
        if (level.getBlockState(worldPosition.below()).getBlock() == Blocks.LAVA)
            amt *= 4;
        return amt;
    }

    @Override
    protected void internalizeFuel() {
        if (water.isEmpty() && temperature >= 100) {
            dryTicks++;
        } else {
            if (dryTicks > 900 && !water.isEmpty()) {
                level.setBlock(worldPosition, Blocks.AIR.defaultBlockState(), 1);
                level.explode(null, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, 6, Level.ExplosionInteraction.NONE); //todo explosion interaction
            }
            dryTicks = 0;
        }
    }

    @Override
    protected boolean getRequirements(Level world, BlockPos pos) {
        if (temperature < 100) //water boiling point
            return false;
        return !water.isEmpty();

//        RotaryAdvancements.STEAMENGINE.triggerAchievement(this.getPlacer());
    }

    @Override
    protected void playSounds(Level world, BlockPos pos, float pitchMultiplier, float volume) {
        soundtick++;
        if (this.isMuffled(world, pos)) {
            volume *= 0.3125F;
        }

        if (soundtick < this.getSoundLength(1F / pitchMultiplier) && soundtick < 2000)
            return;
        soundtick = 0;

        SoundRegistry.STEAM.playSoundAtBlock(world, pos, 0.7F * volume, 1F * pitchMultiplier);
    }

    @Override
    public void updateTemperature(Level world, BlockPos pos) {
        super.updateTemperature(world, pos);

        int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
        ResourceKey<Biome> biome = world.getBiomeManager().getBiome(pos).unwrapKey().get(); //todo check

        boolean fire = RotaryAux.isAboveFire(world, pos);
        boolean lava = RotaryAux.isAboveLava(world, pos);
        if (biome == Biomes.NETHER_WASTES)
            Tamb = 101;    //boils water, so 101C
        if (fire)
            temperature++;
        if (fire && biome == Biomes.NETHER_WASTES)
            temperature++; //Nether has 50% hotter fire
        if (lava) {
            temperature += 2;
        }
        if (Tamb < 0 && fire)
            Tamb += 30;
        if (temperature < Tamb)
            temperature += Math.max((Tamb - temperature) / 40, 1);
        if (!fire && !lava && temperature > Tamb)
            temperature--;
        if (temperature > Tamb) {
            temperature -= (temperature - Tamb) / 96;
        }
        if (temperature > this.getMaxTemperature())
            this.overheat(world, pos);
    }

    @Override
    public void overheat(Level world, BlockPos pos) {
        if (water.isEmpty()) {
            world.setBlock(pos, Fluids.FLOWING_LAVA.defaultFluidState().createLegacyBlock(), 1);
//            world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "DragonAPI.rand.fizz", 2, 1, true);
            return;
        }
        temperature = this.getMaxTemperature();
        ReikaWorldHelper.overheat(world, pos.getX(), pos.getY(), pos.getZ(), RotaryItems.HSLA_STEEL_SCRAP.get().getDefaultInstance(), 0, 17, false, 1F, false, true, 2F);
//        RotaryAdvancements.OVERPRESSURE.triggerAchievement(this.getPlacer());
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 1);
    }

    @Override
    public int getFuelLevel() {
        return water.getFluidLevel();
    }

    @Override
    protected void affectSurroundings(Level world, BlockPos pos) {

    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.STEAM_ENGINE.get();
    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);
        tag.putInt("dry", dryTicks);
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);
        dryTicks = tag.getInt("dry");
    }

    @Override
    protected String getTEName() {
        return "steamengine";
    }

    @Override
    public boolean canBeCooledWithFins() {
        return true;
    }

    @Override
    public boolean allowExternalHeating() {
        return true;
    }

    @Override
    public int getAmbientTemperature() {
        return 20;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getItem(int pIndex) {
        return null;
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        return null;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        return null;
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {

    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return false;
    }

    @Override
    public void clearContent() {

    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return true;
    }



    @Override
    public Component getDisplayName() {
        return Component.literal("Steam Engine");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        return new SteamContainer(p_39954_, p_39955_, this);
    }

    @Override
    public int fill(Direction from, FluidStack resource, IFluidHandler.FluidAction action) {
        return 0;
    }

    @Override
    public FluidStack drain(Direction from, int maxDrain, IFluidHandler.FluidAction doDrain) {
        return null;
    }
}
