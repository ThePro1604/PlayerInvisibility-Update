package win.transgirls.playervisibility.compat;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;

import java.util.function.Consumer;

/**
 * Helper class to replace CrossFabric's ClientCommandManager
 */
public class CommandHelper {
    public static void register(Consumer<CommandDispatcher<FabricClientCommandSource>> registrationCallback) {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            registrationCallback.accept(dispatcher);
        });
    }
}

