package reika.rotarycraft.blockentities.level;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import reika.dragonapi.instantiable.storage.ManagedItemHandler;


import reika.dragonapi.base.OneSlotMachine;
import reika.dragonapi.instantiable.data.immutable.BlockKey;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.blockentity.BlockEntityAreaFiller;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

public class BlockEntityBlockFiller extends BlockEntityAreaFiller implements OneSlotMachine {


    protected ManagedItemHandler itemHandler = new ManagedItemHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public BlockEntityBlockFiller(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.FILLER.get(), pos, state);
    }

    @Override
    protected void onBlockPlaced() {
        /*todo if (ModList.BOTANIA.isLoaded() && itemHandler.getStackInSlot(0).getItem() instanceof IBlockProvider) {
            BlockKey bk = this.getBlockFromBotania((IBlockProvider) itemHandler.getStackInSlot(0).getItem(), itemHandler.getStackInSlot(0));
            ((IBlockProvider) itemHandler.getStackInSlot(0).getItem()).provideBlock(this.getPlacer(), null, itemHandler.getStackInSlot(0), bk.blockID, bk.metadata, true);
        } else {*/
        ReikaInventoryHelper.decrStack(0, itemHandler);
        //}
    }

    @Override
    protected boolean hasRemainingBlocks() {
        if (itemHandler.getStackInSlot(0).isEmpty())
            return false;
        /*todo if (ModList.BOTANIA.isLoaded() && itemHandler.getStackInSlot(0).getItem() instanceof IBlockProvider) {
            BlockKey bk = this.getBlockFromBotania((IBlockProvider) itemHandler.getStackInSlot(0).getItem(), itemHandler.getStackInSlot(0));
            return bk != null && ((IBlockProvider) itemHandler.getStackInSlot(0).getItem()).provideBlock(this.getPlacer(), null, itemHandler.getStackInSlot(0), bk.blockID, bk.metadata, false);
        }*/
        return itemHandler.getStackInSlot(0).getCount() > 0;
    }

    @Override
    protected BlockKey getNextPlacedBlock() {
        return this.getBlock(itemHandler.getStackInSlot(0));
    }

    private BlockKey getBlock(ItemStack is) {
        if (is.isEmpty())
            return null;
      /*  if (ModList.BOTANIA.isLoaded() && is.getItem() instanceof IBlockProvider) {
            return this.getBlockFromBotania((IBlockProvider) is.getItem(), is);
        }*/
        BlockKey bk = ReikaItemHelper.getWorldBlockFromItem(is);
        return bk.blockID != Blocks.AIR.defaultBlockState() ? bk : null;
    }

/* todo   @ModDependent(ModList.BOTANIA)
    private BlockKey getBlockFromBotania(IBlockProvider item, ItemStack is) {
        if (is.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag() == null)
            return null;
        String n = is.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag().getStringOr("blockName", "");
        Block b = Block.getBlockFromName(n);
        return b != null ? new BlockKey(b) : null;
    }*/


    @Override
    protected void animateWithTick(Level world, BlockPos pos) {

    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.FILLER;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    protected void onInventoryChanged() {

    }

    public final ItemStack getStackInSlot(int par1) {
        return itemHandler.getStackInSlot(par1);
    }

    public final void setInventorySlotContents(int par1, ItemStack is) {
        itemHandler.setStackInSlot(par1, is);
        this.onInventoryChanged();
    }

    public void openInventory() {
    }

    public void closeInventory() {
    }

/*todo    @Override
    public final void markDirty() {
        blockMetadata = level.getBlockMetadata(worldPosition);
        level.markBlockEntityChunkModified(worldPosition, this);

        if (this.getBlockType() != Blocks.AIR) {
            level.func_147453_f(worldPosition, this.getBlockType());
        }
        //this.onInventoryChanged();
    }*/

    public int getInventoryStackLimit() {
        return 64;
    }

    public final ItemStack decrStackSize(int par1, int par2) {
        ItemStack ret = ReikaInventoryHelper.decrStackSize(itemHandler, par1, par2);
        this.onInventoryChanged();
        return ret;
    }

/* todo   public final ItemStack getStackInSlotOnClosing(int par1) {
        ItemStack ret = ReikaInventoryHelper.getStackInSlotOnClosing(this, par1);
        this.onInventoryChanged();
        return ret;
    }

    public final int[] getAccessibleSlotsFromSide(int var1) {
        if (this instanceof InertIInv)
            return new int[0];
        return ReikaInventoryHelper.getWholeInventoryForISided(this);
    }

    public final boolean canInsertItem(int i, ItemStack is, int side) {
        if (this instanceof InertIInv)
            return false;
        return ((Container) this).isItemValidForSlot(i, is);
    }*/

    public boolean isUseableByPlayer(Player var1) {
        return this.isPlayerAccessible(var1);
    }

    @Override
    public void saveAdditional(CompoundTag NBT) {
        super.saveAdditional(NBT);

        ListTag nbttaglist = new ListTag();

        // 26.1: ItemStack.save(CompoundTag) removed; round-trip via ItemStack.CODEC + RegistryOps.
        // Also fixed an old inverted-emptiness bug — the legacy loop only saved EMPTY slots.
        var regAccSave = level == null ? net.minecraft.core.RegistryAccess.EMPTY : level.registryAccess();
        var opsSave = regAccSave.createSerializationContext(net.minecraft.nbt.NbtOps.INSTANCE);
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            var stack = itemHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                CompoundTag entry = new CompoundTag();
                entry.putShort("Slot", (short) i);
                net.minecraft.world.item.ItemStack.CODEC.encodeStart(opsSave, stack).result()
                        .ifPresent(stackTag -> entry.put("Stack", stackTag));
                nbttaglist.add(entry);
            }
        }

        NBT.put("Items", nbttaglist);
    }

    @Override
    protected String getTEName() {
        return "block_filler";
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.FILLER.get();
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    public void load(CompoundTag NBT) {
        super.load(NBT);

        ListTag nbttaglist = NBT.getListOrEmpty("Items");
        itemHandler = new ManagedItemHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
        // 26.1: ItemStack.of removed; round-trip via ItemStack.CODEC + RegistryOps.
        var regAccLoad = level == null ? net.minecraft.core.RegistryAccess.EMPTY : level.registryAccess();
        var opsLoad = regAccLoad.createSerializationContext(net.minecraft.nbt.NbtOps.INSTANCE);
        for (int i = 0; i < nbttaglist.size(); i++) {
            CompoundTag entry = nbttaglist.getCompoundOrEmpty(i);
            short slot = entry.getShortOr("Slot", (short) 0);
            if (slot >= 0 && slot < itemHandler.getSlots()) {
                var stackTag = entry.get("Stack");
                if (stackTag != null) {
                    var loaded = net.minecraft.world.item.ItemStack.CODEC.parse(opsLoad, stackTag).result().orElse(ItemStack.EMPTY);
                    itemHandler.setStackInSlot(slot, loaded);
                }
            } else {
                RotaryCraft.LOGGER.error(this + " tried to load an inventory slot " + slot + " from NBT!");
            }
        }
    }

/*    @Override
    public boolean isItemValidForSlot(int slot, ItemStack is) {
        return this.getBlock(is) != null;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack is, int side) {
        return false;
    }*/

    @Override
    protected long getRequiredPower() {
        MapColor mat = this.getNextPlacedBlock().blockID.getMapColor(level, worldPosition);
//todo        if (!mat.isSolid())
//            return 512;
        if (mat == MapColor.METAL)// || mat == MapColor.HEAVY_METAL)
            return 4096;
        return mat == MapColor.STONE ? 2048 : 1024;
    }

    @Override
    protected boolean allowFluidOverwrite() {
        return true;
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }
}
