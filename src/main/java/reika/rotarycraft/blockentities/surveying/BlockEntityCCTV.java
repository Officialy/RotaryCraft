///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.surveying;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.core.BlockPos;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.fml.loading.FMLLoader;
//import org.jetbrains.annotations.NotNull;
//import reika.dragonapi.libraries.mathsci.ReikaPhysicsHelper;
//import reika.rotarycraft.base.blockentity.RemoteControlMachine;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.RotaryItems;
//
//public class BlockEntityCCTV extends RemoteControlMachine {
//
//    public static final boolean ISCAMERAMODE = true;
//    private final double[] playerCam = new double[5];
//    private final double[] cameraPos = new double[5];
//    public boolean cameraIsMoved = false;
//    public String owner;
//    public Player clicked;
//    public float theta;
//
//    public BlockEntityCCTV(BlockEntityType<?> type, BlockPos pos, BlockState state) {
//        super(type, pos, state);
//    }
//
//    @Override
//    public Block getBlockEntityBlockID() {
//        return null;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        if (theta > 60) {
//            theta = -60;
//        }
//        if (theta < -60) {
//            theta = 60;
//        }
//        if (theta > 0)
//            this.setBlockMetadata(1);
//        if (theta < 0)
//            this.setBlockMetadata(0);
//        this.setColors();
//        if (!this.hasCoil()) {
//            on = false;
//            return;
//        }
//        tickcount2++;
//        if (tickcount2 > this.getUnwindTime()) {
//            ItemStack is = this.getDecrementedCharged();
//            itemHandler.getStackInSlot(0) = is;
//            tickcount2 = 0;
//        }
//
//        //Player ep = world.getClosestPlayer(pos, -1);
//        //ReikaJavaLibrary.pConsole(String.format("%f,  %f, %f", ep.getY, ep.getY(), ep.posZ)+" on "+FMLCommonHandler.instance().getEffectiveSide());
//
//
//        Player e = world.getPlayerEntityByName(owner);
//        on = true;
//        if (cameraIsMoved) {
//            //this.moveCameraToLook(clicked);
//            this.movePlayerToCamera(e);
//            tickcount++;
//        }
//        //ReikaJavaLibrary.pConsole("X: "+e.getY+"  Y: "+e.getY()+"  Z: "+e.posZ+"  Y: "+e.rotationYaw+"  P: "+e.rotationPitch);
//        //ReikaJavaLibrary.pConsole("X: "+playerCam[0]+"  Y: "+playerCam[1]+"  Z: "+playerCam[2]+"  Y: "+playerCam[3]+"  P: "+playerCam[4]);
//        //ReikaJavaLibrary.pConsole("X: "+cameraPos[0]+"  Y: "+cameraPos[1]+"  Z: "+cameraPos[2]+"  Y: "+cameraPos[3]+"  P: "+cameraPos[4]);
//        double[] dd = ReikaPhysicsHelper.polarToCartesian(1, theta, phi);
//        //ReikaJavaLibrary.pConsole(dd[0]+"  "+dd[1]+"  "+dd[2]);
//
//        //this.setLook(x+0.5+dd[2], y+0.25-dd[1], z+0.40625+dd[0], -phi, theta);
//        if (tickcount < 20)
//            ;//return;
//        if (!cameraIsMoved)
//            return;
//        if (!Keyboard.isKeyDown(Keyboard.KEY_BACKSLASH) && !itemHandler.getStackInSlot(0).isEmpty() && itemHandler.getStackInSlot(0).getItem() == RotaryItems.SPRING.get() && itemHandler.getStackInSlot(0).getItemDamage() > 0)
//            return;
//        tickcount = 0;
//        this.movePlayerBack(e);
//        //this.moveCameraToPlayer();
//    }
//
//    @Override
//    protected void animateWithTick(Level level, BlockPos blockPos) {
//
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//
//    public void setLook(double x, double y, double z, double t, double p) {
//        cameraPos[0] = x;
//        cameraPos[1] = y;
//        cameraPos[2] = z;
//        cameraPos[3] = t;
//        cameraPos[4] = p;
//    }
//
//    public void moveCameraToLook(Player ep) {
//        if (!on)
//            return;
//        clicked = ep;
//        if (!cameraIsMoved)
//            this.setPlayerCam();
//        cameraIsMoved = true;
//        this.alignCameras(false);
//    }
//
//    private void setPlayerCam() {
//        if (FMLLoader.getDist() != Dist.CLIENT)
//            return;
//        Minecraft mc = Minecraft.getInstance();
//        LivingEntity e = mc.renderviewentity; //renderviewentity
//        if (e == null)
//            return;
//        playerCam[0] = e.getX();
//        playerCam[1] = e.getY();
//        playerCam[2] = e.getZ();
//        playerCam[3] = e.rotationYaw;
//        while (playerCam[3] < 0)
//            playerCam[3] += 360;
//        while (playerCam[3] >= 360)
//            playerCam[3] -= 360;
//        playerCam[4] = e.rotationPitch;
//        //ReikaJavaLibrary.pConsole(playerCam[3]);
//    }
//
//    public void moveCameraToPlayer() {
//        for (int i = 0; i < 5; i++)
//            cameraPos[i] = playerCam[i];
//        cameraIsMoved = false;
//        this.alignCameras(true);
//    }
//
//    /**
//     * Actually moves the ingame camera to the preset coords
//     */
//    private void alignCameras(boolean toPlayer) {
//        if (FMLLoader.getDist() != Dist.CLIENT)
//            return;
//        Minecraft mc = Minecraft.getInstance();
//        LivingEntity e = mc.renderViewEntity;
//        if (toPlayer) {
//            e.getX() = playerCam[0];
//            e.getY() = playerCam[1] + e.getEyeHeight();
//            e.getZ() = playerCam[2];
//            e.rotationYaw = (float) playerCam[3];
//            e.rotationPitch = (float) playerCam[4];
//        } else {
//            e.getX() = cameraPos[0];
//            e.getY() = cameraPos[1];
//            e.getZ() = cameraPos[2];
//            e.rotationYaw = (float) cameraPos[3];
//            e.rotationPitch = (float) cameraPos[4];
//        }
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.CCTV;
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//        theta = NBT.getFloat("thetad");
//        owner = NBT.getString("sowner");
//        cameraIsMoved = NBT.getBoolean("moved");
//    }
//
//    @Override
//    protected String getTEName() {
//        return null;
//    }
//
//    /**
//     * Writes a tile entity to NBT.  Maybe was not saving inv since seems to be acting like
//     * extends BlockEntityPowerReceiver, NOT InventoriedPowerReceiver
//     */
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//        if (owner != null && !owner.isEmpty())
//            NBT.putString("sowner", owner);
//        NBT.putFloat("thetad", theta);
//        NBT.putBoolean("moved", cameraIsMoved);
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return 0;
//    }
//
//    private void movePlayerToCamera(Player ep) {
//        if (ep == null)
//            return;
//        cameraIsMoved = true;
//        double[] dd = ReikaPhysicsHelper.polarToCartesian(1, theta, phi);
//        double dx = 0.5 + dd[2];
//        double dy = 0.25 - dd[1] - ep.getEyeHeight();
//        double dz = 0.40625 + dd[0];
//        playerCam[0] = ep.getX();
//        playerCam[1] = ep.getY();
//        playerCam[2] = ep.getZ();
//        playerCam[3] = ep.rotationYaw;
//        playerCam[4] = ep.rotationPitch;
//
//        ep.getX() = worldPosition.getX() + dx;
//        ep.getY() = worldPosition.getY() + dy;
//        ep.getZ() = worldPosition.getZ() + dz;
//        ep.rotationYaw = -phi;
//        ep.rotationPitch = theta;
//
//        owner = ep.getCommandSenderName();
//
//        ep.allowEdit = false;
//        //ep.setGameType(EnumGameType.ADVENTURE);
//        ep.disableDamage = true;
//
//        //ReikaJavaLibrary.pConsole(String.format("%f,  %f, %f", ep.getY, ep.getY(), ep.posZ)+" on "+FMLCommonHandler.instance().getEffectiveSide());
//
//        //ReikaChatHelper.writeCoords(ep.level, ep.getY, ep.getY(), ep.posZ);
//    }
//
//    private void movePlayerBack(Player ep) {
//        cameraIsMoved = false;
//        ep.setPos(playerCam[0], playerCam[1], playerCam[2]);
//        ep.rotationYaw = (float) playerCam[3];
//        ep.rotationPitch = (float) playerCam[4];
//    }
//
//    @Override
//    public void activate(Level world, Player ep, BlockPos pos) {
//        this.movePlayerToCamera(ep);
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
//    public boolean hasAnInventory() {
//        return false;
//    }
//
//    @Override
//    public boolean hasATank() {
//        return false;
//    }
//}
