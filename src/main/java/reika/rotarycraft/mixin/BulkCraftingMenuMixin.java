package reika.rotarycraft.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import reika.rotarycraft.auxiliary.recipemanagers.BulkShapedRecipe;

/** Vanilla stops after the first empty destination slot; bulk non-stackable yields need several. */
@Mixin(CraftingMenu.class)
abstract class BulkCraftingMenuMixin {
    @Inject(method = "quickMoveStack", at = @At("HEAD"), cancellable = true)
    private void rotarycraft$moveBulkResult(Player player, int slotIndex, CallbackInfoReturnable<ItemStack> callback) {
        if (slotIndex != 0) return;
        CraftingMenu menu = (CraftingMenu) (Object) this;
        Slot resultSlot = menu.getResultSlot();
        if (!(resultSlot.container instanceof ResultContainer results)
                || results.getRecipeUsed() == null
                || !(results.getRecipeUsed().value() instanceof BulkShapedRecipe)) return;

        ItemStack result = resultSlot.getItem();
        if (result.isEmpty()) {
            callback.setReturnValue(ItemStack.EMPTY);
            return;
        }
        // Require room for the entire batch before changing anything or consuming ingredients.
        int capacity = 0;
        for (int i = 45; i >= 10; i--) {
            Slot destination = menu.getSlot(i);
            ItemStack stored = destination.getItem();
            if (destination.mayPlace(result) && (stored.isEmpty() || ItemStack.isSameItemSameComponents(stored, result)))
                capacity += Math.max(0, destination.getMaxStackSize(result) - stored.getCount());
        }
        if (capacity < result.getCount()) {
            callback.setReturnValue(ItemStack.EMPTY);
            return;
        }
        ItemStack crafted = result.copy();
        result.getItem().onCraftedBy(result, player);
        for (int i = 45; i >= 10 && !result.isEmpty(); i--) {
            Slot destination = menu.getSlot(i);
            ItemStack stored = destination.getItem();
            if (!destination.mayPlace(result) || !stored.isEmpty() && !ItemStack.isSameItemSameComponents(stored, result)) continue;
            int amount = Math.min(result.getCount(), destination.getMaxStackSize(result) - stored.getCount());
            if (amount <= 0) continue;
            ItemStack replacement = stored.isEmpty() ? result.copyWithCount(amount) : stored.copyWithCount(stored.getCount() + amount);
            destination.setByPlayer(replacement);
            result.shrink(amount);
        }
        // Keep vanilla's craft awards, recipe remainders and ingredient consumption.
        resultSlot.onQuickCraft(result, crafted);
        resultSlot.setByPlayer(ItemStack.EMPTY);
        resultSlot.onTake(player, result);
        callback.setReturnValue(crafted);
    }
}
