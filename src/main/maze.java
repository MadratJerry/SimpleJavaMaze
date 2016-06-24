package main;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pers.crazymouse.algorithm.visualizer.MazePane;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by crazymouse on 6/19/16.
 */
public class maze extends Application {

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
//        MazePane mazePane = new MazePane(new int[][]{
//                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//                {1, 0, 1, 0, 1, 1, 0, 1, 0, 1},
//                {0, 0, 1, 1, 1, 0, 0, 1, 1, 1},
//                {0, 1, 0, 0, 0, 0, 1, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//                {1, 0, 1, 0, 1, 1, 0, 1, 0, 1},
//                {0, 0, 1, 1, 1, 0, 0, 1, 1, 1},
//                {0, 1, 0, 0, 0, 0, 1, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}});
        mazeFile = new File("/Users/crazymouse/Codes/test.txt");
        loadMaze();
        mazePane = new MazePane(map);
        mazePane.setBegin(1, 1);
        mazePane.setEnd(199, 199);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        VBox btPane = new VBox(10);
        btPane.setAlignment(Pos.CENTER);
        Button btFile = new Button("Maze file");
        Button btRun = new Button("Run!");
        Button btStep = new Button("One step");
        Button btBestPath = new Button("Show Current Best Path");
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
        btBestPath.disableProperty().bind(mazePane.getHasBestPath().not());
        btFile.setOnAction(event -> fileChooser.showOpenDialog(primaryStage));
        btStep.setOnAction(event -> mazePane.singleStep());
        btRun.setOnAction(event -> mazePane.search());
        btPane.getChildren().addAll(btFile, btRun, btStep, btBestPath, movedText);

        pane.getChildren().addAll(mazePane, btPane);
        primaryStage.setTitle("Maze pathfinding");
        primaryStage.setScene(new Scene(pane));
        primaryStage.show();
    }


    public void loadMaze() {
        ArrayList<String> mazeStirng = new ArrayList<>();
        int height, width;
        try (Scanner input = new Scanner(mazeFile)) {
            while (input.hasNext()) {
                mazeStirng.add(input.nextLine());
            }
            height = mazeStirng.size();
            width = mazeStirng.get(height - 1).length();

            map = new int[height][width];
            for (int i = 0; i < height; i++)
                for (int j = 0; j < width; j++)
                    map[i][j] = mazeStirng.get(i).charAt(j) - '0';
        } catch (IOException ex) {
            return;
        }
    }
}
