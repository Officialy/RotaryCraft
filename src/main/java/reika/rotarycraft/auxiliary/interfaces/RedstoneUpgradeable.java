package reika.rotarycraft.auxiliary.interfaces;


import reika.dragonapi.interfaces.blockentity.BreakAction;

public interface RedstoneUpgradeable extends UpgradeableMachine, BreakAction {

    void addRedstoneUpgrade();

    boolean hasRedstoneUpgrade();

    boolean hasRedstoneSignal();
}
