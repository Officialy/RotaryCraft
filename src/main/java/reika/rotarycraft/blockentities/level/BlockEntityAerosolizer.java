/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.level;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.neoforged.fluids.FluidStack;
import net.neoforged.fluids.capability.IFluidHandler;

import reika.dragonapi.libraries.java.ReikaArrayHelper;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.api.interfaces.CustomPotion;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;

import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryFluids;

import java.util.ArrayList;
import java.util.List;

public class BlockEntityAerosolizer extends InventoriedPowerReceiver implements RangedEffect, ConditionalOperation, IFluidHandler {

    public static final int MAXRANGE = Math.max(64, ConfigRegistry.AERORANGE.getValue());
    public static final int CAPACITY = 64;

    private final PotionApplication[] potions = new PotionApplication[9];
    public boolean idle = false;
    private int[] potionLevel = new int[9];
    private int tickcount2 = 0;

    public BlockEntityAerosolizer(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.AEROSOLIZER.get(), pos, state);
    }

    public void testIdle() {
        boolean empty = true;
        for (int i = 0; i < 9; i++) {
            if (potions[i] != null) {
                empty = false;
                break;
            }
        }
        idle = empty;
    }

    @Override
    public void updateBlockEntity() {
        super.updateBlockEntity();
        power = (long) omega * (long) torque;
        this.getSummativeSidedPower();
        tickcount++;
        tickcount2++;
        this.consumeBottlesAndStorePotions();
        if (power < MINPOWER)
            return;
        this.testIdle();
        for (int i = 0; i < 9; i++) {
            if (tickcount2 >= 20 / Math.max(this.getMultiplier(i), 1)) {
                //this.potionDamage[i] = this.getPotion(i);
                AABB room = this.getRoom(level, worldPosition);
                if (potionLevel[i] > 0)
                    this.dispense2(level, worldPosition, room, i);
                if (i == 8)
                    tickcount2 = 0;
            }
            if (tickcount >= 2400 && potionLevel[i] > 0) {
                potionLevel[i]--;
                if (i == 8)
                    tickcount = 0;
            }
            if (potionLevel[i] <= 0) {
                potionLevel[i] = 0;
                if (i == 8)
                    tickcount = 0;
            }
        }
    }

    private void consumeBottlesAndStorePotions() {
        if (level.isClientSide)
            return;
        for (int i = 0; i < 9; i++) {
            ItemStack inslot = this.getStackInSlot(i);
            if (inslot != null) {
                PotionApplication eff = this.getEffectFromItem(inslot);
                if (eff != null) {
                    int num = inslot.getCount() * eff.amount;
                    if (this.tryAddPotionToSlot(i, num, eff))
                        this.setInventorySlotContents(i, new ItemStack(Items.GLASS_BOTTLE, inslot.getCount()));
                }
            }
        }
    }

    private boolean tryAddPotionToSlot(int i, int num, PotionApplication eff) {
        if (this.matchEffects(eff, potions[i]) && potionLevel[i] + num <= CAPACITY) {
            potions[i] = eff;
            potionLevel[i] += num;
            if (potionLevel[i] > CAPACITY)
                potionLevel[i] = CAPACITY;
            return true;
        }
        return false;
    }

    private boolean matchEffects(PotionApplication eff1, PotionApplication eff2) {
        return eff1 == eff2 || eff1 == null || eff2 == null || eff1.equals(eff2);
    }

    public int getPotionColor(int slot) {
        return potions[slot] != null ? (0xff000000 | potions[slot].renderColor) : 0xff000000;
    }

    public int getPotionLevel(int slot) {
        return potionLevel[slot];
    }

    private PotionApplication getEffectFromItem(ItemStack is) { // add mod potion support
        Item i = is.getItem();
        if (i instanceof PotionItem) {
			/*
			int id = ReikaPotionHelper.getPotionID(dmg);
			if (id != -1) {
				Potion p = Potion.potionTypes[id];
				if (p != null && !p.isInstant()) {
					boolean extended = PotionHelper.checkFlag(dmg, 6); //Bit 6 is enhanced
					boolean level2 = PotionHelper.checkFlag(dmg, 5); //Bit 5 is extended
					return new PotionApplication(ReikaJavaLibrary.makeListFrom(new MobEffect(p.id, 0)), extended ? 3 : 1, level2 ? 1 : 0);
				}
			}
			 */
            List<MobEffectInstance> li = PotionUtils.getMobEffects(is);
            for (MobEffectInstance p : li) {
                if (!p.getEffect().isInstantenous()) {
                    boolean extended = false;//todo PotionHelper.checkFlag(dmg, 6); //Bit 6 is enhanced
                    boolean level2 = p.getAmplifier() > 0;
                    return new PotionApplication(ReikaJavaLibrary.makeListFrom(new MobEffectInstance(p.getEffect(), 0)), extended ? 3 : 1, level2 ? 1 : 0);
                }
            }
        } else if (i instanceof CustomPotion cp) {
            Potion p = cp.getPotion(is);
            if (p != null && !p.hasInstantEffects()) {
                boolean extended = cp.isExtended(is);
                boolean level2 = cp.getAmplifier(is) > 0;
                return new PotionApplication(ReikaJavaLibrary.makeListFrom(new MobEffectInstance(p.getEffects().get(0).getEffect() /*todo fix get(0) as this might break a lot of shit*/, 0)), extended ? 3 : 1, level2 ? 1 : 0);
            }
        }
        return null;
    }

    public int getLiquidScaled(int par1, int par2) {
        return (par2 * par1) / CAPACITY;
    }

    private AABB getRoom(Level world, BlockPos pos) {
        int minx = pos.getX();
        int maxx = pos.getX() + 1;
        int miny = pos.getY();
        int maxy = pos.getY() + 1;
        int minz = pos.getZ();
        int maxz = pos.getZ() + 1;

        boolean exit = false;
        for (int i = 1; i < this.getRange() && !exit; i++) {
            if (world.getBlockState(new BlockPos(pos.getX() + i, pos.getY(), pos.getZ())).isSolidRender(world, pos))
                exit = true;
            else
                maxx = pos.getX() + i;
        }
        exit = false;
        for (int i = 1; i < this.getRange() && !exit; i++) {
            if (world.getBlockState(new BlockPos(pos.getX() - i, pos.getY(), pos.getZ())).isSolidRender(world, pos))
                exit = true;
            else
                minx = pos.getX() - i;
        }
        exit = false;
        for (int i = 1; i < this.getRange() && !exit; i++) {
            if (world.getBlockState(new BlockPos(pos.getX(), pos.getY() + i, pos.getZ())).isSolidRender(world, pos))
                exit = true;
            else
                maxy = pos.getY() + i;
        }
        exit = false;
        for (int i = 1; i < this.getRange() && !exit; i++) {
            if (world.getBlockState(new BlockPos(pos.getX(), pos.getY() - i, pos.getZ())).isSolidRender(world, pos))
                exit = true;
            else
                miny = pos.getX() - i;
        }
        exit = false;
        for (int i = 1; i < this.getRange() && !exit; i++) {
            if (world.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ() + i)).isSolidRender(world, pos))
                exit = true;
            else
                maxz = pos.getZ() + i;
        }
        exit = false;
        for (int i = 1; i < this.getRange() && !exit; i++) {
            if (world.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - i)).isSolidRender(world, pos))
                exit = true;
            else
                minz = pos.getZ() - i;
        }
        exit = false;

        return new AABB(minx, miny, minz, maxx, maxy, maxz);
    }

    private void dispense2(Level world, BlockPos pos, AABB room, int i) { // id, duration, amplifier
        if (!level.isClientSide) {
            if (potions[i] != null) {
                List<MobEffectInstance> effects = potions[i].effects;
                if (effects != null && !effects.isEmpty()) {
                    List<LivingEntity> inroom = level.getEntitiesOfClass(LivingEntity.class, room);
                    for (LivingEntity mob : inroom) {
                        for (MobEffectInstance effect : effects) {

                            int bonus = this.getMultiplier(i) - 1;  //-1 since adding
                            if (effect.getAmplifier() == 1)
                                bonus *= 2;
                            mob.addEffect(new MobEffectInstance(effect.getEffect(), 100, effect.getAmplifier() + bonus));
                        }
                    }
                }
            }
        }
    }

    public int getMultiplier(int i) {
        if (potions[i] == null)
            return 0;
        return this.countCopies(potions[i]);
    }

    private int countCopies(PotionApplication p) {
        int c = 0;
        for (int i = 0; i < 9; i++) {
            PotionApplication in = potions[i];
            if (in != null && in.equals(p)) {
                c++;
            }
        }
        return c;
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        for (int i = 0; i < 9; i++) {
            if (NBT.contains("potion_" + i)) {
                CompoundTag tag = NBT.getCompound("potion_" + i);
                potions[i] = PotionApplication.load(tag);
            } else {
                potions[i] = null;
            }
        }
        potionLevel = NBT.getIntArray("levels");
    }

    @Override
    protected String getTEName() {
        return null;
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        for (int i = 0; i < 9; i++) {
            if (potions[i] != null) {
                CompoundTag tag = new CompoundTag();
                potions[i].saveAdditional(tag);
                NBT.put("potion_" + i, tag);
            }
        }
        NBT.putIntArray("levels", potionLevel);
    }

    public int getContainerSize() {
        return 9;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }


    @Override
    public int getRange() {
        return MAXRANGE;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.AEROSOLIZER;
    }

    @Override
    public int getMaxRange() {
        return MAXRANGE;
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

    //  @Override
    public int getRedstoneOverride() {
        return ((int) ((ReikaArrayHelper.sumArray(potionLevel) / 576D) * 15));
    }

    @Override
    public boolean areConditionsMet() {
        for (int i = 0; i < potionLevel.length; i++) {
            if (potionLevel[i] > 0)
                return true;
        }
        return false;
    }

    @Override
    public String getOperationalStatus() {
        return this.areConditionsMet() ? "Operational" : "No Potions";
    }

    public boolean isValidFluid(Fluid f) {
        return f == RotaryFluids.POISON.get() || f == RotaryFluids.CHLORINE.get();
    }

    public boolean canReceiveFrom(Direction from) {
        return from != Direction.UP;
    }

    //  @Override
    public FluidStack drain(Direction from, FluidStack resource, boolean doDrain) {
        return null;
    }

    // @Override
    public FluidStack drain(Direction from, int maxDrain, boolean doDrain) {
        return null;
    }

    //@Override
    public boolean canFill(Direction from, Fluid fluid) {
        return this.isValidFluid(fluid) && this.canReceiveFrom(from);
    }

    // @Override
    public boolean canDrain(Direction from, Fluid fluid) {
        return false;
    }

//    @Override
//    public FluidTankInfo[] getTankInfo(Direction from) {
//        return null;
//    }

    @Override
    public int getTanks() {
        return 0;
    }

    
    @Override
    public FluidStack getFluidInTank(int tank) {
        return null;
    }

    @Override
    public int getTankCapacity(int tank) {
        return 0;
    }

    @Override
    public boolean isFluidValid(int tank,  FluidStack stack) {
        return false;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return fill(null, resource, action == FluidAction.EXECUTE);
    }

    //@Override
    public int fill(Direction from, FluidStack resource, boolean doFill) {
        if (doFill && this.canFill(from, resource.getFluid())) {
            for (int i = 0; i < 9; i++) {
                PotionApplication eff = this.getEffectFromItem(PotionUtils.setPotion(new ItemStack(Items.POTION, 1), Potions.POISON));
                if (this.tryAddPotionToSlot(i, resource.getAmount(), eff))
                    return resource.getAmount();
            }
        }
        return 0;
    }

    
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return null;
    }

    
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return null;
    }

//    @Override
//    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//        return itemstack.getItem() == Items.GLASS_BOTTLE;
//    }

    //    @Override
    public boolean isItemValid(int slot,  ItemStack stack) {
        return stack.getItem() == Items.POTION;
    }

    @Override
    public boolean hasAnInventory() {
        return true;
    }

    @Override
    public boolean hasATank() {
        return true;
    }

    private static class PotionApplication {

        public final int renderColor;
        private final List<MobEffectInstance> effects;
        private final int amount;
        private final int potionLevel;

        private PotionApplication(List<MobEffectInstance> li, int amt, int lvl) {
            effects = li;
            amount = amt;
            potionLevel = lvl;
            //Collections.sort(effects, ReikaPotionHelper.effectSorter);

            renderColor = this.calcColor(effects);
        }

        public static PotionApplication load(CompoundTag NBT) {
            int amt = NBT.getInt("amount");
            int lvl = NBT.getInt("level");
            int c = NBT.getInt("color");
            ArrayList<MobEffectInstance> fx = new ArrayList<>();
            ListTag li = NBT.getList("effects", Tag.TAG_COMPOUND);
            for (Object o : li) {
                CompoundTag tag = (CompoundTag) o;
                MobEffectInstance p = (MobEffectInstance) PotionUtils.getCustomEffects(tag);
                fx.add(p);
            }
            return new PotionApplication(fx, amt, lvl);
        }

        private int calcColor(List<MobEffectInstance> li) {
            int sum = 0;
            for (MobEffectInstance p : li) {
                //sum += Potion.potionTypes[p.getPotionID()].getLiquidColor(); todo check if getcolor works for potions
                sum = p.getEffect().getColor();
            }
            return sum / li.size();
        }

        public void saveAdditional(CompoundTag NBT) {
            ListTag li = new ListTag();
            for (MobEffectInstance eff : effects) {
                CompoundTag tag = new CompoundTag();
                eff.save(tag);//writeCustomMobEffectToNBT(tag);
                li.add(tag);
            }
            NBT.put("effects", li);
            NBT.putInt("amount", amount);
            NBT.putInt("level", potionLevel);
            NBT.putInt("color", renderColor);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof PotionApplication p) {
                return p.potionLevel == potionLevel && this.matchEffects(p);
            }
            return false;
        }

        private boolean matchEffects(PotionApplication p) {
            if (effects.size() != p.effects.size())
                return false;
            for (int i = 0; i < effects.size(); i++) {
                MobEffectInstance p1 = effects.get(i);
                MobEffectInstance p2 = p.effects.get(i);
                //if (!(p1 == p2 && p1.getAmplifier() == p2.getAmplifier())) {
                //    return false;
                //}
            }
            return true;
        }

        @Override
        public int hashCode() {
            return (amount | (potionLevel << 8)) << 16 | effects.hashCode();
        }

    }
}
