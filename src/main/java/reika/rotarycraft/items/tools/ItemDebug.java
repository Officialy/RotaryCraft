/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import reika.dragonapi.libraries.io.ReikaChatHelper;
import reika.rotarycraft.base.ItemRotaryTool;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.base.blockentity.BlockEntitySpringPowered;
import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
import reika.rotarycraft.blockentities.engine.BlockEntityPerformanceEngine;
import reika.rotarycraft.blockentities.piping.BlockEntityHose;
import reika.rotarycraft.blockentities.piping.BlockEntityPipe;
import reika.rotarycraft.blockentities.production.BlockEntityObsidianMaker;
import reika.rotarycraft.blockentities.production.BlockEntityPump;
import reika.rotarycraft.blockentities.storage.BlockEntityReservoir;
import reika.rotarycraft.blockentities.transmission.BlockEntityBevelGear;
import reika.rotarycraft.blockentities.transmission.BlockEntityGearbox;
import reika.rotarycraft.blockentities.transmission.BlockEntityShaft;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryItems;

public class ItemDebug extends ItemRotaryTool {

    public ItemDebug() {
        super(RotaryItems.itemProperties());
    }

    // 26.2 fix: was use(Level,Player,Hand) acting on pos (the player's own feet), so it
    // never targeted the block being looked at — the reservoir creative toggle etc. silently did nothing.
    // useOn gives the clicked block via ctx.getClickedPos().
    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level world = ctx.getLevel();
        Player ep = ctx.getPlayer();
        if (ep == null)
            return InteractionResult.PASS;
        BlockPos pos = ctx.getClickedPos();
        {
            if (!ep.isShiftKeyDown()) {
                ReikaChatHelper.writeBlockAtCoords(world, pos);
                BlockEntity te = world.getBlockEntity(pos);
                if (te instanceof RotaryCraftBlockEntity)
                    ReikaChatHelper.write("Tile Entity Direction Data: " + te + " of " + ((RotaryCraftBlockEntity) te).getMachine(world, pos));//.getNumberDirections());
                else if (te instanceof BlockEntity)
                    ReikaChatHelper.write("Tile Entity Direction Data: " + te);
                ReikaChatHelper.write("Additional Data (Meaning differs per machine):");
            }
            BlockEntity te = world.getBlockEntity(pos);
            if (ep.isCrouching() && te instanceof BlockEntitySpringPowered sp) {
                sp.isCreative = !sp.isCreative;
                return InteractionResult.PASS; //todo check if this works
            }
            MachineRegistry m = MachineRegistry.getMachine(world, pos);
            if (m == MachineRegistry.BEVELGEARS) {
                BlockEntityBevelGear tile = (BlockEntityBevelGear) te;
                if (tile != null) {
                    ReikaChatHelper.write(String.format("%d", tile.direction));
                }
            }
//        if (m == MachineRegistry.BLASTFURNACE) {
//            BlockEntityBlastFurnace tile = (BlockEntityBlastFurnace) te;
//            if (tile != null) {
//                ReikaChatHelper.write(String.format("Temperature: %dC", tile.getTemperature()));
//                if (ep.isShiftKeyDown()) {
//                    tile.addTemperature(BlockEntityBlastFurnace.MAXTEMP - tile.getTemperature());
//                }
//            }
//        }
//        if (m == MachineRegistry.BELT) {
//            BlockEntityBeltHub tile = (BlockEntityBeltHub) te;
//            if (tile != null) {
//                ReikaChatHelper.write(tile.getDistanceToTarget() + " @ " + tile.getBeltDirection());
//            }
//        }
            if (m == MachineRegistry.HOSE) {
                BlockEntityHose tile = (BlockEntityHose) te;
                if (tile != null) {
                    ReikaChatHelper.write(String.format("%d", tile.getFluidLevel()));
                }
            }
            if (world.getBlockState(pos).getBlock() == Blocks.SPAWNER && te instanceof SpawnerBlockEntity tile) {
                CompoundTag spawnData = tile.saveCustomOnly(world.registryAccess()).getCompoundOrEmpty("SpawnData");
                String id = spawnData.getCompoundOrEmpty("entity").getString("id").orElse("unknown");
                ReikaChatHelper.write("Spawner spawns: " + id);
            }
            if (m != null && m.isStandardPipe()) {
                BlockEntityPipe tile = (BlockEntityPipe) te;
                if (tile != null) {
                    if (tile.getAttributes() != null)
                        ReikaChatHelper.write(String.format("%s  %d  %d", BuiltInRegistries.FLUID.getKey(tile.getAttributes()).getNamespace(), tile.getFluidLevel(), tile.getPressure()));
                    else
                        ReikaChatHelper.write("Pipe is empty.");
                }
            }
            if (m == MachineRegistry.PUMP) {
                BlockEntityPump tile = (BlockEntityPump) te;
                if (tile != null) {
                    ReikaChatHelper.write(String.format("%s  %d", tile.getFluidLevel() <= 0 ? 0 : BuiltInRegistries.FLUID.getKey(tile.getLiquid().getFluid()).getNamespace(), tile.getLevel()));
                }
            }
            if (m == MachineRegistry.RESERVOIR) {
                BlockEntityReservoir tile = (BlockEntityReservoir) te;
                if (ep.isShiftKeyDown())
                    tile.isCreative = !tile.isCreative;
                else if (tile != null && !tile.isEmpty()) {
                    ReikaChatHelper.write(String.format("%s  %d", BuiltInRegistries.FLUID.getKey(tile.getFluid().getFluid()).getNamespace(), tile.getLevel()));
                }
            }
//        if (m == MachineRegistry.EXTRACTOR) {
//            BlockEntityExtractor tile = (BlockEntityExtractor) te;
//            if (tile != null) {
//                ReikaChatHelper.write(String.format("%d", tile.getLevel()));
//            }
//        }
//        if (m == MachineRegistry.SPRINKLER) {
//            BlockEntitySprinkler tile = (BlockEntitySprinkler) te;
//            if (tile != null) {
//                ReikaChatHelper.write(String.format("%d  %d", tile.getWater(), tile.getPressure()));
//            }
//        }
            if (m == MachineRegistry.OBSIDIAN) {
                BlockEntityObsidianMaker tile = (BlockEntityObsidianMaker) te;
                if (tile != null) {
                    ReikaChatHelper.write(String.format("%d  %d  %d", tile.getWater(), tile.getLava(), tile.temperature));
                }
                if (ep.isShiftKeyDown()) {
                    tile.setLava(BlockEntityObsidianMaker.CAPACITY);
                    tile.setWater(BlockEntityObsidianMaker.CAPACITY);
                    ReikaChatHelper.write("Filled to capacity.");
                }
            }
//        if (m == MachineRegistry.PULSEJET) {
//            BlockEntityPulseFurnace tile = (BlockEntityPulseFurnace) te;
//            if (tile != null) {
//                ReikaChatHelper.write(String.format("%d  %d  %d", tile.getWater(), tile.temperature, tile.getFuel()));
//                if (ep.isShiftKeyDown()) {
//                    tile.addFuel(BlockEntityPulseFurnace.MAXFUEL);
//                    tile.addWater(BlockEntityPulseFurnace.CAPACITY);
//                    ReikaChatHelper.write("Filled to capacity.");
//                }
//            }
//        }
//        if (m == MachineRegistry.FRACTIONATOR) {
//            BlockEntityFractionator tile = (BlockEntityFractionator) te;
//            if (tile != null) {
//                ReikaChatHelper.write(String.format("%d", tile.getFuelLevel()));
//            }
//        }
//        if (m == MachineRegistry.FAN) {
//            BlockEntityFan tile = (BlockEntityFan) te;
//            if (tile != null) {
//                ReikaChatHelper.write(String.format("%s", tile.getFacing().toString()));
//            }
//        }
            if (m == MachineRegistry.WIND_ENGINE || m == MachineRegistry.STEAM_ENGINE || m == MachineRegistry.PERFORMANCE_ENGINE || m == MachineRegistry.MICRO_TURBINE || m == MachineRegistry.GAS_ENGINE || m == MachineRegistry.DC_ENGINE || m == MachineRegistry.AC_ENGINE) {
                BlockEntityEngine tile = (BlockEntityEngine) te;
                if (tile != null) {
                    ReikaChatHelper.write(String.format("%d  %d", tile.getWater(), tile.temperature));
                }
                if (ep.isShiftKeyDown()) {
                    tile.addFuel(BlockEntityEngine.FUELCAP);
                    if (tile instanceof BlockEntityPerformanceEngine)
                        ((BlockEntityPerformanceEngine) tile).additives = BlockEntityEngine.FUELCAP / 1000;
//                if (tile instanceof BlockEntityHydroEngine)
//                    tile.addLubricant(50000);
                    tile.addWater(BlockEntityEngine.CAPACITY);
                    ReikaChatHelper.write("Filled to capacity.");
                    tile.omega = tile.getEngineType().getSpeed();
                }
            }
            if (m == MachineRegistry.WOOD_SHAFT || m ==
                    MachineRegistry.STONE_SHAFT || m ==
                    MachineRegistry.HSLA_SHAFT || m ==
                    MachineRegistry.TUNGSTEN_SHAFT || m ==
                    MachineRegistry.DIAMOND_SHAFT || m ==
                    MachineRegistry.BEDROCK_SHAFT) {
                BlockEntityShaft tile = (BlockEntityShaft) te;
                if (tile != null) {
                    ReikaChatHelper.write(String.format("%d %d %d %d", tile.readomega[0], tile.readomega[1], tile.readtorque[0], tile.readtorque[1]));
                }
            }
            if (m == MachineRegistry.GEARBOX) {
                BlockEntityGearbox tile = (BlockEntityGearbox) te;
                if (ep.isShiftKeyDown()) {
                    tile.repair(Integer.MAX_VALUE);
                    tile.fillWithLubricant();
                    ReikaChatHelper.write("Filled to capacity.");
                }
            }
        }

        return InteractionResult.PASS;
    }
}
