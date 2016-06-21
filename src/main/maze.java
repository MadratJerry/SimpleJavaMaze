package main;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pers.crazymouse.algorithm.visualizer.SGridPane;
import pers.crazymouse.algorithm.visualizer.Square;

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
        SGridPane sGridPane = new SGridPane();
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                sGridPane.add(new Square(5, Math.random() < 0.5 ? Color.WHITE : Color.BLACK), i, j);
            }
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        VBox btPane = new VBox(10);
        btPane.setAlignment(Pos.CENTER);
        Button btFile = new Button("Maze file");
        Button btRun = new Button("Run!");
        Button btStep = new Button("One step");
        btFile.setOnAction(event -> fileChooser.showOpenDialog(primaryStage));
        btPane.getChildren().addAll(btFile, btRun, btStep);

        pane.getChildren().addAll(sGridPane, btPane);

        primaryStage.setTitle("Maze pathfinding");
        primaryStage.setScene(new Scene(pane));
        primaryStage.show();
    }
}
