#version 450 core
// <primitive-types> (ShadeStyleGLSL.kt)
#define d_vertex_buffer 0
#define d_image 1
#define d_circle 2
#define d_rectangle 3
#define d_font_image_map 4
#define d_expansion 5
#define d_fast_line 6
#define d_mesh_line 7
#define d_point 8
#define d_custom 9
#define d_primitive d_circle
// </primitive-types>
uniform float p_time; 
uniform sampler2D p_texture;
uniform vec4 p_4; 
uniform vec2 p_step; 
uniform int p_window; 

layout(origin_upper_left) in vec4 gl_FragCoord;

// <drawer-uniforms(true, false)> (ShadeStyleGLSL.kt)
            
layout(shared) uniform ContextBlock {
    uniform mat4 u_modelNormalMatrix;
    uniform mat4 u_modelMatrix;
    uniform mat4 u_viewNormalMatrix;
    uniform mat4 u_viewMatrix;
    uniform mat4 u_projectionMatrix;
    uniform float u_contentScale;
    uniform float u_modelViewScalingFactor;
    uniform vec2 u_viewDimensions;
};
            
// </drawer-uniforms>
in vec3 va_position;
in vec3 va_normal;
in vec2 va_texCoord0;
in vec3 vi_offset;
in vec2 vi_radius;
in vec4 vi_fill;
in vec4 vi_stroke;
in float vi_strokeWeight;

// <transform-varying-in> (ShadeStyleGLSL.kt)
in vec3 v_worldNormal;
in vec3 v_viewNormal;
in vec3 v_worldPosition;
in vec3 v_viewPosition;
in vec4 v_clipPosition;
flat in mat4 v_modelNormalMatrix;
// </transform-varying-in>

out vec4 o_color;

vec3 mod289(const in vec3 x) { return x - floor(x * (1. / 289.)) * 289.; }
vec4 mod289(const in vec4 x) { return x - floor(x * (1. / 289.)) * 289.; }
vec3 permute(const in vec3 x) { return mod289(((x * 34.0) + 1.0) * x); }
vec4 permute(const in vec4 x) { return mod289(((x * 34.0) + 1.0) * x); }
vec4 taylorInvSqrt(in vec4 r) { return 1.79284291400159 - 0.85373472095314 * r; }
float simplex13(in vec3 v) {
    const vec2  C = vec2(1.0/6.0, 1.0/3.0) ;
    const vec4  D = vec4(0.0, 0.5, 1.0, 2.0);

    // First corner
    vec3 i  = floor(v + dot(v, C.yyy) );
    vec3 x0 =   v - i + dot(i, C.xxx) ;

    // Other corners
    vec3 g = step(x0.yzx, x0.xyz);
    vec3 l = 1.0 - g;
    vec3 i1 = min( g.xyz, l.zxy );
    vec3 i2 = max( g.xyz, l.zxy );

    //   x0 = x0 - 0.0 + 0.0 * C.xxx;
    //   x1 = x0 - i1  + 1.0 * C.xxx;
    //   x2 = x0 - i2  + 2.0 * C.xxx;
    //   x3 = x0 - 1.0 + 3.0 * C.xxx;
    vec3 x1 = x0 - i1 + C.xxx;
    vec3 x2 = x0 - i2 + C.yyy; // 2.0*C.x = 1/3 = C.y
    vec3 x3 = x0 - D.yyy;      // -1.0+3.0*C.x = -0.5 = -D.y

    // Permutations
    i = mod289(i);
    vec4 p = permute( permute( permute(
                i.z + vec4(0.0, i1.z, i2.z, 1.0 ))
            + i.y + vec4(0.0, i1.y, i2.y, 1.0 ))
            + i.x + vec4(0.0, i1.x, i2.x, 1.0 ));

    // Gradients: 7x7 points over a square, mapped onto an octahedron.
    // The ring size 17*17 = 289 is close to a multiple of 49 (49*6 = 294)
    float n_ = 0.142857142857; // 1.0/7.0
    vec3  ns = n_ * D.wyz - D.xzx;

    vec4 j = p - 49.0 * floor(p * ns.z * ns.z);  //  mod(p,7*7)

    vec4 x_ = floor(j * ns.z);
    vec4 y_ = floor(j - 7.0 * x_ );    // mod(j,N)

    vec4 x = x_ *ns.x + ns.yyyy;
    vec4 y = y_ *ns.x + ns.yyyy;
    vec4 h = 1.0 - abs(x) - abs(y);

    vec4 b0 = vec4( x.xy, y.xy );
    vec4 b1 = vec4( x.zw, y.zw );

    //vec4 s0 = vec4(lessThan(b0,0.0))*2.0 - 1.0;
    //vec4 s1 = vec4(lessThan(b1,0.0))*2.0 - 1.0;
    vec4 s0 = floor(b0)*2.0 + 1.0;
    vec4 s1 = floor(b1)*2.0 + 1.0;
    vec4 sh = -step(h, vec4(0.0));

    vec4 a0 = b0.xzyw + s0.xzyw*sh.xxyy ;
    vec4 a1 = b1.xzyw + s1.xzyw*sh.zzww ;

    vec3 p0 = vec3(a0.xy,h.x);
    vec3 p1 = vec3(a0.zw,h.y);
    vec3 p2 = vec3(a1.xy,h.z);
    vec3 p3 = vec3(a1.zw,h.w);

    //Normalise gradients
    vec4 norm = taylorInvSqrt(vec4(dot(p0,p0), dot(p1,p1), dot(p2, p2), dot(p3,p3)));
    p0 *= norm.x;
    p1 *= norm.y;
    p2 *= norm.z;
    p3 *= norm.w;

    // Mix final noise value
    vec4 m = max(0.6 - vec4(dot(x0,x0), dot(x1,x1), dot(x2,x2), dot(x3,x3)), 0.0);
    m = m * m;
    return 42.0 * dot( m*m, vec4( dot(p0,x0), dot(p1,x1),
                                dot(p2,x2), dot(p3,x3) ) );
}
vec4 f(ivec2 x) { 
    return texture(p_texture, (va_texCoord0 + (((x * 0.1) * step) * cos(p_time))));
}
float w(ivec2 x) { 
    return length((x * 1.0));
}
vec4 weightedAverageBy_3069360881(int startX, int endX, int startY, int endY) {
    vec4 sum = vec4(0.0);
    float weight = 0.0;
    for (int j = startY; j < endY; ++j) {
       for (int i = startX; i < endX; ++i) {
           sum += f(ivec2(i, j));
           weight += w(ivec2(i, j));
       }
    }
    return sum / weight;
}


flat in int v_instance;
in vec3 v_boundsSize;
void main(void) {
        // -- fragmentConstants
    int c_instance = v_instance;
    int c_element = 0;
    vec2 c_screenPosition = gl_FragCoord.xy / u_contentScale;
    float c_contourPosition = 0.0;
    vec3 c_boundsPosition = vec3(va_texCoord0, 0.0);
    vec3 c_boundsSize = v_boundsSize;
    float smoothFactor = 3.0;

    vec4 x_fill = vi_fill;
    vec4 x_stroke = vi_stroke;
    float x_strokeWeight = vi_strokeWeight;
    
    {
        float d = length((c_boundsPosition.xy - vec2(0.5, 0.5)));
float n = ((simplex13(vec3((c_boundsPosition.xy * 10.0), p_time)) * 0.5) + 0.5);
float sd = ((smoothstep(1.0, 0.9, (d * 2.0)) * smoothstep(0.3, 0.9, (d * 2.0))) * n);
vec4 c = texture(p_texture, c_boundsPosition.xy);
vec2 step = (1.0 / textureSize(p_texture, 0));
vec4 s = weightedAverageBy_3069360881((-p_window), (p_window + 1), (-p_window), (p_window + 1));
x_fill = s;

    }
    float wd = fwidth(length(va_texCoord0 - vec2(0.0)));
    float d = length(va_texCoord0 - vec2(0.5)) * 2;

    float or = smoothstep(0, wd * smoothFactor, 1.0 - d);
    float b = x_strokeWeight / vi_radius.x;
    float ir = smoothstep(0, wd * smoothFactor, 1.0 - b - d);

    vec4 final = vec4(0.0);
    final.rgb =  x_stroke.rgb;
    final.a = or * (1.0 - ir) * x_stroke.a;
    final.rgb *= final.a;

    final.rgb += x_fill.rgb * ir * x_fill.a;
    final.a += ir * x_fill.a;
    o_color = final;
}
// -------------
// shade-style-custom:circle--882096270
// created 2023-05-09T08:12:50.112709300
/*
ERROR: 0:136: '*' : wrong operand types no operation '*' exists that takes a left-hand operand of type '2-component vector of highp float' and a right operand of type 'void' (or there is no acceptable conversion)

*/
