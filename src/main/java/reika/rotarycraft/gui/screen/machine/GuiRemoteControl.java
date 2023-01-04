///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.gui.screen.machine;
//
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.entity.player.Player;
//import reika.rotarycraft.base.GuiNonPoweredMachine;
//import reika.rotarycraft.base.blockentity.RemoteControlMachine;
//import reika.rotarycraft.gui.container.Machine.ContainerRemoteControl;
//import reika.rotarycraft.registry.RotaryMenuTypes;
//
//public class GuiRemoteControl extends GuiNonPoweredMachine {
//
//    public GuiRemoteControl(int id, Inventory p5ep, RemoteControlMachine te) {
//        super(new ContainerRemoteControl(RotaryMenuTypes.REMOTE_CONTROL.get() ,id, p5ep, te), te);
//        ep = p5ep;
//    }
//
//    @Override
//    protected ResourceLocation getGuiTexture() {
//        return new ResourceLocation("cctvgui");
//    }
//}
