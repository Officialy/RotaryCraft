//package reika.rotarycraft.items.tools.bedrock;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import reika.rotarycraft.base.ItemRotaryTool;
//
//@Strippable("com.cricketcraft.chisel.api.IChiselItem")
//public class ItemBedrockChisel extends ItemRotaryTool implements IChiselItem {
//
//    public ItemBedrockChisel(int index) {
//        super(index);
//    }
//
//    @Override
//    public boolean canOpenGui(Level Level, Player player, ItemStack chisel) {
//        return true;
//    }
//
//    @Override
//    public boolean onChisel(Level Level, ItemStack chisel, ICarvingVariation target) {
//        return false;
//    }
//
//    @Override
//    public boolean canChisel(Level Level, ItemStack chisel, ICarvingVariation target) {
//        return true;
//    }
//
//    @Override
//    public boolean canChiselBlock(Level Level, Player player, BlockPos pos, Block block) {
//        return true;
//    }
//
//    @Override
//    public boolean hasModes(ItemStack chisel) {
//        return false;
//    }
//
//}
