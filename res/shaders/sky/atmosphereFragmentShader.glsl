#version 430

in vec3 worldPosition;

layout(location = 0) out vec4 color;

void main() {

	// setting color of fragment based on linear function for color (actual hight as parameter):	
	float r = 0.15 -0.00022*(worldPosition.y-2800);
	float g = 0.25 -0.00022*(worldPosition.y-2800);
	float b = 0.57 -0.00022*(worldPosition.y-2800);
	
	color = vec4(r,g,b,1);
}