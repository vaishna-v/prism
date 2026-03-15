package imageEngine;

import javax.imageio.ImageIO;
import java.awt.Graphics;
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

            if (image == null) {
                System.out.println("Failed to read image: unsupported format or empty file.");
                return;
            }

            System.out.println("Width: " + image.getWidth());
            System.out.println("Height: " + image.getHeight());

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }




        // for accessing one pixel value-
        int x = 100;
        int y = 50;
        if (x < 0 || y < 0 || x >= image.getWidth() || y >= image.getHeight()) {
            System.out.println("Requested pixel is out of image bounds.");
            return;
        }
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

        BufferedImage greyImage = toGreyScale2(image);
        try {
            ImageIO.write(greyImage, "png", new File("output_grayscale.png"));
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

    public static BufferedImage toGreyScale2(BufferedImage img) {
        System.out.println("Converting to GreyScale2");
        BufferedImage greyImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = greyImage.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return greyImage;
    }
}