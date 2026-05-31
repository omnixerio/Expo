package dev.michey.expo.render.light.box2dport.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import dev.michey.expo.render.light.box2dport.RayHandler;

public class DynamicShadowShader {
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

		// this is always perfect precision
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
        //
        //
        //
        //
        //				+ "gl_FragColor.rgb = (ambient.rgb + c.rgb * c.a) - (sh.rgb * (1.0 - c.rgb));\n"//
        //
        //
        //
        //
        //
        //
        //				+ "    gl_FragColor.rgb = (ambient.rgb + (" + RayHandler.getDynamicShadowColorReduction() + " * c.rgb)) - (sh.rgb * (1.0 - c.rgb));\n"
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
                uniform sampler2D u_shadows;
                uniform vec4 ambient;
                uniform int isDiffuse;
                
                out vec4 fragColor;
                
                void main() {
    				if(isDiffuse == 0) {
						vec4 c = texture(u_texture, v_texCoords);
						vec4 sh = texture(u_shadows, v_texCoords);
						fragColor.rgb = (ambient.rgb + c.rgb * c.a) - sh.rgb;
						fragColor.a = ambient.a - c.a;
					} else {
						vec4 c = texture(u_texture, v_texCoords);
						vec4 sh = texture(u_shadows, v_texCoords);
						fragColor.rgb = (ambient.rgb + (%s * c.rgb)) - sh.rgb;
						fragColor.a = 1.0;
					}
                }
                """.formatted(RayHandler.getDynamicShadowColorReduction());
		//
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
