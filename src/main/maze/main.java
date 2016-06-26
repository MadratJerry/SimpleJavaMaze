package main.maze;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
        Button btRun = new Button("Run!");
        Button btStep = new Button("One step");
        Button btBestPath = new Button("Show Current Best Path");
        Button btGeneration = new Button("Generation");
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
        btBestPath.disableProperty().bind(mazePane.hasBestPathProperty().not());
        btBestPath.setOnAction(event -> mazePane.showBestPath());
        btStep.setOnAction(event -> mazePane.singleStep());
        btStep.disableProperty().bind(mazePane.runningProperty());
        btRun.setOnAction(event -> mazePane.search());
        btRun.disableProperty().bind(mazePane.runningProperty());
        btGeneration.setOnAction(event -> mazePane.generation());
        btGeneration.disableProperty().bind(mazePane.runningProperty());
        btPane.getChildren().addAll(btGeneration, btRun, btStep, btBestPath, movedText);

        pane.getChildren().addAll(mazePane, btPane);
        primaryStage.setTitle("Maze pathfinding");
        primaryStage.setScene(new Scene(pane));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
