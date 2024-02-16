package com.cpearl.gamephase.events;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

import java.util.Collection;

@Cancelable
public class GamePhaseUpdateEvent extends PlayerEvent {
    private final Collection<String> phasesOld, phasesNew;
    public GamePhaseUpdateEvent(Collection<String> phasesOld, Collection<String> phasesNew, Player player) {
        super(player);
        this.phasesOld = phasesOld;
        this.phasesNew = phasesNew;
    }

    public Collection<String> getPhasesOld() {
        return phasesOld;
    }

    public Collection<String> getPhasesNew() {
        return phasesNew;
    }
}
