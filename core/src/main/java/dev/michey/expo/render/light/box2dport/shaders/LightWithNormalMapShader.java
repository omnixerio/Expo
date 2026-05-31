package dev.michey.expo.render.light.box2dport.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import dev.michey.expo.render.light.box2dport.RayHandler;

/**
 * Shader code adapted from https://github.com/mattdesl/lwjgl-basics/wiki/ShaderLesson6
 */
public class LightWithNormalMapShader {
    static final public ShaderProgram createLightShader() {
        String gamma = "";
        if (RayHandler.getGammaCorrection())
            gamma = "sqrt";

        final String vertexShader =
                """
                #version 330
                
                in vec4 vertex_positions;
                in vec4 quad_colors;
                in float s;
                uniform mat4 u_projTrans;
                out vec4 v_color;
                void main()
                {
                   v_color = s * quad_colors;
                   gl_Position =  u_projTrans * vertex_positions;
                }
                """;

        final String fragmentShader = """
                #version 330
                
                #ifdef GL_ES
                precision lowp float;
                #define MED mediump
                #else
                #define MED\s
                #endif
                in vec4 v_color;
                uniform sampler2D u_normals;
                uniform vec3 u_lightpos;
                uniform vec2 u_resolution;
                uniform float u_intensity;
                uniform vec3 u_falloff;
                
                out vec4 fragColor;
                void main() {
                  vec2 screenPos = gl_FragCoord.xy / u_resolution.xy;
                  vec4 NormalMapTexture = texture(u_normals, screenPos);
                  vec3 NormalMap = NormalMapTexture.rgb;
                  float alpha = NormalMapTexture.a;
                  vec3 LightDir = vec3(u_lightpos.xy - screenPos, u_lightpos.z);
                  LightDir.x *= u_resolution.x / u_resolution.y;
                  float D = length(LightDir);
                  float Attenuation = 1.0 / (u_falloff.x + (u_falloff.y*D) + (u_falloff.z*D*D));
                  vec3 N = normalize(NormalMap * 2.0 - 1.0);
                  vec3 L = normalize(LightDir);
                  float maxProd = (max(dot(N, L), 0.0) * Attenuation - 1.0) * alpha + 1.0;
                  fragColor = %s(v_color * maxProd * u_intensity);
                }""".formatted(gamma);

        ShaderProgram.pedantic = false;
        ShaderProgram lightShader = new ShaderProgram(vertexShader,
                fragmentShader);
        if (!lightShader.isCompiled()) {
            if(!lightShader.isCompiled()){
                Gdx.app.log("ERROR", lightShader.getLog());
            }
        }

        return lightShader;
    }
}
