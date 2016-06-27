package pers.crazymouse.algorithm.visualizer;

import javafx.animation.FillTransition;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import pers.crazymouse.algorithm.std.Maze;

import java.util.Random;
import java.util.Stack;

/**
 * Created by crazymouse on 6/22/16.
 */
public class MazePane extends StackPane {
    static final Paint WALL = Color.BLACK;
    static final Paint BLANK = Color.TRANSPARENT;
    static final Paint OCC = Color.GREY;
    static final Paint DIREC = Color.GREEN;
    static final Paint BEST = Color.RED;
    static final Paint VISIT = Color.RED;
    static final Paint BEGIN = Color.RED;
    static final Paint END = Color.YELLOW;
    private double boxSize;

    private int beginX, beginY, endX, endY;
    private int mazeWidth, mazeHeight;
    private int bestPathLength = Integer.MAX_VALUE;
    private SimpleBooleanProperty hasBestPath = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty running = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty stop = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty animation = new SimpleBooleanProperty(true);
    SGridPane mainPane;
    SGridPane bestPathPane;
    SGridPane pathPane;
    SimpleIntegerProperty map[][];
    MazeElement pathMap[][];
    MazeElement mazeMap[][];
    FillTransition ftMap[][];
    SimpleIntegerProperty movedX = new SimpleIntegerProperty(0);
    SimpleIntegerProperty movedY = new SimpleIntegerProperty(0);
    Maze maze;

    public MazePane(int[][] map) {
        maze = new Maze(map);
        this.map = maze.getMap();
        mazeHeight = map.length;
        mazeWidth = map[0].length;
        boxSize = (Math.min(900 / mazeHeight, 1440 / mazeWidth)) - 1.5;
        mainPane = new SGridPane(boxSize);
        bestPathPane = new SGridPane(boxSize);
        paintMaze();
        initPathPane();
        getChildren().addAll(mainPane, pathPane);
        setBegin(1, 1);
        setEnd(mazeHeight - 2, mazeWidth - 2);

        maze.getPathList().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if (maze.getBestPathLength() < bestPathLength && hasBestPath.getValue() == false) {
                    bestPathLength = maze.getBestPathLength();
                    hasBestPath.setValue(true);
                }
            }
        });
    }

    private void initPathPane() {
        pathMap = new MazeElement[mazeWidth][mazeHeight];
        pathPane = new SGridPane(boxSize);
        for (int i = 0; i < mazeHeight; i++) {
            for (int j = 0; j < mazeWidth; j++) {
                MazeElement element = new MazeElement(i, j, Maze.BLANK);
                pathMap[i][j] = element;
                element.setOnMouseEntered(event -> {
                    pathPane.setMouseTransparent(true);
                    movedX.setValue(element.getX());
                    movedY.setValue(element.getY());
                });
                element.setOnMouseExited(event -> pathPane.setMouseTransparent(false));
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

    public SimpleIntegerProperty[][] getMap() {
        return map;
    }

    int X, Y;

    private void paintMaze() {
        mainPane.getChildren().clear();
        mazeMap = new MazeElement[mazeHeight][mazeWidth];
        ftMap = new FillTransition[mazeHeight][mazeWidth];
        for (int i = 0; i < mazeHeight; i++) {
            for (int j = 0; j < mazeWidth; j++) {
                MazeElement element = new MazeElement(i, j, map[i][j].getValue());
                mazeMap[i][j] = element;
                FillTransition ft = new FillTransition(Duration.millis(1000), element);
                ftMap[i][j] = ft;
                element.typeProperty().bind(map[i][j]);
                X = i;
                Y = j;
                map[i][j].addListener(new InvalidationListener() {
                    int x = X;
                    int y = Y;

                    @Override
                    public void invalidated(Observable observable) {
                        setAnimation(mazeMap[x][y]);
                    }
                });

                ft.setCycleCount(2);
                ft.setAutoReverse(true);
                element.setOnMouseEntered(event -> {
                    movedX.setValue(element.getX());
                    movedY.setValue(element.getY());
                    ft.play();
                    ft.jumpTo(Duration.millis(1000));
                    ft.pause();
                });
                element.setOnMouseExited(event -> ft.play());

                setAnimation(element);
                mainPane.add(element, i, j);
            }
        }
        mainPane.setOnMousePressed(event -> {
            if (running.getValue() == false) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    cleanPathMap();
                    beginX = movedX.getValue();
                    beginY = movedY.getValue();
                }
                if (event.getButton() == MouseButton.SECONDARY) {
                    cleanPathMap();
                    endX = movedX.getValue();
                    endY = movedY.getValue();
                }
                setBegin(beginX, beginY);
                setEnd(endX, endY);
            }
        });
    }

    private void cleanPathMap() {
        for (int i = 0; i < mazeHeight; i++) {
            for (int j = 0; j < mazeWidth; j++) {
                pathMap[i][j].setType(Maze.BLANK);
            }
        }
    }

    private void setAnimation(MazeElement element) {
        FillTransition ft = ftMap[element.getX()][element.getY()];
        if (element.getType() == Maze.WALL) {
            ft.setFromValue((Color) MazePane.WALL);
            ft.setToValue(Color.DARKGRAY);
        }
        if (element.getType() == Maze.BLANK) {
            ft.setFromValue((Color) MazePane.BLANK);
            ft.setToValue(Color.LIGHTGREEN);
        }
        if (element.getType() == Maze.OCC) {
            ft.setFromValue((Color) MazePane.OCC);
            ft.setToValue(Color.LIGHTGOLDENRODYELLOW);
        }
        if (element.getType() == Maze.VISIT) {
            ft.setFromValue((Color) MazePane.VISIT);
            ft.setToValue(Color.LIGHTPINK);
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

    public SimpleBooleanProperty animationProperty() {
        return animation;
    }

    public void setBegin(int x, int y) {
        beginX = x;
        beginY = y;
        maze.setBegin(beginX, beginY);
        pathMap[beginX][beginY].setType(-2);
        bestPathLength = Integer.MAX_VALUE;
    }

    public void setEnd(int x, int y) {
        endX = x;
        endY = y;
        maze.setEnd(endX, endY);
        pathMap[endX][endY].setType(-3);
        bestPathLength = Integer.MAX_VALUE;
    }

    public boolean singleStep() {
        return maze.searchStep();
    }

    public void generation() {
        cleanPathMap();
        hasBestPath.setValue(false);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int x = new Random(System.currentTimeMillis()).nextInt(mazeHeight - 4);
                int y = new Random(System.currentTimeMillis()).nextInt(mazeWidth - 4);
                maze.setBegin(x + x % 2 + 1, y + x % 2 + 1);
                running.setValue(true);
                for (int j = 0; j < mazeWidth; j++) {
                    for (int i = 0; i < mazeHeight; i++) {
                        maze.getMap()[i][j].setValue(Maze.WALL);
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                while (maze.generationStep()) {
                    if (stop.getValue() == true) {
                        stop.setValue(false);
                        break;
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                setBegin(1, 1);
                setEnd(mazeHeight - 2, mazeWidth - 2);
                running.setValue(false);
            }
        });
        thread.setPriority(10);
        if (animationProperty().getValue()) {
            thread.start();
        } else {
            running.setValue(true);
            maze.generation();
            setBegin(1, 1);
            setEnd(mazeHeight - 2, mazeWidth - 2);
            running.setValue(false);
        }
    }

    public void search() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                running.setValue(true);
//                setBegin(beginX, beginY);
//                setEnd(endX, endY);
//                maze.getTurnList().clear();
                try {
                    while (maze.searchStep()) {
                        Thread.sleep(1);
                        if (stop.getValue() == true) {
                            stop.setValue(false);
                            break;
                        }
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
//                setBegin(beginX, beginY);
//                setEnd(endX, endY);
                running.setValue(false);
            }
        });
        thread.setPriority(10);
        if (animationProperty().getValue()) {
            thread.start();
        } else {
            running.setValue(true);
            while (maze.searchStep()) ;
            running.setValue(false);
        }
    }

    public void showBestPath() {
        showPath(maze.getBestTurnList());
        hasBestPath.setValue(false);
    }

    public void showPath(Stack<Integer> bestTurnStack) {
        pathPane.setVisible(true);
        for (int i = 0; i < mazeHeight; i++) {
            for (int j = 0; j < mazeWidth; j++) {
                if (pathMap[i][j].getType() == Maze.DIREC)
                    pathMap[i][j].setType(Maze.BLANK);
            }
        }
        int x = beginX;
        int y = beginY;
        for (int i = 0; i < bestTurnStack.size() - 1; i++) {
            int t = bestTurnStack.get(i);
            t = (t + 2) % 4;
            x = x + (t - 1) % 2;
            y = y + (t - 2) % 2;
            if (pathMap[x][y].getType() != Maze.DIREC)
                pathMap[x][y].setType(Maze.DIREC);
            pathMap[x][y].setRotate(bestTurnStack.get(i) * 90);
        }
    }

    public SimpleBooleanProperty hasBestPathProperty() {
        return hasBestPath;
    }

    public SimpleBooleanProperty runningProperty() {
        return running;
    }

    public void setStop(boolean stop) {
        this.stop.set(stop);
    }

    public double getBoxSize() {
        return boxSize;
    }
}
