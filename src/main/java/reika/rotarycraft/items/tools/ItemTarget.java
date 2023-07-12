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
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.HitResult;
import reika.dragonapi.instantiable.data.immutable.DecimalPosition;
import reika.dragonapi.libraries.ReikaPlayerAPI;
import reika.dragonapi.libraries.io.ReikaChatHelper;
import reika.dragonapi.libraries.mathsci.ReikaVectorHelper;
import reika.rotarycraft.base.ItemRotaryTool;

public class ItemTarget extends ItemRotaryTool {

    public ItemTarget() {
        super(new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        HitResult mov = ReikaPlayerAPI.getLookedAtBlock(player, 512, false);
        for (float i = 0; i <= 512; i += 0.5) {
            DecimalPosition xyz = ReikaVectorHelper.getPlayerLookCoords(player, i);
            if (!xyz.isEmpty(level)) {
                mov = xyz.asMovingPosition(Direction.DOWN, player.getLookAngle());
                break;
            }
        }
        ReikaChatHelper.write(mov);
        if (mov != null) {
            int x = (int) mov.getLocation().x();
            int y = (int) mov.getLocation().y();
            int z = (int) mov.getLocation().z();
            ReikaChatHelper.writeBlockAtCoords(level, new BlockPos((int) player.position().x, (int) player.position().y, (int) player.position().z));
            int range = 16;
            for (int i = -range; i <= range; i++) {
                for (int j = -range; j <= range; j++) {
                    for (int k = -range; k <= range; k++) {
                        BlockEntity te = level.getBlockEntity(new BlockPos((int) player.getX() + i, (int) player.getY() + j, (int) player.getZ() + k));
//                        if (te instanceof BlockEntityLaunchCannon) {
//                            BlockEntityLaunchCannon tc = (BlockEntityLaunchCannon) te;
//                            if (tc.targetMode) {
//                                tc.target[0] = x; todo
//                                tc.target[1] = y;
//                                tc.target[2] = z;
//                            }
//                        }
                    }
                }
            }
        }
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

}
