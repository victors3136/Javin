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
import java.util.ArrayList;
import java.util.List;
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
    private ListView<String> concurrentThreads;
    private TableView<TableEntry> symbolTable;
    private TableView<TableEntry> heapTable;
    private final AtomicInteger logFileCounter = new AtomicInteger(1);
    private final List<Controller> controllers = new ArrayList<>();
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

    private void initNodes() {
        pageLayout = new BorderPane();
        scene = new Scene(pageLayout, pageWidth, pageHeight);

        programCounter = new TextField();
        programCounter.setPrefWidth(pageWidth / 4);
        programCounter.setEditable(false);
        programCounter.setPrefHeight(pageHeight / 8);

        programList = new ListView<>();
        programList.setPrefWidth(pageWidth / 4);
        programList.setPrefHeight(pageHeight / 2);

        stepButton = new Button("Take one step in selected program");
        stepButton.setPrefWidth(pageWidth / 4);
        stepButton.setPrefHeight(pageHeight / 8);

        outputList = new ListView<>();
        outputList.setPrefWidth(pageWidth / 2);
        outputList.setPrefHeight(pageHeight / 4);

        exeStack = new ListView<>();
        exeStack.setPrefWidth(pageWidth / 2);
        exeStack.setPrefHeight(pageHeight / 2);

        fileTable = new ListView<>();
        fileTable.setPrefWidth(pageWidth / 2);
        fileTable.setPrefHeight(pageHeight / 4);

        concurrentThreads = new ListView<>();
        concurrentThreads.setPrefWidth(pageWidth / 4);
        concurrentThreads.setPrefHeight(pageHeight / 4);

        symbolTable = new TableView<>();
        symbolTable.setPrefWidth(pageWidth / 4);
        symbolTable.setPrefHeight(pageHeight / 2);
        TableColumn<TableEntry, String> symbolColumn = new TableColumn<>("Symbol");
        symbolColumn.setCellValueFactory(new PropertyValueFactory<>("first"));
        TableColumn<TableEntry, String> valueColumnForSymT = new TableColumn<>("Value");
        valueColumnForSymT.setCellValueFactory(new PropertyValueFactory<>("second"));
        symbolTable.getColumns().addAll(symbolColumn, valueColumnForSymT);

        heapTable = new TableView<>();
        heapTable.setPrefWidth(pageWidth / 4);
        heapTable.setPrefHeight(pageHeight / 2);
        TableColumn<TableEntry, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("first"));
        TableColumn<TableEntry, String> valueColumnForHeap = new TableColumn<>("Value");
        valueColumnForHeap.setCellValueFactory(new PropertyValueFactory<>("second"));
        heapTable.getColumns().addAll(addressColumn, valueColumnForHeap);
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
            controllers.add(controller);
        }
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
        left.getChildren().addAll(programCounter, programList, concurrentThreads, stepButton);
        center.getChildren().addAll(outputList, exeStack, fileTable);
        right.getChildren().addAll(symbolTable, heapTable);
        main.getChildren().addAll(left, center, right);
        pageLayout.setCenter(main);
    }

    private void populateProgramList() {
        int i = programList.getSelectionModel().getSelectedIndex();
        programList.getItems().clear();
        for (Controller controller : controllers) {
            programList.getItems().add(controller.getProgram().getID().toString());
        }
        programCounter.setText(String.valueOf(programList.getItems().size()));
        if (i != -1 && i < programList.getItems().size()) {
            programList.getSelectionModel().select(i);
        }
    }

    private void updateComponents(ProgramState state) {
        programCounter.setText(String.valueOf(programList.getItems().size()));
        outputList.getItems().clear();
        outputList.getItems().addAll(state
                .getOutputList()
                .stream()
                .map(Value::toString)
                .toList());
        fileTable.getItems().clear();
        fileTable.getItems().addAll(state
                .getFileTable()
                .stream()
                .toList());
        heapTable.getItems().clear();
        ObservableList<TableEntry> heapData = FXCollections.observableArrayList();
        state.getHeapTable().entriesStream().forEach(entry -> heapData.add(new TableEntry(entry.getKey().toString(), entry.getValue().toString())));
        heapTable.setItems(heapData);
        symbolTable.getItems().clear();
        ObservableList<TableEntry> symbolData = FXCollections.observableArrayList();
        state.getSymbolTable().stream().forEach(entry -> symbolData.add(new TableEntry(entry.getKey(), entry.getValue().toString())));
        symbolTable.setItems(symbolData);
        exeStack.getItems().clear();
        exeStack.getItems().addAll(state
                .getExecutionStack()
                .stream()
                .map(Object::toString)
                .toList());
    }

    private void populateThreadList() {
        int programID = programList.getSelectionModel().getSelectedIndex();
        if (programID == -1) {
            showErrorMessageBox("No program selected");
            return;
        }
        Controller current = controllers.get(programID);
        concurrentThreads.getItems().clear();
        concurrentThreads
                .getItems()
                .addAll(current
                        .getPrograms()
                        .stream()
                        .map(s -> "Execution thread " + s.getID().toString())
                        .toList());
        if (!concurrentThreads.getItems().isEmpty()) {
            concurrentThreads.getSelectionModel().select(0);
        }
    }

    private int getCurrentController() {
        int programID = programList.getSelectionModel().getSelectedIndex();
        if (programID == -1) {
            showErrorMessageBox("No program selected");
        }
        return programID;
    }

    private int getCurrentThread() {
        int threadID = concurrentThreads.getSelectionModel().getSelectedIndex();
        if (threadID == -1) {
            showErrorMessageBox("No thread selected");
        }
        return threadID;
    }

    private void registerHandlers() {
        programList.setOnMouseClicked(event -> populateThreadList());
        concurrentThreads.setOnMouseClicked(event -> {
            int currentController = getCurrentController();
            if (currentController == -1)
                return;
            int currentThread = getCurrentThread();
            if (currentThread == -1)
                return;
            Controller current = controllers.get(currentController);
            ProgramState selection = current
                    .getPrograms()
                    .get(currentThread);
            updateComponents(selection);
        });
        stepButton.setOnAction(event -> {
            int currentController = getCurrentController();
            if (currentController == -1)
                return;
            int currentThread = getCurrentThread();
            if (currentThread == -1)
                return;
            List<ProgramState> states = controllers.get(currentController).getPrograms();
            ProgramState selection = states.get(currentThread);
            ProgramState spawnedProcess = null;
            try {
                spawnedProcess = selection.takeOneStep();
                updateComponents(selection);
            } catch (SymbolTableException e) {
                showErrorMessageBox("Symbol table exception -- " + e.getMessage());
            } catch (StatementException e) {
                showErrorMessageBox("Statement exception -- " + e.getMessage());
            } catch (ValueException e) {
                showErrorMessageBox("Value exception -- " + e.getMessage());
            } catch (ProgramStateException e) {
                showErrorMessageBox("Program state exception -- " + e.getMessage());
            } catch (HeapException e) {
                showErrorMessageBox("Heap exception -- " + e.getMessage());
            } catch (ExpressionException e) {
                showErrorMessageBox("Expression exception -- " + e.getMessage());
            }
            controllers.get(currentController).removeCompletedPrograms();
            if (spawnedProcess != null && !spawnedProcess.getExecutionStack().empty()) {
                controllers.get(currentController).getPrograms().add(spawnedProcess);
                populateThreadList();
                concurrentThreads.getSelectionModel().select(currentThread);
            }
            controllers.get(currentController).removeCompletedPrograms();
            populateThreadList();
            controllers.get(currentController).collectGarbage();
            if (controllers.get(currentController).getPrograms().isEmpty()) {
                controllers.remove(currentController);
                populateProgramList();
                populateThreadList();
                if (!programList.getItems().isEmpty()) {
                    programList.getSelectionModel().select(0);
                }
            }
        });
    }

    private static void showErrorMessageBox(String warning) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(warning);
        alert.showAndWait();
    }
}
