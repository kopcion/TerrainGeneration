package core.buffers;

import core.model.Mesh;

//Buffer for storing our data in GPU to draw it
public interface VBO {
	
	public void draw();
	public void delete();	
}
