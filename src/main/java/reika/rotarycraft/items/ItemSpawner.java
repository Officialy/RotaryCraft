/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.*;
import net.minecraft.world.phys.AABB;
import net.minecraft.sounds.*;
import reika.rotarycraft.registry.*;

/** Full native port of DragonAPI's original recovered spawner item, owned by RotaryCraft. */
public final class ItemSpawner extends Item {
    public ItemSpawner() { super(RotaryItems.itemProperties()); }
    public static EntityType<?> type(ItemStack stack) {
        Identifier id = Identifier.tryParse(stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getStringOr("Spawner", ""));
        return id == null ? null : BuiltInRegistries.ENTITY_TYPE.getOptional(id).orElse(null);
    }
    public static ItemStack typed(EntityType<?> type) {
        ItemStack stack = new ItemStack(RotaryItems.SPAWNER.get()); CompoundTag tag = new CompoundTag();
        tag.putString("Spawner", BuiltInRegistries.ENTITY_TYPE.getKey(type).toString()); stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag)); return stack;
    }
    public static CompoundTag spawnerData(SpawnerBlockEntity tile) {
        var output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, tile.getLevel().registryAccess());
        tile.getSpawner().save(output); return output.buildResult();
    }
    public static ItemStack fromSpawner(SpawnerBlockEntity tile) {
        // Initialize native default spawn data as BaseSpawner itself does (pig).
        tile.getSpawner().getOrCreateDisplayEntity(tile.getLevel(), tile.getBlockPos());
        CompoundTag data = spawnerData(tile);
        String id = data.getCompoundOrEmpty("SpawnData").getCompoundOrEmpty("entity").getStringOr("id", "minecraft:pig");
        Identifier key = Identifier.tryParse(id);
        EntityType<?> type = key == null ? null : BuiltInRegistries.ENTITY_TYPE.getOptional(key).orElse(null);
        if (type == null) throw new IllegalStateException("Unknown spawner entity " + id);
        return typed(type); // Source intentionally recovers type only, not custom logic.
    }
    public static List<EntityType<?>> creativeTypes() {
        return List.of(EntityTypes.CREEPER, EntityTypes.SKELETON, EntityTypes.SPIDER, EntityTypes.GIANT, EntityTypes.ZOMBIE, EntityTypes.SLIME,
                EntityTypes.GHAST, EntityTypes.ZOMBIFIED_PIGLIN, EntityTypes.ENDERMAN, EntityTypes.CAVE_SPIDER, EntityTypes.SILVERFISH,
                EntityTypes.BLAZE, EntityTypes.MAGMA_CUBE, EntityTypes.ENDER_DRAGON, EntityTypes.WITHER, EntityTypes.BAT, EntityTypes.WITCH,
                EntityTypes.PIG, EntityTypes.SHEEP, EntityTypes.COW, EntityTypes.CHICKEN, EntityTypes.SQUID, EntityTypes.WOLF,
                EntityTypes.MOOSHROOM, EntityTypes.SNOW_GOLEM, EntityTypes.OCELOT, EntityTypes.IRON_GOLEM, EntityTypes.VILLAGER);
    }
    public static boolean validDimension(EntityType<?> type, Level level) {
        Identifier required = type.builtInRegistryHolder().getData(PileDriverRules.SPAWNER_DIMENSIONS);
        return required == null || level.dimension().identifier().equals(required);
    }
    @Override public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand(); EntityType<?> type = type(stack); var player = context.getPlayer(); Level level = context.getLevel();
        if (type == null || !validDimension(type, level)) {
            if (player != null && !level.isClientSide()) player.sendSystemMessage(type == null
                    ? Component.translatable("message.rotarycraft.spawner.no_type")
                    : Component.translatable("message.rotarycraft.spawner.dimension", type.getDescription(), level.dimension().identifier().toString()));
            return InteractionResult.FAIL;
        }
        var clicked = context.getClickedPos();
        if (!level.hasChunkAt(clicked) || level.isOutsideBuildHeight(clicked)) return InteractionResult.FAIL;
        // Original chooses a soft clicked block first, then the clicked-face neighbour.
        var pos = reika.dragonapi.libraries.level.ReikaWorldHelper.softBlocks(level, clicked) ? clicked : clicked.relative(context.getClickedFace());
        if (!level.hasChunkAt(pos) || level.isOutsideBuildHeight(pos) || player == null || !player.mayUseItemAt(pos, context.getClickedFace(), stack)
                || !reika.dragonapi.libraries.level.ReikaWorldHelper.softBlocks(level, pos) || !level.getEntitiesOfClass(LivingEntity.class, new AABB(pos)).isEmpty()
                || !level.isUnobstructed(Blocks.SPAWNER.defaultBlockState(), pos, net.minecraft.world.phys.shapes.CollisionContext.of(player))) return InteractionResult.FAIL;
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        CompoundTag custom = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (!level.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 3)) return InteractionResult.FAIL;
        var tile = (SpawnerBlockEntity)level.getBlockEntity(pos);
        CompoundTag data = custom.getCompoundOrEmpty("logic").copy();
        int min = Math.max(0, data.getIntOr("MinSpawnDelay", 200)), max = Math.max(0, data.getIntOr("MaxSpawnDelay", 800));
        data.putShort("Delay", (short)Math.max(min, level.getRandom().nextInt((int)Math.min(900L, 1L + max))));
        tile.getSpawner().load(level, pos, TagValueInput.create(ProblemReporter.DISCARDING, level.registryAccess(), data));
        tile.setEntityId(type, level.getRandom()); tile.setChanged(); level.sendBlockUpdated(pos, tile.getBlockState(), tile.getBlockState(), 3);
        level.playSound(null, pos, SoundEvents.STONE_STEP, SoundSource.BLOCKS, 1, 1.5F);
        level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(player, tile.getBlockState()));
        if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) net.minecraft.advancements.triggers.CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, pos, stack);
        stack.consume(1, player); return InteractionResult.SUCCESS;
    }
    @Override public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> lines, TooltipFlag flag) {
        if (!stack.has(DataComponents.CUSTOM_DATA)) return;
        EntityType<?> type = type(stack);
        lines.accept(type == null ? Component.translatable("tooltip.rotarycraft.spawner.no_entity") : Component.translatable("tooltip.rotarycraft.spawner.type", type.getDescription()));
        var data = stack.get(DataComponents.CUSTOM_DATA).copyTag();
        if (!data.contains("logic")) { lines.accept(Component.translatable("tooltip.rotarycraft.spawner.defaults")); return; }
        if (!flag.hasShiftDown() && !flag.shouldDisplayAllInformation()) { lines.accept(Component.translatable("tooltip.rotarycraft.spawner.shift").withStyle(net.minecraft.ChatFormatting.LIGHT_PURPLE)); return; }
        CompoundTag logic = data.getCompoundOrEmpty("logic");
        String[] keys = {"MinSpawnDelay", "MaxSpawnDelay", "MaxNearbyEntities", "SpawnCount", "SpawnRange", "RequiredPlayerRange"};
        int[] defaults = {200, 800, 6, 4, 4, 16};
        for (int n = 0; n < keys.length; n++) lines.accept(Component.translatable("tooltip.rotarycraft.spawner." + keys[n], logic.getIntOr(keys[n], defaults[n])));
    }
}
