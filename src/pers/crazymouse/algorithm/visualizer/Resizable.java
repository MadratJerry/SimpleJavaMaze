package pers.crazymouse.algorithm.visualizer;

import javafx.beans.property.DoubleProperty;

/**
 * Created by crazymouse on 6/21/16.
 */
public interface Resizable {

    void resizeWithRate(double rate);

    boolean isResizable();

    void resize(double width, double height);

    DoubleProperty getResizableWidth();

    DoubleProperty getResizableHeight();
}
