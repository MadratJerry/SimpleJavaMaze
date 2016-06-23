package pers.crazymouse.algorithm.visualizer;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Paint;
import pers.crazymouse.algorithm.std.Maze;

/**
 * Created by crazymouse on 6/22/16.
 */
public class MazeElement extends ResizablePolygon {
    private double[] squarePoints = new double[]{0.0, 0.0, 10.0, 0.0, 10.0, 10.0, 0.0, 10.0};
    private double[] trianglePoints = new double[]{0.0, 0.0, Math.sqrt(3), 1.0, 0.0, 2.0};

    private int x;
    private int y;

    Paint fill;
    SimpleIntegerProperty type = new SimpleIntegerProperty();

    public MazeElement(int index) {
        this(0, 0, index);
    }

    public MazeElement(int x, int y, int index) {
        setX(x);
        setY(y);
        type.addListener(new InvalidationListener() {
            @Override
            // If the value is 0, it doesn't work.
            public void invalidated(Observable observable) {
                convert(type.getValue());
            }
        });
        setType(index);
    }

    private void convert(int index) {
        getPoints().clear();
        if (index == Maze.WALL) {
            addPoints(squarePoints);
            setFill(MazePane.WALL);
        }
        if (index == Maze.BLANK) {
            addPoints(squarePoints);
            setFill(MazePane.BLANK);
        }
        if (index == Maze.OCC) {
            addPoints(squarePoints);
            setFill(MazePane.OCC);
        }
        if (index == Maze.DIREC) {
            addPoints(trianglePoints);
            setFill(MazePane.DIREC);
        }
        resize();
    }

    private void addPoints(double[] points) {
        for (double i : points)
            getPoints().add(i);
    }

    public void setType(int index) {
        type.setValue(index);
    }

    public int getType() {
        return type.getValue();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public SimpleIntegerProperty typeProperty() {
        return type;
    }
}
