package reika.rotarycraft.items;

import java.util.ArrayList;
import java.util.function.Consumer;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import reika.rotarycraft.base.blocks.BlockBasicMachine;

/** Bridges the machine's power/variant tooltip into the 26.3 item tooltip API. */
public final class MachineBlockItem extends BlockItem {
    public MachineBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
            Consumer<Component> lines, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, lines, flag);
        if (getBlock() instanceof BlockBasicMachine machine) {
            var extra = new ArrayList<Component>();
            machine.appendHoverText(stack, context, extra, flag);
            extra.forEach(lines);
        }
    }
}
