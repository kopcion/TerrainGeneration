package core.math;

import core.kernel.Camera;

public class Transform {
	
	private Vec3f translation;
	private Vec3f scale;
	
	public Transform()
	{
		this.translation = new Vec3f(0, 0, 0);
		this.scale = new Vec3f(1,1,1);
	}
	
	public Matrix4f getWorldTransformMatrix()
	{
		Matrix4f translationMatrix = new Matrix4f().Translation(translation);
		
		Matrix4f scalingMatrix = new Matrix4f().Scaling(scale);
		
		return translationMatrix.mul(scalingMatrix);
	}
	
	
	public Matrix4f getModelViewProjectionMatrix()
	{
		return Camera.getInstance().getViewProjectionMatrix().mul(getWorldTransformMatrix());
	}

	public void setTranslation(Vec3f translation) {
		this.translation = translation;
	}

	public void setScale(Vec3f scale) {
		this.scale = scale;
	}
}