package core.buffers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import core.model.Mesh;
import core.utils.BufferUtil;

// Buffer Object to store Mesh in GP U memory - we're going to use it from renderer.
public class MeshVertexBufferObject implements VertexBufferObject {
	
	private int vertexBufferId; 
	private int indicesBufferId;
	private int vertexArrayObjectId; // vertex array object buffer id
	private int size; // size of a mesh 
	
	public MeshVertexBufferObject() {
		// generating each buffer using OpenGL
		vertexBufferId = GL15.glGenBuffers();
		indicesBufferId = GL15.glGenBuffers();
		vertexArrayObjectId = GL30.glGenVertexArrays();
	}
	
	public void allocate(Mesh mesh) {	
		// setting size as a number of incices:
		size = mesh.getIndices().length;
		
		GL30.glBindVertexArray(vertexArrayObjectId);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferId);
		// filling buffer with the data from mesh:
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, BufferUtil.createFlippedBufferAOS(mesh.getVertices()), GL15.GL_STATIC_DRAW);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBufferId);
		// filling buffer with the data from mesh:
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, BufferUtil.createFlippedBuffer(mesh.getIndices()), GL15.GL_STATIC_DRAW );
		
		// setting pointers for pos, normal and textureCoord
		// 3, because Vertex is vec3, 8 because = 3 (pos) + 3(normal) + 2(textureCoord)
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, Float.BYTES * 8, 0);
		
		//Unbinding
		GL30.glBindVertexArray(0);
	}

	public void draw() {
		// Drawing mesh
		GL30.glBindVertexArray(vertexArrayObjectId);
		
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawElements(GL11.GL_TRIANGLES, size, GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);

		GL30.glBindVertexArray(0);
	}

	public void delete() {
		// Deleting Objects:
		GL30.glBindVertexArray(vertexArrayObjectId);
		
		GL15.glDeleteBuffers(vertexBufferId);
		GL15.glDeleteBuffers(indicesBufferId);
		GL30.glDeleteVertexArrays(vertexArrayObjectId);
		
		GL30.glBindVertexArray(0);
	}
	
}

