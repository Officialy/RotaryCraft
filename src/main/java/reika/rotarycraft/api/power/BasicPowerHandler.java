package reika.rotarycraft.api.power;

import net.minecraft.nbt.CompoundTag;

public final class BasicPowerHandler {

    private int torque;
    private int omega;
    private long power;
    private int iotick;

    public void setOmega(int omega) {
        this.omega = omega;
    }

    public void setTorque(int torque) {
        this.torque = torque;
    }

    public void setPower(long power) {
        this.power = power;
    }

    public void noInputMachine() {
        omega = torque = 0;
        power = 0;
    }

    public int getOmega() {
        return omega;
    }

    public int getTorque() {
        return torque;
    }

    public long getPower() {
        return power;
    }

    public int getIORenderAlpha() {
        return iotick;
    }

    public void setIORenderAlpha(int io) {
        iotick = io;
    }

    public void decrementIOTick(int amt) {
        iotick = Math.max(0, iotick - amt);
    }

    public void load(CompoundTag NBT) {
        omega = NBT.getInt("omg");
        torque = NBT.getInt("tq");
        power = NBT.getLong("pwr");
    }

    public void saveAdditional(CompoundTag NBT) {
        NBT.putInt("omg", omega);
        NBT.putInt("tq", torque);
        NBT.putLong("pwr", power);
    }

}
