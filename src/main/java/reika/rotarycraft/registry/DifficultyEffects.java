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

import reika.dragonapi.DragonAPI;
import reika.dragonapi.auxiliary.EnumDifficulty;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.dragonapi.libraries.java.ReikaRandomHelper;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.RotaryCraft;

public enum DifficultyEffects {

    BEDROCKDUST(3, 2, 1),
    PIPECRAFT(32, 16, 8),
    PARTCRAFT(6, 3, 2),
    BELTCRAFT(16, 8, 2),
    COMPACTOR(2, 2, 1),
    SMALLERCRAFT(2, 2, 1),
    BONUSSTEEL(2D, 1D, 0.5D),
    BLASTCONSUME(1F, 0.25F, 0F),
    JETFAILURE(4500, 1800, 900),
    CONSUMEFRAC(0.03125F, 0.25F, 0.75F),
    PRODUCEFRAC(new int[]{1600, 3200}, new int[]{1000, 2200}, new int[]{400, 800}),
    BREAKCOIL(0.01D, 0.05D, 0.15D),
    FURNACEMELT(1800, 600, 150),
    CANOLA(new int[]{128, 280}, new int[]{64, 160}, new int[]{8, 64}),
    RAILGUNCRAFT(16, 8, 2),
    LUBEUSAGE(0.25F, 1F, 2.5F),
    JETINGESTFAIL(0.05F, 0.2F, 0.5F),
    FRACTIONTEAR(0F, 0F, 0.05F);

    private boolean isChance = false;
    private boolean isRandom = false;
    private int easyInt;
    private int mediumInt;
    private int hardInt;

    private int easyMinimum;
    private int mediumMinimum;
    private int hardMinimum;

    private int easyMaximum;
    private int mediumMaximum;
    private int hardMaximum;

    private float easyChance;
    private float mediumChance;
    private float hardChance;

    private double easyDouble;
    private double mediumDouble;
    private double hardDouble;


    DifficultyEffects(int easy, int med, int hard) {
        easyInt = easy;
        mediumInt = med;
        hardInt = hard;
    }

    DifficultyEffects(int[] easy, int[] med, int[] hard) {
        isRandom = true;

        easyMinimum = easy[0];
        mediumMinimum = med[0];
        hardMinimum = hard[0];

        easyMaximum = easy[1];
        mediumMaximum = med[1];
        hardMaximum = hard[1];
    }

    //chances (out of 1F)
    DifficultyEffects(float easy, float med, float hard) {
        isChance = true;

        easyChance = easy;
        mediumChance = med;
        hardChance = hard;
    }

    DifficultyEffects(double easy, double med, double hard) {
        easyDouble = easy;
        mediumDouble = med;
        hardDouble = hard;
    }

    public static EnumDifficulty getDifficulty() {
        return EnumDifficulty.getDifficulty(ConfigRegistry.DIFFICULTY.getValue());
    }

    public int getInt() {
        if (isRandom) {
            return switch (getDifficulty()) {
                case EASY -> easyMinimum + DragonAPI.rand.nextInt(1 + easyMaximum - easyMinimum);
                case MEDIUM -> mediumMinimum + DragonAPI.rand.nextInt(1 + mediumMaximum - mediumMinimum);
                case HARD -> hardMinimum + DragonAPI.rand.nextInt(1 + hardMaximum - hardMinimum);
                default -> mediumMinimum + DragonAPI.rand.nextInt(1 + mediumMaximum - mediumMinimum);
            };
        } else {
            return switch (getDifficulty()) {
                case EASY -> easyInt;
                case MEDIUM -> mediumInt;
                case HARD -> hardInt;
                default -> mediumInt;
            };
        }
    }

    public float getChance() {
        return switch (getDifficulty()) {
            case EASY -> easyChance;
            case MEDIUM -> mediumChance;
            case HARD -> hardChance;
            default -> mediumChance;
        };
    }

    public double getDouble() {
        return switch (getDifficulty()) {
            case EASY -> easyDouble;
            case MEDIUM -> mediumDouble;
            case HARD -> hardDouble;
            default -> mediumDouble;
        };
    }

    public boolean testChance() {
        if (!isChance) {
            RotaryCraft.LOGGER.error(this + " is not chance, but was called for it!");
            ReikaJavaLibrary.dumpStack();
            return false;
        }
        float chance = this.getChance();
        return ReikaRandomHelper.doWithChance(chance);
    }

    public int getMaxAmount() {
        if (isRandom) {
            return switch (getDifficulty()) {
                case EASY -> easyMaximum;
                case MEDIUM -> mediumMaximum;
                case HARD -> hardMaximum;
                default -> mediumMaximum;
            };
        } else {
            return switch (getDifficulty()) {
                case EASY -> easyInt;
                case MEDIUM -> mediumInt;
                case HARD -> hardInt;
                default -> mediumInt;
            };
        }
    }

    public int getAverageAmount() {
        if (isRandom) {
            return switch (getDifficulty()) {
                case EASY -> (easyMaximum + easyMinimum) / 2;
                case MEDIUM -> (mediumMaximum + mediumMinimum) / 2;
                case HARD -> (hardMaximum + hardMinimum) / 2;
                default -> (mediumMaximum + mediumMinimum) / 2;
            };
        } else {
            return switch (getDifficulty()) {
                case EASY -> easyInt;
                case MEDIUM -> mediumInt;
                case HARD -> hardInt;
                default -> mediumInt;
            };
        }
    }

}
