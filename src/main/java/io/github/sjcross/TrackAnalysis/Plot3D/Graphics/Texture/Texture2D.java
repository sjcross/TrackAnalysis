package io.github.sjcross.TrackAnalysis.Plot3D.Graphics.Texture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class Texture2D extends Texture {

    @Override
    protected int getTarget() {
        return GL_TEXTURE_2D;
    }

    public Texture2D(BufferedImage bufferedImage) {
        width = bufferedImage.getWidth();
        height = bufferedImage.getHeight();

        // Load texture contents into a byte buffer
        ByteBuffer buffer = ByteBuffer.allocateDirect(3 * width * height);

        // Push RGBA to byteBuffer
        int[] pixels = new int[width * height];
        bufferedImage.getRGB(0, 0, width, height, pixels, 0, width);

        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                int pixel = pixels[y * width + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));  // Green component
                buffer.put((byte) (pixel & 0xFF));         // Blue component
            }
        }

        buffer.flip();

        bind();
        glTexImage2D(getTarget(), 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, buffer);
        glGenerateMipmap(getTarget());
    }

    public static Texture2D loadFromImage(String filePath) throws Exception {
        if (!filePath.matches(".*\\.(png|jpg)"))
            throw new Exception(String.format("Cant load %s, file type not supported", filePath));

        return new Texture2D(ImageIO.read(Texture2D.class.getResource(filePath)));
    }
}
