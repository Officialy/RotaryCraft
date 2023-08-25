/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items.tools;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;
import reika.dragonapi.auxiliary.trackers.KeyWatcher;
import reika.dragonapi.libraries.ReikaFluidHelper;
import reika.dragonapi.libraries.ReikaNBTHelper;
import reika.dragonapi.libraries.java.ReikaArrayHelper;
import reika.dragonapi.libraries.java.ReikaRandomHelper;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.api.interfaces.Fillable;
import reika.rotarycraft.base.ItemRotaryArmor;
import reika.rotarycraft.registry.*;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

import static reika.dragonapi.DragonAPI.rand;

//@Strippable(value = {"forestry.api.apiculture.IArmorApiarist"})
public class ItemJetPack extends ItemRotaryArmor implements Fillable {

    public ItemJetPack(ArmorMaterial mat) {
        super(mat, Type.CHESTPLATE, new Properties());
    }

    private static final boolean wingEnabled(ItemStack is) {
        return PackUpgrades.WING.existsOn(is) && (is.getTag() == null || is.getTag().getBoolean("wingon"));
    }

    public ArmorMaterial getMaterial() {
        return this.isBedrock() ? Materials.BEDROCK_ALLOY : Materials.HSLA_STEEL;
    }

    public int getFuel(ItemStack is) {
        CompoundTag nbt = is.getTag();
        if (nbt == null)
            return 0;
        return nbt.getInt("fuel");
    }
	/*
	public Fluid getFuelType() {
		return RotaryConfig.COMMON.JETFUELPACK.get() ? RotaryFluids.JET_FUEL : Fluids.getFluid("rc ethanol");
	}*/

    public void use(ItemStack is, int amount) {
        int newFuel = this.getFuel(is) - amount;
        if (newFuel < 0)
            newFuel = 0;

        if (is.getTag() == null)
            is.setTag(new CompoundTag());
        this.setFuel(is, this.getCurrentFluid(is).defaultFluidState(), newFuel);
    }

//    todo @Override
    protected final ItemStack onSneakClicked(ItemStack is, Player ep) {
        if (!PackUpgrades.WING.existsOn(is))
            return is;
        is.getOrCreateTag().putBoolean("wingon", !is.getOrCreateTag().getBoolean("wingon"));
        return is;
    }

    @Override
    public void onArmorTick(ItemStack is, Level world, Player player) {
        boolean flying = this.useJetpack(player, is);
        boolean fuel = this.getCurrentFillLevel(is) > 0;

        if (!PackUpgrades.COOLING.existsOn(is)) {
            if (fuel) {
                if (player.isInLava() && world.getDifficulty() != Difficulty.PEACEFUL) {
                    this.explode(world, player);
                } else if (player.isOnFire() && world.getDifficulty().ordinal() > 1 && flying) {
                    this.explode(world, player);
                }
            }
        }

        if (flying && fuel && world.getDifficulty() != Difficulty.PEACEFUL && rand.nextInt(4) == 0) {
            int x = Mth.floor(player.getX());
            int y = Mth.floor(player.getY());
            int z = Mth.floor(player.getZ());
            int dx = ReikaRandomHelper.getRandomPlusMinus(x, 1);
            int dz = ReikaRandomHelper.getRandomPlusMinus(z, 1);
            int dy = ReikaRandomHelper.getRandomBetween(y - 2, y);
            FluidState f = ReikaWorldHelper.getFluidState(world, dx, dy, dz);
            if (f != null && ReikaFluidHelper.isFlammable(f)) {
                ReikaWorldHelper.ignite(world,new BlockPos(dx, dy, dz));
            }
        }

//     todo   player.getEntityData().putBoolean("jetpack", flying);
    }

    private boolean useJetpack(Player ep, ItemStack is) {
        boolean isFlying = KeyWatcher.instance.isKeyDown(ep, KeyWatcher.Key.JUMP);
        boolean hoverMode = isFlying && ep.isCrouching();
        boolean jetbonus = !ConfigRegistry.JETFUELPACK.getState() && this.isJetFueled(is);
        boolean horiz = KeyWatcher.instance.isKeyDown(ep, KeyWatcher.Key.FORWARD) || KeyWatcher.instance.isKeyDown(ep, KeyWatcher.Key.BACK);
        horiz = horiz || KeyWatcher.instance.isKeyDown(ep, KeyWatcher.Key.LEFT) || KeyWatcher.instance.isKeyDown(ep, KeyWatcher.Key.RIGHT);
        float maxSpeed = jetbonus ? 3 : 1.25F;
        double hspeed = ReikaMathLibrary.py3d(ep.getDeltaMovement().x, 0, ep.getDeltaMovement().z);
        boolean winged = wingEnabled(is);
        boolean propel = PackUpgrades.JET.existsOn(is) && this.isJetFueled(is);
        boolean floatmode = !hoverMode;
        float thrust = winged ? 0.15F : hoverMode ? 0.05F : 0.1F;
        if (propel)
            thrust *= hoverMode ? 2 : 4;
        if (jetbonus)
            thrust *= 1.25F;
        if (ep.getVehicle() != null)
            thrust *= 1.25F;

        boolean canFly = !hoverMode || (!ep.onGround() && ep.getDeltaMovement().y < 0);
        if (isFlying && canFly) {
            if (!ep.level().isClientSide() && !ep.isCreative() && !ep.isFallFlying()) {
                if (ep.level().getGameTime() % 2 == 0)
                    this.use(is, (hoverMode ? 2 : 1) * this.getFuelUsageMultiplier());
            }

            if (this.getFuel(is) > 0) {
                if (hoverMode) {
                    if (ep.getDeltaMovement().y > 0)
                        ep.setDeltaMovement(ep.getDeltaMovement().x, Math.max(ep.getDeltaMovement().y * 0.75, 0), ep.getDeltaMovement().z);
                    else
                        ep.setDeltaMovement(ep.getDeltaMovement().x, Math.min(ep.getDeltaMovement().y + 0.15, 0), ep.getDeltaMovement().z);
                } else {
                    double deltav = ep.getDeltaMovement().y > 0 ? Math.min(0.2, Math.max(0.05, (maxSpeed - ep.getDeltaMovement().y) * 0.25)) : 0.2;
                    if (jetbonus && !horiz) {
                        deltav *= 1.5;
                    }
                    if (ep.getVehicle() != null)
                        deltav *= 1.5;
                    ep.setDeltaMovement(ep.getDeltaMovement().x, Math.min(ep.getDeltaMovement().y + deltav, maxSpeed), ep.getDeltaMovement().z);
                }

                if (KeyWatcher.instance.isKeyDown(ep, KeyWatcher.Key.FORWARD)) {
                    ep.setDeltaMovement(0, thrust, thrust);
                    if (ep.level().getGameTime() % 2 == 0 && !ep.isCreative())
                        this.use(is, this.getFuelUsageMultiplier());
                }
                if (KeyWatcher.instance.isKeyDown(ep, KeyWatcher.Key.BACK)) {
                    ep.setDeltaMovement(0, -thrust, thrust);
                    if (ep.level().getGameTime() % 2 == 0 && !ep.isCreative())
                        this.use(is, this.getFuelUsageMultiplier());
                }
                if (KeyWatcher.instance.isKeyDown(ep, KeyWatcher.Key.LEFT)) {
                    ep.setDeltaMovement(thrust, 0, thrust);
                    if (ep.level().getGameTime() % 2 == 0 && !ep.isCreative())
                        this.use(is, this.getFuelUsageMultiplier());
                }
                if (KeyWatcher.instance.isKeyDown(ep, KeyWatcher.Key.RIGHT)) {
                    ep.setDeltaMovement(-thrust, 0, thrust);
                    if (ep.level().getGameTime() % 2 == 0 && !ep.isCreative())
                        this.use(is, this.getFuelUsageMultiplier());
                }

                if (!ep.level().isClientSide) {
                    ep.fallDistance = -2;
                    if (ConfigRegistry.KICKFLYING.getState()) {
                        /*if (ep instanceof Player) {
                            ((Player) ep).playerNetServerHandler.floatingTickCount = 0;
                        }*/
                    }
                }

                float pitch = 1 + 0.5F * (float) Math.sin((ep.level().getDayTime() * 2) % 360);
                SoundRegistry.JETPACK.playSound(ep.level(), ep.getOnPos(), 0.75F, pitch);
                if (propel) {
                    SoundRegistry.SHORTJET.playSound(ep.level(), ep.getOnPos(), 0.15F, 1F);
                }
            }
        }
/*
        if (ep.motionY < 0 && winged && floatmode && !ep.isSleeping()) {
            if (!ModList.CHROMATICRAFT.isLoaded() || !ChromatiAPI.rituals.isPlayerUndergoingRitual(ep)) {
                boolean sneak = ep.isCrouching() != RotaryConfig.COMMON.SNEAKWINGS.get();
                double ang = Math.cos(Math.toRadians(ep.rotationPitch));
                double d = ep.motionY <= -2 ? 0.0625 : ep.motionY <= -1 ? 0.125 : ep.motionY <= -0.5 ? 0.25 : 0.5; //gives curve
                if (sneak)
                    d *= 0.125; //was 0.25
                double fac = 1 - (d * ang);
                ep.motionY *= fac;
                fac *= sneak ? 0.999 : 0.9;
                ep.fallDistance *= fac;
                //double diff = 0.5*ang*ep.motionY;
                //double maxdecel = jetbonus ? 0.0625 : 0.03125;
                //ep.motionY -= Math.min(diff, maxdecel);
                double dv = sneak ? 0.15 : 0.05;
                double vh = ep.onGround ? 0 : dv * ang;
                double vx = Math.cos(Math.toRadians(ep.rotationYawHead + 90)) * vh;
                double vz = Math.sin(Math.toRadians(ep.rotationYawHead + 90)) * vh;
                ep.motionX += vx;
                ep.motionZ += vz;
            }
        }*/

        if (isFlying && ep.getVehicle() != null) {
            ep.getVehicle().setDeltaMovement(ep.getDeltaMovement());
            ep.getVehicle().fallDistance = ep.fallDistance;
        }

        return isFlying;
    }

    public boolean isBedrock() {
        return this == RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get();
    }

    public boolean isSteel() {
        return this == RotaryItems.HSLA_CHESTPLATE.get();
    }

    private int getFuelUsageMultiplier() {
        return this.isBedrock() ? 2 : 1;
    }

    private void explode(Level world, Player player) {
        ItemStack to = this.isBedrock() ? RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get().getDefaultInstance() : this.isSteel() ? RotaryItems.HSLA_CHESTPLATE.get().getDefaultInstance() : null; //todo bedrock alloy enchantments
        player.setItemSlot(EquipmentSlot.CHEST, to);
        world.explode(player, player.getX(), player.getY(), player.getZ(), 2, Level.ExplosionInteraction.NONE);
        double v = 4;
        double ang = rand.nextDouble() * 360;
        double vx = v * Math.cos(Math.toRadians(ang));
        double vz = v * Math.sin(Math.toRadians(ang));
        player.setDeltaMovement(vx, 1.25, vz);
//        player.velocityChanged = true;
    }

    public int getMaxFuel(ItemStack is) {
        return 30000;
    }

    @Override
    public void appendHoverText(ItemStack is, @Nullable Level level, List<Component> li, TooltipFlag flag) {
        for (int i = 0; i < PackUpgrades.list.length; i++) {
            PackUpgrades pack = PackUpgrades.list[i];
            if (pack.existsOn(is)) {
                li.add(Component.literal(pack.label));
                if (pack == PackUpgrades.WING && !wingEnabled(is))
                    li.add(Component.literal(ChatFormatting.RED + "[Wing Disabled]"));
            }
        }
        int ch = is.getTag() != null ? is.getTag().getInt("fuel") : 0;
        li.add(Component.literal(ch > 0 ? String.format("Fuel: %d mB of %s", ch, this.getCurrentFluid(is).toString()) : "No Fuel")); //todo fluid registry name?
    }

    @Override
    public boolean isValidFluid(FluidStack f, ItemStack is) {
        Fluid f2 = this.getCurrentFluid(is);
        if (f2 != null && !f.equals(f2))
            return false;
        if (f.getFluid().equals(RotaryFluids.JET_FUEL.get()))
            return true;
//     todo   if (f.equals(Fluids.getFluid("rocket fuel")))
//            return true;
        if (f.getFluid().equals(RotaryFluids.ETHANOL.get()))
            return !ConfigRegistry.JETFUELPACK.getState();
        return false;
    }
	/*
	@Override
	public String getArmorTexture(ItemStack is, Entity e, int slot, String nulll) {
		RotaryItems i = RotaryItems.getEntry(is);
		if (i == RotaryItems.BEDROCK_ALLOY_CHESTPLATE)
			return "/Reika/RotaryCraft/Textures/Misc/bedrock_jet.png";
		if (i == RotaryItems.JETPACK)
			return "/Reika/RotaryCraft/Textures/Misc/jet.png";
		return "";
	}*/

    @Override
    public int getCapacity(ItemStack is) {
        return this.getMaxFuel(is);
    }

    @Override
    public int getCurrentFillLevel(ItemStack is) {
        return this.getFuel(is);
    }

    private void setFuel(ItemStack is, FluidState f, int amt) {
        is.getOrCreateTag().putInt("fuel", amt);
        if (amt > 0) {
            ReikaNBTHelper.writeFluidToNBT(is.getTag(), new FluidStack(f.getType(), 0));
        } else {
            ReikaNBTHelper.writeFluidToNBT(is.getTag(), null);
        }
    }

    @Override
    public int addFluid(ItemStack is) {
        return 0;
    }

//    @Override
    public int addFluid(ItemStack is, FluidState f, int amt) { //todo add fluid for jetpack
        if (f == null || !this.isValidFluid(new FluidStack(f.getType(), 0), is))
            return 0;
        CompoundTag nbt = is.getTag();
        if (nbt == null) {
            is.setTag(new CompoundTag());
            this.setFuel(is, f, amt);
            return amt;
        } else {
            int cap = this.getCapacity(is);
            int cur = nbt.getInt("fuel");
            int sum = cur + amt;
            if (sum > cap) {
                this.setFuel(is, f, cap);
                return cap - cur;
            } else {
                this.setFuel(is, f, sum);
                return amt;
            }
        }
    }

    @Override
    public boolean providesProtection() {
        return this.isBedrock() || this.isSteel();
    }
	/*
	@Override
	public int getItemSpriteIndex(ItemStack item) {
		RotaryItems ir = RotaryItems.getEntry(item);
		return ir != null ? ir.getTextureIndex() : 0;
	}
	 *//*
	@Override
	public int getItemSpriteIndex(ItemStack item) {
		int a = this.isWinged(item) ? 32 : 0;
		return a+super.getItemSpriteIndex(item);
	}*/

    @Override
    public boolean canBeDamaged() {
        return this.isSteel();
    }

/*todo    @Override
    public double getDamageMultiplier(DamageSource src) {
        if (this.isBedrock())
            return ((ItemBedrockArmor) RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get()).getDamageMultiplier(src);
        else if (this.isSteel())
            return ((ItemSteelArmor) RotaryItems.HSLA_CHESTPLATE.get()).getDamageMultiplier(src);
        else
            return 1;
    }*/

    @Override
    public boolean isFull(ItemStack is) {
        return this.getCurrentFillLevel(is) >= this.getCapacity(is);
    }

    @Override
    public Fluid getCurrentFluid(ItemStack is) {
        if (is.getTag() == null)
            return null;
        int lvl = this.getCurrentFillLevel(is);
        Fluid f = ReikaNBTHelper.getFluidFromNBT(is.getTag()).getFluid();
        if (lvl > 0 && f == null) {
            this.setFuel(is, null, 0);
            return null;
        }
        return lvl > 0 ? f : null;
    }

    public boolean isJetFueled(ItemStack is) {
        Fluid f = this.getCurrentFluid(is);
        return f != null && f.equals(RotaryFluids.JET_FUEL.get());
    }

    public EnumSet<PackUpgrades> getUpgrades(ItemStack is) {
        EnumSet<PackUpgrades> set = EnumSet.noneOf(PackUpgrades.class);
        for (int i = 0; i < PackUpgrades.list.length; i++) {
            PackUpgrades p = PackUpgrades.list[i];
            if (p.existsOn(is)) {
                set.add(p);
            }
        }
        return set;
    }

//    @Override
    public int[] getIndices(ItemStack is) {
        ArrayList li = new ArrayList<>();
// todo       li.add(this.getItemSpriteIndex(is));
        if (PackUpgrades.WING.existsOn(is)) {
            int w = this.isBedrock() ? 59 : this.isSteel() ? 61 : 60;
            li.add(w);
        }
        return ReikaArrayHelper.intListToArray(li);
    }

    @Override
    public boolean isValidRepairItem(ItemStack tool, ItemStack item) {
        return tool.getItem() == this && this.isSteel() && ReikaItemHelper.matchStacks(item, RotaryItems.HSLA_STEEL_INGOT);
    }

//    @Override
    public boolean protectEntity(LivingEntity entity, ItemStack armor, String cause, boolean doProtect) {
        ItemStack head = entity.getItemBySlot(EquipmentSlot.HEAD);
        return head != null;// && head.getItem() instanceof IArmorApiarist && ((IArmorApiarist) head.getItem()).protectEntity(entity, head, cause, doProtect);
    }

    //@Override
    @Deprecated
    public boolean protectPlayer(Player player, ItemStack armor, String cause, boolean doProtect) {
        return this.protectEntity(player, armor, cause, doProtect);
    }

    @Override
    public final void setDamage(ItemStack stack, int damage) {

    }

    public enum PackUpgrades {
        WING("Winged"),
        JET("Thrust Boost"),
        COOLING("Fin-Cooled");

        private static final PackUpgrades[] list = values();
        public final String label;

        PackUpgrades(String s) {
            label = s;
        }

        public boolean existsOn(ItemStack is) {
            return is.getTag() != null && is.getTag().getBoolean(this.getNBT());
        }

        private String getNBT() {
            return this.name().toLowerCase(Locale.ENGLISH);
        }

        public void enable(ItemStack is, boolean set) {
            is.getOrCreateTag();
            is.getTag().putBoolean(this.getNBT(), set);
            if (this == WING)
                is.getTag().putBoolean("wingon", true);
        }
    }
}
