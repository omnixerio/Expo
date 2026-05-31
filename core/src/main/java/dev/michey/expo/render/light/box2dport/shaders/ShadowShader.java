package dev.michey.expo.render.light.box2dport.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public final class ShadowShader {
	static final public ShaderProgram createShadowShader() {
        //
        //
        //
        //
        //
        //
        //
        //
        final String vertexShader = """
                #version 330
                
                in vec4 a_position;
                in vec2 a_texCoord;
                out vec2 v_texCoords;
                
                void main()
                {
                   v_texCoords = a_texCoord;
                   gl_Position = a_position;
                }
                """;
        //
        //
        //
        //
        //
        //
        //
        //
        //
        //
        final String fragmentShader = """
                #version 330
                
                #ifdef GL_ES
                precision lowp float;
                #define MED mediump
                #else
                #define MED\s
                #endif
                in MED vec2 v_texCoords;
                uniform sampler2D u_texture;
                uniform vec4 ambient;
                
                out vec4 fragColor;
                void main() {
					vec4 c = texture(u_texture, v_texCoords);
					fragColor.rgb = c.rgb * c.a + ambient.rgb;
					fragColor.a = ambient.a - c.a;
                }
                """;
		ShaderProgram.pedantic = false;
		ShaderProgram shadowShader = new ShaderProgram(vertexShader,
				fragmentShader);
		if (!shadowShader.isCompiled()) {
			if(!shadowShader.isCompiled()){
				Gdx.app.log("ERROR", shadowShader.getLog());
			}
		}

		return shadowShader;
	}
}
