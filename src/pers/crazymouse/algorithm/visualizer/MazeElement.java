package pers.crazymouse.algorithm.visualizer;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleIntegerProperty;
import pers.crazymouse.algorithm.std.Maze;

/**
 * Created by crazymouse on 6/22/16.
 */
public class MazeElement extends ResizablePolygon {
    private double[] squarePoints = new double[]{0.0, 0.0, 10.0, 0.0, 10.0, 10.0, 0.0, 10.0};
    private double[] trianglePoints = new double[]{0.0, 0.0, Math.sqrt(3), 1.0, 0.0, 2.0};
    private double[] arrowPoints = new double[]{0.0, 0.0, 10.0, 5.0, 0.0, 10.0, 3.0, 5.0};

    private int x;
    private int y;

    int lastType = Integer.MIN_VALUE;
    SimpleIntegerProperty type = new SimpleIntegerProperty(Integer.MIN_VALUE);

    public MazeElement(int index) {
        this(0, 0, index);
    }

    public MazeElement(int x, int y, int index) {
        setX(x);
        setY(y);
        type.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                convert(type.getValue());
            }
        });
        setType(index);
    }

    private void convert(int index) {
        if (index == lastType)
            return;
        if ((lastType >= 0 && index < 0) || (lastType < 0 && index >= 0) || lastType == Integer.MIN_VALUE) {
            getPoints().clear();
            if (index >= 0) {
                for (double i : squarePoints)
                    getPoints().add(i);
            } else {
                for (double i : arrowPoints)
                    getPoints().add(i);
            }
            resize();
        }
        if (index == Maze.WALL) {
            setFill(MazePane.WALL);
        }
        if (index == Maze.BLANK) {
            setFill(MazePane.BLANK);
        }
        if (index == Maze.OCC) {
            setFill(MazePane.OCC);
        }
        if (index == Maze.DIREC) {
            setFill(MazePane.DIREC);
        }
        if (index == Maze.VISIT) {
            setFill(MazePane.VISIT);
        }
        lastType = type.getValue();
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
