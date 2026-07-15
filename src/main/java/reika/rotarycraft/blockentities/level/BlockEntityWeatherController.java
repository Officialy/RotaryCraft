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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Heightmap;

import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;

/**
 * The Weather Controller consumes weather-seeding reagents from its inventory to force the world's
 * weather: sawdust clears it, silver iodide brings rain, silver iodide + redstone escalates to a
 * thunderstorm, and silver iodide + glowstone dust conjures a superstorm that also calls down
 * lightning nearby. It must see the sky, draws shaft power from below, and has a random cooldown
 * between activations. 1.7.10-faithful; the public {@code WeatherControlEvent} broadcast for other
 * mods is not fired (API-PORT: the event class is not part of this build).
 */
public class BlockEntityWeatherController extends InventoriedPowerReceiver implements ConditionalOperation {

    /** Vanilla weather changes set a duration; the machine re-asserts often, so a long spell persists. */
    private static final int WEATHER_TIME = 12000;

    private int cooldown = 0;
    private RainMode rainmode = RainMode.NONE;

    public BlockEntityWeatherController(BlockPos pos, net.minecraft.world.level.block.state.BlockState state) {
        super(RotaryBlockEntities.WEATHER_CONTROLLER.get(), pos, state);
    }

    private enum RainMode {
        NONE,
        SUN,
        RAIN,
        THUNDER,
        SUPERSTORM;

        public boolean isRain() {
            return this.ordinal() > SUN.ordinal();
        }

        public boolean isThunder() {
            return this.ordinal() > RAIN.ordinal();
        }

        public boolean hasAction() {
            return this != NONE;
        }
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getPowerBelow();
        if (cooldown > 0)
            cooldown--;
        if (power < MINPOWER)
            return;
        if (!world.canSeeSky(pos.above()))
            return;

        // Superstorm is actively maintained every tick and rains down lightning.
        if (rainmode == RainMode.SUPERSTORM && world instanceof ServerLevel sl) {
            this.setWeather(sl, true, true);
            if (sl.getRandom().nextInt(20) == 0) {
                int xl = pos.getX() - 64 + sl.getRandom().nextInt(129);
                int zl = pos.getZ() - 64 + sl.getRandom().nextInt(129);
                int yl = sl.getHeight(Heightmap.Types.MOTION_BLOCKING, xl, zl);
                LightningBolt bolt = new LightningBolt(EntityTypes.LIGHTNING_BOLT, sl);
                bolt.setPos(xl + 0.5, yl, zl + 0.5);
                sl.addFreshEntity(bolt);
            }
        }

        if (cooldown > 0)
            return;

        rainmode = this.getRainMode(world, pos);
        if (this.isAlready(world, rainmode))
            return;
        if (!rainmode.hasAction())
            return;

        if (world instanceof ServerLevel sl) {
            boolean rain = rainmode.isRain();
            boolean thunder = rainmode.isThunder();
            if (rainmode == RainMode.SUN)
                this.setWeather(sl, false, false);
            else
                this.setWeather(sl, rain, thunder);
        }
        // API-PORT: legacy fired MinecraftForge.EVENT_BUS.post(new WeatherControlEvent(...)) here
        // so other mods could react; that event class is not in this build.
    }

    private void setWeather(ServerLevel sl, boolean rain, boolean thunder) {
        // 26.x moved weather off ServerLevel.setWeatherParameters into the WeatherData saved-data object.
        var wd = sl.getWeatherData();
        if (rain) {
            wd.setClearWeatherTime(0);
            wd.setRaining(true);
            wd.setRainTime(WEATHER_TIME);
            wd.setThundering(thunder);
            wd.setThunderTime(thunder ? WEATHER_TIME : 0);
        } else {
            wd.setRaining(false);
            wd.setRainTime(0);
            wd.setThundering(false);
            wd.setThunderTime(0);
            wd.setClearWeatherTime(WEATHER_TIME);
        }
        wd.setDirty();
    }

    private boolean isAlready(Level world, RainMode m) {
        return m.isRain() == world.isRaining() && m.isThunder() == world.isThundering();
    }

    private RainMode getRainMode(Level world, BlockPos pos) {
        int sawdust = ReikaInventoryHelper.locateInInventory(new ItemStack(RotaryItems.SAWDUST.get()), itemHandler, false);
        int silverio = ReikaInventoryHelper.locateInInventory(new ItemStack(RotaryItems.SILVERIODIDE.get()), itemHandler, false);
        int redstone = ReikaInventoryHelper.locateInInventory(Items.REDSTONE, itemHandler);
        int glowdust = ReikaInventoryHelper.locateInInventory(Items.GLOWSTONE_DUST, itemHandler);

        RainMode mode;
        ItemStack fired = ItemStack.EMPTY;
        ItemStack fired2 = ItemStack.EMPTY;
        if (sawdust >= 0) {
            mode = RainMode.SUN;
            fired = new ItemStack(RotaryItems.SAWDUST.get());
        } else if (silverio >= 0) {
            mode = RainMode.RAIN;
            fired = new ItemStack(RotaryItems.SILVERIODIDE.get());
            if (redstone >= 0) {
                mode = RainMode.THUNDER;
                fired2 = new ItemStack(Items.REDSTONE);
            } else if (glowdust >= 0) {
                mode = RainMode.SUPERSTORM;
                fired2 = new ItemStack(Items.GLOWSTONE_DUST);
            }
        } else {
            return RainMode.NONE;
        }

        if (this.isAlready(world, mode))
            return mode;

        cooldown = 200 + world.getRandom().nextInt(400);
        this.fire(world, pos, fired, fired2);

        // Consume the reagents that produced this mode.
        switch (mode) {
            case SUN -> ReikaInventoryHelper.decrStack(sawdust, itemHandler);
            case RAIN -> ReikaInventoryHelper.decrStack(silverio, itemHandler);
            case THUNDER -> {
                ReikaInventoryHelper.decrStack(silverio, itemHandler);
                ReikaInventoryHelper.decrStack(redstone, itemHandler);
            }
            case SUPERSTORM -> {
                ReikaInventoryHelper.decrStack(silverio, itemHandler);
                ReikaInventoryHelper.decrStack(glowdust, itemHandler);
            }
            default -> {
            }
        }
        return mode;
    }

    /** Spits the consumed reagent(s) skyward as a visual "seeding" effect. */
    private void fire(Level world, BlockPos pos, ItemStack is, ItemStack is2) {
        if (world.isClientSide())
            return;
        world.playSound(null, pos, net.minecraft.sounds.SoundEvents.GENERIC_EXPLODE.value(),
                net.minecraft.sounds.SoundSource.BLOCKS, 1F, 1F);
        this.spitUp(world, pos, is);
        this.spitUp(world, pos, is2);
    }

    private void spitUp(Level world, BlockPos pos, ItemStack is) {
        if (is == null || is.isEmpty())
            return;
        ItemEntity ei = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.0625, pos.getZ() + 0.5,
                new ItemStack(is.getItem(), 1));
        ei.setDeltaMovement((world.getRandom().nextDouble() - 0.5) * 0.2, 3, (world.getRandom().nextDouble() - 0.5) * 0.2);
        ei.setPickUpDelay(32767); //effectively un-pickup-able; despawns on its own
        world.addFreshEntity(ei);
    }

    private boolean isValidWeatherItem(ItemStack is) {
        if (is.isEmpty())
            return false;
        Item item = is.getItem();
        return item == RotaryItems.SAWDUST.get() || item == RotaryItems.SILVERIODIDE.get()
                || item == Items.REDSTONE || item == Items.GLOWSTONE_DUST;
    }

    @Override
    public int getContainerSize() {
        return 18;
    }

    public boolean isItemValidForSlot(int slot, ItemStack is) {
        return this.isValidWeatherItem(is);
    }

    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    @Override
    public boolean hasAnInventory() {
        return true;
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.WEATHERCONTROLLER;
    }

    @Override
    protected String getTEName() {
        return "weathercontroller";
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.WEATHER_CONTROLLER.get();
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
    public boolean areConditionsMet() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            if (this.isValidWeatherItem(itemHandler.getStackInSlot(i)))
                return true;
        }
        return false;
    }

    @Override
    public String getOperationalStatus() {
        return this.areConditionsMet() ? "Operational" : "No Reagents";
    }
}
