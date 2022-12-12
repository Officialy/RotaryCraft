///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.processing;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import org.jetbrains.annotations.NotNull;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.auxiliary.RedstoneCycleTracker;
//import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
//import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
//import reika.rotarycraft.auxiliary.interfaces.MagnetizationCore;
//import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
//import reika.rotarycraft.items.tools.ItemEngineUpgrade.Upgrades;
//import reika.rotarycraft.registry.DurationRegistry;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.RotaryItems;
//
//public class BlockEntityMagnetizer extends InventoriedPowerReceiver implements DiscreteFunction, ConditionalOperation, MagnetizationCore {
//
//    private final RedstoneCycleTracker redstone = new RedstoneCycleTracker(3);
//    private boolean hasLodestone = false;
//
//    //    @Override
//    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//        return j == 0;
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.MAGNETIZER;
//    }
//
//    //    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        this.getIOSidesDefault(world, pos);
//        this.getPower(false);
//        if (power < MINPOWER) {
//            tickcount = 0;
//            return;
//        }
//        if (omega < MINSPEED) {
//            tickcount = 0;
//            return;
//        }
//        redstone.update(world, pos);
//        if (!redstone.isAlternating())
//            return;
//        tickcount++;
//        if (tickcount < this.getOperationTime())
//            return;
//        tickcount = 0;
//        if (inv[0] != null) {
//            MagnetizerRecipe r = RecipesMagnetizer.getRecipes().getRecipe(inv[0]);
//            if (r != null && this.canRunRecipe(r))
//                this.magnetize(r);
//        }
//    }
//
//    private boolean hasRecipe() {
//        return inv[0] != null && RecipesMagnetizer.getRecipes().getRecipe(inv[0]) != null;
//    }
//
//    private boolean canRunRecipe(MagnetizerRecipe r) {
//        int ms = r.minSpeed;
//        if (hasLodestoneUpgrade())
//            ms /= 2;
//        return omega >= ms && (r.allowStacking || inv[0].getCount() == 1);
//    }
//
//    private void magnetize(MagnetizerRecipe r) {
//        if (DragonAPI.rand.nextInt(r.timeFactor) > 0)
//            return;
//        ItemStack is = inv[0];
//        if (r.action != null) {
//            r.action.step(hasLodestoneUpgrade() ? omega * 2 : omega, inv[0]);
//        } else {
//            if (is.getTag() == null) {
//                is.put(new CompoundTag());
//                is.getTag().putInt("magnet", 1);
//            } else if (is.getTag().contains("magnet")) {
//                int m = is.getTag().getInt("magnet");
//                if (m < this.getMaxCharge(r))
//                    m++;
//                is.getTag().putInt("magnet", m);
//            } else {
//                is.getTag().putInt("magnet", 1);
//            }
//        }
//    }
//
//    private int getMaxCharge(MagnetizerRecipe r) {
//        return omega / r.speedPeruT;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int slot, ItemStack is) {
//        MagnetizerRecipe rec = RecipesMagnetizer.getRecipes().getRecipe(is);
//        return rec != null && ((inv[0] == null && is.getCount() == 1) || rec.allowStacking);
//    }
//
//    //    @Override
//    public int getRedstoneOverride() {
//        if (!this.hasRecipe())
//            return 15;
//        return 0;
//    }
//
//    @Override
//    public int getOperationTime() {
//        int base = DurationRegistry.MAGNETIZER.getOperationTime(omega);
//        return this.hasLodestoneUpgrade() ? base / 2 : base;
//    }
//
//    @Override
//    public boolean areConditionsMet() {
//        return this.hasRecipe();
//    }
//
//    @Override
//    public String getOperationalStatus() {
//        return this.areConditionsMet() ? "Operational" : "No Shaft Core";
//    }
//
//    @Override
//    public int getCoreMagnetization() {
//        return inv[0] != null && inv[0].getTag() != null ? inv[0].getTag().getInt("magnet") : 0;
//    }
//
//    @Override
//    public void addRedstoneUpgrade() {
//        redstone.addIntegrated();
//    }
//
//    @Override
//    public boolean hasRedstoneUpgrade() {
//        return redstone.hasIntegrated();
//    }
//
//    @Override
//    public boolean hasRedstoneSignal() {
//        return false;
//    }
//
//    public void addLodestoneUpgrade() {
//        hasLodestone = true;
//    }
//
//    public boolean hasLodestoneUpgrade() {
//        return hasLodestone;
//    }
//
//    @Override
//    public void upgrade(ItemStack is) {
//        switch (Upgrades.list[is.getItemDamage()]) {
//            case REDSTONE -> this.addRedstoneUpgrade();
//            case LODESTONE -> this.addLodestoneUpgrade();
//            default -> {
//            }
//        }
//    }
//
//    @Override
//    public boolean canUpgradeWith(ItemStack item) {
//        if (RotaryItems.UPGRADE.matchItem(item)) {
//            return switch (Upgrades.list[item.getItemDamage()]) {
//                case REDSTONE -> !this.hasRedstoneUpgrade();
//                case LODESTONE -> !this.hasLodestoneUpgrade();
//                default -> false;
//            };
//        }
//        return false;
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//
//        NBT.putBoolean("redstoneUpgrade", redstone.hasIntegrated());
//        NBT.putBoolean("lodestoneUpgrade", hasLodestone);
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        redstone.reset();
//        if (NBT.getBoolean("redstoneUpgrade"))
//            redstone.addIntegrated();
//        hasLodestone = NBT.getBoolean("lodestoneUpgrade");
//    }
//
//    @Override
//    public void breakBlock() {
//        if (this.hasRedstoneUpgrade()) {
//            ReikaItemHelper.dropItem(level, worldPosition, RotaryItems.UPGRADE.getStackOfMetadata(Upgrades.REDSTONE.ordinal()));
//        }
//        if (this.hasLodestoneUpgrade()) {
//            ReikaItemHelper.dropItem(level, worldPosition, RotaryItems.UPGRADE.getStackOfMetadata(Upgrades.LODESTONE.ordinal()));
//        }
//    }
//
//    public boolean hasCore() {
//        return ReikaItemHelper.matchStacks(inv[0], RotaryItems.HSLA_SHAFT_CORE) || ReikaItemHelper.matchStacks(inv[0], RotaryItems.TUNGSTEN_ALLOY_SHAFT_CORE);
//    }
//
//    @Override
//    public int getSlots() {
//        return 0;
//    }
//
//    @NotNull
//    @Override
//    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
//        return null;
//    }
//
//    @NotNull
//    @Override
//    public ItemStack extractItem(int slot, int amount, boolean simulate) {
//        return null;
//    }
//
//    @Override
//    public int getSlotLimit(int slot) {
//        return 0;
//    }
//
//    @Override
//    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
//        return false;
//    }
//
//    @Override
//    public String getName() {
//        return null;
//    }
//
//    @Override
//    public boolean hasAnInventory() {
//        return false;
//    }
//
//    @Override
//    public boolean hasATank() {
//        return false;
//    }
//}
