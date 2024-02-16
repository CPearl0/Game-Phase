package com.cpearl.gamephase.events;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GamePhaseSyncEvent extends PlayerEvent {
    private final List<String> phases;
    public GamePhaseSyncEvent(Collection<String> phases, Player player) {
        super(player);
        this.phases = new ArrayList<>(phases);
    }
    public GamePhaseSyncEvent(Collection<String> phases) {
        this(phases, Minecraft.getInstance().player);
    }

    public List<String> getPhases() {
        return phases;
    }
}
