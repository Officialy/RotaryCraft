/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.libraries.java.ReikaRandomHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.api.interfaces.TensionStorage;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
import reika.rotarycraft.auxiliary.interfaces.SimpleProvider;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
import reika.rotarycraft.registry.DifficultyEffects;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryItems;

public class BlockEntityWinder extends InventoriedPowerReceiver implements SimpleProvider, DiscreteFunction, ConditionalOperation {

    //Whether in wind or unwind mode
    public boolean winding = true;

    public BlockEntityWinder(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.WINDER.get(), pos, state);
    }

    public final int getUnwindTorque() {
        if (inv[0] == null)
            return 0;
        return 8 * ((TensionStorage) inv[0].getItem()).getPowerScale(inv[0]);
    }

    public final int getUnwindSpeed() {
        if (inv[0] == null)
            return 0;
        return 1024 * ((TensionStorage) inv[0].getItem()).getPowerScale(inv[0]);
    }

    //    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return j == 0;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
//        this.getIOSidesDefault(world, pos);
        this.getPower(false);
        if (!winding) {
            write = read;
            read = null;
        }
        tickcount++;
        if (inv[0] == null) {
            if (!winding) {
                torque = 0;
                omega = 0;
            }
            return;
        }
        if (!(inv[0].getItem() instanceof TensionStorage)) {
            if (!winding) {
                torque = 0;
                omega = 0;
            }
            return;
        }
        if (winding) {
            if (tickcount < this.getOperationTime())
                return;
            tickcount = 0;
            if (inv[0].getTag().getInt("energy") >= this.getMaxWind())
                return;
            inv[0].getTag().putInt("energy", inv[0].getTag().getInt("energy") - 1); // = new ItemStack(inv[0].getItem(), 1, inv[0].getTag().getInt("energy") + 1); todo testme
            if (!world.isClientSide && this.breakCoil()) {
                inv[0] = null;
//                world.playLocalSound(pos, "DragonAPI.rand.break", 1F, 1F);
                world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1F, 1F, true);
            }
        } else {
            if (inv[0].getTag().getInt("energy") <= 0) {
                omega = 0;
                torque = 0;
                power = 0;
                return;
            }
            omega = this.getUnwindSpeed();
            torque = this.getUnwindTorque();
            power = (long) omega * (long) torque;
            if (tickcount < this.getUnwindTime())
                return;
            tickcount = 0;
            inv[0].getTag().putInt("energy", inv[0].getTag().getInt("energy") - 1); //todo testme
        }

    }

    protected final int getUnwindTime() {
        ItemStack is = inv[0];
        int base = 20;
        return base * ((TensionStorage) is.getItem()).getStiffness(is);
    }

    private boolean breakCoil() {
        ItemStack is = inv[0];
        if (is == null)
            return false;
        if (!(is.getItem() instanceof TensionStorage))
            return false;
        TensionStorage ts = (TensionStorage) is.getItem();
        if (!ts.isBreakable(is))
            return false;
        int dmg = inv[0].getTag().getInt("energy");
        double diff = dmg / 65536D * DifficultyEffects.BREAKCOIL.getDouble();
        boolean rand = ReikaRandomHelper.doWithChance(diff);
        return rand;
    }

    public int getOperationTime() {
        if (inv[0] == null)
            return 1;
        int base = (int) ReikaMathLibrary.logbase(inv[0].getTag().getInt("energy"), 2);
        double factor = 1D / (int) (ReikaMathLibrary.logbase(omega + 1, 2));
        return (int) (base * factor);
    }

    public int getMaxWind() {
        if (inv[0] == null)
            return 0;
        if (!(inv[0].getItem() instanceof TensionStorage))
            return 0;
        Item id = inv[0].getItem();
        int max = torque / ((TensionStorage) inv[0].getItem()).getStiffness(inv[0]);
//        if (max > RotaryItems.HSLA_STEEL_SPRING.getNumberMetadatas()) //technical limit
//            return RotaryItems.HSLA_STEEL_SPRING.getNumberMetadatas();todo technical limit
        return max;
    }

    public int getContainerSize() {
        return 1;
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        winding = NBT.getBoolean("winding");
    }

    @Override
    protected String getTEName() {
        return null;
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putBoolean("winding", winding);
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
        if (!this.isInWorld()) {
            phi = 0;
            return;
        }
        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
    }

    @Override
    public boolean canProvidePower() {
        return !winding;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.WINDER;
    }

    //    @Override
    public boolean isItemValidForSlot(int slot, ItemStack is) {
        if (is.getItem() == RotaryItems.HSLA_STEEL_SPRING.get())
            return true;
        return is.getItem() == RotaryItems.BEDROCK_ALLOY_SPRING.get();
    }

    @Override
    public int getRedstoneOverride() {
        if (inv[0] == null)
            return 15;
        if (inv[0].getItem() != RotaryItems.HSLA_STEEL_SPRING.get())
            return 15;
        if (inv[0].getDamageValue() >= torque && winding)
            return 15;
        return 0;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void onEMP() {
    }

    @Override
    public boolean areConditionsMet() {
        return inv[0] != null && inv[0].getItem() instanceof TensionStorage;
    }

    @Override
    public String getOperationalStatus() {
        return this.areConditionsMet() ? "Operational" : "No Coil";
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
        return true;
    }

    @Override
    public boolean hasATank() {
        return false;
    }
}
