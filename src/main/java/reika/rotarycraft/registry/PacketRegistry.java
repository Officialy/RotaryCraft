/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public enum PacketRegistry implements reika.dragonapi.interfaces.PacketId {

    BORER(0, 1),
    BORERDROPS(1),
    BORERTOGGLEALL(2),
    BORERRESET(3),
    BEVEL(4, 1),
    SPLITTERMODE(5, 1),
    SPAWNERTIMER(6, 1),
    DETECTOR(7, 1),
    HEATER(8, 1),
    CVTMODE(9),
    CVTRATIO(10, 1),
    CVTTARGET(11, 1),
    CVTREDSTONESTATE(12, 1),
    CANNONFIRINGVALS(13, 5),
    SONICPITCH(14),
    SONICVOLUME(15),
    FORCE(16, 1),
    CHEST(17, 1),
    COILSPEED(18, 1),
    COILTORQUE(19, 1),
    MUSICNOTE(20, 4),
    MUSICSAVE(21),
    MUSICREAD(22),
    MUSICDEMO(23),
    MUSICREST(24, 2),
    MUSICBKSP(25, 1),
    MUSICCLEARCH(26, 1),
    MUSICCLEAR(27),
    VACUUMXP(28),
    WINDERTOGGLE(29),
    PROJECTOR(30),
    CONTAINMENT(31, 1),
    ITEMCANNON(32, 4),
    MIRROR(33),
    SAFEPLAYER(34),
    ENGINEBACKFIRE(35),
    MUSICPARTICLE(36),
    MULTISIDE(37, 2),
    TERRAFORMER(38, 1),
    CONVERTERPOWER(39, 1),
    CONVERTERREDSTONE(40),
    FERTILIZER(41),
    GRAVELGUN(42),
    SLIDE(43),
    POWERBUS(44, 1),
    PARTICLES(45, 1),
    BLOWERWHITELIST(46),
    BLOWERMETA(47),
    BLOWERNBT(48),
    BLOWEROREDICT(49),
    DEFOLIATOR(50, 3),
    GPR(51, 1),
    CRAFTERCRAFT(52, 1),
    CRAFTERTHRESH(53, 2),
    CRAFTERMODE(54),
    POWERSYNC(55, 4),
    AFTERBURN(56, 1),
    CRAFTPATTERNMODE(57, 1),
    CRAFTPATTERNLIMIT(58, 1),
    FILTERSETTING(59),
    SPRINKLER(60, 4),
    BLASTLEAVEONE(61, 1),
    EMPEFFECT(62),
    SPARKLOC(63, 5),
    DISTRIBCLUTCH(64, 2),
    DISTRIBCLUTCHPOWER(65, 4),
    FRIDGEBREAK(66),
    ;

    private final int wireId;
    private static final java.util.Map<Integer, PacketRegistry> BY_ID = reika.dragonapi.interfaces.PacketId.index(values());
    @Override public int wireId() { return wireId; }



    public final int numInts;

    PacketRegistry(int id) { this(id, 0); }

    PacketRegistry(int id, int len) {
        wireId = id;
        numInts = len;
    }

    public static PacketRegistry getEnum(int index) {
        return reika.dragonapi.interfaces.PacketId.lookup(BY_ID, index);
    }

    public boolean isLongPacket() {
        return switch (this) {
            case SONICVOLUME, SONICPITCH -> true;
            default -> false;
        };
    }

    public BlockPos getCoordinate(CompoundTag NBT) {
        if (this == PacketRegistry.FILTERSETTING) {
            return new BlockPos(NBT.getIntOr("posX", 0), NBT.getIntOr("posY", 0), NBT.getIntOr("posZ", 0));
        }
        return null;
    }

}
