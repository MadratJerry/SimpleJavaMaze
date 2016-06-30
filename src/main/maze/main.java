package main.maze;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pers.crazymouse.algorithm.visualizer.MazePane;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 * Created by crazymouse on 6/19/16.
 */
public class main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    Text movedText = new Text("(0,0)");
    File mazeFile;
    MazePane mazePane;
    ObservableList<Stack<Integer>> pathList;
    ObservableList<HBox> items = FXCollections.observableArrayList();
    ArrayList<ObservableList<Stack<Integer>>> arraySortPathList = new ArrayList<>();
    int[][] map;

    @Override
    public void start(Stage primaryStage) {

        HBox pane = new HBox(10);
        pane.setAlignment(Pos.CENTER);
        mazePane = new MazePane(map);

        VBox btPane = new VBox(10);
        btPane.setAlignment(Pos.CENTER);
        CheckBox cbAnimation = new CheckBox("Animation");
        CheckBox cbFeature = new CheckBox("Feature");
        Button btRun = new Button("Run!");
        Button btStep = new Button("One step");
        Button btBestPath = new Button("Show Current Best Path");
        Button btGeneration = new Button("Generation");
        Button btStop = new Button("Stop");
        Button btSave = new Button("Save");
        Slider sSpeed = new Slider();
        mazePane.movedXProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                movedText.setText(String.format("(%d,%d)", mazePane.getMovedX(), mazePane.getMovedY()));
            }
        });
        mazePane.movedYProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                movedText.setText(String.format("(%d,%d)", mazePane.getMovedX(), mazePane.getMovedY()));
            }
        });

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        mazePane.animationProperty().bind(cbAnimation.selectedProperty());
        mazePane.featuresProperty().bind(cbFeature.selectedProperty());
        cbAnimation.disableProperty().bind(mazePane.runningProperty());
        cbAnimation.setSelected(true);
        cbFeature.setSelected(true);
        btBestPath.disableProperty().bind(mazePane.hasBestPathProperty().not());
        btBestPath.setOnAction(event -> mazePane.showBestPath());
        btStep.setOnAction(event -> mazePane.singleStep());
        btStep.disableProperty().bind(mazePane.runningProperty());
        btRun.setOnAction(event -> mazePane.search());
        btRun.disableProperty().bind(mazePane.runningProperty());
        btStop.setOnAction(event -> mazePane.setStop(true));
        btStop.disableProperty().bind(mazePane.runningProperty().not());
        btGeneration.setOnAction(event -> mazePane.generation());
        btGeneration.disableProperty().bind(mazePane.runningProperty());
        btSave.setOnAction(event -> {
            mazeFile = fileChooser.showSaveDialog(primaryStage);
            saveMaze();
        });
        mazePane.speedProperty().bind(sSpeed.valueProperty());
        sSpeed.setShowTickLabels(true);
        sSpeed.setShowTickMarks(true);
        sSpeed.setMin(1);
        sSpeed.setMax(100);
        btPane.getChildren().addAll(cbFeature, cbAnimation, btGeneration, btSave, btRun, btStop, btStep, btBestPath, sSpeed, movedText);


        pathList = mazePane.getPathList();
        pathList.addListener(new InvalidationListener() {
            HBox newBox;
            Paint lineColor;
            Line colorLine;
            Text txLength;

            @Override
            public void invalidated(Observable observable) {
                try {
                    if (pathList.size() == 0) {
                        items.clear();
                        arraySortPathList.clear();
                        return;
                    }
                    newBox = new HBox(10);
                    colorLine = new Line(0, 3, 100, 3);
                    colorLine.setStrokeWidth(3);
                    long feed = System.currentTimeMillis();
                    lineColor = Color.color(new Random(feed * 1).nextDouble(),
                            new Random(feed * 2).nextDouble(),
                            new Random(feed * 3).nextDouble());
                    colorLine.setStroke(lineColor);
                    txLength = new Text("Step: " +
                            pathList.get(pathList.size() - 1).size() + "");
                    // Just save the num...
                    txLength.setId(pathList.size() - 1 + "");
                    txLength.setLineSpacing(pathList.get(pathList.size() - 1).size());
                    arraySortPathList.add(pathList);
                    newBox.getChildren().addAll(colorLine, txLength);
                    if (items.size() == 0)
                        items.add(newBox);
                    else {
                        for (int i = 0, size; i < items.size(); i++) {
                            size = (int) ((Text) items.get(i).getChildren().get(1)).getLineSpacing();
                            if (pathList.get(pathList.size() - 1).size() > size) {
                                items.add(i + 1, newBox);
                                break;
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
        });
        ListView<HBox> lvPath = new ListView(items);
        lvPath.setOnMouseClicked(event -> {
            int num = Integer.parseInt(
                    lvPath.getSelectionModel().getSelectedItem().getChildren().get(1).getId());
            Paint color = ((Line) lvPath.getSelectionModel().getSelectedItem().getChildren().get(0)).getStroke();
            mazePane.showPath(pathList.get(num), color);
        });
        pane.getChildren().addAll(mazePane, btPane, lvPath);
        primaryStage.setTitle("Maze pathfinding");
        primaryStage.setScene(new Scene(pane));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void saveMaze() {
        try (PrintWriter output = new PrintWriter(mazeFile)) {
            for (int i = 0; i < mazePane.getMap().length; i++) {
                for (int j = 0; j < mazePane.getMap()[i].length; j++) {
                    output.print(mazePane.getMap()[i][j].getValue());
                }
                output.println();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
