import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;

public class Main extends Application {

    private Image image;
    private final ImageView imageView = new ImageView();
    private final Label statusLabel = new Label("No image loaded");

    @Override
    public void start(Stage stage) {

        // Configure image view
        imageView.setFitWidth(1100);
        imageView.setFitHeight(650);
        imageView.setPreserveRatio(true);

        // Buttons (same as final UI)
        Button openBtn       = new Button("Open Image");
        Button transposeBtn  = new Button("Transpose");
        Button grayscaleBtn  = new Button("Grayscale");
        Button rotateBtn     = new Button("Rotate");
        Button contrastBtn   = new Button("Increase Contrast");
        Button brightnessBtn = new Button("Increase Brightness");
        Button saveBtn       = new Button("Save Image");

        // Button actions (UI-only, no backend)
        openBtn.setOnAction(e -> openImage(stage));

        transposeBtn.setOnAction(e -> updateStatus("Transpose selected"));
        grayscaleBtn.setOnAction(e -> updateStatus("Grayscale selected"));
        rotateBtn.setOnAction(e -> updateStatus("Rotate selected"));
        contrastBtn.setOnAction(e -> updateStatus("Increase Contrast selected"));
        brightnessBtn.setOnAction(e -> updateStatus("Increase Brightness selected"));
        saveBtn.setOnAction(e -> updateStatus("Save action triggered"));

        // Toolbar (same layout as final)
        HBox toolbar = new HBox(10,
                openBtn,
                transposeBtn,
                grayscaleBtn,
                rotateBtn,
                contrastBtn,
                brightnessBtn,
                saveBtn
        );
        toolbar.setAlignment(javafx.geometry.Pos.CENTER);

        // Root layout
        VBox root = new VBox(10, toolbar, imageView, statusLabel);
        root.setAlignment(javafx.geometry.Pos.CENTER);

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
            statusLabel.setText("Image loaded: " + file.getName());
        }
    }

    private void updateStatus(String message) {
        if (image == null) {
            statusLabel.setText("Load an image first!");
        } else {
            statusLabel.setText(message);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}