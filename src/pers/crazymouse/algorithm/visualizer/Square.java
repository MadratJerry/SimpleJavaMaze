package pers.crazymouse.algorithm.visualizer;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * Created by crazymouse on 6/19/16.
 */

/**
 * The {@code Square} class defines a square
 * with the specified size and location.
 */
public class Square extends Rectangle {
    /**
     * Creates an empty instance of Square.
     */
    public Square() {
        this(0);
    }

    /**
     * Creates a new instance of Square with the given size.
     *
     * @param length length of the square
     */
    public Square(double length) {
        this(length, Color.BLACK);
    }

    /**
     * Creates a new instance of Square with the given size.
     *
     * @param color length of the square
     */
    public Square(Paint color) {
        this(0, color);
    }

    /**
     * Creates a new instance of Square with the given size and fill.
     *
     * @param length length of the square
     * @param fill   determines how to fill the interior of the square
     */
    public Square(double length, Paint fill) {
        widthProperty().addListener(observable -> setHeight(widthProperty().getValue()));
        heightProperty().addListener(observable -> setWidth(heightProperty().getValue()));
        setLength(length);
        setFill(fill);
    }

    /**
     * Creates a new instance of Square with the given position and size.
     *
     * @param x      horizontal position of the square
     * @param y      vertical position of the square
     * @param length width of the square
     */
    public Square(double x, double y, double length) {
        this(length);
        setX(x);
        setY(y);
    }

    public final void setLength(double length) {
        setWidth(length);
    }
}
