package interpreter.view;

import inputmanager.InputManager;
import inputmanager.StringToStatementConverter;
import inputmanager.parser.ParseException;
import inputmanager.tokenizer.TokenizerException;
import interpreter.controller.Controller;
import interpreter.controller.ControllerImplementation;
import interpreter.model.exceptions.TypecheckException;
import interpreter.model.programstate.ProgramState;
import interpreter.model.programstate.ProgramStateImplementation;
import interpreter.model.statements.Statement;
import interpreter.model.symboltable.SymbolTableHashMap;
import interpreter.repository.Repository;
import interpreter.repository.RepositoryVector;
import interpreter.view.commands.RunProgramCommand;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class GraphicMenu extends Application implements Menu{
    private BorderPane pageLayout;
    private Scene scene;
    private TextArea codeBox, outputBox;
    private Button submitButton, runButton;
    private ListView<String> programs;
    private static final StringToStatementConverter inputManager = new InputManager();
    private List<String> sources;
    private final Map<String, Command> commands = new HashMap<>();

    private void initNodes(){
        pageLayout = new BorderPane();
        scene = new Scene(pageLayout, 400, 400);
        codeBox = new TextArea();
        outputBox = new TextArea();
        programs = new ListView<>();
        submitButton = new Button("Submit");
        runButton = new Button("Run");
    }

    private void setupPage(Stage stage){
        stage.setScene(scene);
        stage.setTitle("Parsr");
        String src = "images\\icon.png";
        File file = new File(src);
        Image icon = new Image(file.toURI().toString());
        stage.getIcons().add(icon);
    }
    @Override
    public void start(Stage stage) {
        initSources();
        initNodes();
        setupPage(stage);
        addNodes();
        stage.show();
    }

    private void initSources() {
        sources = new Vector<>();
        SourceGenerator.populateList(sources);
        addPrograms();

    }

    private void addNodes() {
        VBox main = new VBox();
        main.setPadding(new Insets(10));
        main.setSpacing(10);

        HBox top = new HBox();
        top.setSpacing(5);
        top.setAlignment(Pos.TOP_LEFT);
        codeBox.setPrefWidth(200);
        programs.setPrefWidth(200);
        programs.getItems().addAll("bla", "bla", "bla");
        top.getChildren().addAll(codeBox, programs);
        main.getChildren().add(top);

        HBox center = new HBox();
        center.setSpacing(5);
        center.setAlignment(Pos.CENTER);
        center.getChildren().addAll(submitButton, runButton);
        main.getChildren().add(center);

        HBox bottom = new HBox();
        bottom.setSpacing(5);
        bottom.setAlignment(Pos.BOTTOM_LEFT);
        bottom.getChildren().add(outputBox);

        main.getChildren().add(bottom);
        pageLayout.setCenter(main);
    }
    public void addCommand(String command, Command effect) {
        commands.put(command, effect);
    }
    private void addPrograms(){
        AtomicInteger counter = new AtomicInteger(1);
        for(String source:sources){
            Controller controller = inputManager.program(source, counter.get());
            if(controller == null) {
                continue;
            }
            this.addCommand(String.valueOf(counter.get()), new RunProgramCommand(String.valueOf(counter.get()), source, controller));
            counter.getAndIncrement();
        }
    }

    @Override
    public void show() {
        launch();
    }
}
