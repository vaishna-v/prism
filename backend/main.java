import imageEngine.Engine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class Main
{
    public static void main(String[] args)
    {
        System.out.println("YES");


        BufferedImage image = null;
        // for printing new file dimension-
        try {
            File input = new File("images/input.jpg");

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


        System.out.println("Rotating Image by 90 degrees");
        BufferedImage rotatedImage = Engine.rotate90AntiClockwise(image);
        Engine.saveImage(rotatedImage, "png", "images/rotatedImage.png");


        System.out.println("Transposing Image-");
        BufferedImage transposedImage = Engine.transpose(image);
        Engine.saveImage(transposedImage, "png", "images/transposeImage.png");



    }
}