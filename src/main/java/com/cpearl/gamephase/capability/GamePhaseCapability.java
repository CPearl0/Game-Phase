package com.cpearl.gamephase.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AutoRegisterCapability
public class GamePhaseCapability implements IGamePhaseCapability {
    private Set<String> phases = new HashSet<>();
    @Override
    public void addPhase(String phase) {
        phases.add(phase);
    }

    @Override
    public void removePhase(String phase) {
        phases.remove(phase);
    }

    @Override
    public void clearPhase() {
        phases.clear();
    }

    @Override
    public boolean hasPhase(String phase) {
        return phases.contains(phase);
    }
    @Override
    public Collection<String> getPhases() {
        return phases;
    }
    @Override
    public void setPhases(Collection<String> phases) {
        this.phases = new HashSet<>(phases);
    }
    @Override
    public GamePhaseCapability clone() throws CloneNotSupportedException {
        GamePhaseCapability clone = (GamePhaseCapability) super.clone();
        clone.setPhases(this.phases);
        return clone;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag listPhases = new ListTag();
        for (var phase: phases) {
            listPhases.add(StringTag.valueOf(phase));
        }
        tag.put("Phases", listPhases);
        return tag;
    }

    public void deserializeNBT(CompoundTag nbt) {
        phases = new HashSet<>();
        var listPhases = nbt.getList("Phases", Tag.TAG_STRING);
        for (int i = 0; i < listPhases.size(); i++) {
            addPhase(listPhases.getString(i));
        }
    }
}
