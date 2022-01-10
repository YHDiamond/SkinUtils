package dev.yhdiamond.skinutils;

import dev.yhdiamond.skinutils.exception.IncorrectlySizedSkinException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

import static dev.yhdiamond.skinutils.SkinUtils.getUUIDFromUsername;

public class Skin {

    private static BufferedImage i;

    private Skin(BufferedImage input) throws IncorrectlySizedSkinException {
        checkImageSize(input);
        this.i = input;
    }

    public static Skin fromBufferedImage(BufferedImage image) throws IncorrectlySizedSkinException {
        return new Skin(image);
    }

    public static Skin fromUsername(String username) throws IncorrectlySizedSkinException, IOException {
        return fromUUID(getUUIDFromUsername(username));
    }

    public static Skin fromUUID(String uuid) throws IncorrectlySizedSkinException, IOException {
        return fromUUID(UUID.fromString(uuid));
    }

    public static Skin fromUUID(UUID uuid) throws IOException, IncorrectlySizedSkinException {
        return fromURL(SkinUtils.getSkinURLFromUUID(uuid));
    }

    public static Skin fromURL(URL url) throws IOException, IncorrectlySizedSkinException {
        return new Skin(ImageIO.read(url));
    }

    public static Skin fromURL(String url) throws IOException, IncorrectlySizedSkinException {
        return fromURL(new URL(url));
    }

    public static Skin fromFile(File file) throws IOException, IncorrectlySizedSkinException {
        return new Skin(ImageIO.read(file));
    }

    public static Skin fromInputStream(InputStream is) throws IOException, IncorrectlySizedSkinException {
        return new Skin(ImageIO.read(is));
    }

    public Skin addLayer(Skin skin) throws IncorrectlySizedSkinException {
        BufferedImage current = this.i;
        Graphics graphics = i.getGraphics();
        graphics.drawImage(skin.getBufferedImage(), 0, 0, null);
        return new Skin(current);
    }

    public Skin addLayers(Skin... skins) throws IncorrectlySizedSkinException {
        Skin output = this;
        for (Skin skin : skins) {
            output = output.addLayer(skin);
        }
        return output;
    }

    public BufferedImage getBufferedImage() {
        return this.i;
    }

    private static void checkImageSize(BufferedImage image) throws IncorrectlySizedSkinException {
        if (image.getWidth() == 64 && image.getHeight() == 64) return;
        throw new IncorrectlySizedSkinException();
    }
}
