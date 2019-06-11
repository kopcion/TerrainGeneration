package core.buffers;

// interface for buffer which we're going to use for storing our data in GPU memory to be able to draw it
public interface VertexBufferObject {
	
	public void draw();
	public void delete();	
}
