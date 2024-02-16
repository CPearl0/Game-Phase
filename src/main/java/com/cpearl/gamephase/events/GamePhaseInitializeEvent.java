package com.cpearl.gamephase.events;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.Collection;

public class GamePhaseInitializeEvent extends PlayerEvent {
    private final Collection<String> phases;
    public GamePhaseInitializeEvent(Collection<String> phases, Player player) {
        super(player);
        this.phases = phases;
    }
    public GamePhaseInitializeEvent(Collection<String> phases) {
        this(phases, Minecraft.getInstance().player);
    }

    public Collection<String> getPhases() {
        return phases;
    }
}
