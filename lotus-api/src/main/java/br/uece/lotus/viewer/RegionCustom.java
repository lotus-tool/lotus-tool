package br.uece.lotus.viewer;

import javafx.beans.property.DoubleProperty;
import javafx.scene.layout.Region;

public abstract class RegionCustom extends Region {

    public abstract DoubleProperty layoutXPropertyCustom();

    public abstract DoubleProperty layoutYPropertyCustom();


}
