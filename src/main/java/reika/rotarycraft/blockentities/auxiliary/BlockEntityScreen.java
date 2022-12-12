///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.auxiliary;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.*;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import org.jetbrains.annotations.NotNull;
//import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
//import reika.rotarycraft.base.blockentity.RemoteControlMachine;
//import reika.rotarycraft.registry.MachineRegistry;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class BlockEntityScreen extends InventoriedPowerReceiver {
//
//    /**
//     * Stores things as key-value as colors[]-xyz[]
//     */
//    private final HashMap<int[], int[]> cameras = new HashMap<int[], int[]>();
//    public int channel = 0;
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.SCREEN;
//    }
//
//    @Override
//    public void updateBlockEntity() {
//        super.updateBlockEntity();
//        this.getPowerBelow();
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    public void activate(Player ep) {
//        if (!this.canRun())
//            return;
//        int[] cameraPos = this.getCameraFromColors(level);
//        if (!this.isValidCamera(cameraPos))
//            return;
//        RemoteControlMachine te = (RemoteControlMachine) level.getBlockEntity(new BlockPos(cameraPos[0], cameraPos[1], cameraPos[2]));
//        //te.moveCameraToLook(ep);
//        te.activate(level, ep, new BlockPos(cameraPos[0], cameraPos[1], cameraPos[2]));
//    }
//
//    private boolean isValidCamera(int[] cameraPos) {
//        if (cameraPos[0] == worldPosition.getX() && cameraPos[1] == worldPosition.getY() && cameraPos[2] == worldPosition.getZ())
//            return false;
//        return (level.getBlockEntity(new BlockPos(cameraPos[0], cameraPos[1], cameraPos[2])) instanceof RemoteControlMachine);
//    }
//
//    private boolean canRun() {
//        if (power < MINPOWER)
//            return false;
//        if (torque < MINTORQUE)
//            return false;
//        if (inv[0] == null || inv[1] == null || inv[2] == null)
//            return false;
//
//        Map<DyeColor, Item> map = new HashMap<>();
//
//        return inv[0].getItem() == map.values() && inv[1].getItem() == map.values() && inv[2].getItem() == map.values(); //todo check if this dye checker works
//    }
//
//    private int[] getCameraFromColors(Level world) {
//        int[] pos = {worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()};
//        int[] colors = {2, 2, 2};//todo {inv[0].getItemDamage(), inv[1].getItemDamage(), inv[2].getItemDamage()};
//        if (!cameras.containsKey(colors))
//            if (!this.searchForMatchingCamera(world, colors))
//                return pos;
//        pos = cameras.get(colors);
//        return pos;
//    }
//
//    private boolean searchForMatchingCamera(Level world, int[] colors) {
//        int range = 64;
//        for (int i = -range; i <= range; i++) {
//            for (int j = -range; j <= range; j++) {
//                for (int k = -range; k <= range; k++) {
//                    BlockEntity te = world.getBlockEntity(new BlockPos(worldPosition.getX() + i, worldPosition.getY() + j, worldPosition.getZ() + k));
//                    if (te != null) {
//                        if (te instanceof RemoteControlMachine) {
//                            RemoteControlMachine cc = (RemoteControlMachine) te;
//                            int[] camcolor = {cc.colors[0], cc.colors[1], cc.colors[2]};
//                            if (camcolor[0] == colors[0] && camcolor[1] == colors[1] && camcolor[2] == colors[2]) {
//                                cameras.put(colors, new int[]{cc.getBlockPos().getX(), cc.getBlockPos().getY(), cc.getBlockPos().getZ()});
//                                return true;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public Block getBlockEntityBlockID() {
//        return null;
//    }
//
//    @Override
//    public void updateEntity(Level level, BlockPos blockPos) {
//
//    }
//
//    @Override
//    protected void animateWithTick(Level level, BlockPos blockPos) {
//
//    }
//
//    //@Override
//    public int getRedstoneOverride() {
//        int[] cam = this.getCameraFromColors(level);
//        if (this.isValidCamera(cam))
//            return 0;
//        return 15;
//    }
//
//    @Override
//    protected String getTEName() {
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
//
//    @Override
//    public int getContainerSize() {
//        return 3;
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
//}
