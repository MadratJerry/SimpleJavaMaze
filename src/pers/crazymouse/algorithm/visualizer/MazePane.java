package pers.crazymouse.algorithm.visualizer;

import javafx.animation.FadeTransition;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import pers.crazymouse.algorithm.std.Maze;

import java.util.Stack;

/**
 * Created by crazymouse on 6/22/16.
 */
public class MazePane extends StackPane {
    static final Paint WALL = Color.BLACK;
    static final Paint BLANK = Color.TRANSPARENT;
    static final Paint OCC = Color.GREY;
    static final Paint DIREC = Color.ORANGE;
    static final Paint BEST = Color.RED;
    static final double boxSize = 50;

    private int mazeWidth, mazeHeight;
    private SimpleBooleanProperty hasBestPath = new SimpleBooleanProperty(false);
    SGridPane mainPane = new SGridPane(boxSize);
    SGridPane bestPathPane = new SGridPane(boxSize);
    SGridPane pathPane;
    SimpleIntegerProperty map[][];
    SimpleIntegerProperty movedX = new SimpleIntegerProperty(0);
    SimpleIntegerProperty movedY = new SimpleIntegerProperty(0);
    Maze maze;

    public MazePane(int[][] map) {
        maze = new Maze(map);
        this.map = maze.getMap();
        paintMaze();
        initPathPane();
        getChildren().addAll(mainPane, pathPane, bestPathPane);
        getChildren().removeAll(bestPathPane);
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
                hasBestPath.setValue(true);
            }
        });
    }

    private void initPathPane() {
        pathPane = new SGridPane(boxSize);
        for (int i = 0; i < mazeHeight; i++) {
            for (int j = 0; j < mazeWidth; j++) {
                MazeElement element = new MazeElement(i, j, Maze.BLANK);
                element.setOnMouseEntered(event -> {
                    sleep(5);
                    pathPane.setMouseTransparent(true);
                    movedX.setValue(element.getX());
                    movedY.setValue(element.getY());
                });
                element.setOnMouseExited(event -> {
                    sleep(5);
                    pathPane.setMouseTransparent(false);
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
        mazeHeight = map.length;
        mazeWidth = map[0].length;

        for (int i = 0; i < mazeHeight; i++) {
            for (int j = 0; j < mazeWidth; j++) {
                MazeElement element = new MazeElement(i, j, map[i][j].getValue());
                FadeTransition ft = new FadeTransition(Duration.millis(1000), element);
                ft.setFromValue(1.0);
                ft.setToValue(0.5);
                ft.setCycleCount(2);
                ft.setAutoReverse(true);
                element.typeProperty().bind(map[i][j]);
                // TODO unfinished
                element.setOnMouseEntered(event -> {
                    movedX.setValue(element.getX());
                    movedY.setValue(element.getY());
                    ft.play();
                    ft.jumpTo(Duration.millis(1000));
                });
                element.setOnMouseExited(event -> {
                });
                mainPane.add(element, i, j);
            }
        }
    }

    public int getMovedX() {
        return movedX.get();
    }

    public SimpleIntegerProperty movedXProperty() {
        return movedX;
    }

    public int getMovedY() {
        return movedY.get();
    }

    public SimpleIntegerProperty movedYProperty() {
        return movedY;
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

    public SimpleBooleanProperty getHasBestPath() {
        return hasBestPath;
    }

    private Paint getColor(int x) {
        if (x == Maze.WALL)
            return WALL;
        else if (x == Maze.BLANK)
            return BLANK;
        else
            return OCC;
    }

    public static double getBoxSize() {
        return boxSize;
    }
}
