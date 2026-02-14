import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Main extends Application {

    String buttonStyle = "-fx-background-color: #8B5CF6;" +
            "-fx-text-fill: white;" +
            "-fx-pref-height: 35px;";


    private HBox createTopBar() {
        HBox topBar = new HBox(10);
        topBar.setStyle("-fx-background-color: #14101F;");
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_LEFT);

        Button home = new Button("Home");
        Button edit = new Button("Edit");

        home.setStyle(buttonStyle);
        edit.setStyle(buttonStyle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button undo = new Button("");
        Button redo = new Button("↪");

        // adding an image to undo button---------------------------------------------------------------------------

        Image undoImg = new Image(getClass().getResource("/icons/undo.png").toExternalForm());
        ImageView undoIcon = new ImageView(undoImg);
        undoIcon.setFitWidth(24);
        undoIcon.setFitHeight(24);
        undo.setGraphic(undoIcon);

        // -------------------------------------------------------------------------------------------------------

        undo.setStyle(buttonStyle);
        redo.setStyle(buttonStyle);

        topBar.getChildren().addAll(home, edit, spacer, undo, redo);

        return topBar;
    }

    private VBox createLeftBar()
    {
        VBox leftBar = new VBox(20);
        leftBar.setStyle("-fx-background-color: #14101F;");
        leftBar.setPadding(new Insets(10));
        leftBar.setAlignment(Pos.TOP_CENTER);

        // i need to add the following buttons(icon)-
        // icon: telegram, magic, brush, eraser, crop, move, colorpicker, text

        // ---------------------------- telegram--------------------------------

        Button telegram = new Button("");
        Image telegramImg = new Image(getClass().getResource("/icons/telegram.png").toExternalForm());
        ImageView telegramIcon = new ImageView(telegramImg);
        telegramIcon.setFitWidth(24);
        telegramIcon.setFitHeight(24);
        telegram.setGraphic(telegramIcon);

        // ----------------------------------------------------------------------------

        // ---------------------------- magic--------------------------------

        Button magic = new Button("");
        Image magicImg = new Image(getClass().getResource("/icons/magic.png").toExternalForm());
        ImageView magicIcon = new ImageView(magicImg);
        magicIcon.setFitWidth(24);
        magicIcon.setFitHeight(24);
        magic.setGraphic(magicIcon);

        // ----------------------------------------------------------------------------


        // ---------------------------- brush --------------------------------

        Button brush = new Button("");
        Image brushImg = new Image(getClass().getResource("/icons/brush.png").toExternalForm());
        ImageView brushIcon = new ImageView(brushImg);
        brushIcon.setFitWidth(24);
        brushIcon.setFitHeight(24);
        brush.setGraphic(brushIcon);

        // ----------------------------------------------------------------------------





        telegram.setStyle("buttonStyle");
        leftBar.getChildren().addAll(telegram, magic, brush);

        return leftBar;
    }
    @Override
    public void start(Stage stage) {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1A1625;");

        // call separate builder
        root.setTop(createTopBar());
        root.setLeft(createLeftBar());
        Scene scene = new Scene(root, 900, 600);

        stage.setTitle("Modular UI");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
