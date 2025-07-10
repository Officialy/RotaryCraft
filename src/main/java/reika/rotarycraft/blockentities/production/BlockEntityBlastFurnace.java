/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/

package reika.rotarycraft.blockentities.production;

import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BlastFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import reika.dragonapi.interfaces.blockentity.XPProducer;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
import reika.rotarycraft.auxiliary.interfaces.FrictionHeatable;
import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
import reika.rotarycraft.auxiliary.recipemanagers.BlastFurnaceRecipe;
import reika.rotarycraft.base.blockentity.InventoriedRCBlockEntity;
import reika.rotarycraft.registry.*;

import java.util.Optional;

public class BlockEntityBlastFurnace extends InventoriedRCBlockEntity implements TemperatureTE, XPProducer, FrictionHeatable, DiscreteFunction, ConditionalOperation, Tickable {

    // The Inventory, last 3 for additives the first 9 for crafting
    private final ItemStackHandler itemHandler = new ItemStackHandler(12) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    //The output inventory, 3 for output
    private final ItemStackHandler outputInv = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private final LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.of(() -> itemHandler);
    private final LazyOptional<IItemHandler> lazyOutputInv = LazyOptional.of(() -> outputInv);
    protected final ContainerData data;
    public int progress = 0;
    private int maxProgress = 120; // Default is 6 seconds, multiplied by tier
    private int temperature;
    private static float minimumOperatingTemperature = 0f;
    private final float maximumTemperature = 2100.0f;
    public boolean leaveLastItem = false;

    public BlockEntityBlastFurnace(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.BLAST_FURNACE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> progress;
                    case 1 -> maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> progress = value;
                    case 1 -> maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public void tick() {
        if (hasRecipe(this) && temperature >= minimumOperatingTemperature) {
            progress++;
            RotaryCraft.LOGGER.info("Progress: " + progress);
            Level level = getLevel();
            SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                inventory.setItem(i, itemHandler.getStackInSlot(i));
            }

            Optional<BlastFurnaceRecipe> match = level.getRecipeManager().getRecipeFor(BlastFurnaceRecipe.Type.INSTANCE, inventory, level);

            if (match.isPresent()) {
                ItemStack output = match.get().getResultItem(RegistryAccess.EMPTY).copy();
                if (progress >= maxProgress) {
                    if (leaveLastItem) {
                        boolean canCraft = true; // set to false if any slot has less than 2 items
                        for (int i = 0; i < 10; i++) { // check first 10 slots
                            if (itemHandler.getStackInSlot(i).getCount() < 2) {
                                canCraft = false;
                                break; // no need to check other slots if one slot has less than 2 items
                            }
                        }
                        if (canCraft) {
                            // Craft the recipe and output the result
                            itemHandler.extractItem(0, match.get().getIngredients().size(), false);
                            for (int i = 1; i < 10; i++) {
                                itemHandler.extractItem(i, 1, false);
                            }
                            outputInv.insertItem(1, output, false);
                        }
                    } else {
                        // Craft the recipe and output the result
                        itemHandler.extractItem(0, match.get().getIngredients().size(), false);
                        for (int i = 1; i < 10; i++) {
                            itemHandler.extractItem(i, 1, false);
                        }
                        outputInv.insertItem(1, output, false);
                    }
                }
            }
        } else {
            progress = 0;
            setChanged();
        }
    }

    private static boolean hasRecipe(BlockEntityBlastFurnace blockEntity) {
        Level level = blockEntity.getLevel();
        if (level == null) {
            return false;
        }
        SimpleContainer inventory = new SimpleContainer(blockEntity.itemHandler.getSlots());
        SimpleContainer outputInventory = new SimpleContainer(blockEntity.outputInv.getSlots());
        for (int i = 0; i < blockEntity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, blockEntity.itemHandler.getStackInSlot(i));
        }

        Optional<BlastFurnaceRecipe> recipe = level.getRecipeManager().getRecipeFor(BlastFurnaceRecipe.Type.INSTANCE, inventory, level);
        recipe.ifPresent(blastFurnaceRecipe -> minimumOperatingTemperature = blastFurnaceRecipe.getOperatingTemperature());
        return recipe.isPresent() && canInsertAmountIntoOutputSlot(outputInventory) && canInsertItemIntoOutputSlot(outputInventory, recipe.get().getResultItem(RegistryAccess.EMPTY));
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack output) {
        return inventory.getItem(3).getItem() == output.getItem() || inventory.getItem(3).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(3).getMaxStackSize() > inventory.getItem(3).getCount();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("itemInventory"));
        outputInv.deserializeNBT(tag.getCompound("outputInventory"));
        temperature = tag.getInt("temperature");
        progress = tag.getInt("progress");
        leaveLastItem = tag.getBoolean("leaveLastItem");
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {

    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.BLASTFURNACE;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("inventory", itemHandler.serializeNBT());
        tag.put("outputInventory", outputInv.serializeNBT());
        tag.putInt("temperature", temperature);
        tag.putInt("progress", progress);
        tag.putBoolean("leaveLastItem", leaveLastItem);
    }

    @Override
    protected String getTEName() {
        return "blast_furnace";
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.BLAST_FURNACE.get();
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {

    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, blockEntity -> this.getUpdateTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        var tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

//    @Override
//    public void setTemperature(float temperature) {
//        this.temperature = temperature;
//    }

    @Override
    public int getTemperature() {
        return temperature;
    }

    @Override
    public int getMaxTemperature() {
        return 0;
    }

    @Override
    public void setTemperature(int temp) {

    }

    @Override
    public void onOverheat(Level world, BlockPos pos) {

    }

    @Override
    public boolean canBeFrictionHeated() {
        return false;
    }

    @Override
    public float getMultiplier() {
        return 0;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {

            return side == Direction.DOWN ? this.lazyOutputInv.cast() : this.lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return null;
//todo        return new BlastFurnaceMenu(id, inv, this, this.data);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Blast Furnace");
    }

    @Override
    public void clearXP() {

    }

    @Override
    public float getXP() {
        return 0;
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    @Override
    public void resetAmbientTemperatureTimer() {

    }

    @Override
    public boolean areConditionsMet() {
        return false;
    }

    @Override
    public String getOperationalStatus() {
        return null;
    }

    @Override
    public int getOperationTime() {
        return 0;
    }

    @Override
    public boolean canBeCooledWithFins() {
        return false;
    }

    @Override
    public boolean allowExternalHeating() {
        return false;
    }

    @Override
    public boolean allowHeatExtraction() {
        return false;
    }

    @Override
    public int getAmbientTemperature() {
        return 0;
    }

    @Override
    public void updateTemperature(Level world, BlockPos pos) {

    }

    @Override
    public void addTemperature(int temp) {

    }

    @Override
    public int getThermalDamage() {
        return 0;
    }

    @Override
    public void overheat(Level world, BlockPos pos) {

    }
}