///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.level;
//
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.level.Level;
//import net.minecraftforge.common.MinecraftForge;
//import reika.dragonapi.interfaces.blockentity.BreakAction;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
//import reika.rotarycraft.base.blockentity.BlockEntityBeamMachine;
//
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.PowerReceivers;
//import reika.rotarycraft.registry.RotaryBlocks;
//
//public class BlockEntityLightBridge extends BlockEntityBeamMachine implements RangedEffect, BreakAction {
//
//    public static final int distancelimit = Math.max(64, RotaryConfig.COMMON.BRIDGERANGE.get());
//    private int animtick = 0;
//
//    /**
//     * Minimum power required to turn on
//     */
//    //public static final long MINPOWER = 90000000; //90MW is about the energy from the sun from a 16-acre farm -> think Portal 2
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        animtick++;
//        power = (long) omega * (long) torque;
//        this.getIOSides(world, pos, meta);
//        this.getPower(false);
//        if (!world.isClientSide)
//            this.makeBeam(world, pos, meta);
//    }
//
//    @Override
//    protected void makeBeam(Level world, BlockPos pos, int metadata) {
//        int animstep = 0;
//        boolean blocked = false;
//        int dir = 0;
//        switch (metadata) {
//            case 0:
//                dir = 3;
//                break;
//            case 1:
//                dir = 1;
//                break;
//            case 2:
//                dir = 2;
//                break;
//            case 3:
//                dir = 0;
//                break;
//        }
//        //Make punch thru snow, tall grass, etc!
//        //if (world.getBlock(x+facing.getStepX(), y+facing.getStepY(), z+facing.getStepZ()) == RotaryBlocks.BRIDGE.get().blockID)
//        //	blocked = true;
//        int range = this.getRange();
//        //ReikaJavaLibrary.pConsole(power+":"+distancelimit+":"+(PowerReceivers.LIGHTBRIDGE.getMinPower()/distancelimit)+":"+range, Dist.DEDICATED_SERVER);
//        if (range > 0 && world.getBlockLightValue(x, y + 1, z) >= 13) { //1 kW - configured so light level 15 (sun) requires approx power of sun on Earth's surface
//            if (!world.isClientSide) {
//                //if (!Blocks.opaqueCubeLookup[world.getBlock(x+facing.getStepX(), y+facing.getStepY(), z+facing.getStepZ())]) {
//                for (int i = 1; (i <= range || range == -1) && i <= animtick && !blocked && (ReikaWorldHelper.softBlocks(world, x + facing.getStepX(), y + facing.getStepY(), z + facing.getStepZ()) || world.getBlock(x + facing.getStepX(), y + facing.getStepY(), z + facing.getStepZ()) == Blocks.AIR || world.getBlock(x + facing.getStepX(), y + facing.getStepY(), z + facing.getStepZ()) == RotaryBlocks.BRIDGE.get()); i++) {//&& world.getBlock(x+facing.getStepX(), y+facing.getStepY(), z+facing.getStepZ()) != RotaryBlocks.BRIDGE.get().blockID; i++) {
//                    //ModLoader.getMinecraftInstance().ingameGUI.addChatMessage(String.format("%d %d %d", pos));
//                    Block idview = world.getBlock(x + facing.getStepX() * i, y + facing.getStepY() * i, z + facing.getStepZ() * i);
//                    int metaview = world.getBlockMetadata(x + facing.getStepX() * i, y + facing.getStepY() * i, z + facing.getStepZ() * i);
//                    if (idview == Blocks.AIR || ReikaWorldHelper.softBlocks(world, x + facing.getStepX() * i, y + facing.getStepY() * i, z + facing.getStepZ() * i) || idview == Blocks.LIGHT.get() || idview == RotaryBlocks.BEAM.get() || idview == RotaryBlocks.BRIDGE.get()) { //Only overwrite air blocks
//                        //ModLoader.getMinecraftInstance().ingameGUI.addChatMessage(String.format("%d", idview, world.getBlockMetadata(x+facing.getStepX()*i, y+facing.getStepY()*i, z+facing.getStepZ()*i)));
//                        world.setBlock(x + facing.getStepX() * i, y + facing.getStepY() * i, z + facing.getStepZ() * i, RotaryBlocks.BRIDGE.get(), dir, 3);
//                        //ModLoader.getMinecraftInstance().ingameGUI.addChatMessage(String.format("%d @ %d", idview, world.getBlockMetadata(x+facing.getStepX()*i, y+facing.getStepY()*i, z+facing.getStepZ()*i)));
//                        //world.markBlockForUpdate(x+facing.getStepX()*i, y+facing.getStepY()*i, z+facing.getStepZ()*i);
//                        //world.notifyBlockOfNeighborChange(x+facing.getStepX()*i, y+facing.getStepY()*i, z+facing.getStepZ()*i, this.getBlockEntityBlockID());
//                    }
//                    if (idview != Blocks.AIR && !ReikaWorldHelper.softBlocks(world, x + facing.getStepX() * i, y + facing.getStepY() * i, z + facing.getStepZ() * i) && idview != Blocks.LIGHT.get() && idview != RotaryBlocks.BEAM.get() && (idview != RotaryBlocks.BRIDGE.get()) || animtick > range + 1) {
//                        animtick--;
//                        blocked = true;
//                    }
//                }
//            }
//        }
//        //}
//        else {
//            MinecraftForge.EVENT_BUS.post(new LightBridgePowerLossEvent(this));
//            this.lightsOut(world, pos);
//        }
//    }
//
//    private void lightsOut(Level world, BlockPos pos) {
//        //ReikaChatHelper.writeInt(44);
//        animtick = 0;
//        int dir = 0;
//        switch (this.getBlockMetadata()) {
//            case 0:
//                dir = 3;
//                break;
//            case 1:
//                dir = 1;
//                break;
//            case 2:
//                dir = 2;
//                break;
//            case 3:
//                dir = 0;
//                break;
//        }
//        for (int i = 1; i < this.getMaxRange(); i++) {
//            Block idview = world.getBlock(x + facing.getStepX() * i, y + facing.getStepY() * i, z + facing.getStepZ() * i);
//            int metaview = world.getBlockMetadata(x + facing.getStepX() * i, y + facing.getStepY() * i, z + facing.getStepZ() * i);
//            if (idview == RotaryBlocks.BRIDGE.get() && metaview == dir)
//                world.setBlockToAir(x + facing.getStepX() * i, y + facing.getStepY() * i, z + facing.getStepZ() * i);
//            world.markBlockForUpdate(x + facing.getStepX() * i, y + facing.getStepY() * i, z + facing.getStepZ() * i);
//        }
//    }
//
//    /**
//     * Writes a tile entity to NBT.
//     */
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//        NBT.putInt("tick", animtick);
//    }
//
//    /**
//     * Reads a tile entity from NBT.
//     */
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//        animtick = NBT.getInt("tick");
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//
//    @Override
//    public int getRange() {
//        return (int) Math.min(distancelimit, power * distancelimit / PowerReceivers.LIGHTBRIDGE.getMinPower());
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.LIGHTBRIDGE;
//    }
//
//    @Override
//    public int getMaxRange() {
//        return distancelimit;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return 0;
//    }
//
//    @Override
//    public void breakBlock() {
//        this.lightsOut(level, xCoord, yCoord, zCoord);
//    }
//}
