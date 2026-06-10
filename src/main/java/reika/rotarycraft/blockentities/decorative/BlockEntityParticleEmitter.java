package reika.rotarycraft.blockentities.decorative;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.instantiable.storage.ManagedItemHandler;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.base.OneSlotMachine;
import reika.dragonapi.interfaces.blockentity.InertIInv;
import reika.dragonapi.libraries.registry.ReikaParticleHelper;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blockentity.BlockEntitySpringPowered;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * Spring-powered decorative tile that continuously emits arbitrary particles.
 * Slot 0 holds a charged HSLA steel spring; the spring drains over time using
 * {@link BlockEntitySpringPowered} utilities. Redstone control is optional.
 */
public class BlockEntityParticleEmitter extends BlockEntitySpringPowered
        implements OneSlotMachine, InertIInv, RangedEffect {

    /* --------------------------------------------------------------------- */
    /*  Configurable fields (synced)                                         */
    /* --------------------------------------------------------------------- */
    public ReikaParticleHelper particleType   = ReikaParticleHelper.SMOKE;
    public double pX = 0, pY = 0, pZ = 0;   // motion vector
    public int    particlesPerTick = 3;
    public boolean useRedstone     = false;

    /* --------------------------------------------------------------------- */
    /*  Inventory (single-slot)                                              */
    /* --------------------------------------------------------------------- */
    private final ManagedItemHandler inv = new ManagedItemHandler(1) {
        @Override protected void onContentsChanged(int slot) { setChanged(); }
    };

    /* --------------------------------------------------------------------- */
    /*  Construction                                                         */
    /* --------------------------------------------------------------------- */
    public BlockEntityParticleEmitter(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.PARTICLE.get(), pos, state);
    }

    /* --------------------------------------------------------------------- */
    /*  Logic                                                                */
    /* --------------------------------------------------------------------- */
    @Override
    public void updateEntity(Level world, BlockPos pos) {
        /* 26.1-lifecycle */ super.updateEntity(); // 26.1: drive BlockEntityBase lifecycle (ticksExisted++, onFirstTick → recompute/sync). Without this, BE never ages and onFirstTick never fires.
        updateCoil();
        if (!canEmit(world) || world.isClientSide()) return;
        for (int i = 0; i < particlesPerTick; i++) {
            particleType.spawnAt(world,
                    pos.getX() + DragonAPI.rand.nextDouble(),
                    pos.getY() + 2 + DragonAPI.rand.nextDouble() * 4,
                    pos.getZ() + DragonAPI.rand.nextDouble(),
                    pX, pY, pZ);
        }
    }

    private boolean canEmit(Level world) {
        if (!hasCoil()) return false;
        return !useRedstone || this.hasRedstoneSignal();
    }

    private void updateCoil() {
        if (!hasCoil()) return;
        tickcount++;
        if (tickcount >= getUnwindTime()) {
            inv.setStackInSlot(0, getDecrementedCharged());
            tickcount = 0;
        }
    }

    /* --------------------------------------------------------------------- */
    /*  Inventory plumbing                                                   */
    /* --------------------------------------------------------------------- */
    @Override public int  getContainerSize()        { return 1; }
    @Override public boolean isEmpty()              { return inv.getStackInSlot(0).isEmpty(); }
    @Override public ItemStack getItem(int i)       { return i==0? inv.getStackInSlot(0): ItemStack.EMPTY; }
    @Override public ItemStack removeItem(int i,int cnt){ return i==0? inv.extractItem(0,cnt,false): ItemStack.EMPTY; }
    @Override public ItemStack removeItemNoUpdate(int i){ if(i!=0) return ItemStack.EMPTY; ItemStack s=inv.getStackInSlot(0); inv.setStackInSlot(0, ItemStack.EMPTY); return s; }
    @Override public void setItem(int i, ItemStack s){ if(i==0) inv.setStackInSlot(0,s); }
    @Override public void clearContent()            { inv.setStackInSlot(0, ItemStack.EMPTY); }
    @Override public boolean stillValid(Player p)   { return !isRemoved() && p.distanceToSqr(worldPosition.getCenter())<=64; }

    /* Capability exposure */

    /* --------------------------------------------------------------------- */
    /*  Misc block-entity overrides                                          */
    /* --------------------------------------------------------------------- */
    @Override public MachineRegistry getMachine()   { return MachineRegistry.PARTICLE; }
    @Override public Block getBlockEntityBlockID()  { return RotaryBlocks.PARTICLE.get(); }
    @Override public int  getBaseDischargeTime()    { return 600; }
    @Override public int  getRedstoneOverride()     { return 0; }
    @Override protected void animateWithTick(Level l, BlockPos p) { }
    @Override public boolean hasModelTransparency() { return false; }
    @Override protected String getTEName()          { return "ParticleEmitter"; }

    /* --------------------------------------------------------------------- */
    /*  Sync                                                                 */
    /* --------------------------------------------------------------------- */
    @Override protected void writeSyncTag(CompoundTag tag){
        super.writeSyncTag(tag);
        tag.putInt("type", particleType.ordinal());
        tag.putInt("ppt",  particlesPerTick);
        tag.putDouble("vx", pX);
        tag.putDouble("vy", pY);
        tag.putDouble("vz", pZ);
        tag.putBoolean("rs", useRedstone);
    }
    @Override protected void readSyncTag(CompoundTag tag){
        super.readSyncTag(tag);
        particleType      = ReikaParticleHelper.values()[tag.getIntOr("type", 0)];
        particlesPerTick  = tag.getIntOr("ppt", 0);
        pX = tag.getDoubleOr("vx", 0);
        pY = tag.getDoubleOr("vy", 0);
        pZ = tag.getDoubleOr("vz", 0);
        useRedstone = tag.getBooleanOr("rs", false);
    }

    @Override
    public int getRange() {
        return 8;
    }

    /* --------------------------------------------------------------------- */
    /*  Interface fulfilment (RangedEffect)                                  */
    /* --------------------------------------------------------------------- */
    @Override public int getMaxRange() { return 8; } // cosmetic only

    @Override
    public boolean hasAnInventory() {
        return true;
    }

    @Override
    public boolean hasATank() {
        return false;
    }
}
