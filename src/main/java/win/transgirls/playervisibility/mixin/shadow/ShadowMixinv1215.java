package win.transgirls.playervisibility.mixin.shadow;

import win.transgirls.playervisibility.PlayerVisibility;
import win.transgirls.playervisibility.config.ModConfig;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityRenderer.class, priority = 1001)
public class ShadowMixinv1215 {
    // Return shadow radius 0 to suppress shadow for hidden entities
    @Inject(method = "getShadowRadius(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;)F",
            at = @At("HEAD"), cancellable = true, require = 0)
    private void injectGetShadowRadius(EntityRenderState renderState, CallbackInfoReturnable<Float> cir) {
        if (ModConfig.hideShadows && PlayerVisibility.shouldHideEntityRenderState(renderState)) {
            cir.setReturnValue(0.0f);
            cir.cancel();
        }
    }
}
