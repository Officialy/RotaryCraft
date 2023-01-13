/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.production;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.instantiable.StepTimer;
import reika.dragonapi.interfaces.blockentity.BreakAction;
import reika.dragonapi.libraries.ReikaAABBHelper;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.dragonapi.libraries.java.ReikaRandomHelper;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.api.interfaces.RefrigeratorAttachment;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.auxiliary.interfaces.MultiOperational;
import reika.rotarycraft.base.blockentity.InventoriedPowerLiquidProducer;
import reika.rotarycraft.registry.*;

import java.util.List;

public class BlockEntityRefrigerator extends InventoriedPowerLiquidProducer implements MultiOperational, BreakAction {

    private final StepTimer timer = new StepTimer(20);
    private final StepTimer soundTimer = new StepTimer(20);
    private final RefrigeratorAttachment[] attachments = new RefrigeratorAttachment[6];
    public int time;

    public BlockEntityRefrigerator(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.REFRIGERATOR.get(), pos, state);
    }


    public static void doBreakFX(Level world, BlockPos pos) {
        for (int i = 0; i < 15; i++) {
            double dx = ReikaRandomHelper.getRandomPlusMinus(0, 0.5);
            double dz = ReikaRandomHelper.getRandomPlusMinus(0, 0.5);
            double dy = ReikaRandomHelper.getRandomPlusMinus(0, 0.25);
            double v = 0.04;
//            EntityBlurFX fx = new EntityBlurFX(world, pos.getX() + 0.5 + dx, pos.getY() + 0.5 + dy, pos.getZ() + 0.5 + dz, dx * v, dy * v, dz * v, IconPrefabs.FADE_GENTLE.getIcon());
//            fx.setColor(0xBFB2FF).setScale(3 + DragonAPI.rand.nextFloat() * 2).setRapidExpand().setAlphaFading().setLife(30 + DragonAPI.rand.nextInt(31)).setColliding();
//            Minecraft.getInstance().effectRenderer.addEffect(fx);
        }
    }

    public void updateEntity(Level world, BlockPos pos) {
//        this.getIOSides(world, pos);
        this.getPower(false);
        timer.setCap(this.getOperationTime());

        if (this.canProgress()) {
            soundTimer.update();
            if (soundTimer.checkCap()) {
                SoundRegistry.FRIDGE.playSoundAtBlock(world, pos, RotaryAux.isMuffled(this) ? 0.25F : 1, 0.88F);
            }
        } else {
            soundTimer.reset();
        }

        int n = this.getNumberConsecutiveOperations();
        for (int i = 0; i < n; i++)
            this.doOperation(n > 1);

        if (!world.isClientSide)
            time = timer.getTick();
    }

    private void doOperation(boolean multiple) {
        if (this.canProgress()) {
            timer.update();
            if (multiple || timer.checkCap()) {
                if (!level.isClientSide)
                    this.cycle();
            }
        } else {
            timer.reset();
        }
    }

    private boolean canProgress() {
        if (power < MINPOWER || torque < MINTORQUE)
            return false;
        if (inv[0] == null)
            return false;
        if (!tank.canTakeIn(this.getProducedLN2()))
            return false;
        return ReikaItemHelper.matchStackWithBlock(inv[0], Blocks.ICE.defaultBlockState()) && (inv[1] == null || inv[1].getCount() < inv[1].getMaxStackSize());
    }

    private void cycle() {
        ReikaInventoryHelper.decrStack(0, inv);
        int amt = this.getProducedLN2();
        tank.addLiquid(amt, RotaryFluids.LIQUID_NITROGEN.get());
        if (amt > 0) {
            for (RefrigeratorAttachment r : attachments) {
                if (r != null) {
                    r.onCompleteCycle(amt);
                }
            }
            if (DragonAPI.rand.nextInt(4) == 0) {
                int n = DragonAPI.rand.nextInt(20) == 0 ? 4 : (DragonAPI.rand.nextInt(4) == 0 ? 2 : 1);
                if (inv[1] != null)
                    n = Math.min(n, RotaryItems.DRY_ICE.get().getMaxStackSize() - inv[1].getCount());
                ReikaInventoryHelper.addOrSetStack(ReikaItemHelper.getSizedItemStack(RotaryItems.DRY_ICE.get().getDefaultInstance(), n), inv, 1);
            }
        }
    }

    private int getProducedLN2() {
        int over = torque / MINTORQUE;
        return Math.min(2000, 100 * over * over);
    }

    public void setLevel(int lvl) {
        if (!tank.isEmpty())
            tank.setContents(lvl, tank.getActualFluid().getFluid());
    }

    public int getProgressScaled(int a) {
        return time * a / this.getOperationTime();
    }


    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return i == 1;
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return m.isStandardPipe();
    }

    @Override
    public FluidStack drain(Direction from, int maxDrain, FluidAction doDrain) {
        return null;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack is) {
        return slot == 0 && ReikaItemHelper.matchStackWithBlock(is, Blocks.ICE.defaultBlockState());
    }

    @Override
    public boolean canOutputTo(Direction to) {
        return true;
    }

    @Override
    public int getCapacity() {
        return 12000;
    }


    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.REFRIGERATOR;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    public int getOperationTime() {
        return DurationRegistry.REFRIGERATOR.getOperationTime(omega);
    }

    @Override
    public int getNumberConsecutiveOperations() {
        return DurationRegistry.REFRIGERATOR.getNumberOperations(omega);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);

        time = NBT.getInt("timer");
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);

        NBT.putInt("timer", time);
    }

    public int getLiquidScaled(int i) {
        return tank.getFluidLevel() * i / tank.getCapacity();
    }

    public void addAttachment(RefrigeratorAttachment te, Direction dir) {
        attachments[dir.ordinal()] = te;
    }

    @Override
    protected void onPlacedNextToThis(BlockEntity te, Direction dir) {
        attachments[dir.ordinal()] = null;
    }

    @Override
    public void breakBlock() {
        float f = tank.getFraction();
        if (f > 0.1) {
//todo            ReikaSoundHelper.playSoundAtBlock(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), "dragonAPI.rand.fizz", 1.2F, 0.8F);
            float hearts = f * 4;
            AABB box = ReikaAABBHelper.getBlockAABB(this).expandTowards(5, 5, 5);
            List<LivingEntity> li = level.getEntitiesOfClass(LivingEntity.class, box);
            RotaryCraft.freezeDamage.lastMachine = this;
            for (LivingEntity e : li) {
                e.hurt(RotaryCraft.freezeDamage, hearts * 2);
            }
            ReikaPacketHelper.sendDataPacketWithRadius(RotaryCraft.packetChannel, PacketRegistry.FRIDGEBREAK.ordinal(), this, 24);
        }
    }

}
