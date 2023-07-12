package reika.rotarycraft;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Tests {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RotaryCraft.MODID);

    public static final RegistryObject<Item> TEST_ITEM = ITEMS.register("test_item", () -> new ItemTest(new Item.Properties()));

    public static class ItemTest extends Item {

        public ItemTest(Properties p_41383_) {
            super(p_41383_);
        }

        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
            if (!player.isShiftKeyDown() && player.level.isClientSide() && hand.equals(InteractionHand.MAIN_HAND)) {
                return InteractionResultHolder.success(this.getDefaultInstance());
            } else if (player.isShiftKeyDown()) {

                return InteractionResultHolder.success(this.getDefaultInstance());
            }

            return InteractionResultHolder.fail(this.getDefaultInstance());
        }
    }
}
