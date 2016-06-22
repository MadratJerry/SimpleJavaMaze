package pers.crazymouse.algorithm.visualizer;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import pers.crazymouse.algorithm.std.Maze;

/**
 * Created by crazymouse on 6/22/16.
 */
public class MazePane extends StackPane {
    static final Paint WALL = Color.BLACK;
    static final Paint BLANK = Color.WHITE;
    static final Paint OCC = Color.ORANGE;
    SGridPane mainPane = new SGridPane(20);
    SimpleIntegerProperty map[][];
    Maze maze;

    public MazePane(int mazeWidth, int mazeHeight) {
        maze = new Maze(mazeWidth, mazeHeight);
        map = maze.getMap();
        getChildren().add(mainPane);
    }

    public void setMap(int[][] map) {
        maze.setMap(map);
        paintMaze();
    }

    public SimpleIntegerProperty[][] getMap() {
        return map;
    }

    private void paintMaze() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                MazeElement element = new MazeElement(map[i][j].getValue());
                element.typeProperty().bind(map[i][j]);
                mainPane.add(element, i, j);
            }
        }
    }

    private Paint getColor(int x) {
        if (x == Maze.WALL)
            return WALL;
        else if (x == Maze.BLANK)
            return BLANK;
        else
            return OCC;
    }
}
