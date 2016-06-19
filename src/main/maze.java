package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pers.crazymouse.algorithm.visualizer.SGridPane;
import pers.crazymouse.algorithm.visualizer.Square;

/**
 * Created by crazymouse on 6/19/16.
 */
public class maze extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        SGridPane sGridPane = new SGridPane();
        for (int i = 0; i < 100; i++)
            for (int j = 0; j < 100; j++)
                sGridPane.add(new Square(Math.random() > 0.5 ? Color.WHITE : Color.BLACK), i, j);
        primaryStage.setScene(new Scene(sGridPane, 500, 500));
        primaryStage.show();
    }
}
