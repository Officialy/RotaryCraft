package reika.rotarycraft.test;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import org.junit.jupiter.api.Test;
import reika.dragonapi.auxiliary.jade.ThermalTileJadePlugin;
import reika.rotarycraft.blockentities.engine.BlockEntityDCEngine;
import reika.rotarycraft.blockentities.engine.BlockEntitySteamEngine;
import reika.rotarycraft.modinterface.jade.RotaryJadePlugin;
import reika.rotarycraft.registry.RotaryBlocks;
import snownee.jade.api.*;

import static org.junit.jupiter.api.Assertions.*;

class JadeEngineTooltipTest {
    @Test
    void dcHasPowerWithoutTemperatureOrFuelRows() {
        var engine = new BlockEntityDCEngine(BlockPos.ZERO, RotaryBlocks.DC_ENGINE.get().defaultBlockState());
        engine.setTemperature(29);
        engine.torque = 4;
        engine.omega = 256;
        var rows = tooltip(engine);
        assertTrue(rows.stream().anyMatch(s -> s.startsWith("Power:")), rows.toString());
        assertTrue(rows.stream().noneMatch(s -> s.startsWith("Temp") || s.startsWith("Fuel:")), rows.toString());
    }

    @Test
    void thermalEngineHasOneServerTemperatureRowAndFuel() {
        var engine = new BlockEntitySteamEngine(BlockPos.ZERO, RotaryBlocks.STEAM_ENGINE.get().defaultBlockState());
        engine.setTemperature(123);
        var rows = tooltip(engine);
        assertEquals(List.of("Temperature: 123°C"), rows.stream().filter(s -> s.startsWith("Temp")).toList());
        assertTrue(rows.stream().anyMatch(s -> s.startsWith("Fuel:")), rows.toString());
    }

    @SuppressWarnings("unchecked")
    private static List<String> tooltip(net.minecraft.world.level.block.entity.BlockEntity engine) {
        var dataProviders = new ArrayList<IServerDataProvider<BlockAccessor>>();
        var components = new ArrayList<IBlockComponentProvider>();
        var common = proxy(IWailaCommonRegistration.class, (name, args) -> {
            if (name.equals("registerBlockDataProvider")) dataProviders.add((IServerDataProvider<BlockAccessor>) args[0]);
            return null;
        });
        var client = proxy(IWailaClientRegistration.class, (name, args) -> {
            if (name.equals("registerBlockComponent")) components.add((IBlockComponentProvider) args[0]);
            return null;
        });
        var thermal = new ThermalTileJadePlugin();
        thermal.register(common);
        thermal.registerClient(client);
        // Only engine_extras is relevant here; the independent machine-state provider needs
        // a level capability lookup and already excludes engines.
        new RotaryJadePlugin().registerClient(client);
        CompoundTag data = new CompoundTag();
        var accessor = proxy(BlockAccessor.class, (name, args) -> switch (name) {
            case "getBlockEntity" -> engine;
            case "getServerData" -> data;
            default -> null;
        });
        dataProviders.forEach(provider -> provider.appendServerData(data, accessor));
        var rows = new ArrayList<String>();
        var tooltip = proxy(ITooltip.class, (name, args) -> {
            if (name.equals("add") && args != null && args.length > 0 && args[0] instanceof Component text)
                rows.add(text.getString());
            return null;
        });
        components.stream().filter(provider -> provider.getUid().equals(ThermalTileJadePlugin.UID)
                        || provider.getUid().getPath().equals("engine_extras"))
                .forEach(provider -> provider.appendTooltip(tooltip, accessor, null));
        return rows;
    }

    private interface Invocation { Object call(String name, Object[] args); }

    private static <T> T proxy(Class<T> type, Invocation invocation) {
        return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type},
                (object, method, args) -> invocation.call(method.getName(), args)));
    }
}
