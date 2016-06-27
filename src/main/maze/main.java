package main.maze;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pers.crazymouse.algorithm.visualizer.MazePane;

import java.io.File;

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
    int[][] map;

    @Override
    public void start(Stage primaryStage) {

        HBox pane = new HBox(10);
        pane.setAlignment(Pos.CENTER);
        mazePane = new MazePane(map);

        VBox btPane = new VBox(10);
        btPane.setAlignment(Pos.CENTER);
        CheckBox cbAnimation = new CheckBox("Animation");
        Button btRun = new Button("Run!");
        Button btStep = new Button("One step");
        Button btBestPath = new Button("Show Current Best Path");
        Button btGeneration = new Button("Generation");
        Button btStop = new Button("Stop");
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
        mazePane.animationProperty().bind(cbAnimation.selectedProperty());
        cbAnimation.disableProperty().bind(mazePane.runningProperty());
        cbAnimation.setSelected(true);
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
        mazePane.speedProperty().bind(sSpeed.valueProperty());
        sSpeed.setShowTickLabels(true);
        sSpeed.setShowTickMarks(true);
        sSpeed.setMin(1);
        sSpeed.setMax(100);
        btPane.getChildren().addAll(cbAnimation, btGeneration, btRun, btStop, btStep, btBestPath, sSpeed, movedText);


        ListView<HBox> lvPath = new ListView();
        HBox a = new HBox();
        a.getChildren().addAll(new Line(0, 1, 100, 1), new Text("1111"));
        lvPath.getItems().addAll(a);
        pane.getChildren().addAll(mazePane, btPane, lvPath);
        primaryStage.setTitle("Maze pathfinding");
        primaryStage.setScene(new Scene(pane));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
