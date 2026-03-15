package imageEngine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class engine {

    public static void main(String[] args) {

        BufferedImage image = null;
        // for printing new file dimension-
        try {
            File input = new File("input.jpg");

            image = ImageIO.read(input);

            System.out.println("Width: " + image.getWidth());
            System.out.println("Height: " + image.getHeight());

        } catch (IOException e) {
            e.printStackTrace();
        }




        // for accessing one pixel value-
        int x = 100;
        int y = 50;
        int rgb = image.getRGB(x, y);
        int red   = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8)  & 0xFF;
        int blue  = rgb & 0xFF;

        System.out.println("R: " + red);
        System.out.println("G: " + green);
        System.out.println("B: " + blue);



        // change format-

        try {
            ImageIO.write(image, "png", new File("output_PNG.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        // my own program : changing color

        int width = image.getWidth();
        int height  = image.getHeight();
        for(x = 0; x<width; x++)
        {
            for(y = 0; y<height; y++)
            {

                rgb = image.getRGB(x, y);

                int alpha = (rgb >> 24) & 0xFF;
                red   = (rgb >> 16) & 0xFF;
                green = (rgb >> 8)  & 0xFF;
                blue  = rgb & 0xFF;

                // increase red
                int newRed = Math.min(255, blue);
                int newGreen = Math.min(255, red);
                int newBlue = Math.min(255, green);

                int newRgb = (alpha << 24) |
                        (newRed << 16) |
                        (newGreen << 8)   |
                        newBlue;

                image.setRGB(x, y, newRgb);
            }
        }

        try {
            ImageIO.write(image, "png", new File("output_switchedColors.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}