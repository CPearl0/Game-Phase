package com.cpearl.gamephase.functions.dimension;

import net.minecraft.resources.ResourceLocation;

import java.util.*;


public class PhaseDimensions {
    public static Map<String, Collection<ResourceLocation>> dimensions = new HashMap<>();
    public static void addDimensionRestriction(String phase, ResourceLocation ...dimension) {
        if (dimensions.containsKey(phase))
            Collections.addAll(dimensions.get(phase), dimension);
        else
            dimensions.put(phase, new HashSet<>(List.of(dimension)));
    }
}
