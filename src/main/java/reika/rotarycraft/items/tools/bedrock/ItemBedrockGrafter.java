/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items.tools.bedrock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import reika.rotarycraft.base.ItemRotaryTool;

//@Strippable(value = {"forestry.api.arboriculture.IToolGrafter"})
public class ItemBedrockGrafter extends ItemRotaryTool {// implements IToolGrafter {

    public ItemBedrockGrafter(Properties properties) {
        super(properties);
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, Level level, Player player) {
        //RotaryAchievements.BEDROCKTOOLS.triggerAchievement(player);
    }

    //@Override
    public float getSaplingModifier(ItemStack stack, Level Level, Player player, BlockPos pos) {
        return 100;
    }

    @Override
    public float getDestroySpeed(ItemStack is, BlockState b) {
        if (b == null)
            return 0;
//        if (ForestryHandler.BlockEntry.LEAF.getBlock() == b)
//            return 30;
        return super.getDestroySpeed(is, b);
    }

//
//    @Override
//    @ModDependent(ModList.FORESTRY)
//    public boolean onBlockStartBreak(ItemStack is, BlockPos pos, Player ep) {
//        if (RotaryConfig.COMMON.FAKEBEDROCK.get() || !ReikaPlayerAPI.isFake(ep)) {
//            Level level = ep.getLevel();
//            BlockState b = level.getBlockState(pos);
//            if (ForestryHandler.BlockEntry.LEAF.getBlock() == b) {
//                //ITree src = TreeManager.treeRoot.getTree(Level, x, y, z); //only works on saplings
//                BlockEntity te = level.getBlockEntity(pos);
//                ITree src = ReikaBeeHelper.getTree(te);
//                if (src != null) {
//                    int r = ep.isCrouching() ? 0 : 4;
//                    for (int i = -r; i <= r; i++) {
//                        for (int j = -r; j <= r; j++) {
//                            for (int k = -r; k <= r; k++) {
//                                int dx = pos.getX() + i;
//                                int dy = pos.getY() + j;
//                                int dz = pos.getZ() + k;
//                                BlockState b2 = level.getBlockState(new BlockPos(dx, dy, dz));
//                                if (b2 == b) {
//                                    //ITree other = TreeManager.treeRoot.getTree(Level, dx, dy, dz);
//                                    //if (other != null && src.isGeneticEqual(other)) {
//                                    BlockEntity te2 = level.getBlockEntity(new BlockPos(dx, dy, dz));
//                                    ITree other = ReikaBeeHelper.getTree(te2);
//                                    if (other != null && other.getGenome().getPrimary().getUID().equals(src.getGenome().getPrimary().getUID())) {
//                                        b2.onBlockHarvested(level, dx, dy, dz, ep);
//                                        b2.dropBlockAsItem(level, dx, dy, dz, 1);
//                                        b2.removedByPlayer(level, ep, dx, dy, dz, false);
//                                        ReikaSoundHelper.playBreakSound(level, dx, dy, dz, b2);
//                                        ReikaPacketHelper.sendDataPacketWithRadius(DragonAPI.packetChannel, PacketIDs.BREAKPARTICLES.ordinal(), level, dx, dy, dz, 24, Block.getId(b2));
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    //return true;
//                }
//            }
//        }
//        return false;
//    }

}
