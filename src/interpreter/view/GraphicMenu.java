package interpreter.view;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

public class GraphicMenu extends Application implements Menu{
    Group page;

    private void setupPage(Stage stage){
        page = new Group();
        Scene scene = new Scene(page, 400, 400);
        stage.setScene(scene);
        stage.setTitle("JavaFX GUI Example");
        String src = "images\\icon.png";
        File file = new File(src);
        Image icon = new Image(file.toURI().toString());
        stage.getIcons().add(icon);

    }
    @Override
    public void start(Stage stage) {
        setupPage(stage);
        stage.show();
    }

    @Override
    public void show() {
        launch();
    }
}
