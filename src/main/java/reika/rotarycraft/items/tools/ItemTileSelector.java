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

import reika.rotarycraft.RotaryCraft;
import reika.dragonapi.libraries.io.ReikaChatHelper;
import reika.rotarycraft.auxiliary.interfaces.SelectableTiles;
import reika.rotarycraft.base.ItemRotaryTool;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Arrays;

public class ItemTileSelector extends ItemRotaryTool {

    public ItemTileSelector() {
        super(new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!super.use(level, player, hand).getResult().consumesAction()) //todo fix this its broke, "consumes action"??
            return InteractionResultHolder.fail(this.asItem().getDefaultInstance());
        BlockEntity te = level.getBlockEntity(player.blockPosition()); //todo might be broken, needs to be what the player is hovering over
        if (te instanceof SelectableTiles && !player.isShiftKeyDown()) {
            SelectableTiles sc = (SelectableTiles) te;
            this.setID(player.getMainHandItem(), sc.getUniqueID());
            ReikaChatHelper.sendChatToPlayer(player, "Linked to " + te);
            return new InteractionResultHolder(InteractionResult.SUCCESS, true);
        }
        SelectableTiles sc = this.getController(level, player.getMainHandItem());
       /* if (sc != null) {
            sc.addTile(pos);
            ReikaChatHelper.sendChatToPlayer(player, "Added [" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "] to " + sc);
        }*/
        return InteractionResultHolder.pass(this.asItem().getDefaultInstance());
    }

    private SelectableTiles getController(Level world, ItemStack is) {
        CompoundTag nbt = is.getTag();
        if (nbt == null)
            return null;
        int[] xyz = nbt.getIntArray("locID");
        BlockEntity te = world.getBlockEntity(new BlockPos(xyz[0], xyz[1], xyz[2]));
        if (te instanceof SelectableTiles) {
            RotaryCraft.LOGGER.debug("Read tile " + te + " at " + Arrays.toString(xyz));
            return (SelectableTiles) te;
        }
        return null;
    }

    private void setID(ItemStack is, int[] id) {
        CompoundTag nbt = is.getTag();
        if (nbt == null)
            is.save(new CompoundTag());
        is.getTag().putIntArray("locID", id);
        RotaryCraft.LOGGER.debug("Saved tile " + Arrays.toString(id));
    }

}
