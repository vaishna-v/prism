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
}