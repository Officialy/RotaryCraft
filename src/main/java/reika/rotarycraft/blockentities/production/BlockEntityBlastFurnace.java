package reika.rotarycraft.blockentities.production;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import reika.dragonapi.instantiable.StepTimer;
import reika.dragonapi.interfaces.IBonusYield;
import reika.dragonapi.interfaces.IHasXP;
import reika.dragonapi.interfaces.IHeatRecipe;
import reika.dragonapi.interfaces.blockentity.XPProducer;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
import reika.rotarycraft.auxiliary.interfaces.FrictionHeatable;
import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
import reika.rotarycraft.base.blockentity.InventoriedRCBlockEntity;
import reika.rotarycraft.gui.container.machine.inventory.ContainerBlastFurnace;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryRecipeTypes;

import java.util.Optional;

/**
 * Full 1.20.1 port of Reika's Blast Furnace tile. This attempts to keep the exact
 * slot layout & behaviour from 1.7.10
 * <p>
 *  Slot map (16 internal + 3 output = 19 logical):
 *  <pre>
 *   0   – centre additive
 *   1-9 – 3×3 crafting grid (left→right, top→bottom)
 *   10  – centre output (DOWN capability)
 *   11  – lower additive
 *   12  – upper output (DOWN capability)
 *   13  – lower output (DOWN capability)
 *   14  – upper additive
 *   15  – pattern slot
 *  </pre>
 */
public class BlockEntityBlastFurnace extends InventoriedRCBlockEntity
        implements TemperatureTE, XPProducer, FrictionHeatable, DiscreteFunction, ConditionalOperation {

    /* --------------------------------------------------------------------- */
    /*  Constants                                                            */
    /* --------------------------------------------------------------------- */
    public static final int SMELT_TEMP  = 232; //Lowest temp you can make Galinstan
    public static final int BEDROCK_TEMP = 1450;
    public static final int MAX_TEMP     = 2000;

    /** slot indices */
    public static final int CENTER_ADDITIVE = 0;
    public static final int LOWER_ADDITIVE  = 11;
    public static final int UPPER_ADDITIVE  = 14;
    public static final int PATTERN_SLOT    = 15;

    public static final int OUTPUT_CENTER = 10;
    public static final int OUTPUT_UPPER  = 12;
    public static final int OUTPUT_LOWER  = 13;

    private static final int INTERNAL_SLOT_COUNT = 16; // indices 0-15 (see above)
    private static final int OUTPUT_SLOT_COUNT   = 3;  // 0-2 via DOWN capability

    /* --------------------------------------------------------------------- */
    /*  Inventories & capabilities                                           */
    /* --------------------------------------------------------------------- */

    /** Separate handler for outputs; exposed only on DOWN side. */
    private final ItemStackHandler outputInv = new ItemStackHandler(OUTPUT_SLOT_COUNT) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private final LazyOptional<IItemHandler> outputCap   = LazyOptional.of(() -> outputInv);

    /* --------------------------------------------------------------------- */
    /*  Persistent / synced state                                            */
    /* --------------------------------------------------------------------- */
    private final StepTimer ambientTimer = new StepTimer(20);

    private int  temperature;
    private int  progress;
    private float storedXP;
    public  boolean leaveLastItem;

    /* --------------------------------------------------------------------- */
    /*  Datacontrol                                                           */
    /* --------------------------------------------------------------------- */
    private final ContainerData containerData = new ContainerData() {
        @Override public int get(int index) {
            return switch (index) {
                case 0 -> progress;
                case 1 -> temperature;
                default -> 0;
            };
        }
        @Override public void set(int index, int value) {
            switch (index) {
                case 0 -> progress    = value;
                case 1 -> temperature = value;
            }
        }
        @Override public int getCount() { return 2; }
    };

    /* --------------------------------------------------------------------- */
    /*  Construction                                                         */
    /* --------------------------------------------------------------------- */
    public BlockEntityBlastFurnace(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.BLAST_FURNACE.get(), pos, state);
    }

    /* --------------------------------------------------------------------- */
    /*  Forge capability plumbing                                            */
    /* --------------------------------------------------------------------- */
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == Direction.DOWN)
                return outputCap.cast();
        }
        return super.getCapability(cap, side);
    }

    /* --------------------------------------------------------------------- */
    /*  Block-entity serialisation                                           */
    /* --------------------------------------------------------------------- */
    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("internal", itemHandler.serializeNBT());
        tag.put("output",   outputInv.serializeNBT());
        tag.putInt("temp",      temperature);
        tag.putInt("progress",  progress);
        tag.putFloat("xp",      storedXP);
        tag.putBoolean("leaveLast", leaveLastItem);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("internal"));
        outputInv.deserializeNBT(tag.getCompound("output"));
        temperature    = tag.getInt("temp");
        progress       = tag.getInt("progress");
        storedXP       = tag.getFloat("xp");
        leaveLastItem  = tag.getBoolean("leaveLast");
    }

    /* --------------------------------------------------------------------- */
    /*  Menu                                                                  */
    /* --------------------------------------------------------------------- */
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new ContainerBlastFurnace(id, inv, this);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Blast Furnace");
    }

    /* --------------------------------------------------------------------- */
    /*  Networking                                                            */
    /* --------------------------------------------------------------------- */
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    /* --------------------------------------------------------------------- */
    /*  Main tick                                                             */
    /* --------------------------------------------------------------------- */

    @Override public void updateEntity(Level world, BlockPos pos) {
        if (level == null || level.isClientSide) return;

        ambientTimer.update();
        if (ambientTimer.checkCap()) {
            updateAmbientTemperature(level, worldPosition);
        }

        boolean recipeValid = processRecipe();
        if (!recipeValid) {
            progress = 0;
        }
    }

    /**
     * Returns <code>true</code> every tick that a valid recipe is present
     * (even while it is still progressing).  Performs the craft when
     * <code>progress == getOperationTime()</code>.
     */
    private boolean processRecipe() {
        /* ------------------------------------------------------------
         * Build a view of the *internal* inventory for recipe lookup
         * ---------------------------------------------------------- */
        SimpleContainer invView = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++)
            invView.setItem(i, itemHandler.getStackInSlot(i));

        /* ------------------------------------------------------------
         * Recipe lookup – shaped first, then shapeless
         * ---------------------------------------------------------- */
        Recipe<SimpleContainer> matched;
        Optional<? extends Recipe<SimpleContainer>> shaped =
                level.getRecipeManager().getRecipeFor(
                        RotaryRecipeTypes.BLAST_FURNACE_SHAPED.get(), invView, level);

        if (shaped.isPresent()) {
            matched = shaped.get();
        } else {
            Optional<? extends Recipe<SimpleContainer>> shapeless =
                    level.getRecipeManager().getRecipeFor(
                            RotaryRecipeTypes.BLAST_FURNACE_SHAPELESS.get(), invView, level);
            if (shapeless.isEmpty())
                return false;
            matched = shapeless.get();
        }

        float requiredTemp = matched instanceof IHeatRecipe ht ? ht.requiredTemperature() : SMELT_TEMP;

        if (temperature < requiredTemp)
            return false;

        // Count actual inputs in the crafting grid (slots 1-9)
        int inputCount = 0;
        for (int i = 1; i <= 9; i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                inputCount += stack.getCount();
            }
        }

        // Get base result and scale by input count
        ItemStack result = matched.getResultItem(RegistryAccess.EMPTY).copy();
        result.setCount(inputCount); // Scale base output to match input amount

        // Apply bonus yield if recipe supports it
        if (matched instanceof IBonusYield bonus) {
            if (rand.nextInt(100) < bonus.bonusChance()) {
                int bonusPerItem = bonus.bonusMin() +
                        rand.nextInt(bonus.bonusMax() - bonus.bonusMin() + 1);
                int totalBonus = bonusPerItem * inputCount; // Scale bonus by input count too
                result.grow(totalBonus);
            }
        }

        /* ------------------------------------------------------------
         * Output‐space check – if blocked, keep progress but do not
         * advance; this mirrors the 1.7.10 logic where the timer stops
         * when outputs are full.
         * ---------------------------------------------------------- */
        if (!canOutput(result))
            return true;

        /* ------------------------------------------------------------
         * Progress bar
         * ---------------------------------------------------------- */
        progress++;
        if (progress < getOperationTime())
            return true;            // still smelting

        /* ------------  craft  ------------------------------------ */
        progress = 0;

        // ---- consume the 9-grid inputs (slots 1‥9)
        for (int i = 1; i <= 9; i++)
            itemHandler.extractItem(i, 1, false);

        // ---- consume the centre additive (slot 0) – at least one,
        //      but possibly more depending on recipe size
        itemHandler.extractItem(0, matched.getIngredients().size(), false);

        // ---- random chance to consume the *other* additives.
        //      Using the original RotaryCraft probabilities:
        //      slot 11: 40 % , slot 14: 25 %
        if (rand.nextFloat() < 0.40F)
            itemHandler.extractItem(11, 1, false);
        if (rand.nextFloat() < 0.25F)
            itemHandler.extractItem(14, 1, false);

        /* ------------------------------------------------------------
         * Push the results – tries 10, then 12, then 13, merging where
         * possible and falling through to the next slot only if needed.
         * ---------------------------------------------------------- */
        RotaryCraft.LOGGER.debug("Blast Furnace {}: pushing result {}", worldPosition, result);
        pushToOutputs(result);
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);

        float xpPerItem = 0.0F;
        if (matched instanceof IHasXP x)           // works for both shapes
            xpPerItem = x.xpPerItem();

        storedXP += xpPerItem * result.getCount();


        setChanged();
        return true;
    }

    /* --------------------------------------------------------------------- */
    /*  Helper – output handling                                             */
    /* --------------------------------------------------------------------- */
    private boolean canOutput(ItemStack stack) {
        for (int i = 0; i < outputInv.getSlots(); i++) {
            ItemStack existing = outputInv.getStackInSlot(i);
            if (existing.isEmpty() || ReikaItemHelper.areStacksCombinable(existing, stack, existing.getMaxStackSize()))
                return true;
        }
        return false;
    }

    private void pushToOutputs(ItemStack stack) {
        for (int i = 0; i < outputInv.getSlots(); i++) {
            stack = outputInv.insertItem(i, stack, false);
            if (stack.isEmpty())
                break;
        }
    }

    /* --------------------------------------------------------------------- */
    /*  Temperature logic                                                    */
    /* --------------------------------------------------------------------- */
    private void updateAmbientTemperature(Level world, BlockPos pos) {
        int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);

        if (RotaryAux.isNextToWater(world, pos)) {
            Tamb /= 2;
        }
        Direction iceside = ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.ICE);
        if (iceside == null)
            iceside = ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.PACKED_ICE);
        if (iceside != null) {
            if (Tamb > 0)
                Tamb /= 4;
            ReikaWorldHelper.changeAdjBlock(world, pos, iceside,  Fluids.FLOWING_WATER.defaultFluidState().createLegacyBlock());
        }
        int Tadd = 0;
        if (RotaryAux.isNextToFire(world, pos)) {
            Tadd += Tamb >= 100 ? 100 : 200;
        }
        if (RotaryAux.isNextToLava(world, pos)) {
            Tadd += Tamb >= 100 ? 400 : 600;
        }
        Tamb += Tadd;

        if (temperature > Tamb)
            temperature--;
        if (temperature > Tamb*2)
            temperature--;
        if (temperature < Tamb)
            temperature++;
        if (temperature*2 < Tamb)
            temperature++;
        if (temperature > MAX_TEMP)
            temperature = MAX_TEMP;
        if (temperature > 100) {
            Direction side = ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.SNOW);
            if (side == null)
                side = ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.SNOW_BLOCK);
            if (side != null)
                ReikaWorldHelper.changeAdjBlock(world, pos, side, Blocks.AIR.defaultBlockState());
            side = ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.ICE);
            if (side != null)
                ReikaWorldHelper.changeAdjBlock(world, pos, side, Fluids.FLOWING_WATER.defaultFluidState().createLegacyBlock());
        }
    }

    public float getTemperatureScaled(int px) {
        return px * (temperature / (float) MAX_TEMP);
    }

    public float getCookScaled(int px) {
        return px * (progress / (float) getOperationTime());
    }

    @Override public void updateTemperature(Level world, BlockPos pos){ updateAmbientTemperature(world, pos); }

    /* --------------------------------------------------------------------- */
    /*  Interface reqs & misc                                                */
    /* --------------------------------------------------------------------- */
    @Override public void animateWithTick(Level world, BlockPos pos) { /* client-side animation placeholder */ }
    @Override public boolean hasModelTransparency(){ return false; }
    @Override public int  getRedstoneOverride(){ return areConditionsMet()?0:15; }
    @Override public void onOverheat(Level world, BlockPos pos){ /* TODO behaviour */ }
    @Override public float getMultiplier(){ return 1f; }
    @Override public boolean canBeCooledWithFins(){ return false; }
    @Override public boolean canBeFrictionHeated(){ return true; }
    @Override public boolean allowExternalHeating(){ return true; }
    @Override public boolean allowHeatExtraction(){ return true; }
    @Override public int  getAmbientTemperature(){ return ReikaWorldHelper.getAmbientTemperatureAt(level, worldPosition); }
    @Override public void resetAmbientTemperatureTimer(){ ambientTimer.reset(); }
    @Override public float getXP(){ return storedXP; }
    @Override public void clearXP(){ storedXP=0; }
    @Override public boolean hasAnInventory(){ return true; }
    @Override public boolean hasATank(){ return false; }
    @Override public boolean areConditionsMet(){ return temperature>=SMELT_TEMP; }
    @Override public String getOperationalStatus(){ return areConditionsMet()?"Operational":"Insufficient Temp/Items"; }
    @Override public MachineRegistry getMachine(){ return MachineRegistry.BLASTFURNACE; }
    @Override public Block getBlockEntityBlockID(){ return RotaryBlocks.BLAST_FURNACE.get(); }
    @Override public int getContainerSize(){ return INTERNAL_SLOT_COUNT; }
    @Override public int getTemperature(){ return temperature; }
    @Override public int getMaxTemperature(){ return MAX_TEMP; }
    @Override public void setTemperature(int t){ temperature=Math.min(t,MAX_TEMP); }
    @Override public void addTemperature(int t){ setTemperature(temperature+t); }
    @Override public int getOperationTime(){ int t=2*((1500-(temperature-SMELT_TEMP))/12); return Math.max(t,1);}
    @Override public int getThermalDamage(){ return 0; }
    @Override public void overheat(Level level, BlockPos pos){ /* todo set lava? */ }
    @Override protected String getTEName(){ return "blast_furnace"; }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        for (int i = 0; i < outputInv.getSlots(); i++) {
            if (!outputInv.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int i) {
        if (i < INTERNAL_SLOT_COUNT) {
            // Internal slots (0-15): use the main item handler
            return itemHandler.getStackInSlot(i);
        } else if (i >= INTERNAL_SLOT_COUNT && i < INTERNAL_SLOT_COUNT + OUTPUT_SLOT_COUNT) {
            // Output slots (16-18): use the output inventory
            return outputInv.getStackInSlot(i - INTERNAL_SLOT_COUNT);
        } else {
            // Invalid slot index
            return ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack removeItem(int i, int i1) {
        ItemStack stack = itemHandler.getStackInSlot(i);
        if (!stack.isEmpty()) {
            ItemStack removed = stack.split(i1);
            if (stack.isEmpty()) {
                itemHandler.setStackInSlot(i, ItemStack.EMPTY);
            } else {
                itemHandler.setStackInSlot(i, stack);
            }
            return removed;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        ItemStack stack = itemHandler.getStackInSlot(i);
        itemHandler.setStackInSlot(i, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        if (i < INTERNAL_SLOT_COUNT) {
            itemHandler.setStackInSlot(i, itemStack);
        } else if (i >= INTERNAL_SLOT_COUNT && i < INTERNAL_SLOT_COUNT + OUTPUT_SLOT_COUNT) {
            outputInv.setStackInSlot(i - INTERNAL_SLOT_COUNT, itemStack);
        } else {
            throw new IndexOutOfBoundsException("Invalid slot index: " + i);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) <= 64 * 64;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            itemHandler.setStackInSlot(i, ItemStack.EMPTY);
        }
        for (int i = 0; i < outputInv.getSlots(); i++) {
            outputInv.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public ItemStackHandler getOutputInventory() {
        return outputInv;
    }
}
