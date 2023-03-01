package reika.rotarycraft.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.registry.MachineRegistry;
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

        for (var machine : MachineRegistry.values()) {
            basicItem(machine.getBlockState().getBlock().asItem()).parent(new ModelFile.UncheckedModelFile(new ResourceLocation(RotaryCraft.MODID, "block/blank")));
        }
    }

    /**
     * Gets a name for this provider, to use in logging.
     */
    @Override
    public String getName() {
        return "RotaryCraftItemModels";
    }


}