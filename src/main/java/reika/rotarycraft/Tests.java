package reika.rotarycraft;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.registry.RotaryItems;

public class Tests {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RotaryCraft.MODID);

    public static final RegistryObject<Item> TEST_ITEM = ITEMS.register("test_item", () -> new ItemTest(new Item.Properties()));

    public static class ItemTest extends Item {

        public ItemTest(Properties p_41383_) {
            super(p_41383_);
        }

        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
            if (!player.isShiftKeyDown() && player.level.isClientSide()) {
//                IORenderer.renderIn(new PoseStack(), player.getOnPos(), 255);
//                RotaryCraft.LOGGER.info("Rendering test item");

                return InteractionResultHolder.success(this.getDefaultInstance());
            } else if (player.isShiftKeyDown()) {
//                RotaryCraft.LOGGER.info("machinemappings" + MachineRegistry.machineMappings);
                return InteractionResultHolder.success(this.getDefaultInstance());
            }

            return InteractionResultHolder.fail(this.getDefaultInstance());
        }
    }
}
