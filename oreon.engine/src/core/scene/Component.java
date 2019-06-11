package core.scene;

import core.math.Transform;

// Component object to be able to deal with objects in game as Terrain and Sky 
public abstract class Component {
	
	private Node parent;
	
	public void update(){};
	
	public void input(){};
	
	public void render(){};
	
	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Transform getWorldTransform()
	{
		return getParent().getWorldTransform();
	}
	
	public Transform getLocalTransform()
	{
		return getParent().getLocalTransform();
	}
}