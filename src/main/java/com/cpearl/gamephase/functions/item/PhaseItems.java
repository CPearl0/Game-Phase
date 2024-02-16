package com.cpearl.gamephase.functions.item;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class PhaseItems {
    public static Map<String, Collection<ItemStack>> items = new HashMap<>();
    public static void addItemRestriction(String phase, ItemStack ...item) {
        if (items.containsKey(phase))
            Collections.addAll(items.get(phase), item);
        else
            items.put(phase, new HashSet<>(List.of(item)));
    }

    public static void addItemModRestriction(String phase, String ...mod) {
        var restrictItems = new ArrayList<ItemStack>();
        for (var modid : mod) {
            var id = modid.toLowerCase();
            ForgeRegistries.ITEMS.getEntries().forEach(entry -> {
                if (entry.getKey().location().getNamespace().equals(id))
                    restrictItems.add(new ItemStack(entry.getValue()));
            });
        }
        PhaseItems.addItemRestriction(phase, restrictItems.toArray(new ItemStack[0]));
    }
}
