package org.openrndr.extra.shadergenerator.phrases
import org.openrndr.extra.shadergenerator.annotations.IndexShaderBook
import org.openrndr.extra.shadergenerator.annotations.ShaderBook
import org.openrndr.extra.shadergenerator.annotations.WrapShaderBook

@IndexShaderBook
@WrapShaderBook
class FibonacciPhrases : ShaderBook {

    /**
     * sphericalDistribution
     * - https://www.shadertoy.com/view/lllXz4
     * - https://github.com/shader-park/shader-park-core/blob/0e4b81c092a7e0670bbab9bd4444221a2a8a0a01/glsl/glsl-lib.js#L506
     */
    val sphericalDistribution = """#pragma import PHI
#pragma import PI        
vec4 sphericalDistribution( vec3 p, int n )
{
    p = normalize(p);
    float m = 1.0 - 1.0/n;

    float phi = min(atan(p.y, p.x), PI), cosTheta = p.z;

    float k = max(2.0, floor( log(n * PI * sqrt(5.0) * (1.0 - cosTheta*cosTheta))/ log(PHI+1.0)));
    float Fk = pow(PHI, k)/sqrt(5.0);
    vec2 F = vec2( floor(Fk + 0.5), floor(Fk * PHI + 0.5) ); // k, k+1

    vec2 ka = 2.0*F/n;
    vec2 kb = 2.0*PI*( fract((F+1.0)*PHI) - (PHI-1.0) );

    mat2 iB = mat2( ka.y, -ka.x,
    kb.y, -kb.x ) / (ka.y*kb.x - ka.x*kb.y);

    vec2 c = floor( iB * vec2(phi, cosTheta - m));
    float d = 8.0;
    float j = 0.0;
    vec3 bestQ = vec3(0.0,0.0,8.0);
    for( int s=0; s<4; s++ )
    {
        vec2 uv = vec2( float(s-2*(s/2)), float(s/2) );

        float i = dot(F, uv + c); // all quantities are ingeters (can take a round() for extra safety)

        float phi = 2.0*PI*fract(i*PHI);
        float cosTheta = m - 2.0*i/n;
        float sinTheta = sqrt(1.0 - cosTheta*cosTheta);

        vec3 q = vec3( cos(phi)*sinTheta, sin(phi)*sinTheta, cosTheta );
        float squaredDistance = dot(q-p, q-p);
        if (squaredDistance < d)
        {
            d = squaredDistance;
            j = i;
            bestQ = q;
        }
        
    }
    return vec4(bestQ,sqrt(d));
}"""
}