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
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.Font;
//import net.minecraft.client.gui.Font;
//import net.minecraft.core.BlockPos;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.phys.AABB;
//
//import reika.dragonapi.interfaces.blockentity.GuiController;
//import reika.dragonapi.interfaces.blockentity.InertIInv;
//import reika.dragonapi.libraries.registry.ReikaDyeHelper;
//import reika.rotarycraft.base.blockentity.BlockEntitySpringPowered;
//import reika.rotarycraft.registry.MachineRegistry;
//
//import java.util.Collections;
//import java.util.List;
//
//public class BlockEntityDisplay extends BlockEntitySpringPowered implements InertIInv, GuiController, OneSlotMachine {
//
//    public static final int displayHeight = 12; //in lines
//    public static final int displayWidth = 27; //in chars
//    public static final int lineHeight = 12;
//    public static final int charWidth = 10;
//    private static final int[] ArRGB = {0, 128, 255};
//    private static final int[] ArBRGB = {0, 255, 255};
//    private final int[] rgb = new int[3];
//    private final int[] Brgb = new int[3];
//    private float scroll;
//    private String message = "";
//    private boolean display;
//
//    private ReikaDyeHelper color;
//    private boolean isArgonBlue = true;
//
//    public BlockEntityDisplay(BlockEntityType<?> type, BlockPos pos, BlockState state) {
//        super(type, pos, state);
//    }
//
//    public ReikaDyeHelper getDyeColor() {
//        return color != null ? color : ReikaDyeHelper.BLACK;
//    }
//
//    public void setDyeColor(ReikaDyeHelper dye) {
//        color = dye;
//        isArgonBlue = false;
//    }
//
//    @Override
//    protected void animateWithTick(Level world, BlockPos pos) {
//        if (this.getMessageLength() <= displayHeight)
//            return;
//        scroll += 0.05F;
//        if (scroll > this.getMessageLength())
//            scroll = 0;
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.DISPLAY;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return 0;
//    }
//
//    @Override
//    public Block getBlockEntityBlockID() {
//        return null;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        this.updateCoil();
//        tickcount++;
//    }
//
//    private void updateCoil() {
//        if (!this.hasCoil()) {
//            display = false;
//            return;
//        }
//        display = true;
//        tickcount++;
//        if (tickcount > this.getUnwindTime()) {
//            ItemStack is = this.getDecrementedCharged();
//            itemHandler.getStackInSlot(0) = is;
//            tickcount = 0;
//        }
//    }
//
//    public boolean canDisplay() {
//        return display;
//    }
//
//    public boolean hasList() {
//        if (message == null)
//            return false;
//        return !message.isEmpty();
//    }
//
//    public int getMessageLength() {
//        return message.length();
//    }
//
//    public float getScrollPos() {
//        return scroll;
//    }
//
//    public boolean isReadyToLoadNewLine() {
//        float frac = scroll - (int) scroll;
//        return frac >= 0.5F;
//    }
//
//    public int getRoundedScroll() {
//        float frac = scroll - (int) scroll;
//        if (frac >= 0.5F)
//            return (int) (scroll) + 1;
//        else
//            return (int) (scroll);
//    }
//
//    @Override
//    public AABB getRenderBoundingBox() {
//        return INFINITE_EXTENT_AABB;
//    }
//
//    public boolean hasSpace() {
//        Level world = level;
//        int x = worldPosition.getX();
//        int y = worldPosition.getY();
//        int z = worldPosition.getZ();
//        for (int j = -2; j <= 2; j++) {
//            int a = 0;
//            int b = 0;
//            if (meta == 0 || meta == 1)
//                b = j;
//            else
//                a = j;
//            for (int i = 1; i <= 3; i++) {
//                Block bk = world.getBlockState(new BlockPos(x + a, y + i, z + b)).getBlock();
//                if (!bk.isAir(world, x + a, y + i, z + b))
//                    return false;
//            }
//        }
//        return true;
//    }
//
//    public int getRed() {
//        return rgb[0];
//    }
//
//    public int getGreen() {
//        return rgb[1];
//    }
//
//    public int getBlue() {
//        return rgb[2];
//    }
//
//    public int getBorderRed() {
//        return Brgb[0];
//    }
//
//    public int getBorderGreen() {
//        return Brgb[1];
//    }
//
//    public int getBorderBlue() {
//        return Brgb[2];
//    }
//
//    public int getTextColor() {
//        return 0xffffff;
//    }
//
//    public void setMessage(String str) {
//        message = str;
//    }
//
//    public void clearMessage() {
//        message = "";
//    }
//
//    public void loadColorData() {
//        if (isArgonBlue)
//            this.loadArgonColor();
//        else {
//            int r = this.getDyeColor().getRed();
//            int g = this.getDyeColor().getGreen();
//            int b = this.getDyeColor().getBlue();
//            rgb[0] = r;
//            rgb[1] = g;
//            rgb[2] = b;
//            Brgb[0] = r;
//            Brgb[1] = g;
//            Brgb[2] = b;
//        }
//    }
//
//    /**
//     * Writes a tile entity to NBT.
//     */
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//        //NBT.putIntArray("color", rgb);
//        //NBT.putIntArray("Bcolor", Brgb);
//        NBT.putInt("dye", this.getDyeColor().ordinal());
//
//        NBT.putBoolean("argon", isArgonBlue);
//
//        NBT.putString("msg", message);
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//        //ReikaJavaLibrary.pConsole(Arrays.toString(NBT.getIntArray("Bcolor")));
//        //rgb = NBT.getIntArray("color");
//        //Brgb = NBT.getIntArray("Bcolor");
//
//        isArgonBlue = NBT.getBoolean("argon");
//        color = ReikaDyeHelper.dyes[NBT.getInt("dye")];
//
//        message = NBT.getString("msg");
//    }
//
//    @Override
//    protected String getTEName() {
//        return null;
//    }
//
//    public void setColorToArgon() {
//        isArgonBlue = true;
//    }
//
//
//    public void loadArgonColor() {
//        rgb[0] = ArRGB[0];
//        rgb[1] = ArRGB[1];
//        rgb[2] = ArRGB[2];
//        Brgb[0] = ArBRGB[0];
//        Brgb[1] = ArBRGB[1];
//        Brgb[2] = ArBRGB[2];
//    }
//
//
//    public List<String> getMessageForDisplay() {
//        Font f = Minecraft.getInstance().font;
//        return Collections.singletonList(f.plainSubstrByWidth(message, displayWidth * f.lineHeight)); //listFormattedStringToWidth
//    }
//
//    @Override
//    public int getBaseDischargeTime() {
//        return 120;
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 0;
//    }
//
//    @Override
//    public boolean isEmpty() {
//        return false;
//    }
//
//    @Override
//    public ItemStack getItem(int pIndex) {
//        return null;
//    }
//
//    @Override
//    public ItemStack removeItem(int pIndex, int pCount) {
//        return null;
//    }
//
//    @Override
//    public ItemStack removeItemNoUpdate(int pIndex) {
//        return null;
//    }
//
//    @Override
//    public void setItem(int pIndex, ItemStack pStack) {
//
//    }
//
//    @Override
//    public boolean stillValid(Player pPlayer) {
//        return false;
//    }
//
//    @Override
//    public void clearContent() {
//
//    }
//
//    @Override
//    public int getSlots() {
//        return 0;
//    }
//
//    
//    @Override
//    public ItemStack insertItem(int slot,  ItemStack stack, boolean simulate) {
//        return null;
//    }
//
//    
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
//    public boolean isItemValid(int slot,  ItemStack stack) {
//        return false;
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
