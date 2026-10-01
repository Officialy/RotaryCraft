package reika.rotarycraft;

import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.data.RoCItemTagsProvider;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;

final class RotarySurvivalTagTests {
    private RotarySurvivalTagTests() {}
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env) {
        RotaryGameTests.register(event,env,"survival_tags_machines_mine_at_pickaxe_speed",40,RotarySurvivalTagTests::mining);
        RotaryGameTests.register(event,env,"survival_tags_tools_and_enchantments",40,RotarySurvivalTagTests::tools);
        RotaryGameTests.register(event,env,"survival_tags_armor_and_enchantments",40,RotarySurvivalTagTests::armor);
        RotaryGameTests.register(event,env,"survival_tags_material_compatibility",40,RotarySurvivalTagTests::materials);
        RotaryGameTests.register(event,env,"survival_tags_glass_block_item_parity",40,RotarySurvivalTagTests::glass);
    }
    private static void mining(GameTestHelper h) {
        var pick = new ItemStack(Items.IRON_PICKAXE);
        for (var entry : RotaryBlocks.BLOCKS.getEntries()) {
            var block = entry.get();
            if (block instanceof BlockBasicMachine) {
                h.assertTrue(block.defaultBlockState().is(BlockTags.MINEABLE_WITH_PICKAXE),"missing machine mining tag: "+entry.getId());
                h.assertTrue(pick.getDestroySpeed(block.defaultBlockState())>1,"iron pick must accelerate machine mining: "+entry.getId());
            }
        }
        h.assertTrue(!RotaryBlocks.CANOLA.get().defaultBlockState().is(BlockTags.MINEABLE_WITH_PICKAXE),"canola must retain crop behavior");
        h.assertTrue(RotaryBlocks.CRAFTER.get().defaultBlockState().is(BlockTags.BLOCKS_MOTION_NO_LEAVES)
                && RotaryBlocks.ITEMFILTER.get().defaultBlockState().is(BlockTags.BLOCKS_MOTION_NO_LEAVES),"new AE machines must participate in motion/heightmap tags");
        h.succeed();
    }
    private static void tools(GameTestHelper h) {
        var pairs = Map.of(ItemTags.PICKAXES,new Item[]{RotaryItems.HSLA_STEEL_PICKAXE.get(),RotaryItems.BEDROCK_ALLOY_PICK.get()},
                ItemTags.AXES,new Item[]{RotaryItems.HSLA_STEEL_AXE.get(),RotaryItems.BEDROCK_ALLOY_AXE.get()},
                ItemTags.SHOVELS,new Item[]{RotaryItems.HSLA_STEEL_SHOVEL.get(),RotaryItems.BEDROCK_ALLOY_SHOVEL.get()},
                ItemTags.HOES,new Item[]{RotaryItems.HSLA_STEEL_HOE.get(),RotaryItems.BEDROCK_ALLOY_HOE.get()},
                ItemTags.SWORDS,new Item[]{RotaryItems.HSLA_STEEL_SWORD.get(),RotaryItems.BEDROCK_ALLOY_SWORD.get()});
        pairs.forEach((tag,items)-> { for (var item:items) {
            check(h,item,tag); check(h,item,ItemTags.DURABILITY_ENCHANTABLE);
            check(h,item,tag==ItemTags.SWORDS?ItemTags.WEAPON_ENCHANTABLE:ItemTags.MINING_ENCHANTABLE);
        }});
        for(var item: new Item[]{RotaryItems.HSLA_STEEL_SHEARS.get(),RotaryItems.BEDROCK_ALLOY_SHEARS.get()}) {
            check(h,item,RoCItemTagsProvider.common("tools/shear")); check(h,item,ItemTags.MINING_ENCHANTABLE);
        }
        h.succeed();
    }
    private static void armor(GameTestHelper h) {
        for (var item:new Item[]{RotaryItems.HSLA_HELMET.get(),RotaryItems.BEDROCK_ALLOY_HELMET.get(),RotaryItems.NVG.get(),RotaryItems.IO_GOGGLES.get()}) check(h,item,ItemTags.HEAD_ARMOR);
        for (var item:new Item[]{RotaryItems.HSLA_CHESTPLATE.get(),RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get(),RotaryItems.JETPACK.get(),RotaryItems.HSLA_STEEL_PACK.get(),RotaryItems.BEDROCK_ALLOY_PACK.get()}) check(h,item,ItemTags.CHEST_ARMOR);
        for (var item:new Item[]{RotaryItems.HSLA_LEGGINGS.get(),RotaryItems.BEDROCK_ALLOY_LEGGINGS.get()}) check(h,item,ItemTags.LEG_ARMOR);
        for (var item:new Item[]{RotaryItems.HSLA_BOOTS.get(),RotaryItems.BEDROCK_ALLOY_BOOTS.get(),RotaryItems.JUMP.get(),RotaryItems.BEDROCK_ALLOY_JUMP_BOOTS.get()}) {
            check(h,item,ItemTags.FOOT_ARMOR); check(h,item,ItemTags.FOOT_ARMOR_ENCHANTABLE);
        }
        check(h,RotaryItems.HSLA_CHESTPLATE.get(),ItemTags.ARMOR_ENCHANTABLE);
        h.succeed();
    }
    private static void materials(GameTestHelper h) {
        check(h,RotaryItems.ALUMINUM_ALLOY_INGOT.get(),RoCItemTagsProvider.common("ingots/aluminum"));
        check(h,RotaryItems.ALUMINUM_ALLOY_POWDER.get(),RoCItemTagsProvider.common("dusts/aluminum"));
        check(h,RotaryItems.SILVER_INGOT.get(),RoCItemTagsProvider.common("ingots/silver"));
        check(h,RotaryItems.TUNGSTEN_INGOT.get(),RoCItemTagsProvider.common("ingots/tungsten"));
        check(h,RotaryItems.HSLA_STEEL_INGOT.get(),RoCItemTagsProvider.common("ingots/hsla"));
        h.assertTrue(!new ItemStack(RotaryItems.HSLA_STEEL_INGOT.get()).is(RoCItemTagsProvider.STEEL_INGOTS),"HSLA must not enter the foreign steel purifier recipe");
        h.assertTrue(!new ItemStack(RotaryItems.TUNGSTEN_ALLOY_INGOT.get()).is(RoCItemTagsProvider.common("ingots/tungsten")),"tungsten alloy must not substitute for pure tungsten");
        check(h,RotaryItems.CANOLA_SEEDS.get(),RoCItemTagsProvider.common("seeds"));
        check(h,RotaryItems.SAWDUST.get(),RoCItemTagsProvider.common("dusts/wood"));
        h.succeed();
    }
    private static void glass(GameTestHelper h) {
        for (String path: new String[]{"glass_blocks/hardened","glass_panes/hardened"}) {
            var block=path.startsWith("glass_blocks")?RotaryBlocks.BLASTGLASS.get():RotaryBlocks.BLASTPANE.get();
            var key=TagKey.create(net.minecraft.core.registries.Registries.BLOCK,net.minecraft.resources.Identifier.fromNamespaceAndPath("c",path));
            h.assertTrue(block.defaultBlockState().is(key),"glass block tag missing"); check(h,block.asItem(),RoCItemTagsProvider.common(path));
        }
        h.succeed();
    }
    private static void check(GameTestHelper h,Item item,TagKey<Item> tag) {
        h.assertTrue(new ItemStack(item).is(tag),"missing tag "+tag+" for "+item);
    }
}
