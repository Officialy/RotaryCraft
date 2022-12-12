/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;

import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;

import java.util.List;

public class BlockEntityPlayerDetector extends BlockEntityPowerReceiver implements RangedEffect {
    public static final int FALLOFF = 128; // 1kW per meter range
    public static final int SPEEDFACTOR = 32; //32 rad/s per -tick
    public static final int BASESPEED = 100; //5s reaction time by default

    public boolean analog = false;
    public int powerLevel = 0;
    public int selectedrange;
    private boolean isActive = false;
    /**
     * Used to determine reaction time
     */
    private int ticksdetected = 0;

    public BlockEntityPlayerDetector(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.PLAYER_DETECTOR.get(), pos, state);
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public void updateBlockEntity() {
        super.updateBlockEntity();
        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage("Run");

        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d", this.selectedrange));
        this.getPowerBelow();
        //ReikaWorldHelper.causeAdjacentUpdates(getLevel(), worldPosition);
        if (power < MINPOWER) {
            isActive = false;
            ticksdetected = 0;
            return;
        }
        if (!analog) {
            powerLevel = 0;
            if (this.checkForPlayers(level, worldPosition)) {
                ticksdetected++;
                if (ticksdetected >= this.getReactionTime())
                    isActive = true;
            } else {
                isActive = false;
                ticksdetected = 0;
            }
        } else {
            isActive = false;
            int level1 = this.countPlayers(level, worldPosition);
            if (level1 > 0) {
                ticksdetected++;
                if (ticksdetected >= this.getReactionTime())
                    powerLevel = level1;
            } else {
                powerLevel = 0;
                ticksdetected = 0;
            }
        }
    }

    public int getReactionTime() { //with current numbers maxes to instant raction at 3200 rad/s
        int time = BASESPEED - (omega / SPEEDFACTOR);
        if (time < 1)
            time = 1;
        return time;
    }

    private int countPlayers(Level world, BlockPos pos) {
        int range = this.getRange();
        AABB box = AABB.of(new BoundingBox(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1)).expandTowards(range, range, range);
        List inbox;
        //if (world.isClientSide)
        //inbox = world.getEntities(Player.class, box);
        //else
        inbox = world.getEntitiesOfClass(Player.class, box);
        int count = inbox.size();
        if (count > 15)
            count = 15; //15 is max redstone
        return count;
    }

    private boolean checkForPlayers(Level world, BlockPos pos) {
        int range = this.getRange();
        AABB box = AABB.of(new BoundingBox(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1)).expandTowards(range, range, range);
        List inbox;
        //if (world.isClientSide)
        //inbox = world.getEntities(Player.class, box);
        //else
        inbox = world.getEntitiesOfClass(Player.class, box);
        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d", inbox.size()));
        return inbox.size() > 0;
    }

    public int getRange() {
        int range = (int) (power / FALLOFF);
        if (range > this.getMaxRange())
            range = this.getMaxRange();
        if (range > selectedrange)
            return selectedrange;
        return range;
    }

    public int getMaxRange() {
        int range = (int) (power / FALLOFF);
        int max = Math.max(64, ConfigRegistry.DETECTORRANGE.getValue());
        if (range > max)
            range = max;
        return range;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    @Override
    public void updateEntity(Level level, BlockPos blockPos) {

    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    /**
     * Writes a tile entity to NBT.
     */
    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);
        tag.putInt("range", selectedrange);
    }

    /**
     * Reads a tile entity from NBT.
     */
    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);
        selectedrange = tag.getInt("range");
    }

    @Override
    protected String getTEName() {
        return "Player Detector";
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.PLAYERDETECTOR;
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
