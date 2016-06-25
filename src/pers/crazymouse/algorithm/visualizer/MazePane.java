package pers.crazymouse.algorithm.visualizer;

import javafx.animation.FadeTransition;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import pers.crazymouse.algorithm.std.Maze;

/**
 * Created by crazymouse on 6/22/16.
 */
public class MazePane extends StackPane {
    static final Paint WALL = Color.BLACK;
    static final Paint BLANK = Color.TRANSPARENT;
    static final Paint OCC = Color.GREY;
    static final Paint DIREC = Color.ORANGE;
    static final Paint BEST = Color.RED;
    static final Paint VISIT = Color.RED;
    private double boxSize;

    private int mazeWidth, mazeHeight;
    private SimpleBooleanProperty hasBestPath = new SimpleBooleanProperty(false);
    SGridPane mainPane;
    SGridPane bestPathPane;
    SGridPane pathPane;
    SimpleIntegerProperty map[][];
    SimpleIntegerProperty movedX = new SimpleIntegerProperty(0);
    SimpleIntegerProperty movedY = new SimpleIntegerProperty(0);
    Maze maze;

    public MazePane(int[][] map) {
        maze = new Maze(map);
        this.map = maze.getMap();
        mazeHeight = map.length;
        mazeWidth = map[0].length;
        boxSize = Math.floor(Math.min(900 / mazeHeight, 1440 / mazeWidth)) - 1;
        mainPane = new SGridPane(boxSize);
        bestPathPane = new SGridPane(boxSize);
        paintMaze();
        initPathPane();
        getChildren().addAll(mainPane);
//        main.getTurnList().addListener(new InvalidationListener() {
//            ObservableList<Integer> list = main.getTurnList();
//            Stack<MazeElement> pathList = new Stack<>();
//            int size = main.getTurnList().size();
//            MazeElement e;
//
//            @Override
//            public void invalidated(Observable observable) {
//                System.out.println(pathList.size());
//                if (list.size() > size) {
//                    e = new MazeElement(Maze.DIREC);
//                    pathList.add(e);
//                    pathPane.add(e, main.getX(), main.getY());
//                    e.setRotate(main.getTurnList().get(main.getTurnList().size() - 1) * 90);
//                } else {
//                    pathPane.getChildren().remove(pathList.pop());
//                }
//                size = list.size();
//            }
//        });
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
//        getChildren().clear();
        maze.setMap(map);
        this.map = maze.getMap();
        paintMaze();
        initPathPane();
//        getChildren().addAll(mainPane, pathPane);
    }

    public SimpleIntegerProperty[][] getMap() {
        return map;
    }

    private void paintMaze() {
        mainPane.getChildren().clear();

        for (int i = 0; i < mazeHeight; i++) {
            for (int j = 0; j < mazeWidth; j++) {
                MazeElement element = new MazeElement(i, j, map[i][j].getValue());
                FadeTransition ft = new FadeTransition(Duration.millis(1000), element);
                ft.setFromValue(1.0);
                ft.setToValue(0.5);
                ft.setCycleCount(2);
                ft.setAutoReverse(true);
                element.typeProperty().bind(map[i][j]);
//                element.fillProperty().bind(map[i][j]);
                // TODO unfinished
                element.setOnMouseEntered(event -> {
                    movedX.setValue(element.getX());
                    movedY.setValue(element.getY());
                    ft.play();
                    ft.jumpTo(Duration.millis(1000));
                    ft.pause();
                });
                element.setOnMouseExited(event -> ft.play());
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

    public void generation() {
        maze.generation();
    }

    public void search() {
        maze.searchPath();
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

    public double getBoxSize() {
        return boxSize;
    }
}
