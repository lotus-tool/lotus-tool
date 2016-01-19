/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.viewer.builder;

import br.uece.lotus.uml.api.ds.BlockBuildDS;
import javafx.geometry.Point2D;
import javafx.scene.Node;

/**
 *
 * @author Bruno Barbosa
 */
public interface BlockBuildDSView {
    
    interface Factory{
        BlockBuildDSView create();
    }
    
    Node getNode();
    boolean isInsideBounds(Point2D point);
    BlockBuildDS getBlockBuildDS();
    void setBlockBuildDS(BlockBuildDS bbds);
}
