package reika.rotarycraft;

import java.util.*;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import reika.dragonapi.instantiable.data.KeyedItemStack;
import reika.dragonapi.instantiable.data.blockstruct.*;
import reika.dragonapi.instantiable.data.immutable.*;
import reika.dragonapi.instantiable.data.maps.ItemHashMap;
import reika.rotarycraft.registry.RotaryBlocks;

final class DragonAPIExtendedAuditTests {
    private static final BlockPos POS = new BlockPos(4, 1, 4);
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> environment) {
        RotaryGameTests.register(event, environment, "dragonapi_extended_world", 40, DragonAPIExtendedAuditTests::world);
        RotaryGameTests.register(event, environment, "dragonapi_extended_items", 40, DragonAPIExtendedAuditTests::items);
        RotaryGameTests.register(event, environment, "dragonapi_extended_menu", 40, DragonAPIExtendedAuditTests::menu);
        RotaryGameTests.register(event, environment, "dragonapi_extended_resources", 40, DragonAPIExtendedAuditTests::resources);
    }
    private static void world(GameTestHelper h) {
        var level = h.getLevel(); var pos = h.absolutePos(POS);
        var location = new DecimalPosition(pos.getX()+0.25, pos.getY()+0.5, pos.getZ()+0.75);
        h.assertTrue(location.setBlock(level, Blocks.STONE), "decimal block placement must terminate and change the world");
        h.assertTrue(level.getBlockState(pos).is(Blocks.STONE), "placed state must be stone");
        var array = new BlockArray(List.of(pos, pos.above())).setWorld(level); array.setTo(Blocks.DIRT);
        h.assertTrue(level.getBlockState(pos).is(Blocks.DIRT) && level.getBlockState(pos.above()).is(Blocks.DIRT), "array writes every block");
        array.clearArea(); h.assertTrue(level.getBlockState(pos).isAir() && level.getBlockState(pos.above()).isAir(), "clearArea removes both positions");
        level.setBlock(pos, Blocks.OAK_LOG.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.X), 3);
        level.setBlock(pos.above(), Blocks.OAK_LOG.defaultBlockState(), 3);
        var structured = new StructuredBlockArray(level); structured.addBlockCoordinate(pos); structured.addBlockCoordinate(pos.above());
        h.assertTrue(structured.getNumberOf(Blocks.OAK_LOG)==2, "block matching must count logs of different axes");
        var iterator = structured.iterator(); var removed = iterator.next(); iterator.remove();
        h.assertTrue(structured.getSize()==1 && !structured.keySet().contains(removed) && structured.getNumberOf(Blocks.OAK_LOG)==1, "iterator removal must synchronize structured data");
        new MultiBlockBlueprint(1,1,1).addOverwriteableBlock(Blocks.OAK_LOG).addBlockAt(0,0,0,Blocks.GOLD_BLOCK).createInWorld(level,pos.getX(),pos.getY(),pos.getZ());
        h.assertTrue(level.getBlockState(pos).is(Blocks.GOLD_BLOCK), "blueprint overwrite uses block identity");
        var stateKey = new BlockKey(Blocks.OAK_LOG.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.X));
        var tag = new CompoundTag(); stateKey.saveAdditional("state",tag);
        h.assertTrue(stateKey.equals(BlockKey.load("state",tag)), "block state persistence retains orientation");
        var event = new reika.dragonapi.instantiable.event.SetBlockEvent.Post(level.getChunkAt(pos), pos.getX() & 15,pos.getY(),pos.getZ() & 15);
        h.assertTrue(!event.isWorldgen, "normal full chunk mutation is not generation");
        location.dropItem(level, new ItemStack(Items.DIAMOND), 0);
        h.assertTrue(!level.getEntitiesOfClass(net.minecraft.world.entity.item.ItemEntity.class, location.getAABB(2), e -> e.getItem().is(Items.DIAMOND)).isEmpty(), "decimal drop spawns an actual item");
        var declared = new FilledBlockArray(level);
        declared.setBlock(pos.getX(),pos.getY(),pos.getZ(),Blocks.OAK_LOG.defaultBlockState().setValue(BlockStateProperties.AXIS,Direction.Axis.Z));
        declared.setPlacementOverride(pos.getX(),pos.getY(),pos.getZ(),Blocks.DIAMOND_BLOCK.defaultBlockState());
        declared.offset(new BlockPos(1,0,0)); declared.place();
        h.assertTrue(level.getBlockState(pos.east()).is(Blocks.DIAMOND_BLOCK) && declared.getSize()==1, "coordinate offsets move checks and placement overrides together");
        var declaredWithoutWorld = new FilledBlockArray(null); declaredWithoutWorld.setBlock(1,2,3,Blocks.STONE);
        h.assertTrue(declaredWithoutWorld.flipX().hasBlock(new BlockPos(-1,2,3)), "declared layouts can mirror without reading a live world");
        h.succeed();
    }
    private static void items(GameTestHelper h) {
        var first = new ItemStack(Items.IRON_SWORD); first.set(DataComponents.CUSTOM_NAME,Component.literal("Audit sword"));
        var otherCount = first.copyWithCount(3);
        var immutable = new ImmutableItemStack(first); var equal = new ImmutableItemStack(otherCount);
        h.assertTrue(immutable.equals(equal) && immutable.hashCode()==equal.hashCode(), "equal patched stacks must hash equally regardless of count");
        immutable.getItemStackReference().set(DataComponents.CUSTOM_NAME,Component.literal("Mutation"));
        h.assertTrue(immutable.equals(equal), "immutable stack accessors must return defensive copies");
        var map = new ItemHashMap<String>().enableNBT(); map.put(first,"saved");
        var reconstructed = map.keySet().iterator().next();
        h.assertTrue(ItemStack.isSameItemSameComponents(first,reconstructed) && "saved".equals(map.get(reconstructed)), "iterated component keys retain lookup identity");
        map.put(new ItemStack(Items.IRON_SWORD),"plain"); h.assertTrue(map.size()==2 && "plain".equals(map.get(new ItemStack(Items.IRON_SWORD))), "plain and patched keys remain distinct");
        h.assertTrue("saved".equals(map.clone().get(first)), "cloning retains component mode");
        var keyed = new KeyedItemStack(first); var same = new KeyedItemStack(otherCount).setSimpleHash(true);
        h.assertTrue(keyed.equals(same) && keyed.hashCode()==same.hashCode() && keyed.compareTo(same)==0, "key hash optimizations and ignored counts must not alter equality");
        var fuzzy = new KeyedItemStack(first).setIgnoreNBT(true);
        h.assertTrue(!keyed.equals(fuzzy) && keyed.matches(fuzzy), "fuzzy matching is explicit and is not collection equality");
        var changed = first.copy(); changed.set(DataComponents.CUSTOM_NAME,Component.literal("Different"));
        var other = new KeyedItemStack(changed);
        h.assertTrue(keyed.compareTo(other)!=0 && Integer.signum(keyed.compareTo(other)) == -Integer.signum(other.compareTo(keyed)), "component ordering is antisymmetric");
        var transientType = net.minecraft.core.component.DataComponentType.<Integer>builder().networkSynchronized(net.minecraft.network.codec.ByteBufCodecs.VAR_INT).build();
        var transientFirst = first.copy(); transientFirst.set(transientType,1);
        var transientSecond = first.copy(); transientSecond.set(transientType,2);
        var t1 = new KeyedItemStack(transientFirst); var t2 = new KeyedItemStack(transientSecond); var t1Copy = t1.copy();
        h.assertTrue(t1.compareTo(t2)!=0 && t1.compareTo(t1Copy)==0 && Integer.signum(t1.compareTo(t2))==Integer.signum(t1Copy.compareTo(t2)), "transient components and equal copies must obey the same total ordering");
        h.assertTrue(new reika.dragonapi.instantiable.ItemMatch(Items.IRON_SWORD).match(first), "item-only ingredients must retain fuzzy component matching");
        h.assertTrue(reika.dragonapi.modregistry.PowerTypes.RF.isLoaded() && reika.dragonapi.modregistry.PowerTypes.FE.isLoaded(), "NeoForge energy integrations are available");
        var ores = reika.dragonapi.modregistry.ModOreList.COPPER;
        h.assertTrue(ores.existsInGame(), "ore catalogue is populated after native tag loading");
        var stone = ores.getGennableIn(Blocks.STONE);
        var deepslate = ores.getGennableIn(Blocks.DEEPSLATE);
        h.assertTrue(stone.stream().anyMatch(stack -> stack.is(Items.COPPER_ORE)) && stone.stream().noneMatch(stack -> stack.is(Items.DEEPSLATE_COPPER_ORE)), "stone hosts select stone ore variants");
        h.assertTrue(deepslate.stream().anyMatch(stack -> stack.is(Items.DEEPSLATE_COPPER_ORE)) && deepslate.stream().noneMatch(stack -> stack.is(Items.COPPER_ORE)), "deepslate hosts select deepslate ore variants");
        h.assertTrue(reika.dragonapi.modregistry.ModOreList.getOreModFromItemStack(new ItemStack(Items.STICK))==null, "ordinary items are not ore owners");
        h.succeed();
    }
    private static void menu(GameTestHelper h) {
        h.setBlock(POS,RotaryBlocks.DC_ENGINE.get());
        var tile=(reika.dragonapi.base.BlockEntityBase)h.getLevel().getBlockEntity(h.absolutePos(POS));
        var player=DragonAPIAuditTests.player(h); player.setPos(net.minecraft.world.phys.Vec3.atCenterOf(tile.getBlockPos()));
        var menu=new reika.dragonapi.base.CoreContainer<>(null,1,player.getInventory(),tile);
        menu.addSlotRelay(player.getInventory(),9);
        h.assertTrue(menu.findSlot(player.getInventory(),9).orElse(-1)==0, "relay lookup returns menu index, not backing inventory index");
        h.assertTrue(menu.findSlot(player.getInventory(),8).isEmpty(), "unregistered slots remain absent");
        h.assertTrue(menu.stillValid(player), "nearby live tile is accessible"); player.setPos(player.position().add(20,0,0));
        h.assertTrue(!menu.stillValid(player), "ordinary menu closes beyond eight blocks"); menu.setAlwaysInteractable();
        h.assertTrue(menu.stillValid(player), "explicit remote-access menus bypass distance");
        var fallback=menu.getSlot(-1); fallback.set(new ItemStack(Items.DIAMOND));
        h.assertTrue(fallback.getItem().isEmpty() && !fallback.mayPlace(new ItemStack(Items.DIAMOND)) && !fallback.mayPickup(player), "invalid slot cannot store or transfer items");
        h.setBlock(POS,Blocks.AIR); h.assertTrue(!menu.stillValid(player), "remote-access menus still reject a removed tile");
        if (net.neoforged.fml.ModList.get().isLoaded("chromaticraft")) {
            var glow = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getValue(net.minecraft.resources.Identifier.parse("chromaticraft:glow_log"));
            var leaf = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getValue(net.minecraft.resources.Identifier.parse("chromaticraft:glowing_leaves"));
            var catalogue = reika.dragonapi.modregistry.ModWoodList.getModWood(glow);
            h.assertTrue(catalogue==reika.dragonapi.modregistry.ModWoodList.LIGHTED && catalogue.exists() && catalogue.canBePlacedSideways(), "accepted glow logs resolve through current registry identifiers and axis support");
            h.assertTrue(reika.dragonapi.modregistry.ModWoodList.getModWoodFromLeaf(leaf)==catalogue, "accepted glow leaves resolve to their wood species");
        }
        h.succeed();
    }
    private static void resources(GameTestHelper h) {
        var manager=reika.dragonapi.io.DirectResourceManager.getInstance();
        var id=net.minecraft.resources.Identifier.parse("dragonapi:audit/item.txt");
        var wrongNamespace=net.minecraft.resources.Identifier.parse("other:audit/item.txt");
        try {
            var file=java.nio.file.Files.createTempFile("dragonapi-audit-asset", ".txt");
            try {
                java.nio.file.Files.writeString(file,"asset data");
                manager.registerCustomPath(id,file.toString(),net.minecraft.sounds.SoundSource.MASTER,false);
                var resource=manager.getResource(id).orElseThrow();
                try(var stream=resource.open()) { h.assertTrue("asset data".equals(new String(stream.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8)), "asset pack opens registered filesystem content"); }
                h.assertTrue(resource.sourcePackId().equals("dragonapi/direct_assets"), "resource owns pack metadata");
                h.assertTrue(manager.getResource(wrongNamespace).isEmpty() && manager.getResourceStack(wrongNamespace).isEmpty(), "namespace collisions and unknown stacks must remain missing");
                h.assertTrue(manager.getNamespaces().contains("dragonapi") && manager.listResources("audit", x -> true).containsKey(id), "registered assets support namespace and directory enumeration");
                java.nio.file.Files.writeString(file,"changed data"); manager.onResourceManagerReload(manager);
                try(var stream=resource.open()) { h.assertTrue("changed data".equals(new String(stream.readAllBytes(),java.nio.charset.StandardCharsets.UTF_8)), "reload invalidates cached bytes"); }
            } finally { java.nio.file.Files.deleteIfExists(file); }
        } catch(java.io.IOException error) { throw new IllegalStateException(error); }
        h.succeed();
    }
}
