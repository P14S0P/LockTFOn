package com.piasop.locktfon;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import com.piasop.locktfon.client.LockOnHandler;

@Mod(LockTFOnMod.MODID)
public class LockTFOnMod {
    public static final String MODID = "locktfon";
    public static final boolean DEBUG_MODE = true;

    public LockTFOnMod() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new LockOnHandler());
    }
}