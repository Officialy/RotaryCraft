/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.level;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import reika.dragonapi.instantiable.ItemMatch;
import reika.dragonapi.instantiable.storage.ManagedItemHandler;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
import reika.rotarycraft.gui.container.machine.inventory.ContainerIgniter;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * Firestarter: loaded with a fuel (wood/coal/blaze/lava/thermite, each with a target temperature), it
 * friction-heats from shaft power toward that fuel's temperature and radiates heat over a
 * torque-scaled area — igniting flammables and, once hot enough, setting nearby mobs on fire. Port of
 * the legacy {@code TileEntityIgniter}; the mod-heavy environmental temperature bonuses are dropped.
 */
public class BlockEntityIgniter extends InventoriedPowerReceiver implements TemperatureTE, RangedEffect, ConditionalOperation {

    public static final int ANIMALIGNITION = 280;
    public static final int MAXTEMP = 2500;
    private static final int AMBIENT = 20;

    private int temperature = AMBIENT;
    private IgnitionFuel fuel;

    public BlockEntityIgniter(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.IGNITER.get(), pos, state);
    }

    @Override
    public int getContainerSize() {
        return 18;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.IGNITER;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.IGNITER.get();
    }

    @Override
    protected String getTEName() {
        return "igniter";
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
    }

    public boolean isItemValidForSlot(int i, ItemStack is) {
        for (IgnitionFuel f : IgnitionFuel.fuelList)
            for (ItemMatch m : f.items)
                if (m.match(is))
                    return true;
        return false;
    }

    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return false;
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        tickcount++;
        this.getSummativeSidedPower();
        if (power < MINPOWER || omega < MINSPEED)
            return;

        if (tickcount >= 40) {
            this.updateTemperature(world, pos);
            tickcount = 0;
            fuel = IgnitionFuel.getFromItems(itemHandler);
            if (fuel != null && temperature < fuel.temperature)
                this.burnFuel();
        }
        if (fuel == null || world.isClientSide())
            return;

        int spread = this.getRange();
        int yspread = Math.max(1, spread / 2);
        int n = 1 + temperature / 50;
        for (int i = 0; i < n; i++) {
            int fx = pos.getX() + world.getRandom().nextInt(spread * 2 + 1) - spread;
            int fy = pos.getY() + world.getRandom().nextInt(yspread * 2 + 1) - yspread;
            int fz = pos.getZ() + world.getRandom().nextInt(spread * 2 + 1) - spread;
            ReikaWorldHelper.temperatureEnvironment(world, new BlockPos(fx, fy, fz), temperature);
        }
        if (temperature >= ANIMALIGNITION) {
            AABB box = new AABB(pos).inflate(spread, yspread, spread);
            for (LivingEntity e : world.getEntitiesOfClass(LivingEntity.class, box))
                e.igniteForSeconds(1);
        }
    }

    private void burnFuel() {
        for (ItemMatch m : fuel.items) {
            for (int i = 0; i < this.getContainerSize(); i++) {
                if (m.match(itemHandler.getStackInSlot(i))) {
                    itemHandler.extractItem(i, 1, false);
                    break;
                }
            }
        }
        if (temperature < fuel.temperature)
            temperature += (fuel.temperature - temperature) / 4;
    }

    // --- TemperatureTE (full contract) --------------------------------------

    @Override
    public void updateTemperature(Level world, BlockPos pos) {
        if (temperature > AMBIENT)
            temperature -= (int) Math.max(1, Math.log(temperature - AMBIENT));
        if (temperature > MAXTEMP)
            temperature = MAXTEMP;
    }

    @Override
    public void addTemperature(int temp) {
        temperature += temp;
    }

    @Override
    public int getTemperature() {
        return temperature;
    }

    @Override
    public void setTemperature(int T) {
        temperature = T;
    }

    @Override
    public int getThermalDamage() {
        return temperature >= ANIMALIGNITION ? 2 : 0;
    }

    @Override
    public void overheat(Level world, BlockPos pos) {
    }

    @Override
    public int getMaxTemperature() {
        return MAXTEMP;
    }

    @Override
    public int getAmbientTemperature() {
        return AMBIENT;
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
    public boolean allowHeatExtraction() {
        return false;
    }

    public int getTemperatureScaled(int a) {
        return a * temperature / MAXTEMP;
    }

    // --- range / status ------------------------------------------------------

    @Override
    public int getRange() {
        return this.getMaxRange();
    }

    @Override
    public int getMaxRange() {
        return 2 + (int) ReikaMathLibrary.logbase(torque + 1, 2);
    }

    @Override
    public boolean areConditionsMet() {
        return IgnitionFuel.getFromItems(itemHandler) != null;
    }

    @Override
    public String getOperationalStatus() {
        return this.areConditionsMet() ? "Operational" : "No Fuel";
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    public boolean hasAnInventory() {
        return true;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        temperature = NBT.getIntOr("temp", AMBIENT);
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putInt("temp", temperature);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new ContainerIgniter(id, inv, this);
    }

    // --- fuel enum -----------------------------------------------------------

    public enum IgnitionFuel {
        WOOD(400, "plankWood", "logWood"),
        COAL(600, Items.COAL),
        BLAZE(800, Items.BLAZE_POWDER),
        LAVA(1200, Items.LAVA_BUCKET),
        THERMITE(2500, "dustAluminum", "ingotIron");

        public static final IgnitionFuel[] fuelList = values();

        public final int temperature;
        public final Collection<ItemMatch> items = new ArrayList<>();

        IgnitionFuel(int t, Object... matchers) {
            temperature = t;
            for (Object o : matchers)
                items.add(ItemMatch.createFromObject(o));
        }

        /** Highest-temperature fuel whose required items are ALL present in the handler; null if none. */
        static IgnitionFuel getFromItems(ManagedItemHandler inv) {
            for (int i = fuelList.length - 1; i >= 0; i--) {
                IgnitionFuel f = fuelList[i];
                boolean valid = true;
                for (ItemMatch m : f.items) {
                    boolean found = false;
                    for (int k = 0; k < inv.getSlots(); k++) {
                        if (m.match(inv.getStackInSlot(k))) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        valid = false;
                        break;
                    }
                }
                if (valid)
                    return f;
            }
            return null;
        }
    }
}
