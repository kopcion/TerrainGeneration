#version 430

layout(triangles) in;
layout(triangle_strip, max_vertices = 3) out;

in vec2 mapCoordinates_GS[];
out vec2 mapCoordinates_FS;

uniform mat4 cameraViewProjection;

void main() {
	
	for (int i = 0; i < gl_in.length(); ++i) {
		vec4 position = gl_in[i].gl_Position;
		
		// translating position to viewProjection space of the camera:
		gl_Position = cameraViewProjection * position; 
		
		mapCoordinates_FS = mapCoordinates_GS[i];
		EmitVertex();
	}
	
	EndPrimitive();
}