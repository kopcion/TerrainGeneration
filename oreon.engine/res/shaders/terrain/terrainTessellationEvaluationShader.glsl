#version 430
//This shader transforms quads into triangles and passes it to GeometryShader:

//inputs:
layout(quads, fractional_odd_spacing, cw) in; 
// fractional_odd_spacing - is tesselation function
// cw - counter clockwise

// We have to pass mapCoord to GeometryShader
in vec2 mapCoordinates_TE[];
out vec2 mapCoordinates_GS; 
 
uniform sampler2D heightMap;
uniform float scaleY;

// Fuctions to calculate position of node based on data about node and corners of patch:
vec4 calculatePosition(float x, float y, vec4 topLeftCorner, vec4 bottomLeftCorner, vec4 bottomRightCorner, vec4 topRightCorner) {
	
	vec4 calculatedPosition = ((1 - x) * (1 - y) * topLeftCorner +
		x * (1 - y) * bottomLeftCorner +
		x * y * bottomRightCorner +
		(1 - x) * y * topRightCorner);
	
	return calculatedPosition;
}

// Computing mapCoord within the Tessellation patches:
vec2 calculatePosition(float x, float y, vec2 topLeftCorner, vec2 bottomLeftCorner, vec2 bottomRightCorner, vec2 topRightCorner) {
	
	vec2 calculatedPosition = ((1 - x) * (1 - y) * topLeftCorner +
		x * (1 - y) * bottomLeftCorner +
		x * y * bottomRightCorner +
		(1 - x) * y * topRightCorner);
	
	return calculatedPosition;
}

void main(){

    float x = gl_TessCoord.x;
    float y = gl_TessCoord.y;

	// Computing the position of vertices within a Patch (from 0 to 1):
	vec4 position = calculatePosition(x, y, gl_in[12].gl_Position, 
		gl_in[0].gl_Position, gl_in[3].gl_Position, gl_in[15].gl_Position);
	
	
	vec2 mapCoordinates = calculatePosition(x, y, mapCoordinates_TE[12], 
		mapCoordinates_TE[0], mapCoordinates_TE[3], mapCoordinates_TE[15]);
	
	// reading height from heighMtap
	float height = texture(heightMap, mapCoordinates).r;
	
	// scaling height to out 3d space:
	height *= scaleY;
	
	// setting y position of point to draw it
	position.y = height;
	
	mapCoordinates_GS = mapCoordinates;
	
	gl_Position = position;
}