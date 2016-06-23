package pers.crazymouse.algorithm.visualizer;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleDoubleProperty;
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
    private SimpleDoubleProperty length = new SimpleDoubleProperty();

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
                if (getChildren().size() == 0)
                    return;
                if (!(getChildren().get(getChildren().size() - 1) instanceof Resizable)) {
                    getChildren().set(getChildren().size() - 1, new Square(getLength(), Color.BLACK));
                } else {
                    ((Resizable) getChildren().get(getChildren().size() - 1)).
                            getResizableHeight().bind(getLengthProperty());
                    ((Resizable) getChildren().get(getChildren().size() - 1)).
                            getResizableWidth().bind(getLengthProperty());
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
        return length.getValue();
    }

    public void setLength(double length) {
        this.length.setValue(length);
    }

    public SimpleDoubleProperty getLengthProperty() {
        return length;
    }
}
