package reika.rotarycraft.auxiliary.interfaces;


import reika.dragonapi.interfaces.blockentity.ThermalTile;

public interface HeatConduction extends ThermalTile {

    boolean canBeCooledWithFins();

    boolean allowExternalHeating();

    boolean allowHeatExtraction();

    double heatEnergyPerDegree();

    int getAmbientTemperature();

}
