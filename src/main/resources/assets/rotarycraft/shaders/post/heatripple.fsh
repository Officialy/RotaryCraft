#version 330

#moj_import <minecraft:globals.glsl>

// Heat haze over hot machines. Ported from RotaryCraft's 1.7.10 heatripple.frag.
//
// The 1.7.10 version ran this once per hot machine, ping-ponging the framebuffer, with that
// machine's values in uniforms and the projection done shader-side. A modern PostChain bakes its
// uniforms when the chain compiles, so instead the CPU (HeatRippleRenderer) projects each emitter to
// screen space and uploads the whole set as one dynamic UBO, and this resolves them in a single
// fullscreen pass.
//
// Batching the points changes nothing about the falloff: each point's strength is still measured
// against the true screen coordinate, exactly as each of the original's passes did. The one real
// difference is that the original re-sampled the previous pass's output per point, whereas this
// accumulates the displacement and samples once at the end -- the same composition, without the
// repeated resampling blurring the image.

const int MAX_HEAT_POINTS = 32;

layout(std140) uniform HeatPoints {
    ivec4 HeatCount;               // .x = number of populated entries
    vec4 HeatTime;                 // .x = world time in ticks (the original's "time" uniform)
    vec4 Focus[MAX_HEAT_POINTS];   // .xy = screen UV, .z = squared distance to viewer, .w = intensity
    vec4 Params[MAX_HEAT_POINTS];  // .x = factor, .y = scale, .z = fade
};

uniform sampler2D InSampler;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    // Screen distances are measured x-normalised, matching lib_math.txt's distsq().
    float aspect = ScreenSize.y / ScreenSize.x;
    float T = HeatTime.x;

    vec2 texUV = texCoord;

    for (int i = 0; i < HeatCount.x; i++) {
        vec2 focus = Focus[i].xy;
        float distSq = Focus[i].z; // named distance in 1.7.10; renamed, as GLSL has a distance()
        float intensity = Focus[i].w;
        float factor = Params[i].x;
        float scale = Params[i].y;
        float fade = Params[i].z;

        vec2 d = texCoord - focus;
        d.y *= aspect;
        float distv = max(1.0e-6, dot(d, d)); // guards the 0.008/(distv*distSq) term at the centre

        float distfac = max(0.0, min(1.0, 2.25 - 65.0 / scale * distv * distSq / factor)
                                 - factor * fade * min(1.0, 0.008 / (distv * distSq)));
        float vf = intensity * distfac * 0.05 * factor;
        if (vf <= 0.0)
            continue;

        float ds = pow(distSq, 0.125) / 1.5;
        float dv = 1.0 + pow(factor, 1.75) * 1.5;

        // Order matters: each line reads the value the previous line just wrote.
        texUV.x += 0.47 * vf * sin(23.3 + texUV.y * 51.8 * ds + T * dv / 4.1);
        texUV.y += 0.62 * vf * cos(34.5 + texUV.x * 45.7 * ds + T * dv / 3.8);
        texUV.x += 0.167 * vf * sin(23.3 + texUV.y * 171.8 * ds + T * dv / 6.1);
        texUV.y += 0.145 * vf * cos(34.5 + texUV.x * 185.7 * ds + T * dv / 5.8);
    }

    fragColor = texture(InSampler, texUV);
}
