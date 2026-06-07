package win.transgirls.playervisibility.mixin.entity;

import win.transgirls.playervisibility.PlayerVisibility;
import win.transgirls.playervisibility.config.ModConfig;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityRenderDispatcher.class, priority = 1001)
public abstract class EntityMixinv1215 {

    // Cancel shouldRender for hidden entities — prevents body, shadow, and nametag from rendering
    @Inject(method = "shouldRender(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/culling/Frustum;DDD)Z",
            at = @At("HEAD"), cancellable = true, require = 0)
    private <E extends Entity> void onShouldRender(E entity, Frustum frustum, double x, double y, double z,
                                                    CallbackInfoReturnable<Boolean> cir) {
        boolean isPlayer = entity instanceof Player;
        boolean shouldHide = (isPlayer && ModConfig.hidePlayers) || (!isPlayer && ModConfig.hideEntities);

        if (shouldHide && PlayerVisibility.shouldHideEntity(entity)) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
