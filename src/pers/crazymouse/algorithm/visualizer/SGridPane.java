package pers.crazymouse.algorithm.visualizer;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;

/**
 * Created by crazymouse on 6/19/16.
 */

/**
 * SGridPane will automatically convert the children which is not Square
 * to Square with the settings.
 */
public class SGridPane extends GridPane {
    private double length;

    /**
     * Creates an Square GridPane, set the length to 5 by default.
     */
    public SGridPane() {
        this(5);
    }

    /**
     * Creates a new instance of SGridPane with the given length.
     *
     * @param length length of the square
     */
    public SGridPane(double length) {
        setLength(length);
        getChildren().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if (!(getChildren().get(getChildren().size() - 1) instanceof Resizable)) {
                    getChildren().set(getChildren().size() - 1, new Square(getLength(), Color.BLACK));
                } else {
                    getChildren().get(getChildren().size() - 1).resize(getLength(), getLength());
                }
            }
        });
    }

    public void fillAll(Paint fill) {
        for (Node i : getChildren()) {
            ((Shape) i).setFill(fill);
        }
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }
}
