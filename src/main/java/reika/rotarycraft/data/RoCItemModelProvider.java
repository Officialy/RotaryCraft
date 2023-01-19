package reika.rotarycraft.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.registry.RotaryItems;

public class RoCItemModelProvider extends ItemModelProvider {

    public RoCItemModelProvider(final DataGenerator generator, final ExistingFileHelper existingFileHelper) {
        super(generator.getPackOutput(), RotaryCraft.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        RotaryItems.ITEMS.getEntries().forEach(e ->
                this.basicItem(e.get())
                );
//        RotaryBlocks.ITEMS.getEntries().forEach(e -> getBuilder(e.get().getRegistryName().toString()).parent(itemGenerated).texture("layer0", "item/" + e.get().getRegistryName().getPath()));
    }

    /**
     * Gets a name for this provider, to use in logging.
     */
    @Override
    public String getName() {
        return "RotaryCraftItemModels";
    }


}