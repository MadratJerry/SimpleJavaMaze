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
    public static int WALL = Maze.WALL;
    public static int BLANK = Maze.BLANK;
    private double[] squarePoints = new double[]{0.0, 0.0, 10.0, 0.0, 10.0, 10.0, 0.0, 10.0};
    private double[] triangle = new double[]{0.0, 0.0, Math.sqrt(3), 1.0, 0.0, 2.0};

    Paint fill;
    SimpleIntegerProperty type = new SimpleIntegerProperty();

    public MazeElement(int index) {
        type.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                convert(type.getValue());
            }
        });
        setType(index);
    }

    private void convert(int index) {
        getPoints().clear();
        if (index == WALL) {
            for (double i : squarePoints)
                getPoints().add(i);
            setFill(MazePane.WALL);
        }
        if (index == BLANK) {
            for (double i : squarePoints)
                getPoints().add(i);
            setFill(MazePane.BLANK);
        }
    }

    public void setType(int index) {
        type.setValue(index);
    }

    public int getType() {
        return type.getValue();
    }

    public SimpleIntegerProperty typeProperty() {
        return type;
    }
}
