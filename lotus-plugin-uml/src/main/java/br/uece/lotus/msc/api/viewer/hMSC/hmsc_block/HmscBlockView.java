/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.api.viewer.hMSC.hmsc_block;

import br.uece.lotus.msc.api.model.msc.hmsc.HmscBlock;
import br.uece.lotus.msc.api.viewer.hMSC.GenericElementView;
import javafx.geometry.Point2D;
import javafx.scene.Node;

/**
 *
 * @author Bruno Barbosa
 */
public interface HmscBlockView extends GenericElementView {
    
    interface Factory{
        HmscBlockView create();
    }
    
    Node getNode();
    boolean isInsideBounds(Point2D point);
    HmscBlock getHMSC();
    void setHMSC(HmscBlock bbds);
}
