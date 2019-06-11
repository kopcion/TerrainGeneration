#version 430 core

// This shader is to calculate mapping (texture) to cover spaces between edges

 // compute space of the shader:
layout (local_size_x = 16, local_size_y = 16) in;

// texture we are writing to:
layout (binding = 0, rgba32f) uniform writeonly image2D normalMapping;

uniform sampler2D heightMap;
uniform int resolution;

void main(void) {
	vec2 textureCoord = gl_GlobalInvocationID.xy/float(resolution);
	float textureSize = 1.0/resolution;
	
	// textures for corners of patch:
	float topLeft = texture(heightMap, textureCoord + vec2(-textureSize,-textureSize)).r;
	float leftMiddle = texture(heightMap, textureCoord + vec2(-textureSize,0)).r;
	float bottomLeft = texture(heightMap, textureCoord + vec2(-textureSize,textureSize)).r;
	
	float topMiddle = texture(heightMap, textureCoord + vec2(0,-textureSize)).r;
	float bottomMiddle = texture(heightMap, textureCoord + vec2(0,textureSize)).r;
	
	float topRight = texture(heightMap, textureCoord + vec2(textureSize,-textureSize)).r;
	float rightMiddle = texture(heightMap, textureCoord + vec2(textureSize,0)).r;
	float bottomRight = texture(heightMap, textureCoord + vec2(textureSize,textureSize)).r;
	
	// Sobel Filter
	vec3 normal;
	// normalization intensity
	normal.z = 1.0/8.0; 
	// on left and right vertices:
	normal.x = topLeft + 2*leftMiddle + bottomLeft - topRight - 2*rightMiddle - bottomRight;
	// on top and boddom verices:
	normal.y = topLeft + 2*topMiddle + topRight -bottomLeft - 2*bottomMiddle - bottomRight;
	
	// storing genereted normal mapping texture:
	imageStore(normalMapping, ivec2(gl_GlobalInvocationID.xy), vec4((normalize(normal)+1)/2,1));
}