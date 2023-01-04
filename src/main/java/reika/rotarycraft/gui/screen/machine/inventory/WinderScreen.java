/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.gui.screen.machine.inventory;


import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import reika.dragonapi.instantiable.io.PacketTarget;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.GuiOneSlotScreen;
import reika.rotarycraft.blockentities.BlockEntityWinder;
import reika.rotarycraft.gui.container.machine.inventory.WinderContainer;
import reika.rotarycraft.registry.PacketRegistry;

public class WinderScreen extends GuiOneSlotScreen<BlockEntityWinder, WinderContainer> {
    private final BlockEntityWinder winder;
    //private Level level = ModLoader.getMinecraftInstance().theWorld;

    int x;
    int y;
    private boolean input;

    public WinderScreen(WinderContainer container, Inventory inventory, Component title) {
        super(inventory, container, title);
        winder = (BlockEntityWinder) inventory.player.level.getBlockEntity(container.tile.getBlockPos());
        imageWidth = 176;
        imageHeight = 166;
        this.inventory = inventory;
        input = winder.winding;
    }

    @Override
    public void init() {
        super.init();
        int var5 = (width - imageWidth) / 2;
        int var6 = (height - imageHeight) / 2;
        if (input)
            addRenderableWidget(new Button.Builder(Component.literal("Input Mode"), this::actionPerformed).size(var5 + imageWidth / 2 - 35, var6 + imageHeight / 2 - 26).pos(65, 20).build());
        else
            addRenderableWidget(new Button.Builder(Component.literal("Output Mode"), this::actionPerformed).size(var5 + imageWidth / 2 - 35, var6 + imageHeight / 2 - 26).pos(65, 20).build());
    }

    protected void actionPerformed(Button button) {
        ReikaPacketHelper.sendUpdatePacket(RotaryCraft.packetChannel, PacketRegistry.WINDERTOGGLE.ordinal(), winder, PacketTarget.server);
        input = !input;
        this.init();
    }
}
