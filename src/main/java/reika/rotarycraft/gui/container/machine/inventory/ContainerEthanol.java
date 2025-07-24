/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.gui.container.machine.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.common.capabilities.ForgeCapabilities;
import net.neoforged.items.SlotItemHandler;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.IOMachineContainer;
import reika.rotarycraft.blockentities.engine.BlockEntityGasEngine;
import reika.rotarycraft.blockentities.processing.BlockEntityGrinder;
import reika.rotarycraft.registry.RotaryItems;
import reika.rotarycraft.registry.RotaryMenus;

public class ContainerEthanol extends IOMachineContainer<BlockEntityGasEngine> {
    private final BlockEntityGasEngine engine;

    //Client
    public ContainerEthanol(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityGasEngine) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ContainerEthanol(final int id, Inventory player, BlockEntityGasEngine engine) {
        super(RotaryMenus.GAS_ENGINE.get(), id, player, engine);
        this.engine = engine;
        this.engine.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(itemHandler -> {
            this.addSlot(new SlotItemHandler(itemHandler, 0, 61, 36) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.getItem() == RotaryItems.ETHANOL.get();
                }
            });
        });
        this.addPlayerInventory(player);
    }

    /**
     * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
     */
    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, engine, "fuel");
    }

}
