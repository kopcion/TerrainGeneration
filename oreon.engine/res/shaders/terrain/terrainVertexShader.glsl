#version 430

// 16 verices position:
layout (location = 0) in vec2 position0;

// map coordinates to pass them to next Shader stage
out vec2 mapCoordinates_TC;

// matrixes to be able to calculate positions of vertexes:
uniform mat4 localTransformMatrix;
uniform mat4 worldTransformMatrix;

uniform float scaleY;
uniform int levelOfDetails;
uniform vec2 index;
uniform float gap;
uniform vec2 location;
uniform int levOfDetailsMorphArea[8];

uniform vec3 cameraPosition;

uniform sampler2D heightMap;

float morphLatitude(vec2 position) {
	
	vec2 frac = position - location;
	
	if (index == vec2(0,0)){
		// bottom left related node:
		float morph = frac.x - frac.y;
		if (morph > 0)
			return morph;
	} if (index == vec2(1,0)){
		// bottom right related node:
		float morph = gap - frac.x - frac.y;
		if (morph > 0)
			return morph;
	} if (index == vec2(0,1)){
		// top left related node:
		float morph = frac.x + frac.y - gap;
		if (morph > 0)
			return -morph;
	} if (index == vec2(1,1)){
		// top right related node:
		float morph = frac.y - frac.x;
		if (morph > 0)
			return -morph;
	}
	return 0;
}

float morphLongitude(vec2 position) {
	
	vec2 frac = position - location;
	
	if (index == vec2(0,0)){
		// bottom left related node:
		float morph = frac.y - frac.x;
		if (morph > 0)
			return -morph;
	} if (index == vec2(1,0)){
		// bottom right related node:
		float morph = frac.y - (gap - frac.x);
		if (morph > 0)
			return morph;
	} if (index == vec2(0,1)){
		// top left related node:
		float morph = gap - frac.y - frac.x;
		if (morph > 0)
			return -morph;
	} if (index == vec2(1,1)){
		// top right related node:
		float morph = frac.x - frac.y;
		if (morph > 0)
			return morph;
	}
	return 0;
}

vec2 morph(vec2 localPosition, int morph_area){
	
	vec2 morphing = vec2(0,0); 
	
	vec2 fixPointLatitude = vec2(0,0);
	vec2 fixPointLongitude = vec2(0,0);
	float distLatitude;
	float distLongitude;
	
	if (index == vec2(0,0)) {
		// bottom left related node:
		fixPointLatitude = location + vec2(gap,0);
		fixPointLongitude = location + vec2(0,gap);
	} else if (index == vec2(1,0)) {
		// bottom right related node:
		fixPointLatitude = location;
		fixPointLongitude = location + vec2(gap,gap);
	} else if (index == vec2(0,1)) {
		// top left related node:
		fixPointLatitude = location + vec2(gap,gap);
		fixPointLongitude = location;
	} else if (index == vec2(1,1)) {
		// top right related node:
		fixPointLatitude = location + vec2(0,gap);
		fixPointLongitude = location + vec2(gap,0);
	}
	
	float planarFactor = 0;
	if (cameraPosition.y > abs(scaleY))
		planarFactor = 1;
	else
		planarFactor = cameraPosition.y/ abs(scaleY);
		
	distLatitude = length(cameraPosition - (worldTransformMatrix * 
					vec4(fixPointLatitude.x,planarFactor,fixPointLatitude.y,1)).xyz);
	distLongitude = length(cameraPosition - (worldTransformMatrix * 
					vec4(fixPointLongitude.x,planarFactor,fixPointLongitude.y,1)).xyz);
					
	if (distLatitude > morph_area)
		morphing.x = morphLatitude(localPosition.xy);
	if (distLongitude > morph_area) 
		morphing.y = morphLongitude(localPosition.xy);
		
	return morphing;
}

void main() {

	//calculating the position to draw:
	vec2 myLocalPosition = (localTransformMatrix * vec4(position0.x, 0, position0.y, 1)).xz;
	
	// We morph vertex when levelOfDetails > 0 (we are not just on the vertexes):
	if (levelOfDetails > 0) {
		// morph position with next level of details - that's why -1:
		myLocalPosition += morph(myLocalPosition, levOfDetailsMorphArea[levelOfDetails-1]);
	}
	
	// fetching height of current position:
	float height = texture(heightMap, myLocalPosition).r;
	
	mapCoordinates_TC = myLocalPosition;
	
	// calculating position in worldSpace:
	gl_Position = worldTransformMatrix * vec4(myLocalPosition.x, height ,myLocalPosition.y ,1);
	
}