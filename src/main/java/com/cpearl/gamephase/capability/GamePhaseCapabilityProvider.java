package com.cpearl.gamephase.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(modid = "gamephase")
public class GamePhaseCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
    public static Capability<IGamePhaseCapability> GAME_PHASE = CapabilityManager.get(new CapabilityToken<IGamePhaseCapability>() {});
    private IGamePhaseCapability gamePhaseCapability = null;
    private final LazyOptional<IGamePhaseCapability> gamePhaseCapabilityLazyOptional = LazyOptional.of(this::createGamePhaseCapability);
    private IGamePhaseCapability createGamePhaseCapability() {
        if (gamePhaseCapability == null)
            gamePhaseCapability = new GamePhaseCapability();
        return gamePhaseCapability;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return GAME_PHASE.orEmpty(cap, gamePhaseCapabilityLazyOptional);
    }

    @Override
    public CompoundTag serializeNBT() {
        var capability = (GamePhaseCapability) createGamePhaseCapability();
        return capability.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        var capability = (GamePhaseCapability) createGamePhaseCapability();
        capability.deserializeNBT(nbt);
    }
}
