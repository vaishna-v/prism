import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import Engine.Engine;

public class Main extends Application {

    private final ImageState state = new ImageState();
    private final Engine     eng   = new Engine();
    private final ImageView  imageView = new ImageView();

    // Status bar labels
    private final Label fileLabel = new Label("No image loaded");
    private final Label dimLabel  = new Label("");

    // Sliders
    private Slider brightnessSlider;
    private Slider contrastSlider;
    private Label  brightnessValLabel;
    private Label  contrastValLabel;

    @Override
    public void start(Stage stage) {


        // ── Toolbar buttons ───────────────────────────────────────────────────
        Button openBtn       = new Button("📂  Open Image");
        Button saveBtn       = new Button("💾  Save Image");
        Button transposeBtn  = new Button("Transpose");
        Button grayscaleBtn  = new Button("Grayscale");
        Button rotateBtn     = new Button("Rotate");
        Button contrastBtn   = new Button("Increase Contrast");
        Button brightnessBtn = new Button("Increase Brightness");

        HBox leftCluster  = new HBox(8, openBtn, saveBtn);
        leftCluster.setAlignment(Pos.CENTER_LEFT);

        HBox centreCluster = new HBox(6, transposeBtn, grayscaleBtn, rotateBtn,
                                         contrastBtn, brightnessBtn);
        centreCluster.setAlignment(Pos.CENTER);
        HBox.setHgrow(centreCluster, Priority.ALWAYS);

        HBox toolbar = new HBox(leftCluster, centreCluster);
        toolbar.setPadding(new Insets(8, 16, 8, 16));
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setStyle("-fx-background-color: #3c3f41; -fx-border-color: #555; -fx-border-width: 0 0 1 0;");

        // ── Image View ────────────────────────────────────────────────────────
        imageView.setFitWidth(1000);
        imageView.setFitHeight(600);
        imageView.setPreserveRatio(true);

        StackPane canvasPane = new StackPane(imageView);
        canvasPane.setAlignment(Pos.CENTER);
        canvasPane.setStyle("-fx-background-color: #383838;");
        canvasPane.setPadding(new Insets(20));

        ScrollPane scrollPane = new ScrollPane(canvasPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: #1e1e1e;");

        // ── Right Panel ───────────────────────────────────────────────────────
        Label adjTitle = new Label("Adjustments Panel");
        adjTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // Brightness slider
        brightnessValLabel = new Label("50%");
        Label bLabel = new Label("Brightness");
        HBox bHeader = new HBox(bLabel, new Region(), brightnessValLabel);
        HBox.setHgrow(bHeader.getChildren().get(1), Priority.ALWAYS);

        brightnessSlider = new Slider(0, 100, 50);
        brightnessSlider.setShowTickLabels(false);

        // Contrast slider
        contrastValLabel = new Label("50%");
        Label cLabel = new Label("Contrast");
        HBox cHeader = new HBox(cLabel, new Region(), contrastValLabel);
        HBox.setHgrow(cHeader.getChildren().get(1), Priority.ALWAYS);

        contrastSlider = new Slider(0, 100, 50);
        contrastSlider.setShowTickLabels(false);

        // Quick filter buttons
        Label filterTitle = new Label("Quick Filters");
        filterTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Button sepiaBtn   = filterTileBtn("Sepia");
        Button invertBtn  = filterTileBtn("Invert");
        Button vignetteBtn= filterTileBtn("Vignette");
        Button warmthBtn  = filterTileBtn("Warmth");

        GridPane filterGrid = new GridPane();
        filterGrid.setHgap(10);
        filterGrid.setVgap(10);
        filterGrid.add(sepiaBtn,    0, 0);
        filterGrid.add(invertBtn,   1, 0);
        filterGrid.add(vignetteBtn, 0, 1);
        filterGrid.add(warmthBtn,   1, 1);

        VBox rightPanel = new VBox(12,
            adjTitle,
            bHeader, brightnessSlider,
            cHeader, contrastSlider,
            new Separator(),
            filterTitle,
            filterGrid
        );
        rightPanel.setPadding(new Insets(16));
        rightPanel.setPrefWidth(210);
        rightPanel.setStyle("-fx-background-color: #3c3f41; -fx-border-color: #555; -fx-border-width: 0 0 0 1;");

        // ── Status Bar ────────────────────────────────────────────────────────
        Button undoBtn = new Button("↩  Undo");
        HBox statusBar = new HBox(fileLabel, new Region(), dimLabel, new Region(), undoBtn);
        HBox.setHgrow(statusBar.getChildren().get(1), Priority.ALWAYS);
        HBox.setHgrow(statusBar.getChildren().get(3), Priority.ALWAYS);
        statusBar.setPadding(new Insets(6, 16, 6, 16));
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.setStyle("-fx-background-color: #3c3f41; -fx-border-color: #555; -fx-border-width: 1 0 0 0;");

        // ── Root Layout ───────────────────────────────────────────────────────
        VBox topSection = new VBox(toolbar);

        BorderPane root = new BorderPane();
        root.setTop(topSection);
        root.setCenter(scrollPane);
        root.setRight(rightPanel);
        root.setBottom(statusBar);
        root.setStyle("-fx-background-color: #2b2b2b;");

        // ── Event Handlers ────────────────────────────────────────────────────

        openBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Open Image");
            fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png","*.jpg","*.jpeg","*.bmp"));
            File file = fc.showOpenDialog(stage);
            if (file != null) {
                try {
                    BufferedImage img = ImageIO.read(file);
                    state.setImage(img, file.getName());
                    imageView.setImage(state.getFXImage());
                    fileLabel.setText("File: " + file.getName());
                    dimLabel.setText(img.getWidth() + " x " + img.getHeight() + "px");
                } catch (Exception ex) {
                    fileLabel.setText("Error opening file.");
                }
            }
        });

        saveBtn.setOnAction(e -> {
            if (state.isEmpty()) { fileLabel.setText("No image to save."); return; }
            FileChooser fc = new FileChooser();
            fc.setTitle("Save Image");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Image", "*.png"));
            File file = fc.showSaveDialog(stage);
            if (file != null) {
                try {
                    ImageIO.write(state.getBufferedImage(), "png", file);
                    fileLabel.setText("Saved: " + file.getName());
                } catch (Exception ex) {
                    fileLabel.setText("Failed to save.");
                }
            }
        });

        transposeBtn.setOnAction(e  -> applyOp(() -> eng.transpose(state.getBufferedImage())));
        grayscaleBtn.setOnAction(e  -> applyOp(() -> eng.toGreyScale2(state.getBufferedImage())));
        rotateBtn.setOnAction(e     -> applyOp(() -> eng.rotate90AntiClockwise(state.getBufferedImage())));
        contrastBtn.setOnAction(e   -> applyOp(() -> eng.increaseContrast(state.getBufferedImage())));
        brightnessBtn.setOnAction(e -> applyOp(() -> eng.increaseBrightness(state.getBufferedImage())));

        sepiaBtn.setOnAction(e    -> applyOp(() -> eng.sepia(state.getBufferedImage())));
        invertBtn.setOnAction(e   -> applyOp(() -> eng.invert(state.getBufferedImage())));
        vignetteBtn.setOnAction(e -> applyOp(() -> eng.vignette(state.getBufferedImage())));
        warmthBtn.setOnAction(e   -> applyOp(() -> eng.warmth(state.getBufferedImage())));

        brightnessSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (state.isEmpty()) return;
            state.setCurrentImage(eng.setBrightness(state.getOriginalImage(), newVal.intValue()));
            imageView.setImage(state.getFXImage());
            brightnessValLabel.setText(newVal.intValue() + "%");
        });

        contrastSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (state.isEmpty()) return;
            state.setCurrentImage(eng.setContrast(state.getOriginalImage(), newVal.intValue()));
            imageView.setImage(state.getFXImage());
            contrastValLabel.setText(newVal.intValue() + "%");
        });

        undoBtn.setOnAction(e -> {
            if (state.canUndo()) {
                state.undo();
                imageView.setImage(state.getFXImage());
                fileLabel.setText("Undo applied.");
            } else {
                fileLabel.setText("Nothing to undo.");
            }
        });

        // ── Stage ─────────────────────────────────────────────────────────────
        stage.setTitle("Photo Editor Pro");
        stage.setScene(new Scene(root, 1280, 820));
        stage.show();
    }

    private void applyOp(ImageOperation op) {
        if (state.isEmpty()) { fileLabel.setText("No image loaded."); return; }
        state.pushUndo();
        BufferedImage result = op.apply();
        state.setImage(result, state.getFileName());
        imageView.setImage(state.getFXImage());
        dimLabel.setText(result.getWidth() + " x " + result.getHeight() + "px");
    }

    private static Button filterTileBtn(String label) {
        Button b = new Button(label);
        b.setPrefSize(85, 60);
        return b;
    }

    @FunctionalInterface
    interface ImageOperation {
        BufferedImage apply();
    }

    public static void main(String[] args) {
        launch();
    }
}
