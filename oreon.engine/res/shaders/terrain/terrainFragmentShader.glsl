#version 430
// FragmentShader sets the color:

layout(location = 0) out vec4 outputColor;

in vec2 mapCoordinates_FS;

uniform sampler2D normalMapping;

// light direction:
const vec3 direction = vec3(0.1,-1,0.1);
// light intensity:
const float intensity = 1.2;

float diffuse(vec3 direction, vec3 normal, float intensity) { 
	return max(dot(normal, -direction) * intensity, 0.01);
}

void main() {
	vec3 normal = texture(normalMapping, mapCoordinates_FS).rgb;
	
	//calculating light diffuse 
	float diffuseFactor = diffuse(direction, normal, intensity);
	
	// setting color to light grey color 
	vec3 colorRGB = vec3(0.9, 0.9,0.8) * diffuseFactor;
	
	// applying diffuse light on it
	outputColor = vec4(colorRGB, 1.0);
}