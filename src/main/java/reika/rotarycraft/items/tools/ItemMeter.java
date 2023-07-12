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

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import reika.dragonapi.interfaces.blockentity.ThermalTile;
import reika.dragonapi.libraries.io.ReikaChatHelper;
import reika.dragonapi.libraries.java.ReikaStringParser;
import reika.dragonapi.libraries.mathsci.ReikaDateHelper;
import reika.dragonapi.libraries.mathsci.ReikaEngLibrary;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.api.interfaces.PressureTile;
import reika.rotarycraft.api.interfaces.Transducerable;
import reika.rotarycraft.api.power.ShaftMachine;
import reika.rotarycraft.api.power.ShaftPowerEmitter;
import reika.rotarycraft.api.power.ShaftPowerReceiver;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.auxiliary.Variables;
import reika.rotarycraft.auxiliary.interfaces.*;
import reika.rotarycraft.base.ItemRotaryTool;
import reika.rotarycraft.base.blockentity.*;
import reika.rotarycraft.blockentities.production.BlockEntityPump;
import reika.rotarycraft.blockentities.transmission.BlockEntityBevelGear;
import reika.rotarycraft.blockentities.transmission.BlockEntityDistributionClutch;
import reika.rotarycraft.blockentities.transmission.BlockEntityGearbox;
import reika.rotarycraft.blockentities.transmission.BlockEntitySplitter;
import reika.rotarycraft.blockentities.weaponry.BlockEntityHeatRay;

import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.MachineRegistry;

import java.util.ArrayList;
import java.util.Collection;

public class ItemMeter extends ItemRotaryTool {
    public ItemMeter() {
        super(new Properties());
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        Player ep = context.getPlayer();
        BlockPos pos = context.getClickedPos();

        if (world.isClientSide && ConfigRegistry.CLEARCHAT.getState())
            ReikaChatHelper.clearChat();
        Block bk = world.getBlockState(pos).getBlock();
        BlockEntity tile = world.getBlockEntity(pos);
        String name = tile != null ? (tile instanceof RotaryCraftBlockEntity ? tile.toString() : tile.getClass().getSimpleName()) : "null";
        MachineRegistry m = MachineRegistry.getMachine(world, pos);

        if (tile instanceof BlockEntityIOMachine) {
            ((BlockEntityIOMachine) tile).iotick = 512;
        } else if (tile instanceof ShaftMachine sm) {
            sm.setIORenderAlpha(512);
            return InteractionResult.SUCCESS;
        }
        //world.markBlockForUpdate(pos);

        if (tile instanceof Transducerable) {
            ArrayList<String> li = ((Transducerable) tile).getMessages(world, pos, context.getClickedFace());
            if (li != null) {
                for (String s : li) this.sendMessage(ep, s);
                return InteractionResult.SUCCESS;
            }
        } else if (bk instanceof Transducerable) {
            ArrayList<String> li = ((Transducerable) bk).getMessages(world, pos, context.getClickedFace());
            if (li != null) {
                for (String s : li) this.sendMessage(ep, s);
                return InteractionResult.SUCCESS;
            }
        }

        if (tile instanceof ThermalTile) {
            ThermalTile th = (ThermalTile) tile;
            this.sendMessage(ep, String.format("%s %s: %s", name, Variables.TEMPERATURE, RotaryAux.formatTemperature(th.getTemperature())));
            return InteractionResult.SUCCESS;
        }

        if (tile instanceof PressureTile) {
            PressureTile th = (PressureTile) tile;
            this.sendMessage(ep, String.format("%s %s: %s", name, Variables.PRESSURE, RotaryAux.formatPressure(th.getPressure())));
            return InteractionResult.SUCCESS;
        }

        if (tile instanceof RangedEffect) {
            RangedEffect te = (RangedEffect) tile;
            this.sendMessage(ep, Variables.RANGE + ": " + RotaryAux.formatDistance(te.getRange()) + "; " + "Max Range: " + RotaryAux.formatDistance(te.getMaxRange()));
            return InteractionResult.SUCCESS;
        }

        if (tile instanceof DiscreteFunction) {
            this.sendMessage(ep, Variables.OPERATIONTIME + ": " + ((DiscreteFunction) tile).getOperationTime() / 20F + " s");
            return InteractionResult.SUCCESS;
        }

        if (tile instanceof IntegratedGearboxable) {
            IntegratedGearboxable te = (IntegratedGearboxable) tile;
            int ratio = te.getIntegratedGear();
            if (ratio != 0) {
                this.sendMessage(ep, "Integrated Gearbox: " + Math.abs(ratio) + " x, " + (ratio > 0 ? "Torque" : "Speed"));
                return InteractionResult.SUCCESS;
            }
        }

        if (tile instanceof PowerSourceTracker) {
            this.sendMessage(ep, "Power is being received from:");
            this.sendMessages(ep, ((PowerSourceTracker) tile).getPowerSources((PowerSourceTracker) tile, null).getMessages());
            return InteractionResult.SUCCESS;
        }

        if (tile instanceof BlockEntityEngine) {
            BlockEntityEngine te = (BlockEntityEngine) tile;
            world.blockUpdated(tile.getBlockPos(), te.getTEBlock());
            long power = te.power;
            this.sendMessage(ep, String.format("%s producing %s", name, RotaryAux.formatPowerIO(te)));
            if (te.getEngineType().isAirBreathing() && te.isDrowned(world, pos))
                this.sendLocalizedMessage(ep, "drowning");
//            if (te.getEngineType() == EngineType.JET) {
//                if (((BlockEntityJetEngine) te).getChokedFraction(world, pos) < 1)
//                    this.sendLocalizedMessage(ep, "choke");
//                if (((BlockEntityJetEngine) te).FOD >= 8)
//                    this.sendLocalizedMessage(ep, "fod");
//            }
            if (te.getEngineType().burnsFuel()) {
                int time = te.getFuelDuration();
                String sg = String.format("%s: %s", Variables.FUEL, ReikaDateHelper.getSecondsAsClock(time));
                this.sendMessage(ep, sg);
                return InteractionResult.SUCCESS;
            }
            if (te.getEngineType().requiresLubricant()) {
                int amt = te.getLube();
                String sg = String.format("Lubricant: %s", RotaryAux.formatLiquidAmount(amt));
                this.sendMessage(ep, sg);
                return InteractionResult.SUCCESS;
            }
            if (te.getEngineType().isWaterPiped()) {
                int amt = te.getWater();
                String sg = String.format("Water: %s", RotaryAux.formatLiquidAmount(amt));
                this.sendMessage(ep, sg);
                return InteractionResult.SUCCESS;
            }
//            if (te.getEngineType() == EngineType.HYDRO) {
//                if (((BlockEntityHydroEngine) te).isStreamPowered()) {
//                    for (String sg : ((BlockEntityHydroEngine) te).getStreamData()) {
//                        this.sendMessage(ep, sg);
//                    }
//                } else {
//                    String sg = String.format("Detected waterfall height: %s", RotaryAux.formatDistance(((BlockEntityHydroEngine) te).getWaterfallHeightForDisplay()));
//                    this.sendMessage(ep, sg);
//                }
//            }
        } else if (tile instanceof ShaftPowerEmitter sp) {
            this.sendMessage(ep, String.format("%s producing %s", name, RotaryAux.formatPowerIO(sp)));
            return InteractionResult.SUCCESS;
        }

        if (tile instanceof BlockEntityPowerReceiver) {
            BlockEntityPowerReceiver te = (BlockEntityPowerReceiver) tile;
            long power = te.power;
            this.sendMessage(ep, String.format("%s receiving %s", name, RotaryAux.formatPowerIO(te)));
            if (power < te.MINPOWER)
                this.sendLocalizedMessage(ep, "minpower");
            if (power < te.MINSPEED)
                this.sendLocalizedMessage(ep, "mintorque");
            if (power < te.MINTORQUE)
                this.sendLocalizedMessage(ep, "minspeed");
        } else if (tile instanceof ShaftPowerReceiver sp) {
            this.sendMessage(ep, String.format("%s receiving %s", name, RotaryAux.formatPowerIO(sp)));
            return InteractionResult.SUCCESS;
        }

        if (m == MachineRegistry.DISTRIBCLUTCH) {
            BlockEntityDistributionClutch te = (BlockEntityDistributionClutch) tile;
            Direction read = te.getInputDirection();
            if (read != null) {
                String sname = ReikaStringParser.capFirstChar(read.name());
                long pwr = (long) te.getInputTorque() * te.omega;
                this.sendMessage(ep, String.format("Input side %s receiving %s", sname, RotaryAux.formatPowerIO(te.omega, pwr)));
                return InteractionResult.SUCCESS;
            }
            for (int i = 0; i < 4; i++) {
                Direction dir = Direction.values()[i + 2];
                if (te.isOutputtingToSide(dir)) {
                    String sname = ReikaStringParser.capFirstChar(dir.name());
                    long out = (long) te.getTorqueToSide(dir) * te.omega;

                    if (dir == read.getOpposite()) {
                        this.sendMessage(ep, String.format("Output to side %s: %s (leftover)", sname, RotaryAux.formatPowerIO(te.omega, out)));
                        return InteractionResult.SUCCESS;
                    } else {
                        int req = te.getTorqueRequest(dir);
                        double base = ReikaMathLibrary.getThousandBase(req);
                        String pre = ReikaEngLibrary.getSIPrefix(req);
                        this.sendMessage(ep, String.format("Output to side %s: %s (requested %.3f%sNm)", sname, RotaryAux.formatPowerIO(te.omega, out), base, pre));
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        } else if (m == MachineRegistry.SPLITTER) {
            BlockEntitySplitter te = (BlockEntitySplitter) tile;
            if (te.isSplitting()) {
                String sname = ReikaStringParser.capFirstChar(te.getReadDirection().name());
                long pwr = (long) te.torque * te.omega;
                this.sendMessage(ep, String.format("Input side %s receiving %s", sname, RotaryAux.formatPowerIO(te.omega, pwr)));

                sname = ReikaStringParser.capFirstChar(te.getWriteDirection().name());
                long out = (long) te.torqueOut1 * te.omega;
                this.sendMessage(ep, String.format("Output to side %s: %s", sname, RotaryAux.formatPowerIO(te.omega, out)));

                sname = ReikaStringParser.capFirstChar(te.getWriteDirection2().name());
                out = (long) te.torqueOut2 * te.omega;
                this.sendMessage(ep, String.format("Output to side %s: %s", sname, RotaryAux.formatPowerIO(te.omega, out)));
                return InteractionResult.SUCCESS;
            } else {
                String sname = ReikaStringParser.capFirstChar(te.getReadDirection().name());
                long pwr = (long) te.getInputTorque1() * te.getInputSpeed1();
                this.sendMessage(ep, String.format("Input side %s receiving %s", sname, RotaryAux.formatPowerIO(te.omega, pwr)));

                pwr = (long) te.getInputTorque2() * te.getInputSpeed2();
                sname = ReikaStringParser.capFirstChar(te.getReadDirection().name());
                this.sendMessage(ep, String.format("Input side %s receiving %s", sname, RotaryAux.formatPowerIO(te.omega, pwr)));

                sname = ReikaStringParser.capFirstChar(te.getWriteDirection().name());
                long out = (long) te.torque * te.omega;
                this.sendMessage(ep, String.format("Output to side %s: %s", sname, RotaryAux.formatPowerIO(te.omega, out)));
            }

            if (te.isBedrock()) {
                this.sendMessage(ep, String.format("%s is upgraded.", te.getName()));
                return InteractionResult.SUCCESS;
            }
        } else if (tile instanceof BlockEntityTransmissionMachine) {
            BlockEntityTransmissionMachine te = (BlockEntityTransmissionMachine) tile;
            this.sendMessage(ep, String.format("%s transmitting %s", name, RotaryAux.formatPowerIO(te)));
            return InteractionResult.SUCCESS;
        }

        if (tile instanceof RCToModConverter) {
            RCToModConverter te = (RCToModConverter) tile;
            int units = te.getGeneratedUnitsPerTick();
            String unit = te.getUnitDisplay();
            this.sendMessage(ep, String.format("Generating %d %s/t", units, unit));
            return InteractionResult.SUCCESS;
        }

        if (tile instanceof EnergyToPowerBase) {
            EnergyToPowerBase te = (EnergyToPowerBase) tile;
            int units = te.getConsumedUnitsPerTick();
            String unit = te.getUnitDisplay();
            this.sendMessage(ep, String.format("%s Outputting %s", name, RotaryAux.formatPowerIO(te)));
            this.sendMessage(ep, String.format("Consuming %d %s/t", units, unit));
            return InteractionResult.SUCCESS;
        }

        if (tile instanceof BlockEntityPiping) {
            BlockEntityPiping te = (BlockEntityPiping) tile;
            Fluid f = te.getAttributes();
            if (f != null) {
                this.sendMessage(ep, String.format("Pipe has %s of %s", RotaryAux.formatLiquidAmount(te.getFluidLevel()), ForgeRegistries.FLUIDS.getKey(f).getNamespace()));
                return InteractionResult.SUCCESS;
            } else {
                this.sendMessage(ep, "Pipe is empty.");
                return InteractionResult.SUCCESS;
            }
        }

        if (tile instanceof BlockEntitySpringPowered) {
            BlockEntitySpringPowered te = (BlockEntitySpringPowered) tile;
            this.sendMessage(ep, String.format("Remaining charge: %.2fs", te.getExpectedCoilLife() / 20D));
            return InteractionResult.SUCCESS;
        }

//        if (m == MachineRegistry.GPR) {
//            BlockEntityGPR te = (BlockEntityGPR) tile;
//            if (ep.isCrouching() && te.power > te.MINPOWER) {
//                double ratio = 100 * te.getSpongy(world, pos.below());
//                this.sendMessage(ep, String.format("The ground is %.3f%s caves here", ratio, "%%"));
//            }
//        }
//        if (m == MachineRegistry.BLASTFURNACE) {
//            BlockEntityBlastFurnace te = (BlockEntityBlastFurnace) tile;
//            no way to say "too cold for recipe"
//        }
//        if (m == MachineRegistry.PLAYERDETECTOR) {
//            BlockPlayerDetector te = (BlockPlayerDetector) tile;
//            this.sendMessage(ep, String.format("Reaction Time: %.2fs", te.getReactionTime() / 20F));
//        }
//        if (m == MachineRegistry.SOLARTOWER) {
//            BlockEntitySolar te = (BlockEntitySolar) tile;
//            BlockEntitySolar top = (BlockEntitySolar) world.getBlockEntity(new BlockPos(pos.getX(), te.getTopOfTower(), pos.getZ()));
//            BlockEntitySolar bottom = (BlockEntitySolar) world.getBlockEntity(new BlockPos(pos.getX(), te.getBottomOfTower(), pos.getZ()));
//            if (bottom.getPlant() == null || bottom.getArraySize() <= 0) {
//                this.sendMessage(ep, String.format("Solar plant is unformed"));
//            } else {
//                this.sendMessage(ep, String.format("Solar plant contains %d mirrors and %d active tower pieces (out of %d total)", top.getArraySize(), bottom.getPlant().getEffectiveTowerBlocks(), bottom.getPlant().rawTowerBlocks()));
//                this.sendMessage(ep, String.format("Outputting %s; Efficiency %.1f%s", RotaryAux.formatPowerIO(bottom), bottom.getArrayOverallBrightness() * 100F, "%%"));
//            }
//        }
        if (m == MachineRegistry.HEATRAY) {
            BlockEntityHeatRay te = (BlockEntityHeatRay) tile;
            if (te.power >= te.MINPOWER)
                this.sendMessage(ep, String.format("Dealing %ds of burn damage", te.getBurnTime()));
            return InteractionResult.SUCCESS;
        }
        if (m == MachineRegistry.PUMP) {
            BlockEntityPump te = (BlockEntityPump) tile;
            if (te.power >= te.MINPOWER && te.duplicationAmount > 1)
                this.sendMessage(ep, String.format("Duplicating water with a factor of %dx", te.duplicationAmount));
            return InteractionResult.SUCCESS;
        }
//        if (m == MachineRegistry.ADVANCEDGEARS) {
//            BlockEntityAdvancedGear te = (BlockEntityAdvancedGear) tile;
//            if (te.getGearType() == BlockEntityAdvancedGear.GearType.WORM && te.power > 0) {
//                this.sendMessage(ep, String.format("Power Loss: %.2f%s", te.getCurrentLoss(), "%%"));
//            } else if (te.getGearType().storesEnergy()) {
//                long energy = te.getEnergy() / 20;
//                this.sendMessage(ep, String.format("Stored Energy: %s", RotaryAux.formatEnergy(energy)));
//            }
//        }
        if (m == MachineRegistry.BEVELGEARS) {
            BlockEntityBevelGear te = (BlockEntityBevelGear) tile;
            this.sendMessage(ep, String.format("Output side: %s", ReikaStringParser.capFirstChar(te.getWriteDirection().name())));
            return InteractionResult.SUCCESS;
        }
//        if (m == MachineRegistry.BORER) {
//            BlockEntityBorer te = (BlockEntityBorer) tile;
//            this.sendMessage(ep, String.format("%s head at %d, %d", te.getName(), te.getHeadX(), te.getHeadZ()));
//            if (te.isJammed())
//                this.sendMessage(ep, String.format("%s is jammed, supply more torque or power!", te.getName()));
//        }
//        if (m == MachineRegistry.BELT || m == MachineRegistry.CHAIN) {
//            BlockEntityBeltHub te = (BlockEntityBeltHub) tile;
//            if (te.isSplitting()) {
//                this.sendMessage(ep, "Belt is splitting power");
//            }
//            if (te.isSlipping()) {
//                this.sendMessage(ep, "Belt is slipping and wasting power!");
//            }
//        }
//        if (m == MachineRegistry.DYNAMO) {
//            BlockEntityDynamo te = (BlockEntityDynamo) tile;
//            if ((te.torque > (te.isUpgraded() ? BlockEntityDynamo.MAXTORQUE_UPGRADE : BlockEntityDynamo.MAXTORQUE) || te.omega > BlockEntityDynamo.MAXOMEGA))
//                this.sendMessage(ep, "Conversion limits exceeded; Power is being wasted");
//        }
        if (m == MachineRegistry.GEARBOX) {
            BlockEntityGearbox te = (BlockEntityGearbox) tile;
            this.sendMessage(ep, String.format("Gearbox %d percent damaged; Lubricant Levels at %s", (int) (100 * (1 - ReikaMathLibrary.doubpow(0.99, te.getDamage()))), RotaryAux.formatLiquidAmount(te.getLubricant())));
            return InteractionResult.SUCCESS;
        }
//        if (m == MachineRegistry.FRACTIONATOR) {
//            BlockEntityFractionator te = (BlockEntityFractionator) tile;
//            if (te.power >= te.MINPOWER && te.omega >= te.MINSPEED)
//                this.sendMessage(ep, String.format("Efficiency: %.3f%%%%", te.getYieldRatio() * 100));
//        }

//        if (m == null && tile instanceof IFluidHandler) {
//            FluidTankInfo[] info = ((IFluidHandler) tile).getTankInfo(Direction.values()[s]);
//            StringBuilder sb = new StringBuilder();
//            if (info != null) {
//                for (int i = 0; i < info.length; i++) {
//                    if (info[i] != null) {
//                        sb.append("Tank " + i + ": ");
//                        FluidStack fs = info[i].fluid;
//                        if (fs != null && fs.getFluid() != null)
//                            sb.append(RotaryAux.formatLiquidAmount(fs.getAmount()) + " of " + fs.getFluid().getRegistryName() + "/" + RotaryAux.formatLiquidAmount(info[i].capacity) + " Capacity");
//                        else
//                            sb.append("Empty");
//                        sb.append("\n");
//                    }
//                }
//            } else {
//                sb.append("No Tank Data");
//            }
//
//            this.sendMessage(ep, sb.toString());
//        }

        return InteractionResult.FAIL;
    }

    private void sendLocalizedMessage(Player ep, String s) {
        this.sendMessage(ep, I18n.get("message." + s));
    }

    private void sendMessage(Player ep, String s) {
        //ReikaChatHelper.writeString(s);
        ReikaChatHelper.sendChatToPlayer(ep, s);
    }

    private void sendMessages(Player ep, Collection<String> s) {
        for (String sg : s) {
            this.sendMessage(ep, sg);
        }
    }
}
