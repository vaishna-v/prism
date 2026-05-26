package Engine;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Engine {

    public static void saveImage(BufferedImage img, String format, String path) {
        try {
            ImageIO.write(img, format, new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage rotate90AntiClockwise(BufferedImage img) {
        int h = img.getHeight(), w = img.getWidth();
        BufferedImage out = new BufferedImage(h, w, img.getType());
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++)
                out.setRGB(y, w - 1 - x, img.getRGB(x, y));
        return out;
    }

    public static BufferedImage transpose(BufferedImage img) {
        int h = img.getHeight(), w = img.getWidth();
        BufferedImage out = new BufferedImage(h, w, img.getType());
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++)
                out.setRGB(y, x, img.getRGB(x, y));
        return out;
    }

    public static BufferedImage toGreyScale2(BufferedImage img) {
        BufferedImage grey = new BufferedImage(img.getWidth(), img.getHeight(),
                                               BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = grey.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return grey;
    }

    public static BufferedImage increaseBrightness(BufferedImage img) {
        return setBrightness(img, 55);
    }

    public static BufferedImage increaseContrast(BufferedImage img) {
        return setContrast(img, 55);
    }

    // Brightness slider: 0-100, 50 = no change
    public static BufferedImage setBrightness(BufferedImage img, int percent) {
        int delta = (percent - 50) * 2;
        int w = img.getWidth(), h = img.getHeight();
        BufferedImage out = new BufferedImage(w, h, safeType(img));
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                int rgb = img.getRGB(x, y);
                int a = (rgb >> 24) & 0xFF;
                int r = clamp(((rgb >> 16) & 0xFF) + delta);
                int g = clamp(((rgb >>  8) & 0xFF) + delta);
                int b = clamp(( rgb        & 0xFF) + delta);
                out.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        return out;
    }

    // Contrast slider: 0-100, 50 = no change
    public static BufferedImage setContrast(BufferedImage img, int percent) {
        int delta = percent - 50;
        double factor = (259.0 * (delta + 255)) / (255.0 * (259 - delta));
        int w = img.getWidth(), h = img.getHeight();
        BufferedImage out = new BufferedImage(w, h, safeType(img));
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                int rgb = img.getRGB(x, y);
                int a = (rgb >> 24) & 0xFF;
                int r = clamp((int)(factor * (((rgb >> 16) & 0xFF) - 128) + 128));
                int g = clamp((int)(factor * (((rgb >>  8) & 0xFF) - 128) + 128));
                int b = clamp((int)(factor * (( rgb        & 0xFF) - 128) + 128));
                out.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        return out;
    }

    public static BufferedImage sepia(BufferedImage img) {
        int w = img.getWidth(), h = img.getHeight();
        BufferedImage out = new BufferedImage(w, h, safeType(img));
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                int rgb = img.getRGB(x, y);
                int a  = (rgb >> 24) & 0xFF;
                int ri = (rgb >> 16) & 0xFF;
                int gi = (rgb >>  8) & 0xFF;
                int bi =  rgb        & 0xFF;
                int ro = clamp((int)(ri * 0.393 + gi * 0.769 + bi * 0.189));
                int go = clamp((int)(ri * 0.349 + gi * 0.686 + bi * 0.168));
                int bo = clamp((int)(ri * 0.272 + gi * 0.534 + bi * 0.131));
                out.setRGB(x, y, (a << 24) | (ro << 16) | (go << 8) | bo);
            }
        return out;
    }

    public static BufferedImage invert(BufferedImage img) {
        int w = img.getWidth(), h = img.getHeight();
        BufferedImage out = new BufferedImage(w, h, safeType(img));
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                int rgb = img.getRGB(x, y);
                int a = (rgb >> 24) & 0xFF;
                int r = 255 - ((rgb >> 16) & 0xFF);
                int g = 255 - ((rgb >>  8) & 0xFF);
                int b = 255 - ( rgb        & 0xFF);
                out.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        return out;
    }

    public static BufferedImage vignette(BufferedImage img) {
        int w = img.getWidth(), h = img.getHeight();
        BufferedImage out = new BufferedImage(w, h, safeType(img));
        double cx = w / 2.0, cy = h / 2.0;
        double maxDist = Math.sqrt(cx * cx + cy * cy);
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                int rgb = img.getRGB(x, y);
                int a = (rgb >> 24) & 0xFF;
                double factor = 1.0 - 0.75 * (Math.sqrt((x-cx)*(x-cx)+(y-cy)*(y-cy)) / maxDist);
                int r = clamp((int)(((rgb >> 16) & 0xFF) * factor));
                int g = clamp((int)(((rgb >>  8) & 0xFF) * factor));
                int b = clamp((int)(( rgb        & 0xFF) * factor));
                out.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        return out;
    }

    public static BufferedImage warmth(BufferedImage img) {
        int w = img.getWidth(), h = img.getHeight();
        BufferedImage out = new BufferedImage(w, h, safeType(img));
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                int rgb = img.getRGB(x, y);
                int a = (rgb >> 24) & 0xFF;
                int r = clamp(((rgb >> 16) & 0xFF) + 30);
                int g = clamp(((rgb >>  8) & 0xFF) + 10);
                int b = clamp(( rgb        & 0xFF) - 20);
                out.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        return out;
    }

    private static int clamp(int v) { return Math.max(0, Math.min(255, v)); }

    private static int safeType(BufferedImage img) {
        return (img.getType() == BufferedImage.TYPE_BYTE_GRAY ||
                img.getType() == BufferedImage.TYPE_CUSTOM)
               ? BufferedImage.TYPE_INT_ARGB : img.getType();
    }
}
