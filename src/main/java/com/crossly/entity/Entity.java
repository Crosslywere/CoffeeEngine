package com.crossly.entity;

import com.crossly.components.ShaderProgram;
import com.crossly.components.subcomponents.Transform;
import com.crossly.interfaces.Component;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Entity {

    private final Map<String, Component> components = new HashMap<>();
    private final ArrayList<Entity> children = new ArrayList<>();
    private final Transform transform = new Transform();
    private String shaderKey = null;

    public Entity() {
    }

    public Entity(Component... components) {
        for (var component : components) {
            addComponent(component.getClass().getSimpleName(), component);
        }
    }

    public <E extends Component> E getComponent(String name, Class<E> type) {
        var comp = components.get(name);
        if (comp.getClass() == type) {
            return (E) comp;
        }
        return null;
    }

    public Optional<ShaderProgram> getShaderComponent() {
        if (shaderKey != null)
            return Optional.of(getComponent(shaderKey, ShaderProgram.class));
        return Optional.empty();
    }

    public void setShaderProjection(Matrix4f matrix) {
        getShaderComponent().ifPresent(shader -> shader.setProjectionMatrix(matrix));
    }

    public void setShaderView(Matrix4f matrix) {
        getShaderComponent().ifPresent(shader -> shader.setViewMatrix(matrix));
    }

    public void setShaderModel(Matrix4f matrix) {
        getShaderComponent().ifPresent(shader -> shader.setModelMatrix(matrix));
    }

    public <E extends Component> void addComponent(String name, E component) {
        // There should only be one shader
        if (component.getClass() == ShaderProgram.class && shaderKey != null) {
            components.get(shaderKey).cleanup();
            components.remove(shaderKey);
        }
        if (component.getClass() == ShaderProgram.class) {
            shaderKey = name;
        }
        components.put(name, component);
    }

    public <E extends Component> void replaceComponent(String name, E component) {
        var instance = components.get(name);
        if (instance != null)
            components.replace(name, instance, component);
    }

    public <E extends Component> void removeComponent(String name) {
        components.remove(name);
    }

    public void addChild(Entity entity) {
        children.add(entity);
        entity.transform.setParent(this.transform);
    }

    public void addChildren(Entity... entities) {
        for (var entity : entities) {
            addChild(entity);
        }
    }

    public Transform getTransform() {
        return transform;
    }

    public void render() {
        if (shaderKey != null) {
            ShaderProgram shader = getComponent(shaderKey, ShaderProgram.class);
            shader.use();
            shader.setModelMatrix(transform.getTransformMatrix());
            components.values().forEach(Component::render);
//            Texture.resetTextureBindings(); // private static int textureBindingIndex = 0 // reset by this function, incremented by calls to render.
        }
        children.forEach(Entity::render);
    }

    public void cleanup() {
        components.values().forEach(Component::cleanup);
        components.clear();
    }
}
