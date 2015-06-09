package br.uece.lotus.runner;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by emerson on 09/06/15.
 */
public class Symbol {

    private StringProperty name = new SimpleStringProperty();
    private ObjectProperty value = new SimpleObjectProperty<>();

    public StringProperty nameProperty() {
        return name;
    }

    public ObjectProperty valueProperty() {
        return value;
    }
}
