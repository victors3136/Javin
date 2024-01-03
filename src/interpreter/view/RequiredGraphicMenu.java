package interpreter.view;

import inputmanager.InputManager;
import inputmanager.StringToStatementConverter;
import inputmanager.parser.ParseException;
import inputmanager.tokenizer.TokenizerException;
import interpreter.controller.Controller;
import interpreter.model.exceptions.*;
import interpreter.model.programstate.ProgramState;
import interpreter.model.values.Value;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import static interpreter.view.SourceGenerator.CODE;

public class RequiredGraphicMenu extends Application implements Menu {
    @SuppressWarnings("unused")
    public static class TableEntry {
        private final SimpleStringProperty first;
        private final SimpleStringProperty second;

        protected TableEntry(String first, String second) {
            this.first = new SimpleStringProperty(first);
            this.second = new SimpleStringProperty(second);
        }

        public String getFirst() {
            return first.get();
        }

        public void setFirst(String first) {
            this.first.set(first);
        }

        public String getSecond() {
            return second.get();
        }

        public void setSecond(String second) {
            this.second.set(second);
        }
    }

    private static final StringToStatementConverter inputManager = new InputManager();
    private BorderPane pageLayout;
    private Scene scene;
    private static final double pageWidth = 1000;
    private static final double pageHeight = 600;
    private static final double defaultSpacing = 5;
    private TextField programCounter;
    private ListView<String> programList;
    private ListView<String> outputList;
    private ListView<String> fileTable;
    private ListView<String> exeStack;
    private TableView<TableEntry> symbolTable;
    private TableView<TableEntry> heapTable;
    private final AtomicInteger logFileCounter = new AtomicInteger(1);
    private final SortedMap<String, Controller> programNameToExecutable = new TreeMap<>();
    private Button stepButton;

    @Override
    public void show() {
        launch();
    }

    @Override
    public void start(Stage stage) {
        initNodes();
        addPrograms();
        setupPage(stage);
        addNodes();
        populateProgramList();
        registerHandlers();
        stage.show();
    }

    private void updateComponents() {
        programCounter.setText(String.valueOf(programList.getItems().size()));
        int id = programList.getSelectionModel().getSelectedIndex();
        if (id == -1) {
            showErrorMessageBox("No program selected");
            return;
        }
        ProgramState selection = programNameToExecutable.values().stream().toList().get(id).getProgram();
        outputList.getItems().clear();
        outputList.getItems().addAll(selection
                .getOutputList()
                .stream()
                .map(Value::toString)
                .toList());
        fileTable.getItems().clear();
        fileTable.getItems().addAll(selection
                .getFileTable()
                .stream()
                .toList());
        heapTable.getItems().clear();
        ObservableList<TableEntry> heapData = FXCollections.observableArrayList();
        selection.getHeapTable().entriesStream().forEach(entry -> heapData.add(new TableEntry(entry.getKey().toString(), entry.getValue().toString())));
        heapTable.setItems(heapData);
        symbolTable.getItems().clear();
        ObservableList<TableEntry> symbolData = FXCollections.observableArrayList();
        selection.getSymbolTable().stream().forEach(entry -> symbolData.add(new TableEntry(entry.getKey(), entry.getValue().toString())));
        symbolTable.setItems(symbolData);
        exeStack.getItems().clear();
        exeStack.getItems().addAll(selection
                .getExecutionStack()
                .stream()
                .map(Object::toString)
                .toList());
    }

    private void registerHandlers() {
        stepButton.setOnAction(event -> {
            int id = programList.getSelectionModel().getSelectedIndex();
            if (id == -1) {
                showErrorMessageBox("No program selected");
                return;
            }
            Controller current = programNameToExecutable.values().stream().toList().get(id);
            ProgramState selection = current.getProgram();
            try {
                selection.takeOneStep();
            } catch (ProgramStateException e) {
                showErrorMessageBox("Program State Exception : " + e.getMessage());
            } catch (SymbolTableException e) {
                showErrorMessageBox("Symbol Table Exception : " + e.getMessage());
            } catch (StatementException e) {
                showErrorMessageBox("Statement Exception : " + e.getMessage());
            } catch (ValueException e) {
                showErrorMessageBox("Value Exception : " + e.getMessage());
            } catch (ExpressionException e) {
                showErrorMessageBox("Expression Exception : " + e.getMessage());
            } catch (HeapException e) {
                showErrorMessageBox("Heap Exception : " + e.getMessage());
            }
            updateComponents();
            populateProgramList();
            if (selection.getExecutionStack().empty()) {
                programNameToExecutable.entrySet().removeIf(entry -> entry.getValue().equals(current));
                populateProgramList();
            }
            current.collectGarbage();
            current.removeCompletedPrograms();
        });
        programList.setOnMouseClicked(event -> updateComponents());

    }

    private void populateProgramList() {
        int i = programList.getSelectionModel().getSelectedIndex();
        programList.getItems().clear();
        for (Controller controller : programNameToExecutable.values()) {
            programList.getItems().add(controller.getProgram().getID().toString());
        }
        programCounter.setText(String.valueOf(programList.getItems().size()));
        if (i != -1 && i < programList.getItems().size()) {
            programList.getSelectionModel().select(i);
        }
    }

    private void addNodes() {
        HBox main = new HBox();
        main.setPadding(new Insets(2 * defaultSpacing));
        main.setSpacing(2 * defaultSpacing);
        VBox left = new VBox(), center = new VBox(), right = new VBox();
        left.setSpacing(defaultSpacing);
        left.setAlignment(Pos.TOP_RIGHT);
        center.setSpacing(defaultSpacing);
        center.setAlignment(Pos.TOP_CENTER);
        right.setSpacing(defaultSpacing);
        right.setAlignment(Pos.TOP_LEFT);
        left.getChildren().addAll(programCounter, programList, stepButton);
        center.getChildren().addAll(outputList, exeStack, fileTable);
        right.getChildren().addAll(symbolTable, heapTable);
        main.getChildren().addAll(left, center, right);
        pageLayout.setCenter(main);
    }

    private void setupPage(Stage stage) {
        stage.setScene(scene);
        stage.setTitle("Parsr");
        String src = "images\\icon.png";
        File file = new File(src);
        Image icon = new Image(file.toURI().toString());
        stage.getIcons().add(icon);
    }

    private void initNodes() {
        pageLayout = new BorderPane();
        scene = new Scene(pageLayout, pageWidth, pageHeight);
        {
            programCounter = new TextField();
            programCounter.setPrefWidth(pageWidth / 4);
            programCounter.setEditable(false);
            programCounter.setPrefHeight(pageHeight / 8);
        }
        {
            programList = new ListView<>();
            programList.setPrefWidth(pageWidth / 4);
            programList.setPrefHeight(pageHeight * 3 / 4);
        }
        {
            stepButton = new Button("Take one step in selected program");
            stepButton.setPrefWidth(pageWidth / 4);
            stepButton.setPrefHeight(pageHeight / 8);
        }
        {
            outputList = new ListView<>();
            outputList.setPrefWidth(pageWidth / 2);
            outputList.setPrefHeight(pageHeight / 4);
        }
        {
            exeStack = new ListView<>();
            exeStack.setPrefWidth(pageWidth / 2);
            exeStack.setPrefHeight(pageHeight / 2);
        }
        {
            fileTable = new ListView<>();
            fileTable.setPrefWidth(pageWidth / 2);
            fileTable.setPrefHeight(pageHeight / 4);
        }
        {
            symbolTable = new TableView<>();
            symbolTable.setPrefWidth(pageWidth / 4);
            symbolTable.setPrefHeight(pageHeight / 2);
            TableColumn<TableEntry, String> symbolColumn = new TableColumn<>("Symbol");
            symbolColumn.setCellValueFactory(new PropertyValueFactory<>("first"));
            TableColumn<TableEntry, String> valueColumn = new TableColumn<>("Value");
            valueColumn.setCellValueFactory(new PropertyValueFactory<>("second"));
            symbolTable.getColumns().addAll(symbolColumn, valueColumn);
        }
        {
            heapTable = new TableView<>();
            heapTable.setPrefWidth(pageWidth / 4);
            heapTable.setPrefHeight(pageHeight / 2);
            TableColumn<TableEntry, String> addressColumn = new TableColumn<>("Address");
            addressColumn.setCellValueFactory(new PropertyValueFactory<>("first"));
            TableColumn<TableEntry, String> valueColumn = new TableColumn<>("Value");
            valueColumn.setCellValueFactory(new PropertyValueFactory<>("second"));
            heapTable.getColumns().addAll(addressColumn, valueColumn);
        }
    }

    private void addPrograms() {
        for (String[] source : SourceGenerator.makeList()) {
            Controller controller;
            try {
                controller = inputManager.program(source[CODE], logFileCounter.getAndIncrement());
            } catch (TypecheckException e) {
                showErrorMessageBox("Typechecking error: " + e.getMessage());
                continue;
            } catch (TokenizerException e) {
                showErrorMessageBox("Tokenizing error: " + e.getMessage());
                continue;
            } catch (ParseException e) {
                showErrorMessageBox("Parsing exception: " + e.getMessage());
                continue;
            }
            programNameToExecutable.put(controller.getProgram().getID().toString(), controller);
        }
    }

    private static void showErrorMessageBox(String warning) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(warning);
        alert.showAndWait();
    }
}
