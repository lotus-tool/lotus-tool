/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.gamut.app.editor;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Region;

/**
 *
 * @author emerson
 */
public class MapEditor extends Region {

    private List<Property> mPropriedades;
    
    private MapEditor(List<Property> p) {
        mPropriedades = p;        
    }

    public interface Converter {
        String objectToString(Object o);
        Object stringToObject(String s);
    }    
    
    static class Property {
        String key;
        Object defaultValue;
        Converter converter;
    }
    
    public static class Builder {
        private List<Property> mPropertys = new ArrayList<>();
        public Builder newProperty(String key, Object defaultValue, Converter converter) {
            Property p = new Property();
            p.key = key;
            p.defaultValue = defaultValue;
            p.converter = converter;
            return this;
        }
        public MapEditor build() {
            return new MapEditor(mPropertys);
        }
    } 
    
}
