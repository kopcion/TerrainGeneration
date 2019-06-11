#version 330

layout (location = 0) in vec3 position0;

out vec3 worldPosition;

uniform mat4 modelViewProjectionMatrix;
uniform mat4 worldMatrix;

void main() {
	// setting position of current vertex:
	gl_Position = modelViewProjectionMatrix * vec4(position0,1);
	
	// setting world position to pass it to fragment shader:
	worldPosition = (worldMatrix * vec4(position0,1)).xyz;
}



