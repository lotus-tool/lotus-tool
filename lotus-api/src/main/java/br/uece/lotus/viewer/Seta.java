package br.uece.lotus.viewer;

import javafx.geometry.Bounds;
import javafx.scene.shape.Polygon;
import static java.lang.Math.abs;

/**
 * Created by emerson on 03/03/15.
 */
public class Seta extends Polygon {

    public Seta() {
        super(-5.0, -5.0, 5.0, 0.0, -5.0, 5.0);
    }

    public boolean isInsideBounds(double x, double y) {
        //return getLocalToSceneTransform().transform(getLayoutBounds()).contains(point);
        Bounds aux = getLocalToSceneTransform().transform(getLayoutBounds());
        double x0 = aux.getMinX() + 5;
        double y0 = aux.getMinY() + 5;
        System.out.printf("(%f %f) (%f %f)\n", x0, y0, x, y);
        return abs(x0 - x) <= 8 && abs(x0 - y) <= 8;
    }

}
