package interpreter.view;

import inputmanager.InputManager;
import inputmanager.StringToStatementConverter;
import inputmanager.parser.ParseException;
import inputmanager.tokenizer.TokenizerException;
import interpreter.controller.Controller;
import interpreter.model.exceptions.TypecheckException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
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
    private Button submitButton, runButton;
    private static final double pageWidth = 600;
    private static final double pageHeight = 400;
    private static final double defaultSpacing = 5;
    private final Map<String, String> nameToCode = new HashMap<>();
    private final Map<String, Controller> nameToProgram = new HashMap<>();
    private List<String[]> sources = SourceGenerator.makeList();
    private final AtomicInteger counter = new AtomicInteger(1);
    private ListView<String> programList;

    @Override
    public void show() {
        launch();
    }
    @Override
    public void start(Stage stage) {
        initSources();
        initNodes();
        setupPage(stage);
        addNodes();
        populateProgramList();
        registerHandlers();
        stage.show();
    }

    private void initSources() {
        sources = SourceGenerator.makeList();
        addPrograms();
    }
    private void addPrograms() {
        for (String[] source : sources) {
            Controller controller;
            try {
                controller = inputManager.program(source[CODE], counter.getAndIncrement());
            } catch (TypecheckException | TokenizerException | ParseException err) {
                showErrorMessageBox(err.getMessage());
                continue;
            }
            if (nameToCode.get(source[NAME]) != null) {
                showErrorMessageBox("Program names must be unique!");
                continue;
            }
            nameToCode.put(source[NAME], source[CODE]);
            nameToProgram.put(source[NAME], controller);
        }
    }
    private void initNodes() {
        pageLayout = new BorderPane();
        scene = new Scene(pageLayout, pageWidth, pageHeight);
        codeBox = new TextArea();
        codeBox.setPromptText("input a program...");
        outputBox = new TextArea();
        outputBox.setPromptText("output will appear here");
        outputBox.setEditable(false);
        programNameField = new TextField();
        programNameField.setPromptText("enter a program name");
        submitButton = new Button("Submit");
        runButton = new Button("Run");
        programList = new ListView<>();
    }
    private void setupPage(Stage stage) {
        stage.setScene(scene);
        stage.setTitle("Parsr");
        String src = "images\\icon.png";
        File file = new File(src);
        Image icon = new Image(file.toURI().toString());
        stage.getIcons().add(icon);
    }
    private void addNodes() {
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
        left.getChildren().addAll(codeBox, programNameField, submitButton);
        topCenter.getChildren().add(left);

        VBox right = new VBox();
        right.setSpacing(defaultSpacing);
        right.setAlignment(Pos.CENTER);
        programList.setPrefWidth(pageWidth / 2);
        programList.setPrefHeight(pageHeight * 2 / 3);
        right.getChildren().addAll(programList, runButton);
        topCenter.getChildren().add(right);
        main.getChildren().add(topCenter);

        HBox bottom = new HBox();
        bottom.setSpacing(defaultSpacing);
        bottom.setAlignment(Pos.BOTTOM_CENTER);
        bottom.getChildren().add(outputBox);

        main.getChildren().add(bottom);
        pageLayout.setCenter(main);
    }
    private void populateProgramList() {
        programList.getItems().clear();
        for (String[] srcCode : sources) {
            programList.getItems().add(srcCode[NAME]);
        }
    }
    private void registerHandlers() {
        submitButton.setOnAction(event -> {
            String name = programNameField.getText();
            if (nameToCode.get(name) != null) {
                showErrorMessageBox("Duplicate program name");
                return;
            }
            String code = codeBox.getText();
            Controller program;
            try {
                program = inputManager.program(code, counter.getAndIncrement());
            } catch (TypecheckException | TokenizerException | ParseException e) {
                showErrorMessageBox(e.getMessage());
                return;
            }
            nameToProgram.put(name, program);
            nameToCode.put(name, code);
            programList.getItems().add(name);
        });

        runButton.setOnAction(event -> {
            String name = programList.getSelectionModel().getSelectedItem();
            if (name == null) {
                showErrorMessageBox("No item selected");
                return;
            }
            String output = nameToProgram.get(name).takeAllSteps();
            nameToProgram.remove(name);
            nameToCode.remove(name);
            programList.getItems().remove(name);
            sources = sources.stream().filter(p -> !Objects.equals(p[NAME], name)).toList();
            outputBox.setText(output);
        });

        programList.getSelectionModel().selectedItemProperty().addListener((obs, prev, curr) -> {
            if (curr == null)
                return;
            programNameField.setText(curr);
            codeBox.setText(nameToCode.get(curr));
        });
    }
    private void showErrorMessageBox(String warning) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(warning);
        alert.showAndWait();
    }

}
