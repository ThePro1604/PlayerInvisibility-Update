package win.transgirls.playervisibility.compat;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.function.Consumer;

public class CommandHelper {
    public static void register(Consumer<CommandDispatcher<FabricClientCommandSource>> registrationCallback) {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, buildContext) -> {
            registrationCallback.accept(dispatcher);
        });
    }
}
