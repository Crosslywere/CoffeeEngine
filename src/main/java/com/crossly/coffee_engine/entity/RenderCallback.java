package com.crossly.coffee_engine.entity;

import com.crossly.coffee_engine.component.graphics.GraphicsShader;
import com.crossly.coffee_engine.component.graphics.Mesh;
import com.crossly.coffee_engine.component.graphics.StaticMesh;

/**
 * @author Jude Ogboru
 */
public interface RenderCallback {
	default void invoke(Entity self) {
		self.getComponent(GraphicsShader.class).ifPresent(
				shader -> {
					shader.use();
					self.getComponent(StaticMesh.class).ifPresent(Mesh::render);
				});
	}
}
