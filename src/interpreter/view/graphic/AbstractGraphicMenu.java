package interpreter.view.graphic;

import inputmanager.InputManager;
import inputmanager.StringAndStatementInputManager;
import interpreter.view.Menu;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractGraphicMenu extends Application implements Menu {
    protected BorderPane pageLayout;
    protected Scene scene;
    protected static final InputManager inputManager = new StringAndStatementInputManager();
    protected static final double pageWidth = 800;
    protected static final double pageHeight = 800;
    protected static final double defaultSpacing = 5;
    protected final AtomicInteger logFileCounter = new AtomicInteger(1);

    protected static void showErrorMessageBox(String warning) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(warning);
        alert.showAndWait();
    }

    @Override
    public void start(Stage stage) {
        initNodes();
        addPrograms();
        setupPage(stage);
        populateProgramList();
        registerHandlers();
        stage.show();
    }

    abstract protected void registerHandlers();

    abstract protected void populateProgramList();

    protected void setupPage(Stage stage) {
        stage.setScene(scene);
        stage.setTitle("Parsr");
        stage.getIcons().add(imageFromPath("images\\icon.png"));
    }

    protected abstract void addPrograms();

    protected void initNodes(){
        pageLayout = new BorderPane();
        scene = new Scene(pageLayout, pageWidth, pageHeight);
    }

    protected static Image imageFromPath(String path) {
        File f = new File(path);
        return new Image(f.toURI().toString());
    }
}
