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

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraftforge.registries.ForgeRegistries;
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

public class ItemDebug extends ItemRotaryTool {

    public ItemDebug() {
        super(new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player ep, InteractionHand hand) {
        //ReikaChatHelper.clearChat();
        if (!ep.isShiftKeyDown()) {
            ReikaChatHelper.writeBlockAtCoords(world, ep.blockPosition());
            BlockEntity te = world.getBlockEntity(ep.blockPosition());
            if (te instanceof RotaryCraftBlockEntity)
                ReikaChatHelper.write("Tile Entity Direction Data: " + te + " of " + ((RotaryCraftBlockEntity) te).getMachine(world, ep.blockPosition()));//.getNumberDirections());
            else if (te instanceof BlockEntity)
                ReikaChatHelper.write("Tile Entity Direction Data: " + te);
            ReikaChatHelper.write("Additional Data (Meaning differs per machine):");
        }
        BlockEntity te = world.getBlockEntity(ep.blockPosition());
        if (ep.isCrouching() && te instanceof BlockEntitySpringPowered) {
            BlockEntitySpringPowered sp = (BlockEntitySpringPowered) te;
            sp.isCreative = !sp.isCreative;
            return InteractionResultHolder.pass(this.getDefaultInstance()); //todo check if this works
        }
        MachineRegistry m = MachineRegistry.getMachine(world, ep.blockPosition());
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
        if (world.getBlockState(ep.blockPosition()).getBlock() == Blocks.SPAWNER) {
            SpawnerBlockEntity tile = (SpawnerBlockEntity) te;
            if (tile != null) {
                BaseSpawner lgc = tile.getSpawner();
                CompoundTag tag = lgc.getSpawnerBlockEntity().getUpdateTag();
                tag.putInt("Delay", 0);
                lgc.getSpawnerBlockEntity().load(tag);
            }
        }
        if (m != null && m.isStandardPipe()) {
            BlockEntityPipe tile = (BlockEntityPipe) te;
            if (tile != null) {
                if (tile.getAttributes() != null)
                    ReikaChatHelper.write(String.format("%s  %d  %d", ForgeRegistries.FLUIDS.getKey(tile.getAttributes()).getNamespace(), tile.getFluidLevel(), tile.getPressure()));
                else
                    ReikaChatHelper.write("Pipe is empty.");
            }
        }
        if (m == MachineRegistry.PUMP) {
            BlockEntityPump tile = (BlockEntityPump) te;
            if (tile != null) {
                ReikaChatHelper.write(String.format("%s  %d", tile.getFluidLevel() <= 0 ? 0 : ForgeRegistries.FLUIDS.getKey(tile.getLiquid().getFluid()).getNamespace(), tile.getLevel()));
            }
        }
        if (m == MachineRegistry.RESERVOIR) {
            BlockEntityReservoir tile = (BlockEntityReservoir) te;
            if (ep.isShiftKeyDown())
                tile.isCreative = !tile.isCreative;
            else if (tile != null && !tile.isEmpty()) {
                ReikaChatHelper.write(String.format("%s  %d", ForgeRegistries.FLUIDS.getKey(tile.getFluid().getFluid()).getNamespace(), tile.getLevel()));
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
        if (m == MachineRegistry.ENGINE) {
            BlockEntityEngine tile = (BlockEntityEngine) te;
            if (tile != null) {
                ReikaChatHelper.write(String.format("%d  %d", tile.getWater(), BlockEntityEngine.temperature));
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
        if (m == MachineRegistry.SHAFT) {
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

        return InteractionResultHolder.pass(this.getDefaultInstance());
    }
}
