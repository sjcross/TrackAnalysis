package wbif.sjx.TrackAnalysis.Plot3D.Utils;

import wbif.sjx.TrackAnalysis.Plot3D.Graphics.Texture.Texture2D;

import javax.imageio.ImageIO;
import java.util.HashMap;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class TextureLoader {

    private static final HashMap<String, Texture2D> loadedTextureMap = new HashMap<>();

    private TextureLoader() {
    }

    public static Texture2D load(String filePath) throws Exception {
        if (filePath.matches(".*\\.(png|jpg)")) {
            if (loadedTextureMap.containsKey(filePath)) {
                return loadedTextureMap.get(filePath);
            }
            else {
                Texture2D texture = new Texture2D(ImageIO.read(TextureLoader.class.getResource(filePath)));
                loadedTextureMap.put(filePath, texture);
                return texture;
            }
        }
        else {
            throw new Exception(String.format("Cant load %s, file type not supported", filePath));
        }
    }
}
