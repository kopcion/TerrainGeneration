package core.buffers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL40;

import core.math.Vec2f;
import core.utils.BufferUtil;

//Buffer Object to store vertices to draw a terrain in GP U memory
public class VerticesVertexBufferObject implements VertexBufferObject {

	private int vertexBufferId;
	private int vertexArrayObjectId;
	private int size;
	
	public VerticesVertexBufferObject() {
		// generating each buffer using OpenGL:
		vertexBufferId = GL15.glGenBuffers();
		vertexArrayObjectId = GL30.glGenVertexArrays();
	}
	
	public void allocate(Vec2f[] vertices) {
		size = vertices.length;
		
		GL30.glBindVertexArray(vertexArrayObjectId);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferId);
		// filling buffer with the vertexes data:
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, BufferUtil.createFlippedBuffer(vertices), GL15.GL_STATIC_DRAW);
		
		// setting pointer - 2, because it's Vec2:
		GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, Float.BYTES*2, 0);
		GL40.glPatchParameteri(GL40.GL_PATCH_VERTICES, size);
		
		GL30.glBindVertexArray(0);
	}

	@Override
	public void draw() {
		GL30.glBindVertexArray(vertexArrayObjectId);
		GL20.glEnableVertexAttribArray(0);
		
		// drawing patch:
		GL11.glDrawArrays(GL40.GL_PATCHES, 0, size);

		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

	@Override
	public void delete() {
		// deleting gpu data:
		GL30.glBindVertexArray(vertexArrayObjectId);
		GL15.glDeleteBuffers(vertexBufferId);
		GL30.glDeleteVertexArrays(vertexArrayObjectId);
		GL30.glBindVertexArray(0);
	}

}
