package win.transgirls.playervisibility.mixin.label;

import win.transgirls.playervisibility.PlayerVisibility;
import win.transgirls.playervisibility.config.ModConfig;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityRenderer.class, priority = 1001)
public class LabelMixinv1212 {
    // shouldShowName replaces hasLabel in 26.1
    @Inject(method = "shouldShowName(Lnet/minecraft/world/entity/Entity;D)Z",
            at = @At("HEAD"), cancellable = true, require = 0)
    private void injectShouldShowName(Entity entity, double squaredDistanceToCamera,
                                       CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.hideNametags && PlayerVisibility.shouldHideEntity(entity)) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
