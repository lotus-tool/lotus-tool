package br.uece.gamut.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProjectModel {
    
    private String mName;
    private ObservableList<ComponentModel> components = FXCollections.observableArrayList();

    public ComponentModel newComponent(String name) {
        ComponentModel c = new ComponentModel();
        c.setName(name);
        components.add(c);
        return c;
    }

    public ObservableList<ComponentModel> getComponents() {
        return this.components;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }
}
