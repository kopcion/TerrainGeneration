package core.buffers;

import core.model.Mesh;

//Buffer for storing our data in GPU to draw it
public interface VBO {
	
	public void allocate(Mesh mesh);
	public void draw();
	public void delete();	
}
