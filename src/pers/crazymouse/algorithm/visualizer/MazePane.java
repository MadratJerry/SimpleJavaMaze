package pers.crazymouse.algorithm.visualizer;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import pers.crazymouse.algorithm.std.Maze;

/**
 * Created by crazymouse on 6/22/16.
 */
public class MazePane extends StackPane {
    static final Paint WALL = Color.BLACK;
    static final Paint BLANK = Color.TRANSPARENT;
    static final Paint OCC = Color.GREY;
    static final Paint DIREC = Color.RED;
    static final double boxSize = 20;

    private int mazeWidth, mazeHeight;
    SGridPane mainPane = new SGridPane(boxSize);
    SGridPane pathPane;
    SimpleIntegerProperty map[][];
    Maze maze;

    public MazePane(int mazeWidth, int mazeHeight) {
        this.mazeWidth = mazeWidth;
        this.mazeHeight = mazeHeight;
        maze = new Maze(mazeWidth, mazeHeight);
        map = maze.getMap();
        initPathPane();
        getChildren().addAll(mainPane, pathPane);
        maze.getTurnList().addListener(new InvalidationListener() {
            ObservableList<Integer> list = maze.getTurnList();
            int size = maze.getTurnList().size();
            MazeElement e;

            @Override
            public void invalidated(Observable observable) {
                if (list.size() > size) {
                    e = new MazeElement(Maze.DIREC);
                    pathPane.add(e, maze.getX(), maze.getY());
                    e.setRotate(maze.getTurnList().get(maze.getTurnList().size() - 1) * 90);
                } else {
                    e.setType(Maze.BLANK);
                }
                size = list.size();
            }
        });
        maze.getPathList().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                initPathPane();
                getChildren().add(pathPane);
            }
        });
    }

    private void initPathPane() {
        pathPane = new SGridPane(boxSize);
        for (int i = 0; i < mazeWidth; i++) {
            for (int j = 0; j < mazeHeight; j++) {
                MazeElement element = new MazeElement(Maze.BLANK);
                pathPane.add(element, i, j);
            }
        }
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

    public void setBegin(int x, int y) {
        maze.setBegin(x, y);
    }

    public void setEnd(int x, int y) {
        maze.setEnd(x, y);
    }

    public boolean singleStep() {
        return maze.singleStep();
    }

    public void search() {
        maze.searchPath();
        // TODO display animation
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
