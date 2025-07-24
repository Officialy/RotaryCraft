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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLLoader;
import reika.dragonapi.libraries.ReikaEntityHelper;
import reika.dragonapi.libraries.io.ReikaChatHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.mathsci.ReikaPhysicsHelper;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.registry.ConfigRegistry;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class BlockEntityAimedCannon extends BlockEntityPowerReceiver implements RangedEffect, DiscreteFunction, ConditionalOperation {

    public static final int MAXLOWANGLE = -10;
    public boolean targetPlayers = true;
    public float theta;
    public boolean isCustomAim;
    protected double[] target = new double[4];

    protected Entity closestMob;
    protected double voffset = -0.125;
    /**
     * Up/down angle
     */
    protected int dir = 1;
    private List<String> safePlayers = new ArrayList<String>();
    private int numSafePlayers = 0;

    public BlockEntityAimedCannon(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public final double[] getTarget() {
        return target;
    }

    protected void printSafeList() {
        for (int i = 0; i < safePlayers.size(); i++) {
            String player = safePlayers.get(i);
            if (player == null)
                player = "NULL PLAYER IN SLOT " + i;
            else if (player.isEmpty())
                player = "EMPTY STRING PLAYER IN SLOT " + i;
            RotaryCraft.LOGGER.info("Side " + FMLLoader.getDist() + ": Safe Player " + (i + 1) + " of " + safePlayers.size() + ": " + player);
        }
    }

    protected final double getFiringPositionY(double dy) {
        double a = dir == 1 ? 0.75 : -0.25;
        return worldPosition.getY() + voffset * 0 + a + dy;
    }

    public final boolean isValidTheta(double incl) {
        if (theta < MAXLOWANGLE && dir == 1)
            return false;
        return !(theta > -MAXLOWANGLE) || dir != -1;
    }

    @Override
    public void updateBlockEntity() {
        //this.printSafeList();
        super.updateBlockEntity();
        tickcount++;
        if (level.isClientSide)
            ;//return;
        if (power < MINPOWER)
            return;
        if (isCustomAim) {
			/*
			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				phi--;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				phi++;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
				if (theta < 45)
					theta++;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
				if (theta > MAXLOWANGLE)
					theta--;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
				if (this.hasAmmo()) {
					double dd = 20;
					double[] tg = ReikaPhysicsHelper.polarToCartesian(dd, theta, 90-phi);
					tg[0] += xCoord;
					tg[1] += yCoord;
					tg[2] += zCoord;
					this.fire(level, tg);
				}
			}
			 */
        } else {
            target = this.getTarget(level, worldPosition);
            this.adjustAim(level, worldPosition, target);
        }
    }

    public boolean isAimingAtTarget(Level world, BlockPos pos, double[] t) {
        double[] tg = ReikaPhysicsHelper.cartesianToPolar(pos.getX() - t[0], pos.getY() - t[1], pos.getZ() - t[2]);
        tg[1] = Math.abs(tg[1]) - 90;
        float phi2 = phi;
        while (phi2 < 0)
            phi2 += 360;
        while (phi2 >= 360)
            phi2 -= 360;
        //ReikaJavaLibrary.pConsole("PHI: "+phi2+" THETA: "+theta+" for "+tg[2]+", "+tg[1]);
        if (tg[2] - phi2 > 180)
            tg[2] -= 360;
        if (!ReikaMathLibrary.approxr(theta, tg[1], 5))
            return false;
        return ReikaMathLibrary.approxr(phi2, tg[2], 5);
    }

    public void adjustAim(Level world, BlockPos pos, double[] t) {
        if (t[3] != 1)
            return;
        double dx = pos.getX() + 0.5 - t[0];
        double dy = pos.getY() + 0.5 - t[1];
        double dz = pos.getZ() + 0.5 - t[2];
        double[] tg = ReikaPhysicsHelper.cartesianToPolar(dx, dy, dz);
        tg[1] = Math.abs(tg[1]) - 90 + 0.25;
        this.adjustAim(tg[2], tg[1]);
    }

    public boolean adjustAim(double ang, double incl) {
        incl += this.getThetaOffset();
        //ReikaJavaLibrary.pConsole("PHI: "+phi+" THETA: "+theta+" for "+ang+", "+incl);
        if (ang - phi > 180)
            ang -= 360;
        float movespeed = this.getAimingSpeed();
        if (phi < ang)
            phi += movespeed * 2;
        if (phi > ang)
            phi -= movespeed * 2;
        if (theta < incl)
            theta += movespeed;
        if (theta > incl)
            theta -= movespeed;
        if (theta < MAXLOWANGLE && dir == 1)
            theta = MAXLOWANGLE;
        if (theta > -MAXLOWANGLE && dir == -1)
            theta = MAXLOWANGLE;
        return ReikaMathLibrary.approxr(phi, ang, 3) && ReikaMathLibrary.approxr(theta, incl, 3);
    }

    protected double getThetaOffset() {
        return 0;
    }

    protected float getAimingSpeed() {
        return 1;
    }

    public abstract boolean hasAmmo();

    protected abstract double[] getTarget(Level world, BlockPos pos);

    public abstract void fire(Level world, double[] xyz);

    protected abstract double randomOffset();

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);
        tag.putFloat("theta", theta);
        tag.putFloat("phi", phi);
        tag.putInt("direction", dir);
    }

    @Override
    public CompoundTag serializeNBT() {
        //return super.serializeNBT();

        CompoundTag nbt = new CompoundTag();

        nbt.putInt("numsafe", numSafePlayers);

        nbt.putBoolean("aim", isCustomAim);

        for (int i = 0; i < safePlayers.size(); i++) {
            nbt.putString("Safe_Player_" + i, safePlayers.get(i));
        }
        return nbt;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        isCustomAim = nbt.getBoolean("aim");

        safePlayers = new ArrayList<String>();
        numSafePlayers = nbt.getInt("numsafe");
        for (int i = 0; i < numSafePlayers; i++) {
            safePlayers.add(nbt.getString("Safe_Player_" + i));
        }
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);
        theta = tag.getFloat("theta");
        phi = tag.getFloat("phi");
        dir = tag.getInt("direction");
    }

    @Override
    public final AABB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    protected abstract boolean isValidTarget(Entity ent);

    protected final boolean isMobOrUnlistedPlayer(LivingEntity ent) {
        return (ReikaEntityHelper.isHostile(ent) || (targetPlayers && ent instanceof Player && !this.playerIsSafe(((Player) ent))));
    }

    public void addPlayerToWhiteList(String name) {
        ReikaChatHelper.write(name + " added to " + "HEY MAX TODO placer" + "'s whitelist for the\n" + this.getName() + " at " + worldPosition.getX() + ", " + worldPosition.getY() + ", " + worldPosition.getZ() + ".");
        if (name.equals("HEY MAX TODO placer")) {
            ReikaChatHelper.write("Note: " + name + " is the owner;");
            ReikaChatHelper.write("They did not need to tell the " + this.getName() + " to not target them.");
        }
        if (FMLLoader.getDist() != Dist.DEDICATED_SERVER)
            return;
        safePlayers.add(name);
        numSafePlayers++;
    }

    public void removePlayerFromWhiteList(String name) {
        if (FMLLoader.getDist() == Dist.DEDICATED_SERVER) {
            safePlayers.remove(name);
            numSafePlayers--;
        }
        safePlayers.removeAll(Arrays.asList("", null));
        ReikaChatHelper.write(name + " removed from " + "HEY MAX TODO placer" + "'s " + this.getName() + " whitelist.");
    }

    public final boolean playerIsSafe(Player ep) {
        if (!ConfigRegistry.TURRETPLAYERS.getState())
            return true;
        if (ep.isCreative())
            return true;
        String name = ep.getName().getString();
        if (name == null)
            return true;
//        if (this.getPlacer() == null)
//            return safePlayers.contains(name);
//        if (safePlayers == null)
//            return name.equals(this.getPlacer().getCommandSenderName());
        return safePlayers.contains(name);// || name.equals(this.getPlacer().getCommandSenderName()); todo getPlacer()
    }

    public List<String> getCopyOfSafePlayerList() {
        return Collections.unmodifiableList(safePlayers);
    }

    public int getOperationTime() {
        return 20;
    }

    @Override
    public boolean areConditionsMet() {
        return this.hasAmmo();
    }

    @Override
    public String getOperationalStatus() {
        return this.areConditionsMet() ? "Operational" : "No Ammunition";
    }

}
