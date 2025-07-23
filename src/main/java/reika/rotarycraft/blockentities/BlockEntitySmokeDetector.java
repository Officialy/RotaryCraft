package reika.rotarycraft.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blockentity.BlockEntitySpringPowered;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.SoundRegistry;

public class BlockEntitySmokeDetector extends BlockEntitySpringPowered implements RangedEffect {

    public int  soundDelay = -1;
    private int unwindTick = 0;
    private boolean isAlarm  = false;
    private boolean isLowBatt = false;

    /* --------------------------------------------------------------------- */
    /*  Construction                                                         */
    /* --------------------------------------------------------------------- */
    public BlockEntitySmokeDetector(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.SMOKE_DETECTOR.get(), pos, state);
    }

    /* --------------------------------------------------------------------- */
    /*  State helpers                                                        */
    /* --------------------------------------------------------------------- */
    public boolean isAlarming()        { return isAlarm; }
    public boolean lowBattery()        {
        return hasCoil() && itemHandler.getStackInSlot(0).getTag().getInt("power") <= 8;
    }
    public int     getRange()          {
        if (!hasCoil()) return 0;
        int dmg = itemHandler.getStackInSlot(0).getTag().getInt("power");
        int val = (int) ReikaMathLibrary.logbase(dmg * dmg, 2);
        return Math.min(val, 8);
    }

    /* --------------------------------------------------------------------- */
    /*  Tick logic                                                           */
    /* --------------------------------------------------------------------- */
    @Override
    public void updateEntity(Level world, BlockPos pos) {
        tickcount++;
        unwindTick++;

        if (!hasCoil())         return;
        if (unwindTick >= getUnwindTime()) {
            itemHandler.setStackInSlot(0, getDecrementedCharged());
            unwindTick = 0;
        }

        boolean foundFire = ReikaWorldHelper.findNearBlock(world, pos, 8, Blocks.FIRE);
        if (foundFire != isAlarm) {
            isAlarm = foundFire;
            ReikaWorldHelper.causeAdjacentUpdates(world, pos);
        }

        isLowBatt = lowBattery();

        soundDelay = isAlarm ? 4 : (isLowBatt ? 600 : -1);

        if (tickcount >= soundDelay && soundDelay != -1) {
            tickcount = 0;
            SoundRegistry.SMOKE.playSoundAtBlock(world, pos, 0.1F, 1);
            world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(),
                    SoundEvents.AMETHYST_BLOCK_CHIME,
                    SoundSource.BLOCKS, 1, 1, false);
        }
    }

    /* --------------------------------------------------------------------- */
    /*  Inventory I/O                                                        */
    /* --------------------------------------------------------------------- */
    @Override public int  getContainerSize()           { return 1; }
    @Override public boolean hasAnInventory()          { return true; }
    @Override public boolean hasATank()                { return false; }
    @Override public boolean isEmpty()                 { return itemHandler.getStackInSlot(0).isEmpty(); }

    @Override
    public ItemStack getItem(int slot) {
        return slot == 0 ? itemHandler.getStackInSlot(0) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return slot == 0 ? itemHandler.extractItem(0, amount, false) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot != 0) return ItemStack.EMPTY;
        ItemStack stack = itemHandler.getStackInSlot(0);
        itemHandler.setStackInSlot(0, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot == 0) itemHandler.setStackInSlot(0, stack);
    }

    @Override
    public void clearContent() {
        itemHandler.setStackInSlot(0, ItemStack.EMPTY);
    }

    @Override
    public boolean stillValid(Player player) {
        return !isRemoved() && player.distanceToSqr(
                worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) <= 64;
    }

    /* --------------------------------------------------------------------- */
    /*  Misc overrides                                                       */
    /* --------------------------------------------------------------------- */
    @Override protected void animateWithTick(Level level, BlockPos pos) {}
    @Override public boolean hasModelTransparency()    { return false; }
    @Override public Block getBlockEntityBlockID()     { return RotaryBlocks.SMOKE_DETECTOR.get(); }
    @Override public MachineRegistry getMachine()      { return MachineRegistry.SMOKEDETECTOR; }
    @Override public int  getMaxRange()                { return getRange(); }
    @Override public int  getRedstoneOverride()        { return 0; }
    @Override public int  getBaseDischargeTime()       { return 1200; }
    @Override protected String getTEName()             { return "SmokeDetector"; }
}
