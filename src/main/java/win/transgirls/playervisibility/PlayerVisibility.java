package win.transgirls.playervisibility;

import win.transgirls.playervisibility.commands.VisibilityCommand;
import win.transgirls.playervisibility.compat.CommandHelper;
import win.transgirls.playervisibility.compat.TextHelper;
import win.transgirls.playervisibility.config.ModConfig;
import win.transgirls.playervisibility.types.FilterType;
import win.transgirls.playervisibility.types.MessageType;
import win.transgirls.playervisibility.util.ArrayListUtil;
import win.transgirls.playervisibility.util.ConfigUtil;
import static win.transgirls.playervisibility.PlayerVisibilityClient.LOGGER;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.lang.reflect.Field;
import java.util.HashMap;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

import org.lwjgl.glfw.GLFW;

public class PlayerVisibility implements ClientModInitializer {
    public static Minecraft minecraftClient;
    public static boolean debugKey = false;

    private static boolean filterEnabled = true;
    private static KeyMapping toggleFilter;

    public static HashMap<Entity, Integer> transparency = new HashMap<>();

    @Override public void onInitializeClient() {
        ConfigUtil.init();

        minecraftClient = Minecraft.getInstance();

        toggleFilter = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.player-visibility.toggle",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                new KeyMapping.Category(Identifier.fromNamespaceAndPath("player-visibility", "main"))
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            debugKey = toggleFilter.isDown();
            while (toggleFilter.consumeClick()) {
                toggleFilter();
            }
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            ConfigUtil.save();
        });

        CommandHelper.register((dispatcher) -> {
            try {
                PlayerVisibility.registerCommands(dispatcher);
            } catch (ClassCastException e) {
                LOGGER.error("Couldn't cast dispatcher as command source", e);
            }
        });

        LOGGER.info("Player Visibility has initialized successfully :3");
    }

    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        VisibilityCommand.register(dispatcher);
    }

    public static void toggleFilter() {
        filterEnabled = !filterEnabled;
        String VisibleString = "§c" + (TextHelper.translatable("text.player-visibility.message.off")).getString();
        if (filterEnabled) {
            VisibleString = "§" + ModConfig.mainColor.getChar() + (TextHelper.translatable("text.player-visibility.message.on")).getString();
        }
        sendMessage((TextHelper.translatable("text.player-visibility.message.visibility-toggle", ModConfig.mainColor.getChar(), VisibleString)));
    }

    public static boolean isVisibilityEnabled() {
        return filterEnabled;
    }

    public static boolean shouldHideEntityRenderState(Object entity) {
        if (isVisibilityEnabled()) {
            return false;
        }

        try {
            if (entity instanceof HumanoidRenderState humanoidState) {
                String playerUsername = null;
                if (humanoidState.nameTag != null) {
                    playerUsername = humanoidState.nameTag.getString();
                }

                if (playerUsername != null) {
                    Minecraft mc = Minecraft.getInstance();
                    if (mc.player != null && playerUsername.equalsIgnoreCase(mc.player.getName().getString())) {
                        return ModConfig.hideSelf;
                    }

                    boolean isInFilterList = ArrayListUtil.containsLowercase(ModConfig.getFilter(), playerUsername);

                    if (ModConfig.filterType == FilterType.BLACKLIST) {
                        return isInFilterList;
                    } else {
                        return !isInFilterList;
                    }
                } else {
                    return true;
                }
            }

            if (entity instanceof LivingEntityRenderState) {
                return true;
            }
        } catch (Throwable ignored) {
        }

        return true;
    }

    public static <E extends Entity> boolean shouldHideEntity(E entity) {
        if (isVisibilityEnabled()) {
            return false;
        }

        if (entity instanceof Mob) {
            return true;
        }

        if (entity instanceof Player) {
            String playerUsername = entity.getName().getString();

            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && playerUsername.equalsIgnoreCase(mc.player.getName().getString())) {
                return ModConfig.hideSelf;
            }

            boolean isInFilterList = ArrayListUtil.containsLowercase(ModConfig.getFilter(), playerUsername);

            if (ModConfig.filterType == FilterType.BLACKLIST) {
                return isInFilterList;
            } else {
                return !isInFilterList;
            }
        }

        return true;
    }

    public static void sendMessage(Object text) {
        if (text instanceof Component component) {
            sendMessage(component.getString());
            return;
        }
        if (text instanceof String str) {
            sendMessage(str);
        }
    }

    public static void sendMessage(String message) {
        LOGGER.info(message);
        if (minecraftClient.player == null) {
            return;
        }

        String messagePrefix = "§fᴘʟᴀʏᴇʀ ᴠɪsɪʙɪʟɪᴛʏ" + "§f" + " » ";

        if (ModConfig.messageType == MessageType.CHAT_MESSAGE) {
            minecraftClient.player.sendSystemMessage(TextHelper.of(messagePrefix + message));
        }
        if (ModConfig.messageType == MessageType.ACTION_BAR) {
            // In 26.1, action bar messages are shown via sendSystemMessage with overlay
            minecraftClient.player.sendSystemMessage(TextHelper.of(message));
        }
    }
}
