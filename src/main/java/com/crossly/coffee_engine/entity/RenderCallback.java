package com.crossly.coffee_engine.entity;

import com.crossly.coffee_engine.component.Mesh;
import com.crossly.coffee_engine.component.Shader;
import com.crossly.coffee_engine.component.StaticMesh;

public interface RenderCallback {
    default void invoke(Entity self) {
        self.getComponent(Shader.class).ifPresent(
                shader -> {
                    shader.use();
                    self.getComponent(StaticMesh.class).ifPresent(Mesh::render);
                }
        );
    }
}
