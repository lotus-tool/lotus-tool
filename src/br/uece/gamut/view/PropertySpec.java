package br.uece.gamut.view;

import br.uece.gamut.model.Model;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

public class PropertySpec {

    private StringProperty mName = new SimpleStringProperty();
    private StringProperty mValue = new SimpleStringProperty();    
    private final String mKey;

    PropertySpec(String name, String key) {
        mName.set(name);
        mKey = key;
    }

    public ObservableValue<String> nameProperty() {
        return mName;
    }
    
    public ObservableValue<String> valueProperty() {
        return mValue;
    }

    public ObservableValue<String> getValue(Model c) {
        mValue.set(c.getValue(mKey));
        return mValue;
    }

    public void setValue(Model c, String newValue) {
        c.setValue(mKey, newValue);
        mValue.set(newValue);
    }
}
