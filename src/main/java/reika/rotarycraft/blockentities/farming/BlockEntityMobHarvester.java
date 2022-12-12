/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.farming;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.libraries.ReikaEntityHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.auxiliary.HarvesterDamage;
import reika.rotarycraft.auxiliary.MachineEnchantmentHandler;
import reika.rotarycraft.auxiliary.interfaces.EnchantableMachine;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;

import java.util.List;

public class BlockEntityMobHarvester extends BlockEntityPowerReceiver implements EnchantableMachine {

    private final MachineEnchantmentHandler enchantments = new MachineEnchantmentHandler().addFilter(Enchantments.INFINITY_ARROWS).addFilter(Enchantments.SHARPNESS).addFilter(Enchantments.FIRE_ASPECT).addFilter(Enchantments.SILK_TOUCH).addFilter(Enchantments.MOB_LOOTING);

    public String owner;
    public boolean laser;

    public BlockEntityMobHarvester(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.MOB_HARVESTER.get(), pos, state);
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        //ReikaJavaLibrary.pConsole(this.hasEnchantments());
        //this.tickcount++;
        this.getSummativeSidedPower();
        if (power < MINPOWER)
            return;
        Player ep = this.getPlacer();
        //if (this.tickcount < 5)
        //return;
        //this.tickcount = 0;
        boolean oneplus = false;
        AABB box = this.getBox();
        List<LivingEntity> inbox = world.getEntitiesOfClass(LivingEntity.class, box);
        for (LivingEntity ent : inbox) {
            if (!(ent instanceof Villager)) {
                oneplus = true;
                if (ep != null) {
                    int dmg = this.getDamage();
                    if (dmg > 0) {
                        ent.hurt(new HarvesterDamage(this), dmg);
                        if (enchantments.hasEnchantment(Enchantments.SILK_TOUCH) && DragonAPI.rand.nextInt(20) == 0)
                            ReikaEntityHelper.dropHead(ent);
                        //Looting is handled with the LivingDropsEvent
                        if (enchantments.hasEnchantment(Enchantments.FIRE_ASPECT))
                            ent.setSecondsOnFire(enchantments.getEnchantment(Enchantments.FIRE_ASPECT) * 2);
                    }
                }
                ent.setDeltaMovement(ent.getDeltaMovement().x(), 0, ent.getDeltaMovement().z());
            }
        }
        laser = oneplus;
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    public int getDamage() {
        double pdiff = 2 + (0.5 * power / MINPOWER);
        double ppdiff = ReikaMathLibrary.intpow(pdiff, 6);
        double base = ReikaMathLibrary.logbase(ppdiff, 2) + 2 * enchantments.getEnchantment(Enchantments.SHARPNESS);
//        if (ModList.CHROMATICRAFT.isLoaded()) {
//            base *= ChromatiAPI.adjacency.getFactorSimple(level, worldPosition, "PINK");
//        }
        return (int) base;
    }

    public AABB getBox() {
        //return AABB.getBoundingBox(this.xCoord-4, this.yCoord-4, this.zCoord-4, this.xCoord+5, this.yCoord+5, this.zCoord+5);
        return new AABB(worldPosition.getX(), worldPosition.getY() + 1, worldPosition.getZ(), worldPosition.getX() + 1, worldPosition.getY() + this.getHeight() + 1, worldPosition.getZ() + 1);
    }

    private int getHeight() {
        return Math.min(6, 2 + Math.max(0, (int) power / 524288));
    }

    public AABB getLaser() {
        return new AABB(worldPosition.getX() + 0.4, worldPosition.getY() + 1, worldPosition.getZ() + 0.4, worldPosition.getX() + 0.6, worldPosition.getY() + this.getHeight(), worldPosition.getZ() + 0.6);
    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);
        if (owner != null && !owner.isEmpty())
            tag.putString("sowner", owner);

        tag.put("enchants", enchantments.saveAdditional());
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);
        owner = tag.getString("sowner");

        enchantments.load(tag.getList("enchants", Tag.TAG_COMPOUND));
    }

    @Override
    protected String getTEName() {
        return null;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }


    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.MOBHARVESTER;
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    public MachineEnchantmentHandler getEnchantmentHandler() {
        return enchantments;
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
