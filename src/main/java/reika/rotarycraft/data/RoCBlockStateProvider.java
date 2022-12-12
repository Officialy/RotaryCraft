package reika.rotarycraft.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reika.rotarycraft.RotaryCraft;

import java.util.ArrayList;
import java.util.List;

public class RoCBlockStateProvider extends BlockStateProvider {
    private static final Logger LOGGER = LogManager.getLogger();

    private final List<String> errors = new ArrayList<>();

    public RoCBlockStateProvider(final DataGenerator gen, final ExistingFileHelper exFileHelper) {
        super(gen, RotaryCraft.MODID, exFileHelper);
    }

    @Override
    public String getName() {
        return "RotaryCraftBlockStates";
    }

    @Override
    protected void registerStatesAndModels() {
        LOGGER.info("Registering block states and models for RotaryCraft");

        // Register block states

    }

}
