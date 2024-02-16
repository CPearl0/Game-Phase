package com.cpearl.gamephase.capability;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.Collection;

@AutoRegisterCapability
public interface IGamePhaseCapability {
    void addPhase(String phase);
    void removePhase(String phase);
    void clearPhase();
    boolean hasPhase(String phase);
    Collection<String> getPhases();
    void setPhases(Collection<String> phases);
}
