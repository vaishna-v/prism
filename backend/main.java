import imageEngine.Engine;
import java.util.Scanner;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class Main
{


    public static void CLIPhotoEditor()
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to CLI-PHOTO EDITOR");

        // Input an image-
        BufferedImage image = null;
        File input = null;
        while(true) {
            System.out.print("\t\tPlease provide the path to photo: ");
            String path = sc.next();
            try
            {
                input = new File(path);
                image = ImageIO.read(input);
                break;
            }
            catch(IOException e)
            {
                System.out.println("Failed to load the image. \n\n ");
            }
        }

        System.out.println("\n\t\tImage Loaded succesfully.");
        int count = 0;
        boolean startProgram = true;
        while(startProgram)
        {
            String choices = "\n\n\t\t1: Rotate Image\n\t\t2: Transpose Image\n\t\t3: Convert to GrayScale\n\t\t4(default)s: Exit\n\n\t\t\t\tEnter choice: ";
            System.out.print(choices);
            int choice = sc.nextInt();

            switch(choice) {
                case 1:
                    System.out.println("Rotating Image by 90 degrees");
                    BufferedImage rotatedImage = Engine.rotate90AntiClockwise(image);
                    Engine.saveImage(rotatedImage, "png", "output" + count + ".png");
                    break;
                case 2:
                    System.out.println("Transposing Image-");
                    BufferedImage transposedImage = Engine.transpose(image);
                    Engine.saveImage(transposedImage, "png", "output" + count + ".png");
                    break;
                case 3:
                    System.out.println("Converting to GrayScale-");
                    BufferedImage grayScaleImage = Engine.toGreyScale2(image);
                    Engine.saveImage(grayScaleImage, "png", "output" + count + ".png");
                    break;
                default:
                    startProgram = false;
        }
            count++;
        }




    }
    public static void main(String[] args)
    {
        System.out.println("YES");

        CLIPhotoEditor();


        /*
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

        */

    }
}