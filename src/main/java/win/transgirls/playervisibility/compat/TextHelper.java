package win.transgirls.playervisibility.compat;

import net.minecraft.network.chat.Component;

public class TextHelper {
    public static Component translatable(String key, Object... args) {
        return Component.translatable(key, args);
    }

    public static Component of(String text) {
        return Component.literal(text);
    }
}
