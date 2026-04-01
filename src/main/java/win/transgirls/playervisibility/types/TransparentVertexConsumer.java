package win.transgirls.playervisibility.types;

import static win.transgirls.playervisibility.PlayerVisibility.transparency;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.entity.Entity;

public class TransparentVertexConsumer<E extends Entity> implements VertexConsumer {
    private final VertexConsumer parent;
    private final E entity; // value is -1 if disabled, value ranges between 0 and 255 if not

    public TransparentVertexConsumer(VertexConsumer parent, E entity) {
        this.parent = parent;
        this.entity = entity;
    }

    @Override public VertexConsumer color(int red, int green, int blue, int alpha) {
        int finalAlpha = transparency.get(entity) == -1 ? alpha : transparency.get(entity);
        return parent.color(red, green, blue, finalAlpha);
    }

    @Override public VertexConsumer color(int argb) {
        int alpha = (argb >> 24) & 0xFF;
        int red = (argb >> 16) & 0xFF;
        int green = (argb >> 8) & 0xFF;
        int blue = argb & 0xFF;
        int finalAlpha = transparency.get(entity) == -1 ? alpha : transparency.get(entity);
        int finalArgb = (finalAlpha << 24) | (red << 16) | (green << 8) | blue;
        return parent.color(finalArgb);
    }

    // not modified
    @Override public VertexConsumer vertex(float x, float y, float z) {
        return parent.vertex(x, y, z);
    }
    @Override public VertexConsumer texture(float u, float v) {
        return parent.texture(u, v);
    }
    @Override public VertexConsumer overlay(int u, int v) {
        return parent.overlay(u, v);
    }
    @Override public VertexConsumer light(int u, int v) {
        return parent.light(u, v);
    }
    @Override public VertexConsumer normal(float x, float y, float z) {
        return parent.normal(x, y, z);
    }
    @Override public VertexConsumer lineWidth(float width) {
        return parent.lineWidth(width);
    }
}