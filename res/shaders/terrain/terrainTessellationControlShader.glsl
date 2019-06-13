#version 430
// We are going to compute tesselation function:
// tessellationFactor / x^tessellationSlope + tessellationShift

uniform int tessellationFactor;
uniform float tessellationSlope;
uniform float tessellationShift;

uniform vec3 cameraPosition;

// outputing 16 vertices:
layout(vertices = 16) out;

// We have to pass mapCoord's to next shader stage:
in vec2 mapCoordinates_TC[];
out vec2 mapCoordinates_TE[];

// Edges indices: 
const int bottomEdge = 2;
const int rightEdge = 3;
const int topEdge = 0;
const int leftEdge = 1; 

// Vertexes indices:
const int bottomLeftVertex = 0;
const int bottomRightVertex = 3;
const int topLeftVertex = 12;
const int topRightVertex = 15; 


float calcTesselationLevel(float distanceToCamera) {
	float functionValue = tessellationFactor/pow(distanceToCamera, tessellationSlope) + tessellationShift;
	return max(0.0, functionValue);
}

float calcDistanceToCamera(vec3 edgeMiddle) {
	return distance(edgeMiddle, cameraPosition);
}

void main() {

	// We have to do this only with the first call of tessellation:
	if(gl_InvocationID == 0) {
		// calculating middles of the edges:
		vec3 bottomEdgeMiddle = vec3(gl_in[bottomLeftVertex].gl_Position + gl_in[bottomRightVertex].gl_Position)/2.0;	
		vec3 rightEdgeMiddle = vec3(gl_in[bottomRightVertex].gl_Position + gl_in[topRightVertex].gl_Position)/2.0;
		vec3 topEdgeMiddle = vec3(gl_in[topRightVertex].gl_Position + gl_in[topLeftVertex].gl_Position)/2.0;
		vec3 leftEdgeMiddle = vec3(gl_in[bottomLeftVertex].gl_Position + gl_in[topLeftVertex].gl_Position)/2.0;		
		
		// setting tessellation levels based on our tesselation function:
		gl_TessLevelOuter[bottomEdge] = mix(1, gl_MaxTessGenLevel, calcTesselationLevel(calcDistanceToCamera(bottomEdgeMiddle)));
		gl_TessLevelOuter[rightEdge] = mix(1, gl_MaxTessGenLevel, calcTesselationLevel(calcDistanceToCamera(rightEdgeMiddle)));
		gl_TessLevelOuter[topEdge] = mix(1, gl_MaxTessGenLevel, calcTesselationLevel(calcDistanceToCamera(topEdgeMiddle)));
		gl_TessLevelOuter[leftEdge] = mix(1, gl_MaxTessGenLevel, calcTesselationLevel(calcDistanceToCamera(leftEdgeMiddle)));
		gl_TessLevelInner[0] = (gl_TessLevelOuter[rightEdge] + gl_TessLevelOuter[leftEdge])/4;
		gl_TessLevelInner[1] = (gl_TessLevelOuter[bottomEdge] + gl_TessLevelOuter[topEdge])/4;	
	}
	
	// setting coords for next stage
	mapCoordinates_TE[gl_InvocationID] = mapCoordinates_TC[gl_InvocationID];
	
	// passing gl position to the next shader stage:
	gl_out[gl_InvocationID].gl_Position = gl_in[gl_InvocationID].gl_Position;
}