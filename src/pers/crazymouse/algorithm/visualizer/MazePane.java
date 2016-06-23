package pers.crazymouse.algorithm.visualizer;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import pers.crazymouse.algorithm.std.Maze;

import java.util.Stack;

/**
 * Created by crazymouse on 6/22/16.
 */
public class MazePane extends StackPane {
    static final Paint WALL = Color.BLACK;
    static final Paint BLANK = Color.TRANSPARENT;
    static final Paint OCC = Color.GREY;
    static final Paint DIREC = Color.RED;
    static final double boxSize = 30;

    private int mazeWidth, mazeHeight;
    SGridPane mainPane = new SGridPane(boxSize);
    SGridPane pathPane;
    Text xyText = new Text();
    SimpleIntegerProperty map[][];
    Maze maze;

    public MazePane(int[][] map) {
        maze = new Maze(map);
        this.map = maze.getMap();
        paintMaze();
        initPathPane();
        getChildren().addAll(mainPane, pathPane, xyText);
        maze.getTurnList().addListener(new InvalidationListener() {
            ObservableList<Integer> list = maze.getTurnList();
            Stack<MazeElement> pathList = new Stack<>();
            int size = maze.getTurnList().size();
            MazeElement e;

            @Override
            public void invalidated(Observable observable) {
                System.out.println(pathList.size());
                if (list.size() > size) {
                    e = new MazeElement(Maze.DIREC);
                    pathList.add(e);
                    pathPane.add(e, maze.getX(), maze.getY());
                    e.setRotate(maze.getTurnList().get(maze.getTurnList().size() - 1) * 90);
                } else {
                    pathPane.getChildren().remove(pathList.pop());
                }
                size = list.size();
            }
        });
        maze.getPathList().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                System.out.println("!!!");
//                initPathPane();
//                getChildren().add(pathPane);
            }
        });
    }

    private void initPathPane() {
        pathPane = new SGridPane(boxSize);
        for (int i = 0; i < mazeWidth; i++) {
            for (int j = 0; j < mazeHeight; j++) {
                MazeElement element = new MazeElement(Maze.BLANK);
                element.setOnMouseMoved(event -> {
                    sleep(1);
                    pathPane.setVisible(false);
                });
                element.setOnMouseExited(event -> {
                    sleep(1);
                    pathPane.setVisible(true);
                });
                pathPane.add(element, i, j);
            }
        }
    }

    private void sleep(long time) {
        try {
            Thread.currentThread().sleep(time);
        } catch (InterruptedException ex) {
            return;
        }
    }

    public void setMap(int[][] map) {
        getChildren().clear();
        maze.setMap(map);
        paintMaze();
        initPathPane();
        getChildren().addAll(mainPane, pathPane);
    }

    public SimpleIntegerProperty[][] getMap() {
        return map;
    }

    private void paintMaze() {
        mazeWidth = map.length;
        mazeHeight = map[0].length;
        for (int i = 0; i < mazeWidth; i++) {
            for (int j = 0; j < mazeHeight; j++) {
                MazeElement element = new MazeElement(map[i][j].getValue());
                element.typeProperty().bind(map[i][j]);
                // TODO unfinished
                element.setOnMouseMoved(event -> {
                    xyText.setText("wow");
                });
                element.setOnMouseExited(event -> {
                    xyText.setText(":)");
                });
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
