package win.transgirls.playervisibility.types;

import static win.transgirls.playervisibility.PlayerVisibility.transparency;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.entity.Entity;

public class TransparentVertexConsumerProvider<E extends Entity> implements MultiBufferSource {
    private final MultiBufferSource parent;
    private final E entity;

    public TransparentVertexConsumerProvider(MultiBufferSource parent, E entity) {
        this.parent = parent;
        this.entity = entity;

        if (!transparency.containsKey(entity)) {
            transparency.put(entity, -1);
        }
    }

    @Override public VertexConsumer getBuffer(RenderType layer) {
        VertexConsumer original = parent.getBuffer(layer);
        return new TransparentVertexConsumer<>(original, entity);
    }
}
