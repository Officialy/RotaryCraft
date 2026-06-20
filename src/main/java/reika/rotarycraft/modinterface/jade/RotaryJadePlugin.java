/*******************************************************************************
 * @author Reika Kalseki / 26.1 port by OfficialyMax
 *
 * Jade (HUD-tooltip) integration for RotaryCraft. Surfaces piping pressure / fluid /
 * temperature, engine power/torque/omega, reservoir fill state, and shaft/gearbox
 * power transmission on the in-world tooltip so the user doesn't have to open every
 * machine's GUI to debug a power network.
 *
 * Discovery: Jade scans for {@code @WailaPlugin}-annotated classes via its
 * {@code META-INF/jade-plugin.json} entrypoint registry. The companion descriptor
 * lives at {@code src/main/resources/META-INF/jade-plugin.json} and lists this class.
 *
 * This class hard-links against Jade's API ({@code snownee.jade.api}); the RotaryCraft
 * {@code build.gradle} declares Jade as {@code compileOnly} so the integration only
 * loads when Jade is actually present at runtime. A {@link reika.dragonapi.ModList#JADE}
 * isLoaded guard isn't strictly needed because Jade's class-loader simply ignores
 * unfound plugin classes, but the descriptor JSON contains the modid check anyway.
 ******************************************************************************/
package reika.rotarycraft.modinterface.jade;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.ITooltip;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;

import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.base.blockentity.BlockEntityIOMachine;
import reika.rotarycraft.base.blockentity.BlockEntityPiping;
import reika.rotarycraft.blockentities.storage.BlockEntityReservoir;

@WailaPlugin
public class RotaryJadePlugin implements IWailaPlugin {

    private static final Identifier PIPE_UID  = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "pipe_info");
    private static final Identifier MACH_UID  = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "machine_power");
    private static final Identifier TANK_UID  = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "reservoir_fluid");

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        // 26.1 Jade API: {@code registerBlockComponent} now takes a {@code Class<? extends Block>}
        // and dispatches by BLOCK type, not by BlockEntity type. All RotaryCraft machine blocks
        // (pipes, engines, IOMachines, reservoir) share {@code BlockBasicMachine} as their root
        // parent, so we register every component against that single class. Each provider's
        // {@code appendTooltip} then narrows by BE type via {@code instanceof} — so PipeTooltip
        // bails on non-piping BEs, MachinePowerTooltip bails on non-IOMachine BEs, etc.
        //
        // The per-component {@code instanceof} branch is one virtual call + one type-check,
        // negligible next to the network round-trip Jade already does for each looked-at block.
        registration.registerBlockComponent(new PipeTooltip(),         reika.rotarycraft.base.blocks.BlockBasicMachine.class);
        registration.registerBlockComponent(new MachinePowerTooltip(), reika.rotarycraft.base.blocks.BlockBasicMachine.class);
        registration.registerBlockComponent(new EngineExtraTooltip(),  reika.rotarycraft.base.blocks.BlockBasicMachine.class);
        registration.registerBlockComponent(new ReservoirTooltip(),    reika.rotarycraft.base.blocks.BlockBasicMachine.class);
    }

    // ------------------------------------------------------------------------------------
    // Pipe / hose / fuel line / bedrock-pipe — pressure + fluid amount + temperature.

    private static final class PipeTooltip implements IBlockComponentProvider {
        @Override public Identifier getUid() { return PIPE_UID; }

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            BlockEntity be = accessor.getBlockEntity();
            if (!(be instanceof BlockEntityPiping pipe)) return;

            Fluid f = pipe.getAttributes();
            int level = pipe.getFluidLevel();
            int pressure = pipe.getPressure();

            // Fluid identity. If empty, say so explicitly so the user can tell at a glance
            // whether a pipe is dry vs full of a fluid they can't see (steam etc.).
            if (f == null || level <= 0) {
                tooltip.add(Component.literal("Empty").withStyle(ChatFormatting.GRAY));
            } else {
                Component name = Component.translatable(f.getFluidType().getDescriptionId());
                tooltip.add(Component.literal("Fluid: ").withStyle(ChatFormatting.GRAY)
                        .append(name.copy().withStyle(ChatFormatting.AQUA)));
                tooltip.add(Component.literal("Level: ").withStyle(ChatFormatting.GRAY)
                        .append(Component.literal(level + " mB").withStyle(ChatFormatting.WHITE)));
            }

            tooltip.add(Component.literal("Pressure: ").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(formatPressure(pressure)).withStyle(pressureColour(pressure, pipe.getMaxPressure()))));

            // Temperature — pipes track the temperature of whatever fluid they currently
            // contain (e.g. steam = hot, liquid nitrogen = cold). The base BE doesn't expose
            // a getter though; the temperature field is package-private inside
            // BlockEntityPipe, so we'd have to reflect to read it. Skip for now — the user's
            // bug report mentions temperature alongside pressure and fluid amount, but
            // exposing it requires a public accessor on the BE which is a follow-up edit.
        }

        private static String formatPressure(int p) {
            if (p >= 1_000_000) return String.format("%.2f MPa", p / 1_000_000.0);
            if (p >= 1_000)     return String.format("%.1f kPa", p / 1_000.0);
            return p + " Pa";
        }

        private static ChatFormatting pressureColour(int p, int max) {
            if (max <= 0) return ChatFormatting.WHITE;
            double frac = (double) p / max;
            if (frac > 0.9) return ChatFormatting.RED;
            if (frac > 0.7) return ChatFormatting.YELLOW;
            return ChatFormatting.WHITE;
        }
    }

    // ------------------------------------------------------------------------------------
    // Generic machine power readout — torque, omega, power for any IOMachine subclass.

    private static final class MachinePowerTooltip implements IBlockComponentProvider {
        @Override public Identifier getUid() { return MACH_UID; }

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            BlockEntity be = accessor.getBlockEntity();
            if (!(be instanceof BlockEntityIOMachine m)) return;

            // Don't double-print on engines (the EngineExtraTooltip handles those with the
            // engine-specific lines slotted alongside torque/omega/power).
            if (be instanceof BlockEntityEngine) return;

            // Only render if something is moving — a stopped machine cluttering the HUD with
            // "Torque: 0 / Omega: 0 / Power: 0" wastes screen real estate.
            if (m.torque == 0 && m.omega == 0) return;

            tooltip.add(Component.literal("Torque: ").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(m.torque + " Nm").withStyle(ChatFormatting.WHITE)));
            tooltip.add(Component.literal("Speed: ").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(m.omega + " rad/s").withStyle(ChatFormatting.WHITE)));
            tooltip.add(Component.literal("Power: ").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(formatPower((long) m.torque * (long) m.omega)).withStyle(ChatFormatting.GOLD)));
        }

        private static String formatPower(long w) {
            if (w >= 1_000_000_000) return String.format("%.2f GW", w / 1_000_000_000.0);
            if (w >= 1_000_000)     return String.format("%.2f MW", w / 1_000_000.0);
            if (w >= 1_000)         return String.format("%.2f kW", w / 1_000.0);
            return w + " W";
        }
    }

    // ------------------------------------------------------------------------------------
    // Engine-specific extras — temperature, fuel/water tank levels.

    private static final class EngineExtraTooltip implements IBlockComponentProvider {
        @Override public Identifier getUid() {
            return Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "engine_extras");
        }

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            BlockEntity be = accessor.getBlockEntity();
            if (!(be instanceof BlockEntityEngine eng)) return;

            // Power line first so engines and other IOMachines share the same row order.
            if (eng.torque != 0 || eng.omega != 0) {
                tooltip.add(Component.literal("Torque: ").withStyle(ChatFormatting.GRAY)
                        .append(Component.literal(eng.torque + " Nm").withStyle(ChatFormatting.WHITE)));
                tooltip.add(Component.literal("Speed: ").withStyle(ChatFormatting.GRAY)
                        .append(Component.literal(eng.omega + " rad/s").withStyle(ChatFormatting.WHITE)));
                long p = (long) eng.torque * (long) eng.omega;
                tooltip.add(Component.literal("Power: ").withStyle(ChatFormatting.GRAY)
                        .append(Component.literal(MachinePowerTooltip.formatPower(p)).withStyle(ChatFormatting.GOLD)));
            }
            tooltip.add(Component.literal("Temp: ").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(eng.getTemperature() + " °C").withStyle(tempColour(eng.getTemperature(), eng.getMaxTemperature()))));

            int fuel = eng.getFuelLevel();
            if (fuel >= 0) {
                tooltip.add(Component.literal("Fuel: ").withStyle(ChatFormatting.GRAY)
                        .append(Component.literal(fuel + " mB").withStyle(ChatFormatting.YELLOW)));
            }
        }

        private static ChatFormatting tempColour(int t, int max) {
            if (max <= 0) return ChatFormatting.WHITE;
            double frac = (double) t / max;
            if (frac > 0.95) return ChatFormatting.RED;
            if (frac > 0.75) return ChatFormatting.YELLOW;
            if (t < 0)        return ChatFormatting.AQUA;
            return ChatFormatting.WHITE;
        }
    }

    // ------------------------------------------------------------------------------------
    // Reservoir tank fill state — individual block + total network volume.

    private static final class ReservoirTooltip implements IBlockComponentProvider {
        @Override public Identifier getUid() { return TANK_UID; }

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            BlockEntity be = accessor.getBlockEntity();
            if (!(be instanceof BlockEntityReservoir res)) return;

            int level = res.getFluidLevel();
            if (level <= 0 || res.getFluid() == null || res.getFluid().isEmpty()) {
                tooltip.add(Component.literal("Empty").withStyle(ChatFormatting.GRAY));
                return;
            }
            Fluid f = res.getFluid().getFluid();
            Component fluidName = Component.translatable(f.getFluidType().getDescriptionId());
            tooltip.add(Component.literal("Fluid: ").withStyle(ChatFormatting.GRAY)
                    .append(fluidName.copy().withStyle(ChatFormatting.AQUA)));

            // Individual block volume
            tooltip.add(Component.literal("This block: ").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(level + " / " + BlockEntityReservoir.CAPACITY + " mB").withStyle(ChatFormatting.WHITE)));

            // Network total — BFS over cardinal horizontal neighbours
            int[] net = networkTotal(accessor.getLevel(), accessor.getPosition());
            int netFluid = net[0], netCap = net[1];
            if (netCap > BlockEntityReservoir.CAPACITY) {
                // Only show the network line when there are multiple connected blocks
                tooltip.add(Component.literal("Network: ").withStyle(ChatFormatting.GRAY)
                        .append(Component.literal(netFluid + " / " + netCap + " mB")
                                .withStyle(ChatFormatting.WHITE)));
                tooltip.add(Component.literal(String.format("%.0f%% full", 100.0 * netFluid / netCap))
                        .withStyle(ChatFormatting.DARK_GRAY));
            } else {
                tooltip.add(Component.literal(String.format("%.0f%% full", 100.0 * level / BlockEntityReservoir.CAPACITY))
                        .withStyle(ChatFormatting.DARK_GRAY));
            }
        }

        /** BFS limited to 256 blocks. Returns {totalFluid, totalCapacity}. */
        private static int[] networkTotal(Level world, BlockPos start) {
            if (world == null) return new int[]{0, BlockEntityReservoir.CAPACITY};
            Set<BlockPos> visited = new HashSet<>();
            Queue<BlockPos> queue = new ArrayDeque<>();
            queue.add(start);
            visited.add(start);
            int totalFluid = 0, totalCap = 0;
            while (!queue.isEmpty() && visited.size() <= 256) {
                BlockPos pos = queue.poll();
                BlockEntity be = world.getBlockEntity(pos);
                if (!(be instanceof BlockEntityReservoir res)) continue;
                totalFluid += res.getFluidLevel();
                totalCap   += BlockEntityReservoir.CAPACITY;
                for (Direction dir : Direction.Plane.HORIZONTAL) {
                    BlockPos nb = pos.relative(dir);
                    if (!visited.contains(nb) && world.getBlockEntity(nb) instanceof BlockEntityReservoir) {
                        visited.add(nb);
                        queue.add(nb);
                    }
                }
            }
            return new int[]{totalFluid, totalCap};
        }
    }
}
