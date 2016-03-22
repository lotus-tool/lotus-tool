/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.viewer.hMSC;

import br.uece.lotus.uml.api.ds.Hmsc;
import javafx.geometry.Point2D;
import javafx.scene.Node;

/**
 *
 * @author Bruno Barbosa
 */
public interface HmscView {
    
    interface Factory{
        HmscView create();
    }
    
    Node getNode();
    boolean isInsideBounds(Point2D point);
    Hmsc getHMSC();
    void setHMSC(Hmsc bbds);
}
