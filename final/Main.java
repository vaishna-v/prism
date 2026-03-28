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
import javax.imageio.ImageIO;

import Engine.Engine;

public class Main extends Application {

    private Image image;
    private final Engine eng = new Engine();
    private final ImageView imageView = new ImageView();
    private final Label statusLabel = new Label("No image loaded");

    public void start(Stage stage) {
        imageView.setFitWidth(1100);
        imageView.setFitHeight(650);
        imageView.setPreserveRatio(true);

        Button openBtn       = new Button("Open Image");
        Button transposeBtn  = new Button("Transpose");
        Button grayscaleBtn  = new Button("Grayscale");
        Button rotateBtn     = new Button("Rotate");
        Button contrastBtn   = new Button("Increase Contrast");
        Button brightnessBtn = new Button("Increase Brightness");
        Button saveBtn       = new Button("Save Image");

        openBtn.setOnAction(e -> openImage(stage));
        transposeBtn.setOnAction(e -> transpose());
        grayscaleBtn.setOnAction(e -> grayscale());
        rotateBtn.setOnAction(e -> rotate());
        contrastBtn.setOnAction(e -> increaseContrast());
        brightnessBtn.setOnAction(e -> increaseBrightness());
        saveBtn.setOnAction(e -> saveImage(stage));

        HBox toolbar = new HBox(10, openBtn, transposeBtn, grayscaleBtn, rotateBtn, contrastBtn, brightnessBtn, saveBtn);
        toolbar.setAlignment(javafx.geometry.Pos.CENTER);

        VBox root = new VBox(10, toolbar, imageView, statusLabel);
        root.setAlignment(javafx.geometry.Pos.CENTER);

        stage.setTitle("Photo Editor");
        stage.setScene(new Scene(root, 1200, 800));
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

    private void saveImage(Stage stage) {
        if (image == null) { statusLabel.setText("No image to save."); return; }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Image", "*.png"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                ImageIO.write(bufferedImage, "png", file);
                statusLabel.setText("Image saved: " + file.getName());
            } catch (Exception ex) {
                statusLabel.setText("Failed to save image.");
            }
        }
    }

    private void transpose() {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        bufferedImage = eng.transpose(bufferedImage);
        image = SwingFXUtils.toFXImage(bufferedImage, null);
        imageView.setImage(image);
    }

    private void grayscale() {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        bufferedImage = eng.toGreyScale2(bufferedImage);
        image = SwingFXUtils.toFXImage(bufferedImage, null);
        imageView.setImage(image);
    }

    private void rotate() {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        bufferedImage = eng.rotate90AntiClockwise(bufferedImage);
        image = SwingFXUtils.toFXImage(bufferedImage, null);
        imageView.setImage(image);
    }

    private void increaseContrast() {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        bufferedImage = eng.increaseContrast(bufferedImage);
        image = SwingFXUtils.toFXImage(bufferedImage, null);
        imageView.setImage(image);
    }

    private void increaseBrightness() {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        bufferedImage = eng.increaseBrightness(bufferedImage);
        image = SwingFXUtils.toFXImage(bufferedImage, null);
        imageView.setImage(image);
    }

    public static void main(String[] args) 
    { 
        launch(); 
    }
}