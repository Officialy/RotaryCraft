package reika.rotarycraft.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import reika.rotarycraft.RotaryCraft;

@Mod.EventBusSubscriber(modid = RotaryCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RoCDataProviders {

    @SubscribeEvent
    public static void registerDataProviders(final GatherDataEvent event) {
        final DataGenerator dataGenerator = event.getGenerator();
        final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        //dataGenerator.addProvider(new RoCLanguageProvider(dataGenerator));

        final RoCItemModelProvider itemModelProvider = new RoCItemModelProvider(dataGenerator, existingFileHelper);
        dataGenerator.addProvider(true, itemModelProvider);

        // Let blockstate provider see generated item models by passing its existing file helper
        dataGenerator.addProvider(true, new RoCBlockStateProvider(dataGenerator, itemModelProvider.existingFileHelper));

    }
}
