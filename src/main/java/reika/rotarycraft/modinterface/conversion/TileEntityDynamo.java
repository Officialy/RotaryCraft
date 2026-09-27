package reika.rotarycraft.modinterface.conversion;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.modinteract.power.ReikaRFHelper;
import reika.rotarycraft.auxiliary.interfaces.RCToModConverter;
import reika.rotarycraft.auxiliary.interfaces.UpgradeableMachine;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.items.tools.ItemEngineUpgrade;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

/** Original rotational dynamo: receives shaft power opposite its facing and emits RF forward. */
public final class TileEntityDynamo extends BlockEntityPowerReceiver implements RCToModConverter, UpgradeableMachine {
    public static final int MAXTORQUE = 1024;
    public static final int MAXTORQUE_UPGRADE = 2048;
    public static final int MAXOMEGA = 8192;

    private boolean upgraded;

    public TileEntityDynamo(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.ROTATIONAL_DYNAMO.get(), pos, state);
    }

    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        Direction output = getBlockState().getValue(BlockRotaryCraftMachine.FACING);
        write = output;
        read = output.getOpposite();
        getPower(false);
        if (world.isClientSide()) return;
        if ((world.getGameTime() & 31) == 0) ReikaWorldHelper.causeAdjacentUpdates(world, pos);
        int generated = getGeneratedUnitsPerTick();
        if (generated <= 0) return;
        EnergyHandler receiver = world.getCapability(Capabilities.Energy.BLOCK,
                pos.relative(output), output.getOpposite());
        if (receiver == null) return;
        try (Transaction tx = Transaction.openRoot()) {
            if (receiver.insert(generated, tx) > 0) tx.commit();
        }
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
        if (!isInWorld()) { phi = 0; return; }
        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
    }

    @Override public MachineRegistry getMachine() { return MachineRegistry.DYNAMO; }
    @Override public Block getBlockEntityBlockID() { return RotaryBlocks.ROTATIONAL_DYNAMO.get(); }
    @Override public boolean hasModelTransparency() { return false; }
    @Override public int getRedstoneOverride() { return 0; }
    @Override protected String getTEName() { return "Rotational Dynamo"; }
    @Override public boolean hasAnInventory() { return false; }
    @Override public boolean hasATank() { return false; }

    @Override
    public int getGeneratedUnitsPerTick() {
        long mechanical = (long)Math.min(torque, upgraded ? MAXTORQUE_UPGRADE : MAXTORQUE)
                * Math.min(omega, MAXOMEGA);
        if (mechanical <= 0) return 0;
        return (int)Math.min(Integer.MAX_VALUE,
                mechanical * ConfigRegistry.getConverterEfficiency() / ReikaRFHelper.getWattsPerRF());
    }

    @Override public String getUnitDisplay() { return "RF"; }
    @Override public void upgrade(ItemStack stack) {
        if (!canUpgradeWith(stack)) return;
        upgraded = true;
        setChanged();
        syncAllData(true);
    }
    @Override public boolean canUpgradeWith(ItemStack stack) {
        return !upgraded && ItemEngineUpgrade.getUpgrade(stack) == ItemEngineUpgrade.UpgradeType.FLUX;
    }
    public boolean isUpgraded() { return upgraded; }

    @Override protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);
        tag.putBoolean("upgrade", upgraded);
    }
    @Override protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);
        upgraded = tag.getBooleanOr("upgrade", false);
    }
}
