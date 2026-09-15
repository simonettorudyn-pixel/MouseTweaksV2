package com.mousetweaksv2;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class MouseTweaksV2 implements ClientModInitializer {

    public static boolean aimAssistEnabled = false;
    public static boolean triggerbotEnabled = false;
    public static List<Integer> validTriggerSlots = new ArrayList<>(); 
    public static List<String> alliesList = new ArrayList<>();

    private static KeyBinding keyToggleAim;
    private static KeyBinding keyToggleTrigger;
    private static KeyBinding keyOpenGui;

    @Override
    public void onInitializeClient() {
        for (int i = 0; i < 9; i++) {
            validTriggerSlots.add(i);
        }

        keyToggleAim = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mousetweaksv2.toggleaim", 
                InputUtil.Type.KEYSYM, 
                GLFW.GLFW_KEY_V, 
                "category.mousetweaksv2.general"
        ));

        keyToggleTrigger = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mousetweaksv2.toggletrigger", 
                InputUtil.Type.KEYSYM, 
                GLFW.GLFW_KEY_B, 
                "category.mousetweaksv2.general"
        ));

        keyOpenGui = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mousetweaksv2.opengui", 
                InputUtil.Type.KEYSYM, 
                GLFW.GLFW_KEY_RIGHT_SHIFT, 
                "category.mousetweaksv2.general"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) return;

            while (keyToggleAim.wasPressed()) {
                aimAssistEnabled = !aimAssistEnabled;
                client.player.sendMessage(Text.literal("§7[§9MouseTweaksV2§7] Aim Assist: " + (aimAssistEnabled ? "§aON" : "§cOFF")), true);
            }

            while (keyToggleTrigger.wasPressed()) {
                triggerbotEnabled = !triggerbotEnabled;
                client.player.sendMessage(Text.literal("§7[§9MouseTweaksV2§7] Triggerbot: " + (triggerbotEnabled ? "§aON" : "§cOFF")), true);
            }

            while (keyOpenGui.wasPressed()) {
                client.setScreen(new ClickGuiScreen());
            }

            if (aimAssistEnabled) {
                runAimAssist(client);
            }

            if (triggerbotEnabled) {
                runTriggerbot(client);
            }
        });
    }

    private void runAimAssist(MinecraftClient client) {
        PlayerEntity player = client.player;
        Entity closestTarget = null;
        double closestDistance = 4.0;

        for (Entity entity : client.world.getEntities()) {
            if (entity instanceof PlayerEntity && entity != player) {
                String targetName = entity.getName().getString();
                if (alliesList.contains(targetName)) continue;

                double distance = player.distanceTo(entity);
                if (distance = 1.0f) {
                    client.interactionManager.attackEntity(client.player, target);
                    client.player.swingHand(Hand.MAIN_HAND);
                }
            }
        }
    }
}

