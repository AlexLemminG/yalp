attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_velocity;
attribute vec2 a_texCoord0;
uniform mat4 u_worldView;
uniform float time;
varying vec4 v_color;
varying vec2 v_texCoords;
varying vec2 v_velocity;
void main()
{
	
   //v_color = a_color;
   v_velocity = a_velocity;
   v_texCoords = a_texCoord0;
   gl_Position = a_position;
}