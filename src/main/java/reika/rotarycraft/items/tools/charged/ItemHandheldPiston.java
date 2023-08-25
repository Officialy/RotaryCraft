/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items.tools.charged;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.instantiable.data.immutable.BlockKey;
import reika.dragonapi.libraries.ReikaPlayerAPI;
import reika.dragonapi.libraries.io.ReikaChatHelper;
import reika.dragonapi.libraries.level.ReikaBlockHelper;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.rotarycraft.base.ItemChargedTool;

public class ItemHandheldPiston extends ItemChargedTool {

    public ItemHandheldPiston() {

    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player ep, InteractionHand hand) {
        int side = 0;
//        if (this.getDamage(ep.getItemInHand(hand)) <= 0){
//            ReikaChatHelper.write("fail");
//            return InteractionResultHolder.fail(this.getDefaultInstance());
//    }
        if (!world.isClientSide) {
            var pos = ReikaPlayerAPI.getLookedAtBlock(ep, ep.getEntityReach(), false).getBlockPos();
            ReikaChatHelper.write("moving block");
            ReikaChatHelper.writeBlockAtCoords(world, pos);
            BlockEntity te = world.getBlockEntity(ep.blockPosition());
            if (te != null)
                return InteractionResultHolder.fail(this.getDefaultInstance());
            Direction dir = Direction.values()[side].getOpposite();
            int power = 0;
            if (ep.isShiftKeyDown()) {
                int d = 12;
                for (int i = 1; i <= 13; i++) {
                    int dx = pos.getX() + dir.getStepX() * i;
                    int dy = pos.getY() + dir.getStepY() * i;
                    int dz = pos.getZ() + dir.getStepZ() * i;
                    if (i == 13) {
                        if (!ReikaWorldHelper.softBlocks(world, new BlockPos(dx, dy, dz))) {
                            ReikaChatHelper.write("fail1");
                            return InteractionResultHolder.fail(this.getDefaultInstance());
                        }
                    }
                    if (dy < 0 || dy >= 256) {
                        ReikaChatHelper.write("fail2");
                        return InteractionResultHolder.fail(this.getDefaultInstance());
                    }
                    Block bk = world.getBlockState(new BlockPos(dx, dy, dz)).getBlock();

                    if (ReikaBlockHelper.isUnbreakable(world, new BlockPos(dx, dy, dz), bk, ep))
                        return InteractionResultHolder.fail(this.getDefaultInstance());

                    te = world.getBlockEntity(new BlockPos(dx, dy, dz));
                    if (te != null) {
                        ReikaChatHelper.write("fail3");
                        return InteractionResultHolder.fail(this.getDefaultInstance());
                    }
                    /*int amt = bk.defaultBlockState().getMaterial().getMaterialMobility() == 2 ? 8 : 2;
                    power += amt;*/
                    if (ReikaWorldHelper.softBlocks(world, new BlockPos(dx, dy, dz)) || power >= ep.getItemInHand(hand).getDamageValue()) {
                        d = i - 1;
                        break;
                    }
                }
                for (int i = d; i >= 0; i--) {
                    int dx1 = pos.getX() + dir.getStepX() * i;
                    int dy1 = pos.getY() + dir.getStepY() * i;
                    int dz1 = pos.getZ() + dir.getStepZ() * i;
                    int dx2 = pos.getX() + dir.getStepX() * (i + 1);
                    int dy2 = pos.getY() + dir.getStepY() * (i + 1);
                    int dz2 = pos.getZ() + dir.getStepZ() * (i + 1);
                    BlockKey bk = BlockKey.getAt(world, new BlockPos(dx1, dy1, dz1));
                    bk.place(world, new BlockPos(dx2, dy2, dz2));
                }
            } else {
                int dx = pos.getX() + dir.getStepX();
                int dy = pos.getY() + dir.getStepY();
                int dz = pos.getZ() + dir.getStepZ();
                if (!ReikaWorldHelper.softBlocks(world, new BlockPos(dx, dy, dz))) {
                    ReikaChatHelper.write("fail4");
                    return InteractionResultHolder.fail(this.getDefaultInstance());
                }
                BlockKey bk = BlockKey.getAt(world, pos);
                bk.place(world, new BlockPos(dx, dy, dz));
                power = 1;
            }
            ep.setItemInHand(hand, new ItemStack(this, this.getDefaultInstance().getCount(), new CompoundTag()));
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 0);
        }
//        ReikaSoundHelper.playSoundAtBlock(world, pos, "tile.piston.out");
        ep.playSound(SoundEvents.PISTON_EXTEND, 1, 1);

        return InteractionResultHolder.pass(this.getDefaultInstance());
    }


}
