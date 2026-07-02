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

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.RotaryCraft;
import reika.dragonapi.libraries.io.ReikaChatHelper;
import reika.rotarycraft.auxiliary.interfaces.SelectableTiles;
import reika.rotarycraft.base.ItemRotaryTool;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.registry.RotaryItems;

import java.util.Arrays;

public class ItemTileSelector extends ItemRotaryTool {

    public ItemTileSelector() {
        super(RotaryItems.itemProperties());
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        // 1.21.5: InteractionResult.getResult().consumesAction() removed; success-check via SUCCESS instance.
        InteractionResult parent = super.use(level, player, hand);
        if (parent != InteractionResult.SUCCESS && hand.equals(InteractionHand.MAIN_HAND))
            return InteractionResult.FAIL;
        BlockEntity te = level.getBlockEntity(player.blockPosition()); //todo might be broken, needs to be what the player is hovering over
        if (te instanceof SelectableTiles && !player.isShiftKeyDown()) {
            SelectableTiles sc = (SelectableTiles) te;
            this.setID(player.getMainHandItem(), sc.getUniqueID());
            ReikaChatHelper.sendChatToPlayer(player, "Linked to " + te);
            return InteractionResult.SUCCESS;
        }
        SelectableTiles sc = this.getController(level, player.getMainHandItem());
       /* if (sc != null) {
            sc.addTile(pos);
            ReikaChatHelper.sendChatToPlayer(player, "Added [" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "] to " + sc);
        }*/
        return InteractionResult.PASS;
    }

    private SelectableTiles getController(Level world, ItemStack is) {
        CompoundTag nbt = is.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (nbt == null)
            return null;
        // 1.21.5: CompoundTag#getIntArray now returns Optional<int[]>.
        int[] xyz = nbt.getIntArray("locID").orElse(null);
        if (xyz == null || xyz.length < 3) return null;
        BlockEntity te = world.getBlockEntity(new BlockPos(xyz[0], xyz[1], xyz[2]));
        if (te instanceof SelectableTiles) {
            RotaryCraft.LOGGER.debug("Read tile " + te + " at " + Arrays.toString(xyz));
            return (SelectableTiles) te;
        }
        return null;
    }

    private void setID(ItemStack is, int[] id) {
        // 1.21.5: ItemStack.save was removed; persist via the CUSTOM_DATA helper.
        ReikaItemHelper.updateStackTag(is, __T__ -> __T__.putIntArray("locID", id));
        RotaryCraft.LOGGER.debug("Saved tile " + Arrays.toString(id));
    }

}
