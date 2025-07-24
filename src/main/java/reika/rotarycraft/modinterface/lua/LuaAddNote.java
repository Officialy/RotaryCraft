///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.modinterface.Lua;
//
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.neoforged.event.level.NoteBlockEvent;
//import reika.dragonapi.libraries.mathsci.ReikaMusicHelper;
//import reika.dragonapi.modinteract.lua.LuaMethod;
//
//public class LuaAddNote extends LuaMethod {
//
//	public LuaAddNote() {
//		super("addNote", BlockEntityMusicBox.class);
//	}
//
//	@Override
//	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
//		BlockEntityMusicBox mus = (BlockEntityMusicBox) te;
//		int pitch = ((Double)args[0]).intValue();
//		int channel = ((Double)args[1]).intValue();
//		int length = ((Double)args[2]).intValue();
//		int voice = ((Double)args[3]).intValue();
//		ReikaMusicHelper.Note n = new NoteBlockEvent.Note(NoteLength.values()[length], pitch, Instrument.values()[voice]);
//		for (int i = 0; i < 3; i++)
//			n.play(mus);
//		mus.addNote(channel, n);
//		return null;
//	}
//
//	@Override
//	public String getDocumentation() {
//		return "Adds a note to the music box.\nArgs: Pitch (0-63), Channel (0-15), Length (0-4), Instrument (0-5)\nReturns: Nothing";
//	}
//
//	@Override
//	public String getArgsAsString() {
//		return "int pitch, int channel, int length, int voice";
//	}
//
//	@Override
//	public ReturnType getReturnType() {
//		return ReturnType.VOID;
//	}
//
//}
