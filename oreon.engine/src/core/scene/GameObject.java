 package core.scene;

import java.util.HashMap;

public class GameObject extends Node {

	private HashMap<String, Component> components;
	
	public GameObject()
	{
		components = new HashMap<String, Component>();
	}
	
	public void input()
	{
		for (String key : components.keySet()) {
			components.get(key).input();
		}
		
		super.input();
	}
	
	public void update()
	{	
		for (String key : components.keySet()) {
			components.get(key).update();
		}
		
		super.update();
	}
	
	public void render()
	{
		for (String key : components.keySet()) {
			components.get(key).render();
		}
	}

	public void addComponent(String string, Component component)
	{
		component.setParent(this);
		components.put(string, component);
	}
	
	public HashMap<String, Component> getComponents() {
		return components;
	}
	
	public void setComponents(HashMap<String, Component> components)
	{
		this.components = components;
	}

//	public Component getComponent(String component)
//	{
//		return this.components.get(component);
//	}
//

}