///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.decorative;
//
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.AABB;
//import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
//import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.RotaryItems;
//import reika.rotarycraft.registry.SoundRegistry;
//
//public class BlockEntityProjector extends InventoriedPowerReceiver implements RangedEffect {
//
//    public static final int MAXCHANNELS = 24;
//    public static final int DELAY = 400;
//
//    public int channel = 0;
//    public boolean on = false;
//    public boolean emptySlide = true;
//
//    public boolean canProject(int x2, int y2, int z2) {
//
//        return true;
//    }
//
//    @Override
//    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//        return false;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        tickcount++;
//        this.getIOSides(world, pos);
//        this.getPower(false);
//        if (power < MINPOWER) {
//            on = false;
//            return;
//        }
//        on = true;
//        if (tickcount >= DELAY) {
//            tickcount = 0;
//            //this.cycleInv();
//        }
//        this.getChannelFromActiveSlide();
//    }
//
//    @Override
//    protected void onPositiveRedstoneEdge() {
//        this.cycleInv();
//    }
//
//    private void getChannelFromActiveSlide() {
//        if (itemHandler.getStackInSlot(0).isEmpty()) {
//            emptySlide = true;
//            channel = 0;
//            return;
//        }
//        if (itemHandler.getStackInSlot(0).getItem() == Items.ender_eye) {
//            emptySlide = false;
//            channel = -1;
//        }
//        if (itemHandler.getStackInSlot(0).getItem() == Items.clock) {
//            emptySlide = false;
//            channel = -3;
//        }
//        if (itemHandler.getStackInSlot(0).getItem() != RotaryItems.SLIDE.get()) {
//            emptySlide = true;
//            return;
//        }
//        emptySlide = false;
//        if (itemHandler.getStackInSlot(0).getItemDamage() == 24) {
//            channel = -2;
//        } else
//            channel = itemHandler.getStackInSlot(0).getItemDamage();
//    }
//
//    public String getCustomImagePath() {
//        if (itemHandler.getStackInSlot(0).isEmpty() || itemHandler.getStackInSlot(0).getTag() == null)
//            return "";
//        CompoundTag nbt = itemHandler.getStackInSlot(0).getTag();
//        return nbt.getString("file");
//    }
//
//    public void cycleInv() {
//        ItemStack active = itemHandler.getStackInSlot(0);
//        for (int i = 0; i < itemHandler.getSlots() - 1; i++) {
//            itemHandler.getStackInSlot(i) = inv[i + 1];
//        }
//        inv[itemHandler.getSlots() - 1] = active;
//        SoundRegistry.PROJECTOR.playSoundAtBlock(level, xCoord, yCoord, zCoord, 1, 1);
//    }
//
//    public void getIOSides(Level world, BlockPos pos, int metadata) {
//        switch (metadata) {
//            case 0:
//                read = Direction.EAST;
//                break;
//            case 1:
//                read = Direction.WEST;
//                break;
//            case 2:
//                read = Direction.NORTH;
//                break;
//            case 3:
//                read = Direction.SOUTH;
//                break;
//        }
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//
//    @Override
//    public AABB getRenderBoundingBox() {
//        return INFINITE_EXTENT_AABB;
//    }
//
//    @Override
//    public int getRange() {
//        int x;
//        int z;
//        switch (this.getBlockMetadata()) {
//            case 0:
//                x = xCoord - 1;
//                while (x >= xCoord - 12 && level.getBlock(x, yCoord, zCoord) == Blocks.AIR)
//                    x--;
//                return x - xCoord + 1;
//            case 1:
//                x = xCoord + 1;
//                while (x <= xCoord + 12 + 1 && (level.getBlock(x, yCoord, zCoord) == Blocks.AIR || level.getBlock(x, yCoord, zCoord).isAir(level, x, yCoord, zCoord)))
//                    x++;
//                return -(x - xCoord);
//            case 2:
//                z = zCoord + 1;
//                while (z <= zCoord + 1 + 12 && level.getBlock(xCoord, yCoord, z) == Blocks.AIR)
//                    z++;
//                return -(z - zCoord);
//            case 3:
//                z = zCoord - 1;
//                while (z >= zCoord - 12 && level.getBlock(xCoord, yCoord, z) == Blocks.AIR)
//                    z--;
//                return z - zCoord + 1;
//            default:
//                return 0;
//        }
//    }
//
//    public boolean canShow() {
//        int r = this.getRange();
//        int x = xCoord;
//        int y = yCoord;
//        int z = zCoord;
//        int a = 0;
//        int b = 0;
//        switch (this.getBlockMetadata()) {
//            case 0:
//                x += r - 1;
//                a = 1;
//                break;
//            case 1:
//                x -= r;
//                a = 1;
//                break;
//            case 2:
//                z -= r;
//                b = 1;
//                break;
//            case 3:
//                z += r - 1;
//                b = 1;
//                break;
//        }
//        int x2 = x;
//        int z2 = z;
//        switch (this.getBlockMetadata()) {
//            case 0:
//                x2++;
//                break;
//            case 1:
//                x2--;
//                break;
//            case 2:
//                z2--;
//                break;
//            case 3:
//                z2++;
//                break;
//        }
//        Level world = level;
//        for (int k = 0; k <= 4; k++) {
//            for (int i = -3; i <= 3; i++) {
//                Block id = world.getBlock(x + b * i, y + k, z + a * i);
//                if (id == Blocks.AIR || !id.isOpaqueCube()) {
//                    return false;
//                }
//                if (!this.canProject(x + b * i, y + k, z + a * i))
//                    return false;
//            }
//        }
//        for (int k = 0; k <= 4; k++) {
//            for (int i = -3; i <= 3; i++) {
//                Block id = world.getBlock(x2 + b * i, y + k, z2 + a * i);
//                if (id != Blocks.AIR) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.PROJECTOR;
//    }
//
//    @Override
//    public int getContainerSize() {
//        return MAXCHANNELS;
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int slot, ItemStack is) {
//        return is.getItem() == RotaryItems.SLIDE.get();
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//        channel = NBT.getInt("ch");
//        emptySlide = NBT.getBoolean("empty");
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//        NBT.putInt("ch", channel);
//        NBT.putBoolean("empty", emptySlide);
//    }
//
//    @Override
//    public int getMaxRange() {
//        return 8;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        if (!this.canShow())
//            return 15;
//        return 0;
//    }
//
//}
