package reika.rotarycraft.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.common.data.ExistingFileHelper;
import net.neoforged.data.event.GatherDataEvent;
import net.neoforged.eventbus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.registries.ForgeRegistries;
import reika.rotarycraft.RotaryCraft;

@Mod.EventBusSubscriber(modid = RotaryCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RoCDataProviders {

    @SubscribeEvent
    public static void registerDataProviders(final GatherDataEvent event) {
        final DataGenerator dataGenerator = event.getGenerator();
        final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        final RoCItemModelProvider itemModelProvider = new RoCItemModelProvider(dataGenerator, existingFileHelper);
        dataGenerator.addProvider(true, itemModelProvider);
        dataGenerator.addProvider(true, new RotaryRecipeProvider(dataGenerator.getPackOutput()));
//        dataGenerator.addProvider(true, new RoCBlockLootTableProvider(dataGenerator));
//        dataGenerator.addProvider(true, new RoCAdvancementProvider(dataGenerator));
//        dataGenerator.addProvider(true, new RoCBlockTagsProvider(dataGenerator));
//        dataGenerator.addProvider(true, new RoCItemTagsProvider(dataGenerator));
        dataGenerator.addProvider(true, new RotaryLang(dataGenerator, "en_us"));
        dataGenerator.addProvider(true, new RoCBlockStateProvider(dataGenerator, itemModelProvider.existingFileHelper));

    }

    protected static String name(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block).getPath();
    }

    protected static String name(Item item) {
        return ForgeRegistries.ITEMS.getKey(item).getPath();
    }
}
