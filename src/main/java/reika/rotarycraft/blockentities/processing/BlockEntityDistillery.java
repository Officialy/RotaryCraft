/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.processing;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import reika.rotarycraft.auxiliary.recipemanagers.DistilleryRecipe;
import reika.rotarycraft.base.blockentity.PoweredLiquidIO;
import reika.rotarycraft.modinterface.jei.RotaryRecipeSync;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryRecipeTypes;

/** Bottom-powered distiller: 6000mB in each tank, one complete fluid conversion every six ticks. */
public class BlockEntityDistillery extends PoweredLiquidIO {
    private int conversionTicks;

    public BlockEntityDistillery(BlockPos pos, BlockState state) { super(RotaryBlockEntities.DISTILLER.get(), pos, state); }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        if (world.isClientSide()) return;
        getPowerBelow();
        if (++conversionTicks >= 6) {
            conversionTicks = 0;
            DistilleryRecipe recipe = getConversion();
            if (recipe != null && power >= recipe.minPower() && torque >= recipe.minTorque()
                    && input.getFluidLevel() >= recipe.inputAmount()
                    && output.canTakeIn(recipe.output().value(), recipe.outputAmount())) {
                input.removeLiquid(recipe.inputAmount());
                output.addLiquid(recipe.outputAmount(), recipe.output().value());
                setChanged();
                syncAllData(false);
            }
        }
    }

    @Override protected void animateWithTick(Level world, BlockPos pos) {
        // The original distiller model has no moving parts.
    }

    public static String getValidConversions(Level level) {
        var recipes = RotaryRecipeSync.getRecipes(level);
        if (recipes == null) return "No world recipe data loaded.";
        StringBuilder text = new StringBuilder();
        for (var holder : recipes.byType(RotaryRecipeTypes.DISTILLER.get())) {
            var recipe = holder.value();
            var inputs = net.minecraft.core.registries.BuiltInRegistries.FLUID.listElements()
                    .filter(fluid -> fluid.is(recipe.input())).toList();
            for (var fluid : inputs) {
                if (!text.isEmpty()) text.append("\n");
                text.append(fluid.getKey().identifier()).append(" (").append(recipe.inputAmount()).append(" mB) + [")
                        .append(recipe.minTorque()).append(" Nm & ").append(recipe.minPower()).append(" W] -> ")
                        .append(net.minecraft.core.registries.BuiltInRegistries.FLUID.getKey(recipe.output().value()))
                        .append(" (").append(recipe.outputAmount()).append(" mB)");
            }
        }
        return text.isEmpty() ? "No compatible oil, bioethanol or biofuel fluids loaded." : text.toString();
    }

    public DistilleryRecipe getConversion() {
        var recipes = RotaryRecipeSync.getRecipes(level);
        if (recipes == null || input.isEmpty()) return null;
        return recipes.byType(RotaryRecipeTypes.DISTILLER.get()).stream().map(holder -> holder.value())
                .filter(recipe -> recipe.accepts(input.getActualFluid().getFluid())).findFirst().orElse(null);
    }

    @Override
    public boolean isValidFluid(Fluid fluid) {
        var recipes = RotaryRecipeSync.getRecipes(level);
        return recipes != null && recipes.byType(RotaryRecipeTypes.DISTILLER.get()).stream()
                .anyMatch(holder -> holder.value().accepts(fluid));
    }

    @Override public Fluid getInputFluid() { return input.isEmpty() ? Fluids.EMPTY : input.getActualFluid().getFluid(); }
    @Override public boolean canConnectToPipe(MachineRegistry pipe) { return pipe.isStandardPipe() || pipe == MachineRegistry.HOSE || pipe == MachineRegistry.FUELLINE; }
    @Override public boolean canIntakeFromPipe(MachineRegistry pipe) { return canConnectToPipe(pipe); }
    @Override public boolean canOutputToPipe(MachineRegistry pipe) { return canConnectToPipe(pipe); }
    @Override public boolean canOutputTo(Direction side) { return side == Direction.UP; }
    @Override public boolean canReceiveFrom(Direction side) { return side != Direction.UP; }
    @Override public boolean hasATank() { return true; }
    @Override public boolean hasAnInventory() { return false; }
    @Override public int getCapacity() { return 6000; }
    @Override protected String getTEName() { return "Distiller"; }
    @Override public net.minecraft.world.level.block.Block getBlockEntityBlockID() { return reika.rotarycraft.registry.RotaryBlocks.DISTILLER.get(); }
    @Override public MachineRegistry getMachine() { return MachineRegistry.DISTILLER; }
    @Override public boolean hasModelTransparency() { return false; }
    @Override public int getRedstoneOverride() { return 0; }

    @Override protected void writeSyncTag(CompoundTag tag) { super.writeSyncTag(tag); tag.putInt("conversionTicks", conversionTicks); }
    @Override protected void readSyncTag(CompoundTag tag) { super.readSyncTag(tag); conversionTicks = Math.clamp(tag.getIntOr("conversionTicks", 0), 0, 5); }
}
