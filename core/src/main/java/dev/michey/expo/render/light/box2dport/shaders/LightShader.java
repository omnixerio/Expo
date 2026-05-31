package dev.michey.expo.render.light.box2dport.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import dev.michey.expo.render.light.box2dport.RayHandler;

public final class LightShader {
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
                        out float D;
                        void main()
                        {
                           v_color = s * quad_colors;
                           D = s;
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
                in float D;
                uniform float u_intensity;
                uniform vec3 u_falloff;
                
                out vec4 fragColor;
                
                
                void main()
                {
                  float Attenuation = 1.0 / (u_falloff.x + (u_falloff.y*D) + (u_falloff.z*D*D));
                  fragColor = %s(v_color * u_intensity) * Attenuation;
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
