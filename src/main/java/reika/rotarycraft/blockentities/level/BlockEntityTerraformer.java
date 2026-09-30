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

import java.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.commands.FillBiomeCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import reika.dragonapi.libraries.ReikaPlayerAPI;
import reika.rotarycraft.api.interfaces.TerraformerAreaProvider;
import reika.rotarycraft.auxiliary.TerraformerTerrain;
import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
import reika.rotarycraft.auxiliary.interfaces.SelectableTiles;
import reika.rotarycraft.auxiliary.recipemanagers.TerraformingRecipe;
import reika.rotarycraft.base.blockentity.InventoriedPowerLiquidReceiver;
import reika.rotarycraft.gui.container.machine.inventory.ContainerTerraformer;
import reika.rotarycraft.modinterface.jei.RotaryRecipeSync;
import reika.rotarycraft.registry.*;

/** A modern biome cell contains 16 legacy columns; each completed cell pays all sixteen column costs. */
public class BlockEntityTerraformer extends InventoriedPowerLiquidReceiver implements SelectableTiles, DiscreteFunction {
    private final LinkedHashSet<BlockPos> coordinates = new LinkedHashSet<>();
    private final LinkedHashSet<BlockPos> selectedArea = new LinkedHashSet<>();
    private ResourceKey<Biome> target;
    public int operationTicks;
    public int queuedCells;
    public BlockEntityTerraformer(BlockPos pos, BlockState state) { super(RotaryBlockEntities.TERRAFORMER.get(), pos, state); }

    public static BlockPos cell(BlockPos pos) { return new BlockPos(QuartPos.toBlock(QuartPos.fromBlock(pos.getX())), 0, QuartPos.toBlock(QuartPos.fromBlock(pos.getZ()))); }
    @Override public int[] getUniqueID() { return new int[] {worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()}; }
    @Override public void addTile(BlockPos pos) {
        if (level == null || level.isClientSide() || level.hasNeighborSignal(worldPosition) || !level.getWorldBorder().isWithinBounds(pos)) return;
        if (coordinates.isEmpty()) selectedArea.clear();
        selectedArea.add(cell(pos));
        if (coordinates.add(cell(pos))) { queuedCells = coordinates.size(); setChanged(); syncAllData(false); }
    }
    public Set<BlockPos> getCoordinates() { return Collections.unmodifiableSet(coordinates); }
    public ResourceKey<Biome> getTarget() { return target; }
    public void setTarget(ResourceKey<Biome> biome) { target = biome; setChanged(); syncAllData(false); }
    public Holder<Biome> getCentralBiome() { return biomeAt(worldPosition); }
    private Holder<Biome> biomeAt(BlockPos pos) {
        return level.getNoiseBiome(QuartPos.fromBlock(pos.getX()), QuartPos.fromBlock(worldPosition.getY()), QuartPos.fromBlock(pos.getZ()));
    }
    public static List<TerraformingRecipe> getTransformList(Level level) {
        var data = RotaryRecipeSync.getRecipes(level);
        return data == null ? List.of() : data.byType(RotaryRecipeTypes.TERRAFORMER.get()).stream().map(h -> h.value()).toList();
    }
    public List<TerraformingRecipe> getValidTargetBiomes(Holder<Biome> from) {
        return getTransformList(level).stream().filter(r -> from.is(r.source()))
                .sorted(Comparator.comparing(r -> r.target().identifier().toString())).toList();
    }
    public TerraformingRecipe getTransform(Holder<Biome> from) {
        return getTransformList(level).stream().filter(r -> r.target().equals(target) && from.is(r.source())).findFirst().orElse(null);
    }
    @Override protected void onFirstTick(Level world, BlockPos pos) {
        if (world.isClientSide() || !coordinates.isEmpty()) return;
        for (Direction side : Direction.Plane.HORIZONTAL) {
            var entity = world.getBlockEntity(pos.relative(side));
            if (entity instanceof TerraformerAreaProvider area) {
                BlockPos min = area.minimum(), max = area.maximum();
                for (int x = min.getX(); x <= max.getX(); x += 4)
                    for (int z = min.getZ(); z <= max.getZ(); z += 4) addTile(new BlockPos(x, pos.getY(), z));
                // Include partial cells on the far edges of an inclusive marker rectangle.
                for (int x = min.getX(); x <= max.getX(); x += 4) addTile(new BlockPos(x, pos.getY(), max.getZ()));
                for (int z = min.getZ(); z <= max.getZ(); z += 4) addTile(new BlockPos(max.getX(), pos.getY(), z));
                addTile(new BlockPos(max.getX(), pos.getY(), max.getZ()));
                area.removeAreaMarker();
                return;
            }
        }
        // BUILDCRAFT-PORT: bridge buildcraft.api.core.IAreaProvider into TerraformerAreaProvider when
        // a 26.3 BuildCraft API is available: xMin/xMax/zMin/zMax (inclusive), then removeFromWorld.
    }
    @Override public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        if (!(world instanceof ServerLevel server)) return;
        getSummativeSidedPower();
        operationTicks = Math.min(operationTicks + 1, getOperationTime());
        if (coordinates.isEmpty() || !hasRedstoneSignal()) return;
        if (operationTicks >= getOperationTime()) {
            var available = coordinates.stream().filter(server::hasChunkAt).toList();
            if (!available.isEmpty()) {
                BlockPos selected = available.get(server.getRandom().nextInt(available.size()));
                if (transformCell(server, selected)) {
                    coordinates.remove(selected); queuedCells = coordinates.size();
                }
            }
            operationTicks = 0; setChanged(); syncAllData(false);
        }
    }
    private boolean transformCell(ServerLevel world, BlockPos pos) {
        if (target == null || !world.hasChunkAt(pos)) return false;
        var from = biomeAt(pos);
        // An already converted cell (including another machine's work) is completed without charging twice.
        if (from.is(target)) return true;
        var recipe = getTransform(from);
        var to = world.registryAccess().lookupOrThrow(Registries.BIOME).get(target).orElse(null);
        if (recipe == null || to == null || power < recipe.minPower() || tank.getFluidLevel() < recipe.water() * 16) return false;
        var owner = getServerPlacer();
        if (owner == null && getFakePlacer() instanceof net.minecraft.server.level.ServerPlayer fake) owner = fake;
        for (int x = pos.getX(); x < pos.getX() + 4; x++) for (int z = pos.getZ(); z < pos.getZ() + 4; z++) {
            var check = new BlockPos(x, worldPosition.getY(), z);
            if (!world.getWorldBorder().isWithinBounds(check) || owner != null
                    && !ReikaPlayerAPI.playerCanBreakAt(world, check, world.getBlockState(check), owner)) return false;
        }
        var remainder = new ItemStack[getContainerSize()];
        for (int n = 0; n < remainder.length; n++) remainder[n] = getItem(n).copy();
        // Prepare the complete cell's resource debit before changing the biome; no partial batch losses.
        for (int column = 0; column < 16; column++) for (var cost : recipe.items()) {
            int slot = -1;
            for (int n = 0; n < remainder.length; n++) if (cost.ingredient().test(remainder[n])) { slot = n; break; }
            if (slot < 0) return false;
            if (cost.consume(world.getRandom())) remainder[slot].shrink(1);
        }
        boolean terrain = modifyBlocks();
        var result = FillBiomeCommand.fill(world, new BlockPos(pos.getX(), world.getMinY(), pos.getZ()),
                new BlockPos(pos.getX() + 3, world.getMaxY() - 1, pos.getZ() + 3), to);
        if (result.left().isEmpty()) return false;
        for (int n = 0; n < remainder.length; n++) setItem(n, remainder[n]);
        if (recipe.water() > 0) tank.removeLiquid(recipe.water() * 16);
        TerraformerTerrain.apply(world, pos, recipe, to.value(), terrain, owner, selectedArea);
        return true;
    }
    public boolean modifyBlocks() {
        return ConfigRegistry.BIOMEBLOCKS.getState() && java.util.stream.IntStream.range(0, getContainerSize()).anyMatch(n -> getItem(n).is(Items.DIAMOND));
    }
    @Override protected void animateWithTick(Level world, BlockPos pos) { /* The original full-cube machine has no moving parts. */ }
    @Override public int getOperationTime() { return DurationRegistry.TERRAFORMER.getOperationTime(omega); }
    @Override public int getContainerSize() { return 54; }
    @Override public int getCapacity() { return 24000; }
    @Override public Fluid getInputFluid() { return Fluids.WATER; }
    @Override public boolean canReceiveFrom(Direction side) { return true; }
    @Override public boolean canConnectToPipe(MachineRegistry machine) { return machine.isStandardPipe(); }
    @Override public boolean hasATank() { return true; }
    @Override public boolean hasAnInventory() { return true; }
    @Override public boolean isItemValidForSlot(int slot, ItemStack stack) { return true; }
    @Override public MachineRegistry getMachine() { return MachineRegistry.TERRAFORMER; }
    @Override protected String getTEName() { return "Terraformer"; }
    @Override public net.minecraft.world.level.block.Block getBlockEntityBlockID() { return RotaryBlocks.TERRAFORMER.get(); }
    @Override public boolean hasModelTransparency() { return false; }
    @Override public int getRedstoneOverride() { return 0; }
    public ItemStack getItem(int slot) { return itemHandler.getStackInSlot(slot); }
    public ItemStack removeItem(int slot, int count) { return itemHandler.extractItem(slot, count, false); }
    public ItemStack removeItemNoUpdate(int slot) { return removeItem(slot, getItem(slot).getCount()); }
    public void setItem(int slot, ItemStack stack) { itemHandler.setStackInSlot(slot, stack); }
    public boolean stillValid(Player player) { return isPlayerAccessible(player); }
    public void clearContent() { for (int n = 0; n < 54; n++) setItem(n, ItemStack.EMPTY); }
    @Override public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        if (level != null && !level.isClientSide()) {
            // This liquid receiver uses managed slots rather than Container; spill them on every removal path.
            for (int n = 0; n < getContainerSize(); n++) {
                var stack = removeItemNoUpdate(n);
                if (!stack.isEmpty()) net.minecraft.world.Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
            }
        }
        super.preRemoveSideEffects(pos, state);
    }
    public int[] getSlotsForFace(Direction side) { return java.util.stream.IntStream.range(0, 54).toArray(); }
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) { return true; }
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) { return false; }
    @Override public net.neoforged.neoforge.transfer.ResourceHandler<net.neoforged.neoforge.transfer.item.ItemResource> getAutomationItemHandler() {
        return new net.neoforged.neoforge.transfer.DelegatingResourceHandler<>(() -> itemHandler) {
            @Override public int extract(int index, net.neoforged.neoforge.transfer.item.ItemResource item, int amount, net.neoforged.neoforge.transfer.transaction.TransactionContext transaction) {
                net.neoforged.neoforge.transfer.TransferPreconditions.checkNonEmptyNonNegative(item, amount);
                java.util.Objects.checkIndex(index, size());
                return 0; // Original canExtractItem disallows every automated extraction; manual slots remain usable.
            }
            @Override public int extract(net.neoforged.neoforge.transfer.item.ItemResource item, int amount, net.neoforged.neoforge.transfer.transaction.TransactionContext transaction) {
                net.neoforged.neoforge.transfer.TransferPreconditions.checkNonEmptyNonNegative(item, amount);
                return 0;
            }
        };
    }
    @Override public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) { return new ContainerTerraformer(id, inv, this); }
    @Override protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);
        if (target != null) tag.putString("targetBiome", target.identifier().toString());
        tag.putInt("operationTicks", operationTicks);
        tag.putLongArray("terrainArea", selectedArea.stream().mapToLong(BlockPos::asLong).toArray());
        tag.putLongArray("selectedCells", coordinates.stream().mapToLong(BlockPos::asLong).toArray());
    }
    @Override protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);
        var id = Identifier.tryParse(tag.getStringOr("targetBiome", ""));
        target = id == null ? null : ResourceKey.create(Registries.BIOME, id);
        operationTicks = Math.clamp(tag.getIntOr("operationTicks", 0), 0, 800);
        coordinates.clear();
        for (long value : tag.getLongArray("selectedCells").orElse(new long[0])) coordinates.add(cell(BlockPos.of(value)));
        selectedArea.clear();
        for (long value : tag.getLongArray("terrainArea").orElse(new long[0])) selectedArea.add(cell(BlockPos.of(value)));
        selectedArea.addAll(coordinates);
        queuedCells = coordinates.size();
    }
}
