package core.scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import core.math.Transform;

public class Node {
	
	private Node parent;
	private List<Node> relatedNodes;
	private Transform worldTransform;
	private Transform localTransform;
	private HashMap<String, Component> components;
	
	public Node() {
		setWorldTransform(new Transform());
		setLocalTransform(new Transform());
		setChildren(new ArrayList<Node>());
		setComponents(new HashMap<String, Component>());
	}
	
	public void addNode(Node child) {
		child.setParent(this);
		relatedNodes.add(child);
	}
	
	public void update() {
		for (String key : components.keySet()) {
			components.get(key).update();
		}
		
		for (Node child: relatedNodes) {
			child.update();
		}
	}
	
	public void input() {
		for (String key : components.keySet()) {
	 		components.get(key).input();
		}
		
		for (Node node: relatedNodes) {
			node.input();
		}
	}
	
	public void render() {
		for (String key : components.keySet()) {
			components.get(key).render();
		}
		
		for (Node node: relatedNodes) {
			node.render();
		}
	}
	
	public void shutdown() {
		for (Node child: relatedNodes) {
			child.shutdown();
		}
	}
	
	public Node getParent() {
		return parent;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public List<Node> getRelatedNodes() {
		return relatedNodes;
	}
	
	public void setChildren(List<Node> children) {
		this.relatedNodes = children;
	}
	
	public Transform getWorldTransform() {
		return worldTransform;
	}
	
	public void setWorldTransform(Transform worldTransform) {
		this.worldTransform = worldTransform;
	}
	
	public Transform getLocalTransform() {
		return localTransform;
	}
	
	public void setLocalTransform(Transform localTransform) {
		this.localTransform = localTransform;
	}
	 
	public void setComponents(HashMap<String, Component> cmp) {
		this.components = cmp;
	}
	
	public void addComponent(String string, Component component)
	{
		component.setParent(this);
		components.put(string, component);
	}
	
	public HashMap<String, Component> getComponents() {
		return components;
	}
}
