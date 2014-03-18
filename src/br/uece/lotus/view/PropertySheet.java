package br.uece.lotus.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PropertySheet {
    
    private ObservableList<PropertySpec> properties = FXCollections.observableArrayList();
        
    public void newProperty(String name, String key) {
        PropertySpec aux = new PropertySpec(name, key);
        properties.add(aux);
    }

    public ObservableList<PropertySpec> propertiesProperty() {
        return properties;
    }
    
}
