/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.base.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.items.IItemHandler;
import reika.dragonapi.instantiable.storage.ManagedItemHandler;
import reika.dragonapi.libraries.ReikaInventoryHelper;

import java.util.Optional;

// TODO(1.21.9): drop `implements IItemHandler` once the cannon path stops relying on the
// legacy `te instanceof IItemHandler` resolution and migrates fully to ResourceHandler<ItemResource>.
// The inner {@link ManagedItemHandler} already implements the new API; the class-level marker
// is kept only so external code that still uses the legacy interface (auto-feeders etc.) finds it.
@SuppressWarnings("removal")
public abstract class BlockEntityInventoriedCannon extends BlockEntityAimedCannon implements IItemHandler, Container {

    public ManagedItemHandler itemHandler = new ManagedItemHandler(getContainerSize()) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public BlockEntityInventoriedCannon(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    //        @Override
    public final void setInventorySlotContents(int i, ItemStack itemstack) {
        itemHandler.setStackInSlot(i, itemstack);
    }

    @Override
    public final ItemStack getStackInSlot(int i) {
        return itemHandler.getStackInSlot(i);
    }

    public final ItemStack decrStackSize(int par1, int par2) {
        return ReikaInventoryHelper.decrStackSize(itemHandler, par1, par2);
    }


    public void openInventory() {
    }

    public void closeInventory() {
    }

    // 1.21.5: ValueOutput/ValueInput pattern. ManagedItemHandler#serialize/deserialize handles the
    // per-slot stack codec; we just wrap a CompoundTag through TagValueOutput/TagValueInput so we
    // can write it as a single "ItemsRaw" entry on the modern BE save API.
    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        TagValueOutput nested = TagValueOutput.createWithContext(ProblemReporter.DISCARDING,
                this.level == null ? RegistryAccess.EMPTY : this.level.registryAccess());
        itemHandler.serialize(nested);
        output.store("ItemsRaw", CompoundTag.CODEC, nested.buildResult());
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        itemHandler = new ManagedItemHandler(getContainerSize()) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
        Optional<CompoundTag> raw = input.read("ItemsRaw", CompoundTag.CODEC);
        if (raw.isPresent()) {
            ValueInput nested = TagValueInput.create(ProblemReporter.DISCARDING,
                    this.level == null ? RegistryAccess.EMPTY : this.level.registryAccess(), raw.get());
            itemHandler.deserialize(nested);
        }
    }

}
