package com.piasop.locktfon.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Mth;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import com.piasop.locktfon.LockTFOnMod;

import javax.annotation.Nullable;

public class LockOnHandler {
    private static final Minecraft mc = Minecraft.getInstance();
    private static final float MAX_DISTANCE = 12.0f;
    private static final float LERP_SPEED = 0.2f;
    
    @Nullable
    private static Entity lockedTarget = null;

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (event.getEntity() instanceof Player && 
            event.getTarget() instanceof LivingEntity target &&
            !(target instanceof AbstractVillager)) {
            lockedTarget = target;
            debugLog("Locked target: " + target.getName().getString());
        }
    }

    @SubscribeEvent
    public void onTargetDeath(LivingDeathEvent event) {
        if (lockedTarget != null && event.getEntity().equals(lockedTarget)) {
            releaseLock();
        }
    }

    public static void updateCamera() {
        Player player = mc.player;
        Entity target = lockedTarget;
        
        if (player == null || target == null || !target.isAlive()) {
            releaseLock();
            return;
        }

        if (player.distanceTo(target) > MAX_DISTANCE) {
            releaseLock();
            return;
        }

        try {
            double dx = target.getX() - player.getX();
            double dz = target.getZ() - player.getZ();
            double dy = (target.getEyeY() - player.getEyeY()) * 0.7;

            float targetYaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90;
            float targetPitch = (float) -Math.toDegrees(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)));

            player.setYRot(lerpAngle(player.getYRot(), targetYaw, LERP_SPEED));
            player.setXRot(lerpAngle(player.getXRot(), targetPitch, LERP_SPEED));
        } catch (Exception e) {
            debugLog("Camera error: " + e.getMessage());
            releaseLock();
        }
    }

    private static float lerpAngle(float current, float target, float speed) {
        return current + (Mth.wrapDegrees(target - current) * speed);
    }

    public static void releaseLock() {
        if (lockedTarget != null) {
            debugLog("Released target: " + lockedTarget.getName().getString());
            lockedTarget = null;
        }
    }

    public static boolean isTargetLocked() {
        return lockedTarget != null;
    }

    private static void debugLog(String message) {
        if (LockTFOnMod.DEBUG_MODE) {
            System.out.println("[LockTFOn] " + message);
        }
    }
}