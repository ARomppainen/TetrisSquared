#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform mat4 u_projTrans;
uniform vec3 u_key;

void main() {
    vec3 color = texture2D(u_texture, v_texCoords).rgb;

    if (color == u_key) {
        color = v_color.rgb;
    }

    gl_FragColor = vec4(color, v_color.a);
}