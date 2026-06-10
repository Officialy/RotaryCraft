package reika.rotarycraft;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredItem;

public class Tests {

    // 1.21.5: DeferredRegister<Item>#register returns DeferredHolder; DeferredItem needs the
    // typed DeferredRegister.Items helper.
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RotaryCraft.MODID);

    // 1.21.5: Item.Properties needs setId() before Item.<init> dereferences it via effectiveDescriptionId().
    public static final DeferredItem<Item> TEST_ITEM = ITEMS.register("test_item",
            rl -> new ItemTest(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, rl))));

    public static class ItemTest extends Item {

        public ItemTest(Properties p_41383_) {
            super(p_41383_);
        }

        @Override
        public InteractionResult use(Level level, Player player, InteractionHand hand) {
            if (!player.isShiftKeyDown() && player.level().isClientSide() && hand.equals(InteractionHand.MAIN_HAND)) {
                return InteractionResult.SUCCESS;
            } else if (player.isShiftKeyDown()) {

                return InteractionResult.SUCCESS;
            }

            return InteractionResult.FAIL;
        }
    }
}
