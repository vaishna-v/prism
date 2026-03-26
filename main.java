import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;

public class Main extends Application {

    private ImageView imageView = new ImageView();
    private Label statusLabel = new Label("No tool selected");
    private String activeTool = "None";

    @Override
    public void start(Stage stage) {

        Button openBtn = new Button("Open Image");
        Button cropBtn = new Button("Crop");
        Button rotateBtn = new Button("Rotate");
        Button brightnessBtn = new Button("Brightness");

        openBtn.setOnAction(e -> openImage(stage));
        cropBtn.setOnAction(e -> selectTool("Crop"));
        rotateBtn.setOnAction(e -> selectTool("Rotate"));
        brightnessBtn.setOnAction(e -> selectTool("Brightness"));

        VBox root = new VBox(10, openBtn, cropBtn, rotateBtn, brightnessBtn, imageView, statusLabel);

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
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
            statusLabel.setText("Image Loaded");
        }
    }

    private void selectTool(String tool) {
        activeTool = tool;
        statusLabel.setText("Active Tool: " + tool);
    }

    public static void main(String[] args) {
        launch();
    }
}