import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.awt.image.BufferedImage;

import java.io.File;


import Engine.Engine;

public class Main extends Application {
    Image image;
    Engine eng = new Engine();


    private ImageView imageView = new ImageView();
    private Label statusLabel = new Label("No tool selected");
    private String activeTool = "None";

    @Override
    public void start(Stage stage) {



        // Add buttons to the UI-
        Button openBtn = new Button("Open Image");
        Button transposeButton = new Button("Transpose");
        Button grayScaleButton = new Button("GrayScale");
        Button rotateButton = new Button("Rotate");


        // Describe function to be called when button is pressed-
        openBtn.setOnAction(e -> openImage(stage));
        transposeButton.setOnAction(e -> transpose());
        grayScaleButton.setOnAction(e -> grayscale());
        rotateButton.setOnAction(e -> rotate());
        


        // Put button in containers-
        HBox buttons = new HBox(10, openBtn, transposeButton, grayScaleButton, rotateButton);
        VBox root = new VBox(10, buttons, imageView, statusLabel);


        // Render the scene
        Scene scene = new Scene(root, 1200, 800);
        stage.setTitle("Photo Editor");
        stage.setScene(scene);
        stage.show();
    }

    private void openImage(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");

        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            image = new Image(file.toURI().toString());
            imageView.setImage(image);
            statusLabel.setText("Image Loaded");
        }
    }

    private void selectTool(String tool) {
        activeTool = tool;
        statusLabel.setText("Active Tool: " + tool);
    }






    // Functions for when a button is pressed-

    private void transpose()
    {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        bufferedImage = eng.transpose(bufferedImage);
        image = SwingFXUtils.toFXImage(bufferedImage, null);
        imageView.setImage(image); 
    }
    private void grayscale()
    {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        bufferedImage = eng.toGreyScale2(bufferedImage);
        image = SwingFXUtils.toFXImage(bufferedImage, null);
        imageView.setImage(image); 
    }
    private void rotate()
    {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        bufferedImage = eng.rotate90AntiClockwise(bufferedImage);
        image = SwingFXUtils.toFXImage(bufferedImage, null);
        imageView.setImage(image); 
    }






    public static void main(String[] args) {
        launch();
    }
}