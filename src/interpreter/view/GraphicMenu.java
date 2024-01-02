package interpreter.view;

import inputmanager.InputManager;
import inputmanager.StringToStatementConverter;
import inputmanager.parser.ParseException;
import inputmanager.tokenizer.TokenizerException;
import interpreter.controller.Controller;
import interpreter.model.exceptions.TypecheckException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static interpreter.view.SourceGenerator.CODE;
import static interpreter.view.SourceGenerator.NAME;

public class GraphicMenu extends Application implements Menu {
    private static final StringToStatementConverter inputManager = new InputManager();
    private BorderPane pageLayout;
    private Scene scene;
    private TextField programNameField;
    private TextArea codeBox, outputBox;
    private Button submitButton, runButton, exitButton;
    private static final double pageWidth = 600;
    private static final double pageHeight = 600;
    private static final double defaultSpacing = 5;
    private final Map<String, String> programNameToSourceCode = new HashMap<>();
    private final Map<String, Controller> programNameToExecutable = new HashMap<>();
    private List<String[]> sources = SourceGenerator.makeList();
    private final AtomicInteger logFileCounter = new AtomicInteger(1);
    private ListView<String> runnableProgramsDisplayList;

    @Override
    public void show() {
        launch();
    }

    @Override
    public void start(Stage stage) {
        initSources();
        initNodes();
        addPrograms();
        setupPage(stage);
        addNodes();
        populateProgramList();
        registerHandlers();
        stage.show();
    }

    private void initSources() {
        sources = SourceGenerator.makeList();
    }

    private void addPrograms() {
        for (String[] source : sources) {
            Controller controller = parseAndCreateExecutableProgram(source[CODE], logFileCounter);
            if (controller == null)
                continue;
            programNameToSourceCode.put(source[NAME], source[CODE]);
            programNameToExecutable.put(source[NAME], controller);
        }
    }

    private void initNodes() {
        pageLayout = new BorderPane();
        scene = new Scene(pageLayout, pageWidth, pageHeight);
        codeBox = new TextArea();
        codeBox.setPromptText("input a program...");
        codeBox.wrapTextProperty().set(true);
        outputBox = new TextArea("output will appear here");
        outputBox.setEditable(false);
        outputBox.wrapTextProperty().set(true);
        programNameField = new TextField();
        programNameField.setPromptText("enter a name for the program!");
        submitButton = new Button("Submit");
        runButton = new Button("Run");
        exitButton = new Button("Exit");
        runnableProgramsDisplayList = new ListView<>();
    }

    private void setupPage(Stage stage) {
        stage.setScene(scene);
        stage.setTitle("Parsr");
        String src = "images\\icon.png";
        File file = new File(src);
        Image icon = new Image(file.toURI().toString());
        stage.getIcons().add(icon);
    }

    @SuppressWarnings("DuplicatedCode")
    private void addNodes() {
        HBox header = new HBox();
        header.setPadding(new Insets(2 * defaultSpacing));
        header.setSpacing(2 * defaultSpacing);
        ImageView headerImage = new ImageView(
                new Image(
                        new File("images\\header.png")
                                .toURI()
                                .toString()));
        headerImage.setAccessibleText("Pristine landscape");
        header.getChildren().add(headerImage);

        VBox main = new VBox();
        main.setPadding(new Insets(2 * defaultSpacing));
        main.setSpacing(2 * defaultSpacing);

        HBox topCenter = new HBox();
        topCenter.setSpacing(defaultSpacing * 3 / 2);
        topCenter.setAlignment(Pos.BOTTOM_CENTER);

        VBox left = new VBox();
        left.setSpacing(defaultSpacing);
        left.setAlignment(Pos.CENTER);
        codeBox.setPrefWidth(pageWidth / 2);
        codeBox.setPrefHeight(pageHeight * 2 / 3);
        programNameField.setPrefWidth(pageWidth / 2);
        submitButton.setPrefWidth(pageWidth / 2);
        left.getChildren().addAll(codeBox, programNameField, submitButton);
        topCenter.getChildren().add(left);

        VBox right = new VBox();
        right.setSpacing(defaultSpacing);
        right.setAlignment(Pos.CENTER);
        runnableProgramsDisplayList.setPrefWidth(pageWidth / 2);
        runnableProgramsDisplayList.setPrefHeight(pageHeight * 2 / 3);
        runButton.setPrefWidth(pageWidth / 2);
        right.getChildren().addAll(runnableProgramsDisplayList, runButton);
        topCenter.getChildren().add(right);
        main.getChildren().add(topCenter);

        VBox bottom = new VBox();
        bottom.setSpacing(defaultSpacing);
        bottom.setAlignment(Pos.BOTTOM_CENTER);
        outputBox.setPrefWidth(pageWidth);
        outputBox.setPrefHeight(pageWidth / 2);
        exitButton.setPrefWidth(pageWidth);
        bottom.getChildren().addAll(outputBox, exitButton);

        main.getChildren().add(bottom);
        pageLayout.setTop(header);
        pageLayout.setCenter(main);
    }

    private void populateProgramList() {
        runnableProgramsDisplayList.getItems().clear();
        for (String[] srcCode : sources) {
            runnableProgramsDisplayList.getItems().add(srcCode[NAME]);
        }
    }

    private void registerHandlers() {
        submitButton.setOnAction(event -> {
            String name = programNameField.getText();
            if (programNameToSourceCode.get(name) != null) {
                showErrorMessageBox("Duplicate program name");
                return;
            }
            String code = codeBox.getText();
            Controller program = parseAndCreateExecutableProgram(code, logFileCounter);
            if (program == null)
                return;
            programNameToExecutable.put(name, program);
            programNameToSourceCode.put(name, code);
            runnableProgramsDisplayList.getItems().add(name);
        });

        runButton.setOnAction(event -> {
            String name = runnableProgramsDisplayList.getSelectionModel().getSelectedItem();
            if (name == null) {
                showErrorMessageBox("No item selected");
                return;
            }
            String output = programNameToExecutable.get(name).takeAllSteps();
            programNameToExecutable.remove(name);
            programNameToSourceCode.remove(name);
            runnableProgramsDisplayList.getItems().remove(name);
            runnableProgramsDisplayList.getSelectionModel().clearSelection();
            sources = sources.stream().filter(p -> !Objects.equals(p[NAME], name)).toList();
            outputBox.setText(output);
        });

        runnableProgramsDisplayList.setOnMouseClicked(event -> {
            String programName = runnableProgramsDisplayList.getSelectionModel().getSelectedItem();
            if (programName == null)
                return;
            programNameField.setText(programName);
            codeBox.setText(programNameToSourceCode.get(programName));
        });

        exitButton.setOnAction(event -> Platform.exit());
    }

    private static Controller parseAndCreateExecutableProgram(String code, AtomicInteger logFileCounter) {
        try {
            return inputManager.program(code, logFileCounter.getAndIncrement());
        } catch (TypecheckException e) {
            showErrorMessageBox("Typechecking error: " + e.getMessage());
            return null;
        } catch (TokenizerException e) {
            showErrorMessageBox("Tokenizing error: " + e.getMessage());
            return null;
        } catch (ParseException e) {
            showErrorMessageBox("Parsing exception: " + e.getMessage());
            return null;
        }
    }

    private static void showErrorMessageBox(String warning) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(warning);
        alert.showAndWait();
    }

}
