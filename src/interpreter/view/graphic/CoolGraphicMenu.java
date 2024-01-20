package interpreter.view.graphic;

import inputmanager.parser.ParseException;
import inputmanager.tokenizer.TokenizerException;
import interpreter.controller.Controller;
import interpreter.model.exceptions.TypecheckException;
import interpreter.view.utils.SourceGenerator;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static interpreter.view.utils.SourceGenerator.CODE;
import static interpreter.view.utils.SourceGenerator.NAME;

public class CoolGraphicMenu extends AbstractGraphicMenu {
    private TextField programNameField;
    private TextArea codeBox, outputBox;
    private Button submitButton, runButton, exitButton;
    private final Map<String, String> programNameToSourceCode = new HashMap<>();
    private final Map<String, Controller> programNameToExecutable = new HashMap<>();
    private List<String[]> sources;
    private ListView<String> runnableProgramsDisplayList;
    private Box spinningCube;
    private Sphere spinningSphere;
    private Cylinder spinningCylinder;

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
        runnableProgramsDisplayList.requestFocus();
        stage.show();
    }

    @Override
    protected void addPrograms() {
        for (String[] source : sources) {
            Controller controller = parseAndCreateExecutableProgram(source[CODE]);
            if (controller == null)
                continue;
            programNameToSourceCode.put(source[NAME], source[CODE]);
            programNameToExecutable.put(source[NAME], controller);
        }
    }

    @Override
    protected void initNodes() {
        super.initNodes();
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
        spinningCube = new Box();
        spinningSphere = new Sphere();
        spinningCylinder = new Cylinder();
        sources = SourceGenerator.makeStrings();
    }

    private HBox setupHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(2 * defaultSpacing));
        header.setSpacing(2 * defaultSpacing);
        ImageView headerImage = new ImageView(imageFromPath("images\\header.png"));
        headerImage.setAccessibleText("Pristine landscape");
        header.getChildren().add(headerImage);
        return header;
    }

    private void setup3DElements() {
        spinningCube.setTranslateX(-pageWidth / 2);
        spinningCylinder.setTranslateX(-pageWidth / 6);
        spinningSphere.setTranslateX(pageWidth / 8);
        spinningCube.setHeight(pageHeight / 24);
        spinningCube.setWidth(pageHeight / 24);
        spinningCube.setDepth(pageHeight / 24);
        spinningCube.getTransforms().add(new Rotate(0, Rotate.Y_AXIS));
        spinningSphere.setRadius(pageHeight / 12);
        spinningSphere.getTransforms().add(new Rotate(45, Rotate.X_AXIS));
        spinningCylinder.getTransforms().add(new Rotate(0, Rotate.Z_AXIS));
        spinningCylinder.setHeight(pageHeight / 6);
        spinningCylinder.setRadius(pageHeight / 24);
        PhongMaterial sphereMaterial = new PhongMaterial();
        sphereMaterial.setDiffuseMap(imageFromPath("images\\sphere-texture.jpg"));
        PhongMaterial cubeMaterial = new PhongMaterial();
        cubeMaterial.setDiffuseMap(imageFromPath("images\\cube-texture.jpg"));
        PhongMaterial cylinderMaterial = new PhongMaterial();
        cylinderMaterial.setDiffuseMap(imageFromPath("images\\cylinder-texture.jpg"));
        spinningSphere.setMaterial(sphereMaterial);
        spinningCube.setMaterial(cubeMaterial);
        spinningCylinder.setMaterial(cylinderMaterial);
    }

    private VBox setupBottom() {
        VBox bottom = new VBox();
        bottom.setSpacing(defaultSpacing);
        bottom.setAlignment(Pos.BOTTOM_CENTER);
        outputBox.setPrefWidth(pageWidth);
        outputBox.setPrefHeight(pageWidth / 2);
        exitButton.setPrefWidth(pageWidth);
        bottom.getChildren().addAll(outputBox, exitButton, create3DScene());
        return bottom;
    }

    private VBox setupSide() {
        VBox side = new VBox();
        side.setSpacing(defaultSpacing);
        side.setAlignment(Pos.CENTER);
        return side;
    }

    private HBox setupCenter() {
        HBox topCenter = new HBox();
        topCenter.setSpacing(defaultSpacing * 3 / 2);
        topCenter.setAlignment(Pos.BOTTOM_CENTER);

        codeBox.setPrefWidth(pageWidth / 2);
        codeBox.setPrefHeight(pageHeight * 2 / 3);
        programNameField.setPrefWidth(pageWidth / 2);
        submitButton.setPrefWidth(pageWidth / 2);
        runnableProgramsDisplayList.setPrefWidth(pageWidth / 2);
        runButton.setPrefWidth(pageWidth / 2);
        VBox left = setupSide(), right = setupSide();
        left.getChildren().addAll(codeBox, programNameField, submitButton);
        right.getChildren().addAll(runnableProgramsDisplayList, runButton);
        topCenter.getChildren().addAll(left, right);
        return topCenter;
    }

    private void addNodes() {
        pageLayout.setTop(setupHeader());
        VBox main = new VBox();
        main.setPadding(new Insets(2 * defaultSpacing));
        main.setSpacing(2 * defaultSpacing);
        main.getChildren().addAll(setupCenter(), setupBottom());
        pageLayout.setCenter(main);
    }

    private SubScene create3DScene() {
        setup3DElements();
        RotateTransition cubeRotateTransition = new RotateTransition(Duration.seconds(2), spinningCube);
        RotateTransition sphereRotateTransition = new RotateTransition(Duration.seconds(6), spinningSphere);
        RotateTransition cylinderRotateTransition = new RotateTransition(Duration.seconds(4), spinningCylinder);
        cubeRotateTransition.setAxis(Rotate.Y_AXIS);
        sphereRotateTransition.setAxis(Rotate.X_AXIS);
        cylinderRotateTransition.setAxis(Rotate.Z_AXIS);
        cubeRotateTransition.setByAngle(360);
        sphereRotateTransition.setByAngle(360);
        cylinderRotateTransition.setByAngle(360);
        cubeRotateTransition.setCycleCount(Animation.INDEFINITE);
        sphereRotateTransition.setCycleCount(Animation.INDEFINITE);
        cylinderRotateTransition.setCycleCount(Animation.INDEFINITE);
        cubeRotateTransition.setInterpolator(Interpolator.LINEAR);
        sphereRotateTransition.setInterpolator(Interpolator.LINEAR);
        cylinderRotateTransition.setInterpolator(Interpolator.LINEAR);
        cubeRotateTransition.play();
        sphereRotateTransition.play();
        cylinderRotateTransition.play();
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setFieldOfView(45);
        camera.setFarClip(1000);
        camera.getTransforms().addAll(
                new Rotate(-20, Rotate.Y_AXIS),
                new Rotate(-20, Rotate.X_AXIS),
                new Translate(0, 0, -300)
        );
        SubScene amazing3DScene = new SubScene(new Group(spinningCube, spinningCylinder, spinningSphere), pageWidth, pageHeight / 3, true, SceneAntialiasing.BALANCED);
        amazing3DScene.setCamera(camera);
        return amazing3DScene;
    }

    @Override
    protected void populateProgramList() {
        runnableProgramsDisplayList.getItems().clear();
        for (String[] srcCode : sources) {
            runnableProgramsDisplayList.getItems().add(srcCode[NAME]);
        }
    }

    @Override
    protected void registerHandlers() {
        submitButton.setOnAction(event -> {
            String name = programNameField.getText();
            if (programNameToSourceCode.get(name) != null) {
                showErrorMessageBox("Duplicate program name");
                return;
            }
            String code = codeBox.getText();
            Controller program = parseAndCreateExecutableProgram(code);
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

    private Controller parseAndCreateExecutableProgram(String code) {
        try {
            return inputManager.program(code, logFileCounter.getAndIncrement());
        } catch (TypecheckException e) {
            showErrorMessageBox("Typechecking error:\n" + e.getMessage());
            return null;
        } catch (TokenizerException e) {
            showErrorMessageBox("Tokenizing error:\n" + e.getMessage());
            return null;
        } catch (ParseException e) {
            showErrorMessageBox("Parsing exception:\n" + e.getMessage());
            return null;
        }
    }
}