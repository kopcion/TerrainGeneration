package core.buffers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL40;

import core.math.Vec2f;
import core.utils.BufferUtil;

public class PatchVBO implements VBO {

	private int vbId;
	private int vaoId;
	private int size;
	
	public PatchVBO() {
		vbId = GL15.glGenBuffers();
		vaoId = GL30.glGenVertexArrays();
	}
	
	public void allocate(Vec2f[] vertices) {
		size = vertices.length;
		
		GL30.glBindVertexArray(vaoId);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, BufferUtil.createFlippedBuffer(vertices), GL15.GL_STATIC_DRAW);
		
		GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, Float.BYTES*2, 0);
		GL40.glPatchParameteri(GL40.GL_PATCH_VERTICES, size);
		
		GL30.glBindVertexArray(0);
	}

	@Override
	public void draw() {
		GL30.glBindVertexArray(vaoId);
		GL20.glEnableVertexAttribArray(0);
		
		GL11.glDrawArrays(GL40.GL_PATCHES, 0, size);
		
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

	@Override
	public void delete() {
		GL30.glBindVertexArray(vaoId);
		GL15.glDeleteBuffers(vbId);
		GL30.glDeleteVertexArrays(vaoId);
		GL30.glBindVertexArray(0);
	}

}
