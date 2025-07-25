/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.weaponry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.interfaces.block.SemiUnbreakable;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.base.blockentity.BlockEntitySpringPowered;
import reika.rotarycraft.gui.container.machine.inventory.LandmineContainer;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryAdvancements;
import reika.rotarycraft.registry.RotaryBlockEntities;

import java.util.List;

public class BlockEntityLandmine extends BlockEntitySpringPowered {

    private final int explosionDelay = 0;
    private final boolean isChainExploding = false;
    private boolean flaming = false;
    private boolean poison = false;
    private boolean chain = false;
    private boolean shrapnel = false;

    public BlockEntityLandmine(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.LANDMINE.get(), pos, state);
    }

    private boolean checkForPlayer(Level world, BlockPos pos) {
        AABB above = new AABB(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 3, pos.getZ() + 1);
        List<LivingEntity> in = world.getEntitiesOfClass(LivingEntity.class, above);
        for (LivingEntity o : in) {
            if (o.onGround() && !o.isShiftKeyDown())
                return true;
        }
        return false;
    }

    private boolean checkForArrow(Level world, BlockPos pos) {
        AABB above = new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).expandTowards(1, 1, 1);
        List<Arrow> in = world.getEntitiesOfClass(Arrow.class, above);
        return in.size() > 0;
    }

    private boolean ageFail() {
        if (DragonAPI.rand.nextInt(20) > 0)
            return false;
        int age = this.getAge();
        if (age == 0)
            return false;
        return (DragonAPI.rand.nextInt(1 + 65536 - this.getAge()) == 0);
    }

    private void maxPowerExplosion(Level world, BlockPos pos) {
//        world.addParticle("hugeexplosion", pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0);
//        world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), "DragonAPI.rand.explode", 1, 1);
        for (int i = 1; i < 6; i++) {
            for (int k = 0; k < 6; k++) {
                Direction dir = Direction.values()[k];
                int dx = pos.getX() + dir.getStepX();
                int dy = pos.getY() + dir.getStepY();
                int dz = pos.getZ() + dir.getStepZ();
                Block b = world.getBlockState(new BlockPos(dx, dy, dz)).getBlock();
                if (b instanceof SemiUnbreakable && ((SemiUnbreakable) b).isUnbreakable(world, new BlockPos(dx, dy, pos.getZ())))
                    continue;
//                ReikaWorldHelper.recursiveBreakWithinSphere(world, dx, dy, dz, world.getBlockState(new BlockPos(dx, dy, dz)).getBlock(), -1, pos, 4, 0);
            }
            this.chainedExplosion(world, pos);
        }
    }

    public void detonate(Level world, BlockPos pos) {
        if (chain)
            this.chainedExplosion(world, pos);
        if (itemHandler.getStackInSlot(1) != null && !itemHandler.getStackInSlot(2).isEmpty() && !itemHandler.getStackInSlot(3).isEmpty() && !itemHandler.getStackInSlot(4).isEmpty()) {
            boolean flag = true;
            for (int i = 1; i <= 4; i++) {
        if (ReikaItemHelper.matchStackWithBlock(
            itemHandler.getStackInSlot(i), Blocks.TNT.defaultBlockState())) flag = false;
            }
            if (flag)
                this.maxPowerExplosion(world, pos);
        }
        float power = this.getExplosionPower();
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 0);
        if (flaming) {
            if (!world.isClientSide)
                world.explode(null, pos.getX(), pos.getY(), pos.getZ(), power, true, Level.ExplosionInteraction.BLOCK);
        } else if (!world.isClientSide)
            world.explode(null, pos.getX(), pos.getY(), pos.getZ(), power, Level.ExplosionInteraction.BLOCK);
        AABB region = new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).expandTowards(2, 2, 2);
        List<LivingEntity> in = world.getEntitiesOfClass(LivingEntity.class, region);
        for (int i = 0; i < in.size(); i++) {
            LivingEntity e = in.get(i);
            if (e instanceof Player) {
                if (!((Player) e).isCreative()) {
                    RotaryAdvancements.LANDMINE.triggerAchievement((Player) e);
                }
                e.hurt(e.damageSources().explosion(new Explosion(world, null, e.getX(), e.getY(), e.getZ(), power, false, Explosion.BlockInteraction.DESTROY)), (int) power * 4);
                e.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 400, 0));
                e.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 450, 5));
                if (poison)
                    e.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 0));
                if (e instanceof Creeper) {
                    world.explode(e, pos.getX(), pos.getY(), pos.getZ(), 3, Level.ExplosionInteraction.BLOCK);
                }
            }
            if (shrapnel) {
                AABB region2 = new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).expandTowards(8, 8, 8);
                List in2 = world.getEntitiesOfClass(LivingEntity.class, region2);
                for (int i2 = 0; i < in2.size(); i++) {
                    LivingEntity e2 = (LivingEntity) in2.get(i2);
                    double dx = e2.getX() - pos.getX() - 0.5;
                    double dy = e2.getY() - pos.getY() - 0.5;
                    double dz = e2.getZ() - pos.getZ() - 0.5;
                    double dd = ReikaMathLibrary.py3d(dx, dy, dz);
                    int dmg = dd < 4 ? 8 : dd < 8 ? 6 : 4;
                    e2.hurt(e.damageSources().generic(), dmg);
//                ReikaEntityHelper.spawnParticlesAround("crit", e, 8);
                }
            }
        }/*
		for (int i = -8; i <= 8; i++) {
			for (int j = -8; j <= 8; j++) {
				for (int k = -8; k <= 8; k++) {
					if (world.getBlock(x+i, y+j, z+k) == this.getBlockEntityBlockID()) {
						BlockEntity te = world.getBlockEntity(x+i, y+j, z+k);
						if (te instanceof BlockEntityLandmine)
							((BlockEntityLandmine)te).detonate(world, te.xCoord, te.yCoord, te.zCoord);
					}
				}
			}
		}*/
    }

    private void getExplosionModifiers() {
        for (int i = 5; i <= 8; i++) {
            if (itemHandler.getStackInSlot(i).isEmpty()) {
                if (itemHandler.getStackInSlot(i).getItem() == Items.BLAZE_POWDER)
                    flaming = true;
                if (itemHandler.getStackInSlot(i).getItem() == Items.SPIDER_EYE)
                    poison = true;
                if (ReikaItemHelper.matchStackWithBlock(itemHandler.getStackInSlot(i), Blocks.TNT.defaultBlockState()))
                    chain = true;
                if (ReikaItemHelper.matchStackWithBlock(itemHandler.getStackInSlot(i), Blocks.GLASS.defaultBlockState()))
                    shrapnel = true;
            }
        }
    }

    private float getExplosionPower() {
        int num = 0;
        for (int i = 1; i <= 4; i++)
            if (itemHandler.getStackInSlot(i).isEmpty()) {
                if (itemHandler.getStackInSlot(i).getItem() == Items.GUNPOWDER)
                    num++;
            }
        return 2F * num; //Each item is 1/2 block TNT (so capped at 2x)
    }

    private int getAge() {
        return 65536 - itemHandler.getStackInSlot(0).getDamageValue();
    }

    @Override
    public void openInventory() {
        super.openInventory();
        if (itemHandler.getStackInSlot(0).isEmpty())
            return;
        if (DragonAPI.rand.nextInt(65536 - this.getAge()) / 2 == 0)
            this.detonate(level, worldPosition);
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack is) {
        switch (i) {
            case 0:
                return super.isItemValidForSlot(i, is);
            case 1:
            case 2:
            case 3:
            case 4:
                return is.getItem() == Items.GUNPOWDER;
            case 5:
            case 6:
            case 7:
            case 8:
                return this.isModifier(is);
            default:
                return false;
        }
    }

    private boolean isModifier(ItemStack is) {
        if (is.getItem() == Items.BLAZE_POWDER)
            return true;
        if (is.getItem() == Items.SPIDER_EYE)
            return true;
        if (ReikaItemHelper.matchStackWithBlock(is, Blocks.TNT.defaultBlockState()))
            return true;
        return ReikaItemHelper.matchStackWithBlock(is, Blocks.GLASS.defaultBlockState());
    }

    private void chainedExplosion(Level world, BlockPos pos) {
        for (int i = 0; i < 12; i++) {
            PrimedTnt tnt = new PrimedTnt(world, pos.getX() - 5 + DragonAPI.rand.nextInt(11), pos.getY() - 5 + DragonAPI.rand.nextInt(11), pos.getZ() - 5 + DragonAPI.rand.nextInt(11), null);
            tnt.setFuse(5 + DragonAPI.rand.nextInt(10));
            if (!world.isClientSide)
                world.addFreshEntity(tnt);
        }
    }


    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.LANDMINE;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    public void updateEntity(Level world, BlockPos pos) {
        if (!this.hasCoil())
            return;
        tickcount++;
        if (tickcount > this.getUnwindTime()) {
            ItemStack is = this.getDecrementedCharged();
            itemHandler.setStackInSlot(0, is);
            tickcount = 0;
        }

        this.getExplosionModifiers();
        if (!DragonAPI.debugtest && this.ageFail())
            this.detonate(world, pos);
        if (this.checkForArrow(world, pos))
            this.detonate(world, pos);
        if (this.checkForPlayer(world, pos))
            this.detonate(world, pos);
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    @Override
    public int getRedstoneOverride() {
        return 0;
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
    public int getBaseDischargeTime() {
        return 360;
    }

    @Override
    public int getContainerSize() {
        return 9;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int i) {
        if (i < 0 || i >= this.getContainerSize()) {
            return ItemStack.EMPTY;
        }
        return itemHandler.getStackInSlot(i);
    }

    @Override
    public ItemStack removeItem(int i, int i1) {
        if (i < 0 || i >= this.getContainerSize()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = itemHandler.extractItem(i, i1, false);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        itemHandler.setStackInSlot(i, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        if (i < 0 || i >= this.getContainerSize()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = itemHandler.getStackInSlot(i);
        itemHandler.setStackInSlot(i, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        if (i < 0 || i >= this.getContainerSize()) {
            return;
        }
        itemHandler.setStackInSlot(i, itemStack);
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.isRemoved()) {
            return false;
        }
        return player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) <= 64;
    }

    @Override
    public boolean hasAnInventory() {
        return true;
    }

    @Override
    public boolean hasATank() {
        return false;
    }
    @Override
    public Component getDisplayName() {
        return Component.literal("Landmine");
    }

    @Override
    public  AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        return new LandmineContainer(p_39954_, p_39955_, this);
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            itemHandler.setStackInSlot(i, ItemStack.EMPTY);
        }
    }
}
