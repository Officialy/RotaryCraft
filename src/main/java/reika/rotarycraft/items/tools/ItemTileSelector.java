/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.auxiliary.interfaces.SelectableTiles;
import reika.rotarycraft.base.ItemRotaryTool;
import reika.rotarycraft.registry.RotaryItems;

/** Binds on a normal machine click; subsequent block clicks (including sneak-clicking the machine) select columns. */
public class ItemTileSelector extends ItemRotaryTool {
    public ItemTileSelector() { super(RotaryItems.itemProperties()); }
    @Override public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() == null) return InteractionResult.PASS;
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        var entity = level.getBlockEntity(pos);
        if (entity instanceof SelectableTiles selectable && !context.getPlayer().isShiftKeyDown()) {
            if (!level.isClientSide()) {
                ReikaItemHelper.updateStackTag(context.getItemInHand(), tag -> {
                    tag.putIntArray("locID", selectable.getUniqueID());
                    tag.putString("locDimension", level.dimension().identifier().toString());
                });
                context.getPlayer().sendSystemMessage(Component.translatable("item.rotarycraft.tile_selector_linked", pos.toShortString()));
            }
            return InteractionResult.SUCCESS;
        }
        var controller = getController(level, context.getItemInHand());
        if (controller == null) return InteractionResult.PASS;
        if (!level.isClientSide()) {
            controller.addTile(pos);
            context.getPlayer().sendSystemMessage(Component.translatable("item.rotarycraft.tile_selector_selected", pos.toShortString()));
        }
        return InteractionResult.SUCCESS;
    }
    private SelectableTiles getController(Level level, ItemStack stack) {
        var tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        var xyz = tag.getIntArray("locID").orElse(null);
        if (xyz == null || xyz.length != 3 || !tag.getStringOr("locDimension", level.dimension().identifier().toString()).equals(level.dimension().identifier().toString())) return null;
        BlockPos pos = new BlockPos(xyz[0], xyz[1], xyz[2]);
        return level.hasChunkAt(pos) && level.getBlockEntity(pos) instanceof SelectableTiles selectable ? selectable : null;
    }
}
