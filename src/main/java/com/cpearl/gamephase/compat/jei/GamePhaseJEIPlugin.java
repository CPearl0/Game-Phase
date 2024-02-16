package com.cpearl.gamephase.compat.jei;

import com.cpearl.gamephase.GamePhase;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class GamePhaseJEIPlugin implements IModPlugin {
    public static IJeiRuntime runtime;

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(GamePhase.MODID, "main");
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
    }
}
