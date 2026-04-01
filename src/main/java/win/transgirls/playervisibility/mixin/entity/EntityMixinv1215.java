package win.transgirls.playervisibility.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import win.transgirls.playervisibility.PlayerVisibility;
import win.transgirls.playervisibility.config.ModConfig;
import win.transgirls.playervisibility.types.TransparentVertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderManager;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static win.transgirls.playervisibility.PlayerVisibility.transparency;

@Mixin(value = EntityRenderManager.class, priority = 1001)
public abstract class EntityMixinv1215 {
    @Shadow public abstract double getSquaredDistanceToCamera(Entity entity);

    // Cancel rendering for hidden entities
    @Inject(method = "render(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/render/state/CameraRenderState;DDDLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;)V", at = @At("HEAD"), cancellable = true)
    private <S extends EntityRenderState> void onRender(S renderState, CameraRenderState cameraState, double offsetX, double offsetY, double offsetZ, MatrixStack matrices, OrderedRenderCommandQueue queue, CallbackInfo ci) {
        // Check if this is a player
        boolean isPlayer = renderState instanceof PlayerEntityRenderState;

        boolean shouldHide = (isPlayer && ModConfig.hidePlayers) || (!isPlayer && ModConfig.hideEntities);

        if (shouldHide && PlayerVisibility.shouldHideEntityRenderState(renderState)) {
            ci.cancel();
        }
    }
}
