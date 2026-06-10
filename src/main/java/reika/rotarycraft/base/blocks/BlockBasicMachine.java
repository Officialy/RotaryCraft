/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.base.blocks;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.lwjgl.glfw.GLFW;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;


import reika.dragonapi.interfaces.blockentity.AdjacentUpdateWatcher;
import reika.dragonapi.interfaces.blockentity.PlaceNotification;
import reika.dragonapi.libraries.ReikaEntityHelper;
import reika.dragonapi.libraries.io.ReikaChatHelper;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.dragonapi.libraries.io.ReikaSoundHelper;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaEngLibrary;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.dragonapi.modinteract.ReikaXPFluidHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.auxiliary.interfaces.CachedConnection;
import reika.rotarycraft.auxiliary.interfaces.EnchantableMachine;
import reika.rotarycraft.auxiliary.interfaces.NBTMachine;
import reika.rotarycraft.auxiliary.interfaces.PressureTE;
import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityMirror;
import reika.rotarycraft.blockentities.decorative.BlockEntityMusicBox;
import reika.rotarycraft.blockentities.level.BlockEntityFloodlight;
import reika.rotarycraft.blockentities.processing.BlockEntityLavaSmeltery;
import reika.rotarycraft.blockentities.production.BlockEntityObsidianMaker;
import reika.rotarycraft.blockentities.storage.BlockEntityReservoir;
import reika.rotarycraft.blockentities.surveying.BlockEntityCaveFinder;
import reika.rotarycraft.blockentities.transmission.BlockEntityAdvancedGear;
import reika.rotarycraft.blockentities.transmission.BlockEntitySplitter;
import reika.rotarycraft.registry.*;

import java.util.List;
import net.minecraft.world.MenuProvider;

public abstract class BlockBasicMachine extends BlockRotaryCraftMachine {

    public BlockBasicMachine(BlockBehaviour.Properties properties) {
        super(properties.strength(4, 15));
    }

    /**
     * Opt-in for blocks that are drawn entirely by their {@link BlockEntity} renderer (TESR /
     * BlockEntityRenderer). When this returns {@code true}, the block's static blockstate /
     * model is suppressed via {@link #getRenderShape(BlockState)} returning {@code INVISIBLE},
     * so the missing-texture purple/black cube no longer z-fights the animated TESR draw.
     *
     * <p>Defaults to {@code false} so blocks with a real static model (blast furnace chassis,
     * worktable, etc.) keep rendering normally. Override and return {@code true} on each block
     * whose entire visible body comes from a {@code BlockEntityRenderer} entry in
     * {@code RotaryModelLayers#registerEntityRenderers}.
     */
    protected boolean isCustomRendered() {
        return false;
    }

    @Override
    protected net.minecraft.world.level.block.RenderShape getRenderShape(BlockState state) {
        return isCustomRendered()
                ? net.minecraft.world.level.block.RenderShape.INVISIBLE
                : super.getRenderShape(state);
    }

    /**
     * 26.1 helper: shared client-side ticker used by transmission/engine blocks to drive the
     * rotation animation. {@code phi} (the shaft angle) is server-only and not part of the
     * sync packet — too expensive to ship every tick — but {@code omega} IS synced via the
     * IOMachine sync tag. This ticker mirrors the server's {@code animateWithTick} step so
     * shafts visibly spin on the client whenever the BE has power.
     *
     * <p>Callers pass the BE's runtime class so the lambda's cast site is monomorphic.</p>
     */
    /** 26.1 DEBUG: temporary diagnostic for pump non-animation. Set to {@code true} to log the
     *  observed client-side omega for each ticker invocation (gated to once/sec to keep the log
     *  readable). Flip off once we've confirmed the sync path. */
    private static final boolean DEBUG_CLIENT_PHI_TICKER = true;

    protected static <E extends reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity>
            net.minecraft.world.level.block.entity.BlockEntityTicker<E> clientPhiTicker(Class<E> beClass) {
        return (lvl, pos, st, be) -> {
            if (!beClass.isInstance(be)) return;
            int omega = 0;
            if (be instanceof reika.rotarycraft.base.blockentity.BlockEntityIOMachine io) omega = io.omega;
            else if (be instanceof reika.rotarycraft.base.blockentity.BlockEntityEngine en) omega = en.omega;
            if (DEBUG_CLIENT_PHI_TICKER && lvl.getGameTime() % 20 == 0) {
                reika.rotarycraft.RotaryCraft.LOGGER.info(
                        "[clientPhiTicker] " + beClass.getSimpleName() + " @ " + pos
                                + " omega=" + omega
                                + " phi=" + ((reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity) be).phi);
            }
            if (omega <= 0) return;
            E typed = beClass.cast(be);
            typed.phi += (float) reika.dragonapi.libraries.mathsci.ReikaMathLibrary
                    .doubpow(reika.dragonapi.libraries.mathsci.ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
        };
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState pState,  LivingEntity e, ItemStack pStack) {
        super.setPlacedBy(world, pos, pState, e, pStack);
        RotaryCraftBlockEntity te = (RotaryCraftBlockEntity) world.getBlockEntity(pos);
        if (e instanceof Player ep && te != null) {
            te.setPlacer(ep);
        }
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState p_60569_, boolean p_60570_) {
        super.onPlace(state, world, pos, p_60569_, p_60570_);

        RotaryCraftBlockEntity te = (RotaryCraftBlockEntity) world.getBlockEntity(pos);

        if (te instanceof TemperatureTE) {
            int Tb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
            ((TemperatureTE) te).setTemperature(Tb);
        }
        if (te instanceof PressureTE) {
            ((PressureTE) te).addPressure(101);
        }
        if (te instanceof PlaceNotification)
            ((PlaceNotification) te).onPlaced();
        if (te instanceof BlockEntityEngine engine) {
            engine.temperature = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block pBlock, @Nullable Orientation pFromOrientation, boolean pIsMoving) {
        long _ncT0 = System.nanoTime();
        reika.rotarycraft.auxiliary.PipeDebugLog.event("BBM.neighborChanged.call");
        super.neighborChanged(state, world, pos, pBlock, pFromOrientation, pIsMoving);
        MachineRegistry m = MachineRegistry.getMachine(world, pos);
        if (m != null) {
            BlockEntity te = world.getBlockEntity(pos);
            if (m.cachesConnections()) {
                CachedConnection tc = (CachedConnection) te;
                reika.rotarycraft.auxiliary.PipeDebugLog.event("BBM.neighborChanged.recompute_path");
                tc.recomputeConnections(world, pos);
            }
            if (m == MachineRegistry.SMOKEDETECTOR) {
                // Smoke detector hangs from a ceiling — drop it as an item if the block above
                // is no longer solid enough to support it. Legacy 1.7 used `isOpaqueCube`;
                // 26.1 equivalent is `isCollisionShapeFullBlock`. Previously this branch had a
                // commented-out `else if` with stray bracket-mismatched code that destroyed the
                // smoke detector on EVERY neighborChanged regardless of the support state, so a
                // placed smoke detector self-destructed the first time any neighbour changed.
                BlockState above = world.getBlockState(pos.above());
                boolean supported = !above.isAir() && above.isCollisionShapeFullBlock(world, pos.above());
                if (!supported) {
                    world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    if (!world.isClientSide()) {
                        ItemStack is = MachineRegistry.SMOKEDETECTOR.getBlockState().getBlock().asItem().getDefaultInstance();
                        world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), is));
                    }
                }
            }
            if (te instanceof AdjacentUpdateWatcher) {
                ((AdjacentUpdateWatcher) te).onAdjacentUpdate(world, pos, state.getBlock());
            }
           /* if (m.hasTemperature()) {
                TemperatureTE tr = (TemperatureTE) te;
                int temp = Math.min(tr.getTemperature(), 800);
                //ReikaWorldHelper.temperatureEnvironment(world, x, y, z, temp);
            }*/
        }
        long _ncDt = System.nanoTime() - _ncT0;
        if (_ncDt > 5_000_000L) {
            reika.rotarycraft.auxiliary.PipeDebugLog.event("BBM.neighborChanged.slow_ms_" + (_ncDt / 1_000_000L));
        }
        // 26.1: roll the stats dump from here — neighborChanged fires from genuine world
        // events (place / break / connect) rather than every-tick, so the log-IO cost won't
        // taint the per-tick measurements.
        reika.rotarycraft.auxiliary.PipeDebugLog.maybeDumpStats();
    }

    /**
     * @param state The current state
     * @param level The current level
     * @param pos   Block position in level
     * @param ep    The player that right-clicked the block
     * @param pHand The hand that was used
     * @param pHit  The side the player hit the block on
     * @return
     */
    @Override
    protected InteractionResult useItemOn(ItemStack pStack, BlockState state, Level level, BlockPos pos, Player ep, InteractionHand pHand, BlockHitResult pHit) {
        super.useItemOn(pStack, state, level, pos, ep, pHand, pHit);
        RotaryCraftBlockEntity te = (RotaryCraftBlockEntity) level.getBlockEntity(pos);
        ItemStack is = pStack;
        MachineRegistry m = MachineRegistry.getMachine(level, pos);

        if (ep.isCrouching() && !(te instanceof BlockEntityCaveFinder))
            return InteractionResult.PASS;

        // 26.1: generic bucket-fill for fuel-burning engines (microturbine, gas, jet, sport,
        // steam). The original 1.7 pathway routed bucket → fuel-tank through
        // FluidContainerRegistry; that API is gone. Vanilla {@link net.minecraft.world.item.BucketItem}
        // now exposes its content fluid via the public {@code content} field. We map
        // water/jet-fuel/ethanol/lubricant buckets to the matching engine sub-tank, swap the
        // bucket back to an empty one (in survival), and sync. Without this branch a fresh
        // microturbine had no way to receive jet fuel — the only intake was a fuel-line from
        // below, which the user wouldn't have built that early.
        if (te instanceof reika.rotarycraft.base.blockentity.BlockEntityEngine engine
                && is != null && !is.isEmpty()
                && is.getItem() instanceof net.minecraft.world.item.BucketItem bi) {
            net.minecraft.world.level.material.Fluid f = bi.content;
            if (f != null && f != net.minecraft.world.level.material.Fluids.EMPTY) {
                int filled = engine.fillPipe(
                        engine.getBlockState().getValue(BlockRotaryCraftMachine.FACING),
                        new net.neoforged.neoforge.fluids.FluidStack(f, 1000),
                        net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.SIMULATE);
                // If that side rejects (canFill is direction-sensitive), retry through the
                // engine's actual fuel input direction by going directly through addFuel for
                // jet fuel / ethanol — they're the engine-fuels the user wants to bucket-load.
                if (filled <= 0) {
                    if (f == reika.rotarycraft.registry.RotaryFluids.JET_FUEL.get() && engine.getEngineType().isJetFueled()
                            || f == reika.rotarycraft.registry.RotaryFluids.ETHANOL.get() && engine.getEngineType().isEthanolFueled()) {
                        engine.addFuel(1000);
                        if (!ep.isCreative())
                            ep.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BUCKET));
                        te.syncAllData(true);
                        return InteractionResult.SUCCESS;
                    }
                    if (f == net.minecraft.world.level.material.Fluids.WATER && engine.getEngineType().isWaterPiped()) {
                        engine.addWater(1000);
                        if (!ep.isCreative())
                            ep.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BUCKET));
                        te.syncAllData(true);
                        return InteractionResult.SUCCESS;
                    }
                } else {
                    // The pipe-side path accepted it — commit the fill.
                    engine.fillPipe(
                            engine.getBlockState().getValue(BlockRotaryCraftMachine.FACING),
                            new net.neoforged.neoforge.fluids.FluidStack(f, 1000),
                            net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE);
                    if (!ep.isCreative())
                        ep.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BUCKET));
                    te.syncAllData(true);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        if (te instanceof BlockEntityAdvancedGear) {
            BlockEntityAdvancedGear tile = (BlockEntityAdvancedGear) te;
            if (tile.getGearType().isLubricated() && tile.canAcceptAnotherLubricantBucket()) {
                if (is != null && ReikaItemHelper.matchStacks(is, RotaryItems.LUBE_BUCKET)) {
                    tile.addLubricant(1000);
                    if (!ep.isCreative())
                        ep.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BUCKET));
                    return InteractionResult.SUCCESS;
                }
            }
        }

      /*  if (ModList.DARTCRAFT.isLoaded() && DartItemHandler.getInstance().isWrench(is)) {
            ep.setItemSlot(EquipmentSlot.MAINHAND, null);
            ep.playSound("random.break", 1, 1);
            ep.attackEntityFrom(DamageSource.inWall, 2);
            ReikaChatHelper.write("Your tool has shattered into a dozen pieces.");
            return InteractionResult.SUCCESS;
        }*/
        if (ep.isShiftKeyDown() && !m.hasSneakActions())
            return InteractionResult.FAIL;
        /*if (is != null && RotaryItems.isRegistered(is) && RotaryItems.getEntry(is).overridesRightClick(is)) {
            return InteractionResult.FAIL;
        }*/
        if (RotaryAux.isHoldingScrewdriver(ep))
            return InteractionResult.FAIL;
        if (is != null && is.getItem() == Items.ENCHANTED_BOOK && m.isEnchantable()) {
            if (((EnchantableMachine) te).getEnchantmentHandler().applyEnchants(is)) {
                if (!ep.isCreative())
                    ep.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                te.syncAllData(true);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.FAIL;
        }
        if (m == MachineRegistry.MUSICBOX) {
            if (is != null && is.getItem() == RotaryItems.DISK.get()) {
                BlockEntityMusicBox tile = (BlockEntityMusicBox) te;
                if (is.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag() != null) {
                    tile.setMusicFromDisc(is);
                } else {
                    tile.saveMusicToDisk(is);
                }
                te.syncAllData(true);
                return InteractionResult.SUCCESS;
            }
        }
        if (m == MachineRegistry.SPLITTER) {
            if (is != null && ReikaItemHelper.matchStacks(is, GearboxTypes.BEDROCK.getPart(GearboxTypes.GearPart.UNIT2))) {
                BlockEntitySplitter tile = (BlockEntitySplitter) te;
                if (!tile.isBedrock()) {
                    tile.setBedrock();
                    if (!ep.isCreative())
                        is.setCount(is.getCount() - 1);
                    te.syncAllData(true);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        if (m == MachineRegistry.FLOODLIGHT) {
            if (is != null && ReikaItemHelper.matchStacks(is, RotaryItems.LENS)) {
                BlockEntityFloodlight tile = (BlockEntityFloodlight) te;
                if (!tile.fresnel) {
                    tile.fresnel = true;
                    if (!ep.isCreative())
                        is.setCount(is.getCount() - 1);
                }
                te.syncAllData(true);
                return InteractionResult.SUCCESS;
            }
        }
        /*f (m == MachineRegistry.BORER) {
            if (is != null && ReikaItemHelper.matchStacks(is, RotaryItems.HSLA_DRILL)) {
                BlockEntityBorer tile = (BlockEntityBorer)te;
                if (tile.repair()) {
                    if (!ep.isCreative())
                        is.setCount(is.getCount() -1);
                }
                ((BlockEntityBase)te).syncAllData(true);
                return InteractionResult.SUCCESS;
            }
        }
        if (m == MachineRegistry.ECU) {
            if (is != null) {
                ReikaDyeHelper dye = ReikaDyeHelper.getColorFromItem(is);
                if (dye != null) {
                    BlockEntityEngineController tile = (BlockEntityEngineController)te;
                    tile.setColor(dye);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        if (m == MachineRegistry.BUSCONTROLLER) {
            if (is != null) {
                if (FluidContainerRegistry.isFilledContainer(is)) {
                    FluidStack f = FluidContainerRegistry.getFluidForFilledItem(is);
                    if (f != null && f.getFluid().equals(FluidRegistry.getFluid("rc lubricant"))) {
                        BlockEntityBusController tb = (BlockEntityBusController)te;
                        tb.fill(Direction.DOWN, f, true);
                        if (!ep.isCreative())
                            ep.setItemSlot(EquipmentSlot.MAINHAND, is.getItem().getContainerItem(is));
                        ((BlockEntityBase)te).syncAllData(true);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        if (m == MachineRegistry.POWERBUS) {
            BlockEntityPowerBus tile = (BlockEntityPowerBus)te;
            if (is != null && ep.isShiftKeyDown()) {
                if (tile.insertItem(is, Direction.VALID_DIRECTIONS[side])) {
                    if (!ep.isCreative())
                        is.setCount(is.getCount() -1);
                }
                ((BlockEntityBase)te).syncAllData(true);
                return InteractionResult.SUCCESS;
            }
        }
        if (m == MachineRegistry.FUELENGINE) {
            if (is != null) {
                if (FluidContainerRegistry.isFilledContainer(is)) {
                    FluidStack f = FluidContainerRegistry.getFluidForFilledItem(is);
                    if (f != null) {
                        BlockEntityFuelEngine tf = (BlockEntityFuelEngine)te;
                        if (f.getFluid().equals(FluidRegistry.getFluid("fuel"))) {
                            tf.fill(Direction.DOWN, f, true);
                            if (!ep.isCreative())
                                ep.setItemSlot(EquipmentSlot.MAINHAND, is.getItem().getContainerItem(is));
                            ((BlockEntityBase)te).syncAllData(true);
                            return InteractionResult.SUCCESS;
                        }
                        else if (f.getFluid().equals(FluidRegistry.WATER)) {
                            tf.addWater(f.amount);
                            if (!ep.isCreative())
                                ep.setItemSlot(EquipmentSlot.MAINHAND, is.getItem().getContainerItem(is));
                            ((BlockEntityBase)te).syncAllData(true);
                            return InteractionResult.SUCCESS;
                        }
                        else if (f.getFluid().equals(FluidRegistry.getFluid("rc lubricant"))) {
                            tf.addLube(f.amount);
                            if (!ep.isCreative())
                                ep.setItemSlot(EquipmentSlot.MAINHAND, is.getItem().getContainerItem(is));
                            ((BlockEntityBase)te).syncAllData(true);
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }
        }
        if (m == MachineRegistry.FRACTIONATOR) {
            if (is != null && is.getCount() == 1) {
                BlockEntityFractionator tf = (BlockEntityFractionator)te;
                if (FluidContainerRegistry.isFilledContainer(is)) {
                    FluidStack f = FluidContainerRegistry.getFluidForFilledItem(is);
                    if (f != null) {
                        if (f.getFluid().equals(FluidRegistry.getFluid("rc ethanol"))) {
                            tf.addLiquid(f.amount);
                            if (!ep.isCreative())
                                ep.setItemSlot(EquipmentSlot.MAINHAND, is.getItem().getContainerItem(is));
                            ((BlockEntityBase)te).syncAllData(true);
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
                else if (is.getItem() == Items.bucket) {
                    int amt = tf.getFuelLevel();
                    if (amt >= 1000) {
                        tf.removeLiquid(1000);
                        if (!ep.isCreative())
                            ep.setItemSlot(EquipmentSlot.MAINHAND, ItemStacks.fuelbucket.copy());
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }

        if (m == MachineRegistry.DRYING) {
            BlockEntityDryingBed tr = (BlockEntityDryingBed)te;
            if (!tr.isPlayerAccessible(ep))
                return InteractionResult.FAIL;
            if (is != null && FluidContainerRegistry.isFilledContainer(is)) {
                boolean bucket = FluidContainerRegistry.isBucket(is);
                FluidStack f = FluidContainerRegistry.getFluidForFilledItem(is);
                if (f != null) {
                    Fluid fluid = f.getFluid();
                    int size = is.getCount();
                    if (tr.getLevel()+size*f.amount <= tr.CAPACITY) {
                        if (tr.isEmpty() || f.getFluid().equals(tr.getFluid())) {
                            tr.addLiquid(fluid, size*f.amount);
                            if (!ep.isCreative()) {
                                if (bucket)
                                    ep.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.bucket, size, 0));
                                else
                                    ep.setItemSlot(EquipmentSlot.MAINHAND, null);
                            }
                            ((BlockEntityBase)te).syncAllData(true);
                            if (!level.isClientSide())
                                ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, tr, "tank");
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }
        }*/
        if (m == MachineRegistry.RESERVOIR) {
            BlockEntityReservoir tr = (BlockEntityReservoir) te;
            if (!tr.isPlayerAccessible(ep))
                return InteractionResult.FAIL;
            if (is == null) {
                FluidStack f = tr.getFluid();
                if (ep.isShiftKeyDown() && ReikaXPFluidHelper.fluidsExist() && f == ReikaXPFluidHelper.getFluid()) {
                    int amt = Math.min(Math.max(1000, Math.min(tr.getFluidLevel() / 4, 4000)), tr.getFluidLevel());
                    int xp = ReikaXPFluidHelper.getXPForAmount(amt);
                    tr.removeLiquid(amt);
                    ep.giveExperiencePoints(xp); //todo might be levels
                    ReikaSoundHelper.playSoundAtBlock(level, pos, SoundEvents.EXPERIENCE_ORB_PICKUP);
                    return InteractionResult.SUCCESS;
                }
            } else {
                if (ReikaItemHelper.matchStackWithBlock(is, Blocks.GLASS_PANE.defaultBlockState())) {
                    if (!tr.isCovered) {
                        tr.isCovered = true;
                        if (!ep.isCreative())
                            ep.setItemSlot(EquipmentSlot.MAINHAND, ReikaItemHelper.getSizedItemStack(is, is.getCount() - 1));
                        te.syncAllData(true);
                        return InteractionResult.SUCCESS;
                    }
                // 26.1: NeoForge's Capabilities.FluidHandler.ITEM still works, but the underlying
                // wire format moved to ResourceHandler<FluidResource>. Generic bucket fill/drain
                // via the item capability isn't wired yet; only the explicit single-item branches
                // below are honoured (glass bottle → water bottle, jet-fuel bucket reject). Adding
                // generic bucket support is a self-contained future improvement and doesn't gate
                // anything else.
                } else if (is.getItem() == Items.GLASS_BOTTLE) {
                    int size = is.getCount();
                    if (tr.getFluidLevel() > 0 && tr.getFluid().getFluid().equals(Fluids.WATER)) {
                        ep.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.POTION, size));
                        te.syncAllData(true);
                        return InteractionResult.SUCCESS;
                    }
                } else if (is.getItem() instanceof net.minecraft.world.item.BucketItem bi) {
                    // 26.1 fix: previously special-cased only vanilla water/lava buckets AND
                    // rejected the jet-fuel bucket outright — but the user wanted "jet fuel and
                    // other mod fluids" to fill the reservoir too. Generic path: probe the bucket
                    // item for its content fluid via {@code BucketItem.content}, which is set by
                    // all standard {@code DispensibleContainerItem} buckets including vanilla
                    // water/lava, the RotaryCraft fluid buckets, and modded fluid buckets.
                    net.minecraft.world.level.material.Fluid f = bi.content;
                    if (f != null && f != net.minecraft.world.level.material.Fluids.EMPTY && tr.canAcceptFluid(f)) {
                        net.neoforged.neoforge.fluids.FluidStack stack = new net.neoforged.neoforge.fluids.FluidStack(f, 1000);
                        int filled = tr.fillPipe(net.minecraft.core.Direction.NORTH, stack,
                                net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE);
                        if (filled > 0) {
                            if (!ep.isCreative())
                                ep.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BUCKET));
                            te.syncAllData(true);
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }
        }
        /*if (m == MachineRegistry.SCALECHEST) {
            BlockEntityScaleableChest tc = (BlockEntityScaleableChest)te;
            if (!tc.isUseableByPlayer(ep))
                return InteractionResult.FAIL;
        }
        if (m == MachineRegistry.BEDROCKBREAKER && !ep.isShiftKeyDown()) {
            BlockEntityBedrockBreaker tb = (BlockEntityBedrockBreaker)te;
            tb.dropInventory();
            ((BlockEntityBase)te).syncAllData(true);
            return InteractionResult.SUCCESS;
        }
        if (m == MachineRegistry.EXTRACTOR) {
            BlockEntityExtractor ex = (BlockEntityExtractor)te;
            if (is != null) {
                if (is.getItem() == Items.water_bucket && is.getCount() == 1 && ex.getLevel()+1000 <= ex.CAPACITY) {
                    ex.addLiquid(1000);
                    if (!ep.isCreative()) {
                        ep.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.bucket));
                    }
                    ((BlockEntityBase)te).syncAllData(true);
                    return InteractionResult.SUCCESS;
                }
                else if (ReikaItemHelper.matchStacks(is, ItemStacks.bedrockdrill)) {
                    if (ex.upgrade() && !ep.isCreative())
                        is.setCount(is.getCount() -1);
                    ((BlockEntityBase)te).syncAllData(true);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        if (m == MachineRegistry.PULSEJET) {
            BlockEntityPulseFurnace ex = (BlockEntityPulseFurnace)te;
            int f = ex.getFuel();
            if (is != null && f+1000*is.getCount() <= ex.MAXFUEL && ReikaItemHelper.matchStacks(is, ItemStacks.fuelbucket)) {
                ex.addFuel(1000*is.getCount());
                if (!ep.isCreative()) {
                    ep.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.bucket, is.getCount(), 0));
                }
                ((BlockEntityBase)te).syncAllData(true);
                return InteractionResult.SUCCESS;
            }
            int water = ex.getWater();
            if (water+1000 <= ex.CAPACITY && is != null && is.getItem() == Items.water_bucket) {
                ex.addWater(1000);
                if (!ep.isCreative()) {
                    ep.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.bucket));
                }
                ((BlockEntityBase)te).syncAllData(true);
                return InteractionResult.SUCCESS;
            }
        }
        if (m == MachineRegistry.FERMENTER) {
            BlockEntityFermenter fm = (BlockEntityFermenter)te;
            if (fm.getLevel()+1000 <= fm.CAPACITY && is != null && is.getItem() == Items.water_bucket) {
                fm.addLiquid(1000);
                if (!ep.isCreative()) {
                    ep.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.bucket));
                }
                ((BlockEntityBase)te).syncAllData(true);
                return InteractionResult.SUCCESS;
            }
        }*/
        if (m == MachineRegistry.OBSIDIAN) {
            BlockEntityObsidianMaker fm = (BlockEntityObsidianMaker) te;
            if (fm.getWater() + 1000 <= BlockEntityObsidianMaker.CAPACITY && is != null && is.getItem() == Items.WATER_BUCKET) {
                fm.addWater(1000);
                if (!ep.isCreative()) {
                    ep.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BUCKET));
                }
                te.syncAllData(true);
                return InteractionResult.SUCCESS;
            } else if (fm.getLava() + 1000 <= BlockEntityObsidianMaker.CAPACITY && is != null && is.getItem() == Items.LAVA_BUCKET) {
                fm.addLava(1000);
                if (!ep.isCreative()) {
                    ep.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BUCKET));
                }
                te.syncAllData(true);
                return InteractionResult.SUCCESS;
            }
        }
        /*if (m == MachineRegistry.FERTILIZER) {
            BlockEntityFertilizer fm = (BlockEntityFertilizer)te;
            if (fm.getLevel()+1000 <= fm.getCapacity() && is != null && is.getItem() == Items.water_bucket) {
                fm.addLiquid(1000);
                if (!ep.isCreative()) {
                    ep.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.bucket));
                }
                ((BlockEntityBase)te).syncAllData(true);
                return InteractionResult.SUCCESS;
            }
        }*/
        if (m == MachineRegistry.BIGFURNACE) {
            BlockEntityLavaSmeltery bf = (BlockEntityLavaSmeltery) te;
            if (bf.getLiquidLevel() + 1000 <= bf.getCapacity() && is != null && is.getItem() == Items.LAVA_BUCKET) { //todo check getLiquidLevel
                bf.addLiquid(1000);
                if (!ep.isCreative()) {
                    ep.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BUCKET));
                }
                te.syncAllData(true);
                return InteractionResult.SUCCESS;
            }
        }
        /*if (m == MachineRegistry.EMP) {
            BlockEntityEMP tp = (BlockEntityEMP)te;
            tp.updateListing();
            ((BlockEntityBase)te).syncAllData(true);
            return InteractionResult.SUCCESS;
        }
        if (m == MachineRegistry.FUELENHANCER) {
            BlockEntityFuelConverter tf = (BlockEntityFuelConverter)te;
            if (is != null) {
                FluidStack liq = FluidContainerRegistry.getFluidForFilledItem(is);
                if (liq != null && liq.getFluid().equals(FluidRegistry.getFluid("fuel"))) {
                    boolean bucket = FluidContainerRegistry.isBucket(is);
                    tf.fill(Direction.UP, liq, true);
                    if (!ep.isCreative()) {
                        if (bucket)
                            ep.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.bucket));
                        else
                            ep.setItemSlot(EquipmentSlot.MAINHAND, null);
                    }
                    ((BlockEntityBase)te).syncAllData(true);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        if (m == MachineRegistry.DISPLAY && is != null) {
            if (ReikaDyeHelper.isDyeItem(is)) {
                BlockEntityDisplay td = (BlockEntityDisplay)te;
                td.setDyeColor(ReikaDyeHelper.getColorFromItem(is));
                ((BlockEntityBase)te).syncAllData(true);
                return InteractionResult.SUCCESS;
            }
            if (is.getItem() == Items.glowstone_dust) {
                BlockEntityDisplay td = (BlockEntityDisplay)te;
                td.setColorToArgon();
                ((BlockEntityBase)te).syncAllData(true);
                return InteractionResult.SUCCESS;
            }
            if (is.getItem() == Items.written_book) {
                try {
                    BlockEntityDisplay td = (BlockEntityDisplay)te;
                    NBTTagCompound nbt = is.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
                    NBTTagList li = nbt.getTagList("pages", NBTTypes.STRING.ID);
                    ArrayList<String> s = new ArrayList();
                    for (int i = 0; i < li.tagCount(); i++) {
                        String ns = li.getStringTagAt(i);
                        s.add(ns);
                    }
                    td.clearMessage();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < s.size(); i++) {
                        sb.append(s.get(i));
                        if (i < s.size()-1 && !s.get(i).endsWith(" ")) {
                            sb.append(" ");
                        }
                    }
                    td.setMessage(sb.toString());
                }
                catch (Exception e) {
                    ReikaChatHelper.writeString("Error reading book.");
                    e.printStackTrace();
                }
                ((BlockEntityBase)te).syncAllData(true);
                return InteractionResult.SUCCESS;
            }
        }*/
       /* if (m == MachineRegistry.BLOWER) {
            if (is != null && ReikaItemHelper.matchStacks(is, m.getCraftedProduct()))
                return InteractionResult.FAIL;
        }*/
        if (m == MachineRegistry.MIRROR) {
            BlockEntityMirror tm = (BlockEntityMirror) te;
            if (tm.broken) {
                if (ReikaItemHelper.matchStacks(is, RotaryItems.MIRROR)) {
                    tm.repair(level, pos);
                    if (!ep.isCreative()) {
                        ep.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(is.getItem(), is.getCount() - 1));
                    }
                    te.syncAllData(true);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        /*if (m == MachineRegistry.SCREEN) {
            BlockEntityScreen tc = (BlockEntityScreen)te;
            if (ep.isShiftKeyDown()) {
                tc.activate(ep);
                ((BlockEntityBase)te).syncAllData(true);
                return InteractionResult.SUCCESS;
            }
        }*/
        if (m == MachineRegistry.CAVESCANNER) {
            BlockEntityCaveFinder tc = (BlockEntityCaveFinder) te;
            Direction dir = ReikaEntityHelper.getDirectionFromEntityLook(ep, true);
            int mov = 4;
            if (ep.isShiftKeyDown())
                mov *= -1;
            tc.moveSrc(mov, dir);
            te.syncAllData(true);
            return InteractionResult.SUCCESS;
        }

        if (te instanceof BlockEntityEngine tile) {
            if (is != null && is.getItem() == RotaryItems.JET_FUEL_BUCKET.get())
                return InteractionResult.FAIL;
            /*if (is != null && ReikaItemHelper.matchStacks(is, RotaryItems.TURBINE)) {
                if (tile.getEngineType() == EngineType.JET && ((TileEntityJetEngine) tile).FOD > 0) {
                    ((TileEntityJetEngine) tile).repairJet();
                    if (!ep.isCreative())
                        --is.stackSize;
                    return InteractionResult.SUCCESS;
                }
            }
            if (is != null && ReikaItemHelper.matchStacks(is, RotaryItems.COMPRESSOR)) {
                if (tile.getEngineType() == EngineType.JET && ((TileEntityJetEngine) tile).FOD > 0) {
                    ((TileEntityJetEngine) tile).repairJetPartial();
                    if (!ep.isCreative())
                        is.setCount(is.getCount() - 1);
                    return InteractionResult.SUCCESS;
                }
            }
            if (is != null && ReikaItemHelper.matchStacks(is, RotaryItems.BEDROCK_ALLOY_SHAFT)) {
                if (tile.getEngineType() == EngineType.HYDRO && !((TileEntityHydroEngine) tile).isBedrock()) {
                    ((TileEntityHydroEngine) tile).makeBedrock();
                    if (!ep.isCreative())
                        is.setCount(is.getCount() - 1);
                    return InteractionResult.SUCCESS;
                }
            }*/
            if (is != null && is.getCount() == 1) {
                if (is.getItem() == Items.BUCKET) {
                    if (tile.getEngineType().isEthanolFueled()) {
                        if (tile.getFuelLevel() >= 1000) {
                            ep.setItemSlot(EquipmentSlot.MAINHAND, RotaryItems.ETHANOL_BUCKET.get().getDefaultInstance());
                            tile.subtractFuel(1000);
                        } else {
                            if (ConfigRegistry.CLEARCHAT.getState())
                                ReikaChatHelper.clearChat();
                            ReikaChatHelper.write("Engine does not have enough fuel to extract!");
                        }
                        return InteractionResult.SUCCESS;
                    }
                    if (tile.getEngineType().isJetFueled()) {
                        if (tile.getFuelLevel() >= 1000) {
                            ep.setItemSlot(EquipmentSlot.MAINHAND, RotaryItems.JET_FUEL_BUCKET.get().getDefaultInstance());
                            tile.subtractFuel(1000);
                        } else {
                            if (ConfigRegistry.CLEARCHAT.getState())
                                ReikaChatHelper.clearChat();
                            ReikaChatHelper.write("Engine does not have enough fuel to extract!");
                        }
                        return InteractionResult.SUCCESS;
                    }
                    if (tile.getEngineType().requiresLubricant()) {
                        if (tile.getLube() >= 1000) {
                            ep.setItemSlot(EquipmentSlot.MAINHAND, RotaryItems.LUBE_BUCKET.get().getDefaultInstance());
                            tile.removeLubricant(1000);
                        } else {
                            if (ConfigRegistry.CLEARCHAT.getState())
                                ReikaChatHelper.clearChat();
                            ReikaChatHelper.write("Engine does not have enough fuel to extract!");
                        }
                        return InteractionResult.SUCCESS;
                    }
                }
                if (tile.getEngineType().isJetFueled()) {
                    if (ReikaItemHelper.matchStacks(is, RotaryItems.JET_FUEL_BUCKET)) {
                        if (tile.getFuelLevel() <= BlockEntityEngine.FUELCAP - 1000) {
                            if (!ep.isCreative())
                                ep.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BUCKET));
                            tile.addFuel(1000);
                        } else {
                            if (ConfigRegistry.CLEARCHAT.getState())
                                ReikaChatHelper.clearChat();
                            ReikaChatHelper.write("Engine is too full to add fuel!");
                        }
                        return InteractionResult.SUCCESS;
                    }
                }
                if (tile.getEngineType().isEthanolFueled()) {
                    if (ReikaItemHelper.matchStacks(is, RotaryItems.ETHANOL_BUCKET)) {
                        if (tile.getFuelLevel() <= BlockEntityEngine.FUELCAP - 1000) {
                            if (!ep.isCreative())
                                ep.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BUCKET));
                            tile.addFuel(1000);
                        } else {
                            if (ConfigRegistry.CLEARCHAT.getState())
                                ReikaChatHelper.clearChat();
                            ReikaChatHelper.write("Engine is too full to add fuel!");
                        }
                        return InteractionResult.SUCCESS;
                    }
                }
                if (tile.getEngineType().requiresLubricant()) {
                    if (ReikaItemHelper.matchStacks(is, RotaryItems.LUBE_BUCKET)) {
                        if (tile.getLube() <= BlockEntityEngine.LUBECAP - 1000) {
                            if (!ep.isCreative())
                                ep.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BUCKET));
                            tile.addLubricant(1000);
                        } else {
                            if (ConfigRegistry.CLEARCHAT.getState())
                                ReikaChatHelper.clearChat();
                            ReikaChatHelper.write("Engine is too full to add lubricant!");
                        }
                        return InteractionResult.SUCCESS;
                    }
                }
                if (tile.getEngineType().needsWater()) {
                    if (is != null && is.getItem() == Items.WATER_BUCKET) {
                        if (tile.getWater() <= BlockEntityEngine.CAPACITY - 1000) {
                            if (!ep.isCreative())
                                ep.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BUCKET));
                            tile.addWater(1000);
                        } else {
                            if (ConfigRegistry.CLEARCHAT.getState())
                                ReikaChatHelper.clearChat();
                            ReikaChatHelper.write("Engine is too full to add water!");
                        }
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        if (te != null && RotaryAux.hasGui(level, pos, ep) && te.isPlayerAccessible(ep)) {
            if (!level.isClientSide() && ep instanceof ServerPlayer sp) {
                // Use the block entity as the MenuProvider, which should now provide a BlankContainer-based menu
                sp.openMenu((MenuProvider) te, pos);
            }
            ep.swing(InteractionHand.MAIN_HAND, true);
            return InteractionResult.SUCCESS;
        }
        // 26.1 fix: legacy fall-through used to {@code syncAllData(true)} here, which fires a
        // full sync packet on EVERY right-click that didn't open a menu. For pipe-like blocks
        // (no GUI) every right-click was triggering an expensive serialize+broadcast — causing
        // the user's "right-click a pipe drops to 0 fps" lag. The relevant data changes already
        // trigger syncs via {@code setChanged}/{@code recomputeConnections}; this fallback is
        // redundant and harmful.
        return InteractionResult.FAIL;
    }

    // 1.21.5: Block.appendHoverText removed; tooltips are now on Item. Kept as a helper for BlockItem subclass wiring.
    public void appendHoverText(ItemStack is, Item.TooltipContext ctx, List<Component> li, TooltipFlag flag) {
        MachineRegistry m = MachineRegistry.getMachineMapping(Block.byItem(is.getItem()));
        if (m == null) {
            return;
        }
//        RotaryCraft.LOGGER.info(m);
//        ItemMachineRenderer ir = ClientProxy.machineItems;
//        BlockEntity te = ir.getRenderingInstance(m, 0);

    //    if (m.isIncomplete()) {
        //    li.add("This machine is in development. Use at your own risk.");
    //    }
       //if (m.hasNBTVariants() && is.stackTagCompound != null) {
       //    li.addAll(((NBTMachine)te).getDisplayTags(is.stackTagCompound));
       //}
        /*if (m == MachineRegistry.FUELENGINE) {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                int t = TileEntityFuelEngine.GEN_TORQUE;
                int o = TileEntityFuelEngine.GEN_OMEGA;
                li.add(String.format("Power: %.3f %sW", ReikaMathLibrary.getThousandBase(t * o), ReikaEngLibrary.getSIPrefix(t * o)));
                li.add(String.format("Torque: %.3f %sNm", ReikaMathLibrary.getThousandBase(t), ReikaEngLibrary.getSIPrefix(t)));
                li.add(String.format("Speed: %.3f %srad/s", ReikaMathLibrary.getThousandBase(o), ReikaEngLibrary.getSIPrefix(o)));
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Hold ");
                sb.append(EnumChatFormatting.GREEN.toString());
                sb.append("Shift");
                sb.append(EnumChatFormatting.GRAY.toString());
                sb.append(" for power data");
                li.add(sb.toString());
            }
        }*/
        if (m.isPowerReceiver()) {
            PowerReceivers p = m.getPowerReceiverEntry();
            long pow = p.getMinPowerForDisplay();
            int trq = p.getMinTorqueForDisplay();
            int spd = p.getMinSpeedForDisplay();
            boolean minp = !p.hasNoDirectMinPower();
            boolean mint = !p.hasNoDirectMinTorque();
            boolean mins = !p.hasNoDirectMinSpeed();
            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
                if (minp)
                    li.add(Component.literal(String.format("Minimum Power: %.3f %sW", ReikaMathLibrary.getThousandBase(pow), ReikaEngLibrary.getSIPrefix(pow))));
                if (mint)
                    li.add(Component.literal(String.format("Minimum Torque: %.3f %sNm", ReikaMathLibrary.getThousandBase(trq), ReikaEngLibrary.getSIPrefix(trq))));
                if (mins)
                    li.add(Component.literal(String.format("Minimum Speed: %.3f %srad/s", ReikaMathLibrary.getThousandBase(spd), ReikaEngLibrary.getSIPrefix(spd))));
            } else {
                if (minp || mint || mins) {
                    String sb = "Hold " +
                            ChatFormatting.GREEN +
                            "Shift" +
                            ChatFormatting.GRAY +
                            " for power data";
                    li.add(Component.literal(sb));
                }
            }
        }

        if (m.isEngine()) {
            EngineType type = m.getEngineType();
            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
                double power = type.getPower();
                double speed = type.getSpeed();
                double torque = type.getTorque();
                li.add(Component.literal(String.format("Power: %.3f %sW", ReikaMathLibrary.getThousandBase(power), ReikaEngLibrary.getSIPrefix(power))));
                li.add(Component.literal(String.format("Torque: %.3f %sNm", ReikaMathLibrary.getThousandBase(torque), ReikaEngLibrary.getSIPrefix(torque))));
                li.add(Component.literal(String.format("Speed: %.3f %srad/s", ReikaMathLibrary.getThousandBase(speed), ReikaEngLibrary.getSIPrefix(speed))));
            } else {
                String sb = "Hold " +
                        ChatFormatting.GREEN +
                        "Shift" +
                        ChatFormatting.GRAY +
                        " for power data";
                li.add(Component.literal(sb));
            }
            if (is.has(DataComponents.CUSTOM_DATA)) {
                int dmg = is.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag().getIntOr("damage", 0);
                li.add(Component.literal(String.format("Damage: %.1f%s", dmg * 12.5F, "%")));
            }
            if (is.has(DataComponents.CUSTOM_DATA)) {
                if (is.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag().getBooleanOr("bed", false)) {
                    li.add(Component.literal("Bedrock Upgrade"));
                }
            }
            // BlockEntityEngine te = (BlockEntityEngine) MachineRegistry.AC_ENGINE//.createTEInstanceForRender(i);
            // if (te instanceof NBTMachine && is.has(DataComponents.CUSTOM_DATA)) {
                // for (String s : ((NBTMachine)te).getDisplayTags(is.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag())) {
                    // li.add(Component.literal(s));
                //}
            //}
        }
    }
}
