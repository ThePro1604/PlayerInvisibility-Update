package win.transgirls.playervisibility.mixin.shadow;

import win.transgirls.playervisibility.PlayerVisibility;
import win.transgirls.playervisibility.config.ModConfig;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityRenderer.class, priority = 1001)
public class ShadowMixinv1215 {
    // Hide shadow by clearing the shadow pieces list when rendering hidden entities
    @Inject(method = "updateShadow(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/render/entity/state/EntityRenderState;)V", at = @At("HEAD"), cancellable = true)
    private void injectShadow(Object entity, EntityRenderState renderState, CallbackInfo ci) {
        if (ModConfig.hideShadows && PlayerVisibility.shouldHideEntityRenderState(renderState)) {
            // Clear shadow by preventing shadow update
            ci.cancel();
        }
    }
}