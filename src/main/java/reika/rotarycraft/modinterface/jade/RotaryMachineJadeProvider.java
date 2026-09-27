package reika.rotarycraft.modinterface.jade;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.api.interfaces.TemperatureTile;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.base.blockentity.BlockEntityPiping;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
import reika.rotarycraft.blockentities.production.BlockEntityPump;
import reika.rotarycraft.blockentities.storage.BlockEntityReservoir;
import reika.rotarycraft.blockentities.transmission.BlockEntityGearbox;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

/** Server-backed status for RotaryCraft machines not covered by the specialized Jade panels. */
public enum RotaryMachineJadeProvider implements IServerDataProvider<BlockAccessor>, IBlockComponentProvider {
    INSTANCE;

    private static final Identifier UID = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "machine_state");
    private static final String PREFIX = "rotarycraft_machine_";

    @Override
    public Identifier getUid() {
        return UID;
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        BlockEntity be = accessor.getBlockEntity();
        if (!(be instanceof RotaryCraftBlockEntity machine)
                || be instanceof BlockEntityPiping || be instanceof BlockEntityEngine
                || be instanceof BlockEntityReservoir || be instanceof BlockEntityGearbox)
            return;

        data.putBoolean(key("present"), true);
        if (machine.isShutdown())
            data.putBoolean(key("shutdown"), true);
        if (machine instanceof BlockEntityPowerReceiver receiver && receiver.MINPOWER > 0
                && receiver.power < receiver.MINPOWER) {
            data.putLong(key("power_required"), receiver.MINPOWER);
        }
        if (machine instanceof ConditionalOperation conditional) {
            data.putBoolean(key("operational"), conditional.areConditionsMet());
            String status = conditional.getOperationalStatus();
            if (status != null && !status.isBlank())
                data.putString(key("status"), status);
        }
        if (machine instanceof TemperatureTile thermal) {
            data.putInt(key("temperature"), thermal.getTemperature());
            data.putInt(key("max_temperature"), thermal.getMaxTemperature());
        }
        if (machine instanceof RangedEffect ranged) {
            data.putInt(key("range"), ranged.getRange());
            data.putInt(key("max_range"), ranged.getMaxRange());
        }

        ListTag fluids = new ListTag();
        var handler = accessor.getLevel().getCapability(Capabilities.Fluid.BLOCK, accessor.getPosition(), null);
        if (handler != null) {
            for (int i = 0; i < Math.min(handler.size(), 8); i++) {
                FluidResource resource = handler.getResource(i);
                if (!resource.isEmpty())
                    addFluid(fluids, resource.toStack(handler.getAmountAsInt(i)),
                            handler.getCapacityAsInt(i, resource));
            }
        } else if (machine instanceof BlockEntityPump pump && pump.getLiquid() != null
                && !pump.getLiquid().isEmpty()) {
            addFluid(fluids, pump.getLiquid(), BlockEntityPump.CAPACITY);
        }
        if (!fluids.isEmpty())
            data.put(key("fluids"), fluids);

        // Every machine has a comparator output, including ones without a power or tank state.
        // Keep zero-valued output out of busy tooltips unless it is the only available datum.
        int comparator = machine.getRedstoneOverride();
        if (comparator > 0 || (!data.contains(key("status"))
                && !data.contains(key("power_required")) && !data.contains(key("temperature"))
                && !data.contains(key("range")) && fluids.isEmpty()))
            data.putInt(key("comparator"), comparator);
    }

    private static void addFluid(ListTag fluids, FluidStack stack, int capacity) {
        CompoundTag fluid = new CompoundTag();
        fluid.putString("id", BuiltInRegistries.FLUID.getKey(stack.getFluid()).toString());
        fluid.putInt("amount", stack.getAmount());
        fluid.putInt("capacity", capacity);
        fluids.add(fluid);
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag data = accessor.getServerData();
        if (!data.getBooleanOr(key("present"), false))
            return;

        if (data.getBooleanOr(key("shutdown"), false))
            tooltip.add(Component.translatable("jade.rotarycraft.shutdown").withStyle(ChatFormatting.RED));
        if (data.contains(key("power_required")))
            tooltip.add(Component.translatable("jade.rotarycraft.power_required",
                    data.getLongOr(key("power_required"), 0)).withStyle(ChatFormatting.YELLOW));
        if (data.contains(key("status")))
            tooltip.add(Component.translatable("jade.rotarycraft.status",
                    data.getStringOr(key("status"), ""))
                    .withStyle(data.getBooleanOr(key("operational"), false)
                            ? ChatFormatting.GREEN : ChatFormatting.YELLOW));
        if (data.contains(key("temperature")))
            tooltip.add(Component.translatable("jade.rotarycraft.temperature",
                    data.getIntOr(key("temperature"), 0),
                    data.getIntOr(key("max_temperature"), 0)).withStyle(ChatFormatting.WHITE));
        if (data.contains(key("range")))
            tooltip.add(Component.translatable("jade.rotarycraft.range",
                    data.getIntOr(key("range"), 0), data.getIntOr(key("max_range"), 0))
                    .withStyle(ChatFormatting.WHITE));
        for (var entry : data.getListOrEmpty(key("fluids"))) {
            if (!(entry instanceof CompoundTag fluid))
                continue;
            Identifier id = Identifier.tryParse(fluid.getStringOr("id", ""));
            var type = id == null ? null : BuiltInRegistries.FLUID.getValue(id);
            if (type != null) {
                tooltip.add(Component.translatable("jade.rotarycraft.fluid",
                        Component.translatable(type.getFluidType().getDescriptionId()),
                        fluid.getIntOr("amount", 0), fluid.getIntOr("capacity", 0))
                        .withStyle(ChatFormatting.AQUA));
            }
        }
        if (data.contains(key("comparator")))
            tooltip.add(Component.translatable("jade.rotarycraft.comparator",
                    data.getIntOr(key("comparator"), 0)).withStyle(ChatFormatting.GRAY));
    }

    private static String key(String name) {
        return PREFIX + name;
    }
}
