package win.transgirls.playervisibility.compat;

import net.minecraft.text.Text;

/**
 * Helper class to replace CrossFabric's VersionedText
 */
public class TextHelper {
    public static Text translatable(String key, Object... args) {
        return Text.translatable(key, args);
    }
    
    public static Text of(String text) {
        return Text.literal(text);
    }
}

