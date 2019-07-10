package wbif.sjx.TrackAnalysis.Plot3D.Graphics.Texture;

import ij.ImagePlus;

import java.nio.ByteBuffer;

import static com.jogamp.opengl.GL2ES3.GL_TEXTURE_2D_ARRAY;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL12.glTexImage3D;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class Texture2DArray extends Texture {

    private int frames;

    @Override
    protected int getTarget() {
        return GL_TEXTURE_2D_ARRAY;
    }

    public Texture2DArray(ImagePlus ipl, int xi, int yi, int w, int h) {
        width = w;
        height = h;
        frames = ipl.getNFrames();

        // Load texture contents into a byte buffer
        ByteBuffer buffer = ByteBuffer.allocateDirect(3 * width * height * frames);
        int[] pixels;

        final int originalIplPosition = ipl.getFrame();

        for (int f = 0; f < frames; f++) {
            ipl.setPosition(f + 1);

            // Push RGBA to byteBuffer
            pixels = new int[width * height];
            ipl.getBufferedImage().getRGB(xi, yi, width, height, pixels, 0, width);

            for (int y = height - 1; y >= 0; y--) {
                for (int x = 0; x < width; x++) {
                    int pixel = pixels[y * width + x];
                    buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
                    buffer.put((byte) ((pixel >> 8) & 0xFF));  // Green component
                    buffer.put((byte) (pixel & 0xFF));         // Blue component
                }
            }
        }

        ipl.setPosition(originalIplPosition);

        buffer.flip();

        bind();
        glTexImage3D(getTarget(), 0, GL_RGB, width, height, frames, 0, GL_RGB, GL_UNSIGNED_BYTE, buffer);
    }

    public int getFrames() {
        return frames;
    }
}
