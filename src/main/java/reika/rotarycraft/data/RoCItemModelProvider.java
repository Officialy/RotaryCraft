package reika.rotarycraft.data;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.models.AerosolizerModel;
import reika.rotarycraft.models.FinModel;
import reika.rotarycraft.models.engine.DCModel;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;

import java.util.Objects;

public class RoCItemModelProvider extends ItemModelProvider {

    public RoCItemModelProvider(final DataGenerator generator, final ExistingFileHelper existingFileHelper) {
        super(generator.getPackOutput(), RotaryCraft.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        RotaryItems.ITEMS.getEntries().forEach(e ->
                this.basicItem(e.get())
        );

        basicItem(RotaryBlocks.WOOD_SHAFT.get().asItem()).parent(new ModelFile.UncheckedModelFile(new ResourceLocation(RotaryCraft.MODID, "block/blank")));
        basicItem(RotaryBlocks.STONE_SHAFT.get().asItem()).parent(new ModelFile.UncheckedModelFile(new ResourceLocation(RotaryCraft.MODID, "block/blank")));
        basicItem(RotaryBlocks.HSLA_SHAFT.get().asItem()).parent(new ModelFile.UncheckedModelFile(new ResourceLocation(RotaryCraft.MODID, "block/blank")));
        basicItem(RotaryBlocks.TUNGSTEN_SHAFT.get().asItem()).parent(new ModelFile.UncheckedModelFile(new ResourceLocation(RotaryCraft.MODID, "block/blank")));
        basicItem(RotaryBlocks.DIAMOND_SHAFT.get().asItem()).parent(new ModelFile.UncheckedModelFile(new ResourceLocation(RotaryCraft.MODID, "block/blank")));
        basicItem(RotaryBlocks.BEDROCK_SHAFT.get().asItem()).parent(new ModelFile.UncheckedModelFile(new ResourceLocation(RotaryCraft.MODID, "block/blank")));


        var modelparts = DCModel.createLayer().bakeRoot().children.entrySet();
        for (var part : modelparts) {
            part.getValue().cubes.forEach(cube -> {
                var texture = DCModel.TEXTURE_LOCATION.toString().replaceAll("textures/", "").replaceAll(".png", "");

                getBuilder(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(RotaryBlocks.DC_ENGINE.get().asItem())).toString())
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .element().textureAll(texture)
                        .from(cube.minX, cube.minY, cube.minZ)
                        .to(cube.maxX, cube.maxY, cube.maxZ).end();
                        /*.rotation()
                            .origin(part.x, part.y, part.z).end();
                 */
            });
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