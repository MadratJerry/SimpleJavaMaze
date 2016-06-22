package main;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pers.crazymouse.algorithm.visualizer.MazePane;

import java.io.File;

/**
 * Created by crazymouse on 6/19/16.
 */
public class maze extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        HBox pane = new HBox(10);
        pane.setAlignment(Pos.CENTER);
        MazePane mazePane = new MazePane(10, 10);
        mazePane.setMap(new int[][]{
                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 1, 1, 0, 1, 0, 1},
                {0, 1, 0, 1, 1, 0, 0, 1, 1, 1},
                {0, 0, 0, 1, 0, 0, 1, 0, 0, 0},
                {1, 1, 1, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 1, 0, 1, 1, 0, 1, 0, 1},
                {0, 0, 1, 1, 1, 0, 0, 1, 1, 1},
                {0, 1, 0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}});
        mazePane.setBegin(0, 0);
        mazePane.setEnd(3, 2);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        VBox btPane = new VBox(10);
        btPane.setAlignment(Pos.CENTER);
        Button btFile = new Button("Maze file");
        Button btRun = new Button("Run!");
        Button btStep = new Button("One step");
        btFile.setOnAction(event -> fileChooser.showOpenDialog(primaryStage));
        btStep.setOnAction(event -> mazePane.singleStep());
        btRun.setOnAction(event -> mazePane.search());
        btPane.getChildren().addAll(btFile, btRun, btStep);

        pane.getChildren().addAll(mazePane, btPane);

        primaryStage.setTitle("Maze pathfinding");
        primaryStage.setScene(new Scene(pane));
        primaryStage.show();
    }
}
