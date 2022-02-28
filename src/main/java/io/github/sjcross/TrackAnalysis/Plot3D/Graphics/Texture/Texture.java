package io.github.sjcross.TrackAnalysis.Plot3D.Graphics.Texture;

import static com.jogamp.opengl.GL.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public abstract class Texture
{
    private final int id;
    protected int width;
    protected int height;

    protected abstract int getTarget();

    public Texture()
    {
        id = glGenTextures();
        bind();

        glTexParameteri(getTarget(), GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(getTarget(), GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexParameteri(getTarget(), GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(getTarget(), GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    }

    public void bind()
    {
        glBindTexture(getTarget(), id);
    }

    public void bindToUnit(int textureUnit)
    {
        SetActivateUnit(textureUnit);
        bind();
    }

    public static void SetActivateUnit(int textureUnit)
    {
        if (0 <= textureUnit && textureUnit <= glGetInteger(GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS))
        {
            glActiveTexture(GL_TEXTURE0 + textureUnit);
        }
    }

    public int getId()
    {
        return id;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public void dispose()
    {
        glDeleteTextures(id);
    }
}
