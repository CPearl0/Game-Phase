package com.cpearl.gamephase;

import com.cpearl.gamephase.network.GamePhasePacketHandler;
import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(GamePhase.MODID)
public class GamePhase
{
    public static final String MODID = "gamephase";
    public static final Logger LOGGER = LogUtils.getLogger();

    static {
        GamePhasePacketHandler.init();
    }
}
