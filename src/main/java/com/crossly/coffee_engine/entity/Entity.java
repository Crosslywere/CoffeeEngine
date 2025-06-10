package com.crossly.coffee_engine.entity;

import com.crossly.coffee_engine.component.Component;
import com.crossly.coffee_engine.component.Transform;

import java.util.*;

public class Entity {

    private final Map<Class<? extends Component>, Component> components = new HashMap<>();
    private Entity parent = null;
    private final List<Entity> children = new ArrayList<>();
    private UpdateCallback updateCallback = null;
    private RenderCallback renderCallback = null;

    public Entity() {
    }

    public Entity(Component... components) {
        for (var component : components)
            addComponent(component);
    }

    public final <Comp extends Component> void addComponent(Comp component) {
        components.put(component.getClass(), component.get());
        if (component instanceof Transform && parent != null)
            ((Transform) component)
                    .setParent(parent.getComponent(Transform.class).orElse(null));
    }

    public final <Comp extends Component> Optional<Comp> getComponent(Class<Comp> compType) {
        if (components.containsKey(compType))
            return Optional.of((Comp) components.get(compType));
        return Optional.empty();
    }

    public final <Comp extends Component> void removeComponent(Class<Comp> compType) {
        getComponent(compType).ifPresent(Component::delete);
        components.remove(compType);
    }

    public final Entity getParent() {
        return parent;
    }

    public final void setParent(Entity parent) {
        if (this.parent == parent || parent == this) return; // Prevents infinite recursion
        this.parent = parent;
        if (this.parent != null) {
            this.parent.getComponent(Transform.class).ifPresent(
                    transform -> this.getComponent(Transform.class).ifPresent(
                            childTransform -> childTransform.setParent(transform)
                    )
            );
            parent.addChild(this);
        }
    }

    public final void addChild(Entity child) {
        if (!children.contains(child) || child != this) {
            children.add(child);
            child.setParent(this);
        }
    }

    public final void setUpdateCallback(UpdateCallback callback) {
        updateCallback = callback;
    }

    public final void setRenderCallback(RenderCallback callback) {
        renderCallback = callback;
    }

    public final void update() {
        if (updateCallback != null)
            updateCallback.invoke(this);
    }

    public final void updateStack() {
        update();
        children.forEach(Entity::updateStack);
    }

    public final void render() {
        if (renderCallback != null)
            renderCallback.invoke(this);
    }

    public final void renderStack() {
        render();
        children.forEach(Entity::renderStack);
    }

    public final void destroy() {
        components.forEach((compClass, component) ->
                component.delete()
        );
        components.clear();
        updateCallback = null;
        renderCallback = null;
    }

    public final void destroyStack() {
        destroy();
        children.forEach(Entity::destroyStack);
    }

    public static Entity create(Component... components) {
        var entity = new Entity(new Transform());
        for (var component : components)
            entity.addComponent(component);
        return entity;
    }
}
