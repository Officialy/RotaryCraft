package reika.rotarycraft.blockentities.level;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import reika.dragonapi.ModList;
import reika.dragonapi.base.OneSlotMachine;
import reika.dragonapi.instantiable.data.immutable.BlockKey;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.blockentity.BlockEntityAreaFiller;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

public class BlockEntityBlockFiller extends BlockEntityAreaFiller implements IItemHandler, OneSlotMachine {

    private ItemStack[] inv = new ItemStack[1];

    public BlockEntityBlockFiller(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.FILLER.get(), pos, state);
    }

    @Override
    protected void onBlockPlaced() {
        /*todo if (ModList.BOTANIA.isLoaded() && inv[0].getItem() instanceof IBlockProvider) {
            BlockKey bk = this.getBlockFromBotania((IBlockProvider) inv[0].getItem(), inv[0]);
            ((IBlockProvider) inv[0].getItem()).provideBlock(this.getPlacer(), null, inv[0], bk.blockID, bk.metadata, true);
        } else {*/
            ReikaInventoryHelper.decrStack(0, inv);
        //}
    }

    @Override
    protected boolean hasRemainingBlocks() {
        if (inv[0] == null)
            return false;
        /*todo if (ModList.BOTANIA.isLoaded() && inv[0].getItem() instanceof IBlockProvider) {
            BlockKey bk = this.getBlockFromBotania((IBlockProvider) inv[0].getItem(), inv[0]);
            return bk != null && ((IBlockProvider) inv[0].getItem()).provideBlock(this.getPlacer(), null, inv[0], bk.blockID, bk.metadata, false);
        }*/
        return inv[0].getCount() > 0;
    }

    @Override
    protected BlockKey getNextPlacedBlock() {
        return this.getBlock(inv[0]);
    }

    private BlockKey getBlock(ItemStack is) {
        if (is == null)
            return null;
      /*  if (ModList.BOTANIA.isLoaded() && is.getItem() instanceof IBlockProvider) {
            return this.getBlockFromBotania((IBlockProvider) is.getItem(), is);
        }*/
        BlockKey bk = ReikaItemHelper.getWorldBlockFromItem(is);
        return bk.blockID != Blocks.AIR.defaultBlockState() ? bk : null;
    }

/* todo   @ModDependent(ModList.BOTANIA)
    private BlockKey getBlockFromBotania(IBlockProvider item, ItemStack is) {
        if (is.getTag() == null)
            return null;
        String n = is.getTag().getString("blockName");
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

    @Override
    public int getSlots() {
        return 1;
    }

    public final ItemStack getStackInSlot(int par1) {
        return inv[par1];
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return null;
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return null;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 0;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return false;
    }

    public final void setInventorySlotContents(int par1, ItemStack is) {
        inv[par1] = is;
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
        ItemStack ret = ReikaInventoryHelper.decrStackSize((Container) this, par1, par2);
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

        for (int i = 0; i < inv.length; i++) {
            if (inv[i] != null) {
                CompoundTag CompoundTag = new CompoundTag();
                CompoundTag.putShort("Slot", (short) i);
                inv[i].save(CompoundTag);
                nbttaglist.add(CompoundTag);
                //ReikaJavaLibrary.pConsole(i+":"+inv[i]);
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

        ListTag nbttaglist = NBT.getList("Items", Tag.TAG_COMPOUND);
        inv = new ItemStack[this.getSlots()];

        for (int i = 0; i < nbttaglist.size(); i++) {
            CompoundTag CompoundTag = nbttaglist.getCompound(i);
            short byte0 = CompoundTag.getShort("Slot");

            if (byte0 >= 0 && byte0 < inv.length) {
                inv[byte0] = ItemStack.of(CompoundTag);
                //ReikaJavaLibrary.pConsole(byte0+":"+inv[byte0]);
            } else {
                RotaryCraft.LOGGER.error(this + " tried to load an inventory slot " + byte0 + " from NBT!");
                //Thread.dumpStack();
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
        Material mat = this.getNextPlacedBlock().blockID.getMaterial();
        if (!mat.isSolid())
            return 512;
        if (mat == Material.METAL || mat == Material.HEAVY_METAL)
            return 4096;
        return mat == Material.STONE ? 2048 : 1024;
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
