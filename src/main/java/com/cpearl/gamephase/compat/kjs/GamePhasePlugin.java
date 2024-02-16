package com.cpearl.gamephase.compat.kjs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;

public class GamePhasePlugin extends KubeJSPlugin {
    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("GamePhase", new GamePhaseKubeJSBindings());
    }
}
