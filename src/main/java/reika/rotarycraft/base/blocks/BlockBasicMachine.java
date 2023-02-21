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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reika.dragonapi.base.BlockEntityBase;
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
import reika.rotarycraft.auxiliary.interfaces.*;
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

public abstract class BlockBasicMachine extends BlockRotaryCraftMachine {

    public BlockBasicMachine(Properties properties) {
        super(properties);
//        this.setHardness(4F);
//        this.setResistance(15F);
//        this.setLightLevel(0F);
//        if (par3Material == Material.METAL)
//            this.setStepSound(soundTypeMetal);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState pState, @Nullable LivingEntity e, ItemStack pStack) {
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
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        super.neighborChanged(state, world, pos, pBlock, pFromPos, pIsMoving);
        MachineRegistry m = MachineRegistry.getMachine(world, pos);
        if (m != null) {
            BlockEntity te = world.getBlockEntity(pos);
            if (m.cachesConnections()) {
                CachedConnection tc = (CachedConnection) te;
                tc.recomputeConnections(world, pos);
            }
            if (m == MachineRegistry.SMOKEDETECTOR) {
                Block upid = world.getBlockState(pos.above()).getBlock();
                if (upid == Blocks.AIR) {
                    world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    ItemStack is = MachineRegistry.SMOKEDETECTOR.getBlockState().getBlock().asItem().getDefaultInstance();
                    if (!world.isClientSide)
                        world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), is));
                } //todo else if (!upid.isOpaqueCube()) {
                world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                ItemStack is = MachineRegistry.SMOKEDETECTOR.getBlockState().getBlock().asItem().getDefaultInstance();
                if (!world.isClientSide)
                    world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), is));
                // }
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player ep, InteractionHand pHand, BlockHitResult pHit) {
        super.use(state, level, pos, ep, pHand, pHit);
        RotaryCraftBlockEntity te = (RotaryCraftBlockEntity) level.getBlockEntity(pos);
        ItemStack is = ep.getMainHandItem();
        MachineRegistry m = MachineRegistry.getMachine(level, pos);

        if (ep.isCrouching() && !(te instanceof BlockEntityCaveFinder))
            return InteractionResult.PASS;
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
//todo                if (!ep.isCreative())
//                    ep.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                te.syncAllData(true);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.FAIL;
        }
        if (m == MachineRegistry.MUSICBOX) {
            if (is != null && is.getItem() == RotaryItems.DISK.get()) {
                BlockEntityMusicBox tile = (BlockEntityMusicBox) te;
                if (is.getTag() != null) {
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
                            if (!level.isClientSide)
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
                        ((BlockEntityBase) te).syncAllData(true);
                        return InteractionResult.SUCCESS;
                    }
                } else if (is.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()) {

                    if (is.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve().get().getFluidInTank(0).getAmount() > 0) {
                        FluidStack f = is.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve().get().getFluidInTank(0);
                        if (f != null) {
                            Fluid fluid = f.getFluid();
                            int size = is.getCount();
                            if (tr.getFluidLevel() + (size - 1) * f.getAmount() <= tr.CAPACITY) {
                                if (tr.isEmpty()) {
                                    tr.addLiquid(size * f.getAmount(), fluid);
                                    if (!ep.isCreative()) {
                                        @NotNull FluidStack ret = is.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve().get().drain(f, IFluidHandler.FluidAction.EXECUTE);
                                        ep.setItemSlot(EquipmentSlot.MAINHAND, ret.isEmpty() ? ReikaItemHelper.getSizedItemStack(is, size) : ItemStack.EMPTY);
                                    }
                                    ((BlockEntityBase) te).syncAllData(true);
                                    if (!level.isClientSide)
                                        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, tr, "tank");
                                    return InteractionResult.SUCCESS;
                                } else if (f.getFluid().equals(tr.getFluid().getFluid())) {
                                    tr.addLiquid(size * f.getAmount(), fluid);
                                    if (!ep.isCreative()) {
                                        @NotNull FluidStack ret = is.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve().get().drain(f, IFluidHandler.FluidAction.EXECUTE);
                                        ep.setItemSlot(EquipmentSlot.MAINHAND, ret.isEmpty() ? ReikaItemHelper.getSizedItemStack(is, size) : ItemStack.EMPTY);
                                    }
                                    ((BlockEntityBase) te).syncAllData(true);
                                    if (!level.isClientSide)
                                        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, tr, "tank");
                                    return InteractionResult.SUCCESS;
                                }
                            }
                        }
                    }
                } else if (is.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent() && !tr.isEmpty()) {
                    FluidStack stack = tr.getContents();
                    boolean hasCapacityAvailable = is.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).map(e -> stack.getAmount() <= e.getTankCapacity(0) - e.getFluidInTank(0).getAmount()).orElse(false);
                    if (hasCapacityAvailable) {
                        int size = is.getCount();
                        boolean actionSuccessful = is.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).map(e -> {
                            e.fill(stack, IFluidHandler.FluidAction.EXECUTE);
                            int amt = e.getFluidInTank(0).getAmount() * size;
                            if (tr.getFluidLevel() >= amt) {
                                tr.removeLiquid(amt);
                                if (!ep.isCreative())
                                    ep.setItemSlot(EquipmentSlot.MAINHAND, ReikaItemHelper.getSizedItemStack(is, size)); //todo check if this breaks lol
                                ((BlockEntityBase) te).syncAllData(true);
                                if (!level.isClientSide)
                                    ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, tr, "tank");
                                return true;
                            }
                            return false;
                        }).orElse(false);
                        if (actionSuccessful)
                            return InteractionResult.SUCCESS;
                    }
                } else if (is.getItem() == Items.GLASS_BOTTLE) {
                    int size = is.getCount();
                    if (tr.getFluidLevel() > 0 && tr.getFluid().getFluid().equals(Fluids.WATER)) {
                        ep.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.POTION, size));
                        ((BlockEntityBase) te).syncAllData(true);
                        return InteractionResult.SUCCESS;
                    }
                } else if (is.getItem() == RotaryItems.JET_FUEL_BUCKET.get()) { //todo make sure this is meant to be jet fuel
                    return InteractionResult.FAIL;
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
            if (fm.getWater() + 1000 <= fm.CAPACITY && is != null && is.getItem() == Items.WATER_BUCKET) {
                fm.addWater(1000);
                if (!ep.isCreative()) {
                    ep.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BUCKET));
                }
                ((BlockEntityBase) te).syncAllData(true);
                return InteractionResult.SUCCESS;
            } else if (fm.getLava() + 1000 <= fm.CAPACITY && is != null && is.getItem() == Items.LAVA_BUCKET) {
                fm.addLava(1000);
                if (!ep.isCreative()) {
                    ep.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BUCKET));
                }
                ((BlockEntityBase) te).syncAllData(true);
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
                ((BlockEntityBase) te).syncAllData(true);
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
                    NBTTagCompound nbt = is.getTag();
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
                        if (tile.getFuelLevel() <= tile.FUELCAP - 1000) {
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
                        if (tile.getFuelLevel() <= tile.FUELCAP - 1000) {
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
                        if (tile.getLube() <= tile.LUBECAP - 1000) {
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
                        if (tile.getWater() <= tile.CAPACITY - 1000) {
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
            if (!level.isClientSide()) {
                NetworkHooks.openScreen((ServerPlayer) ep, te, pos);
            }
            ep.swing(InteractionHand.MAIN_HAND, true);
            /*if (te instanceof BlockEntityReservoir reservoir && !level.isClientSide()) {
                NetworkHooks.openScreen((ServerPlayer) player, reservoir, entity.getBlockPos());
                return InteractionResult.SUCCESS;
            }*/
            return InteractionResult.SUCCESS;
        }
        te.syncAllData(true);

        return InteractionResult.FAIL;
    }

    @Override
    public void appendHoverText(ItemStack is, @Nullable BlockGetter p_49817_, List<Component> li, TooltipFlag p_49819_) {
        super.appendHoverText(is, p_49817_, li, p_49819_);
        MachineRegistry m = MachineRegistry.getMachineMapping(Block.byItem(is.getItem()));
        if (m == null) {
            return;
        }
//        RotaryCraft.LOGGER.info(m);
//        ItemMachineRenderer ir = ClientProxy.machineItems;
//        BlockEntity te = ir.getRenderingInstance(m, 0);

//        if (m.isIncomplete()) {
//            li.add("This machine is in development. Use at your own risk.");
//        }
//        if (m.hasNBTVariants() && is.stackTagCompound != null) {
//            li.addAll(((NBTMachine)te).getDisplayTags(is.stackTagCompound));
//        }
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
            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LSHIFT)) {
                if (minp)
                    li.add(Component.literal(String.format("Minimum Power: %.3f %sW", ReikaMathLibrary.getThousandBase(pow), ReikaEngLibrary.getSIPrefix(pow))));
                if (mint)
                    li.add(Component.literal(String.format("Minimum Torque: %.3f %sNm", ReikaMathLibrary.getThousandBase(trq), ReikaEngLibrary.getSIPrefix(trq))));
                if (mins)
                    li.add(Component.literal(String.format("Minimum Speed: %.3f %srad/s", ReikaMathLibrary.getThousandBase(spd), ReikaEngLibrary.getSIPrefix(spd))));
            } else {
                if (minp || mint || mins) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Hold ");
                    sb.append(ChatFormatting.GREEN);
                    sb.append("Shift");
                    sb.append(ChatFormatting.GRAY);
                    sb.append(" for power data");
                    li.add(Component.literal(sb.toString()));
                }
            }
        }

        if (m.isEngine()) {
            EngineType type = m.getEngineType();
            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LSHIFT)) {
                double power = type.getPower();
                double speed = type.getSpeed();
                double torque = type.getTorque();
                li.add(Component.literal(String.format("Power: %.3f %sW", ReikaMathLibrary.getThousandBase(power), ReikaEngLibrary.getSIPrefix(power))));
                li.add(Component.literal(String.format("Torque: %.3f %sNm", ReikaMathLibrary.getThousandBase(torque), ReikaEngLibrary.getSIPrefix(torque))));
                li.add(Component.literal(String.format("Speed: %.3f %srad/s", ReikaMathLibrary.getThousandBase(speed), ReikaEngLibrary.getSIPrefix(speed))));
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Hold ");
                sb.append(ChatFormatting.GREEN);
                sb.append("Shift");
                sb.append(ChatFormatting.GRAY);
                sb.append(" for power data");
                li.add(Component.literal(sb.toString()));
            }
            if (is.hasTag()) {
                int dmg = is.getTag().getInt("damage");
                li.add(Component.literal(String.format("Damage: %.1f%s", dmg * 12.5F, "%")));
            }
            if (is.hasTag()) {
                if (is.getTag().getBoolean("bed")) {
                    li.add(Component.literal("Bedrock Upgrade"));
                }
            }
           /* todo BlockEntityEngine te = (BlockEntityEngine)MachineRegistry.AC_ENGINE.createTEInstanceForRender(i);
            if (te instanceof NBTMachine && is.hasTag()) {
                for (String s : ((NBTMachine)te).getDisplayTags(is.getTag())) {
                    li.add(Component.literal(s));
                }
            }*/
        }
    }
}
