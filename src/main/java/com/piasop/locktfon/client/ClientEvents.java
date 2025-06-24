package com.piasop.locktfon.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.piasop.locktfon.LockTFOnMod;

@Mod.EventBusSubscriber(modid = LockTFOnMod.MODID)
public class ClientEvents {
    private static final Minecraft mc = Minecraft.getInstance();

    @Mod.EventBusSubscriber(modid = LockTFOnMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void onRender(RenderLevelStageEvent event) {
            if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL) {
                LockOnHandler.updateCamera();
            }
        }

        @SubscribeEvent
        public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
            if (LockOnHandler.isTargetLocked()) {
                LockOnHandler.releaseLock();
                event.setCanceled(true);
            }
        }
    }
}