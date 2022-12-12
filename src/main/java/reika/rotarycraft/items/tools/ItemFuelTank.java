/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
/*package reika.rotarycraft.items.tools;

import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.blockentities.processing.BlockEntityPulseFurnace;
import reika.rotarycraft.blockentities.Storage.BlockEntityReservoir;
import reika.rotarycraft.registry.EngineType;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryItems;
import reika.dragonapi.libraries.ReikaNBTHelper;
import reika.rotarycraft.api.Interfaces.Fillable;
import reika.rotarycraft.base.ItemRotaryTool;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class ItemFuelTank extends ItemRotaryTool implements Fillable {

    private static final ArrayList<Fluid> creativeFluids = new ArrayList<>();

    public ItemFuelTank() {
        super(new Properties().tab(RotaryCraft.ROTARY_TOOLS));
    }

    private static void addCreativeFluid(String name) {
        Fluid f = Fluids.getFluid(name);
        if (f != null)
            creativeFluids.add(f);
    }

    public static void initCreativeFluids() {
        creativeFluids.clear();
        addCreativeFluid("fuel");
        addCreativeFluid("rc jet fuel");
        addCreativeFluid("rc ethanol");
        addCreativeFluid("bioethanol");
        addCreativeFluid("biofuel");
    }

    @Override
    public boolean isValidFluid(Fluid f, ItemStack is) {
        return is.getTag() == null || f.equals(ReikaNBTHelper.getFluidFromNBT(is.getTag()));
    }

    @Override
    public int getCapacity(ItemStack is) {
        return 16000;
    }

    @Override
    public int getCurrentFillLevel(ItemStack is) {
        return is.getTag() != null ? is.getTag().getInt("fuel") : 0;
    }

    @Override
    public int addFluid(FluidStack fs) {
        int fuel = 0;
        if (!this.isValidFluid(fs)) {
            return 0;
        }
        if (fs.getTag() == null) {
            fs.put(new CompoundTag());
            ReikaNBTHelper.writeFluidToNBT(fs.getTag(), fs);
        } else {
            fuel = fs.getTag().getInt("fuel");
            if (!fs.equals(ReikaNBTHelper.getFluidFromNBT(fs.getTag()))) {
                return 0;
            }
        }
        int toadd = Math.min(amt, this.getCapacity(is) - fuel);
        is.getTag().putInt("fuel", fuel + toadd);
        return toadd;
    }

    public boolean isValidFluid(FluidStack f) {
        if (f == null)
            return false;
        if (f.equals(Fluids.getFluid("fuel")))
            return true;
        if (f.equals(Fluids.getFluid("rc ethanol")))
            return true;
        if (f.equals(RotaryFluids.JET_FUEL))
            return true;
        if (f.equals(Fluids.getFluid("bioethanol")))
            return true;
        return f.equals(Fluids.getFluid("biofuel"));
    }

    
    private String getDisplayTag(CompoundTag nbt) {
        Fluid f = ReikaNBTHelper.getFluidFromNBT(nbt);
        String fluid = f != null ? f.getRegistryName().toString() : "Null Fluid";
        int amt = nbt.getInt("fuel");
        String amount = String.format("%d", amt);
        return "Contents: " + amount + " mB of " + fluid;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, Level world, Player ep) {
        if (is.getTag() != null) {
            Fluid f = ReikaNBTHelper.getFluidFromNBT(is.getTag());
            int amt = this.getCurrentFillLevel(is);
            int slot = ReikaInventoryHelper.locateIDInInventory(RotaryItems.JETPACK.get(), ep.inventory);
            if (slot == -1) {
                slot = ReikaInventoryHelper.locateIDInInventory(RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get(), ep.inventory);
            }
            if (slot == -1) {
                slot = ReikaInventoryHelper.locateIDInInventory(RotaryItems.HSLA_CHESTPLATE.get(), ep.inventory);
            }
            if (slot != -1) {
                ItemStack jet = ep.inventory.getStackInSlot(slot);
                ItemJetPack item = (ItemJetPack) jet.getItem();
                int fuel = this.getCurrentFillLevel(is);
                int added = item.addFluid(jet, f, fuel);
                int newfuel = fuel - added;
                is.getTag().putInt("fuel", newfuel);
                if (newfuel <= 0)
                    is.put(null);
            }
        }
        return is;
    }

    private void removeFuel(ItemStack is, int amt) {
        int newfuel = this.getCurrentFillLevel(is) - amt;
        if (newfuel > 0)
            is.getTag().putInt("fuel", newfuel);
        else
            is.put(null);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        MachineRegistry m = MachineRegistry.getMachine(world, pos);
        BlockEntity tile = world.getBlockEntity(pos);
        if (m == MachineRegistry.ENGINE) {
            BlockEntityEngine te = (BlockEntityEngine) tile;
            EngineType eng = te.getEngineType();
            Fluid f = this.getCurrentFluid(is);
            if (f != null) {
                int amt = Math.min(this.getCurrentFillLevel(is), te.getFuelCapacity() - te.getFuelLevel());
                if (amt > 0) {
                    boolean flag = false;
                    if (eng.isJetFueled() && f.equals(RotaryFluids.JET_FUEL)) {
                        te.addFuel(amt);
                        flag = true;
                    } else if (eng.isEthanolFueled() && f.equals(Fluids.getFluid("rc ethanol"))) {
                        te.addFuel(amt);
                        flag = true;
                    }
                    if (flag) {
                        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, te, "fuel");
                        this.removeFuel(is, amt);
                        return true;
                    }
                }
            } else {
                int amt = Math.min(this.getCapacity(is), te.getFuelLevel());
                if (amt > 0) {
                    boolean flag = false;
                    if (eng.isJetFueled()) {
                        this.addFluid(is, RotaryFluids.JET_FUEL, amt);
                        flag = true;
                    } else if (eng.isEthanolFueled()) {
                        this.addFluid(is, Fluids.getFluid("rc ethanol"), amt);
                        flag = true;
                    }
                    if (flag) {
                        te.subtractFuel(amt);
                        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, te, "fuel");
                        return true;
                    }
                }
            }
        }
        if (m == MachineRegistry.FUELENGINE) {
            BlockEntityFuelEngine te = (BlockEntityFuelEngine) tile;
            Fluid f = this.getCurrentFluid(is);
            if (f == null) {
                int amt = Math.min(this.getCapacity(is), te.getFuelLevel());
                if (amt > 0) {
                    this.addFluid(is, Fluids.getFluid("fuel"), amt);
                    te.removeFuel(amt);
                    return true;
                }
            } else if (f.equals(Fluids.getFluid("fuel"))) {
                int amt = Math.min(this.getCurrentFillLevel(is), BlockEntityFuelEngine.CAPACITY - te.getFuelLevel());
                if (amt > 0) {
                    te.addFuel(amt);
                    ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, te, "tank");
                    this.removeFuel(is, amt);
                    return true;
                }
            }
        }
        if (m == MachineRegistry.PULSEJET) {
            BlockEntityPulseFurnace te = (BlockEntityPulseFurnace) tile;
            Fluid f = this.getCurrentFluid(is);
            if (f == null) {
                int amt = Math.min(this.getCapacity(is), te.getFuel());
                if (amt > 0) {
                    this.addFluid(is, RotaryFluids.JET_FUEL, amt);
                    te.removeFuel(amt);
                    ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, te, "fuel");
                    return true;
                }
            } else if (f.equals(RotaryFluids.JET_FUEL)) {
                int amt = Math.min(this.getCurrentFillLevel(is), BlockEntityPulseFurnace.MAXFUEL - te.getFuel());
                if (amt > 0) {
                    te.addFuel(amt);
                    ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, te, "fuel");
                    this.removeFuel(is, amt);
                    return true;
                }
            }
        }
        if (m == MachineRegistry.RESERVOIR) {
            BlockEntityReservoir te = (BlockEntityReservoir) tile;
            Fluid f = this.getCurrentFluid(is);
            Fluid f2 = te.getFluid();
            if (f != null) {
                int amt = Math.min(this.getCurrentFillLevel(is), BlockEntityReservoir.CAPACITY - te.getLevel());
                if (amt > 0 && te.canAcceptFluid(f)) {
                    te.addLiquid(amt, f);
                    ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, te, "tank");
                    this.removeFuel(is, amt);
                    return true;
                }
            } else {
                if (this.isValidFluid(f2)) {
                    int amt = Math.min(this.getCapacity(is), te.getLevel());
                    if (amt > 0) {
                        te.removeLiquid(amt);
                        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, te, "tank");
                        this.addFluid(is, f2, amt);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isFull(ItemStack is) {
        return this.getCurrentFillLevel(is) >= this.getCapacity(is);
    }

    @Override
    public Fluid getCurrentFluid(ItemStack is) {
        return is.getTag() != null ? ReikaNBTHelper.getFluidFromNBT(is.getTag()) : null;
    }

}
*/