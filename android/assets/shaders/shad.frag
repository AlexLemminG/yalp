#ifdef GL_ES
precision mediump float;
#endif

uniform float time;
uniform vec2 mouse;
uniform vec2 resolution;
uniform sampler2D u_vel_texture;
uniform sampler2D u_vel_blured_texture;
uniform sampler2D u_texture;
varying vec4 v_color;
varying vec2 v_velocity;
varying vec2 v_texCoords;
#define PI 3.14159265359

uniform float weight[2] = float[]( 1., 0.25 );


void main2( void ) {
	
	vec2 uv = gl_FragCoord.xy / resolution;
	vec4 c2 = texture2D(u_texture, uv);
	gl_FragColor = c2;
}

void main(void)
{
	//vec4 vColor = vec4(v_velocity /2 +0.5, 0.5, 1.);

	vec2 uv = v_texCoords;
    vec4 d = texture2D( u_texture, uv );
    vec4 vel = texture2D( u_vel_texture, uv );
    vec4 vel_b = texture2D( u_vel_blured_texture, uv );
    vec2 v = (vel.xy - 0.5)*50.;
	v /=100;
	float samplesCount = 10;
	for(int i = 1; i <= samplesCount; i++){
		float t = i / samplesCount;
		d +=  texture2D( u_texture, uv+v.xy*t);
		d +=  texture2D( u_texture, uv-v.xy*t);
	}
	d /= samplesCount*2+1.;
	
	vec4 vv = vec4(v.xy+0.5,0.0,1.);

	gl_FragColor =vv;
}