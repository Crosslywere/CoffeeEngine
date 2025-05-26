package com.crossly.entities;

import com.crossly.CoffeeEngine;
import com.crossly.components.ShaderProgram;
import com.crossly.components.subcomponents.Transform;
import com.crossly.interfaces.Component;
import org.joml.Matrix4f;

import java.util.*;

/**
 * @author Jude Ogboru
 */
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

    @SuppressWarnings("unchecked")
    public <E extends Component> E getComponent(String name, Class<E> type) {
        var comp = components.get(name);
        if (comp != null && comp.getClass() == type) {
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
        getShaderComponent().ifPresent(
                shader -> {
                    shader.use();
                    CoffeeEngine.getCurrentActiveCamera().ifPresent(
                            camera -> {
                                shader.setProjectionMatrix(camera.getProjection());
                                shader.setViewMatrix(camera.getView());
                            }
                    );
                    shader.setModelMatrix(transform.getTransformMatrix());
                    components.values().forEach(Component::render);
                }
        );
        children.forEach(Entity::render);
    }

    public void cleanup() {
        components.values().forEach(Component::cleanup);
        components.clear();
        children.forEach(Entity::cleanup);
    }
}
