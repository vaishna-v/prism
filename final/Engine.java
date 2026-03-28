package Engine;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Engine {


    // Save Image by providing its object, format and path
    public static void saveImage(BufferedImage img, String format, String path)
    {
        try {
            ImageIO.write(img, format, new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // Rotates image by 90 degrees
    public static BufferedImage rotate90AntiClockwise(BufferedImage img)
    {
        int height = img.getHeight(), width = img.getWidth();
        BufferedImage rotatedImage = new BufferedImage(height, width, img.getType());
        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                int rgb = img.getRGB(x, y);
                rotatedImage.setRGB(y, width-1-x, rgb);
            }
        }
        return rotatedImage;
    }

    // Transpose image
    public static BufferedImage transpose(BufferedImage img)
    {
        int height = img.getHeight(), width = img.getWidth();
        BufferedImage rotatedImage = new BufferedImage(height, width, img.getType());
        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                int rgb = img.getRGB(x, y);
                rotatedImage.setRGB(y, x, rgb);
            }
        }
        return rotatedImage;
    }


    public static BufferedImage toGreyScale2(BufferedImage img) {
        System.out.println("Converting to GreyScale2");
        BufferedImage greyImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = greyImage.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return greyImage;
    }

    // Increases brightness of each pixel by 5
public static BufferedImage increaseBrightness(BufferedImage img)
{
    int width = img.getWidth(), height = img.getHeight();
    BufferedImage result = new BufferedImage(width, height, img.getType());
    for (int x = 0; x < width; x++)
    {
        for (int y = 0; y < height; y++)
        {
            int rgb = img.getRGB(x, y);
            int a = (rgb >> 24) & 0xFF;
            int r = Math.min(((rgb >> 16) & 0xFF) + 5, 255);
            int g = Math.min(((rgb >> 8)  & 0xFF) + 5, 255);
            int b = Math.min(( rgb        & 0xFF) + 5, 255);
            result.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
        }
    }
    return result;
}

// Increases contrast by 5 using standard contrast formula
public static BufferedImage increaseContrast(BufferedImage img)
{
    int width = img.getWidth(), height = img.getHeight();
    BufferedImage result = new BufferedImage(width, height, img.getType());
    double factor = (259.0 * (5 + 255)) / (255.0 * (259 - 5));
    for (int x = 0; x < width; x++)
    {
        for (int y = 0; y < height; y++)
        {
            int rgb = img.getRGB(x, y);
            int a = (rgb >> 24) & 0xFF;
            int r = (int)((rgb >> 16) & 0xFF);
            int g = (int)((rgb >> 8)  & 0xFF);
            int b = (int)( rgb        & 0xFF);
            r = Math.min(Math.max((int)(factor * (r - 128) + 128), 0), 255);
            g = Math.min(Math.max((int)(factor * (g - 128) + 128), 0), 255);
            b = Math.min(Math.max((int)(factor * (b - 128) + 128), 0), 255);
            result.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
        }
    }
    return result;
}
}