package win.transgirls.playervisibility.types;

import static win.transgirls.playervisibility.PlayerVisibility.transparency;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.entity.Entity;

public class TransparentVertexConsumer<E extends Entity> implements VertexConsumer {
    private final VertexConsumer parent;
    private final E entity;

    public TransparentVertexConsumer(VertexConsumer parent, E entity) {
        this.parent = parent;
        this.entity = entity;
    }

    @Override public VertexConsumer setColor(int red, int green, int blue, int alpha) {
        int finalAlpha = transparency.get(entity) == -1 ? alpha : transparency.get(entity);
        return parent.setColor(red, green, blue, finalAlpha);
    }

    @Override public VertexConsumer setColor(int argb) {
        int alpha = (argb >> 24) & 0xFF;
        int red = (argb >> 16) & 0xFF;
        int green = (argb >> 8) & 0xFF;
        int blue = argb & 0xFF;
        int finalAlpha = transparency.get(entity) == -1 ? alpha : transparency.get(entity);
        int finalArgb = (finalAlpha << 24) | (red << 16) | (green << 8) | blue;
        return parent.setColor(finalArgb);
    }

    @Override public VertexConsumer addVertex(float x, float y, float z) {
        return parent.addVertex(x, y, z);
    }
    @Override public VertexConsumer setUv(float u, float v) {
        return parent.setUv(u, v);
    }
    @Override public VertexConsumer setUv1(int u, int v) {
        return parent.setUv1(u, v);
    }
    @Override public VertexConsumer setUv2(int u, int v) {
        return parent.setUv2(u, v);
    }
    @Override public VertexConsumer setNormal(float x, float y, float z) {
        return parent.setNormal(x, y, z);
    }
    @Override public VertexConsumer setLineWidth(float width) {
        return parent.setLineWidth(width);
    }
}
