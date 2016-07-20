package main.maze;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pers.crazymouse.algorithm.std.Maze;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by crazymouse on 6/25/16.
 */
public class load extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    File mazeFile;
    int[][] map;

    @Override
    public void start(Stage primaryStage) {
        main main = new main();
        Stage loadStage = new Stage();
        VBox loadPane = new VBox(10);
        loadPane.setPadding(new Insets(10, 10, 10, 10));
        loadPane.setAlignment(Pos.CENTER);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Maze File(*.txt)", "*.txt"));
        Button btFile = new Button("Load maze file");
        Button btGeneration = new Button("Maze generation");
        Text txWarnning = new Text();
        btFile.setOnAction(event -> {
            mazeFile = fileChooser.showOpenDialog(loadStage);
            if (loadMaze()) {
                main.map = map;
                main.start(new Stage());
                loadStage.close();
            } else {
                txWarnning.setText("Not a standard maze file!");
                txWarnning.setFill(Color.RED);
            }
        });
        btGeneration.setOnAction(event -> {
            GridPane pane = new GridPane();
            pane.setPadding(new Insets(10, 10, 10, 10));
            pane.setHgap(5.5);
            pane.setVgap(5.5);

            pane.add(new Text("Width: "), 0, 0);
            pane.add(new Text("Height:"), 0, 1);
            TextField textFieldWidth = new TextField();
            TextField textFieldHeight = new TextField();
            pane.addColumn(1, textFieldWidth, textFieldHeight);
            Button generation = new Button("Go!");

            pane.add(generation, 1, 2);
            pane.add(txWarnning, 0, 2);
            GridPane.setHalignment(generation, HPos.RIGHT);
            generation.setOnAction(event1 -> {
                Pattern pattern = Pattern.compile("^[0-9]{1,}$");
                int width = 0, height = 0;
                String widthStr = textFieldWidth.getText();
                String heightStr = textFieldHeight.getText();
                if (pattern.matcher(widthStr).matches() && pattern.matcher(heightStr).matches()) {
                    width = Integer.parseInt(widthStr);
                    height = Integer.parseInt(heightStr);
                    if (width * height > 100000) {
                        txWarnning.setText("It's too large.");
                        txWarnning.setFill(Color.RED);
                    } else {
                        width = width + width % 2 + 1;
                        height = height + height % 2 + 1;
                        map = new int[width][height];
                        for (int i = 0; i < width; i++) {
                            for (int j = 0; j < height; j++) {
                                map[i][j] = Maze.BLANK;
                                if (i == 0 || j == 0 || i == width - 1 || j == height - 1)
                                    map[i][j] = Maze.WALL;
                            }
                        }
                        main.map = map;
                        main.start(new Stage());
                        loadStage.close();
                    }
                } else {
                    txWarnning.setText("Input is NOT allowed!");
                    txWarnning.setFill(Color.RED);
                }
            });
            loadStage.setScene(new Scene(pane));
            txWarnning.setText("Only allowed numbers");
        });
        loadPane.getChildren().addAll(btFile, btGeneration, txWarnning);

        loadStage.setScene(new Scene(loadPane, 200, 100));
        loadStage.show();
        loadStage.setResizable(false);
    }


    public boolean loadMaze() {
        if (mazeFile == null || !mazeFile.exists())
            return false;
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
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
