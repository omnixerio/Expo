package dev.michey.expo.render.light.box2dport.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import dev.michey.expo.render.light.box2dport.RayHandler;

public class Gaussian {

	public static ShaderProgram createBlurShader(int width, int heigth) {
		final String FBO_W = Integer.toString(width);
		final String FBO_H = Integer.toString(heigth);
		final String rgb = RayHandler.isDiffuseLight()  ? ".rgb" : "";

        final String vertexShader = """
                #version 330
                
                in vec4 a_position;
                uniform vec2  dir;
                in vec2 a_texCoord;
                out vec2 v_texCoords0;
                out vec2 v_texCoords1;
                out vec2 v_texCoords2;
                out vec2 v_texCoords3;
                out vec2 v_texCoords4;
                #define FBO_W %s.0
                #define FBO_H %s.0
                const vec2 futher = vec2(3.2307692308 / FBO_W, 3.2307692308 / FBO_H );
                const vec2 closer = vec2(1.3846153846 / FBO_W, 1.3846153846 / FBO_H );
                void main()
                {
                vec2 f = futher * dir;
                vec2 c = closer * dir;
                v_texCoords0 = a_texCoord - f;
                v_texCoords1 = a_texCoord - c;
                v_texCoords2 = a_texCoord;
                v_texCoords3 = a_texCoord + c;
                v_texCoords4 = a_texCoord + f;
                gl_Position = a_position;
                }
                """.formatted(FBO_W, FBO_H);
        final String fragmentShader = """
                #version 330
                
                #ifdef GL_ES
                precision lowp float;
                #define MED mediump
                #else
                #define MED\s
                #endif
                uniform sampler2D u_texture;
                in MED vec2 v_texCoords0;
                in MED vec2 v_texCoords1;
                in MED vec2 v_texCoords2;
                in MED vec2 v_texCoords3;
                in MED vec2 v_texCoords4;
                const float center = 0.2270270270;
                const float close  = 0.3162162162;
                const float far    = 0.0702702703;
                
                out vec4 fragColor;
                void main()
                {\t\s
                fragColor%s = far    * texture(u_texture, v_texCoords0)%s
                \t      \t\t+ close  * texture(u_texture, v_texCoords1)%s
                \t\t\t\t+ center * texture(u_texture, v_texCoords2)%s
                \t\t\t\t+ close  * texture(u_texture, v_texCoords3)%s
                \t\t\t\t+ far    * texture(u_texture, v_texCoords4)%s;
                }
                """.formatted(rgb, rgb, rgb, rgb, rgb, rgb);
		ShaderProgram.pedantic = false;
		ShaderProgram blurShader = new ShaderProgram(vertexShader,
				fragmentShader);
		if (!blurShader.isCompiled()) {
			if(!blurShader.isCompiled()){
				Gdx.app.log("ERROR", blurShader.getLog());
			}
		}

		return blurShader;
	}
}